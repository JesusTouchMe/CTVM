package cum.jesus.ctvm.executor;

import cum.jesus.ctni.NativeFunction;
import cum.jesus.ctvm.VM;
import cum.jesus.ctvm.bytecode.ByteCode;
import cum.jesus.ctvm.data.Register;
import cum.jesus.ctvm.module.LocalSymbol;
import cum.jesus.ctvm.module.Module;
import cum.jesus.ctvm.util.Opcodes;
import cum.jesus.ctvm.value.*;

import java.util.Stack;

import static cum.jesus.ctvm.util.ConstantOpcodes.*;

public final class SynchronousCodeExecutor implements Executor {
    private VM vm = null;
    private Module currentModule = null;
    private int pos = 0;

    /**
     * since the implementation of a call stack isn't specified in the standard, just use simple java stack.
     * the standard only specified saving some registers
     */
    private final Stack<CallFrame> callStack;

    /**
     * Simple check if the executor is supposed to be interrupted. If true, it won't run before start has been called
     */
    private boolean interrupted = false;

    /**
     * Just some nice stats
     */
    private long instructionCount = 0;

    public SynchronousCodeExecutor() {
        this.callStack = new Stack<>();
    }

    /**
     * Retrieves the current byte and increments the position
     * @return The current byte in the bytecode
     */
    private byte getOpcode() {
        return currentModule.getCodeSection().get(pos++);
    }

    /**
     * Retrieves the default operands for an instruction. Only use if certain it's a normal instruction
     * @return The next 4 bytes in the bytecode
     */
    private byte[] getOperands() {
        ByteCode codeSection = currentModule.getCodeSection();
        return new byte[] {
                codeSection.get(pos++),
                codeSection.get(pos++),
                codeSection.get(pos++),
                codeSection.get(pos++),
        };
    }

    /**
     * Retrieves an amount of operands from the bytecode
     * @param amount The amount of operand bytes to get
     * @return The operands in a new array
     */
    private byte[] getOperands(int amount) {
        ByteCode codeSection = currentModule.getCodeSection();
        byte[] operands = new byte[amount];
        for (int i = 0; i < amount; i++) {
            operands[i] = codeSection.get(pos++);
        }
        return operands;
    }

    private byte getNext() {
        return currentModule.getCodeSection().get(pos++);
    }

    @Override
    public void setVM(VM vm) {
        this.vm = vm;
    }

    @Override
    public void setModule(Module module) {
        currentModule = module;
    }

