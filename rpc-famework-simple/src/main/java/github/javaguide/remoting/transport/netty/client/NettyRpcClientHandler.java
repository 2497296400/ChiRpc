package github.javaguide.remoting.transport.netty.client;

import github.javaguide.enums.CompressTypeEnum;
import github.javaguide.enums.SerializationTypeEnum;
import github.javaguide.factory.SingletonFactory;
import github.javaguide.remoting.contants.RpcConstants;
import github.javaguide.remoting.dto.RpcMessage;
import github.javaguide.remoting.dto.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {
    private final UnprocessedRequests unprocessedRequests;
    private final NettyRpcClient nettyRpcClient;

    public NettyRpcClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.nettyRpcClient = SingletonFactory.getInstance(NettyRpcClient.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info("客户端收到的消息为:{}", msg);
            if (msg instanceof RpcMessage) {
                RpcMessage rpcMessage = (RpcMessage) msg;
                byte messageType = rpcMessage.getMessageType();
                if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                    log.info("心跳请求消息{}", rpcMessage.getData());
                } else if (messageType == RpcConstants.RESPONSE_TYPE) {
                    atomicInteger.set(0);
                    RpcResponse<Object> rpcResponse = (RpcResponse<Object>) rpcMessage.getData();
                    unprocessedRequests.complete(rpcResponse);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE && atomicInteger.getAndIncrement() < 10) {
                log.info("写空闲发生 链接地址为{}", ctx.channel().remoteAddress());
                log.info("当前重试次数为{}", atomicInteger.get());
                Channel channel = nettyRpcClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = RpcMessage.builder()
                        .codec(SerializationTypeEnum.HESSIAN.getCode())
                        .messageType(RpcConstants.HEARTBEAT_REQUEST_TYPE)
                        .compress(CompressTypeEnum.GZIP.getCode())
                        .data(RpcConstants.PING)
                        .build();
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
            if (atomicInteger.get() >= 10) {
                log.info("写空闲过长 断开连接 滚蛋吧 {}", atomicInteger.get());
                ctx.close();
                nettyRpcClient.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("客户端连接错误：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}