package NettySpringTest;

import NettySpringTest.SpringInter.Fuck;
import github.javaguide.annotation.RpcReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@NoArgsConstructor
public class ClientMethodTest {
    @RpcReference(gruop = "gone", version = "one")
   public  Fuck fuck;
    
    public String fuckTest() {
       return fuck.fuck("辉");
    }
}
