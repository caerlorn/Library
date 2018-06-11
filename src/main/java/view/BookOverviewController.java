package view;

import controller.DatabaseTools;
import controller.Main;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Book;

import java.sql.SQLException;


public class BookOverviewController {

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, Integer> isbnColumn;
    @FXML
    private TableColumn<Book, String> bookNameColumn;
    @FXML
    private Label isbnLabel;
    @FXML
    private Label bookNameLabel;
    @FXML
    private Label shelfNumberLabel;
    @FXML
    private Label floorNumberLabel;
    @FXML
    private Label availabilityLabel;
    @FXML
    private TextField searchField;


    private int isbn;
    private FilteredList<Book> filteredData;
    private SortedList<Book> sortedData;


    // Reference to the main application.
    private Main main;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public BookOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the book table with the two columns.
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty().asObject());
        bookNameColumn.setCellValueFactory(cellData -> cellData.getValue().bookNameProperty());

        // Clear book details.
        showBookDetails(null);

        // Listen for selection changes and show the book details when changed.
        bookTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showBookDetails(newValue));

        //Set the filter Predicate whenever the filter changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                // If search text is empty, display all books.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare isbn and book name of every books with the search text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (Integer.toString(book.getIsbn()).contains(newValue)) {
                    return true; // Filter matches isbn
                } else if (book.getBookName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches book name.
                }
                return false; // Does not match.
            });
        });
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;

        // Add observable list data to the table
        //bookTable.setItems(main.getBookData());

        //Wrap the ObservableList in a FilteredList.
        filteredData = new FilteredList<>(main.getBookData(), p -> true);

        // Wrap the FilteredList in a SortedList.
        sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(bookTable.comparatorProperty());

        // Add sorted (and filtered) data to the table.
        bookTable.setItems(sortedData);

    }

    /**
     * Fills all text fields to show details about the book.
     * If the specified book is null, all text fields are cleared.
     *
     * @param book the book or null
     */
    private void showBookDetails(Book book) {
        if (book != null) {
            // Fill the labels with info from the book object.
            isbnLabel.setText(Integer.toString(book.getIsbn()));
            bookNameLabel.setText(book.getBookName());
            shelfNumberLabel.setText(Integer.toString(book.getShelfNumber()));
            floorNumberLabel.setText(Integer.toString(book.getFloorNumber()));
            availabilityLabel.setText(Boolean.toString(book.isAvailability()));
        } else {
            // Book is null, remove all the text.
            isbnLabel.setText("");
            bookNameLabel.setText("");
            shelfNumberLabel.setText("");
            floorNumberLabel.setText("");
            availabilityLabel.setText("");
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteBook() {
        int selectedIndex = bookTable.getSelectionModel().getSelectedIndex();
        int sourceIndex = sortedData.getSourceIndexFor(main.getBookData(), selectedIndex);

        if (selectedIndex >= 0) {
            isbn = bookTable.getSelectionModel().getSelectedItem().getIsbn();
            DatabaseTools.deleteBook(isbn);
            //bookTable.getItems().remove(selectedIndex); //can't remove from sorted list with selected index
            main.getBookData().remove(sourceIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Book Selected");
            alert.setContentText("Please select a book in the table.");
            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new book.
     */
    @FXML
    private void handleNewBook() {
        //Book tempBook = new Book(5005, "Shadow of the Sun", 4, 30, TRUE);
        Book tempBook = new Book(0, "", 0, 0, false);
        boolean okClicked = main.showBookEditDialog(tempBook, true);
        if (okClicked) {
            try {
                DatabaseTools.insertBook(tempBook);
                main.getBookData().add(tempBook);
            } catch (SQLException e) {
                if (e.getErrorCode() == 23505) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(main.getPrimaryStage());
                    alert.setTitle("Non-unique ISBN!");
                    alert.setHeaderText("Enter a different ISBN");
                    alert.setContentText("A book with that ISBN already exists!");
                    alert.showAndWait();
                } else e.printStackTrace();
            }
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected book.
     */
    @FXML
    private void handleEditBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            boolean okClicked = main.showBookEditDialog(selectedBook, false);
            if (okClicked) {
                try {
                    DatabaseTools.updateBookStatus(selectedBook);
                    showBookDetails(selectedBook);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Book Selected");
            alert.setContentText("Please select a book in the table.");

            alert.showAndWait();
        }
    }

}