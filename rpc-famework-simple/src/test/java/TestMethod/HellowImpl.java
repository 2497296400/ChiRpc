package TestMethod;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class HellowImpl implements Hellow, Serializable {
    @Override
    public String hellow(String name) {
        System.out.println("fuck" + name);
        return "fuck" + name;
    }
}
