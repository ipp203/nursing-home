package nursinghome.resident.validator;


import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;


public class DateOfBirthValidator implements ConstraintValidator<DateOfBirth, LocalDate> {

    private final int minAge;
    private final int maxAge;
    private String message;

    public DateOfBirthValidator(@Value("${nursing-home.resident.minAge}") int minAge, @Value("${nursing-home.resident.maxAge}") int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        message= "Invalid date of birth. Age must be between " + minAge + " and " + maxAge + "!";
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        int years = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return (years >= minAge && years <= maxAge);
    }

    @Override
    public void initialize(DateOfBirth constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
