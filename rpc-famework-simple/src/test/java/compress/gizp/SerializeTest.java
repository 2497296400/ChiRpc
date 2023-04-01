package compress.gizp;

import github.javaguide.compress.gzip.GzipCompress;
import github.javaguide.extension.ExtensionLoader;
import github.javaguide.serialize.Serializer;
import github.javaguide.serialize.hessian.HessianSerializer;
import github.javaguide.serialize.kyro.KryoSerializer;
import github.javaguide.serialize.protostuff.ProtostuffSerializer;
import github.javaguide.TestPojo.HellowImpl;
import org.junit.Test;

public class SerializeTest {
    @Test
    public void hessianTest() {
        Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension("hessian");
        HellowImpl hellow = new HellowImpl();
        byte[] bytes = serializer.serialize(hellow);
        GzipCompress compress = new GzipCompress();
        byte[] compress1 = compress.compress(bytes);
        bytes = compress.decopress(compress1);
        HellowImpl hellow1 = serializer.deserialize(bytes, HellowImpl.class);
        hellow1.print();
    }
    @Test
    public  void  KryoTest(){
        KryoSerializer serializer = new KryoSerializer();
        HellowImpl hellow = new HellowImpl();
        byte[] bytes = serializer.serialize(hellow);
        GzipCompress compress = new GzipCompress();
        byte[] compress1 = compress.compress(bytes);
        bytes = compress.decopress(compress1);
        HellowImpl hellow1 = serializer.deserialize(bytes, HellowImpl.class);
        hellow1.print();
    }
    @Test
    public void  ProtostuffTest(){
        ProtostuffSerializer serializer = new ProtostuffSerializer();
        HellowImpl hellow = new HellowImpl();
        byte[] bytes = serializer.serialize(hellow);
        GzipCompress compress = new GzipCompress();
        byte[] compress1 = compress.compress(bytes);
        bytes = compress.decopress(compress1);
        HellowImpl hellow1 = serializer.deserialize(bytes, HellowImpl.class);
        hellow1.print();
    }
}
