package cum.jesus.ctvm.module;

public final class LocalSymbol {
    public final Module module;
    public final String name;
    public final int location;

    public LocalSymbol(Module module, String name, int location) {
        this.module = module;
        this.name = name;
        this.location = location;
    }

    @Override
    public String toString() {
        return name + ":" + location;
    }
}
