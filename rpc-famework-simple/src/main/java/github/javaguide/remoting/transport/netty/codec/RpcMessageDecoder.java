package github.javaguide.remoting.transport.netty.codec;

import github.javaguide.compress.Compress;
import github.javaguide.enums.CompressTypeEnum;
import github.javaguide.enums.SerializationTypeEnum;
import github.javaguide.extension.ExtensionLoader;
import github.javaguide.remoting.contants.RpcConstants;
import github.javaguide.remoting.dto.RpcMessage;
import github.javaguide.remoting.dto.RpcRequest;
import github.javaguide.remoting.dto.RpcResponse;
import github.javaguide.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {
    //数据的最大长度定义为 8 * 1024 * 1024 字节
    //数据的偏移量为 5 字节 魔术4字节 加版本号一字节
    //偏移的长度为 4字节 长度int位置
    //总共偏移9字节

    public RpcMessageDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);

    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    log.error("encode error ", e);
                    throw e;
                } finally {
                    frame.release();
                }
            }
        }
        return decoded;
    }

    private Object decodeFrame(ByteBuf frame) {
        log.info("当前数据可读长度为 {}", frame.readableBytes());
        checkMagicNumber(frame);
        checkVersion(frame);
        int fullLength = frame.readInt();
        byte messageType = frame.readByte();
        byte codecType = frame.readByte();
        byte compressType = frame.readByte();
        int requestId = frame.readInt();
        RpcMessage rpcMessage = RpcMessage.builder()
                .messageType(messageType)
                .compress(compressType)
                .codec(codecType)
                .requestId(requestId)
                .build();
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
            return rpcMessage;
        }
        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
            return rpcMessage;
        }
        int bodyLengh = fullLength - RpcConstants.HEAD_LENGTH;
        if (bodyLengh > 0) {
            byte[] bs = new byte[bodyLengh];
            frame.readBytes(bs);
            Compress compress = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(CompressTypeEnum.getName(compressType));
            log.info("解码解压缩前消息长高度{}", bs.length);
            bs = compress.decopress(bs);
            log.info("解码解压缩后消息长度{}", bs.length);
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(SerializationTypeEnum.getName(codecType));
            if (messageType == RpcConstants.REQUEST_TYPE) {
                RpcRequest request = serializer.deserialize(bs, RpcRequest.class);
                rpcMessage.setData(request);
            }
            if (messageType == RpcConstants.RESPONSE_TYPE) {
                RpcResponse rpcResponse = serializer.deserialize(bs, RpcResponse.class);
                rpcMessage.setData(rpcResponse);
            }
        }
        return rpcMessage;
    }

    private void checkVersion(ByteBuf frame) {
        byte version = frame.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("哪个沙雕发过来的 " + version);
        }
    }

    private void checkMagicNumber(ByteBuf frame) {
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] magic = new byte[len];
        frame.readBytes(magic);
        for (int i = 0; i < len; i++) {
            if (magic[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("哪个沙雕发过来的 " + Arrays.toString(magic));
            }
        }
    }

    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
    
}
