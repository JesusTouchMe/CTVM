package cum.jesus.ctvm.module;

import cum.jesus.ctni.Handle;
import cum.jesus.ctni.IEnvironment;
import cum.jesus.ctni.NativeFunction;
import cum.jesus.ctvm.VM;
import cum.jesus.ctvm.bytecode.ByteCode;
import cum.jesus.ctvm.constant.ConstantPool;
import cum.jesus.ctvm.env.Environment;
import cum.jesus.ctvm.value.FunctionHandleValue;
import cum.jesus.ctvm.value.ModuleHandleValue;
import cum.jesus.ctvm.value.Value;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Module implements Handle {
    private VM vm;

    private String name;

    private int lineNumber = 0;
    private int version;

    private final byte[] fullByteCode;
    private ByteCode dataSection;
    private ByteCode codeSection;

    private Map<String, Integer> functions;
    private Map<String, NativeFunction> natives;
    private ConstantPool constPool;

    public final IEnvironment env;
    private LibraryLoader libraryLoader = null;

    public Module(VM vm, String name, byte[] fullByteCode) {
        this.vm = vm;
        this.name = name;
        this.fullByteCode = fullByteCode;
        this.natives = new HashMap<>();
        this.env = new Environment(vm, this);
    }

    public String getName() {
        return name;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int number) {
        lineNumber = number;
    }

    public int getVersion() {
        return version;
    }

    public ByteCode getDataSection() {
        return dataSection;
    }

    public ByteCode getCodeSection() {
        return codeSection;
    }

    public Map<String, Integer> getFunctions() {
        return functions;
    }

    public ConstantPool getConstPool() {
        return constPool;
    }

    public void prepare() {
        int index = 0;
        StringBuilder sb;

        functions = new HashMap<>();

        sb = new StringBuilder();
        while (fullByteCode[index] != 0) {
            sb.append((char) fullByteCode[index++]);
        }
        index++;
        name = sb.toString();

        if (fullByteCode[index] == 'v') {
            sb = new StringBuilder();
            index++;
            while (fullByteCode[index] != 0) {
                sb.append((char) fullByteCode[index++]);
            }
            index++;
            version = Integer.parseInt(sb.toString());
        }

        if (fullByteCode[index] != 'F' && fullByteCode[index + 1] != ' ') {
            throw new RuntimeException("error F ");
        }
        index += 2;

        while (fullByteCode[index] != 0 || fullByteCode[index + 1] != 0) {
            sb = new StringBuilder();
            while (fullByteCode[index] != 0) {
                sb.append((char) fullByteCode[index++]);
            }
            index++;

            int pos = ((fullByteCode[index++] & 0xFF) << 24) | ((fullByteCode[index++] & 0xFF) << 16) | ((fullByteCode[index++] & 0xFF) << 8) | ((fullByteCode[index++] & 0xFF));

            functions.put(sb.toString(), pos);
        }
        index += 2;

        constPool = new ConstantPool(fullByteCode[index++] + 1);
        index = constPool.parse(fullByteCode, index, this);

        constPool.set(0, new ModuleHandleValue(this));

        dataSection = new ByteCode(fullByteCode, 0, index);

        if (fullByteCode[index] != 0x43 && fullByteCode[index + 1] != 0x6F && fullByteCode[index + 2] != 0x64 && fullByteCode[index + 3] != 0x65) {
            throw new RuntimeException("error");
        }
        index += 4;

        codeSection = new ByteCode(fullByteCode, index);

        int constructor = functions.getOrDefault(".constructor", -1);
        if (constructor != -1) {
            vm.addConstructor(new LocalSymbol(this, ".constructor", constructor));
        }
    }

    public Map<String, NativeFunction> getNatives() {
        return natives;
    }

    public LocalSymbol getFunction(String name) {
        int symbol = functions.getOrDefault(name, -1);
        if (symbol == -1) {
            return getNative(name);
        }
        return new LocalSymbol(this, name, symbol);
    }

    public LocalSymbol getNative(String name) {
        return new LocalSymbol(this, name, natives.get(name));
    }

    public void putNative(String name, NativeFunction function) {
        natives.put(name, function);
    }

    public Value getConstant(int index) {
        Value value = constPool.get(index);

        if (value instanceof FunctionHandleValue && ((FunctionHandleValue) value).function.nativeFunction == null && ((FunctionHandleValue) value).function.location == -1) { // unloaded native. attempt to load it
            constPool.set(index, new FunctionHandleValue(getNative(((FunctionHandleValue) value).function.name)));
            value = constPool.get(index);
        }

        return value;
    }

    public void setConstant(int index, Value newValue) {
        constPool.set(index, newValue);
    }

    public boolean hasLibraryLoader() {
        return libraryLoader != null;
    }

    public LibraryLoader getLibraryLoader() {
        return libraryLoader;
    }

    public void setLibraryLoader() {
        libraryLoader = new LibraryLoader(this);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, lineNumber, version, dataSection, codeSection, functions, constPool);
        result = 31 * result + Arrays.hashCode(fullByteCode);
        return result;
    }
}
