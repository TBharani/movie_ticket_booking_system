import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private RoundedTextField nameField, emailField, userField;
    private RoundedPasswordField passField, confirmField;

    public RegisterFrame() {
        setTitle("CinemaHub - Register");
        setSize(ThemeManager.WINDOW_W, ThemeManager.WINDOW_H);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        ThemeManager.centerFrame(this);

        GradientPanel bg = new GradientPanel(
            new Color(10, 10, 30), new Color(30, 10, 35));
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(ThemeManager.padding(25, 40, 25, 40));

        // Title
        JLabel icon = ThemeManager.centeredLabel("🎬",
            new Font("Segoe UI Emoji", Font.PLAIN, 42), ThemeManager.PRIMARY);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(icon);

        JLabel title = ThemeManager.centeredLabel("Create Account",
            ThemeManager.HEADING, ThemeManager.TEXT_WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(5));

        JLabel sub = ThemeManager.centeredLabel("Join CinemaHub today!",
            ThemeManager.SMALL, ThemeManager.TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(sub);
        card.add(Box.createVerticalStrut(22));

        // Fields
        nameField    = addField(card, "Full Name",        "John Doe");
        emailField   = addField(card, "Email",            "john@email.com");
        userField    = addField(card, "Username",         "Choose a username");

        card.add(Box.createVerticalStrut(4));
        JLabel pl = ThemeManager.label("  Password", ThemeManager.BODY_BOLD,
                                       ThemeManager.TEXT_SECONDARY);
        pl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(pl);
        card.add(Box.createVerticalStrut(4));
        passField = new RoundedPasswordField("Create password", 20);
        passField.setMaximumSize(new Dimension(340, 42));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(passField);
        card.add(Box.createVerticalStrut(10));

        JLabel cpl = ThemeManager.label("  Confirm Password", ThemeManager.BODY_BOLD,
                                        ThemeManager.TEXT_SECONDARY);
        cpl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(cpl);
        card.add(Box.createVerticalStrut(4));
        confirmField = new RoundedPasswordField("Confirm password", 20);
        confirmField.setMaximumSize(new Dimension(340, 42));
        confirmField.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(confirmField);
        card.add(Box.createVerticalStrut(22));

        // Register button
        RoundedButton regBtn = new RoundedButton("🎟  Create Account",
            ThemeManager.PRIMARY, ThemeManager.PRIMARY_HOVER);
        regBtn.setMaximumSize(new Dimension(340, 44));
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        regBtn.addActionListener(e -> doRegister());
        card.add(regBtn);
        card.add(Box.createVerticalStrut(14));

        // Back to login
        JPanel linkP = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkP.setOpaque(false);
        JLabel t1 = ThemeManager.label("Already have an account? ",
            ThemeManager.SMALL, ThemeManager.TEXT_MUTED);
        JLabel t2 = ThemeManager.label("Sign In",
            new Font("Segoe UI", Font.BOLD, 12), ThemeManager.NEON_BLUE);
        t2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        t2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        linkP.add(t1); linkP.add(t2);
        linkP.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(linkP);

        // Card wrapper
        JPanel wrapper = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 20, 40, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(new Color(60, 60, 90));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 24, 24);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(430, 640));
        wrapper.add(card);
        bg.add(wrapper);

        getRootPane().setDefaultButton(regBtn);
    }

    private RoundedTextField addField(JPanel card, String label, String placeholder) {
        JLabel l = ThemeManager.label("  " + label, ThemeManager.BODY_BOLD,
                                      ThemeManager.TEXT_SECONDARY);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(l);
        card.add(Box.createVerticalStrut(4));
        RoundedTextField f = new RoundedTextField(placeholder, 20);
        f.setMaximumSize(new Dimension(340, 42));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(f);
        card.add(Box.createVerticalStrut(10));
        return f;
    }

    private void doRegister() {
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();
        String user  = userField.getText().trim();
        String pass  = new String(passField.getPassword());
        String conf  = new String(confirmField.getPassword());

        if (name.isEmpty() || email.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pass.equals(conf)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pass.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DBConnection.registerUser(user, pass, email, name)) {
            JOptionPane.showMessageDialog(this,
                "Account created successfully! Please sign in.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Registration failed. Username may already exist.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}