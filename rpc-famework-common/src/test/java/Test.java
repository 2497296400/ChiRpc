import github.javaguide.TestPojo.Hellow;
import github.javaguide.TestPojo.HellowImpl;
import github.javaguide.extension.ExtensionLoader;
import github.javaguide.factory.SingletonFactory;

public class Test {
    @org.junit.Test
    public void extension() {
        Hellow hellow = ExtensionLoader.getExtensionLoader(Hellow.class).getExtension("Hellow");
        hellow.print();
        Hellow shllow = ExtensionLoader.getExtensionLoader(Hellow.class).getExtension("Shllow");
        shllow.print();
        HellowImpl hellow1 = SingletonFactory.getInstance(HellowImpl.class);
        hellow1.print();
        
    }
}
