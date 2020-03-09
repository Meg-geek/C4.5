package lab1.neural.nsu.well;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class ValueHolder {
    private float value;
    private ValueType valueType;

    public void setValue(float value) {
        this.value = value;
        this.valueType = ValueType.FLOAT;
    }

    private ValueHolder() {
        value = 0f;
        valueType = ValueType.EMPTY_VALUE;
    }

    public static ValueHolder getEmptyValueHolder() {
        return new ValueHolder();
    }

    public boolean isEmptyValueHolder() {
        return this.equals(getEmptyValueHolder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ValueHolder that = (ValueHolder) o;
        return Float.compare(that.value, value) == 0 &&
                valueType == that.valueType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, valueType);
    }
}
