package SequenceDice;

public class Team {
    String colour;
    int number;
    Player[] players;

    public Team(String colour, int number, Player[] ps) {
        players = new Player[2];
        this.number = number;
        this.players[0] = ps[0];
        this.players[1] = ps[1];
    }

    public Player[] getPlayers(){
        return players;
    }
    public int getNumber(){
        return number;
    }
}
