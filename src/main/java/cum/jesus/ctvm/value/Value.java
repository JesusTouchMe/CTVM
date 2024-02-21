package cum.jesus.ctvm.value;

public abstract class Value {
    public static final byte TYPE_BYTE = 0x01;
    public static final byte TYPE_SHORT = 0x02;
    public static final byte TYPE_INT = 0x03;
    public static final byte TYPE_LONG = 0x04;
    public static final byte TYPE_STRING = 0x05;

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
}
