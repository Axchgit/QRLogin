package cn.trunch.auth.http;


public class ApiUtil {
    public static String USER_ID = ""; // 用户ID
    public static String USER_TOKEN = ""; // 用户TOKEN
    public static String USER_AVATAR = ""; // 用户头像
    public static String USER_NAME = ""; // 用户昵称


    public final static String IP_PORT = "http://phone.xchtzon.top/";
    public final static String LOGIN = IP_PORT + "login/getUserInfo";
    public final static String TOKEN_INFO = IP_PORT + "login/getAuthInfo/";
    public final static String TOKEN_USE = IP_PORT + "login/phoneConfirmLogin/";
}
