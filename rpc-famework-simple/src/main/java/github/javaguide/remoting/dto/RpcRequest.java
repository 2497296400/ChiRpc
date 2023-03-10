package github.javaguide.remoting.dto;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcRequest  implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String requestId;

    private String interfaceName;

    private String methodName;

    private Object[] parameters;

    private Class<?>[] paramTypes;

    private String version;

    private String group;

    public String getRpcServerName() {
        return this.getInterfaceName() + this.getGroup() + this.getVersion();
    }
}
