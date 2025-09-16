package SequenceDice;

public class Team {
    String colour;
    int number;
    Player[] players;

    private int longestSequence;

    public Team(String colour, int number, Player[] ps) {
        this.colour = colour;
        players = new Player[2];
        this.number = number;
        this.players[0] = ps[0];
        this.players[1] = ps[1];
        longestSequence = 0;
    }

    public Player[] getPlayers(){
        return players;
    }
    public int getNumber(){
        return number;
    }

    public int getLongestSequence(){
        return longestSequence;
    }
    public void setLongestSequence(int longestSequence){
        this.longestSequence = longestSequence;
    }
}
