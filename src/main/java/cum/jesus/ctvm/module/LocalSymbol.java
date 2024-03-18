package cum.jesus.ctvm.module;

import cum.jesus.ctni.Handle;
import cum.jesus.ctni.NativeFunction;
import cum.jesus.ctvm.VM;

public final class LocalSymbol implements Handle {
    public final Module module;
    public final String name;
    public final int location;

    public final NativeFunction nativeFunction;

    public LocalSymbol(Module module, String name, int location) {
        this.module = module;
        this.name = name;
        this.location = location;
        this.nativeFunction = null;
    }

    public LocalSymbol(Module module, String name, NativeFunction nativeFunction) {
        this.module = module;
        this.name = name;
        this.location = -1;
        this.nativeFunction = nativeFunction;
    }

    @Override
    public String toString() {
        return module + "." + name + "@" + location;
    }
}
