package github.javaguide.remoting.dto;

import github.javaguide.enums.RpcResponseCodeEnum;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 715745410605631233L;

    //请求ID
    private String requestId;
    //应答消息code
    private Integer code;
    //response 的消息
    private String message;

    //response 消息体
    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> rpcResponse = new RpcResponse<>();
        rpcResponse.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        rpcResponse.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        rpcResponse.setRequestId(requestId);
        if (data != null) {
            rpcResponse.setData(data);
        }
        return rpcResponse;
    }

    public static <T> RpcResponse<T> fali(RpcResponseCodeEnum rpcResponseCodeEnum) {
        RpcResponse<T> rpcResponse = new RpcResponse<>();
        rpcResponse.setCode(rpcResponseCodeEnum.getCode());
        rpcResponse.setMessage(rpcResponseCodeEnum.getMessage());
        return rpcResponse;
    }
}
