package cloud.ruan.boot.annotation;

import java.lang.annotation.*;

/**
 * 接口数据体解密注解，对接口数据进行验签，并解密data字段
 *
 * @author ruanzh.eth
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Decrypt {
    boolean value() default true;
}
