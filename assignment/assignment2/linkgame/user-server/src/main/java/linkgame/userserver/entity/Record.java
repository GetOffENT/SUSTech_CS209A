package linkgame.userserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName record
 */
@TableName(value ="record")
@Data
public class Record implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer opponentId;

    private Integer score;

    private Integer opponentScore;

    private Date createAt;

    private static final long serialVersionUID = 1L;
}
