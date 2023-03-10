import gitHub.chi.factory.SingletonFactory;
import gitHub.chi.utils.RunTimeUtil;
import gitHub.chi.utils.concurrent.threadpool.ThreadPoolFactoryUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class Test {
    @org.junit.Test
    public void getInstance() {
        System.out.println(RunTimeUtil.cpus());
    }
    @org.junit.Test
    public  void  getThreadPool(){
        ExecutorService service = ThreadPoolFactoryUtil.creatrCustomThreadPoolIfAbsent("Chi");
        service.submit(()->{
            System.out.println("THread");
        });
        ThreadPoolFactoryUtil.printThreadPoolStatus((ThreadPoolExecutor) service);
    }
}