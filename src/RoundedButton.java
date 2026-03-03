import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {

    private Color baseColor, hoverColor, pressColor, textColor;
    private int arc = 25;
    private boolean hovered = false, pressed = false;
    private float glowAlpha = 0f;

    public RoundedButton(String text, Color base, Color hover) {
        super(text);
        this.baseColor  = base;
        this.hoverColor = hover;
        this.pressColor = hover.darker();
        this.textColor  = Color.WHITE;

        setFont(ThemeManager.BUTTON);
        setForeground(textColor);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            public void mouseExited(MouseEvent e)  { hovered = false; pressed = false; repaint(); }
            public void mousePressed(MouseEvent e)  { pressed = true;  repaint(); }
            public void mouseReleased(MouseEvent e)  { pressed = false; repaint(); }
        });
    }

    public RoundedButton(String text, Color base) {
        this(text, base, base.brighter());
    }

    public void setArc(int arc) { this.arc = arc; }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        Color bg = pressed ? pressColor : (hovered ? hoverColor : baseColor);

        // Shadow
        if (hovered) {
            g2.setColor(new Color(0, 0, 0, 60));
            g2.fill(new RoundRectangle2D.Float(3, 3, getWidth()-3, getHeight()-3, arc, arc));
        }

        // Button body
        g2.setColor(bg);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, arc, arc));

        // Subtle top highlight
        g2.setColor(new Color(255, 255, 255, 30));
        g2.fill(new RoundRectangle2D.Float(1, 1, getWidth()-3, getHeight()/2, arc, arc));

        // Text
        g2.setFont(getFont());
        g2.setColor(textColor);
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(getText(), x, y);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        int w = fm.stringWidth(getText()) + 50;
        int h = fm.getHeight() + 20;
        return new Dimension(Math.max(w, 120), Math.max(h, 40));
    }
}