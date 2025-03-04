package com.musa3team.devout.domain.store.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TenMinuteIntervalValidator.class) //유효성 검사 수행할 클래스 지정
@Target({ElementType.FIELD}) //이 인터페이스를 필드에 적용하겠다.
@Retention(RetentionPolicy.RUNTIME) //이 인터페이스를런타임 동안 유지
public @interface ValidTenMinuteInterval {
    String message() default "분은 10분 단위로만 작성하실 수 있습니다.";    //유효성 검사 실패했을 때 메세지
    Class<?>[] groups() default {}; //유효성 검사 그룹을 정의. 현재는 빈 배열이라 아무런 그룹이 정의되지 않음
    Class<? extends Payload>[] payload() default {};    //유효성 검사에 대한 추가 정보를 제공하는 데 사용. 현재는 빈 배열이라 아무런 추가 정보를 제공하지 않음
}
