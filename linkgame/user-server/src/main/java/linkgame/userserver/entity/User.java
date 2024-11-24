package linkgame.userserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
@Accessors(chain = true)
public class User implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    private static final long serialVersionUID = 1L;
}
