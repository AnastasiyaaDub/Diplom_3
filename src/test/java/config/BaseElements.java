package config;

public class BaseElements {

    //общая ссылка
    public static final String BASE_URL = "https://stellarburgers.education-services.ru";


    //Для UI методов
    public static final String REGISTER_UI = BASE_URL + "/register";


    //Для api методов
    public static final String REGISTER = BASE_URL + "/api/auth/register";
    public static final String USER = BASE_URL + "/api/auth/user";
    public static final String LOGIN = BASE_URL + "/api/auth/login";
}
