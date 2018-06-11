package controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Book;
import view.BookEditDialogController;
import view.BookOverviewController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane motherContainer;

    /**
     * The data as an observable list of Books.
     */
    private ObservableList<Book> bookData = FXCollections.observableArrayList();


    /**
     * Constructor
     */
    public Main() throws SQLException {
        DatabaseTools.initialDB();
        ResultSet res = DatabaseTools.getInitDB();
        while (res.next()) {
            try {
                bookData.add((new Book(res.getInt("ISBN"), res.getString("BookName"),
                        res.getInt("FloorNumber"), res.getInt("ShelfNumber"),
                        res.getBoolean("Available"))));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Returns the data as an observable list of Books.
     *
     * @return bookData
     */
    public ObservableList<Book> getBookData() {
        return bookData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("eLib");

        initRootLayout();

        showBookOverview();
    }

    /**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/MotherContainer.fxml"));
            motherContainer = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(motherContainer);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the book overview inside the root layout.
     */
    private void showBookOverview() {
        try {
            // Load book overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/BookOverview.fxml"));
            AnchorPane bookOverview = loader.load();

            // Set book overview into the center of root layout.
            motherContainer.setCenter(bookOverview);

            // Give the controller access to the main app.
            BookOverviewController controller = loader.getController();
            controller.setMain(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified book. If the user
     * clicks OK, the changes are saved into the provided book object and true
     * is returned.
     *
     * @param book the book object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showBookEditDialog(Book book, boolean editable) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/BookEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Book");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the book into the controller.
            BookEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBook(book);
            if (editable) {
                controller.changeBookNameFieldEdit(true);
                controller.changeIsbnFieldEdit(true);
            } else {
                controller.changeIsbnFieldEdit(false);
                controller.changeBookNameFieldEdit(false);
            }


            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}