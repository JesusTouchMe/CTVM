package cum.jesus.ctvm.module;

import cum.jesus.ctvm.VM;
import cum.jesus.ctvm.util.TwoConsumer;

public final class LocalSymbol {
    public final Module module;
    public final String name;
    public final int location;

    public final TwoConsumer<VM, Module> nativeFunction;

    public LocalSymbol(Module module, String name, int location) {
        this.module = module;
        this.name = name;
        this.location = location;
        this.nativeFunction = null;
    }

    public LocalSymbol(Module module, String name, TwoConsumer<VM, Module> nativeFunction) {
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
