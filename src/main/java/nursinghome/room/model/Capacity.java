package nursinghome.room.model;

public enum Capacity {
    SINGLE(1),
    DOUBLE(2),
    TRIPLE(3),
    FOUR_BED(4);

    private int numberOfBeds;

    Capacity(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }
}
