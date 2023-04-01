package NettySpringTest.SpringInter;

import github.javaguide.annotation.RpcService;

@RpcService(version = "one",group = "gone")
public class FuckImpl implements Fuck {
    @Override
    public String fuck(String name) {
        return "fuck" + name;
    }
}
