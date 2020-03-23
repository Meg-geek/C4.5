package lab1.neural.nsu.well.view.gainratio;

import lab1.neural.nsu.well.GainParameter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphicGainRatioPainter extends JFrame {
    private final int FRAME_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private final int FRAME_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    public GraphicGainRatioPainter(List<GainParameter> parameterGains) {
        super("Gain ratio diagram");
        JPanel graphicPanel = new JPanel(new BorderLayout());
        setContentPane(graphicPanel);
        graphicPanel.add(new GainRatioDiagramPanel(parameterGains, FRAME_HEIGHT, FRAME_WIDTH), BorderLayout.CENTER);
        graphicPanel.setBackground(Color.gray);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
