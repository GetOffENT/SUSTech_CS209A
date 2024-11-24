package linkgame.userserver.controller;

import linkgame.userserver.entity.vo.UserVO;
import linkgame.userserver.result.Result;
import linkgame.userserver.entity.User;
import linkgame.userserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    // 所有已经登录的用户
    private static final Set<Integer> users = new HashSet<>();

    private final UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestParam String username, @RequestParam String password) {
        Result<User> loginUser = userService.login(username, password, users);
        if (loginUser.getCode() == 200) {
            users.add(loginUser.getData().getId());
        }
        return loginUser;
    }

    @PostMapping("/register")
    public Result<User> register(User user) {
        return userService.register(user);
    }

    @PostMapping("/logout")
    public Result<Object> logout(@RequestParam Integer userId) {
        users.remove(userId);
        return Result.success();
    }

    /**
     * 获取不包括自己的已登录用户列表
     */
    @PostMapping("/list")
    public Result<List<UserVO>> list(@RequestParam Integer userId) {
        List<User> list = userService.list();
        list.removeIf(user -> user.getId().equals(userId));

        List<UserVO> users = new ArrayList<>();
        for (User user : list) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setOnline(UserController.users.contains(user.getId()));
            users.add(userVO);
        }

        users.sort((o1, o2) -> {
            if (o1.isOnline() && !o2.isOnline()) {
                return -1;
            } else if (!o1.isOnline() && o2.isOnline()) {
                return 1;
            } else {
                return 0;
            }
        });

        return Result.success(users);
    }

    /**
     * 根据用户id列表获取用户信息
     */
    @PostMapping("/listByIds")
    public Result<List<UserVO>> listByIds(@RequestParam List<Integer> userIds) {
        List<User> list = userService.listByIds(userIds);

        List<UserVO> users = new ArrayList<>();
        for (User user : list) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            users.add(userVO);
        }

        return Result.success(users);
    }
}
