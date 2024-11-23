package linkgame.userserver.service;

import linkgame.userserver.result.Result;
import linkgame.userserver.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author W
* @description 针对表【user】的数据库操作Service
* @createDate 2024-11-24 03:15:05
*/
public interface UserService extends IService<User> {

    Result<User> login(String username, String password);

    Result<User> register(User user);
}
