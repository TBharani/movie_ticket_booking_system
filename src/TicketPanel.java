import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class TicketPanel extends JPanel {

    private String movieTitle, theatre, showDate, showTime, seats, bookingId;
    private double totalAmount;

    public TicketPanel(String movieTitle, String theatre, String showDate,
                       String showTime, String seats, double totalAmount, String bookingId) {
        this.movieTitle  = movieTitle;
        this.theatre     = theatre;
        this.showDate    = showDate;
        this.showTime    = showTime;
        this.seats       = seats;
        this.totalAmount = totalAmount;
        this.bookingId   = bookingId;
        setPreferredSize(new Dimension(520, 280));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int w = getWidth(), h = getHeight();
        int tearX = (int)(w * 0.65);

        // Left part (main ticket)
        g2.setColor(new Color(35, 35, 58));
        g2.fill(new RoundRectangle2D.Float(0, 0, tearX, h, 18, 18));

        // Right part (stub)
        g2.setColor(new Color(45, 45, 70));
        g2.fill(new RoundRectangle2D.Float(tearX + 2, 0, w - tearX - 2, h, 18, 18));

        // Tear line (dashed)
        g2.setColor(new Color(80, 80, 110));
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
                     BasicStroke.JOIN_BEVEL, 0, new float[]{6, 4}, 0));
        g2.drawLine(tearX, 10, tearX, h - 10);

        // Semicircle cutouts
        g2.setColor(ThemeManager.BG_DARK);
        g2.fillOval(tearX - 12, -12, 24, 24);
        g2.fillOval(tearX - 12, h - 12, 24, 24);

        // Left: Red accent bar
        g2.setColor(ThemeManager.PRIMARY);
        g2.fill(new RoundRectangle2D.Float(0, 0, 8, h, 18, 0));

        // ─── LEFT SIDE CONTENT ───
        int x = 22, y = 30;

        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(ThemeManager.TEXT_MUTED);
        g2.drawString("🎬 CINEMAHUB PRESENTS", x, y);

        y += 28;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g2.setColor(ThemeManager.TEXT_WHITE);
        // Truncate if too long
        String t = movieTitle;
        if (g2.getFontMetrics().stringWidth(t) > tearX - 40) {
            while (g2.getFontMetrics().stringWidth(t + "..") > tearX - 40 && t.length() > 0)
                t = t.substring(0, t.length()-1);
            t += "..";
        }
        g2.drawString(t, x, y);

        y += 30;
        g2.setColor(ThemeManager.TEXT_MUTED);
        g2.setFont(ThemeManager.SMALL);
        g2.drawString("THEATRE", x, y);
        g2.drawString("DATE", x + 160, y);
        y += 18;
        g2.setColor(ThemeManager.TEXT_WHITE);
        g2.setFont(ThemeManager.BODY_BOLD);
        g2.drawString(theatre, x, y);
        g2.drawString(showDate, x + 160, y);

        y += 30;
        g2.setColor(ThemeManager.TEXT_MUTED);
        g2.setFont(ThemeManager.SMALL);
        g2.drawString("TIME", x, y);
        g2.drawString("SEATS", x + 160, y);
        y += 18;
        g2.setColor(ThemeManager.NEON_BLUE);
        g2.setFont(ThemeManager.BODY_BOLD);
        g2.drawString(showTime, x, y);
        g2.setColor(ThemeManager.GOLD);
        String seatDisp = seats.length() > 20 ? seats.substring(0,18)+".." : seats;
        g2.drawString(seatDisp, x + 160, y);

        // Bottom bar
        y += 30;
        g2.setColor(ThemeManager.PRIMARY);
        g2.fill(new RoundRectangle2D.Float(x, y, tearX - 35, 30, 8, 8));
        g2.setColor(Color.WHITE);
        g2.setFont(ThemeManager.BODY_BOLD);
        g2.drawString("TOTAL: ₹" + String.format("%.2f", totalAmount), x + 10, y + 20);

        // ─── RIGHT SIDE (STUB) ───
        int rx = tearX + 18;
        int ry = 35;

        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.setColor(ThemeManager.GOLD);
        g2.drawString("TICKET", rx, ry);

        ry += 22;
        g2.setFont(new Font("Consolas", Font.BOLD, 14));
        g2.setColor(ThemeManager.TEXT_WHITE);
        g2.drawString("#" + bookingId, rx, ry);

        ry += 30;
        g2.setFont(ThemeManager.SMALL);
        g2.setColor(ThemeManager.TEXT_MUTED);
        g2.drawString("SCREEN", rx, ry);
        ry += 16;
        g2.setColor(ThemeManager.TEXT_WHITE);
        g2.setFont(ThemeManager.BODY_BOLD);
        String scr = theatre.length() > 12 ? theatre.substring(0,12) : theatre;
        g2.drawString(scr, rx, ry);

        ry += 25;
        g2.setFont(ThemeManager.SMALL);
        g2.setColor(ThemeManager.TEXT_MUTED);
        g2.drawString("TIME", rx, ry);
        ry += 16;
        g2.setColor(ThemeManager.NEON_BLUE);
        g2.setFont(ThemeManager.BODY_BOLD);
        g2.drawString(showTime, rx, ry);

        ry += 25;
        g2.setFont(ThemeManager.SMALL);
        g2.setColor(ThemeManager.TEXT_MUTED);
        g2.drawString("SEAT(S)", rx, ry);
        ry += 16;
        g2.setColor(ThemeManager.GOLD);
        g2.setFont(ThemeManager.BODY_BOLD);
        String stubSeats = seats.length() > 14 ? seats.substring(0,12)+".." : seats;
        g2.drawString(stubSeats, rx, ry);

        // Barcode-like decoration at bottom right
        ry += 25;
        g2.setColor(ThemeManager.TEXT_MUTED);
        for (int i = 0; i < 30; i++) {
            int bw = (i % 3 == 0) ? 3 : 1;
            g2.fillRect(rx + i*4, ry, bw, 18);
        }

        g2.dispose();
    }
}