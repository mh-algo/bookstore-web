function removeCommas() {
    const priceInput = document.getElementById("price");
    const discountInput = document.getElementById("discount");
    remove(priceInput);
    remove(discountInput);
}

function remove(element) {
    element.value = element.value.replace(/,/g, ''); // 쉼표 제거
}

function toggleCategoryList() {
    const categoryList = document.getElementById('categoryList');
    if (categoryList.style.display === 'none' || categoryList.style.display === '') {
        categoryList.style.display = 'block';
    } else {
        categoryList.style.display = 'none';
    }
}

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

function selectCategory(element) {
    const secondCategory = element.closest("ul").parentNode;
    const firstCategory = secondCategory.closest("ul").parentNode;

    const firstText = firstCategory.childNodes[0].textContent.trim();
    const secondText = secondCategory.childNodes[0].textContent.trim();
    const thirdText = element.textContent.trim();

    const selectedCategory = document.getElementById('selectedCategoryPath');
    selectedCategory.textContent = firstText + ' > ' + secondText + ' > ' + thirdText;

    const hiddenCategory = document.getElementById('category');
    const hiddenSubcategory = document.getElementById('subcategory');
    const hiddenSubSubcategory = document.getElementById('sub-subcategory');

    hiddenCategory.value = firstText;
    hiddenSubcategory.value = secondText;
    hiddenSubSubcategory.value = thirdText;

    const categoryList = document.getElementById('categoryList');
    categoryList.style.display = 'none';
}

function formatPrice(input) {
    if (input.value === '') {
        input.value = '0';
        return;
    }
    let value = input.value.replace(/[^0-9]/g, '');
    if (value) {
        value = parseInt(value).toLocaleString('ko-KR');
    }
    input.value = value;
}

// 최종 판매가 계산
document.getElementById('price').addEventListener('input', calculateFinalPrice);
document.getElementById('discount').addEventListener('input', calculateFinalPrice);

function calculateFinalPrice() {
    const price = parseFloat(document.getElementById('price').value.replace(/,/g, '')) || 0;
    let discount = parseFloat(document.getElementById('discount').value.replace(/,/g, '')) || 0;
    if (discount > price) {
        discount = price;
        document.getElementById('discount').value = discount.toLocaleString('ko-KR');
    }
    const finalPrice = price - discount;
    document.getElementById('finalPrice').value = finalPrice >= 0 ? finalPrice.toLocaleString('ko-KR') : 0;
}

function validateStockQuantity(input) {
    if (input.value < 0) {
        input.value = 0;
    }
    if (input.value > 1000) {
        input.value = 1000;
    }
    input.value = input.value.replace(/^0+(?!$)/, '')
}

function previewImage(inputElement, previewId) {
    const reader = new FileReader();
    reader.onload = function () {
        const output = document.getElementById(previewId);
        output.src = reader.result;
    };
    reader.readAsDataURL(inputElement.files[0]);
}

function previewAdditionalImages(inputElement, file, fileName) {
    if (validateAdditionalImages(inputElement)) {
        showSelectedFile(file, fileName)

        const previewContainer = document.getElementById('additionalImagesPreview');
        previewContainer.innerHTML = '';
        Array.from(inputElement.files).forEach(file => {
            const reader = new FileReader();
            reader.onload = function () {
                const img = document.createElement('img');
                img.src = reader.result;
                img.classList.add('image-preview', 'me-2', 'mb-2');
                previewContainer.appendChild(img);
            };
            reader.readAsDataURL(file);
        });
    }
}

function validateAdditionalImages(inputElement) {
    const files = inputElement.files;

    // 최대 파일 개수 검사
    if (files.length > 9) {
        alert("최대 9개의 파일만 업로드할 수 있습니다.");
        // 파일 선택을 초기화
        inputElement.value = "";
        return false;
    }
    return true;
}

function showSelectedFile(file, fileName) {
    const fileInput = document.getElementById(file);
    const fileNameInput = document.getElementById(fileName);

    if (fileInput.files.length === 1) {
        fileNameInput.value = fileInput.files[0].name;
    } else if (fileInput.files.length > 1) {
        fileNameInput.value = "파일 " + fileInput.files.length + "개 선택됨";
    } else {
        fileNameInput.value = "선택된 파일 없음";
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("searchInput");

    // Enter 키를 눌렀을 때 searchBooks() 호출
    searchInput.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            event.preventDefault(); // 기본 폼 제출 동작 방지
            searchBooks(); // 검색 버튼 함수 호출
        }
    });
});

let currentPage = 1; // 현재 페이지 번호를 저장하는 변수

