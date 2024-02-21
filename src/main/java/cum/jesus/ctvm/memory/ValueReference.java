package cum.jesus.ctvm.memory;

import cum.jesus.ctvm.VM;
import cum.jesus.ctvm.value.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * A reference to a point in virtual memory.
 * @see cum.jesus.ctvm.memory.MemoryManager
 */
public final class ValueReference {
    public final long address;
    public final boolean isOriginal;
    public Value value;

    public final boolean readable;
    public final boolean writable;
    public final boolean executable;

    public long ownerAddress;
    public final List<ValueReference> relatives;

    public ValueReference(long address, boolean isOriginal, long ownerAddress, boolean readable, boolean writable, boolean executable) {
        this.address = address;
        this.isOriginal = isOriginal;
        if (isOriginal) {
            this.ownerAddress = -1;
        } else {
            this.ownerAddress = ownerAddress;
        }
        this.value = VM.ZERO;
        this.readable = readable;
        this.writable = writable;
        this.executable = executable;
        this.relatives = new ArrayList<>();
    }

    public ValueReference(long address, boolean isOriginal, long ownerAddress) {
        this(address, isOriginal, ownerAddress, true, true, false);
    }
}
