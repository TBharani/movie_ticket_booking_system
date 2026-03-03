import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private RoundedTextField userField;
    private RoundedPasswordField passField;

    public LoginFrame() {
        setTitle("CinemaHub - Login");
        setSize(ThemeManager.WINDOW_W, ThemeManager.WINDOW_H);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        ThemeManager.centerFrame(this);

        GradientPanel bg = new GradientPanel(
            new Color(10, 10, 30), new Color(25, 10, 40));
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        // Center card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 520));
        card.setBorder(ThemeManager.padding(40, 40, 40, 40));

        // Logo / Title
        JLabel icon = ThemeManager.centeredLabel("🎬", new Font("Segoe UI Emoji", Font.PLAIN, 52),
                                                  ThemeManager.PRIMARY);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(icon);
        card.add(Box.createVerticalStrut(5));

        JLabel title = ThemeManager.centeredLabel("CinemaHub",
            ThemeManager.TITLE, ThemeManager.PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel subtitle = ThemeManager.centeredLabel("Your Premium Movie Experience",
            ThemeManager.SMALL, ThemeManager.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createVerticalStrut(35));

        // Username
        JLabel userLbl = ThemeManager.label("  Username", ThemeManager.BODY_BOLD,
                                            ThemeManager.TEXT_SECONDARY);
        userLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(userLbl);
        card.add(Box.createVerticalStrut(6));
        userField = new RoundedTextField("Enter your username", 20);
        userField.setMaximumSize(new Dimension(340, 44));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(userField);
        card.add(Box.createVerticalStrut(18));

        // Password
        JLabel passLbl = ThemeManager.label("  Password", ThemeManager.BODY_BOLD,
                                            ThemeManager.TEXT_SECONDARY);
        passLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(passLbl);
        card.add(Box.createVerticalStrut(6));
        passField = new RoundedPasswordField("Enter your password", 20);
        passField.setMaximumSize(new Dimension(340, 44));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(passField);
        card.add(Box.createVerticalStrut(30));

        // Login Button
        RoundedButton loginBtn = new RoundedButton("🔐  Sign In", ThemeManager.PRIMARY,
                                                     ThemeManager.PRIMARY_HOVER);
        loginBtn.setMaximumSize(new Dimension(340, 46));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.addActionListener(e -> doLogin());
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(18));

        // Register link
        JPanel regPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        regPanel.setOpaque(false);
        JLabel regText = ThemeManager.label("Don't have an account? ",
            ThemeManager.SMALL, ThemeManager.TEXT_MUTED);
        JLabel regLink = ThemeManager.label("Sign Up",
            new Font("Segoe UI", Font.BOLD, 12), ThemeManager.NEON_BLUE);
        regLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new RegisterFrame().setVisible(true);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                regLink.setForeground(ThemeManager.NEON_BLUE.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                regLink.setForeground(ThemeManager.NEON_BLUE);
            }
        });
        regPanel.add(regText);
        regPanel.add(regLink);
        regPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(regPanel);

        // Draw card background
        JPanel cardWrapper = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 20, 40, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(new Color(60, 60, 90));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 24, 24);
                g2.dispose();
            }
        };
        cardWrapper.setOpaque(false);
        cardWrapper.setPreferredSize(new Dimension(430, 530));
        cardWrapper.add(card);

        bg.add(cardWrapper);

        // Decorative elements
        addDecorations(bg);

        // Enter key triggers login
        getRootPane().setDefaultButton(loginBtn);
    }

    private void addDecorations(JPanel bg) {
        // Floating film reel circles in background
        JPanel deco = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // Decorative circles
                g2.setColor(new Color(229, 9, 20, 15));
                g2.fillOval(-80, -80, 300, 300);
                g2.fillOval(getWidth()-200, getHeight()-200, 350, 350);

                g2.setColor(new Color(0, 212, 255, 10));
                g2.fillOval(getWidth()-150, -100, 280, 280);
                g2.fillOval(-100, getHeight()-180, 300, 300);

                // Stars
                g2.setColor(new Color(255, 255, 255, 20));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                for (int i = 0; i < 30; i++) {
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    g2.drawString("✦", x, y);
                }
                g2.dispose();
            }
        };
        deco.setOpaque(false);
        deco.setBounds(0, 0, ThemeManager.WINDOW_W, ThemeManager.WINDOW_H);
        bg.add(deco);
    }

    private void doLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword()).trim();
        if (user.isEmpty() || pass.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }
        User u = DBConnection.authenticate(user, pass);
        if (u == null) {
            showError("Invalid username or password!");
            return;
        }
        dispose();
        if (u.isAdmin()) {
            new AdminDashboard(u).setVisible(true);
        } else {
            new UserDashboard(u).setVisible(true);
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Login Failed",
            JOptionPane.ERROR_MESSAGE);
    }
}