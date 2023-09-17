package model;

public class Customer {

    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;
    private Loyalty loyalty;
    private String shopCode;
private String updatedAt;
private String createdAt;

    public Customer(int id, String firstName, String lastName, String phoneNumber, String email, String dateOfBirth, Loyalty loyalty, String shopCode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.loyalty = loyalty;
        this.shopCode = shopCode;
    }

    public Customer() {
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public Loyalty getLoyalty() {
        return loyalty;
    }

    public String getShopCode() {
        return shopCode;
    }

    public String get(String argument) {
        return switch (argument) {
            case "firstName" -> getFirstName();
            case "lastName" -> getLastName();
            case "phoneNumber" -> getPhoneNumber();
            default -> "";
        };
    }
}