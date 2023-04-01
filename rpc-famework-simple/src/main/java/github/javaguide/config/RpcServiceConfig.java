package github.javaguide.config;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcServiceConfig {
    //rpc请求版本
    private String version;

    private String group;

    private Object service;

    public String getRpcServiceName() {
        return this.getServiceName() + this.getVersion() + this.getGroup();
    }
    

    public String getServiceName() {
        return service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
