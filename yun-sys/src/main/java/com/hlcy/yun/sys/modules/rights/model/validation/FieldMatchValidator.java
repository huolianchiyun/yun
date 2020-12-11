package com.hlcy.yun.sys.modules.rights.model.validation;

import com.hlcy.yun.sys.modules.common.config.RsaProperties;
import org.springframework.beans.BeanWrapperImpl;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import static com.hlcy.yun.common.utils.encodec.RsaUtils.decryptByPrivateKey;

public class FieldMatchValidator implements ConstraintValidator<ValidateFieldMatch, Object> {
    // 第一个字段名称
    private String first;
    // 第二个字段名称
    private String second;
    private boolean isEqual;
    private boolean isDecrypt;

    @Override
    public void initialize(ValidateFieldMatch constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
        this.isEqual = constraintAnnotation.isEqualMode();
        this.isDecrypt = constraintAnnotation.isEqualMode();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
        Object firstValue = wrapper.getPropertyValue(first);
        Object secondValue = wrapper.getPropertyValue(second);
        if (isDecrypt) {
            try {
                firstValue = decryptByPrivateKey(RsaProperties.privateKey, Objects.requireNonNull(firstValue).toString());
                secondValue = decryptByPrivateKey(RsaProperties.privateKey, Objects.requireNonNull(secondValue).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isEqual == (firstValue != null && firstValue.equals(secondValue));
    }
}
