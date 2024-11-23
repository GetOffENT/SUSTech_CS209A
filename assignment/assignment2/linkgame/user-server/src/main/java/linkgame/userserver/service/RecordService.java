package linkgame.userserver.service;

import linkgame.userserver.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import linkgame.userserver.entity.vo.RecordVO;

import java.util.List;

/**
* @author W
* @description 针对表【record】的数据库操作Service
* @createDate 2024-11-24 06:24:58
*/
public interface RecordService extends IService<Record> {

    List<RecordVO> getUserRecords(Integer userId);
}
