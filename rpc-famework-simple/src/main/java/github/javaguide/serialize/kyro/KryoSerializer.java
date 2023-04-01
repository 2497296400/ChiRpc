package github.javaguide.serialize.kyro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import github.javaguide.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class KryoSerializer implements Serializer {

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Output output = new Output(outputStream);
        ) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (IOException e) {
            throw new RuntimeException("Kyro serialize error");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(inputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            T object = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(object);
        } catch (IOException e) {
            throw new RuntimeException("Kyro deserialize error");
        }
    }
}
