package github.javaguide.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {
    //定义版本
    String version() default "";
    //定义分组
    String group() default "";
}
