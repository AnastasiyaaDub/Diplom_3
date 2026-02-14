package data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUI {

    private final String name;
    private final String email;
    private String password;
    private String accessToken;


    public static UserUI generateRandom() {
        String timestamp = String.valueOf(System.currentTimeMillis());

        return new UserUI(
                "Тестовый Пользователь ",
                "test_" + timestamp + "@test.com",
                "password" + timestamp.substring(timestamp.length() - 6),
                null
        );
    }
}
