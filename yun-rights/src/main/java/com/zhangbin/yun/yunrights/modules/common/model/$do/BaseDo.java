package com.zhangbin.yun.yunrights.modules.common.model.$do;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhangbin.yun.yunrights.modules.common.audit.annotation.CreatedBy;
import com.zhangbin.yun.yunrights.modules.common.audit.annotation.CreatedDate;
import com.zhangbin.yun.yunrights.modules.common.audit.annotation.LastModifiedBy;
import com.zhangbin.yun.yunrights.modules.common.audit.annotation.LastModifiedDate;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 子类不要加 lombok.Data注解，其会导致子类重写 toString()，从而导致父类 toString失效
 */
public abstract class BaseDo {
    protected Long id;
    @CreatedBy
    @ApiModelProperty(hidden = true)
    protected String creator; // 用户登录账号，全局唯一
    @LastModifiedBy
    @ApiModelProperty(hidden = true)
    protected String updater; // 用户登录账号，全局唯一
    @CreatedDate
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    protected LocalDateTime createTime;
    @LastModifiedDate
    @ApiModelProperty(hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    protected LocalDateTime updateTime;

    /* 分组校验 */
    public @interface Create {
    }

    /* 分组校验 */
    public @interface Update {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
