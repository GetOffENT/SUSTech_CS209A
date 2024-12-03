package practice;

import java.lang.annotation.*;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-12-03 11:02
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CustomValidations.class)
public @interface CustomValidation {
    Rule rule();
}
