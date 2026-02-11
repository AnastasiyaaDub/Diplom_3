package data;


import config.BaseElements;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;


public class UserApi {


    public static UserUI createUserForUITest() {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String name = "Тест_" + timestamp;
            String email = "test_" + timestamp + "@mail.com";
            String password = "password" + timestamp.substring(0, 6); // минимум 6 символов

            //Создаем объект пользователя
            UserUI user = new UserUI(name, email, password);

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
                System.out.println("Создан пользователь: " + user.getEmail());
                return user;
            } else {
                System.err.println("Ошибка создания пользователя. Код: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Исключение при создании пользователя: " + e.getMessage());
            return null;
        }
    }

        public static void deleteUser (UserUI user){
            if (user == null) {
                System.out.println("Пользователь null, пропускаем удаление");
                return;
            }

            if (user.getAccessToken() == null || user.getAccessToken().isEmpty()) {
                System.out.println("У пользователя нет токена, пропускаем удаление: " + user.getEmail());
                return;
            }

            try {
                given()
                        .header("Authorization", user.getAccessToken())
                        .when()
                        .delete(BaseElements.USER)
                        .then()
                        .statusCode(202); // или 200
                System.out.println("Удален пользователь: " + user.getEmail());
            } catch (Exception e) {
                System.err.println("Ошибка удаления пользователя " + user.getEmail() + ": " + e.getMessage());
            }
        }
    }


