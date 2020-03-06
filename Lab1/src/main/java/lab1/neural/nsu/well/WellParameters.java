package lab1.neural.nsu.well;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WellParameters {
    private int number;
    private String date;

    private ValueHolder manometerDepth;
    private ValueHolder dAmount;

    private PressureHolder averageParams, endParams;
    private TemperatureHolder temperatures;

    private StandardFlowRateHolder standardFlowRate;
    private LinearFlowRateHolder linFlowRate;

    private float hEffective;
    private RplHolder rplHolder;
}
