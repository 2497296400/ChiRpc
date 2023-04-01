package github.javaguide.remoting.contants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RpcConstants {
    /*
    * 请求的一系列常量
    *
    * */
    
    //用于魔术值 识别消息是否合法
    public static final byte[] MAGIC_NUMBER = {(byte) 'c', (byte) 'h', (byte) 'i', (byte) 'g'};
    //默认字符集
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    //当前版本号
    public static final byte VERSION = 1;
    //头部总长度
    public static final byte TOTAL_LENGTH = 16;
    //请求消息类型定义
    public static final byte REQUEST_TYPE = 1;
    //响应消息类型定义
    public static final byte RESPONSE_TYPE = 2;
    //心跳请求类型
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    
    //心跳响应类型
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;
    
    //头部长度
    public static final int HEAD_LENGTH = 16;
    
    public static final String PING = "ping";

    public static final String PONG = "pong";
    //最大长度 为 8MB
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
}
