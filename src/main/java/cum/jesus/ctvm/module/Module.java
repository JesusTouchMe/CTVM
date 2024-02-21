package cum.jesus.ctvm.module;

import cum.jesus.ctvm.bytecode.ByteCode;
import cum.jesus.ctvm.constant.ConstantPool;
import cum.jesus.ctvm.value.Value;

import java.util.HashMap;
import java.util.Map;

public final class Module {
    private final String name;

    private int lineNumber = 0;
    private int version;

    private final byte[] fullByteCode;
    private ByteCode dataSection;
    private ByteCode codeSection;

    private Map<String, Integer> functions;
    private ConstantPool constPool;

    public Module(String name, byte[] fullByteCode) {
        this.name = name;
        this.fullByteCode = fullByteCode;
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

        constPool = new ConstantPool(fullByteCode[index++]);
        index = constPool.parse(fullByteCode, index);

        dataSection = new ByteCode(fullByteCode, 0, index);

        if (fullByteCode[index] != 0x43 && fullByteCode[index + 1] != 0x6F && fullByteCode[index + 2] != 0x64 && fullByteCode[index + 3] != 0x65) {
            throw new RuntimeException("error");
        }
        index += 4;

        codeSection = new ByteCode(fullByteCode, index);
    }

    public Value getConstant(int index) {
        return constPool.get(index);
    }

    public void setConstant(int index, Value newValue) {
        constPool.set(index, newValue);
    }
}
