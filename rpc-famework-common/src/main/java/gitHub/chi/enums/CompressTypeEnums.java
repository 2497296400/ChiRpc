package gitHub.chi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompressTypeEnums {
    GZIP((byte) 0x01, "gzip");
    private final byte code;
    private final String name;
    public static String getName(byte code) {
        for (CompressTypeEnums c : CompressTypeEnums.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }
}
