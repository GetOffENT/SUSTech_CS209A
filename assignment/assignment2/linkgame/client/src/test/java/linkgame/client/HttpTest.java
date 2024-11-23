package linkgame.client;

import linkgame.common.OkHttpUtils;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-24 3:53
 */
public class HttpTest {
    @Test
    public void testLogin() {
        String s = OkHttpUtils.postForm("http://localhost:8080/user/login", Map.of("username", "admin", "password", "admin"), null);
        System.out.println("s = " + s);
    }

    @Test
    public void testRegister() {
        String s = OkHttpUtils.postForm("http://localhost:8080/user/register",
                Map.of("username", "admin2",
                        "password", "admin",
                        "nickname", "admin"),
                null);
        System.out.println("s = " + s);
    }
}
