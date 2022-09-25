package cloud.ruan.boot.annotation;

import java.lang.annotation.*;

/**
 * @author ruanzh.eth
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Encrypt {
    boolean value() default true;
}
