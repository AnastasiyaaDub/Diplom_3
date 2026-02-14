package data;


import config.BaseElements;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;


public class UserApi {


    public static UserUI createUserForUITest() {

        UserUI user = UserUI.generateRandom();

        //Отправляем запрос регистрации
        Response response = given()
                .header("Content-type", "application/json")
                .body(Map.of(
                        "email", user.getEmail(),
                        "password", user.getPassword(),
                        "name", user.getName()
                ))
                .when()
                .post(BaseElements.REGISTER);

        //Проверяем успешность
        if (response.statusCode() == 200) {
            String accessToken = response.jsonPath().getString("accessToken");
            user.setAccessToken(accessToken);
            System.out.println("API: Создан пользователь: " + user.getEmail());
            return user;
        } else {
            System.err.println("API: Ошибка создания пользователя");
            return null;
        }
    }


    public static void deleteUser(UserUI user) {
        if (user == null) {
            System.out.println("Пользователь null, пропускаем удаление");
            return;
        }

        try {
            String token = user.getAccessToken();

            if (token == null || token.isEmpty()) {
                System.out.println("API: Токен отсутствует, пробуем логин для удаления...");

                Response loginResp = given()
                        .header("Content-type", "application/json")
                        .body(Map.of(
                                "email", user.getEmail(),
                                "password", user.getPassword()
                        ))
                        .when()
                        .post(BaseElements.LOGIN);
                if (loginResp.statusCode() == 200) {
                    token = loginResp.jsonPath().getString("accessToken");
                } else {
                    System.out.println("API: Логин не удался, значит пользователь не существует или неверный пароль.");
                    return;
                }
            }

            given()
                    .header("Authorization", token)
                    .when()
                    .delete(BaseElements.USER)
                    .then()
                    .statusCode(202); // или 200
            System.out.println("API: Удален пользователь " + user.getEmail());
        } catch (Exception e) {
            System.err.println("API: Ошибка удаления пользователя " + e.getMessage());
        }
    }
}



