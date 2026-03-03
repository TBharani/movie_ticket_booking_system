import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private User admin;
    private JTable movieTable;
    private DefaultTableModel movieModel;

    public AdminDashboard(User admin) {
        this.admin = admin;
        setTitle("CinemaHub Admin - Dashboard");
        setSize(ThemeManager.WINDOW_W, ThemeManager.WINDOW_H);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        ThemeManager.centerFrame(this);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(ThemeManager.BG_DARK);

        // Navbar
        main.add(createNavbar(), BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(ThemeManager.BG_DARK);
        content.setBorder(ThemeManager.padding(15, 25, 15, 25));

        // Action buttons
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        actionBar.setOpaque(false);

        RoundedButton addBtn = new RoundedButton("➕ Add Movie",
            ThemeManager.SUCCESS, ThemeManager.SUCCESS.brighter());
        addBtn.setPreferredSize(new Dimension(150, 40));
        addBtn.addActionListener(e -> addMovie());

        RoundedButton editBtn = new RoundedButton("✏️ Edit Movie",
            ThemeManager.INFO, ThemeManager.INFO.brighter());
        editBtn.setPreferredSize(new Dimension(150, 40));
        editBtn.addActionListener(e -> editMovie());

        RoundedButton deleteBtn = new RoundedButton("🗑 Delete Movie",
            ThemeManager.DANGER, ThemeManager.DANGER.brighter());
        deleteBtn.setPreferredSize(new Dimension(160, 40));
        deleteBtn.addActionListener(e -> deleteMovie());

        RoundedButton bookingsBtn = new RoundedButton("📋 View Bookings",
            ThemeManager.PURPLE, ThemeManager.PURPLE.brighter());
        bookingsBtn.setPreferredSize(new Dimension(170, 40));
        bookingsBtn.addActionListener(e ->
            new ViewBookingsFrame().setVisible(true));

        RoundedButton refreshBtn = new RoundedButton("🔄 Refresh",
            ThemeManager.NEON_BLUE, ThemeManager.NEON_BLUE.brighter());
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.addActionListener(e -> loadMovies());

        actionBar.add(addBtn);
        actionBar.add(editBtn);
        actionBar.add(deleteBtn);
        actionBar.add(bookingsBtn);
        actionBar.add(refreshBtn);
        content.add(actionBar, BorderLayout.NORTH);

        // Movie table
        String[] cols = {"ID", "Title", "Genre", "Duration", "Language",
                         "Rating", "Status", "Color"};
        movieModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        movieTable = new JTable(movieModel);
        styleAdminTable(movieTable);

        JScrollPane scroll = new JScrollPane(movieTable);
        scroll.getViewport().setBackground(ThemeManager.BG_DARK);
        scroll.setBorder(BorderFactory.createLineBorder(ThemeManager.SURFACE, 1));
        content.add(scroll, BorderLayout.CENTER);

        // Stats bar
        content.add(createStatsBar(), BorderLayout.SOUTH);

        main.add(content, BorderLayout.CENTER);
        setContentPane(main);
        loadMovies();
    }

    private JPanel createNavbar() {
        JPanel nav = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(15, 15, 35));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setPaint(new GradientPaint(0, getHeight()-2,
                    ThemeManager.GOLD, getWidth(), getHeight()-2,
                    ThemeManager.PRIMARY));
                g2.fillRect(0, getHeight()-2, getWidth(), 2);
                g2.dispose();
            }
        };
        nav.setPreferredSize(new Dimension(0, 60));
        nav.setBorder(ThemeManager.padding(0, 25, 0, 25));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        left.setOpaque(false);
        left.add(ThemeManager.label("🎬 CinemaHub",
            new Font("Segoe UI", Font.BOLD, 22), ThemeManager.PRIMARY));
        left.add(ThemeManager.label("  ADMIN PANEL",
            new Font("Segoe UI", Font.BOLD, 14), ThemeManager.GOLD));
        nav.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        right.setOpaque(false);
        right.add(ThemeManager.label("👤 " + admin.getFullName() + "  ",
            ThemeManager.BODY, ThemeManager.TEXT_SECONDARY));
        RoundedButton logout = new RoundedButton("Logout",
            ThemeManager.DANGER, ThemeManager.DANGER.brighter());
        logout.setPreferredSize(new Dimension(100, 36));
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        right.add(logout);
        nav.add(right, BorderLayout.EAST);

        return nav;
    }

    private JPanel createStatsBar() {
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(0, 50));

        int movieCount = movieModel.getRowCount();
        int bookingCount = DBConnection.getAllBookings().size();

        stats.add(createStatItem("🎬 Total Movies", String.valueOf(movieCount),
                                 ThemeManager.NEON_BLUE));
        stats.add(createStatItem("🎟 Total Bookings", String.valueOf(bookingCount),
                                 ThemeManager.GOLD));
        return stats;
    }

    private JPanel createStatItem(String label, String value, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);
        p.add(ThemeManager.label(label + ": ", ThemeManager.BODY,
                                 ThemeManager.TEXT_SECONDARY));
        p.add(ThemeManager.label(value, ThemeManager.BODY_BOLD, color));
        return p;
    }

    private void styleAdminTable(JTable t) {
        t.setBackground(ThemeManager.BG_CARD);
        t.setForeground(ThemeManager.TEXT_WHITE);
        t.setFont(ThemeManager.BODY);
        t.setGridColor(ThemeManager.SURFACE);
        t.setRowHeight(38);
        t.setSelectionBackground(new Color(50, 50, 80));
        t.setSelectionForeground(ThemeManager.GOLD);
        t.setShowGrid(true);

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
                        : new Color(38, 38, 58));
                    c.setForeground(ThemeManager.TEXT_WHITE);
                }
                ((JLabel) c).setBorder(ThemeManager.padding(0, 10, 0, 10));
                return c;
            }
        });
    }

    private void loadMovies() {
        movieModel.setRowCount(0);
        List<Movie> movies = DBConnection.getAllMovies();
        for (Movie m : movies) {
            movieModel.addRow(new Object[]{
                m.getId(), m.getTitle(), m.getGenre(),
                m.getDurationFormatted(), m.getLanguage(),
                m.getRating(), m.getStatus(), m.getPosterColor()
            });
        }
    }

    private void addMovie() {
        new AddMovieDialog(this, null).setVisible(true);
        loadMovies();
    }

    private void editMovie() {
        int row = movieTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a movie to edit.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) movieModel.getValueAt(row, 0);
        List<Movie> movies = DBConnection.getAllMovies();
        Movie selected = null;
        for (Movie m : movies) {
            if (m.getId() == id) { selected = m; break; }
        }
        if (selected != null) {
            new AddMovieDialog(this, selected).setVisible(true);
            loadMovies();
        }
    }

    private void deleteMovie() {
        int row = movieTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a movie to delete.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) movieModel.getValueAt(row, 0);
        String title = movieModel.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete \"" + title + "\"?\nThis will also delete all related showtimes.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DBConnection.deleteMovie(id);
            loadMovies();
        }
    }
}