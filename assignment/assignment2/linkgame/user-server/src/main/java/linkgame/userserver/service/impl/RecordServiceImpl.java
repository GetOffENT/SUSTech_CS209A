package linkgame.userserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import linkgame.userserver.entity.Record;
import linkgame.userserver.entity.vo.RecordVO;
import linkgame.userserver.entity.vo.UserVO;
import linkgame.userserver.mapper.UserMapper;
import linkgame.userserver.service.RecordService;
import linkgame.userserver.mapper.RecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author W
 * @description 针对表【record】的数据库操作Service实现
 * @createDate 2024-11-24 06:24:58
 */
@Service
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record>
        implements RecordService {

    private final UserMapper userMapper;

    @Override
    public List<RecordVO> getUserRecords(Integer userId) {

        List<Record> records = lambdaQuery().eq(Record::getUserId, userId).list();

        List<RecordVO> recordVOList = new ArrayList<>();

        records.forEach(record -> {
            RecordVO recordVO = new RecordVO();
            BeanUtils.copyProperties(record, recordVO);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userMapper.selectById(record.getUserId()), UserVO.class);
            recordVO.setUser(userVO);
            UserVO opponentVO = new UserVO();
            BeanUtils.copyProperties(userMapper.selectById(record.getOpponentId()), UserVO.class);
            recordVO.setOpponent(opponentVO);
            recordVOList.add(recordVO);
        });

        return recordVOList;
    }
}




