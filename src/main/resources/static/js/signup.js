function updateEmailAddressInput() {
    const select = document.getElementById("addressSelect");
    const input = document.getElementById("emailAddress");

    const text = select.options[select.selectedIndex].text;

    if (text === "직접 입력") {
        input.removeAttribute("readonly");
        input.text = "";
        input.value = "";
    } else {
        input.setAttribute("readonly", true);
        input.text = text;
        input.value = text;
    }
}

function checkUsername() {
    const username = document.getElementById("username").value;
    const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
    const csrfToken = $('meta[name="_csrf"]').attr('content');

    fetch("/signup/check-username", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({username: username})
    })
        .then(response => response.json())
        .then(data => {
            const message = document.getElementById("username-check");
            const elementById = document.getElementById("username-valid");

            if (elementById) {
                elementById.style.display="none";
            }

            message.removeAttribute("style");
            const errorMessage = data.error;
            if (errorMessage) {
                message.textContent = errorMessage
                message.style.color = "red";
            } else {
                message.textContent = "사용 가능한 아이디입니다."
                message.style.color = "green";
            }
        })
        .catch(error => console.error("Error: ", error));
}