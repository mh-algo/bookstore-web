function toggleMenu(menuId) {
    const menu = document.getElementById(menuId);
    if (menu) {
        menu.classList.toggle('active');
    }
}