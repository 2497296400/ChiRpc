package github.javaguide.compress;

import gitHub.chi.extension.SPI;

@SPI
public interface Compress {
    byte[] compress(byte[] bytes);
    
    byte[] decompress(byte[] bytes);
}
