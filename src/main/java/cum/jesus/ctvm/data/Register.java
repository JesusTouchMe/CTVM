package cum.jesus.ctvm.data;

import cum.jesus.ctvm.value.Value;

public class Register {
    public static final int prefixBuf = 0;
    public static final int regA = 1;
    public static final int regB = 2;
    public static final int regC = 3;
    public static final int regD = 4;
    public static final int regE = 5;
    public static final int regF = 6;
    public static final int regG = 7;
    public static final int regH = 8;
    public static final int regStackBase = 9;
    public static final int regStackTop = 10;

    private Value value = null;

    public Value getValue() {
        return value.clone();
    }

    public Value getValueNoClone() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value.clone();
    }

    public void setValueNoClone(Value value) {
        this.value = value;
    }

    @Override
    public Register clone() {
        Register newRegister = new Register();
        newRegister.setValue(value);
        return newRegister;
    }
}
