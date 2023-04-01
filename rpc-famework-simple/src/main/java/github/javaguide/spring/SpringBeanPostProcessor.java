package github.javaguide.spring;

import github.javaguide.annotation.RpcReference;
import github.javaguide.annotation.RpcService;
import github.javaguide.config.RpcServiceConfig;
import github.javaguide.extension.ExtensionLoader;
import github.javaguide.factory.SingletonFactory;
import github.javaguide.provider.Impl.ZkServiceProviderImpl;
import github.javaguide.provider.ServiceProvider;
import github.javaguide.proxy.RpcClientProxy;
import github.javaguide.remoting.transport.RpcRequestTransport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {
    private final ServiceProvider serviceProvider;
    private final RpcRequestTransport rpcClient;

    public SpringBeanPostProcessor() {
        this.serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
        this.rpcClient = ExtensionLoader.getExtensionLoader(RpcRequestTransport.class).getExtension("netty");
    }

    @Override
    @SneakyThrows
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                    .version(rpcService.version())
                    .group(rpcService.group())
                    .service(bean).build();
            serviceProvider.publishService(rpcServiceConfig);
        }
        return bean;
    }

    @Override
    @SneakyThrows
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] fields = targetClass.getFields();
        for (Field field : fields) { 
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                        .service(bean)
                        .version(rpcReference.version())
                        .group(rpcReference.gruop()).build();
                RpcClientProxy proxy = new RpcClientProxy(rpcClient, rpcServiceConfig);
                Object result = proxy.getProxy(field.getType());
                field.setAccessible(true);
                field.set(bean, result);
            }
        }
        return bean;
    }
}
