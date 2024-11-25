package linkgame.common;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-22 14:51
 */
public enum MessageType {
    INIT,
    WAIT,
    PICK,
    JUDGE,
    LINE_SHOW,
    LINE_DISAPPEAR,
    FAIL,
    TIMEOUT,
    END,
    LIST,
    PICK_OPPONENT,
    RECONNECT,
    RECONNECT_FAIL,
    WAIT_RECONNECT,
    WAIT_RECONNECT_FAIL,
    RESET,
    RESET_TRUE,
    RESET_FALSE,
}
