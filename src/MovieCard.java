import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class MovieCard extends JPanel {

    private Movie movie;
    private boolean hovered = false;
    private Runnable onBook;

    public MovieCard(Movie movie, Runnable onBook) {
        this.movie  = movie;
        this.onBook = onBook;
        setPreferredSize(new Dimension(220, 380));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setLayout(new BorderLayout());

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int w = getWidth(), h = getHeight();
        int arc = 20;

        // Card Shadow
        if (hovered) {
            g2.setColor(new Color(229, 9, 20, 40));
            g2.fill(new RoundRectangle2D.Float(4, 4, w-4, h-4, arc, arc));
        }

        // Card background
        g2.setColor(ThemeManager.BG_CARD);
        g2.fill(new RoundRectangle2D.Float(0, 0, w-1, h-1, arc, arc));

        // Set clip for poster area
        Shape oldClip = g2.getClip();
        g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

        // POSTER area (top 55%)
        int posterH = (int)(h * 0.55);
        Color pc = movie.getPosterColorObj();
        GradientPaint gp = new GradientPaint(0, 0, pc, w, posterH, pc.darker().darker());
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, posterH);

        // Movie initial large letter
        g2.setFont(new Font("Segoe UI", Font.BOLD, 60));
        g2.setColor(new Color(255, 255, 255, 60));
        String initial = movie.getTitle().substring(0, 1).toUpperCase();
        FontMetrics fmI = g2.getFontMetrics();
        g2.drawString(initial, (w - fmI.stringWidth(initial))/2,
                      posterH/2 + fmI.getAscent()/2 - 5);

        // Film strip decoration
        g2.setColor(new Color(0, 0, 0, 40));
        for (int i = 0; i < w; i += 20) {
            g2.fillRect(i, 0, 8, 10);
            g2.fillRect(i, posterH - 10, 8, 10);
        }

        // Rating badge
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fill(new RoundRectangle2D.Float(w - 55, 8, 47, 24, 12, 12));
        g2.setFont(ThemeManager.BODY_BOLD);
        g2.setColor(ThemeManager.GOLD);
        g2.drawString("★ " + movie.getRating(), w - 50, 25);

        // Status badge
        if ("Coming Soon".equals(movie.getStatus())) {
            g2.setColor(new Color(139, 92, 246, 200));
            g2.fill(new RoundRectangle2D.Float(6, 8, 90, 22, 11, 11));
            g2.setFont(ThemeManager.SMALL);
            g2.setColor(Color.WHITE);
            g2.drawString("Coming Soon", 14, 23);
        }

        g2.setClip(oldClip);

        // TEXT area
        int textY = posterH + 12;
        int pad = 12;

        // Title
        g2.setFont(ThemeManager.BODY_BOLD);
        g2.setColor(ThemeManager.TEXT_WHITE);
        String title = movie.getTitle();
        if (g2.getFontMetrics().stringWidth(title) > w - 2*pad) {
            while (g2.getFontMetrics().stringWidth(title + "...") > w - 2*pad && title.length() > 0)
                title = title.substring(0, title.length()-1);
            title += "...";
        }
        g2.drawString(title, pad, textY + 4);

        // Genre
        g2.setFont(ThemeManager.SMALL);
        g2.setColor(ThemeManager.TEXT_SECONDARY);
        g2.drawString(movie.getGenre(), pad, textY + 22);

        // Duration & Language
        g2.setColor(ThemeManager.TEXT_MUTED);
        g2.drawString(movie.getDurationFormatted() + "  •  " + movie.getLanguage(),
                      pad, textY + 38);

        // Hover border
        if (hovered) {
            g2.setColor(ThemeManager.PRIMARY);
            g2.setStroke(new BasicStroke(2));
            g2.draw(new RoundRectangle2D.Float(1, 1, w-3, h-3, arc, arc));
        }

        g2.dispose();
    }

    public void addBookButton() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 10, 12, 10));

        RoundedButton btn = new RoundedButton("🎬 Book Now", ThemeManager.PRIMARY,
                                               ThemeManager.PRIMARY_HOVER);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(190, 36));
        btn.addActionListener(e -> { if (onBook != null) onBook.run(); });
        bottom.add(btn);

        add(bottom, BorderLayout.SOUTH);
    }
}