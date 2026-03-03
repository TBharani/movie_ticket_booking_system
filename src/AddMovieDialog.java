import javax.swing.*;
import java.awt.*;

public class AddMovieDialog extends JDialog {

    private RoundedTextField titleField, genreField, durationField,
                             langField, ratingField, colorField;
    private JTextArea descArea;
    private JComboBox<String> statusCombo;
    private Movie editMovie;

    public AddMovieDialog(JFrame parent, Movie movie) {
        super(parent, movie == null ? "Add Movie" : "Edit Movie", true);
        this.editMovie = movie;
        setSize(500, 620);
        setResizable(false);
        setLocationRelativeTo(parent);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(ThemeManager.BG_DARK);
        main.setBorder(ThemeManager.padding(25, 30, 25, 30));

        // Title
        JLabel header = ThemeManager.centeredLabel(
            movie == null ? "🎬 Add New Movie" : "✏️ Edit Movie",
            ThemeManager.HEADING, ThemeManager.GOLD);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(header);
        main.add(Box.createVerticalStrut(20));

        // Form fields
        titleField    = addFormField(main, "Movie Title",   "Enter movie title");
        genreField    = addFormField(main, "Genre",         "e.g. Action / Sci-Fi");
        durationField = addFormField(main, "Duration (min)","e.g. 148");
        langField     = addFormField(main, "Language",      "e.g. English");
        ratingField   = addFormField(main, "Rating (0-10)", "e.g. 8.5");
        colorField    = addFormField(main, "Poster Color",  "#E50914");

        // Status
        JLabel sLbl = ThemeManager.label("  Status", ThemeManager.BODY_BOLD,
                                         ThemeManager.TEXT_SECONDARY);
        sLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(sLbl);
        main.add(Box.createVerticalStrut(4));
        statusCombo = new JComboBox<>(new String[]{"Now Showing", "Coming Soon"});
        statusCombo.setFont(ThemeManager.BODY);
        statusCombo.setMaximumSize(new Dimension(420, 38));
        statusCombo.setBackground(ThemeManager.SURFACE);
        statusCombo.setForeground(ThemeManager.TEXT_WHITE);
        statusCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(statusCombo);
        main.add(Box.createVerticalStrut(12));

        // Description
        JLabel dLbl = ThemeManager.label("  Description", ThemeManager.BODY_BOLD,
                                         ThemeManager.TEXT_SECONDARY);
        dLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(dLbl);
        main.add(Box.createVerticalStrut(4));
        descArea = new JTextArea(3, 20);
        descArea.setFont(ThemeManager.BODY);
        descArea.setBackground(ThemeManager.SURFACE);
        descArea.setForeground(ThemeManager.TEXT_WHITE);
        descArea.setCaretColor(ThemeManager.NEON_BLUE);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(ThemeManager.padding(8, 12, 8, 12));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setMaximumSize(new Dimension(420, 70));
        descScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        descScroll.setBorder(BorderFactory.createLineBorder(ThemeManager.SURFACE_LIGHT));
        main.add(descScroll);
        main.add(Box.createVerticalStrut(18));

        // Buttons
        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnP.setOpaque(false);
        btnP.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton saveBtn = new RoundedButton(
            movie == null ? "➕ Add Movie" : "💾 Save Changes",
            ThemeManager.SUCCESS, ThemeManager.SUCCESS.brighter());
        saveBtn.setPreferredSize(new Dimension(180, 42));
        saveBtn.addActionListener(e -> saveMovie());
        btnP.add(saveBtn);

        RoundedButton cancelBtn = new RoundedButton("Cancel",
            ThemeManager.SURFACE_LIGHT, ThemeManager.SURFACE_LIGHT.brighter());
        cancelBtn.setPreferredSize(new Dimension(120, 42));
        cancelBtn.addActionListener(e -> dispose());
        btnP.add(cancelBtn);

        main.add(btnP);

        // Pre-fill if editing
        if (movie != null) {
            titleField.setText(movie.getTitle());
            genreField.setText(movie.getGenre());
            durationField.setText(String.valueOf(movie.getDuration()));
            langField.setText(movie.getLanguage());
            ratingField.setText(String.valueOf(movie.getRating()));
            colorField.setText(movie.getPosterColor());
            descArea.setText(movie.getDescription());
            statusCombo.setSelectedItem(movie.getStatus());
        }

        setContentPane(new JScrollPane(main) {{
            setBorder(null);
            getViewport().setBackground(ThemeManager.BG_DARK);
        }});
    }

    private RoundedTextField addFormField(JPanel panel, String label, String placeholder) {
        JLabel l = ThemeManager.label("  " + label, ThemeManager.BODY_BOLD,
                                      ThemeManager.TEXT_SECONDARY);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(l);
        panel.add(Box.createVerticalStrut(4));
        RoundedTextField f = new RoundedTextField(placeholder, 20);
        f.setMaximumSize(new Dimension(420, 40));
        f.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(f);
        panel.add(Box.createVerticalStrut(8));
        return f;
    }

    private void saveMovie() {
        String title = titleField.getText().trim();
        String genre = genreField.getText().trim();
        String durStr = durationField.getText().trim();
        String lang = langField.getText().trim();
        String ratStr = ratingField.getText().trim();
        String color = colorField.getText().trim();
        String desc = descArea.getText().trim();
        String status = (String) statusCombo.getSelectedItem();

        if (title.isEmpty() || genre.isEmpty() || durStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Title, Genre, and Duration are required!",
                "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int dur;
        double rating;
        try {
            dur = Integer.parseInt(durStr);
            rating = ratStr.isEmpty() ? 0 : Double.parseDouble(ratStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Duration must be a number and Rating a decimal.",
                "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (color.isEmpty()) color = "#E50914";
        if (lang.isEmpty()) lang = "English";

        boolean success;
        if (editMovie == null) {
            success = DBConnection.addMovie(title, genre, dur, lang,
                                            rating, desc, color, status);
        } else {
            success = DBConnection.updateMovie(editMovie.getId(), title,
                genre, dur, lang, rating, desc, color, status);
        }

        if (success) {
            JOptionPane.showMessageDialog(this,
                editMovie == null ? "Movie added successfully!"
                                  : "Movie updated successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Operation failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}