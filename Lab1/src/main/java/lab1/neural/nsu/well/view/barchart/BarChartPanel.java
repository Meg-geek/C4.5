package lab1.neural.nsu.well.view.barchart;

import lab1.neural.nsu.well.WellParameters;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class BarChartPanel extends JPanel {
    private static final String BACK_BUTTON_TEXT = "Предыдущий параметр";
    private static final String FORWARD_BUTTON_TEXT = "Следующий параметр";
    private final int HEIGHT;
    private final int WIDTH;
    private final int INDENT;
    private final int AXIS_X;
    private final int AXIS_Y;
    private final int INTERVALS_AMOUNT = 10;
    private List<WellParameters> examples;
    private List<String> parameterNames;
    private int parameterIndex = 0, notEmptyValuesAmount;

    BarChartPanel(List<WellParameters> examples, List<String> parameterNames, int height, int width) {
        this.examples = examples;
        this.parameterNames = parameterNames;
        this.HEIGHT = height;
        this.WIDTH = width;
        this.INDENT = max(height / 15, width / 15);
        this.AXIS_X = width - INDENT;
        this.AXIS_Y = height - INDENT;
        addButtons();
    }

    private void addButtons() {
        JButton backButton = new JButton(BACK_BUTTON_TEXT);
        backButton.addActionListener(e -> {
            if (parameterIndex == 0) {
                parameterIndex = examples.get(0).getParameters().size();
            }
            parameterIndex--;
            repaint();
        });
        JButton forwardButton = new JButton(FORWARD_BUTTON_TEXT);
        forwardButton.addActionListener(e -> {
            parameterIndex++;
            if (parameterIndex == examples.get(0).getParameters().size()) {
                parameterIndex = 0;
            }
            repaint();
        });
        add(backButton);
        add(forwardButton);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawLine(INDENT, AXIS_Y, INDENT, INDENT);
        graphics2D.drawLine(INDENT, AXIS_Y, AXIS_X, AXIS_Y);
        graphics2D.drawString("Parameter name: " + parameterNames.get(parameterIndex), INDENT, INDENT / 2);
        drawBarChart(graphics2D);
    }

    private void drawBarChart(Graphics2D graphics2D) {
        List<Float> intervalPoints = getIntervalPoints();
        int[] intervalHeights = getIntervalHeights(intervalPoints);
        graphics2D.setColor(Color.BLUE);
        int x = INDENT * 2;
        for (int height : intervalHeights) {
            graphics2D.setColor(Color.BLUE);
            int rectangleHeight = (int) ((float) height / notEmptyValuesAmount * (AXIS_Y - INDENT));
            int y = AXIS_Y - rectangleHeight;
            graphics2D.fillRect(x, y, INDENT, rectangleHeight);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(x, y, INDENT, rectangleHeight);
            graphics2D.drawString(String.valueOf(height), x, y);
            x += INDENT;
        }
        drawIntervalValues(graphics2D, intervalPoints);
    }

    private void drawIntervalValues(Graphics2D graphics2D, List<Float> intervalPoints) {
        int x = INDENT * 2;
        int indent = graphics2D.getFont().getSize();
        for (Float point : intervalPoints) {
            graphics2D.drawString(point.toString(), x, AXIS_Y + indent);
            x += INDENT;
        }
    }

    private int[] getIntervalHeights(List<Float> intervalPoints) {
        int[] intervalAmount = new int[INTERVALS_AMOUNT];
        for (WellParameters wellParameters : examples) {
            if (wellParameters.getParameters().get(parameterIndex).isEmptyValueHolder()) {
                continue;
            }
            float value = wellParameters.getParameters().get(parameterIndex).getValue();
            for (int i = 1; i < intervalPoints.size(); i++) {
                if (value <= intervalPoints.get(i)) {
                    intervalAmount[i - 1]++;
                    break;
                }
            }
        }
        return intervalAmount;
    }

    private List<Float> getIntervalPoints() {
        float maxValue = Float.MIN_VALUE, minValue = Float.MAX_VALUE;
        notEmptyValuesAmount = examples.size();
        for (WellParameters wellParameters : examples) {
            if (wellParameters.getParameters().get(parameterIndex).isEmptyValueHolder()) {
                notEmptyValuesAmount--;
                continue;
            }
            float value = wellParameters.getParameters().get(parameterIndex).getValue();
            if (value > maxValue) {
                maxValue = value;
            }
            if (value < minValue) {
                minValue = value;
            }
        }
        return getIntervalPoints(minValue, maxValue);
    }

    private List<Float> getIntervalPoints(float minValue, float maxValue) {
        List<Float> intervalPoints = new ArrayList<>();
        float intervalSize = (maxValue - minValue) / INTERVALS_AMOUNT;
        float lastValue = minValue;
        for (int i = 0; i < INTERVALS_AMOUNT; i++) {
            intervalPoints.add(lastValue);
            lastValue += intervalSize;
        }
        return intervalPoints;
    }
}
