package practice;

import java.lang.reflect.Field;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-12-03 11:13
 */
public class DataValidator {
    public static boolean validate(Object obj) {
        boolean isValid = true;

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String value = (String) field.get(obj);
                MinLength minLength = field.getAnnotation(MinLength.class);
                if (minLength != null && (value == null || value.length() < minLength.value())) {
                    System.out.printf("Validation failed for field *%s*: should have a minimum length of %d%n",
                            field.getName(), minLength.value());
                    isValid = false;
                }

                CustomValidation[] validations = field.getAnnotationsByType(CustomValidation.class);
                for (CustomValidation validation : validations) {
                    switch (validation.rule()) {
                        case ALL_LOWERCASE:
                            if (value != null && !value.equals(value.toLowerCase())) {
                                System.out.printf("Validation failed for field *%s*: should be all lowercase%n", field.getName());
                                isValid = false;
                            }
                            break;
                        case NO_USERNAME:
                            Field username = obj.getClass().getDeclaredField("username");
                            username.setAccessible(true);
                            if (value != null && value.contains((String) username.get(obj))) {
                                System.out.printf("Validation failed for field *%s*: should not contain username%n", field.getName());
                                isValid = false;
                            }
                            break;
                        case HAS_BOTH_DIGITS_AND_LETTERS:
                            if (value != null && !Pattern.compile("(?=.*[0-9])(?=.*[a-zA-Z])").matcher(value).find()) {
                                System.out.printf("Validation failed for field *%s*: should have both letters and digits%n", field.getName());
                                isValid = false;
                            }
                            break;
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return isValid;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("Username: ");
            String username = sc.next();
            System.out.print("Password: ");
            String pwd = sc.next();
            User user = new User(username,pwd);
            if(validate(user)){
                System.out.println("Success!");
                break;
            }
        }
    }
}
