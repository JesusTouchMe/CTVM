package cum.jesus.ctvm.value;

import cum.jesus.ctvm.module.Module;

import java.util.Objects;

public class ModuleHandleValue extends Value {
    public final Module module;

    public ModuleHandleValue(Module module) {
        this.module = module;
    }

    @Override
    public void add(Value other) {

    }

    @Override
    public void sub(Value other) {

    }

    @Override
    public void mul(Value other) {

    }

    @Override
    public void div(Value other) {

    }

    @Override
    public void and(Value other) {

    }

    @Override
    public void or(Value other) {

    }

    @Override
    public void xor(Value other) {

    }

    @Override
    public void shl(Value other) {

    }

    @Override
    public void shr(Value other) {

    }

    @Override
    public void not() {

    }

    @Override
    public void neg() {

    }

    @Override
    public boolean isTrue() {
        return module != null;
    }

    @Override
    public void inc() {

    }

    @Override
    public void dec() {

    }

    @Override
    public boolean lt(Value other) {
        return false;
    }

    @Override
    public boolean gt(Value other) {
        return false;
    }

    @Override
    public boolean lte(Value other) {
        return false;
    }

    @Override
    public boolean gte(Value other) {
        return false;
    }

    @Override
    public Value clone() {
        return new ModuleHandleValue(module);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleHandleValue that = (ModuleHandleValue) o;
        return Objects.equals(module, that.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(module);
    }

    @Override
    public String toString() {
        return "module " + module.getName();
    }
}
