package github.javaguide.config;


import gitHub.chi.extension.SPI;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcServerConfig {
    private String version = "";
    private String group = "";
    private Object server;

    public String getRpcServiceName() {
        return this.getServerName() + this.getGroup() + this.getVersion();
    }
    
    public String getServerName() {
        return this.server.getClass().getInterfaces()[0].getCanonicalName();
    }
}
