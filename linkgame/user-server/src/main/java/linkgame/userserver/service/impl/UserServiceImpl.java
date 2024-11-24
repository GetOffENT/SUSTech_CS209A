package linkgame.userserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import linkgame.userserver.result.Result;
import linkgame.userserver.entity.User;
import linkgame.userserver.service.UserService;
import linkgame.userserver.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
* @author W
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-11-24 03:15:05
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final UserMapper userMapper;

    @Override
    public Result<User> login(String username, String password, Set<Integer> users) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );

        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!user.getPassword().equals(password)) {
            return Result.error("密码错误");
        }

        if (users.contains(user.getId())) {
            return Result.error("用户已登录");
        }

        return Result.success(user);
    }

    @Override
    public Result<User> register(User user) {
        User existUser = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, user.getUsername())
        );

        if (existUser != null) {
            return Result.error("用户已存在");
        }

        userMapper.insert(user);
        user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, user.getUsername())
        );
        return Result.success(user);
    }
}




