package com.CanTinGo.dev.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class CheckConnectDB {
	@Autowired
	private DataSource dataSource;
	@PostConstruct
	public void checkDatabaseCnn() {
		if (dataSource == null) {
            System.out.println("❌ DataSource is null! Spring chưa inject");
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ Kết nối SQL Server thành công!");
            System.out.println("Database URL: " + conn.getMetaData().getURL());
            System.out.println("Database: " + conn.getCatalog());
            System.out.println("Driver: " + conn.getMetaData().getDriverName());
        } catch (SQLException e) {
            System.out.println("❌ Kết nối SQL Server thất bại!");
            e.printStackTrace();
        }
	}
}
