package github.javaguide.remoting.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcMessage {
    /*
     *  rpc messageByte
     * */
    private byte messageByte;

    /**
     * serialization type
     */
    private byte codec;

    /**
     * compress type
     */
    private byte compress;
    
    
    private int requsetId;

    /**
     * requestData
     */
    private Object data;

}
