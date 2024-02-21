package cum.jesus.ctvm;

import cum.jesus.ctvm.executor.Executor;
import cum.jesus.ctvm.executor.SynchronousCodeExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException {
        File inputTemp = new File("C:\\Users\\JesusTouchMe\\IdeaProjects\\CTS-Compiler\\test.ct");
        if (!inputTemp.exists()) {
            throw new IOException("Input does not exist");
        }

        VM vm = new VM(Files.readAllBytes(inputTemp.toPath()));
        inputTemp = null;

        Executor mainExecutor = new SynchronousCodeExecutor();

        vm.prepare(mainExecutor);
        vm.start();
    }
}