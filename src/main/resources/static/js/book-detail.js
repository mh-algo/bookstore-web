const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

const pathParts = window.location.pathname.split('/');
const bookId = pathParts[2];
const safeBookId = encodeURIComponent(bookId);

function getStock() {
    const stockDisplay = document.getElementById('stock');
    return parseInt(stockDisplay.textContent);
}

function validateInput(quantityInput) {
    const currentValue = parseInt(quantityInput.value);
    if (isNaN(currentValue) || quantityInput.value.trim() === "") {
        return 1;
    }

    return Math.max(1, Math.min(getStock(), currentValue));
}

function decreaseBtnClick() {
    const quantityInput = document.getElementById('quantity');
    quantityInput.value = validateInput(quantityInput) - 1;
    if (quantityInput.value < 1) {
        quantityInput.value = 1;
    }
}

function increaseBtnClick() {
    const quantityInput = document.getElementById('quantity');
    quantityInput.value = validateInput(quantityInput) + 1;
    const stock = getStock();
    if (quantityInput.value > stock) {
        quantityInput.value = stock;
    }
}

function quantityInputEvent(quantity) {
    const currentValue = quantity.value;
    // Allow empty input temporarily for user editing
    if (currentValue.trim() === "") {
        return;
    }

    // Remove non-numeric characters
    const numericValue = currentValue.replace(/[^0-9]/g, '');
    quantity.value = numericValue;

    // Ensure value does not exceed stock
    const stock = getStock();
    if (parseInt(numericValue) > stock) {
        quantity.value = stock;
    }
}

function quantityBlurEvent(quantity) {
    // On blur, reset to valid value if empty
    if (quantity.value.trim() === "") {
        quantity.value = 1;
    }

    // Ensure value does not exceed stock
    const stock = getStock();
    if (parseInt(quantity.value) > stock) {
        quantity.value = stock;
    }
}

function reviewInputEvent(reviewText) {
    const charCount = reviewText.parentElement.querySelector('span');
    const currentLength = reviewText.value.length;
    const maxChars = reviewText.getAttribute('maxlength');

    // 글자 수 제한 초과 방지
    if (currentLength > maxChars) {
        reviewText.value = reviewText.value.substring(0, maxChars);
    }

    // 글자 수 갱신
    charCount.textContent = reviewText.value.length.toString();
}

// Update carousel slide number dynamically
function carouselSlideEvent(event) {
    const activeIndex = event.to;
    const currentSlideElement = document.getElementById('currentSlide');
    currentSlideElement.textContent = (activeIndex + 1).toString().padStart(2, '0');
}

const carousel = document.getElementById('bookCarousel');
carousel.addEventListener('slid.bs.carousel', carouselSlideEvent);

// 리뷰 별점 표시
function calculateRating(star, rating) {
    star.textContent = '☆'.repeat(5 - rating) + '★'.repeat(rating)
}
const ratingElements = document.getElementsByClassName('star');
for (const ratingElement of ratingElements) {
    const ratingValue = parseInt(ratingElement.getAttribute('data-rating'));
    calculateRating(ratingElement, ratingValue)
}

function toggleLikeBtn(button) {
    const reviewId = button.getAttribute('data-review-id');

    fetch(`/books/${safeBookId}/reviews/like`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            'Accept': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({reviewId: reviewId})
    })
        .then(response => response.json())
        .then(response => {
            if (response.status === 200) {
                const like = document.getElementsByClassName('like-count').namedItem(reviewId);
                like.textContent = response.data.likeCount;

                const icon = button.querySelector('i'); // 아이콘 엘리먼트
                if (response.data.liked === true) {
                    button.setAttribute('data-liked', 'true');
                    button.classList.add('liked');
                    icon.classList.remove('far'); // 비활성화 아이콘 클래스 제거
                    icon.classList.add('fas'); // 활성화 아이콘 클래스 추가
                } else {
                    button.setAttribute('data-liked', 'false');
                    button.classList.remove('liked');
                    icon.classList.remove('fas'); // 활성화 아이콘 클래스 제거
                    icon.classList.add('far'); // 비활성화 아이콘 클래스 추가
                }
            } else if (response.status === 401) {
                alert(response.message);
            }
        })
        .catch(error => {
            console.error("Fetch Error:", error);
        });
}

