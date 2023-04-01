package github.javaguide.serialize.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import github.javaguide.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HessianOutput hessianOutput = new HessianOutput(outputStream);
            hessianOutput.writeObject(obj);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("hessian serialize error");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);) {
            HessianInput hessianInput = new HessianInput(inputStream);
            Object object = hessianInput.readObject();
            return clazz.cast(object);
        } catch (IOException e) {
           throw  new  RuntimeException("hessian deserialize error");
        }
    }
}
