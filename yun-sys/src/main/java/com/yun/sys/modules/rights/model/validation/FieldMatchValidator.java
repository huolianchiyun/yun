package com.yun.sys.modules.rights.model.validation;

import org.springframework.beans.BeanWrapperImpl;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<ValidateFieldMatch, Object> {
    // 第一个字段名称
    private String first;
    // 第二个字段名称
    private String second;
    private boolean isEqual;

    @Override
    public void initialize(ValidateFieldMatch constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
        this.isEqual = constraintAnnotation.isEqualMode();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
        Object firstValue = wrapper.getPropertyValue(first);
        Object secondValue = wrapper.getPropertyValue(second);
        return isEqual == (firstValue != null && firstValue.equals(secondValue));
    }
}
