function updateEmailAddressInput() {
    const select = document.getElementById("addressSelect");
    const input = document.getElementById("emailAddress");

    const text = select.options[select.selectedIndex].text;

    if (text == "직접 입력") {
        input.removeAttribute("readonly")
        input.text = "";
        input.value = "";
    } else {
        input.setAttribute("readonly", true)
        input.text = text;
        input.value = text;
    }
}