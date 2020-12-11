package com.hlcy.yun.sys.modules.rights.model.validation;

import com.hlcy.yun.common.utils.str.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BlurryValidator implements ConstraintValidator<ValidateBlurry, Object> {
    private String blurry;
    private String blurryType;

    @Override
    public void initialize(ValidateBlurry constraintAnnotation) {
        blurry = constraintAnnotation.blurry();
        blurryType = constraintAnnotation.blurryType();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Object blurry = beanWrapper.getPropertyValue(this.blurry);
        Object blurryType = beanWrapper.getPropertyValue(this.blurryType);
        return !StringUtils.isNotBlank((CharSequence) blurry)  || blurryType != null;
    }
}
