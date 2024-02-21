package cum.jesus.ctvm.util;

public enum Types {
    BYTE(0x01),
    SHORT(0x02),
    INT(0x03),
    LONG(0x04),

    STRING(0x05),

    ;

    public final byte typeCode;

    Types(int typeCode) {
        this.typeCode = (byte) typeCode;
    }
}