// 서버에 POST 요청을 보내서 책을 검색
function searchBooks(page = 1) {
    const searchQuery = document.getElementById('searchInput').value;
    const searchResults = document.getElementById('searchResults');
    const nextButtonContainer = document.getElementById("nextButtonContainer");

    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

    // 새로운 검색을 시작할 때 페이지 번호를 1로 초기화
    if (page === 1) {
        currentPage = 1;
        searchResults.innerHTML = ''; // 이전 검색 결과 초기화
    }

    if (searchQuery.length > 0) {
        // Fetch를 이용한 POST 요청
        fetch('/seller/register/search-book', {
            method: 'POST',
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                [csrfHeader]: csrfToken
            },
            body: new URLSearchParams({
                bookName: searchQuery,
                page:page.toString() // 페이지 번호를 추가
            }),
        })
            .then(response => response.json())
            .then(data => {
                // 검색 결과를 화면에 표시
                if (data.items && data.items.length > 0) {
                data.items.forEach(book => {
                    const listItem = document.createElement('li');
                    listItem.classList.add('list-group-item');
                    listItem.className = "card mb-3 shadow-sm";
                    const title = splitTitleByParentheses(book.title)
                    listItem.innerHTML = `
                        <div class="row g-0">
                            <div class="col-md-4">
                                <img src="${book.image}" class="img-fluid rounded-start" alt="Book Cover">
                            </div>
                            <div class="col-md-8">
                                <div class="card-body">
                                    <h5 class="card-title">${title.mainTitle}</h5>
                                    <p class="card-text"><small class="text-muted">${title.subTitle}</small></p>
                                    <p class="card-text"><strong>저자:</strong> ${book.author}</p>
                                    <p class="card-text"><strong>출판사:</strong> ${book.publisher}</p>
                                    <p class="card-text"><strong>출판일:</strong> ${formatDate(book.pubdate)}</p>
                                    <p class="card-text"><strong>ISBN:</strong> ${book.isbn}</p>
                                </div>
                            </div>
                        </div>
                    `;

                    listItem.onclick = function () {
                        selectBook(book);
                    };

                    searchResults.appendChild(listItem);
                });

                // 다음 페이지가 있는 경우에만 다음 버튼을 동적으로 추가
                const start = data.start;
                const display = data.display;
                const total = data.total;

                if (start + display <= total) {
                    if (!document.getElementById("nextButton")) {
                        const nextButton = document.createElement("button");
                        nextButton.id = "nextButton";
                        nextButton.className = "btn btn-primary";
                        nextButton.type = "button";
                        nextButton.textContent = "다음";
                        nextButton.onclick = function () {
                            currentPage++; // 페이지 번호 증가
                            searchBooks(currentPage); // 다음 페이지 검색 요청
                        };
                        nextButtonContainer.appendChild(nextButton);
                    }
                } else {
                    // 검색 결과가 없을 경우 다음 버튼을 제거
                    removeNextButtonIfExists();
                }
            } else {
                    displayNoResultsMessage(searchResults);
                }
            })
            .catch(error => console.error('Error:', error));
    }
}

// 선택된 책을 입력 필드에 설정하고 모달을 닫음
function selectBook(bookItem) {
    document.getElementById('bookName').value = bookItem.title;
    const price = document.getElementById('price');
    price.value = bookItem.discount;
    formatPrice(price)
    toggleReadOnly(price)

    document.getElementById('discount').value = 0;
    const finalPrice = document.getElementById('finalPrice');
    finalPrice.value = bookItem.discount;
    formatPrice(finalPrice)

    document.getElementById('product.isbn').value = bookItem.isbn

    // 모달 닫기
    const modalElement = document.getElementById('bookSearchModal');
    const modal = bootstrap.Modal.getInstance(modalElement);
    modal.hide();
}

function splitTitleByParentheses(title) {
    // 괄호를 기준으로 문자열을 분리
    const regex = /^(.*?)\s*\((.*)\)$/;  // 괄호를 포함한 문자열을 찾기 위한 정규 표현식
    const match = title.match(regex);

    if (match) {
        // 괄호가 있을 경우
        return {
            mainTitle: match[1].trim(),   // 괄호 앞 부분
            subTitle: match[2].trim()     // 괄호 안의 내용
        };
    } else {
        // 괄호가 없을 경우
        return {
            mainTitle: title.trim(),      // 전체 제목을 mainTitle에 저장
            subTitle: ""                  // 빈 문자열로 설정
        };
    }
}

function formatDate(dateString) {
    // 날짜 문자열을 연도, 월, 일로 분리
    const year = dateString.substring(0, 4);
    const month = dateString.substring(4, 6);
    const day = dateString.substring(6, 8);

    // "YYYY년 MM월 DD일" 형식으로 반환
    return `${year}년 ${month}월 ${day}일`;
}

// 다음 버튼을 제거하는 함수
function removeNextButtonIfExists() {
    const nextButton = document.getElementById("nextButton");
    if (nextButton) {
        nextButton.remove();
    }
}

// 검색 결과가 없을 때 메시지를 표시하는 함수
function displayNoResultsMessage(container) {
    const message = document.createElement('p');
    message.className = 'text-muted text-center mt-3';
    message.textContent = '검색 결과가 존재하지 않습니다.';
    container.appendChild(message);
}

function toggleReadOnly(input) {
    if (input.value === "0" || input.value === "") {
        input.removeAttribute("readonly");
    } else {
        input.setAttribute("readonly", true);
    }
}