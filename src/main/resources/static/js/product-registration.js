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

    if (selectedCategory.style.display === 'none') {
        selectedCategory.style.display = 'block'
    }

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