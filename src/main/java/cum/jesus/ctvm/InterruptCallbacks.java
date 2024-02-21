package cum.jesus.ctvm;

import cum.jesus.ctvm.data.Register;
import cum.jesus.ctvm.value.NumberValue;
import cum.jesus.ctvm.value.StringValue;

public final class InterruptCallbacks {
    public static void exit(VM vm, byte byte1, byte byte2, byte byte3) {
        System.exit(vm.getRegister(Register.regE).getValueNoClone().asNumber().getInt());
    }

    public static void printvm(VM vm, byte byte1, byte byte2, byte byte3) {

    }

    public static void gc(VM vm, byte byte1, byte byte2, byte byte3) {
        System.gc();

        vm.resumeExecution();
    }

    public static void write(VM vm, byte byte1, byte byte2, byte byte3) {
        assert vm.getRegister(Register.regC).getValueNoClone() instanceof StringValue; //TODO be sophisticated
        System.out.print(vm.getRegister(Register.regC).getValueNoClone().asString().toJavaString());

        vm.resumeExecution();
    }

    public static void writei(VM vm, byte byte1, byte byte2, byte byte3) {
        assert vm.getRegister(Register.regC).getValueNoClone() instanceof NumberValue; //TODO be sophisticated
        System.out.print(vm.getRegister(Register.regC).getValueNoClone().asNumber().getLong());

        vm.resumeExecution();
    }

    public static void writef(VM vm, byte byte1, byte byte2, byte byte3) {

    }

    public static void getline(VM vm, byte byte1, byte byte2, byte byte3) {
        String in = System.console().readLine();
        vm.getRegister(Register.regE).setValue(new StringValue(in));

        vm.resumeExecution();
    }

    public static void freestr(VM vm, byte byte1, byte byte2, byte byte3) {

    }
}
