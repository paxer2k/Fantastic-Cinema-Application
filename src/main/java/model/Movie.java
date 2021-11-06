package model;

// this class represents a data object (like a struct in C++), thus all fields are public!
public class Movie {
    public String title;
    public int durationMinutes;
    public double price;

    public Movie(String title, int durationMinutes, double price) {
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.price = price;
    }

    // these getters are used in runtime by TableViews
    public String getTitle() {
        return title;
    }

    public String getDurationMinutes() {
        return String.format("%d", durationMinutes);
    }

    public String getPrice() {
        return String.format("%.2f", price);
    }
}
