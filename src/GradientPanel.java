import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {

    private Color color1, color2;
    private boolean vertical;

    public GradientPanel(Color c1, Color c2) {
        this(c1, c2, true);
    }

    public GradientPanel(Color c1, Color c2, boolean vertical) {
        this.color1   = c1;
        this.color2   = c2;
        this.vertical = vertical;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        GradientPaint gp = vertical
            ? new GradientPaint(0, 0, color1, 0, h, color2)
            : new GradientPaint(0, 0, color1, w, 0, color2);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
        g2.dispose();
        super.paintComponent(g);
    }
}