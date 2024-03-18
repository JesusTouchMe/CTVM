package cum.jesus.ctvm;

import cum.jesus.ctvm.data.Register;
import cum.jesus.ctvm.executor.Executor;
import cum.jesus.ctvm.executor.SynchronousCodeExecutor;
import cum.jesus.ctvm.module.Module;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException {
        //File inputTemp = new File("C:\\Users\\JesusTouchMe\\IdeaProjects\\CTS-Compiler\\test.ct");
        File inputTemp = new File("C:\\Users\\Jannik\\IdeaProjects\\CheatTriggersCompiler\\io.ct");
        if (!inputTemp.exists()) {
            throw new IOException("Input does not exist");
        }

        VM vm = new VM();

        Module io = new Module(vm, inputTemp.getName(), Files.readAllBytes(inputTemp.toPath()));

        inputTemp = new File("C:\\Users\\Jannik\\IdeaProjects\\CheatTriggersCompiler\\test.ct");
        if (!inputTemp.exists()) {
            throw new IOException("Input does not exist");
        }
        Module module = new Module(vm, inputTemp.getName(), Files.readAllBytes(inputTemp.toPath()));

        vm.addModule(io);
        vm.addModule(module);

        inputTemp = null;

        Executor mainExecutor = new SynchronousCodeExecutor();

        vm.prepare(mainExecutor);
        vm.start();
    }
}