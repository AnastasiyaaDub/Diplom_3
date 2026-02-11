package data;

public class UserUI {

    private final String name;
    private final String email;
    private String password;
    private String accessToken;

    public UserUI(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accessToken = null;
    }

    public static UserUI generateRandom() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomSuffix = timestamp.substring(timestamp.length() - 6);

        return new UserUI(
                "Тестовый Пользователь " + randomSuffix,
                "test_" + timestamp + "@test.com",
                "password" + randomSuffix
        );
    }

    public String getName() {
        return name; }
    public String getEmail() {
        return email; }
    public String getPassword() {
        return password; }
    public String getAccessToken() {
        return accessToken;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setAccessToken(String accessToken) {
        if (accessToken != null && !accessToken.trim().isEmpty()) {
            this.accessToken = accessToken;
        }
    }
    // Проверка наличия токена
    public boolean hasAccessToken() {
        return accessToken != null && !accessToken.trim().isEmpty();
    }

}
