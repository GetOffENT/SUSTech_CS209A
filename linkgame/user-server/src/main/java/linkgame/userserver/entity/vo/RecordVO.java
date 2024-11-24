package linkgame.userserver.entity.vo;

import linkgame.userserver.entity.User;
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
    private User user;

    private User opponent;

    private Integer score;

    private Integer opponentScore;

    private Date createAt;
}
