package ui;

import database.Database;
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
import model.User;

public class ManageMovies {
    private final App app;
    private final Database db;
    private final Stage window;
    private final TableView<Movie> tableViewMovie;

    Label pageTitleLabel;
    Label listOfMoviesTitle;

    Label movieTitleLabel;
    Label movieDurationLabel;
    Label moviePriceLabel;
    TextField movieTitleInput;
    TextField movieDurationInput;
    TextField moviePriceInput;
    Button addMovieButton;
    Button clearButton;

    public ManageMovies(App app, Database db, User user) {
        this.app = app;
        this.db = db;
        this.window = new Stage();
        tableViewMovie = new TableView<>();

        manageMoviesContent(user);
    }

    private void manageMoviesContent(User user) {
        pageTitleLabel = new Label("Manage movies");
        pageTitleLabel.setFont(new Font(30));
        pageTitleLabel.setPadding(new Insets(30));
        pageTitleLabel.setId("screenTitle");

        listOfMoviesTitle = new Label("List of movies");
        listOfMoviesTitle.setFont(new Font(15));
        listOfMoviesTitle.setId("listOfMoviesTitle");

        window.setTitle("Fantastic Cinema - Manage Movies - Username: " + user.username);
        window.setResizable(false);

        Table table = new Table(db);
        table.addColumnsToTableViewOfMovies(tableViewMovie);
        tableViewMovie.setMinWidth(420);


        BorderPane container = new BorderPane();
        GridPane mainGrid = new GridPane();
        GridPane manageMoviesGrid = new GridPane();
        manageMoviesGrid.setId("manageMoviesGrid");

        VBox top = new VBox(); // think of different name

        Navigation nav = new Navigation();
        top.getChildren().addAll(nav.getMenuBar(window, user, app, db), pageTitleLabel);

        mainGrid.getChildren().addAll(listOfMoviesTitle, tableViewMovie);
        GridPane.setConstraints(listOfMoviesTitle, 0, 0);
        GridPane.setConstraints(tableViewMovie, 0, 1);
        mainGrid.setPadding(new Insets(30));
        mainGrid.setHgap(10);

        container.setTop(top);
        container.setCenter(mainGrid);
        container.setBottom(manageMoviesGridContent(manageMoviesGrid)); // you know what to do here

        Scene scene = new Scene(container);
        window.setScene(scene);

        scene.getStylesheets().add("style.css");
        JMetro jMetro = new JMetro(container, Style.DARK);

        window.show();
    }

    // change name of method later
    private GridPane manageMoviesGridContent(GridPane manageMoviesGrid) {
        addMoviesGrid(manageMoviesGrid);
        addMovieOnAction();
        clearOnAction();

        return manageMoviesGrid;
    }

    private void addMoviesGrid(GridPane manageMoviesGrid) {
        movieTitleLabel = new Label("Movie title:");
        movieDurationLabel = new Label("Movie duration:");
        moviePriceLabel = new Label("Movie price:");
        movieTitleInput = new TextField();
        movieDurationInput = new TextField();
        moviePriceInput = new TextField();
        addMovieButton = new Button("Add movie");
        clearButton = new Button("Clear");

        movieTitleLabel.setId("movieTitleLabel");
        movieDurationLabel.setId("movieDurationLabel");
        moviePriceLabel.setId("moviePriceLabel");

        GridPane.setConstraints(movieTitleLabel, 0, 0);
        GridPane.setConstraints(movieDurationLabel, 0, 1);
        GridPane.setConstraints(moviePriceLabel, 0, 2);
        GridPane.setConstraints(movieTitleInput, 1, 0);
        GridPane.setConstraints(movieDurationInput, 1, 1);
        GridPane.setConstraints(moviePriceInput, 1, 2);
        GridPane.setConstraints(addMovieButton, 2, 1);
        GridPane.setConstraints(clearButton, 2, 2);

        manageMoviesGrid.getChildren().addAll(movieTitleLabel, movieDurationLabel, moviePriceLabel,
                movieTitleInput, movieDurationInput, moviePriceInput, addMovieButton, clearButton);
        manageMoviesGrid.setPadding(new Insets(10));
        manageMoviesGrid.setHgap(30);
        manageMoviesGrid.setVgap(8);
    }

    private void addMovieOnAction() {
        addMovieButton.setOnAction(actionEvent -> {
            try {
                String movieTitle = movieTitleInput.getText();
                if (movieTitle.isEmpty()) {
                    new Alert(Alert.AlertType.INFORMATION, "Please fill out the movie title").showAndWait();
                    return;
                }
                ;
                if (movieDurationInput.getText().trim().length() <= 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Please fill out the movie duration").showAndWait();
                    return;
                }
                ;
                if (moviePriceInput.getText().trim().length() <= 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Please fill out movie price").showAndWait();
                    return;
                }
                ;
                Integer movieDuration = Integer.parseInt(movieDurationInput.getText());
                Double moviePrice = Double.parseDouble(moviePriceInput.getText());


                if (db.addMovie(new Movie(movieTitle, movieDuration, moviePrice))) {
                    // if(!db.saveMovies()) { handle_error(); return; }; // we do not save movies for now
                    new Alert(Alert.AlertType.INFORMATION, movieTitle + " was successfully added.").showAndWait();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Something went wrong while trying to add " + movieTitle + ".").showAndWait();
                }
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                new Alert(Alert.AlertType.WARNING, "Please enter whole number for duration or a proper decimal for price").showAndWait();
            }
        });
    }

    private void clearOnAction() {
        clearButton.setOnAction(actionEvent -> {
            movieTitleInput.clear();
            movieDurationInput.clear();
            moviePriceInput.clear();
        });
    }

}
