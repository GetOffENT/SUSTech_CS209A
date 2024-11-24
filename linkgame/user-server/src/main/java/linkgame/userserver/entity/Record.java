package linkgame.userserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @TableName record
 */
@TableName(value ="record")
@Data
public class Record implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer opponentId;

    private Integer score;

    private Integer opponentScore;

    private LocalDateTime createAt;

    private static final long serialVersionUID = 1L;
}
