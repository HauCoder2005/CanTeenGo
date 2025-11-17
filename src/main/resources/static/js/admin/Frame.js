const loadPage = (url) => {
    fetch(url)
        .then(res => res.text())
        .then(html => {
            document.getElementById('contentArea').innerHTML = html;
        })
        .catch(err => console.error(err));
}
