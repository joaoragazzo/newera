package com.new_era.alpha.services.utils.color;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Payload;



@jakarta.validation.Constraint(validatedBy = HexColorValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHexColor {
    String message() default "Invalid HEX color format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
