package com.CanTinGo.dev.config;

import com.CanTinGo.dev.daos.userDaos;
import com.CanTinGo.dev.models.userModels;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.util.Collection;
import java.util.List;

@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final userDaos userDao;

    public SecurityConfig(userDaos userDao) {
        this.userDao = userDao;
    }
    
    // function filterChain -> Configure the security flow – define who has access to which paths.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/client/**").hasRole("USER")
                    .anyRequest().permitAll()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successHandler((request, response, authentication) -> {
                        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                        String redirectUrl = "";
                        for (GrantedAuthority authority : authorities) {
                        	String role = authority.getAuthority();
                        	if ("ROLE_ADMIN".equals(role)) {
                        		redirectUrl = "/admin/";
                        		// if not find role 
                        		break;
                        	} else if ("ROLE_USER".equals(role)) {
                        		redirectUrl = "/client/home";
                        		break;
                        	}
                        }
                        response.sendRedirect(redirectUrl);
                    })
                    .failureUrl("/login?error")
                    .permitAll()
                )

                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                );

            return http.build();
        } catch (Exception e) {
            System.err.println("Lỗi khi cấu hình SecurityFilterChain: " + e.getMessage());
            e.printStackTrace();
            try {
                return HttpSecurity.class.getDeclaredConstructor().newInstance().build();
            } catch (Exception ex) {
                throw new RuntimeException("Không thể tạo fallback SecurityFilterChain", ex);
            }
        }
    }
    
    // function authManager -> Used to register the authentication mechanism. (em tu note ra nhe thay)
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) {
        try {
            return http.getSharedObject(AuthenticationManagerBuilder.class)
                       .authenticationProvider(customAuthProvider())
                       .build();
        } catch (Exception e) {
            System.err.println("=> Lỗi khi tạo AuthenticationManager: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể khởi tạo AuthenticationManager", e);
        }
    }
    
    // function customAuthProvider -> This function is for authenticating the username and password
    @Bean
    public AuthenticationProvider customAuthProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                try {
                    String username = authentication.getName();
                    String password = authentication.getCredentials().toString();

                    System.out.println("==== LOGIN ATTEMPT ====");
                    System.out.println("Username: " + username);
                    System.out.println("Password: " + password);

                    userModels user = userDao.checkUser(username, password);
                    if (user == null) {
                        throw new BadCredentialsException("Invalid username/password");
                    }
                    String roleName = user.getRole().getRole_name();
                    if (!roleName.startsWith("ROLE_")) {
                        roleName = "ROLE_" + roleName;
                    }
                    List<GrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority(roleName)
                    );
                    CustomUserDetails customUser = new CustomUserDetails(user, authorities);

                    System.out.println("-> Đăng nhập thành công, quyền: " + roleName);
                    return new UsernamePasswordAuthenticationToken(customUser, password, authorities);

                } catch (BadCredentialsException e) {
                    System.err.println("=> Sai tài khoản hoặc mật khẩu!");
                    throw e; 
                } catch (Exception e) {
                    System.err.println("=> Lỗi khi xác thực người dùng: " + e.getMessage());
                    e.printStackTrace();
                    throw new AuthenticationServiceException("=> Lỗi hệ thống khi xác thực", e);
                }
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
