package com.bookshelf.bookproject.publicpage;

public final class BookServiceUtil {
    public static String getImageUrl(String imageUrl, String defaultImageUrl) {
        return imageUrl == null || imageUrl.isBlank() ? defaultImageUrl : imageUrl;
    }

    public static Integer getPrice(Integer price, Integer customPrice) {
        return price == null || price == 0 ? customPrice : price;
    }

    public static int calculateDiscountPrice(Integer price, Integer discount) {
        if (price == null || discount == null) {
            return 0;
        }
        return Math.max(price - discount, 0);
    }

    public static String calculateDiscountRate(Integer price, Integer discount) {
        if (price == null || price == 0 || discount == null || discount == 0) {
            return "0";
        }
        double discountRate = (double) discount / price * 100;
        return String.format("%.1f", discountRate);
    }

    public static boolean validateStringId(String id) {
        String regex = "^[0-9]+$";
        return id == null || id.matches(regex);
    }

    // id가 존재하지 않을 경우(null) 유효
    // 존재한다면 숫자로 이루어져있을 경우에만 유효
    public static long stringToLongId(String id) {
        return validateStringId(id) ? Long.parseLong(id) : -1L;
    }
}
