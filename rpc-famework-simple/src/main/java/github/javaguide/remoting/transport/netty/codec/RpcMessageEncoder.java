package github.javaguide.remoting.transport.netty.codec;

import github.javaguide.compress.Compress;
import github.javaguide.enums.CompressTypeEnum;
import github.javaguide.enums.SerializationTypeEnum;
import github.javaguide.extension.ExtensionLoader;
import github.javaguide.remoting.contants.RpcConstants;
import github.javaguide.remoting.dto.RpcMessage;
import github.javaguide.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import javax.lang.model.element.Name;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf out) throws Exception {
        try {
            //写魔术值 c', (byte) 'h', (byte) 'i', (byte) 'g 4字节
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            //写入Rpc的版本号 5字节
            out.writeByte(RpcConstants.VERSION);
            //预留四个字节 用来稍后填充数据长度 9 字节
            out.writeInt(out.writerIndex() * 4);
            byte messageType = rpcMessage.getMessageType();
            //写入rpc的消息类型 10字节
            out.writeByte(messageType);
            //写入序列化的标识 11字节
            out.writeByte(rpcMessage.getCodec());
            //写入压缩字节的byte 12字节
            out.writeByte(CompressTypeEnum.GZIP.getCode());
            //写入当前消息次数 16字节
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());
            //创建消息体
            byte[] bodyBytes = null;
            //头字节 长度为16 
            int fullLenth = RpcConstants.HEAD_LENGTH;
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                //序列化消息
                String codecName = SerializationTypeEnum.getName(rpcMessage.getCodec());
                log.info("利用{}序列化", codecName);
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(codecName);
                bodyBytes = serializer.serialize(rpcMessage.getData());
                log.info("编码压缩前数据长度{}", bodyBytes.length);
                //压缩消息体
                String compressName = CompressTypeEnum.getName(rpcMessage.getCompress());
                Compress compress = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
                bodyBytes = compress.compress(bodyBytes);
                log.info("编码压缩后数据长度{}", bodyBytes.length);
                fullLenth += bodyBytes.length;
            }
            if (bodyBytes != null) {
                out.writeBytes(bodyBytes);
            }
            int writerIndex = out.writerIndex();
            //修改写指针的位置 到之前预留的写指针
            out.writerIndex(writerIndex - fullLenth + RpcConstants.MAGIC_NUMBER.length + 1);
            log.info("当前数据写指针位置为：{}", out.writerIndex());
            //写入数据总长度
            out.writeInt(fullLenth);
            //恢复写指针位置
            out.writerIndex(writerIndex);
            
        } catch (Exception e){
            log.error("Encoder时候出现错误",e);
        }
    }
}
