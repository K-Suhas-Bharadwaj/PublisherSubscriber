public class Value<Generic> {
    Generic value;
    public Value(Generic value) {
        this.value = value;
    }
    public Generic getValue() {
        return value;
    }
}
