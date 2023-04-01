package github.javaguide.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {
    //定义版本
    String version() default "";
    //定义分组
    String gruop() default "";
}
