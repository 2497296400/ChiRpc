package NettySpringTest;

import github.javaguide.annotation.RpcScan;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;

@RpcScan(basePackage = {"github.javaguide", "NettySpringTest"})
public class SpringClient {
    

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringClient.class);
        ClientMethodTest methodTest = (ClientMethodTest) annotationConfigApplicationContext.getBean("clientMethodTest");
        System.out.println(methodTest.fuckTest());
    }
}
