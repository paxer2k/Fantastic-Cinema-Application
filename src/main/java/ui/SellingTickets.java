package ui;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import model.Session;
import model.User;

public class SellingTickets {
    private final App app;
    private final Database db;
    private final Stage window;

    // creation of the tableviews
    private final TableView<Session> tableViewRoom1;
    private final TableView<Session> tableViewRoom2;

    // general components
    Label pageTitleLabel;
    Label room1TitleLabel;
    Label room2TitleLabel;

    // grid components
    Label roomNumberLabel;
    Label roomNumberInput;
    Label startTimeLabel;
    Label startTimeInput;
    Label endTimeLabel;
    Label endTimeInput;
    Label movieTitleLabel;
    Label movieTitleInput;
    Label nrOfSeatsRoomLabel;
    Label nameLabel;
    ComboBox<Integer> nrOfSeatsCombo;
    TextField nameInput;
    Button purchaseButton;
    Button clearButton;

    // constructor of the sellingtickets
    public SellingTickets(App app, Database db, User user) {
        this.app = app;
        this.db = db;
        this.window = new Stage();
        tableViewRoom1 = new TableView<>();
        tableViewRoom2 = new TableView<>();

        sellingTicketsContent(user);
    }

    // basic code that you would put in the constructor
    private void sellingTicketsContent(User user) {
        pageTitleLabel = new Label("Purchasing Tickets");
        pageTitleLabel.setFont(new Font(30));
        pageTitleLabel.setPadding(new Insets(30));
        pageTitleLabel.setId("screenTitle");

        room1TitleLabel = new Label("Room 1");
        room1TitleLabel.setFont(new Font(15));
        room1TitleLabel.setId("titleRoom1");

        room2TitleLabel = new Label("Room 2");
        room2TitleLabel.setFont(new Font(15));
        room2TitleLabel.setId("titleRoom2");

        window.setTitle("Fantastic Cinema - Purchase tickets - Username: " + user.username);
        window.setResizable(false);

        Table table = new Table(db);
        table.addColumnsToTableViewOfRooms(1, tableViewRoom1);
        table.addColumnsToTableViewOfRooms(2, tableViewRoom2);
        tableViewRoom1.setMinWidth(610);
        tableViewRoom2.setMinWidth(610);

        BorderPane container = new BorderPane();
        GridPane mainGrid = new GridPane();
        GridPane tableViewComponentGrid = new GridPane();
        tableViewComponentGrid.setId("tableViewComponentGrid");
        VBox top = new VBox(); // think of a different name...

        // methods for selecting an index in tableview and extracting info
        selectIndexChangedTableViewRoom1(container);
        selectIndexChangedTableViewRoom2(container);

        // navigation bar
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

        Scene scene = new Scene(container);
        window.setScene(scene);

        scene.getStylesheets().add("style.css");
        JMetro jMetro = new JMetro(container, Style.DARK);

        window.show();
    }

