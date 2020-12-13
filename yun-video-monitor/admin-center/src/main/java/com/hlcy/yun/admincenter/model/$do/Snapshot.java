package com.hlcy.yun.admincenter.model.$do;

import com.hlcy.yun.common.model.BaseDO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * 抓拍表
 * 表 t_vm_snapshot
 * @author ASUS
 * @date 2020-12-11 21:00:52
 */
@Getter
@Setter
public class Snapshot extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 文件URL
     */
    private String filePath;

    /**
     * 描述
     */
    private String description;

}
