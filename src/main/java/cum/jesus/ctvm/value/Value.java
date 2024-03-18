package cum.jesus.ctvm.value;

import cum.jesus.ctni.Handle;
import cum.jesus.ctvm.VM;
import cum.jesus.ctvm.util.Types;

public abstract class Value implements Handle {
    public static final byte TYPE_BYTE = 0x01;
    public static final byte TYPE_SHORT = 0x02;
    public static final byte TYPE_INT = 0x03;
    public static final byte TYPE_LONG = 0x04;
    public static final byte TYPE_STRING = 0x05;
    public static final byte TYPE_FUNCTION = 0x06;

    public static Value fromJavaObject(Object obj) {
        if (obj instanceof Integer) {
            return new NumberValue(Types.INT.typeCode, (Integer) obj);
        } else if (obj instanceof String) {
            return new StringValue((String) obj);
        } else if (obj instanceof Handle) {
            if (obj instanceof Value) {
                return (Value) obj;
            } else {
                return VM.ZERO.clone();
            }
        } else if (obj instanceof Long) {
            return new NumberValue(Types.LONG.typeCode, (Long) obj);
        } else if (obj instanceof Byte) {
            return new NumberValue(Types.BYTE.typeCode, (Byte) obj);
        } else if (obj instanceof Short) {
            return new NumberValue(Types.SHORT.typeCode, (Short) obj);
        }

        return VM.ZERO.clone();
    }

    public abstract Object toJavaObject();

    public final NumberValue asNumber() {
        return (NumberValue) this;
    }

    public final StringValue asString() {
        return (StringValue) this;
    }

    public abstract void add(Value other);
    public abstract void sub(Value other);
    public abstract void mul(Value other);
    public abstract void div(Value other);

    public abstract void and(Value other);

    public abstract void or(Value other);

    public abstract void xor(Value other);

    public abstract void shl(Value other);

    public abstract void shr(Value other);

    public abstract void not();

    public abstract void neg();

    public abstract boolean isTrue();

    public abstract void inc();
    public abstract void dec();

    public abstract boolean lt(Value other);

    public abstract boolean gt(Value other);

    public abstract boolean lte(Value other);

    public abstract boolean gte(Value other);

    public abstract Value clone();

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
