package github.javaguide.TestPojo;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class HellowImpl implements Hellow , Serializable {
    
    public  void print(){
        System.out.println("你好");
    }
}
