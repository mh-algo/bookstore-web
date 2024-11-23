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

function previewAdditionalImages(inputElement) {
    if (validateAdditionalImages(inputElement)) {
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