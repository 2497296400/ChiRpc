package github.javaguide.remoting.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcRequest implements Serializable {
    //序列化版本Id
    private static final long serialVersionUID = 1905122041950251207L;
    //请求Id
    private String requestId;

    //请求接口名称
    private String interfaceName;

    //请求方法名称
    private String methodName;

    //方法参数
    private Object[] parameters;

    //参数类型
    private Class<?>[] paramTypes;

    //请求版本
    private String version;

    //请求所在组
    private String group;

    //获取Rpc服务名称 接口名字加版本加分组
    public String getRpcServiceName() {
        return this.getInterfaceName() + this.getVersion() + this.getGroup();
    }
}
