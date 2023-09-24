package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

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
    private Date updatedAt;
    private Date createdAt;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(phoneNumber, customer.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phoneNumber);
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