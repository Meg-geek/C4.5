package lab1.neural.nsu.well;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class ResultClass {
    //kgf in г/м^3
    private ValueHolder gTotalHolder, kgfHolder;
    private static final float FORMAT_COEF = 1000f;

    private static float setKgfInOtherFormat(float kgfInOtherFormat) {
        return kgfInOtherFormat / FORMAT_COEF;
    }

    private ResultClass() {
        gTotalHolder = ValueHolder.getEmptyValueHolder();
        kgfHolder = ValueHolder.getEmptyValueHolder();
    }

    public static ResultClass getEmptyResultClass() {
        return new ResultClass();
    }

    public boolean isEmpty(){
        return this.equals(getEmptyResultClass());
    }

    public static ResultClass getResultClassFromHolders(List<ValueHolder> resultHolders) {
        if (resultHolders == null || resultHolders.isEmpty()) {
            return getEmptyResultClass();
        }
        if (resultHolders.size() == 1) {
            return new ResultClass(resultHolders.get(0), ValueHolder.getEmptyValueHolder());
        }
        if (resultHolders.size() == 2) {
            return new ResultClass(resultHolders.get(0), resultHolders.get(1));
        }
        if (resultHolders.get(1).isEmptyValueHolder() && !resultHolders.get(2).isEmptyValueHolder()) {
            float otherFormatValue = resultHolders.get(2).getValue();
            return new ResultClass(resultHolders.get(0),
                    new ValueHolder(setKgfInOtherFormat(otherFormatValue), ValueType.FLOAT));
        }
        return new ResultClass(resultHolders.get(0), resultHolders.get(1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ResultClass that = (ResultClass) o;
        return Float.compare(that.gTotalHolder.getValue(), gTotalHolder.getValue()) == 0 &&
                Float.compare(that.kgfHolder.getValue(), kgfHolder.getValue()) == 0 &&
                gTotalHolder.getValueType() == that.gTotalHolder.getValueType() &&
                kgfHolder.getValueType() == that.kgfHolder.getValueType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(gTotalHolder, kgfHolder,
                gTotalHolder.getValueType(), kgfHolder.getValueType());
    }
}
