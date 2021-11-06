package ui;

import database.Database;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import model.User;


public class Navigation {
    private final MenuBar menuBar;
    private final Menu adminMenu;
    private final Menu helpMenu;
    private final Menu logoutMenu;
    private final MenuItem purchaseTicketsItem;
    private final MenuItem manageShowingsItem;
    private final MenuItem manageMoviesItem;
    private final MenuItem aboutItem;
    private final MenuItem logoutItem;

    public Navigation() {
        menuBar = new MenuBar();
        adminMenu = new Menu("Admin");
        helpMenu = new Menu("Help");
        logoutMenu = new Menu("Log out");
        purchaseTicketsItem = new MenuItem("Purchase tickets");
        manageShowingsItem = new MenuItem("Manage showings");
        manageMoviesItem = new MenuItem("Manage movies");
        aboutItem = new MenuItem("About");
        logoutItem = new MenuItem("Log out...");
    }

    public MenuBar getMenuBar(Stage window, User user, App app, Database db) {

        if (user.typeOfUser != User.TypeOfUser.NORMAL_USER)
            adminMenu.setVisible(true);
        else
            adminMenu.setVisible(false);

        adminMenu.getItems().addAll(purchaseTicketsItem, manageShowingsItem, manageMoviesItem);
        helpMenu.getItems().addAll(aboutItem);
        logoutMenu.getItems().addAll(logoutItem);

        menuBar.getMenus().addAll(adminMenu, helpMenu, logoutMenu);

        purchaseTicketsItem.setOnAction(actionEvent -> {
            new SellingTickets(app, db, user);
            window.close();

        });

        manageShowingsItem.setOnAction(actionEvent -> {
            new ManageShowings(app, db, user);
            window.close();
        });

        manageMoviesItem.setOnAction(actionEvent -> {
            new ManageMovies(app, db, user);
            window.close();
        });

        aboutItem.setOnAction(actionEvent -> {

        });

        logoutItem.setOnAction(actionEvent -> {
            app.loginContent();
            window.close();
        });

        return menuBar;
    }
}
