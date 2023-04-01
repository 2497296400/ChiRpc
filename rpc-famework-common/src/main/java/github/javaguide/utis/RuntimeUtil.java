package github.javaguide.utis;
//获取cpu资源数
public class RuntimeUtil {
    public static  int cpus(){
        return Runtime.getRuntime().availableProcessors();
    }
}
