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
    const charCount = document.getElementById('charCount');
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

// review submit 검증
function reviewSubmitEvent() {
    const reviewText = document.getElementById('reviewText')
    const rating = document.querySelector('input[name="rating"]:checked')?.value || 0;

    if (rating === 0) {
        alert('별점을 입력해주세요.');
        return false;
    }

    if (!reviewText.trim()) {
        alert('리뷰 내용을 작성해주세요.');
        return false;
    }

    return true;
}

function toggleLikeBtn(button) {
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const pathParts = window.location.pathname.split('/');
    const bookId = pathParts[2];
    const reviewId = button.getAttribute('data-review-id');

    fetch(`/books/${bookId}/review/like`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({reviewId: reviewId})
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 403) {
                alert("로그인 후 시도해주세요.");
            }

            if (data.status === 200) {
                const like = document.getElementsByClassName('like-count').namedItem(reviewId);
                like.textContent = data.data.likeCount;

                const icon = button.querySelector('i'); // 아이콘 엘리먼트
                if (data.data.liked === true) {
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
            }
        })
        .catch(error => {
            console.error("Fetch Error:", error);
        });
}