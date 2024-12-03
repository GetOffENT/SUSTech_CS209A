package practice;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-12-03 10:58
 */
public class User {
    @MinLength
    @CustomValidation(rule = Rule.ALL_LOWERCASE)
    private String username;
    @MinLength(8)
    @CustomValidation(rule = Rule.NO_USERNAME)
    @CustomValidation(rule = Rule.HAS_BOTH_DIGITS_AND_LETTERS)
    private String password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
