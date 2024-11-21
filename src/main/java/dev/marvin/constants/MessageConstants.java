package dev.marvin.constants;

public class MessageConstants {

    private MessageConstants() {
    }

    /*
     * Shared
     * */
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred while processing the request. Please try again later";

    /*
     * Category
     * */
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String DUPLICATE_CATEGORY_NAME = "Category with given name already exists";
    public static final String CATEGORY_CREATED = "Category created successfully";
    public static final String CATEGORY_UPDATED = "Category updated successfully";
    public static final String CATEGORY_STATUS_UPDATED = "Category status updated successfully";

    /*
     * Product
     * */
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String DUPLICATE_PRODUCT_NAME = "Product with given name already exists";
    public static final String PRODUCT_CREATED = "Product created successfully";
    public static final String PRODUCT_UPDATED = "Product updated successfully";
    public static final String PRODUCT_STATUS_UPDATED = "Product visibility status updated successfully";


    /*
     * User
     * */
    public static final String USER_NOT_FOUND = "User not found";
    public static final String DUPLICATE_MOBILE = "mobile number already taken";
    public static final String PASSWORD_CHANGED = "Password changed successfully";
    public static final String PROFILE_UPDATED = "Profile updated successfully";
    public static final String PASSWORD_CREATED = "Password created successfully";

    /*
     * Authentication
     * */
    public static final String PROCEED_TO_LOGIN = "Proceed to login screen";
    public static final String OTP_SENT = "OTP sent successfully. Proceed to otp verification screen";
    public static final String PROCEED_TO_PASSWORD_CREATION = "OTP is valid. Proceed to password creation screen";
    public static final String ACCOUNT_CREATED = "Your Account has been created";

    /*
     * Cart
     * */
    public static final String ADD_PRODUCT_TO_CART = "Product added to cart successfully";

}
