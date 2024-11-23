package linkgame.userserver.controller;

import linkgame.userserver.entity.Record;
import linkgame.userserver.entity.vo.RecordVO;
import linkgame.userserver.result.Result;
import linkgame.userserver.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @PostMapping("/all")
    public Result<List<RecordVO>> getUserRecords(@RequestParam Integer userId) {
        return Result.success(recordService.getUserRecords(userId));
    }

    @PostMapping()
    public Result<Boolean> addRecord(Record record) {
        record.setCreateAt(new Date());
        return Result.success(recordService.save(record));
    }
}
