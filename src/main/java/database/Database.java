package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Movie;
import model.Session;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class Database {
    private final ObservableList<User> allUsers;
    private final ObservableList<Movie> allMovies;
    private final ObservableList<Session> allSessions;

    public Database() {
        allUsers = FXCollections.observableArrayList();
        allMovies = FXCollections.observableArrayList();
        allSessions = FXCollections.observableArrayList();
        readAllFiles();
    }

    private void readAllFiles() {
        readUsers();
        readMovies();
        readSessions();
    }

    private void readUsers() {
        try {
            List<String> sl = Files.readAllLines(Path.of("src/main/resources/users.csv"));
            sl.remove(0);
            for (String line : sl) {
                String[] row = line.split(",");
                User.TypeOfUser type = User.TypeOfUser.valueOf(row[2].trim());
                allUsers.add(new User((row[0].trim()), row[1].trim(), type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readMovies() {
        try {
            List<String> sl = Files.readAllLines(Path.of("src/main/resources/movies.csv"));
            sl.remove(0);
            for (String line : sl) {
                String[] row = line.split(",");
                allMovies.add(new Movie(row[0], Integer.parseInt(row[1]), Double.parseDouble(row[2])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readSessions() {
        try {
            List<String> sl = Files.readAllLines(Path.of("src/main/resources/sessions.csv"));
            sl.remove(0);
            for (String line : sl) {
                String[] row = line.split(",");
                allSessions.add(new Session(Integer.parseInt(row[0]), findMovieByTitle(row[1]), LocalDateTime.parse(row[2]), LocalDateTime.parse(row[3]), Integer.parseInt(row[4])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User validationUser(String username, String password) {
        for (User user : allUsers) {
            if (user.username.equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public boolean saveMovies() {
        try (Writer writer = new FileWriter("src/main/resources/allMovies.csv")) {
            writer.write("title,durationMinutes,price");
            for (Movie movie : allMovies) {
                String movieString = String.format("\n%s,%s,%s",
                        movie.title,
                        movie.durationMinutes,
                        movie.price);
                writer.write(movieString);
            }
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public boolean saveSessions() {
        try (Writer writer = new FileWriter("src/main/resources/allSessions.csv")) {
            writer.write("roomNumber,movie,startTime,endTime,maxNrOfSeats");
            for (Session session : allSessions) {
                String sessionString = String.format("\n%d,%s,%s,%s,%d",
                        session.roomNumber,
                        session.movie.title,
                        session.startTime,
                        session.endTime,
                        session.nrOfSeats);
                writer.write(sessionString);
            }
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public Movie findMovieByTitle(String title) throws Exception {
        for (Movie m : allMovies) {
            if (m.title.equals(title))
                return m;
        }
        throw new Exception("Movie not found: " + title);
    }

    public boolean addMovie(Movie movie) {
        return allMovies.add(movie);
    }

    public ObservableList<Movie> getAllMovies() {
        return allMovies;
    }

    public ObservableList<Session> getSessionsForRoomNumber(int roomN) {
        ObservableList<Session> sessForRoomN = FXCollections.observableArrayList();
        for (Session s : allSessions) {
            if (s.roomNumber == roomN)
                sessForRoomN.add(s);
        }
        return sessForRoomN;
    }


    public boolean addSession(int roomNumber, int maxNrOfSeats, Movie movie, LocalDateTime startTime, LocalDateTime endTime) throws Exception {
        Session newSess = new Session(roomNumber, movie, startTime, endTime, maxNrOfSeats);
        // check whether the movies are overlapping each other, if not, add to session
        ObservableList<Session> sessionsForRoomN = getSessionsForRoomNumber(roomNumber);
        LocalDateTime S15 = startTime.minusMinutes(Session.extraTimeInMin);
        LocalDateTime E15 = endTime.plusMinutes(Session.extraTimeInMin);
        boolean isBadRange = false;
        for (Session s : sessionsForRoomN) {
            LocalDateTime St = s.startTime;
            LocalDateTime Et = s.endTime;
            if ((St.isBefore(S15) || St.equals(S15)) && (S15.isBefore(Et))) isBadRange = true;
            if (St.isBefore(E15) && (E15.isBefore(Et) || (E15.equals(Et)))) isBadRange = true;
            if ((S15.isBefore(St) || S15.equals(St)) && (Et.isBefore(E15) || Et.equals(E15))) isBadRange = true;
            if (isBadRange)
                return false;
        }
        allSessions.add(newSess);
        return true;
    }
}
