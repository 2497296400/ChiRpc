package github.javaguide.compress.gizp;

import github.javaguide.compress.Compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GizpCompress implements Compress {
    private static final int BUFFER_SIZE = 1024 * 4;

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("byte is null");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            GZIPOutputStream gizp = new GZIPOutputStream(out);
            gizp.write(bytes);
            gizp.flush();
            gizp.finish();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip compress error", e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("byte is null");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(bytes));
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gunzip.read(buffer)) > -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip compress error", e);
        }
    }
}
