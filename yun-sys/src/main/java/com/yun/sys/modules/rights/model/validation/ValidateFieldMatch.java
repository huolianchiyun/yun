package com.yun.sys.modules.rights.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证两个字段是否相等，即验证 first 和 second是否相等
 * 一般用于密码验证
 */
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
@Repeatable(ValidateFieldMatch.List.class)
public @interface ValidateFieldMatch {
    // 验证的第一个字段
    String first() default "";

    // 验证的第二个字段
    String second() default "";

    String message() default "两次输入的{keyWord}必须相同!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // 验证 first、second 的相等性还是不等性，默认验证相等性
    boolean isEqualMode() default true;

    // first、second 是否解密后比较
    boolean isDecrypt() default false;

    @Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ValidateFieldMatch[] value();
    }
}
