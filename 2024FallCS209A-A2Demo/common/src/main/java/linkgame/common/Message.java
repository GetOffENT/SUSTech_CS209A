package linkgame.common;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;


/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-22 7:21
 */
@Getter
@Setter
public class Message implements Serializable {
    private MessageType type;
    private Map<String, Object> data;

    private Message() {
    }

    public Message(MessageType type, Map<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public Message data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        // json格式 使用fastJson
        return JSON.toJSONString(this);
    }
}