    private void selectIndexChangedTableViewRoom1(BorderPane borderPane) {
        tableViewRoom1.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                Session session = tableViewRoom1.getSelectionModel().getSelectedItem();
                String roomTitle = room1TitleLabel.getText();
                processOnMouseClick(session, roomTitle, borderPane);
            } // if(event.getButton().equals(MouseButton.PRIMARY))
        });
    }

    private void selectIndexChangedTableViewRoom2(BorderPane borderPane) {
        tableViewRoom2.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                Session session = tableViewRoom2.getSelectionModel().getSelectedItem();
                String roomTitle = room2TitleLabel.getText();
                processOnMouseClick(session, roomTitle, borderPane);
            } // if(event.getButton().equals(MouseButton.PRIMARY))
        });
    }

    private void processOnMouseClick(Session session, String roomTitle, BorderPane borderPane) {
        if (session == null)
            return;

        GridPane tableViewComponentGrid = new GridPane();
        tableViewComponentGrid.setId("tableViewComponentGrid");

        addPurchaseGrid(tableViewComponentGrid, roomTitle, session, borderPane);
        purchaseOnAction(session, tableViewComponentGrid);
        clearOnAction(tableViewComponentGrid);
    }

    private void addPurchaseGrid(GridPane tableViewComponentGrid, String roomTitle, Session session, BorderPane borderPane) {
        tableViewComponentGrid.setId("tableViewComponentGrid");

        roomNumberLabel = new Label("Room:");
        roomNumberInput = new Label(roomTitle);
        startTimeLabel = new Label("Start:");
        startTimeInput = new Label(session.getStartTime());

        endTimeLabel = new Label("End:");
        endTimeInput = new Label(session.getEndTime());

        movieTitleLabel = new Label("Movie title:");
        movieTitleInput = new Label(session.getTitle());

        nrOfSeatsRoomLabel = new Label("Nr. of seats:");
        nrOfSeatsCombo = new ComboBox();
        nrOfSeatsCombo.setId("nrOfSeatsComboBox");
        ObservableList<Integer> numbers = FXCollections.observableArrayList();
        numbers.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        nrOfSeatsCombo.setItems(numbers);

        nameLabel = new Label("Name");
        nameInput = new TextField();

        purchaseButton = new Button("Purchase");
        purchaseButton.setMinWidth(100);
        clearButton = new Button("Clear");
        clearButton.setMinWidth(100);

        // settings placemenets for components on the grid
        GridPane.setConstraints(roomNumberLabel, 0, 0);
        GridPane.setConstraints(roomNumberInput, 1, 0);
        GridPane.setConstraints(startTimeLabel, 0, 1);
        GridPane.setConstraints(startTimeInput, 1, 1);
        GridPane.setConstraints(endTimeLabel, 0, 2);
        GridPane.setConstraints(endTimeInput, 1, 2);
        GridPane.setConstraints(movieTitleLabel, 2, 0);
        GridPane.setConstraints(movieTitleInput, 3, 0);
        GridPane.setConstraints(nrOfSeatsRoomLabel, 2, 1);
        GridPane.setConstraints(nrOfSeatsCombo, 3, 1);
        GridPane.setConstraints(nameLabel, 2, 2);
        GridPane.setConstraints(nameInput, 3, 2);
        GridPane.setConstraints(purchaseButton, 4, 1);
        GridPane.setConstraints(clearButton, 4, 2);

        tableViewComponentGrid.getChildren().addAll(roomNumberLabel, roomNumberInput, startTimeLabel, startTimeInput, endTimeLabel, endTimeInput
                , movieTitleLabel, movieTitleInput, nrOfSeatsRoomLabel, nrOfSeatsCombo, nameLabel, nameInput, purchaseButton, clearButton);
        tableViewComponentGrid.setPadding(new Insets(10));
        tableViewComponentGrid.setHgap(30);
        tableViewComponentGrid.setVgap(8);

        borderPane.setBottom(tableViewComponentGrid);
    }

    private void purchaseOnAction(Session session, GridPane tableViewComponentGrid) {
        purchaseButton.setOnAction(actionEvent -> {
            if (nrOfSeatsCombo.getSelectionModel().getSelectedItem() == null) {
                new Alert(Alert.AlertType.WARNING, "Please choose proper amount").showAndWait();
                return;
            }
            if (nameInput.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please fill out the Name field").showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please confirm your purchase for the movie: " + session.getTitle() + ", " + nameInput.getText(), ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                if (!session.sellTicket(nrOfSeatsCombo.getValue())) {
                    new Alert(Alert.AlertType.WARNING, "Not enough tickets for transaction!").showAndWait();
                    return;
                }
                if (session.roomNumber == 1)
                    tableViewRoom1.refresh();
                else
                    tableViewRoom2.refresh();
                tableViewComponentGrid.setVisible(false);
            }
        });
    }

    private void clearOnAction(GridPane tableViewComponentGrid) {
        clearButton.setOnAction(actionEvent -> {
            tableViewComponentGrid.setVisible(false);
        });
    }
}
