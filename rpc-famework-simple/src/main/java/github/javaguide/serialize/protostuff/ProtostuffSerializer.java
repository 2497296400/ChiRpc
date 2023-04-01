package github.javaguide.serialize.protostuff;

import github.javaguide.serialize.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffSerializer implements Serializer {
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public byte[] serialize(Object obj) {
        Class<?> aClass = obj.getClass();
        Schema schema = RuntimeSchema.getSchema(aClass);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(obj, schema, BUFFER);
        } finally {
            BUFFER.clear();
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T newMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, newMessage, schema);
        return newMessage;
    }
}
