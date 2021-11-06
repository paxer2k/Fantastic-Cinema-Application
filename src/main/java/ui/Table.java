package ui;

import database.Database;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Movie;
import model.Session;

public class Table {
    private final Database db;

    public Table(Database db) {
        this.db = db;
    }

    public void addColumnsToTableViewOfRooms(int roomN, TableView<Session> tableViewRoom) {
        tableViewRoom.getColumns().clear();
        tableViewRoom.getItems().clear();
        // define columns
        TableColumn<Session, String> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<Session, String> endColumn = new TableColumn<>("End");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        TableColumn<Session, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Session, String> seatsColumn = new TableColumn<>("Seats");
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("nrOfSeats"));
        seatsColumn.setMinWidth(70);

        TableColumn<Session, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //noinspection unchecked
        tableViewRoom.getColumns().addAll(startColumn, endColumn, titleColumn, seatsColumn, priceColumn);

        //link tableview to session list
        tableViewRoom.setItems(db.getSessionsForRoomNumber(roomN));

    }

    public void addColumnsToTableViewOfMovies(TableView<Movie> tableViewMovies) {

        tableViewMovies.getColumns().clear();
        // tableViewMovies.getItems().clear(); // do not clear Items here - they are pointing to db.allMovies!

        TableColumn<Movie, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Movie, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        durationColumn.setMinWidth(100);

        TableColumn<Movie, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //noinspection unchecked
        tableViewMovies.getColumns().addAll(titleColumn, durationColumn, priceColumn);
        //link tableview to people list

        tableViewMovies.setItems(db.getAllMovies()); // set db.allMovies as Items
    }
}


