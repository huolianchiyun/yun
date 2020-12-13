package com.hlcy.yun.admincenter.model.$do;

import com.hlcy.yun.common.model.BaseDO;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 录像表
 * 表 t_vm_record_video
 * @author ASUS
 * @date 2020-12-11 21:00:52
 */
@Getter
@Setter
public class RecordVideo extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 文件URL
     */
    private String filePath;

    /**
     * 录像产生类型,alarm报警,manual手动
     */
    private String type;

    /**
     * 录像开始时间
     */
    private LocalDateTime startTime;

    /**
     * 录像结束时间
     */
    private LocalDateTime endTime;

    /**
     * 描述
     */
    private String description;

}
