package github.javaguide.annotation;

import github.javaguide.spring.CustomScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegistrar.class)
@Documented
//@Import(CustomScannerRegistrar.class) 等待spring扫描
public @interface RpcScan {
    String[] basePackage();
}
