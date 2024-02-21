package cum.jesus.ctvm.module;

public final class LocalSymbol {
    public final Module module;
    public final int location;

    public LocalSymbol(Module module, int location) {
        this.module = module;
        this.location = location;
    }
}
