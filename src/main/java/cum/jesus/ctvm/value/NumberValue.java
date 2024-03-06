package cum.jesus.ctvm.value;

public final class NumberValue extends Value {
    public final byte typeByte;
    private long value;

    public NumberValue(byte type, long value) {
        this.typeByte = type;
        this.value = value;
    }

    public NumberValue(byte type, int value) {
        this.typeByte = type;
        this.value = value;
    }

    public NumberValue(byte type, short value) {
        this.typeByte = type;
        this.value = value;
    }

    public NumberValue(byte type, byte value) {
        this.typeByte = type;
        this.value = value;
    }

    public NumberValue(byte type, boolean value) {
        this.typeByte = type;
        this.value = value ? 1 : 0;
    }

    public long getLong() {
        return value;
    }

    public int getInt() {
        return (int) value;
    }

    public short getShort() {
        return (short) value;
    }

    public byte getByte() {
        return (byte) value;
    }

    @Override
    public void add(Value other) {
        if (other instanceof NumberValue) {
            value += ((NumberValue) other).value;
        } else {
            // handle error
        }
    }

    @Override
    public void sub(Value other) {
        if (other instanceof NumberValue) {
            value -= ((NumberValue) other).value;
        } else {
            // handle error
        }
    }

    @Override
    public void mul(Value other) {
        if (other instanceof NumberValue) {
            value *= ((NumberValue) other).value;
        } else {
            // handle error
        }
    }

    @Override
    public void div(Value other) {
        if (other instanceof NumberValue) {
            value /= ((NumberValue) other).value;
        } else {
            // handle error
        }
    }

    @Override
    public void and(Value other) {
        if (other instanceof NumberValue) {
            value &= ((NumberValue) other).value;
        } else {
            // handle error
        }
    }

    @Override
    public void or(Value other) {
        if (other instanceof NumberValue) {
            value |= ((NumberValue) other).value;
        } else {
            // handle error
        }
    }

    @Override
    public void xor(Value other) {
        if (other instanceof NumberValue) {
            value ^= ((NumberValue) other).value;
        } else {
            // handle error
        }
    }

    @Override
    public void shl(Value other) {
        if (other instanceof NumberValue) {
            value <<= ((NumberValue) other).value;
        } else {
            // error
        }
    }

    @Override
    public void shr(Value other) {
        if (other instanceof NumberValue) {
            value >>= ((NumberValue) other).value;
        } else {
            // error
        }
    }

    @Override
    public void not() {
        value = ~value;
    }

    @Override
    public void neg() {
        value = -value;
    }

    @Override
    public boolean isTrue() {
        return value != 0;
    }

    @Override
    public void inc() {
        value++;
    }

    @Override
    public void dec() {
        value--;
    }

    @Override
    public boolean lt(Value other) {
        if (other instanceof NumberValue) {
            return value < ((NumberValue) other).value;
        } else {
            // error
        }

        return false;
    }

    @Override
    public boolean gt(Value other) {
        if (other instanceof NumberValue) {
            return value > ((NumberValue) other).value;
        } else {
            // error
        }

        return false;
    }

    @Override
    public boolean lte(Value other) {
        if (other instanceof NumberValue) {
            return value <= ((NumberValue) other).value;
        } else {
            // error
        }

        return false;
    }

    @Override
    public boolean gte(Value other) {
        if (other instanceof NumberValue) {
            return value >= ((NumberValue) other).value;
        } else {
            // error
        }

        return false;
    }

    @Override
    public Value clone() {
        return new NumberValue(typeByte, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumberValue that = (NumberValue) o;

        if (typeByte != that.typeByte) return false;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        int result = typeByte;
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
