package app.entities;

public class User {
    private int userId;
    private String name;
    private String password;
    private String email;
    private boolean admin;
    private double balance;

    public User(int userId, String name, String password, String email, boolean admin, double balance) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.admin = admin;
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean getAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", admin=" + admin +
                ", balance=" + balance +
                '}';
    }
}
