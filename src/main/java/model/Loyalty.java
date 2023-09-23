package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Loyalty {

    private String bonusCardNumber;
    private String status;
    private int discountRate;

    public String getBonusCardNumber() {
        return bonusCardNumber;
    }

    public String getStatus() {
        return status;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}