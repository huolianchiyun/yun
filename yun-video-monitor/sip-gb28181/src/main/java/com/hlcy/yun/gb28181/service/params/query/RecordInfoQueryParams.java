package com.hlcy.yun.gb28181.service.params.query;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class RecordInfoQueryParams extends QueryParams {

    public RecordInfoQueryParams() {
        super("RecordInfo");
    }

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String filePath;
    /**
     * 保密属性(可选)缺省为0;0:不涉密,1:涉密
     */
    private int secrecy;

    /**
     * 录像产生类型(可选) time 或 alarm 或 manual或 all
     */
    private TriggerType type = TriggerType.time;

    /**
     * 触发者 ID
     */
    private String recorderID;

    /**
     * 录像模糊查询属性(可选)缺省为0;
     * 0:不进行模糊查询,此时根据SIP消息中To头域URI中的ID值确定查询录像位置,若ID值为本域系统ID则进行中心历史记录检索,若为前端设备ID则进行前端设备历史记录检索;
     * 1:进行模糊查询,此时设备所在域应同时进行中心检索和前端检索并将结果统一返回
     */
    private String indistinctQuery = "0";


    enum TriggerType {
        time, alarm, manual, all
    }
}
