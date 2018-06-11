package model;

import javafx.beans.property.*;

public class Book {

    private final IntegerProperty isbn;
    private final StringProperty bookName;
    private final IntegerProperty floorNumber;
    private final IntegerProperty shelfNumber;
    private final BooleanProperty availability;

    /**
     * Default constructor.
     */
    public Book() {
        this(null, null, null, null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param isbn
     * @param bookName
     * @param floorNumber
     * @param shelfNumber
     * @param availability
     */
    public Book(Integer isbn, String bookName, Integer floorNumber, Integer shelfNumber, Boolean availability) {
        this.isbn = new SimpleIntegerProperty(isbn);
        this.bookName = new SimpleStringProperty(bookName);
        this.floorNumber = new SimpleIntegerProperty(floorNumber);
        this.shelfNumber = new SimpleIntegerProperty(shelfNumber);
        this.availability = new SimpleBooleanProperty(availability);
    }

    public int getIsbn() {
        return isbn.get();
    }

    public void setIsbn(int isbn) {
        this.isbn.set(isbn);
    }

    public IntegerProperty isbnProperty() {
        return isbn;
    }

    public String getBookName() {
        return bookName.get();
    }

    public void setBookName(String bookName) {
        this.bookName.set(bookName);
    }

    public StringProperty bookNameProperty() {
        return bookName;
    }

    public int getShelfNumber() {
        return shelfNumber.get();
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber.set(shelfNumber);
    }

    public IntegerProperty shelfNumberProperty() {
        return shelfNumber;
    }

    public int getFloorNumber() {
        return floorNumber.get();
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber.set(floorNumber);
    }

    public IntegerProperty floorNumberProperty() {
        return floorNumber;
    }

    public boolean isAvailability() {
        return availability.get();
    }

    public void setAvailability(boolean availability) {
        this.availability.set(availability);
    }

    public BooleanProperty availabilityProperty() {
        return availability;
    }
}
