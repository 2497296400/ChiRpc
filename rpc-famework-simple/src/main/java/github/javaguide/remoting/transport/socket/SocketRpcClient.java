package github.javaguide.remoting.transport.socket;

import github.javaguide.exception.RpcException;
import github.javaguide.extension.ExtensionLoader;
import github.javaguide.registry.ServiceDiscovery;
import github.javaguide.remoting.dto.RpcRequest;
import github.javaguide.remoting.transport.RpcRequestTransport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
@AllArgsConstructor
public class SocketRpcClient implements RpcRequestTransport {
    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient() {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            //发送数据
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object readObject = inputStream.readObject();
            System.out.println(readObject.toString());
            return readObject;
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("Socket 调用服务失败:", e);
        }
    }
}
