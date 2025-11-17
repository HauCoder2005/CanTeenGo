document.addEventListener("DOMContentLoaded", function() {
    const menuItems = document.querySelectorAll('.sidebar ul li a');
    const mainContent = document.getElementById('main-content');

    menuItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault(); // ngăn default href="#"

            const url = this.getAttribute('data-url'); // lấy data-url
            if (!url) return;

            fetch(url)
                .then(response => response.text())
                .then(html => {
                    mainContent.innerHTML = html;
                })
                .catch(err => {
                    mainContent.innerHTML = "<p>Không load được nội dung.</p>";
                    console.error(err);
                });
        });
    });
});
