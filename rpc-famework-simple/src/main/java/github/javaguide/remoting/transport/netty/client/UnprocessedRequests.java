package github.javaguide.remoting.transport.netty.client;

import github.javaguide.remoting.dto.RpcResponse;
import io.netty.util.concurrent.CompleteFuture;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class UnprocessedRequests {

    private static final Map<String, CompletableFuture<RpcResponse<Object>>>
            UNPROCESSED_RESPONSE_FUTURE = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse<Object>> future) {
        UNPROCESSED_RESPONSE_FUTURE.put(requestId, future);
    }

    public void complete(RpcResponse<Object> rpcResponse) {
        String requestId = rpcResponse.getRequestId();
        CompletableFuture<RpcResponse<Object>> future = UNPROCESSED_RESPONSE_FUTURE.remove(requestId);
        if (future != null) {
            future.complete(rpcResponse);
        }else {
            log.error("没有当前任务");
            throw new IllegalStateException();
        }
    }
}
