package view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Book;


public class BookEditDialogController {

    @FXML
    private TextField isbnField;
    @FXML
    private TextField bookNameField;
    @FXML
    private TextField shelfNumberField;
    @FXML
    private TextField floorNumberField;
    @FXML
    private CheckBox availabilityField;

    private Stage dialogStage;
    private Book book;
    private boolean okClicked = false;

    public void changeIsbnFieldEdit(boolean editable) {
        if (editable) {
            this.isbnField.setDisable(false);
        } else {
            this.isbnField.setDisable(true);
        }
    }

    public void changeBookNameFieldEdit(boolean editable) {
        if (editable) {
            this.bookNameField.setDisable(false);
        } else {
            this.bookNameField.setDisable(true);
        }

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the book to be edited in the dialog.
     *
     * @param book
     */
    public void setBook(Book book) {
        this.book = book;

        isbnField.setText(Integer.toString(book.getIsbn()));
        bookNameField.setText(book.getBookName());
        shelfNumberField.setText(Integer.toString(book.getShelfNumber()));
        floorNumberField.setText(Integer.toString(book.getFloorNumber()));
        availabilityField.setSelected(book.isAvailability());

    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return okClicked
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            book.setIsbn(Integer.parseInt(isbnField.getText()));
            book.setBookName(bookNameField.getText());
            book.setShelfNumber(Integer.parseInt(shelfNumberField.getText()));
            book.setFloorNumber(Integer.parseInt(floorNumberField.getText()));
            book.setAvailability(availabilityField.isSelected());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";
        if (isbnField.getText() == null || isbnField.getText().length() == 0) {
            errorMessage += "Invalid ISBN!\n";
        } else {
            // try to parse the ISBN into an int.
            try {
                Integer.parseInt(isbnField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid ISBN (must be an integer)!\n";
            }
        }

        if (bookNameField.getText() == null || bookNameField.getText().length() == 0) {
            errorMessage += "Invalid book name!\n";
        }

        if (shelfNumberField.getText() == null || shelfNumberField.getText().length() == 0) {
            errorMessage += "Invalid Shelf Number!\n";
        } else {
            // try to parse the shelf number into an int.
            try {
                Integer.parseInt(shelfNumberField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid shelf number (must be an integer)!\n";
            }
        }

        if (floorNumberField.getText() == null || floorNumberField.getText().length() == 0) {
            errorMessage += "Invalid Floor Number!\n";
        } else {
            // try to parse the floor number into an int.
            try {
                Integer.parseInt(floorNumberField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid floor number (must be an integer)!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}