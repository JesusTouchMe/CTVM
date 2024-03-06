package cum.jesus.ctvm.data;

import cum.jesus.ctvm.VM;
import cum.jesus.ctvm.memory.MemoryManager;
import cum.jesus.ctvm.value.Value;

public final class ValueStack {
    private final Value[] data;

    public int highestAddress = 0;

    public ValueStack() {
        this.data = new Value[(int) (MemoryManager.stackRange.to() - MemoryManager.stackRange.from() + 1)];
    }

    public Value get(int address) {
        if (address > highestAddress) {
            highestAddress = address;
        }
        return data[address];
    }

    /**
     * Should never be used due to retardation. Only for the INTERNAL workings of the vm.
     * <br> Use {@link cum.jesus.ctvm.data.ValueStack#get} instead
     */
    public Value getAndRemove(int address) {
        if (address > highestAddress) {
            highestAddress = address;
        }
        Value value = data[address];
        data[address] = VM.ZERO;
        return value;
    }

    public void set(int address, Value newValue) {
        if (address > highestAddress) {
            highestAddress = address;
        }
        data[address] = newValue;
    }
}
