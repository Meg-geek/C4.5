package lab1.neural.nsu.well;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class WellParameters {
    private int number;
    private Date date;
    public static final int PARAMETERS_AMOUNT = 29;

    private List<ValueHolder> parameters = new ArrayList<>();

    private ResultClass resultClass;

    public void addParameter(ValueHolder parameter) {
        parameters.add(parameter);
    }
}
