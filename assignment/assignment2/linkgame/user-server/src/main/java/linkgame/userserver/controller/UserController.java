package linkgame.userserver.controller;

import linkgame.userserver.result.Result;
import linkgame.userserver.entity.User;
import linkgame.userserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-24 3:16
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }

    @PostMapping("/register")
    public Result<User> register(User user) {
        return userService.register(user);
    }


}
