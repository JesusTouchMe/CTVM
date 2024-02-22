package cum.jesus.ctvm;

import cum.jesus.ctvm.bytecode.ByteCode;
import cum.jesus.ctvm.data.PrefixBufferRegister;
import cum.jesus.ctvm.data.Register;
import cum.jesus.ctvm.data.ValueStack;
import cum.jesus.ctvm.executor.Executor;
import cum.jesus.ctvm.memory.MemoryManager;
import cum.jesus.ctvm.module.LocalSymbol;
import cum.jesus.ctvm.module.Module;
import cum.jesus.ctvm.util.QuadConsumer;
import cum.jesus.ctvm.value.NumberValue;
import cum.jesus.ctvm.value.Value;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class VM {
    public static final Value ZERO = new NumberValue(Value.TYPE_BYTE, 0);

    private Map<String, Module> modules;

    private boolean prepared = false;

    private Map<String, LocalSymbol> globalFunctions;
    private List<Value> globalConstPool;

    private Executor mainExecutor;

    private final Register[] registers;
    public final ValueStack stack;

    public final MemoryManager memoryManager;

    public final Map<Byte, QuadConsumer<VM, Byte, Byte, Byte>> interruptCallbacks;

    public VM() {
        registers = new Register[11];
        stack = new ValueStack();
        memoryManager = new MemoryManager(this);

        interruptCallbacks = new HashMap<>();
        modules = new HashMap<>();
    }

    public int getVersion() {
        return modules.values().toArray(new Module[0])[0].getVersion();
    }

    public int getLineNumber(String module) {
        Module mod;
        return (mod = modules.get(module)) != null ? mod.getLineNumber() : -1;
    }

    public void incLineNumber(String module) {
        Module mod = modules.get(module);
        if (mod != null) {
            mod.setLineNumber(mod.getLineNumber() + 1);
        }
    }

    public ByteCode getDataSection(String module) {
        Module mod;
        return (mod = modules.get(module)) != null ? mod.getDataSection() : null;
    }

    public ByteCode getCodeSection(String module) {
        Module mod;
        return (mod = modules.get(module)) != null ? mod.getCodeSection() : null;
    }

    public VM addModule(Module module) {
        modules.put(module.getName(), module);
        return this;
    }

    public void prepare(Executor mainExecutor) {
        if (prepared) {
            return;
        }

        ExecutorService moduleService = Executors.newFixedThreadPool(4);
        globalConstPool = new ArrayList<>();
        globalFunctions = new HashMap<>();

        for (Module module : modules.values()) {
            moduleService.submit(() -> {
                module.prepare();
                for (Value value : module.getConstPool()) {
                    globalConstPool.add(value);
                }
                for (Map.Entry<String, Integer> symbol : module.getFunctions().entrySet()) {
                    globalFunctions.put(symbol.getKey(), new LocalSymbol(module, symbol.getKey(), symbol.getValue()));
                }
            });
        }

        this.mainExecutor = mainExecutor;

        for (int i = 1; i < registers.length; i++) {
            registers[i] = new Register();
            registers[i].setValue(ZERO);
        }

        registers[0] = new PrefixBufferRegister();

        interruptCallbacks.put((byte) 0x01, InterruptCallbacks::exit);
        interruptCallbacks.put((byte) 0x02, InterruptCallbacks::printvm);
        interruptCallbacks.put((byte) 0x03, InterruptCallbacks::gc);
        interruptCallbacks.put((byte) 0x04, InterruptCallbacks::write);
        interruptCallbacks.put((byte) 0x05, InterruptCallbacks::writei);
        interruptCallbacks.put((byte) 0x06, InterruptCallbacks::writef);
        interruptCallbacks.put((byte) 0x07, InterruptCallbacks::getline);
        interruptCallbacks.put((byte) 0x84, InterruptCallbacks::freestr);

        moduleService.shutdown();
        try {
            moduleService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int version = -1;
        for (Module module : modules.values()) {
            if (version == -1) {
                version = module.getVersion();
            } else {
                if (module.getVersion() != version) {
                    throw new RuntimeException("TODO: be sophisticated");
                }
            }
        }

        LocalSymbol startSymbol = globalFunctions.get(".start");
        if (startSymbol == null) {
            //TODO: handle error
            throw new RuntimeException();
        }

        mainExecutor.setVM(this);
        mainExecutor.setModule(startSymbol.module);
        mainExecutor.setPos(startSymbol.location);

        prepared = true;
    }

    public void start() {
        mainExecutor.start();
    }

    public void resumeExecution() {
        mainExecutor.start();
    }

    public void pauseExecution() {
        mainExecutor.stop();
    }

    public Module getModule(String name) {
        return modules.get(name);
    }

    public Value getConstant(int index, String module) {
        Module mod = getModule(module);
        if (mod == null) {
            return null;
        }

        return mod.getConstant(index);
    }

    public Value getConstant(int index) {
        return globalConstPool.get(index);
    }

    public void setConstant(int index, Value newValue, String module) {
        Module mod = getModule(module);
        if (mod == null) {
            return;
        }

        mod.setConstant(index, newValue); //TODO: sync up global and local constants
    }

    public void setConstant(int index, Value newValue) {
        globalConstPool.set(index, newValue);
    }

    public LocalSymbol getSymbol(String name) {
        return globalFunctions.getOrDefault(name, new LocalSymbol(null, null, -1));
    }

    public Register getRegister(int id) {
        return registers[id];
    }

    public PrefixBufferRegister getPrefixBuffer() {
        return (PrefixBufferRegister) registers[0];
    }

    /**
     * Will return an array of registers that contains all the registers that need to be saved for a call according to the standard. <br>
     * Registers are ordered as follows: {regA, regB, regC, regD, regF, regG, regH}
     * @return An array of important registers
     */
    public Register[] saveRegisters() {
        return new Register[] {
                registers[Register.regA].clone(),
                registers[Register.regB].clone(),
                registers[Register.regC].clone(),
                registers[Register.regD].clone(),
                registers[Register.regF].clone(),
                registers[Register.regG].clone(),
                registers[Register.regH].clone(),
        };
    }

    /**
     * @see VM#saveRegisters()
     */
    public void restoreRegisters(Register[] savedRegisters) {
        assert savedRegisters.length == 7;
        registers[Register.regA] = savedRegisters[0];
        registers[Register.regB] = savedRegisters[1];
        registers[Register.regC] = savedRegisters[2];
        registers[Register.regD] = savedRegisters[3];
        registers[Register.regF] = savedRegisters[4];
        registers[Register.regG] = savedRegisters[5];
        registers[Register.regH] = savedRegisters[6];
    }

    public void push(Value val) {
        registers[Register.regStackTop].getValueNoClone().inc();
        stack.set(registers[Register.regStackTop].getValueNoClone().asNumber().getInt(), val.clone());
    }

    /**
     * Removes the value at the top of the stack and returns that it
     * @return The previous top value
     */
    public Value pop() {
        Value val = stack.getAndRemove(registers[Register.regStackTop].getValueNoClone().asNumber().getInt());
        registers[Register.regStackTop].getValueNoClone().dec();
        return val;
    }
}
