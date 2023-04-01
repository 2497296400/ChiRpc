package github.javaguide.serialize;

import github.javaguide.extension.SPI;

/*
 * 序列化
 * */
@SPI
public interface Serializer {
    byte[] serialize(Object obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
    
}
