package github.javaguide.compress.gzip;

import github.javaguide.compress.Compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/*
* 解压缩和压缩
* */
public class GzipCompress implements Compress {
    private static final int BUFFER_SIZE = 1024 * 4;

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("Gzip is null");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
            gzipOutputStream.write(bytes);
            gzipOutputStream.flush();
            gzipOutputStream.finish();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip compress error", e);
        }
    }

    @Override
    public byte[] decopress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("Gzip is null");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));
            byte[] buffer = new byte[BUFFER_SIZE];
            int size;
            while ((size = gzipInputStream.read(buffer)) > -1) {
                out.write(buffer, 0, size);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip decompress error", e);
        }
    }
}
