package fr.manu.petitesannonces.web.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


/**
 * @author emmanuel.mura
 *
 */
@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {

    String message() default "Error in password confirmation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
