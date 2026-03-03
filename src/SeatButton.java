import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class SeatButton extends JToggleButton {

    public enum SeatState { AVAILABLE, BOOKED, SELECTED }

    private String seatId;
    private SeatState state;

    public SeatButton(String seatId, SeatState state) {
        super(seatId);
        this.seatId = seatId;
        this.state  = state;

        setPreferredSize(new Dimension(48, 40));
        setFont(new Font("Consolas", Font.BOLD, 11));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        if (state == SeatState.BOOKED) {
            setEnabled(false);
            setCursor(Cursor.getDefaultCursor());
        } else {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    public String getSeatId() { return seatId; }
    public SeatState getState() { return state; }

    public void setState(SeatState s) {
        this.state = s;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        Color bg;

        switch (state) {
            case BOOKED:   bg = ThemeManager.SEAT_BOOKED; break;
            case SELECTED: bg = ThemeManager.SEAT_SELECTED; break;
            default:       bg = isSelected() ? ThemeManager.SEAT_SELECTED
                                             : ThemeManager.SEAT_FREE;
        }

        if (state == SeatState.AVAILABLE && isSelected()) {
            state = SeatState.SELECTED;
            bg = ThemeManager.SEAT_SELECTED;
        } else if (state == SeatState.SELECTED && !isSelected()) {
            state = SeatState.AVAILABLE;
            bg = ThemeManager.SEAT_FREE;
        }

        // Seat shape (chair-like)
        g2.setColor(bg);
        g2.fill(new RoundRectangle2D.Float(4, 2, w-8, h-10, 8, 8));
        // Seat base
        g2.fill(new RoundRectangle2D.Float(8, h-12, w-16, 8, 4, 4));

        // Seat label
        g2.setFont(getFont());
        g2.setColor(state == SeatState.SELECTED ? Color.BLACK : Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(seatId,
            (w - fm.stringWidth(seatId))/2,
            (h - 6)/2 + fm.getAscent()/2 - 2);

        g2.dispose();
    }
}