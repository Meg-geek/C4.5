package lab1.neural.nsu.well;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class ResultClass {
    //kgf in г/м^3
    private ValueHolder gTotalHolder, kgfHolder;
    private final float FORMAT_COEF = 1000f;

    void setKgfInOtherFormat(float kgfInOtherFormat) {
        this.kgfHolder.setValue(kgfInOtherFormat / FORMAT_COEF);
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
