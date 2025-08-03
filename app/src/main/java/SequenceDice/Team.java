package SequenceDice;

public class Team {
    String colour;
    Player[] players;

    public Team(String colour, Player[] ps) {
        players = new Player[2];
        this.players[0] = ps[0];
        this.players[1] = ps[1];
    }
}
