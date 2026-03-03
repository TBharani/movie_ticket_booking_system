import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SeatSelectionFrame extends JFrame {

    private User user;
    private Movie movie;
    private JPanel seatGrid;
    private JLabel selectedLabel, totalLabel;
    private JComboBox<String> showtimeCombo;
    private List<String[]> showtimes;
    private Set<String> bookedSeats = new HashSet<>();
    private List<SeatButton> seatButtons = new ArrayList<>();
    private double pricePerSeat = 300;

    private static final int ROWS = 8;
    private static final int COLS = 12;
    private static final String[] ROW_LABELS = {"A","B","C","D","E","F","G","H"};

    public SeatSelectionFrame(User user, Movie movie) {
        this.user  = user;
        this.movie = movie;
        setTitle("CinemaHub - Select Seats: " + movie.getTitle());
        setSize(ThemeManager.WINDOW_W, ThemeManager.WINDOW_H);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        ThemeManager.centerFrame(this);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(ThemeManager.BG_DARK);

        // ═══ TOP: Movie Info ═══
        JPanel topP = createTopPanel();
        main.add(topP, BorderLayout.NORTH);

        // ═══ CENTER: Seat Layout ═══
        JPanel centerP = createSeatPanel();
        main.add(centerP, BorderLayout.CENTER);

        // ═══ BOTTOM: Summary ═══
        JPanel bottomP = createBottomPanel();
        main.add(bottomP, BorderLayout.SOUTH);

        setContentPane(main);

        // Load showtimes
        showtimes = DBConnection.getShowtimes(movie.getId());
        for (String[] st : showtimes) {
            showtimeCombo.addItem(st[1] + "  |  " + st[3] + "  |  ₹" + st[4]);
        }
        if (!showtimes.isEmpty()) {
            loadSeats(0);
        }
    }

    private JPanel createTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(15, 15, 35));
        top.setBorder(ThemeManager.padding(12, 25, 12, 25));
        top.setPreferredSize(new Dimension(0, 90));

        // Movie info
        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        info.setOpaque(false);

        // Color dot
        JLabel dot = new JLabel("●");
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        dot.setForeground(movie.getPosterColorObj());
        info.add(dot);

        JPanel textP = new JPanel();
        textP.setLayout(new BoxLayout(textP, BoxLayout.Y_AXIS));
        textP.setOpaque(false);
        textP.add(ThemeManager.label(movie.getTitle(), ThemeManager.SUBHEADING,
                                     ThemeManager.TEXT_WHITE));
        textP.add(ThemeManager.label(movie.getGenre() + "  •  " +
                  movie.getDurationFormatted(), ThemeManager.SMALL,
                  ThemeManager.TEXT_SECONDARY));
        info.add(textP);
        top.add(info, BorderLayout.WEST);

        // Showtime selector
        JPanel stPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        stPanel.setOpaque(false);
        stPanel.add(ThemeManager.label("Showtime: ", ThemeManager.BODY_BOLD,
                                       ThemeManager.TEXT_SECONDARY));
        showtimeCombo = new JComboBox<>();
        showtimeCombo.setFont(ThemeManager.BODY);
        showtimeCombo.setPreferredSize(new Dimension(320, 35));
        showtimeCombo.setBackground(ThemeManager.SURFACE);
        showtimeCombo.setForeground(ThemeManager.TEXT_WHITE);
        showtimeCombo.addActionListener(e -> {
            int idx = showtimeCombo.getSelectedIndex();
            if (idx >= 0) loadSeats(idx);
        });
        stPanel.add(showtimeCombo);

        // Back button
        RoundedButton backBtn = new RoundedButton("← Back",
            ThemeManager.SURFACE_LIGHT, ThemeManager.SURFACE_LIGHT.brighter());
        backBtn.setPreferredSize(new Dimension(80, 35));
        backBtn.addActionListener(e -> dispose());
        stPanel.add(backBtn);

        top.add(stPanel, BorderLayout.EAST);
        return top;
    }

    private JPanel createSeatPanel() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(ThemeManager.BG_DARK);
        center.setBorder(ThemeManager.padding(15, 40, 10, 40));

        // Screen indicator
        JPanel screenP = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                // Screen glow
                g2.setPaint(new GradientPaint(w/4, h, new Color(0,212,255,80),
                                              w/2, 0, new Color(0,212,255,0)));
                g2.fillRect(w/4, 0, w/2, h);
                // Screen shape
                g2.setColor(ThemeManager.NEON_BLUE);
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(w/6, -10, w*2/3, 50, 0, 180);
                g2.setFont(ThemeManager.BODY_BOLD);
                g2.setColor(ThemeManager.TEXT_MUTED);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("S C R E E N", (w-fm.stringWidth("S C R E E N"))/2, 42);
                g2.dispose();
            }
        };
        screenP.setOpaque(false);
        screenP.setPreferredSize(new Dimension(0, 55));
        center.add(screenP, BorderLayout.NORTH);

        // Seat grid
        seatGrid = new JPanel(new GridBagLayout());
        seatGrid.setOpaque(false);
        center.add(seatGrid, BorderLayout.CENTER);

        // Legend
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        legend.setOpaque(false);
        legend.setPreferredSize(new Dimension(0, 40));
        legend.add(createLegendItem("Available", ThemeManager.SEAT_FREE));
        legend.add(createLegendItem("Selected", ThemeManager.SEAT_SELECTED));
        legend.add(createLegendItem("Booked", ThemeManager.SEAT_BOOKED));
        center.add(legend, BorderLayout.SOUTH);

        return center;
    }

    private JPanel createLegendItem(String text, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel("■");
        dot.setFont(new Font("Segoe UI", Font.BOLD, 18));
        dot.setForeground(color);
        p.add(dot);
        p.add(ThemeManager.label(text, ThemeManager.SMALL, ThemeManager.TEXT_SECONDARY));
        return p;
    }

    private JPanel createBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(15, 15, 35));
        bottom.setBorder(ThemeManager.padding(12, 30, 12, 30));
        bottom.setPreferredSize(new Dimension(0, 65));

        JPanel leftP = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        leftP.setOpaque(false);
        selectedLabel = ThemeManager.label("Selected: None",
            ThemeManager.BODY_BOLD, ThemeManager.GOLD);
        totalLabel = ThemeManager.label("Total: ₹0.00",
            ThemeManager.BODY_BOLD, ThemeManager.NEON_BLUE);
        leftP.add(selectedLabel);
        leftP.add(totalLabel);
        bottom.add(leftP, BorderLayout.WEST);

        JPanel rightP = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        rightP.setOpaque(false);
        RoundedButton confirmBtn = new RoundedButton("🎟 Confirm Booking",
            ThemeManager.PRIMARY, ThemeManager.PRIMARY_HOVER);
        confirmBtn.setPreferredSize(new Dimension(200, 42));
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        confirmBtn.addActionListener(e -> confirmBooking());
        rightP.add(confirmBtn);
        bottom.add(rightP, BorderLayout.EAST);

        return bottom;
    }

    private void loadSeats(int showtimeIndex) {
        String[] st = showtimes.get(showtimeIndex);
        int showtimeId = Integer.parseInt(st[0]);
        pricePerSeat = Double.parseDouble(st[4]);
        bookedSeats = DBConnection.getBookedSeats(showtimeId);

        seatGrid.removeAll();
        seatButtons.clear();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);

        for (int r = 0; r < ROWS; r++) {
            // Row label
            gbc.gridx = 0; gbc.gridy = r;
            JLabel rowLbl = ThemeManager.label(ROW_LABELS[r],
                ThemeManager.BODY_BOLD, ThemeManager.TEXT_MUTED);
            rowLbl.setPreferredSize(new Dimension(25, 36));
            rowLbl.setHorizontalAlignment(SwingConstants.CENTER);
            seatGrid.add(rowLbl, gbc);

            for (int c = 1; c <= COLS; c++) {
                gbc.gridx = c;
                if (c == 7) { // Aisle gap
                    gbc.gridx = c;
                    JLabel gap = new JLabel();
                    gap.setPreferredSize(new Dimension(20, 36));
                    seatGrid.add(gap, gbc);
                    continue;
                }
                int seatNum = (c <= 6) ? c : c - 1;
                String seatId = ROW_LABELS[r] + seatNum;

                SeatButton.SeatState state = bookedSeats.contains(seatId)
                    ? SeatButton.SeatState.BOOKED
                    : SeatButton.SeatState.AVAILABLE;

                SeatButton sb = new SeatButton(seatId, state);
                sb.addActionListener(e -> updateSelection());
                seatButtons.add(sb);
                seatGrid.add(sb, gbc);
            }

            // Row label right
            gbc.gridx = COLS + 1;
            JLabel rowLblR = ThemeManager.label(ROW_LABELS[r],
                ThemeManager.BODY_BOLD, ThemeManager.TEXT_MUTED);
            rowLblR.setPreferredSize(new Dimension(25, 36));
            rowLblR.setHorizontalAlignment(SwingConstants.CENTER);
            seatGrid.add(rowLblR, gbc);
        }

        seatGrid.revalidate();
        seatGrid.repaint();
        updateSelection();
    }

    private void updateSelection() {
        List<String> selected = new ArrayList<>();
        for (SeatButton sb : seatButtons) {
            if (sb.isSelected() && sb.getState() != SeatButton.SeatState.BOOKED) {
                sb.setState(SeatButton.SeatState.SELECTED);
                selected.add(sb.getSeatId());
            } else if (!sb.isSelected() && sb.getState() == SeatButton.SeatState.SELECTED) {
                sb.setState(SeatButton.SeatState.AVAILABLE);
            }
        }

        if (selected.isEmpty()) {
            selectedLabel.setText("Selected: None");
            totalLabel.setText("Total: ₹0.00");
        } else {
            selectedLabel.setText("Selected: " + String.join(", ", selected));
            double total = selected.size() * pricePerSeat;
            totalLabel.setText("Total: ₹" + String.format("%.2f", total));
        }
    }

    private void confirmBooking() {
        List<String> selected = new ArrayList<>();
        for (SeatButton sb : seatButtons) {
            if (sb.isSelected() && sb.getState() != SeatButton.SeatState.BOOKED) {
                selected.add(sb.getSeatId());
            }
        }

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one seat!", "No Seats",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idx = showtimeCombo.getSelectedIndex();
        String[] st = showtimes.get(idx);
        int showtimeId = Integer.parseInt(st[0]);
        String theatre = st[1];
        String showDate = st[2];
        String showTime = st[3];
        double total = selected.size() * pricePerSeat;
        String seatStr = String.join(", ", selected);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm booking?\n\n" +
            "Movie: " + movie.getTitle() + "\n" +
            "Theatre: " + theatre + "\n" +
            "Time: " + showTime + "\n" +
            "Seats: " + seatStr + "\n" +
            "Total: ₹" + String.format("%.2f", total),
            "Confirm Booking", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = DBConnection.createBooking(user.getId(),
                showtimeId, movie.getTitle(), theatre, showDate,
                showTime, seatStr, total);
            if (success) {
                dispose();
                new BookingConfirmationFrame(user, movie.getTitle(),
                    theatre, showDate, showTime, seatStr, total)
                    .setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Booking failed! Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}