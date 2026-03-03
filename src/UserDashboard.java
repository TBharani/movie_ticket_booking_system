import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserDashboard extends JFrame {

    private User currentUser;
    private JPanel movieGrid;
    private RoundedTextField searchField;

    public UserDashboard(User user) {
        this.currentUser = user;
        setTitle("CinemaHub - Movies");
        setSize(ThemeManager.WINDOW_W, ThemeManager.WINDOW_H);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        ThemeManager.centerFrame(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeManager.BG_DARK);

        // ═══ TOP NAVBAR ═══
        JPanel navbar = createNavbar();
        mainPanel.add(navbar, BorderLayout.NORTH);

        // ═══ MOVIE GRID ═══
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(ThemeManager.BG_DARK);
        content.setBorder(ThemeManager.padding(10, 30, 20, 30));

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        searchPanel.setOpaque(false);
        JLabel sIcon = ThemeManager.label("🔍", ThemeManager.SUBHEADING,
                                          ThemeManager.TEXT_MUTED);
        searchField = new RoundedTextField("Search movies...", 25);
        searchField.setPreferredSize(new Dimension(300, 40));
        RoundedButton searchBtn = new RoundedButton("Search",
            ThemeManager.NEON_BLUE, ThemeManager.NEON_BLUE.brighter());
        searchBtn.setPreferredSize(new Dimension(100, 38));
        searchBtn.addActionListener(e -> filterMovies());

        JLabel headerLbl = ThemeManager.label("🎬 Now Showing",
            ThemeManager.HEADING, ThemeManager.TEXT_WHITE);

        searchPanel.add(headerLbl);
        searchPanel.add(Box.createHorizontalStrut(200));
        searchPanel.add(sIcon);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        content.add(searchPanel, BorderLayout.NORTH);

        // Movie grid
        movieGrid = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        movieGrid.setOpaque(false);

        JScrollPane scroll = new JScrollPane(movieGrid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Style scrollbar
        scroll.getVerticalScrollBar().setBackground(ThemeManager.BG_DARK);

        content.add(scroll, BorderLayout.CENTER);
        mainPanel.add(content, BorderLayout.CENTER);

        setContentPane(mainPanel);
        loadMovies("");
    }

    private JPanel createNavbar() {
        JPanel nav = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(15, 15, 35));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bottom glow line
                g2.setPaint(new GradientPaint(0, getHeight()-2,
                    ThemeManager.PRIMARY, getWidth(), getHeight()-2,
                    ThemeManager.PURPLE));
                g2.fillRect(0, getHeight()-2, getWidth(), 2);
                g2.dispose();
            }
        };
        nav.setPreferredSize(new Dimension(0, 60));
        nav.setBorder(ThemeManager.padding(0, 25, 0, 25));

        // Left: Logo
        JPanel leftP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        leftP.setOpaque(false);
        JLabel logo = ThemeManager.label("🎬 CinemaHub",
            new Font("Segoe UI", Font.BOLD, 22), ThemeManager.PRIMARY);
        leftP.add(logo);
        nav.add(leftP, BorderLayout.WEST);

        // Right: User actions
        JPanel rightP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightP.setOpaque(false);

        JLabel welcome = ThemeManager.label("Welcome, " + currentUser.getFullName() + "  ",
            ThemeManager.BODY, ThemeManager.TEXT_SECONDARY);
        rightP.add(welcome);

        RoundedButton historyBtn = new RoundedButton("📋 My Bookings",
            ThemeManager.PURPLE, ThemeManager.PURPLE.brighter());
        historyBtn.setPreferredSize(new Dimension(150, 36));
        historyBtn.addActionListener(e -> {
            new BookingHistoryFrame(currentUser).setVisible(true);
        });
        rightP.add(historyBtn);

        RoundedButton logoutBtn = new RoundedButton("Logout",
            ThemeManager.DANGER, ThemeManager.DANGER.brighter());
        logoutBtn.setPreferredSize(new Dimension(100, 36));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        rightP.add(logoutBtn);

        nav.add(rightP, BorderLayout.EAST);
        return nav;
    }

    private void loadMovies(String search) {
        movieGrid.removeAll();
        List<Movie> movies = DBConnection.getAllMovies();

        for (Movie m : movies) {
            if (!search.isEmpty() &&
                !m.getTitle().toLowerCase().contains(search.toLowerCase()) &&
                !m.getGenre().toLowerCase().contains(search.toLowerCase())) {
                continue;
            }
            MovieCard card = new MovieCard(m, () -> openSeatSelection(m));
            card.addBookButton();
            movieGrid.add(card);
        }

        if (movieGrid.getComponentCount() == 0) {
            JLabel noResult = ThemeManager.centeredLabel(
                "No movies found.", ThemeManager.SUBHEADING, ThemeManager.TEXT_MUTED);
            movieGrid.add(noResult);
        }

        movieGrid.revalidate();
        movieGrid.repaint();
    }

    private void filterMovies() {
        loadMovies(searchField.getText().trim());
    }

    private void openSeatSelection(Movie movie) {
        new SeatSelectionFrame(currentUser, movie).setVisible(true);
    }
}