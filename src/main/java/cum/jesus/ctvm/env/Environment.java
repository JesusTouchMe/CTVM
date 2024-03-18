package cum.jesus.ctvm.env;

import cum.jesus.ctni.DiagnosticReport;
import cum.jesus.ctni.Handle;
import cum.jesus.ctni.IEnvironment;
import cum.jesus.ctni.exception.BadHandleException;
import cum.jesus.ctni.exception.SecurityException;
import cum.jesus.ctvm.VM;
import cum.jesus.ctvm.data.Register;
import cum.jesus.ctvm.module.LocalSymbol;
import cum.jesus.ctvm.module.Module;
import cum.jesus.ctvm.util.QuinConsumer;
import cum.jesus.ctvm.util.Types;
import cum.jesus.ctvm.value.*;

import java.util.List;

public final class Environment implements IEnvironment {
    private final VM vm;
    private final Module module;

    public Environment(VM vm, Module module) {
        this.vm = vm;
        this.module = module;
    }

    /**
     * Wraps java type arguments to ct and places them correctly in the vm
     *
     * @param args java type arguments
     */
    private void wrapJavaToCtArgs(Object[] args) {
        int[] registers = { Register.regC, Register.regD, Register.regF, Register.regG };

        for (int i = 0; i < Math.min(registers.length, args.length); i++) {
            vm.getRegister(registers[i]).setValueNoClone(Value.fromJavaObject(args[i]));
        }

        if (args.length > registers.length) {
            for (int i = args.length - 1; i >= registers.length; i--) {
                vm.push(Value.fromJavaObject(args[i]));
            }
        }
    }
    
    private void wrapJavaToCtArgs(List<Object> args) {
        wrapJavaToCtArgs(args.toArray());
    }

    @Override
    public Handle GetModule(String name) {
        return vm.getModule(name);
    }

    @Override
    public Handle GetModule() {
        return module;
    }

    @Override
    public DiagnosticReport PreviewLatestError() {
        return null;
    }

    @Override
    public DiagnosticReport GetLatestError() {
        return null;
    }

    @Override
    public DiagnosticReport[] GetAllErrors() {
        return new DiagnosticReport[0];
    }

    @Override
    public Handle GetFunction(Handle module, String name) throws BadHandleException {
        if (module instanceof Module) {
            return ((Module) module).getFunction(name);
        } else if (module instanceof ModuleHandleValue) {
            return ((ModuleHandleValue) module).module.getFunction(name);
        }

        throw new BadHandleException("Required handle to module, got a handle to " + module.getClass().getSimpleName());
    }

    @Override
    public Handle GetFunction(String name) {
        return module.getFunction(name);
    }