function loadReviews(page) {
    const reviewList = document.getElementById('review-list');
    reviewList.innerHTML = '';

    const safePage = encodeURIComponent(page)

    return fetch(`/books/${safeBookId}/reviews?page=${safePage}`, {
        method: "GET",
        headers: {"Accept": "application/json"}
    })
        .then(response => response.json())
        .then(response => {
            if (response.status === 200) {
                const reviews = response.data.content
                reviews.forEach(review => {
                    const accountId = escapeHTML(review.accountId);
                    const createdDate = escapeHTML(formatDate(review.createdDate));
                    const rating = escapeHTML(review.rating);
                    const likeStatus = escapeHTML(review.likeStatus);
                    const id = escapeHTML(review.id);
                    const likeCount = escapeHTML(review.likeCount);
                    const context = escapeHTML(review.context);

                    let reviewItem = `
                        <li class="list-group-item">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <strong class="me-2">${accountId}</strong>
                                    <span class="text-muted me-2">${createdDate}</span>
                                    <div class="star-rating star" data-rating="${rating}">
                                        ${'☆'.repeat(5 - rating) + '★'.repeat(rating)}
                                    </div>
                                </div>
                                <div>
                                    <button class="btn btn-sm like-btn ${likeStatus ? 'liked' : ''}"
                                    data-review-id="${id}" onclick="toggleLikeBtn(this)">
                                        <i class="fa-thumbs-up ${likeStatus ? 'fas' : 'far'}"></i></button>
                                    <span class="like-count" name="${id}">${likeCount}</span>
                                    `;
                    if (review.reviewOwner) {
                        reviewItem += `
                                    <button class="btn btn-sm btn-danger ms-2" data-review-id="${id}" onclick="deleteReview(this)">삭제</button>
                                    <button class="btn btn-sm btn-primary ms-2" data-review-id="${id}" onclick="editReview(this)">수정</button>
                                    `;
                    }

                    reviewItem += `
                                </div>
                            </div>
                            <p class="mt-2 mb-0">${context}</p>
                        </li>
                        `;

                    reviewList.innerHTML += reviewItem;
                });
                return response.data;
            }
        })
        .catch(error => {
            console.error("Fetch Error:", error);
            throw error;
        });
}

function generatePagination(data) {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    const pageNumber = escapeHTML(data.pageable.pageNumber);
    const totalPages = escapeHTML(data.totalPages);

    pagination.innerHTML += `
                <li class="page-item ${pageNumber === 1 ? 'disabled' : ''}">
                    <a class="page-link" onclick="goToPage(${pageNumber - 1}, ${totalPages})">❮</a>
                </li>
            `;

    if (pageNumber > 4) {
        pagination.innerHTML += `
                <li class="page-item"><a class="page-link" onclick="goToPage(1, ${totalPages})">1</a></li>
                <li class="page-item disabled"><a class="page-link">...</a></li>
                `;
    }

    const startPage = Math.max(1, pageNumber - 3);
    const endPage = Math.min(totalPages, pageNumber + 3);

    for (let i = startPage; i <= endPage; i++) {
        pagination.innerHTML += `
                <li class="page-item ${i === pageNumber ? 'active' : ''}">
                    <a class="page-link" onclick="goToPage(${i}, ${totalPages})">${i}</a>
                </li>
                `;
    }

    if (pageNumber + 3 < totalPages) {
        pagination.innerHTML += `
                <li class="page-item disabled"><a class="page-link">...</a></li>
                <li class="page-item"><a class="page-link" onclick="goToPage(${totalPages}, ${totalPages})">${totalPages}</a></li>
                `;
    }

    // Next page button
    pagination.innerHTML += `
                <li class="page-item ${pageNumber === totalPages ? 'disabled' : ''}">
                    <a class="page-link" onclick="goToPage(${pageNumber + 1}, ${totalPages})">❯</a>
                </li>
            `;

}

function goToPage(page, totalPages) {
    if (page < 1 || page > totalPages) return; // Limit page range
    loadReviews(page)
        .then(data => generatePagination(data))
        .catch(error => {
            console.error("Error fetching data:", error);
        })
}

