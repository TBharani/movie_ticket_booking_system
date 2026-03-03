import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class BookingHistoryFrame extends JFrame {

    private User user;
    private JTable table;
    private DefaultTableModel model;

    public BookingHistoryFrame(User user) {
        this.user = user;
        setTitle("CinemaHub - My Bookings");
        setSize(1000, 600);
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

        JLabel title = ThemeManager.label("📋 My Booking History",
            ThemeManager.HEADING, ThemeManager.TEXT_WHITE);
        header.add(title, BorderLayout.WEST);

        RoundedButton closeBtn = new RoundedButton("✕ Close",
            ThemeManager.SURFACE_LIGHT, ThemeManager.SURFACE_LIGHT.brighter());
        closeBtn.setPreferredSize(new Dimension(100, 36));
        closeBtn.addActionListener(e -> dispose());
        header.add(closeBtn, BorderLayout.EAST);
        main.add(header, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Movie", "Theatre", "Date", "Time",
                         "Seats", "Amount (₹)", "Status", "Booked On"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(ThemeManager.BG_DARK);
        scroll.setBorder(BorderFactory.createLineBorder(ThemeManager.SURFACE, 1));
        main.add(scroll, BorderLayout.CENTER);

        // Bottom
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottom.setOpaque(false);
        RoundedButton cancelBtn = new RoundedButton("Cancel Selected Booking",
            ThemeManager.DANGER, ThemeManager.DANGER.brighter());
        cancelBtn.setPreferredSize(new Dimension(220, 38));
        cancelBtn.addActionListener(e -> cancelBooking());
        bottom.add(cancelBtn);
        main.add(bottom, BorderLayout.SOUTH);

        setContentPane(main);
        loadBookings();
    }

    private void styleTable(JTable t) {
        t.setBackground(ThemeManager.BG_CARD);
        t.setForeground(ThemeManager.TEXT_WHITE);
        t.setFont(ThemeManager.BODY);
        t.setGridColor(ThemeManager.SURFACE);
        t.setRowHeight(36);
        t.setSelectionBackground(ThemeManager.PRIMARY_DARK);
        t.setSelectionForeground(Color.WHITE);
        t.setShowGrid(true);
        t.setIntercellSpacing(new Dimension(1, 1));

        // Header styling
        JTableHeader th = t.getTableHeader();
        th.setBackground(new Color(25, 25, 50));
        th.setForeground(ThemeManager.GOLD);
        th.setFont(ThemeManager.BODY_BOLD);
        th.setPreferredSize(new Dimension(0, 40));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ThemeManager.PRIMARY));

        // Alternating row colors
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0
                        ? ThemeManager.BG_CARD
                        : new Color(35, 35, 55));
                    c.setForeground(ThemeManager.TEXT_WHITE);

                    // Color status
                    if (column == 7 && value != null) {
                        String s = value.toString();
                        c.setForeground(s.equals("Confirmed")
                            ? ThemeManager.SUCCESS : ThemeManager.DANGER);
                    }
                }
                ((JLabel) c).setBorder(ThemeManager.padding(0, 8, 0, 8));
                return c;
            }
        });
    }

    private void loadBookings() {
        model.setRowCount(0);
        List<String[]> bookings = DBConnection.getUserBookings(user.getId());
        for (String[] b : bookings) {
            model.addRow(b);
        }
    }

    private void cancelBooking() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a booking to cancel.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String status = model.getValueAt(row, 7).toString();
        if ("Cancelled".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "This booking is already cancelled.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this booking?",
            "Cancel Booking", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DBConnection.cancelBooking(id);
            loadBookings();
        }
    }
}