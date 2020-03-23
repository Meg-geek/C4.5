package lab1.neural.nsu.well.view.gainratio;

import lab1.neural.nsu.well.GainParameter;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.max;

public class GainRatioDiagramPanel extends JPanel {
    private final int AXIS_X;
    private final int AXIS_Y;
    private final int INDENT;
    private final int TEXT_INDENT;
    private final int LINE_COEF = 2;
    private List<GainParameter> parameterGain;


    public GainRatioDiagramPanel(List<GainParameter> parameterGain, int frameHeight, int frameWidth) {
        this.parameterGain = parameterGain;
        this.INDENT = max(frameHeight / 15, frameWidth / 15);
        this.TEXT_INDENT = INDENT / 10;
        this.AXIS_X = frameWidth - INDENT;
        this.AXIS_Y = frameHeight - INDENT;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawLine(INDENT, AXIS_Y, INDENT, INDENT);
        graphics2D.drawLine(INDENT, AXIS_Y, AXIS_X, AXIS_Y);
        drawGainRatioDiagram(graphics2D);
    }

    private void drawGainRatioDiagram(Graphics2D graphics2D) {
        float maxGainRatio = Collections.max(parameterGain,
                Comparator.comparing(GainParameter::getGainRatio))
                .getGainRatio();
        int yIndent = (AXIS_Y - INDENT) / (parameterGain.size() * 2);
        int y = AXIS_Y - yIndent;
        for (GainParameter gainParameter : parameterGain) {
            int rectWidth = (int) ((gainParameter.getGainRatio() / maxGainRatio) * (AXIS_X - INDENT));
            graphics2D.setColor(Color.BLUE);
            graphics2D.fillRect(INDENT, y, rectWidth, yIndent);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(gainParameter.getGainRatio().toString(), INDENT + rectWidth, y + yIndent);
            drawParameterName(graphics2D,
                    gainParameter.getParameterName(),
                    TEXT_INDENT,
                    y + yIndent,
                    INDENT - TEXT_INDENT, yIndent);
            y -= 2 * yIndent;
        }
    }

    private void drawParameterName(Graphics2D graphics2D, String parameterName, int x, int y, int width, int height) {
        if (graphics2D.getFontMetrics().stringWidth(parameterName) > width) {
            String firstLine = parameterName.substring(0, parameterName.length() / LINE_COEF);
            graphics2D.drawString(firstLine, x, y);
            String secondLine = parameterName.substring(parameterName.length() / LINE_COEF);
            graphics2D.drawString(secondLine, x, y + height);
        } else {
            graphics2D.drawString(parameterName, x, y);
        }
    }
}
