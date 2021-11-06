package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// this class represents a data object (like a struct in C++), thus all fields are public!
public class Session {
    public static final Integer extraTimeInMin = 15;
    public static final Integer nrOfSeatsRoom1 = 200;
    public static final Integer nrOfSeatsRoom2 = 100;

    public Movie movie;
    public LocalDateTime startTime, endTime;
    public int nrOfSeats;
    public int roomNumber;

    public Session(int roomNumber, Movie movie, LocalDateTime startTime, LocalDateTime endTime, int nrOfSeats) {
        this.roomNumber = roomNumber;
        this.movie = movie;
        this.startTime = startTime;
        this.endTime = endTime;
        this.nrOfSeats = nrOfSeats;
    }

    // function to process ticket sale with validation
    public boolean sellTicket(int nrOfSeats) {
        if (this.nrOfSeats < nrOfSeats)
            return false;
        this.nrOfSeats -= nrOfSeats;
        return true;
    }

    // these getters are used in runtime by TableViews
    public String getStartTime() {
        return startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getEndTime() {
        return endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getTitle() {
        return movie.title;
    }

    public String getPrice() {
        return String.format("%.2f", movie.price);
    }

    public String getNrOfSeats() {
        return String.format("%d", nrOfSeats);
    }
}

