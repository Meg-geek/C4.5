package lab1.neural.nsu.well;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LinearFlowRateHolder {
    private ValueHolder gasRate, waterRate, nonStableRate;
}
