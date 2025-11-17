document.addEventListener("DOMContentLoaded", function() {
          const navLinks = document.querySelectorAll("nav a"); // tất cả link trong navbar
          const loading = document.getElementById("loading");

          navLinks.forEach(link => {
              link.addEventListener("click", function() {
                  loading.style.display = "block";
              });
          });
      });