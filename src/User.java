public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String role;

    public User(int id, String username, String password,
                String email, String fullName, String role) {
        this.id       = id;
        this.username = username;
        this.password = password;
        this.email    = email;
        this.fullName = fullName;
        this.role     = role;
    }

    public int    getId()       { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail()   { return email; }
    public String getFullName(){ return fullName; }
    public String getRole()    { return role; }
    public boolean isAdmin()   { return "admin".equalsIgnoreCase(role); }
}