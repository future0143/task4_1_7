package model;

public class Loyalty {

    private String bonusCardNumber;
    private String status;
    private int discountRate;

    public Loyalty() {
    }

    public String getBonusCardNumber() {
        return bonusCardNumber;
    }

    public String getStatus() {
        return status;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public Loyalty(String bonusCardNumber, String status, int discountRate) {
        this.bonusCardNumber = bonusCardNumber;
        this.status = status;
        this.discountRate = discountRate;
    }
}
