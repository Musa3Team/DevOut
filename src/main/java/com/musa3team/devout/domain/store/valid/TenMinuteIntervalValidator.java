package com.musa3team.devout.domain.store.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class TenMinuteIntervalValidator implements ConstraintValidator<ValidTenMinuteInterval, LocalTime> {

    @Override
    public void initialize(ValidTenMinuteInterval constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation); //어노테이션 속성의 초가화. ConstraintValidator 인터페이스에는 기본적으로 initialize 메서드가 비어 있으므로, 실제로 아무 작업도 수행하지 않음
    }

    @Override
    public boolean isValid(LocalTime localTime, ConstraintValidatorContext context) {
        return isValidTenMinuteInterval(localTime);
    }

    public static boolean isValidTenMinuteInterval(LocalTime time) {
        int minute = time.getMinute();
        return minute % 10 == 0;
    }
}
