package linkgame.userserver.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-24 6:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordVO {
    private UserVO user;

    private UserVO opponent;

    private Integer score;

    private Integer opponentScore;

    private Date createAt;
}
