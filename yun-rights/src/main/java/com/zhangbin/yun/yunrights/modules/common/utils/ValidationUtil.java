
package com.zhangbin.yun.yunrights.modules.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.zhangbin.yun.yunrights.modules.common.exception.BadRequestException;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * 验证工具
 */
public class ValidationUtil{

    /**
     * 验证空
     */
    public static void isNull(Object obj, String entity, String parameter , Object value){
        if(ObjectUtil.isNull(obj)){
            String msg = entity + " 不存在: "+ parameter +" is "+ value;
            throw new BadRequestException(msg);
        }
    }

    /**
     * 验证是否为邮箱
     */
    public static boolean isEmail(String email) {
        return new EmailValidator().isValid(email, null);
    }

    public static void validateRequestParams(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String messages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .reduce((m1, m2) -> m1 + "；" + m2)
                    .orElse("参数输入有误！");
            throw new IllegalArgumentException(messages);
        }
    }
}
