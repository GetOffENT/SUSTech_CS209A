package linkgame.userserver.controller;

import linkgame.userserver.entity.Record;
import linkgame.userserver.entity.vo.RecordVO;
import linkgame.userserver.result.Result;
import linkgame.userserver.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-24 6:27
 */
@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping
    public Result<List<RecordVO>> getUserRecords(@RequestParam Integer userId) {
        return Result.success(recordService.getUserRecords(userId));
    }

    @PostMapping
    public Result<Boolean> addRecord(Record record) {
        recordService.save(record);
        Record recordOpponent = new Record();
        recordOpponent.setUserId(record.getOpponentId());
        recordOpponent.setOpponentId(record.getUserId());
        recordOpponent.setScore(record.getOpponentScore());
        recordOpponent.setOpponentScore(record.getScore());
        recordOpponent.setCreateAt(record.getCreateAt());
        recordService.save(recordOpponent);
        return Result.success();
    }
}
