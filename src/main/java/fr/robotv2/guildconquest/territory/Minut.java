package fr.robotv2.guildconquest.territory;

public enum Minut {
    THIRTY(30), ZERO(0);

    private final int number;
    Minut(int i) {
        this.number = i;
    }

    public int getNumber() {
        return number;
    }
}
