package cum.jesus.ctvm.value;

public final class StringValue extends Value {
    private String javaString;

    public StringValue(String javaString) {
        this.javaString = javaString;
    }

    public String getJavaString() {
        return javaString;
    }

    public void setJavaString(String javaString) {
        this.javaString = javaString;
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
        return true;
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
        return new StringValue(javaString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringValue that = (StringValue) o;

        return javaString.equals(that.javaString);
    }

    @Override
    public int hashCode() {
        return javaString.hashCode();
    }

    @Override
    public String toString() {
        return '"' + javaString.replace("\n", "\\n") + '"';
    }
}
