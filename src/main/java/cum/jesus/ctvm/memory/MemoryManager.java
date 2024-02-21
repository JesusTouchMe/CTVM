package cum.jesus.ctvm.memory;

import cum.jesus.ctvm.VM;
import cum.jesus.ctvm.util.Range;
import cum.jesus.ctvm.value.Value;

import java.util.HashMap;
import java.util.Map;

public final class MemoryManager {
    public static final Range<Long> stackRange = new Range<>(1L, 116509L);

    private final VM vm;
    private final Map<Long, ValueReference> memory;

    public MemoryManager(VM vm) {
        this.vm = vm;
        this.memory = new HashMap<>();
    }

    private long addressOffset = stackRange.to() + 250000;
    private long getUnusedAddress() {
        while (memory.containsKey(addressOffset)) {
            addressOffset += 65355;
        }
        long address = addressOffset;
        addressOffset += 65355;
        return address;
    }

    private static final int SAFETY_FREE = 0x01;
    private static final int SAFETY_READ = 0x02;
    private static final int SAFETY_WRITE = 0x04;
    private static final int SAFETY_MUST_EXIST = 0x08;

    /**
     * Performs several safety checks on an address
     * @param address The address to check
     * @param flags The flags for how it should check
     * @return True if the address is good, otherwise false
     */
    private boolean safetyCheck(long address, int flags) {
        if (address == 0) {
            return false;
        }

        ValueReference reference = memory.get(address);
        if ((flags & SAFETY_FREE) != 0) {
            if (stackRange.contains(address)) {
                return false;
            }

            if (reference == null || !reference.isOriginal) {
                return false;
            }
        }

        if ((flags & SAFETY_READ) != 0) {
            if (stackRange.contains(address)) {
                return true;
            }

            if (reference == null || !reference.readable) {
                return false;
            }
        }

        if ((flags & SAFETY_WRITE) != 0) {
            if (stackRange.contains(address)) {
                return true;
            }

            if ((flags & SAFETY_MUST_EXIST) != 0 && reference == null) {
                return false;
            }

            if (reference != null && !reference.writable) {
                return false;
            }
        }

        return true;
    }

    public long alloc() {
        long address = getUnusedAddress();
        if (!safetyCheck(address, 0)) {
            System.out.println("TODO: errors");
            return 0;
        }

        memory.put(address, new ValueReference(address, true, -1));
        return address;
    }

    private void freeInternal(ValueReference reference) {
        for (ValueReference relative : reference.relatives) {
            freeInternal(relative);
        }
        memory.remove(reference.address);
    }

    public void free(long address) {
        if (!safetyCheck(address, SAFETY_FREE)) {
            System.out.println("TODO: errors");
            return;
        }

        ValueReference reference = memory.get(address);

        freeInternal(reference);
    }

    public Value load(long address, short offset) {
        if (!safetyCheck(address, SAFETY_READ) || !safetyCheck(address + offset, SAFETY_READ)) {
            System.out.println("TODO: errors");
            return null;
        }

        if (stackRange.contains(address + offset)) {
            Value value = vm.stack.get((int) address + offset);
            if (value == null) {
                vm.stack.set((int) address + offset, VM.ZERO);
                value = VM.ZERO;
            }
            return value.clone();
        }

        long realAddress = address + offset;
        ValueReference reference = memory.get(realAddress);
        return reference.value.clone();
    }

    public void store(long address, short offset, Value value) {
        if (!safetyCheck(address, SAFETY_WRITE | SAFETY_MUST_EXIST) || !safetyCheck(address + offset, SAFETY_WRITE)) {
            System.out.println("TODO: errors");
            return;
        }

        if (stackRange.contains(address + offset)) {
            vm.stack.set((int) address + offset, value.clone());
            return;
        }

        long realAddress = address + offset;
        ValueReference addressReference = memory.get(address);
        ValueReference reference = memory.computeIfAbsent(realAddress, a -> new ValueReference(a, false, addressReference.ownerAddress));
        reference.value = value.clone();

        addressReference.relatives.add(reference);
    }
}
