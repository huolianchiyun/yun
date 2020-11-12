package com.yun.sys.modules.rights.model.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

public class DateRangeValidator implements ConstraintValidator<ValidateDateRange, Object> {
    // 开始时间
    private String from;
    // 结束时间
    private String to;

    @Override
    public void initialize(ValidateDateRange constraintAnnotation) {
        this.from = constraintAnnotation.from();
        this.to = constraintAnnotation.to();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Object start = beanWrapper.getPropertyValue(from);
        Object end = beanWrapper.getPropertyValue(to);
        if (start == null || end == null) {
            return true;
        }
        if (end instanceof Date) {
            return ((Date) end).compareTo((Date) start) >= 0;
        } else if (start instanceof Calendar) {
            return ((Calendar) end).compareTo((Calendar) start) >= 0;
        } else {
            return end.toString().compareTo(start.toString()) >= 0;
        }
    }
}