function formatDate(date) {
    const year = date[0];
    const month = String(date[1]).padStart(2, '0'); // 0-based
    const day = String(date[2]).padStart(2, '0');
    const hours = String(date[3]).padStart(2, '0');
    const minutes = String(date[4]).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

// review 검증
function validateReview(rating, reviewContext) {
    if (rating === 0) {
        alert('별점을 입력해주세요.');
        return false;
    }

    if (!reviewContext.trim()) {
        alert('리뷰 내용을 작성해주세요.');
        return false;
    }

    return true;
}

// 리뷰 등록
function submitReview() {
    const ratingElement = document.querySelector('input[name="rating"]:checked');
    const reviewElement = document.getElementById('reviewText');
    const rating = ratingElement?.value || 0;
    const reviewContext = reviewElement.value;

    if (validateReview(rating, reviewContext)) {
        fetch(`/books/${safeBookId}/reviews`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({
                rating: rating,
                context: reviewContext
            })
        })
            .then(response => response.json())
            .then(response => {
                if (response.status === 200) {
                    alert(response.message);
                    ratingElement.checked = false;
                    reviewElement.value = '';
                    goToPage(1, 1);
                } else {
                    alert('리뷰 등록에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('등록 중 오류 발생:', error);
            });
    }
}

// 리뷰 삭제
function deleteReview(button) {
    const reviewId = button.getAttribute('data-review-id');
    const safeReviewId = encodeURIComponent(reviewId)

    if (confirm('정말로 삭제하시겠습니까?')) {
        fetch(`/books/${safeBookId}/reviews/${safeReviewId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                [csrfHeader]: csrfToken
            }
        })
            .then(response => response.json())
            .then(response => {
                if (response.status === 200) {
                    alert(response.message);
                    goToPage(1,1);
                } else {
                    alert('리뷰 삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('삭제 중 오류 발생:', error);
            });
    }
}

// 리뷰 수정
function editReview(button) {
    const reviewId = button.getAttribute('data-review-id');
    const reviewItem = button.closest('li');
    const contextElement = reviewItem.querySelector('p');
    const currentContext = contextElement.textContent;

    // 기존 버튼 영역을 숨기기
    const buttonContainer = button.parentElement;
    buttonContainer.style.display = 'none';

    // 수정 폼 생성
    const editForm = document.createElement('div');

    // textarea 생성 및 내용 설정
    const textarea = document.createElement('textarea');
    textarea.className = 'form-control mb-2';
    textarea.textContent = currentContext;
    textarea.maxLength = 500;
    textarea.oninput = () => reviewInputEvent(textarea);

    const charCount = document.createElement('div');
    charCount.className = 'text-end small text-muted';
    charCount.innerHTML = `<span>0</span> / 500`;

    const btn = document.createElement('div');
    btn.className = 'd-flex justify-content-end';

    // 저장 버튼 생성
    const saveButton = document.createElement('button');
    saveButton.className = 'btn btn-sm btn-success ms-2';
    saveButton.textContent = '저장';

    // 취소 버튼 생성
    const cancelButton = document.createElement('button');
    cancelButton.className = 'btn btn-sm btn-secondary ms-2';
    cancelButton.textContent = '취소';

    // 수정 폼에 요소 추가
    editForm.appendChild(textarea);
    editForm.appendChild(charCount);
    btn.appendChild(saveButton);
    btn.appendChild(cancelButton);
    editForm.appendChild(btn);

    // 기존 컨텍스트 요소를 수정 폼으로 대체
    contextElement.replaceWith(editForm);

    // 저장 버튼 이벤트
    editForm.querySelector('.btn-success').addEventListener('click', () => {
        const updatedContext = editForm.querySelector('textarea').value;
        const safeReviewId = encodeURIComponent(reviewId)

        if (!updatedContext.trim()) {
            alert('리뷰 내용을 작성해주세요.');
            return;
        }

        fetch(`/books/${safeBookId}/reviews/${safeReviewId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({context: updatedContext}),
        })
            .then(response => response.json())
            .then(response => {
                if (response.status === 200) {
                    alert(response.message);
                    const updatedContextElement = document.createElement('p');
                    updatedContextElement.className = 'mt-2 mb-0';
                    updatedContextElement.textContent = response.data.context;
                    editForm.replaceWith(updatedContextElement);
                    buttonContainer.removeAttribute('style'); // style 속성 제거
                } else {
                    alert('리뷰 수정에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('수정 중 오류 발생:', error);
            });
    });

    // 취소 버튼 이벤트
    editForm.querySelector('.btn-secondary').addEventListener('click', () => {
        const originalContextElement = document.createElement('p');
        originalContextElement.className = 'mt-2 mb-0';
        originalContextElement.textContent = currentContext;
        editForm.replaceWith(originalContextElement);
        buttonContainer.removeAttribute('style'); // style 속성 제거
    });
}

function addBookToCart() {
    const quantity = document.getElementById('quantity')?.value || 1;

    fetch(`/books/${safeBookId}/cart`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
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