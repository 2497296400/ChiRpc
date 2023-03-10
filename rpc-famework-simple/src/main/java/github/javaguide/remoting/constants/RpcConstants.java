package github.javaguide.remoting.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RpcConstants {

    public static final byte[] MAGIC_NUMBER = {(byte) 'g', (byte) 'r', (byte) 'p', (byte) 'c'};
    public static final Charset DEFAULT_CHAREST = StandardCharsets.UTF_8;
    public static final byte VERSION = 1;
    public static final byte TOTAL_LENGTH = 16;

    public static final byte REQUEST_TYPE = 1;

    public static final byte RESPONSE_TYPE = 2;

    public static final byte HEARTABLE_REQUSE_TYPE = 3;

    public static final byte HEARTABLE_RESPONSE_TYPE = 4;

    public static final byte HEAD_LENGHT = 16;

    public static final String PING = "PING";

    public static final String PONG = "PONG";

    public static final  int MAX_FRAME_LENGHTH =8*1024*1024;
}
