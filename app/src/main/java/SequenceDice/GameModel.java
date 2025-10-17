package SequenceDice;

import android.util.Log;

public class GameModel {
    private GameObserver observer;
    private final int numOfPlayers;
    private String[] playerNames;
    private Player currentPlayer;
    private Player[] players;
    private Team[] teams;
    private String[] colours = {"Green", "Blue", "Red"};
    private Dice dice = new Dice();

    private GameEvent currentEvent;
    Board board = new Board();

    boolean playerHasRolled = false;
    int numOfTokensInARowForWin;

    private int numberOfRoundsPlayed;
    private int numberOfTokensPlaced;
    private int numberOfTokensRemoved;

    public GameModel(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        this.playerNames = playerNames;
        numberOfRoundsPlayed = 0;
        numberOfTokensPlaced = 0;
        initializePlayers();
        setNumOfTokensInARowForWin();
    }

    private void setNumOfTokensInARowForWin(){
        if(numOfPlayers == 2)
            numOfTokensInARowForWin = 2;
        else
            numOfTokensInARowForWin = 2;
    }

    private boolean initializePlayers(){
        //if individual game
        if(numOfPlayers == 2 || numOfPlayers == 3){
            players = new Player[numOfPlayers];
            for (int i = 0; i < numOfPlayers; i++) {
                Player player = new Player(i + 1, colours[i], "name");
                players[i] = player;
            }
            return true;
        }
        //if teams game
        else if(numOfPlayers == 4){
            Player player1 = new Player(1, colours[0], playerNames[0]);
            Player player2 = new Player(2, colours[1], playerNames[1]);
            Player player3 = new Player(3, colours[0], playerNames[2]);
            Player player4 = new Player(4, colours[1], playerNames[3]);

            players = new Player[]{player1, player2, player3, player4};

            teams = new Team[2];
            teams[0] = new Team(colours[0],1, new Player[]{player1, player3});
            teams[1] = new Team(colours[1],2, new Player[]{player2, player4});
            return true;
        }
        return false;
    }

    public Team getCurrentTeam(){
        if(teams.length == 0)
            return null;
        for(Team team: teams){
            if(team.getPlayers()[0].equals(currentPlayer) || team.getPlayers()[1].equals(currentPlayer))
                return team;
        }
        return null;
    }

    public void setObserver(GameObserver observer) {
        this.observer = observer;
    }


    /**
     * @param playerNumber sent from the server, currentplayer should be updated to this
     */
    public void nextTurn(int playerNumber) {
        currentPlayer = players[playerNumber-1];
        playerHasRolled = false;
    }

    public boolean processChoice(int[] coords) {
        switch (currentEvent.getType()) {
            case DICE_ROll_OUTCOME:
                if(board.currentPlayerIsAbleToPlay(currentPlayer, (int)currentEvent.getCargo())) {
                    if(board.isValidCellCoordinates(coords, (int)currentEvent.getCargo(),currentPlayer)) {

                        placeToken(coords);
                        notifyObservers(new GameEvent(GameEventType.PLACE_TOKEN, String.valueOf(getCurrentPlayer().getNumber()), coords));
                        return true;
                    }
                    else return false;
                }
                break;

            case WILD_DICE_ROLL:
                if(board.isEmptyCellCoordinates(coords)){
                    placeToken(coords);
                    notifyObservers(new GameEvent(GameEventType.PLACE_TOKEN, String.valueOf(getCurrentPlayer().getNumber()), coords));
                    return true;
                }
                break;
            case DEFENSIVE_DICE_ROLL:
                if(board.isOppenentsCellsNotOnGrey(coords, currentPlayer)) {
                    removeToken(coords);
                    notifyObservers(new GameEvent(GameEventType.REMOVE_TOKEN, "Player " + getCurrentPlayer().getNumber(), coords));
                    return true;
                }
                break;
            case EXTRA_TURN_DICE_ROLL:
                if(board.isValidCellCoordinates(coords, (int)currentEvent.getCargo(),currentPlayer)) {
                    placeToken(coords);
                    notifyObservers(new GameEvent(GameEventType.PLACE_TOKEN, String.valueOf(getCurrentPlayer().getNumber()), coords));
                    return true;
                }
                break;
        }
        return false;
    }

    public void placeToken(int[] coords) {
        board.placeToken(coords[0], coords[1], currentPlayer);
        numberOfTokensPlaced++;
    }
    public void removeToken(int[] coords) {
        numberOfTokensRemoved++;
        board.removeToken(coords[0], coords[1]);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public Player getPreviousPlayer(){
        return players[(currentPlayer.getNumber() - 2 + numOfPlayers) % numOfPlayers];
    }

    private void notifyObservers(GameEvent gameEvent) {
        currentEvent = gameEvent;
        observer.onGameEvent(gameEvent);
    }

    public void setCurrentEvent(GameEvent event){this.currentEvent = event;}

    public Board getBoard() {
        return board;
    }
    public int getNumOfPlayers(){
        return numOfPlayers;
    }

    public int getRoundsPlayed(){return numberOfRoundsPlayed;}
    public int getTokensPlaced(){return numberOfTokensPlaced;}
    public int getTokensRemoved(){return numberOfTokensRemoved;}


}
