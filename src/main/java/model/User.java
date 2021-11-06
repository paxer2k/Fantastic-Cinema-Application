package model;

// this class represents a data object (like a struct in C#), thus all fields are public!
// except password field - it is private for limited access
public class User {
    public enum TypeOfUser {
        NORMAL_USER, ADMIN
    }

    public String username;
    private String password;
    public TypeOfUser typeOfUser;

    public User(String username, String password, TypeOfUser typeOfUser) {
        this.username = username;
        this.password = password;
        this.typeOfUser = typeOfUser;
    }

    public String getPassword() {
        return password;
    }
}