    @Override
    public void CallVoidFunction(Handle function, Object... args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }
        
        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);
    }

    @Override
    public void CallVoidFunctionA(Handle function, Object[] args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);
    }

    @Override
    public void CallVoidFunctionL(Handle function, List<Object> args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);
    }

    @Override
    public byte CallByteFunction(Handle function, Object... args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getByte();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public byte CallByteFunctionA(Handle function, Object[] args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getByte();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public byte CallByteFunctionL(Handle function, List<Object> args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getByte();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public short CallShortFunction(Handle function, Object... args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getShort();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public short CallShortFunctionA(Handle function, Object[] args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getShort();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public short CallShortFunctionL(Handle function, List<Object> args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getShort();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public int CallIntFunction(Handle function, Object... args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getInt();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public int CallIntFunctionA(Handle function, Object[] args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getInt();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public int CallIntFunctionL(Handle function, List<Object> args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getInt();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public long CallLongFunction(Handle function, Object... args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getLong();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public long CallLongFunctionA(Handle function, Object[] args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getLong();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public long CallLongFunctionL(Handle function, List<Object> args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        if (returnValue instanceof NumberValue) {
            return returnValue.asNumber().getLong();
        }

        //TODO: diagnostics error
        return 0;
    }

    @Override
    public String CallStringFunction(Handle function, Object... args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        return returnValue.toString();
    }

    @Override
    public String CallStringFunctionA(Handle function, Object[] args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        return returnValue.toString();
    }

    @Override
    public String CallStringFunctionL(Handle function, List<Object> args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        Value returnValue = vm.getRegister(Register.regE).getValueNoClone();

        return returnValue.toString();
    }

    @Override
    public Handle CallHandleFunction(Handle function, Object... args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        return vm.getRegister(Register.regE).getValueNoClone();
    }

    @Override
    public Handle CallHandleFunctionA(Handle function, Object[] args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        return vm.getRegister(Register.regE).getValueNoClone();
    }

    @Override
    public Handle CallHandleFunctionL(Handle function, List<Object> args) throws BadHandleException {
        LocalSymbol symbol = null;

        if (function instanceof LocalSymbol) {
            symbol = (LocalSymbol) function;
        } else if (function instanceof FunctionHandleValue) {
            symbol = ((FunctionHandleValue) function).function;
        } else {
            throw new BadHandleException("Required handle to function, got a handle to " + function.getClass().getSimpleName());
        }

        wrapJavaToCtArgs(args);

        vm.callFunction(symbol, true);

        return vm.getRegister(Register.regE).getValueNoClone();
    }

    @Override
    public Handle CallInterruptFunction(int id, byte byte1, byte byte2, byte byte3, Object... args) {
        QuinConsumer<VM, Module, Byte, Byte, Byte> callback = vm.interruptCallbacks.get((byte) id);

        if (callback != null) {
            wrapJavaToCtArgs(args);

            callback.accept(vm, module, byte1, byte2, byte3);

            return vm.getRegister(Register.regE).getValueNoClone();
        }

        return null;
    }

    @Override
    public boolean IsByte(Handle handle) {
        return handle instanceof NumberValue && ((NumberValue) handle).typeByte == Types.BYTE.typeCode;
    }

    @Override
    public boolean IsShort(Handle handle) {
        return handle instanceof NumberValue && ((NumberValue) handle).typeByte == Types.SHORT.typeCode;
    }

    @Override
    public boolean IsInt(Handle handle) {
        return handle instanceof NumberValue && ((NumberValue) handle).typeByte == Types.INT.typeCode;
    }

    @Override
    public boolean IsLong(Handle handle) {
        return handle instanceof NumberValue && ((NumberValue) handle).typeByte == Types.LONG.typeCode;
    }

    @Override
    public boolean IsNumber(Handle handle) {
        return handle instanceof NumberValue;
    }

    @Override
    public boolean IsString(Handle handle) {
        return handle instanceof StringValue;
    }

    @Override
    public boolean IsModule(Handle handle) {
        return handle instanceof Module || handle instanceof ModuleHandleValue;
    }

    @Override
    public boolean IsFunction(Handle handle) {
        return handle instanceof LocalSymbol || handle instanceof FunctionHandleValue;
    }

    @Override
    public byte GetByteFromHandle(Handle handle, boolean strictType) throws BadHandleException {
        if (handle instanceof NumberValue) {
            if (!strictType) {
                return ((NumberValue) handle).getByte();
            } else {
                if (((NumberValue) handle).typeByte == Types.BYTE.typeCode) {
                    return ((NumberValue) handle).getByte();
                } else {
                    throw new BadHandleException("The handle pointed to a number, but strict typing was enabled and the number was not of type byte");
                }
            }
        }

        throw new BadHandleException("Handle does not point to a valid number");
    }

    @Override
    public short GetShortFromHandle(Handle handle, boolean strictType) throws BadHandleException {
        if (handle instanceof NumberValue) {
            if (!strictType) {
                return ((NumberValue) handle).getShort();
            } else {
                if (((NumberValue) handle).typeByte == Types.SHORT.typeCode) {
                    return ((NumberValue) handle).getShort();
                } else {
                    throw new BadHandleException("The handle pointed to a number, but strict typing was enabled and the number was not of type short");
                }
            }
        }

        throw new BadHandleException("Handle does not point to a valid number");
    }

    @Override
    public int GetIntFromHandle(Handle handle, boolean strictType) throws BadHandleException {
        if (handle instanceof NumberValue) {
            if (!strictType) {
                return ((NumberValue) handle).getInt();
            } else {
                if (((NumberValue) handle).typeByte == Types.INT.typeCode) {
                    return ((NumberValue) handle).getInt();
                } else {
                    throw new BadHandleException("The handle pointed to a number, but strict typing was enabled and the number was not of type int");
                }
            }
        }

        throw new BadHandleException("Handle does not point to a valid number");
    }

    @Override
    public long GetLongFromHandle(Handle handle, boolean strictType) throws BadHandleException {
        if (handle instanceof NumberValue) {
            if (!strictType) {
                return ((NumberValue) handle).getLong();
            } else {
                if (((NumberValue) handle).typeByte == Types.LONG.typeCode) {
                    return ((NumberValue) handle).getLong();
                } else {
                    throw new BadHandleException("The handle pointed to a number, but strict typing was enabled and the number was not of type long");
                }
            }
        }

        throw new BadHandleException("Handle does not point to a valid number");
    }

    @Override
    public String GetStringFromHandle(Handle handle) throws BadHandleException {
        if (handle instanceof StringValue) {
            return ((StringValue) handle).getJavaString();
        }

        throw new BadHandleException("Handle does not point to a valid string");
    }

    @Override
    public Handle NewByte(byte b) {
        return new NumberValue(Types.BYTE.typeCode, b);
    }

    @Override
    public Handle NewShort(short s) {
        return new NumberValue(Types.SHORT.typeCode, s);
    }

    @Override
    public Handle NewInt(int i) {
        return new NumberValue(Types.INT.typeCode, i);
    }

    @Override
    public Handle NewLong(long l) {
        return new NumberValue(Types.LONG.typeCode, l);
    }

    @Override
    public Handle NewString(String s) {
        return new StringValue(s);
    }

    @Override
    public Handle Alloc(int i) {
        return null;
    }

    @Override
    public Handle ReAlloc(Handle handle, int i) {
        return null;
    }

    @Override
    public void Free(Handle handle) {

    }

    @Override
    public Handle Read(Handle handle, int i) throws BadHandleException, SecurityException {
        return null;
    }

    @Override
    public void Write(Handle handle, int i, Handle handle1) throws BadHandleException, SecurityException {

    }
}
