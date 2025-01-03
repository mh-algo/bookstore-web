function toggleSubCategoryList(element) {
    if (typeof event !== 'undefined') {
        // 현재 이벤트가 걸린 요소와 실제 클릭된 요소가 같은지 확인
        if (event.target !== event.currentTarget) {
            event.stopPropagation();
            return;
        }
    }
    const subCategoryList = element.firstElementChild
    if (subCategoryList.style.display === 'none' || subCategoryList.style.display === '') {
        subCategoryList.style.display = 'block';
    } else {
        subCategoryList.style.display = 'none';
    }
}

function addBookToCart(button) {
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    const quantity = 1;
    const bookId = button.getAttribute("data-book-id");

    fetch(`/books/${bookId}/cart`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({ quantity: quantity })
    })
        .then(response => response.json())
        .then(response => {
            const status = response.status;
            const message = response.message;

            if (status === 200 || status === 403 || status === 500) {
                alert(message);
            } else if (status === 401) {
                if (confirm(message)) {
                    window.location.href = '/login';
                }
            }
        })
        .catch(error => {
            console.error('저장 중 오류 발생:', error);
        });
}