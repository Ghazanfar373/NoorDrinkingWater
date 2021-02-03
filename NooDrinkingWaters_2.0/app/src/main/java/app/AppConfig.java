package app;

public class AppConfig {

    public static int DEVICE_WIDTH = 0;
    public static int DEVICE_HEIGHT = 0;

    //default location is Ajman
    public static String DEFAULT_LOCATION_LAT = "25.4007049";
    public static String DEFAULT_LOCATION_LNG = "55.4848655";

    public static String PREF_IS_LOGIN = "prefIsLogin";
    public static String PREF_IS_FIRST_RUN = "prefIsFirstRun";
    public static String PREF_IS_REMEMBER_LOGIN = "prefIsRememberLogin";
    public static String FIRE_BASE_TOKEN = "fbToken";
    public static final String NEXT_NOTIFICATION_ID = "nextNotificationID";

    public static final String IMAGE_DIRECTORY = "NoorDrinkingWaterTempFolder";
    public static final String IMAGE_PROFILE_NAME = "IMG_NDWC_Profile_Temp.jpg";

    //the value of below variables are according to the response of login api
    // so be careful and compare with api if need to be changed
    public static String PREF_USER_ID = "id";
    public static String PREF_USER_NAME = "name";
    public static String PREF_USER_PROFILE_IMAGE = "profile_image";
    public static String PREF_USER_EMAIL = "email";
    public static String PREF_USER_PHONE = "contact_nos";
    public static String PREF_DELIVERY_DAYS = "delivery_required";
    public static String PREF_USER_CUSTOMER_TYPE = "customer_grp_name";
    public static String PREF_HOUSE_NO = "house_no";
    public static String PREF_BUILDING_NO = "building_no";
    public static String PREF_CITY = "city_id";
    public static String PREF_ADDRESS = "address";
    public static String PREF_ADDRESS_LATITUDE = "customer_address_lat";
    public static String PREF_ADDRESS_LONGITUDE = "customer_address_lng";
    public static String PREF_ARIA_ID = "area_id";
}
