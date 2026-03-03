import java.awt.Color;

public class Movie {
    private int id;
    private String title, genre, language, description, posterColor, status;
    private int duration;
    private double rating;

    public Movie(int id, String title, String genre, int duration,
                 String language, double rating, String description,
                 String posterColor, String status) {
        this.id          = id;
        this.title       = title;
        this.genre       = genre;
        this.duration    = duration;
        this.language    = language;
        this.rating      = rating;
        this.description = description;
        this.posterColor = posterColor;
        this.status      = status;
    }

    public int    getId()          { return id; }
    public String getTitle()       { return title; }
    public String getGenre()       { return genre; }
    public int    getDuration()    { return duration; }
    public String getLanguage()    { return language; }
    public double getRating()      { return rating; }
    public String getDescription() { return description; }
    public String getPosterColor() { return posterColor; }
    public String getStatus()      { return status; }

    public String getDurationFormatted() {
        return (duration / 60) + "h " + (duration % 60) + "m";
    }

    public Color getPosterColorObj() {
        try { return Color.decode(posterColor); }
        catch (Exception e) { return ThemeManager.PRIMARY; }
    }
}