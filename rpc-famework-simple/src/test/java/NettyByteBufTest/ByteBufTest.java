package NettyByteBufTest;

import github.javaguide.remoting.contants.RpcConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import org.junit.Test;

public class ByteBufTest {
    @Test
    public void byteTest() {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(RpcConstants.MAGIC_NUMBER);
        //写入Rpc的版本号 1字节
        out.writeByte(RpcConstants.VERSION);
        //留一点字节
        System.out.println(out.writerIndex());
        out.writeInt(out.writerIndex() * 4);
        System.out.println(out);
        while (out.isReadable()){
            System.out.println(out.readByte());
        }
    }
}
