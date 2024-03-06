package cum.jesus.ctvm.value;

import cum.jesus.ctvm.module.LocalSymbol;

import java.util.Objects;

public class FunctionHandleValue extends Value {
    public final LocalSymbol function;

    public FunctionHandleValue(LocalSymbol function) {
        this.function = function;
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
        return function != null;
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
        return new FunctionHandleValue(function);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionHandleValue that = (FunctionHandleValue) o;
        return Objects.equals(function, that.function);
    }

    @Override
    public int hashCode() {
        return Objects.hash(function);
    }

    @Override
    public String toString() {
        return "function " + function.name;
    }
}
