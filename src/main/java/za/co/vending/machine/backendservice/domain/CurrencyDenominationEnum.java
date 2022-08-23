package za.co.vending.machine.backendservice.domain;

public enum CurrencyDenominationEnum {
    FIVERAND(5),
    TENRAND(10),
    TWENTYRAND(20);
    private int denomination;

    private CurrencyDenominationEnum(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination(){ return denomination; }
}