    @Override
    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public void cycle() {
        byte opcode = getOpcode();
        instructionCount++;

        if ((opcode & PREFIX_MASK) == PREFIX_COMPARE) { // prefix. special requirements
            Opcodes specialPrefix = Opcodes.getPrefix(opcode);
            assert specialPrefix != null;

            byte[] operands = getOperands(specialPrefix.codeLength - 1);

            switch (specialPrefix) {
                case IMM8: {
                    vm.getPrefixBuffer().addValue(new NumberValue(operands[0], operands[1]));
                } break;

                case IMM16: {
                    vm.getPrefixBuffer().addValue(new NumberValue(operands[0], ((operands[1] & 0xFF) << 8) | (operands[2] & 0xFF)));
                } break;

                case IMM32: {
                    vm.getPrefixBuffer().addValue(new NumberValue(operands[0], ((operands[1] & 0xFF) << 24) | ((operands[2] & 0xFF) << 16) | ((operands[3] & 0xFF) << 8) | (operands[4] & 0xFF)));
                } break;

                case IMM64: {
                    vm.getPrefixBuffer().addValue(new NumberValue(operands[0], ((long) (operands[1] & 0xFF) << 56) | ((long) (operands[2] & 0xFF) << 48) | ((long) (operands[3] & 0xFF) << 40) | ((long) (operands[4] & 0xFF) << 32) | ((long) (operands[5] & 0xFF) << 24) | ((long) (operands[6] & 0xFF) << 16) | ((long) (operands[7] & 0xFF) << 8) | (long) (operands[8] & 0xFF)));
                } break;

                case IMMS: {
                    int length = ((operands[0] & 0xFF) << 8) | (operands[1] & 0xFF);
                    StringBuilder sb = new StringBuilder(length);
                    for (int i = 0; i < length; i++) {
                        sb.append((char) getNext());
                    }
                    vm.getPrefixBuffer().addValue(new StringValue(sb.toString()));
                } break;

                case PPOP: {
                    vm.getPrefixBuffer().addValue(vm.pop());
                } break;

                case PMEMI: {
                    vm.getPrefixBuffer().addValue(vm.memoryManager.load(((long) (operands[0] & 0xFF) << 24) | ((operands[1] & 0xFF) << 16) | ((operands[2] & 0xFF) << 8) | (operands[3] & 0xFF), (short) 0));
                } break;

                case PMEM: {
                    vm.getPrefixBuffer().addValue(vm.memoryManager.load(vm.getRegister(operands[0]).getValueNoClone().asNumber().getLong(), (short) (((operands[1] & 0xFF) << 8) | (operands[2] & 0xFF))));
                } break;

                case CENT: {
                    vm.getPrefixBuffer().addValue(currentModule.getConstant((operands[0] & 0xFF << 8) | (operands[1] & 0xFF)));
                } break;

                default: {
                    System.out.println("bad prefix???");
                } break;
            }
        } else { // normal
            byte[] operands = getOperands();

            switch (opcode) {
                case NOP: {
                } break;

                case NEWL: {
                    currentModule.setLineNumber(currentModule.getLineNumber() + 1);
                } break;

                case PUSH: {
                    vm.push(vm.getRegister(operands[0]).getValue());
                } break;

                case POP: {
                    vm.getRegister(operands[0]).setValue(vm.pop());
                } break;

                case DUP: {
                    Value tos = vm.stack.get(vm.getRegister(Register.regStackTop).getValueNoClone().asNumber().getInt());
                    vm.push(tos);
                } break;

                case ALCA: {
                    Value amount = new NumberValue(Value.TYPE_SHORT, ((operands[0] & 0xFF) << 8) | (operands[1] & 0xFF));
                    vm.getRegister(Register.regStackTop).getValueNoClone().add(amount);
                } break;

                case FREA: {
                    Value amount = new NumberValue(Value.TYPE_SHORT, ((operands[0] & 0xFF) << 8) | (operands[1] & 0xFF));
                    vm.getRegister(Register.regStackTop).getValueNoClone().sub(amount);
                } break;

                case MOV: {
                    Register dest = vm.getRegister(operands[0]);
                    dest.setValue(vm.getRegister(operands[1]).getValueNoClone()); // we don't clone here because setValue clones it, and it would be a waste of memory
                } break;

                case MOVZ: {
                    Register dest = vm.getRegister(operands[0]);
                    Register src = vm.getRegister(operands[1]);
                    dest.setValue(src.getValueNoClone()); // we don't clone here because setValue clones it, and it would be a waste of memory
                    src.setValue(VM.ZERO);
                } break;

                case ALC: {
                    vm.getRegister(operands[0]).setValueNoClone(new NumberValue(Value.TYPE_LONG, vm.memoryManager.alloc()));
                } break;

                case FRE: {
                    vm.memoryManager.free(vm.getRegister(operands[0]).getValueNoClone().asNumber().getLong());
                } break;

                case LOD: {
                    Register dest = vm.getRegister(operands[0]);
                    Register src = vm.getRegister(operands[1]);
                    dest.setValueNoClone(vm.memoryManager.load(src.getValueNoClone().asNumber().getLong(), (short) (((operands[2] & 0xFF) << 8) | (operands[3] & 0xFF))));
                } break;

                case STR: {
                    Register dest = vm.getRegister(operands[0]);
                    Register src = vm.getRegister(operands[3]);
                    vm.memoryManager.store(dest.getValueNoClone().asNumber().getLong(), (short) (((operands[1] & 0xFF) << 8) | (operands[2] & 0xFF)), src.getValueNoClone());
                } break;

                case ADD: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.add(vm.getRegister(operands[2]).getValue());
                    dest.setValueNoClone(result);
                } break;

                case SUB: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.sub(vm.getRegister(operands[2]).getValue());
                    dest.setValueNoClone(result);
                } break;

                case MUL: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.mul(vm.getRegister(operands[2]).getValue());
                    dest.setValueNoClone(result);
                } break;

