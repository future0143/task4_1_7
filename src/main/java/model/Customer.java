package model;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    public String get(String argument) {
        return switch (argument) {
            case "firstName" -> getFirstName();
            case "lastName" -> getLastName();
            case "phoneNumber" -> getPhoneNumber();
            default -> "";
        };
    }
}