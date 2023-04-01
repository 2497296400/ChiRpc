package github.javaguide.remoting.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class RpcMessage {
    //消息类型
    private byte messageType;
    
    //序列化使用的类型
    private byte codec;
    
    //压缩类型
    private byte compress;
    
    //请求消息的Id
    private  int requestId;
    
    //消息体
    private Object data;
}

