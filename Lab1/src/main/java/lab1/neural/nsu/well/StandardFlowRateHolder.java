package lab1.neural.nsu.well;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class StandardFlowRateHolder {
    private ValueHolder gasRate, condRate, waterRate, mixRate;
}
