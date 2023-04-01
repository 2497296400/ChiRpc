package github.javaguide.remoting.transport.socket;

import github.javaguide.factory.SingletonFactory;
import github.javaguide.remoting.dto.RpcRequest;
import github.javaguide.remoting.dto.RpcResponse;
import github.javaguide.remoting.handler.RpcRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable {
    private final Socket socket;
    private final RpcRequestHandler rpcRequestHandler;

    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest rpcRequest = (RpcRequest) in.readObject();
            Object handle = rpcRequestHandler.handle(rpcRequest);
            out.writeObject(RpcResponse.success(handle, rpcRequest.getRequestId()));
            out.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("occur exception:", e);
        } 
    }
}
