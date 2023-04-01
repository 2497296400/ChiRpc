package github.javaguide.remoting.transport;

import github.javaguide.extension.SPI;
import github.javaguide.remoting.dto.RpcRequest;
import io.protostuff.Rpc;

//公共接口 rpc远程调用方法
@SPI
public interface RpcRequestTransport {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
