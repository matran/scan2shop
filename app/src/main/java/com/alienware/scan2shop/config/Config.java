package com.alienware.scan2shop.config;
/**
 * Created by henry cheruiyot on 2/3/2018.
 */
public class Config {
    public static final String TOPIC_GLOBAL = "global";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    private static final String BASE_URL = "https://scan2shop-service.herokuapp.com/api";
    //private static final String BASE_URL = "http://192.169.1.122/api";
    public static final String URL_HELP = BASE_URL + "/help";
    public static final String URL_ITEM = BASE_URL + "/products/";
    public static final String URL_LOGIN = BASE_URL + "/login";
    public static final String URL_REGISTER = BASE_URL + "/users";
    public static final String URL_SEARCH = BASE_URL + "/search/";
    public static final String URL_RECEIPT = BASE_URL + "/receipt/";
    public static final String URL_RESENT=BASE_URL+"/resent/";
    public static final String URL_PAYMENT = BASE_URL + "/payment";
    public static final String URL_FIREBASE = BASE_URL + "/fcm";
    public static final String URL_HISTORY = BASE_URL + "/history/";
    public static final String URL_FEEDBACK = BASE_URL + "/feedback";
    public static final String URL_CATEGORIES = BASE_URL + "/category";
    public static final String URL_GOODS = BASE_URL + "/item";
    public static final String URL_PASSWORD_CHANGE = BASE_URL + "/passwordrequest/";
    public static final String URL_PHONE=BASE_URL+"/phone/";
    public static final String URL_VERIFY=BASE_URL+"/verify";
    public static final String URL_PROFILE_PHOTO=BASE_URL+"/uploadphoto";
}