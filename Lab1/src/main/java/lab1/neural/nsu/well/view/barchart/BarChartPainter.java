package lab1.neural.nsu.well.view.barchart;

import lab1.neural.nsu.well.WellParameters;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BarChartPainter extends JFrame {
    private final int FRAME_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private final int FRAME_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    public BarChartPainter(List<WellParameters> examples, List<String> parameterNames) {
        super("Гистограмма значений");
        JPanel graphicPanel = new JPanel(new BorderLayout());
        setContentPane(graphicPanel);
        graphicPanel.add(new BarChartPanel(examples, parameterNames, FRAME_HEIGHT, FRAME_WIDTH), BorderLayout.CENTER);
        graphicPanel.setBackground(Color.gray);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
