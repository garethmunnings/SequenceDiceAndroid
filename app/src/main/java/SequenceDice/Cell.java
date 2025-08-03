package SequenceDice;

public class Cell {
    private int number;
    private Player occupant;

    public Cell(int number) {
        this.number = number;
        occupant = null;
    }

    public int getNumber() {return number;}
    public Player getOccupant() {return occupant;}
    public boolean isEmpty() {return occupant == null;}

    public void setOccupant(Player occupant) {this.occupant = occupant;}
}