                case DIV: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.div(vm.getRegister(operands[2]).getValue());
                    dest.setValueNoClone(result);
                } break;

                case AND: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.and(vm.getRegister(operands[2]).getValue());
                    dest.setValueNoClone(result);
                } break;

                case OR: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.or(vm.getRegister(operands[2]).getValue());
                    dest.setValueNoClone(result);
                } break;

                case XOR: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.xor(vm.getRegister(operands[2]).getValue());
                    dest.setValueNoClone(result);
                } break;

                case SHL: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.shl(vm.getRegister(operands[2]).getValue());
                    dest.setValueNoClone(result);
                } break;

                case SHR: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.shr(vm.getRegister(operands[2]).getValue());
                    dest.setValueNoClone(result);
                } break;

                case LAND: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = new NumberValue(Value.TYPE_BYTE, vm.getRegister(operands[1]).getValueNoClone().isTrue() && vm.getRegister(operands[2]).getValueNoClone().isTrue());
                    dest.setValueNoClone(result);
                } break;

                case LOR: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = new NumberValue(Value.TYPE_BYTE, vm.getRegister(operands[1]).getValueNoClone().isTrue() || vm.getRegister(operands[2]).getValueNoClone().isTrue());
                    dest.setValueNoClone(result);
                } break;

                case LXOR: {
                    Register dest = vm.getRegister(operands[0]);
                    Value result = new NumberValue(Value.TYPE_BYTE, vm.getRegister(operands[1]).getValueNoClone().isTrue() ^ vm.getRegister(operands[2]).getValueNoClone().isTrue());
                    dest.setValueNoClone(result);
                } break;

                case INC: {
                    vm.getRegister(operands[0]).getValueNoClone().inc();
                } break;

                case DEC: {
                    vm.getRegister(operands[0]).getValueNoClone().dec();
                } break;

                case NOT: {
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.not();
                    vm.getRegister(operands[0]).setValueNoClone(result);
                } break;

                case NEG: {
                    Value result = vm.getRegister(operands[1]).getValue();
                    result.neg();
                    vm.getRegister(operands[0]).setValueNoClone(result);
                } break;

                case LNOT: {
                    Value result = new NumberValue(Value.TYPE_BYTE, !vm.getRegister(operands[1]).getValueNoClone().isTrue());
                    vm.getRegister(operands[0]).setValueNoClone(result);
                } break;

                case CMPEQ: {
                    Value result = new NumberValue(Value.TYPE_BYTE, vm.getRegister(operands[1]).getValueNoClone().equals(vm.getRegister(operands[2]).getValueNoClone()));
                    vm.getRegister(operands[0]).setValueNoClone(result);
                } break;

                case CMPNE: {
                    Value result = new NumberValue(Value.TYPE_BYTE, !vm.getRegister(operands[1]).getValueNoClone().equals(vm.getRegister(operands[2]).getValueNoClone()));
                    vm.getRegister(operands[0]).setValueNoClone(result);
                } break;

                case CMPLT: {
                    Value result = new NumberValue(Value.TYPE_BYTE, vm.getRegister(operands[1]).getValueNoClone().lt(vm.getRegister(operands[2]).getValueNoClone()));
                    vm.getRegister(operands[0]).setValueNoClone(result);
                } break;

                case CMPGT: {
                    Value result = new NumberValue(Value.TYPE_BYTE, vm.getRegister(operands[1]).getValueNoClone().gt(vm.getRegister(operands[2]).getValueNoClone()));
                    vm.getRegister(operands[0]).setValueNoClone(result);
                } break;

                case CMPLTE: {
                    Value result = new NumberValue(Value.TYPE_BYTE, vm.getRegister(operands[1]).getValueNoClone().lte(vm.getRegister(operands[2]).getValueNoClone()));
                    vm.getRegister(operands[0]).setValueNoClone(result);
                } break;

                case CMPGTE: {
                    Value result = new NumberValue(Value.TYPE_BYTE, vm.getRegister(operands[1]).getValueNoClone().gte(vm.getRegister(operands[2]).getValueNoClone()));
                    vm.getRegister(operands[0]).setValueNoClone(result);
                } break;

                case JMP: {
                    pos += ((operands[0] & 0xFF) << 8) | (operands[1] & 0xFF);
                } break;

                case JMV: {
                    pos += vm.getRegister(operands[0]).getValueNoClone().asNumber().getInt();
                } break;

                case JIT: {
                    if (vm.getRegister(operands[0]).getValueNoClone().isTrue()) {
                        pos += ((operands[1] & 0xFF) << 8) | (operands[2] & 0xFF);
                    }
                } break;

                case JVT: {
                    if (vm.getRegister(operands[0]).getValueNoClone().isTrue()) {
                        pos += vm.getRegister(operands[1]).getValueNoClone().asNumber().getInt();
                    }
                } break;

                case JIZ: {
                    if (!vm.getRegister(operands[0]).getValueNoClone().isTrue()) {
                        pos += ((operands[1] & 0xFF) << 8) | (operands[2] & 0xFF);
                    }
                } break;

                case JVZ: {
                    if (!vm.getRegister(operands[0]).getValueNoClone().isTrue()) {
                        pos += vm.getRegister(operands[1]).getValueNoClone().asNumber().getInt();
                    }
                } break;

                case CALL: {
                    Value module = vm.getRegister(operands[0]).getValueNoClone();
                    Value function = vm.getRegister(operands[1]).getValueNoClone();

                    Module callModule = null;

                    if (module instanceof ModuleHandleValue) {
                        callModule = ((ModuleHandleValue) module).module;
                    } else if (module instanceof StringValue) {
                        callModule = vm.getModule(((StringValue) module).getJavaString());
                    }

                    if (callModule == null) {
                        //TODO: handle error
                        throw new RuntimeException();
                    }

                    if (function instanceof FunctionHandleValue) {
                        callFunction(((FunctionHandleValue) function).function, false);
                    } else if (function instanceof StringValue) {
                        LocalSymbol symbol = callModule.getFunction(((StringValue) function).getJavaString());
                        callFunction(symbol, false);
                    } else {
                        //TODO: handle error
                        throw new RuntimeException();
                    }

                } break;

                case RET: {
                    CallFrame frame = callStack.pop();
                    vm.restoreRegisters(frame.registers);
                    currentModule = frame.module;
                    pos = frame.position;
                    if (frame.stopAtReturn) {
                        stop();
                    }
                } break;

                case INT: {
                    stop();
                    vm.interruptCallbacks.get(operands[0]).accept(vm, currentModule, operands[1], operands[2], operands[3]);
                    startInternal();
                } break;

                case CLD: {
                    vm.getRegister(operands[0]).setValue(currentModule.getConstant(((operands[1] & 0xFF) << 8) | (operands[2] & 0xFF)));
                } break;

                case CST: {
                    currentModule.setConstant(((operands[0] & 0xFF) << 8) | (operands[1] & 0xFF), vm.getRegister(operands[2]).getValue());
                } break;

                case MOD: {
                    String name = vm.getRegister(operands[1]).getValueNoClone().asString().getJavaString();
                    vm.getRegister(operands[0]).setValueNoClone(new ModuleHandleValue(vm.getModule(name)));
                } break;

                case FUN: {
                    Value module = vm.getRegister(operands[1]).getValueNoClone();
                    String name = vm.getRegister(operands[2]).getValueNoClone().asString().getJavaString();

                    if (module instanceof ModuleHandleValue) {
                        LocalSymbol symbol = ((ModuleHandleValue) module).module.getFunction(name);

                        vm.getRegister(operands[0]).setValueNoClone(new FunctionHandleValue(symbol));
                    } else if (module instanceof StringValue) {
                        Module mod = vm.getModule(((StringValue) module).getJavaString());
                        LocalSymbol symbol = mod.getFunction(name);

                        vm.getRegister(operands[0]).setValueNoClone(new FunctionHandleValue(symbol));
                    }
                } break;

                default: {
                    System.out.println("bad instruction???");
                } break;
            }
        }
    }

    private void startInternal() {
        interrupted = false;
    }

    @Override
    public void start() {
        startInternal();
        while (!interrupted) {
            cycle();
        }
    }

    @Override
    public void stop() {
        interrupted = true;
    }

    @Override
    public void callFunction(LocalSymbol function, boolean stopAtReturn) {
        if (function.nativeFunction != null) {
            Object[] args = new Object[function.nativeFunction.argc()];

            int[] registers = { Register.regC, Register.regD, Register.regF, Register.regG };
            for (int i = 0; i < Math.min(registers.length, args.length); i++) {
                args[i] = vm.getRegister(registers[i]).getValueNoClone().toJavaObject();
            }

            if (args.length > registers.length) {
                int stackBase = vm.getRegister(Register.regStackBase).getValueNoClone().asNumber().getInt();
                for (int i = registers.length; i < args.length; i++) {
                    args[i] = vm.stack.get(stackBase - (i - registers.length)).toJavaObject();
                }
            }

            function.nativeFunction.call(function.module.env, args);

            if (stopAtReturn) {
                stop();
            }
        } else {
            int symbol = function.location;

            if (symbol == -1) {
                throw new RuntimeException(); //TODO: error
            }

            callStack.push(new CallFrame(currentModule, vm.saveRegisters(), pos, stopAtReturn));
            pos = symbol;

            if (interrupted) {
                start();
            }
        }
    }

    private final class CallFrame {
        Module module;
        Register[] registers;
        int position;
        boolean stopAtReturn;

        public CallFrame(Module module, Register[] registers, int position, boolean stopAtReturn) {
            this.module = module;
            this.registers = registers;
            this.position = position;
            this.stopAtReturn = stopAtReturn;
        }
    }
}
