import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedTextField extends JTextField {

    private String placeholder;
    private int arc = 20;

    public RoundedTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        setOpaque(false);
        setFont(ThemeManager.BODY);
        setForeground(ThemeManager.TEXT_WHITE);
        setCaretColor(ThemeManager.NEON_BLUE);
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(ThemeManager.SURFACE);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, arc, arc));

        if (isFocusOwner()) {
            g2.setColor(ThemeManager.NEON_BLUE);
            g2.setStroke(new BasicStroke(2));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-3, getHeight()-3, arc, arc));
        } else {
            g2.setColor(ThemeManager.SURFACE_LIGHT);
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, arc, arc));
        }
        g2.dispose();

        super.paintComponent(g);

        // Placeholder
        if (getText().isEmpty() && !isFocusOwner() && placeholder != null) {
            Graphics2D g3 = (Graphics2D) g.create();
            g3.setFont(getFont().deriveFont(Font.ITALIC));
            g3.setColor(ThemeManager.TEXT_MUTED);
            Insets ins = getInsets();
            g3.drawString(placeholder, ins.left, getHeight()/2 + g3.getFontMetrics().getAscent()/2 - 2);
            g3.dispose();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width, Math.max(d.height, 42));
    }
}