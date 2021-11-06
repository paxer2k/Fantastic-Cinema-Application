package ui;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.Movie;
import model.Session;
import model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ManageShowings {
    private final App app;
    private final Database db;
    private final Stage window;
    private final TableView<Session> tableViewRoom1;
    private final TableView<Session> tableViewRoom2;

    // components for main screen
    Label pageTitleLabel;
    Label room1TitleLabel;
    Label room2TitleLabel;

    // components for grid
    Label movieTitleLabel;
    ComboBox<String> movieTitleCombo;
    Label roomLabel;
    ComboBox<Integer> roomNumberCombo;
    Label nrOfSeatsLabel;
    Label nrOfSeatsInput;
    Label startTimeLabel;
    DatePicker startTimeDatePicker;
    Label endTimeLabel;
    Label endTimeInput;
    Label moviePriceLabel;
    Label moviePriceInput;
    TextField startTimeInput;
    Button addShowingButton;
    Button clearButton;


    public ManageShowings(App app, Database db, User user) {
        this.app = app;
        this.db = db;
        this.window = new Stage();
        tableViewRoom1 = new TableView<>();
        tableViewRoom2 = new TableView<>();

        manageShowingsContent(user);
    }

    private void manageShowingsContent(User user) {
        // initialization of components
        pageTitleLabel = new Label("Manage Showings");
        pageTitleLabel.setFont(new Font(30));
        pageTitleLabel.setPadding(new Insets(30));
        pageTitleLabel.setId("screenTitle");

        room1TitleLabel = new Label("Room 1");
        room1TitleLabel.setFont(new Font(15));
        room1TitleLabel.setId("titleRoom1");

        room2TitleLabel = new Label("Room 2");
        room2TitleLabel.setFont(new Font(15));
        room2TitleLabel.setId("titleRoom2");

        window.setTitle("Fantastic Cinema - Manage Showings - Username: " + user.username);
        window.setResizable(false);

        Table table = new Table(db);
        table.addColumnsToTableViewOfRooms(1, tableViewRoom1);
        table.addColumnsToTableViewOfRooms(2, tableViewRoom2);
        tableViewRoom1.setMinWidth(610);
        tableViewRoom2.setMinWidth(610);

        BorderPane container = new BorderPane();
        GridPane mainGrid = new GridPane();
        GridPane showingsComponentGrid = new GridPane();
        showingsComponentGrid.setId("showingComponentGrid");

        VBox top = new VBox(); // think of different name...

        Navigation nav = new Navigation();
        top.getChildren().addAll(nav.getMenuBar(window, user, app, db), pageTitleLabel);

        mainGrid.getChildren().addAll(room1TitleLabel, room2TitleLabel, tableViewRoom1, tableViewRoom2);
        GridPane.setConstraints(room1TitleLabel, 0, 0);
        GridPane.setConstraints(room2TitleLabel, 1, 0);
        GridPane.setConstraints(tableViewRoom1, 0, 1);
        GridPane.setConstraints(tableViewRoom2, 1, 1);
        mainGrid.setPadding(new Insets(30));
        mainGrid.setHgap(10);

        container.setTop(top);
        container.setCenter(mainGrid);
        container.setBottom(manageShowingsGridContent(showingsComponentGrid));


        Scene scene = new Scene(container);
        window.setScene(scene);

        scene.getStylesheets().add("style.css");
        JMetro jMetro = new JMetro(container, Style.DARK);

        window.show();
    }

    // change name of method later
    private GridPane manageShowingsGridContent(GridPane showingsComponentGrid) {
        addShowingsGrid(showingsComponentGrid);
        fillComboBoxes();
        fillComponentsWithData();
        addShowingOnAction();
        clearOnAction(showingsComponentGrid);

        return showingsComponentGrid;
    }

    private void addShowingsGrid(GridPane showingComponentGrid) {
        movieTitleLabel = new Label("Movie title:");
        movieTitleCombo = new ComboBox<>();
        roomLabel = new Label("Room");
        roomNumberCombo = new ComboBox<>();
        nrOfSeatsLabel = new Label("No. of seats");
        nrOfSeatsInput = new Label();
        startTimeLabel = new Label("Start");
        startTimeDatePicker = new DatePicker();
        endTimeLabel = new Label("End");
        endTimeInput = new Label();
        moviePriceLabel = new Label("Price");
        moviePriceInput = new Label();
        startTimeInput = new TextField();
        addShowingButton = new Button("Add showing");
        clearButton = new Button("Clear");

        // set ids
        movieTitleCombo.setId("movieTitleCombo");
        roomNumberCombo.setId("roomCombo");
        startTimeDatePicker.setId("startDatePicker");

        // set constraints for grid
        GridPane.setConstraints(movieTitleLabel, 0, 0);
        GridPane.setConstraints(movieTitleCombo, 1, 0);
        GridPane.setConstraints(roomLabel, 0, 1);
        GridPane.setConstraints(roomNumberCombo, 1, 1);
        GridPane.setConstraints(nrOfSeatsLabel, 0, 2);
        GridPane.setConstraints(nrOfSeatsInput, 1, 2);
        GridPane.setConstraints(startTimeLabel, 2, 0);
        GridPane.setConstraints(startTimeDatePicker, 3, 0);
        GridPane.setConstraints(endTimeLabel, 2, 1);
        GridPane.setConstraints(endTimeInput, 3, 1);
        GridPane.setConstraints(moviePriceLabel, 2, 2);
        GridPane.setConstraints(moviePriceInput, 3, 2);
        GridPane.setConstraints(startTimeInput, 4, 0);
        GridPane.setConstraints(addShowingButton, 5, 1);
        GridPane.setConstraints(clearButton, 5, 2);

        // add components to grid
        showingComponentGrid.getChildren().addAll(movieTitleLabel, movieTitleCombo, roomLabel, roomNumberCombo, nrOfSeatsLabel,
                nrOfSeatsInput, startTimeLabel, startTimeDatePicker, endTimeLabel, endTimeInput, moviePriceLabel,
                moviePriceInput, startTimeInput, addShowingButton, clearButton);
        showingComponentGrid.setPadding(new Insets(10));
        showingComponentGrid.setHgap(30);
        showingComponentGrid.setVgap(8);
    }

    private void fillComboBoxes() {

        ObservableList<String> moviesListCombo = FXCollections.observableArrayList();
        for (Movie movie : db.getAllMovies())
            moviesListCombo.add(movie.title);
        movieTitleCombo.setItems(moviesListCombo);

        ObservableList<Integer> roomsListCombo = FXCollections.observableArrayList();
        roomsListCombo.addAll(1, 2);
        roomNumberCombo.setItems(roomsListCombo);
    }

    private void fillComponentsWithData() {
        EventHandler handleAnyChanges = actionEvent ->
        {
            try {
                Movie movie = db.findMovieByTitle(movieTitleCombo.getSelectionModel().getSelectedItem());
                moviePriceInput.setText(String.format("%.2f", movie.price));

                Integer selectedRoom = roomNumberCombo.getSelectionModel().getSelectedItem();
                if (selectedRoom == null) nrOfSeatsInput.setText("");
                else if (selectedRoom == 1) nrOfSeatsInput.setText(Session.nrOfSeatsRoom1.toString());
                else if (selectedRoom == 2) nrOfSeatsInput.setText(Session.nrOfSeatsRoom2.toString());
                else // impossible, but better to have for fool-proof protection :-)
                    nrOfSeatsInput.setText("");

                LocalDate startDate = startTimeDatePicker.getValue();
                String strStartTime = startTimeInput.getText().trim();
                if (startDate == null || strStartTime.isEmpty()) {
                    endTimeInput.setText("");
                    return;
                }
                ;
                try {
                    LocalTime startTime = LocalTime.parse(strStartTime); // 23:59
                    LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
                    LocalDateTime endTime = startDateTime.plusMinutes(movie.durationMinutes);
                    endTimeInput.setText(endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                } catch (Exception x) {
                    endTimeInput.setText("bad start time (must be like 23:59)");
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
            }
        };
        roomNumberCombo.setOnAction(handleAnyChanges);
        movieTitleCombo.setOnAction(handleAnyChanges);
        startTimeDatePicker.setOnAction(handleAnyChanges);
        startTimeInput.setOnKeyTyped(handleAnyChanges);
    }

    private void addShowingOnAction() {
        addShowingButton.setOnAction(actionEvent -> {
            String missingFields = "";
            if (movieTitleCombo.getSelectionModel().getSelectedItem() == null)
                missingFields += " Title";
            if (roomNumberCombo.getSelectionModel().getSelectedItem() == null)
                missingFields += " Room";
            if (startTimeDatePicker.getValue() == null)
                missingFields += " StartDate";
            if (startTimeInput.getText() == null)
                missingFields += " StartTime";
            if (missingFields.length() > 0) {
                new Alert(Alert.AlertType.WARNING, "Please fill out the missing fields: " + missingFields).showAndWait();
                return;
            }

            try {
                int roomNumber = roomNumberCombo.getValue();
                int maxNrOfSeats = Integer.parseInt(nrOfSeatsInput.getText());
                Movie movie = db.findMovieByTitle(movieTitleCombo.getSelectionModel().getSelectedItem());
                LocalDateTime startDateTime;
                try {
                    LocalDate startDate = startTimeDatePicker.getValue();
                    String strStartTime = startTimeInput.getText().trim();
                    LocalTime startTime = LocalTime.parse(strStartTime); // 23:59
                    startDateTime = LocalDateTime.of(startDate, startTime);
                } catch (Exception x) {
                    endTimeInput.setText("bad start time (must be like 23:59)");
                    return;
                }
                LocalDateTime endTime = startDateTime.plusMinutes(movie.durationMinutes);

                if (!db.addSession(roomNumber, maxNrOfSeats, movie, startDateTime, endTime)) {
                    // if(!db.saveSessions()) { handle_error(); return; }; // we do not save sessions for now
                    new Alert(Alert.AlertType.WARNING,
                            "The end time is overlapping another session. Please enter a valid start time!")
                            .showAndWait();
                    return;
                }
                if (roomNumber == 1) {
                    var items1 = tableViewRoom1.getItems();
                    items1.clear();
                    items1.addAll(db.getSessionsForRoomNumber(1));
                    tableViewRoom1.refresh();
                } else {
                    var items2 = tableViewRoom2.getItems();
                    items2.clear();
                    items2.addAll(db.getSessionsForRoomNumber(2));
                    tableViewRoom2.refresh();
                }
                new Alert(Alert.AlertType.INFORMATION, movie.title + " was successfully added to Room " + roomNumber + ".").showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void clearOnAction(GridPane showingComponentGrid) {
        clearButton.setOnAction(actionEvent -> {
            showingComponentGrid.setVisible(false);
        });
    }
}
