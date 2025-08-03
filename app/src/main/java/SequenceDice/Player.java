package SequenceDice;

public class Player {
    private int number;
    private String colour;

    public Player(int number, String colour){
        this.number = number;
        this.colour = colour;
    }

    public int getNumber() {return number;}
    public String getColour() {return colour;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Player other = (Player) obj;
        return this.number == other.number;
    }
    public boolean isOnSameTeam(Player otherPlayer){
        return this.colour.equals(otherPlayer.getColour());
    }
}
