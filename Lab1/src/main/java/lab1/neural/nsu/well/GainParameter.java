package lab1.neural.nsu.well;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GainParameter {
    private final String parameterName;
    private final Float gainRatio;
}
