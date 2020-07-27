package com.zhangbin.yun.yunrights.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码业务场景
 */
@Getter
@AllArgsConstructor
public enum BizCodeEnum {

    /* 旧邮箱修改邮箱 */
    ONE(1, "旧邮箱修改邮箱"),

    /* 通过邮箱修改密码 */
    TWO(2, "通过邮箱修改密码");

    private final Integer code;
    private final String description;

    public static BizCodeEnum find(Integer code) {
        for (BizCodeEnum value : BizCodeEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return null;
    }

}
