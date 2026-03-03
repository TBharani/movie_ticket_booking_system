import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ViewBookingsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewBookingsFrame() {
        setTitle("CinemaHub Admin - All Bookings");
        setSize(1200, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        ThemeManager.centerFrame(this);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(ThemeManager.BG_DARK);
        main.setBorder(ThemeManager.padding(20, 25, 20, 25));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(ThemeManager.padding(0, 0, 15, 0));

        JLabel title = ThemeManager.label("📋 All Customer Bookings",
            ThemeManager.HEADING, ThemeManager.GOLD);
        header.add(title, BorderLayout.WEST);

        JPanel rightH = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightH.setOpaque(false);

        RoundedButton refreshBtn = new RoundedButton("🔄 Refresh",
            ThemeManager.NEON_BLUE, ThemeManager.NEON_BLUE.brighter());
        refreshBtn.setPreferredSize(new Dimension(120, 36));
        refreshBtn.addActionListener(e -> loadBookings());
        rightH.add(refreshBtn);

        RoundedButton closeBtn = new RoundedButton("✕ Close",
            ThemeManager.SURFACE_LIGHT, ThemeManager.SURFACE_LIGHT.brighter());
        closeBtn.setPreferredSize(new Dimension(100, 36));
        closeBtn.addActionListener(e -> dispose());
        rightH.add(closeBtn);

        header.add(rightH, BorderLayout.EAST);
        main.add(header, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Customer", "Username", "Movie", "Theatre",
                         "Date", "Time", "Seats", "Amount (₹)", "Status", "Booked On"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(ThemeManager.BG_DARK);
        scroll.setBorder(BorderFactory.createLineBorder(ThemeManager.SURFACE, 1));
        main.add(scroll, BorderLayout.CENTER);

        // Stats
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(0, 45));
        main.add(stats, BorderLayout.SOUTH);

        setContentPane(main);
        loadBookings();
    }

    private void styleTable(JTable t) {
        t.setBackground(ThemeManager.BG_CARD);
        t.setForeground(ThemeManager.TEXT_WHITE);
        t.setFont(ThemeManager.BODY);
        t.setGridColor(ThemeManager.SURFACE);
        t.setRowHeight(36);
        t.setSelectionBackground(new Color(50, 50, 80));
        t.setSelectionForeground(ThemeManager.GOLD);

        JTableHeader th = t.getTableHeader();
        th.setBackground(new Color(25, 25, 50));
        th.setForeground(ThemeManager.GOLD);
        th.setFont(ThemeManager.BODY_BOLD);
        th.setPreferredSize(new Dimension(0, 42));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ThemeManager.GOLD));

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0
                        ? ThemeManager.BG_CARD
                        : new Color(35, 35, 55));
                    c.setForeground(ThemeManager.TEXT_WHITE);

                    if (column == 9 && value != null) {
                        String s = value.toString();
                        c.setForeground(s.equals("Confirmed")
                            ? ThemeManager.SUCCESS : ThemeManager.DANGER);
                    }
                    if (column == 8) c.setForeground(ThemeManager.NEON_BLUE);
                }
                ((JLabel) c).setBorder(ThemeManager.padding(0, 8, 0, 8));
                return c;
            }
        });

        // Column widths
        t.getColumnModel().getColumn(0).setPreferredWidth(40);
        t.getColumnModel().getColumn(1).setPreferredWidth(120);
        t.getColumnModel().getColumn(2).setPreferredWidth(80);
        t.getColumnModel().getColumn(3).setPreferredWidth(160);
        t.getColumnModel().getColumn(7).setPreferredWidth(120);
    }

    private void loadBookings() {
        model.setRowCount(0);
        List<String[]> bookings = DBConnection.getAllBookings();
        for (String[] b : bookings) {
            model.addRow(b);
        }
    }
}