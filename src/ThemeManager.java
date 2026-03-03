import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class ThemeManager {

    /* ── COLOUR PALETTE ── */
    public static final Color BG_DARK       = new Color(10, 10, 26);
    public static final Color BG_MEDIUM     = new Color(22, 33, 62);
    public static final Color BG_CARD       = new Color(30, 30, 50);
    public static final Color SURFACE       = new Color(40, 40, 65);
    public static final Color SURFACE_LIGHT = new Color(55, 55, 80);

    public static final Color PRIMARY       = new Color(229, 9, 20);
    public static final Color PRIMARY_DARK  = new Color(180, 8, 16);
    public static final Color PRIMARY_HOVER = new Color(255, 50, 60);
    public static final Color GOLD          = new Color(255, 215, 0);
    public static final Color NEON_BLUE     = new Color(0, 212, 255);
    public static final Color PURPLE        = new Color(139, 92, 246);
    public static final Color PINK          = new Color(236, 72, 153);

    public static final Color TEXT_WHITE    = Color.WHITE;
    public static final Color TEXT_SECONDARY= new Color(180, 180, 200);
    public static final Color TEXT_MUTED    = new Color(120, 120, 145);

    public static final Color SUCCESS       = new Color(34, 197, 94);
    public static final Color DANGER        = new Color(239, 68, 68);
    public static final Color WARNING       = new Color(234, 179, 8);
    public static final Color INFO          = new Color(59, 130, 246);

    public static final Color SEAT_FREE     = new Color(34, 197, 94);
    public static final Color SEAT_BOOKED   = new Color(239, 68, 68);
    public static final Color SEAT_SELECTED = new Color(255, 215, 0);
    public static final Color SEAT_HOVER    = new Color(96, 165, 250);

    /* ── FONTS ── */
    public static final Font TITLE      = new Font("Segoe UI", Font.BOLD, 38);
    public static final Font HEADING    = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font SUBHEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font BODY       = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BODY_BOLD  = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font SMALL      = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BUTTON     = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font MONO       = new Font("Consolas", Font.PLAIN, 13);

    /* ── SIZES ── */
    public static final int WINDOW_W = 1366;
    public static final int WINDOW_H = 768;

    public static void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("OptionPane.background",        BG_DARK);
        UIManager.put("OptionPane.messageForeground",  TEXT_WHITE);
        UIManager.put("Panel.background",              BG_DARK);
        UIManager.put("OptionPane.buttonFont",         BODY_BOLD);
        UIManager.put("Button.background",             SURFACE);
        UIManager.put("Button.foreground",             TEXT_WHITE);
        UIManager.put("Button.font",                   BODY_BOLD);
    }

    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static JLabel centeredLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static void centerFrame(JFrame f) {
        f.setLocationRelativeTo(null);
    }

    public static Border padding(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }
}