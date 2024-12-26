package com.new_era.alpha.services.utils.color;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HexColorValidator implements ConstraintValidator<ValidHexColor, String> {

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$");

    @Override
    public boolean isValid(String color, ConstraintValidatorContext context) {
        return color != null && HEX_COLOR_PATTERN.matcher(color).matches();
    }

    public static boolean isValidHexColor(String color) {
        return color != null && HEX_COLOR_PATTERN.matcher(color).matches();
    }
}
