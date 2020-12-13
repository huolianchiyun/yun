package com.hlcy.yun.admincenter.model.$do;

import com.hlcy.yun.common.model.BaseDO;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 历史摄像表 (设备目录表) ，历史回放使用
 * 表 t_vm_history_video
 * @author ASUS
 * @date 2020-12-11 21:00:52
 */
@Getter
@Setter
public class HistoryVideo extends BaseDO implements Serializable {
    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 视频文件录制的日期
     */
    private LocalDate videoDate;

    /**
     * 视频开始时间
     */
    private LocalDateTime startTime;

    /**
     * 视频结束时间
     */
    private LocalDateTime endTime;

    /**
     * 涉密属性,0不涉密,1涉密
     */
    private Boolean secrecy;

    /**
     * 视频文件URL
     */
    private String filePath;

    /**
     * 视频文件大小
     */
    private Integer fileSize;

    /**
     * 摄像头所在位置
     */
    private String address;

    private static final long serialVersionUID = 1L;
}