package cum.jesus.ctvm.executor;

import cum.jesus.ctvm.VM;
import cum.jesus.ctvm.module.Module;

public interface Executor {
    byte PREFIX_MASK = (byte) 0b11100000;
    byte PREFIX_COMPARE = (byte) 0b10000000;

    /**
     * Set the VM for the executor to use
     * @param vm
     */
    void setVM(VM vm);

    /**
     * Sets the current module to execute
     *
     * @param module the module
     */
    void setModule(Module module);

    /**
     * Set the location to begin executing
     *
     * @param pos The execution position
     */
    void setPos(int pos);

    /**
     * Abstract method for 1 instruction cycle
     */
    void cycle();

    /**
     * Starts the execution cycle. Has a default method, but should be overwritten to fit the executor design
     */
    default void start() {
        while (true) {
            cycle();
        }
    }

    /**
     * Stops the execution cycle
     */
    void stop();
}
