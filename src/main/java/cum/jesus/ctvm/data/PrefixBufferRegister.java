package cum.jesus.ctvm.data;

import cum.jesus.ctvm.value.Value;

import java.util.Stack;

public final class PrefixBufferRegister extends Register {
    private final Stack<Value> values = new Stack<>();

    public void addValue(Value value) {
        values.push(value);
    }

    @Override
    public Value getValue() {
        return values.pop();
    }

    @Override
    public Value getValueNoClone() {
        return values.pop();
    }

    @Override
    public void setValue(Value value) {

    }

    @Override
    public void setValueNoClone(Value value) {

    }

    @Override
    public Register clone() {
        PrefixBufferRegister newRegister = new PrefixBufferRegister();
        for (Value value : values) {
            newRegister.values.push(value.clone());
        }
        return newRegister;
    }
}
