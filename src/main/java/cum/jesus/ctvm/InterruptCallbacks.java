package cum.jesus.ctvm;

import cum.jesus.ctvm.data.Register;
import cum.jesus.ctvm.module.LibraryLoader;
import cum.jesus.ctvm.module.LocalSymbol;
import cum.jesus.ctvm.module.Module;
import cum.jesus.ctvm.value.ModuleHandleValue;
import cum.jesus.ctvm.value.NumberValue;
import cum.jesus.ctvm.value.StringValue;
import cum.jesus.ctvm.value.Value;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public final class InterruptCallbacks {
    public static void exit(VM vm, Module module, byte byte1, byte byte2, byte byte3) {
        System.exit(vm.getRegister(Register.regC).getValueNoClone().asNumber().getInt());
    }

    public static void printvm(VM vm, Module __, byte byte1, byte byte2, byte byte3) {
        System.out.printf("regA: %s, regB: %s, regC: %s, regD: %s\nregE: %s, regF: %s, regG: %s, regH: %s\n\n",
                vm.getRegister(1).getValueNoClone().toString(), vm.getRegister(2).getValueNoClone().toString(),
                vm.getRegister(3).getValueNoClone().toString(), vm.getRegister(4).getValueNoClone().toString(),
                vm.getRegister(5).getValueNoClone().toString(), vm.getRegister(6).getValueNoClone().toString(),
                vm.getRegister(7).getValueNoClone().toString(), vm.getRegister(8).getValueNoClone().toString());
        System.out.printf("regSB: %s, regST: %s\n\n", vm.getRegister(Register.regStackBase).getValueNoClone().toString(), vm.getRegister(Register.regStackTop).getValueNoClone().toString());

        System.out.println("Globally accessible symbols:");
        int i = 0;
        {
            Iterator<Map.Entry<String, LocalSymbol>> it = vm.getGlobalSymbols().iterator();
            while (it.hasNext()) {
                Map.Entry<String, LocalSymbol> symbol = it.next();
                if (i >= 100) {
                    System.out.println();
                    i = 0;
                }

                String thing = symbol.getKey() + " (" + symbol.getValue().location + ")";
                System.out.print(thing);
                i += thing.length();

                if (it.hasNext()) {
                    System.out.print(", ");
                    i += 2;
                }
            }
        }

        System.out.println("\n\nStack addresses between 1 and highest stack address in use:");
        i = Math.max(vm.getRegister(Register.regStackTop).getValueNoClone().asNumber().getInt(), vm.stack.highestAddress);
        for (int j = i; j >= 1; j--) {
            System.out.printf("%-8d %s", j, vm.stack.get(j));

            if (j == vm.getRegister(Register.regStackTop).getValueNoClone().asNumber().getInt()) {
                System.out.println("  <-- Current frame top");
            } else if (j == vm.getRegister(Register.regStackBase).getValueNoClone().asNumber().getInt()) {
                System.out.println("  <-- Current frame base");
            } else {
                System.out.println();
            }
        }
        System.out.println();

        for (Module module : vm.getModules()) {
            System.out.println("Module " + module.getName());

            System.out.println("\nConstant pool:");
            i = 0;
            Iterator<Value> it = module.getConstPool().iterator();
            while (it.hasNext()) {
                if (i >= 100) {
                    System.out.println();
                    i = 0;
                }

                Value constant = it.next();
                String s = constant.toString();
                if (constant instanceof ModuleHandleValue && ((ModuleHandleValue) constant).module == module) {
                    System.out.print("this");
                    i += 4;
                } else {
                    System.out.print(s);
                    i += s.length();
                }

                if (it.hasNext()) {
                    System.out.print(", ");
                    i+= 2;
                }
            }
            System.out.println();
        }
    }

    public static void gc(VM vm, Module module, byte byte1, byte byte2, byte byte3) {
        int flags = (((int) byte1 & 0xFF) << 8) | ((int) byte2 & 0xFF);
        System.gc();
    }

    public static void write(VM vm, Module module, byte byte1, byte byte2, byte byte3) {
        assert vm.getRegister(Register.regC).getValueNoClone() instanceof StringValue; //TODO be sophisticated
        System.out.print(vm.getRegister(Register.regC).getValueNoClone().asString().getJavaString());
    }

    public static void writei(VM vm, Module module, byte byte1, byte byte2, byte byte3) {
        assert vm.getRegister(Register.regC).getValueNoClone() instanceof NumberValue; //TODO be sophisticated
        System.out.print(vm.getRegister(Register.regC).getValueNoClone().asNumber().getLong());
    }

    public static void writef(VM vm, Module module, byte byte1, byte byte2, byte byte3) {

    }

    public static void getline(VM vm, Module module, byte byte1, byte byte2, byte byte3) {
        String in = System.console().readLine();
        vm.getRegister(Register.regE).setValue(new StringValue(in));
    }

    public static void loadlib(VM vm, Module module, byte byte1, byte byte2, byte byte3) {
        LibraryLoader libraryLoader = LibraryLoader.get(module);
        String jarPath = vm.getRegister(Register.regC).getValueNoClone().asString().getJavaString();

        try {
            libraryLoader.loadJarArchive(jarPath);
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: figure out
        }
    }

    public static void freestr(VM vm, Module module, byte byte1, byte byte2, byte byte3) {
        if (!(vm.getRegister(Register.regC).getValueNoClone() instanceof StringValue)) {
            vm.getRegister(Register.regE).setValueNoClone(new NumberValue(Value.TYPE_INT, 1));
            return;
        }

        ((StringValue) vm.getRegister(Register.regC).getValueNoClone()).setJavaString(null);
        vm.getRegister(Register.regC).setValue(VM.ZERO);
        vm.getRegister(Register.regE).setValue(VM.ZERO);
    }
}
