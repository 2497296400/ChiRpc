package compress.gizp;

import github.javaguide.compress.gzip.GzipCompress;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class GizpTest {
    @Test
    public void testGzip() {
        String s = "你好";
        GzipCompress compress = new GzipCompress();
        System.out.println(s.getBytes().length);
        byte[] bytes = compress.compress(s.getBytes(StandardCharsets.UTF_8));
        System.out.println(bytes.length);
        byte[] decopress = compress.decopress(bytes);
        System.out.println(decopress.length);
    }
}
