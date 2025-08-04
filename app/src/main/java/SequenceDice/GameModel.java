package SequenceDice;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private List<GameObserver> observers = new ArrayList<>();
    private int numOfPlayers;
    private Player currentPlayer;
    private int currentPlayerIndex = -1;
    private Player[] players;
    private Team[] teams;
    private String[] colours = {"Red", "Green", "Blue"};
    private Dice dice = new Dice();

    private GameEvent currentEvent;
    Board board = new Board();

    boolean playerHasRolled = false;

    public GameModel(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        initializePlayers();
    }

    private boolean initializePlayers(){
        //if individual game
        if(numOfPlayers == 2 || numOfPlayers == 3){
            players = new Player[numOfPlayers];
            for (int i = 0; i < numOfPlayers; i++) {
                Player player = new Player(i, colours[i]);
                players[i] = player;
            }
            return true;
        }
        //if teams game
        else if(numOfPlayers == 4){
            Player player1 = new Player(1, colours[0]);
            Player player2 = new Player(2, colours[1]);
            Player player3 = new Player(3, colours[0]);
            Player player4 = new Player(4, colours[1]);

            players = new Player[]{player1, player2, player3, player4};

            teams = new Team[2];
            teams[0] = new Team(colours[0], new Player[]{player1, player3});
            teams[1] = new Team(colours[1], new Player[]{player2, player4});
            return true;
        }
        return false;
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }
    public void startGame(){
        nextTurn();
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % numOfPlayers;
        currentPlayer = players[currentPlayerIndex];
        playerHasRolled = false;
        notifyObservers(new GameEvent(GameEventType.NEXT_PLAYER_TURN, "Player " + (currentPlayerIndex + 1) + "'s turn", currentPlayer));

    }

    public void rollDice() {
        if(playerHasRolled)
            return;
        int[] outcome = dice.roll();
        int total = outcome[0] + outcome[1];
        GameEvent gameEvent;

        if(total == 10){
            //defensive roll
            //remove opponents tokens anywhere except 2 or 12
            List<int[]> opponentsCells = board.findOpponentsCellsNotOnGrey(currentPlayer);
            gameEvent = new GameEvent(GameEventType.DEFENSIVE_DICE_ROLL, "You rolled " + total + ", defensive roll", total);
        }
        else if(total == 11){
            //wild roll
            List<int[]> emptyCells = board.findAllEmptyCells();
            //place token on any empty space
            gameEvent = new GameEvent(GameEventType.WILD_DICE_ROLL, "You rolled " + total+ ", wild roll", total);
        }
        else {
            //check for empty cells of same number as roll
            List<int[]> validCells = board.findValidCells(total);

            //prompt user to pick one of the cells
            if(total == 2 || total == 12)
            {
                gameEvent = new GameEvent(GameEventType.EXTRA_TURN_DICE_ROLL, "You rolled " + total + ", roll again", total);
            }
            else{
                gameEvent = new GameEvent(GameEventType.DICE_ROll, "You rolled " + total + ", pick a cell", total);
            }

        }
        playerHasRolled = true;
        notifyObservers(gameEvent);
    }

    public boolean processChoice(int[] coords) {
        switch (currentEvent.getType()) {
            case DICE_ROll:
                if(board.currentPlayerIsAbleToPlay(currentPlayer, (int)currentEvent.getCargo())) {
                    if(board.isValidCellCoordinates(coords, (int)currentEvent.getCargo(),currentPlayer)) {
                        placeToken(coords);
                        nextTurn();
                        return true;
                    }
                    else return false;
                }
                else {
                    nextTurn();
                }
                break;

            case WILD_DICE_ROLL:
                if(board.isEmptyCellCoordinates(coords)){
                    placeToken(coords);
                    nextTurn();
                    return true;
                }
                break;
            case DEFENSIVE_DICE_ROLL:
                if(board.isOppenentsCellsNotOnGrey(coords, currentPlayer)) {
                    removeToken(coords);
                    nextTurn();
                    return true;
                }
                break;
            case EXTRA_TURN_DICE_ROLL:
                if(board.isValidCellCoordinates(coords, (int)currentEvent.getCargo(),currentPlayer)) {
                    placeToken(coords);
                    return true;
                }
                break;
        }
        return false;
    }

    public void placeToken(int[] coords) {
        board.placeToken(coords[0], coords[1], currentPlayer);
        if(board.checkWinCondition(coords[0], coords[1])){
            notifyObservers(new GameEvent(GameEventType.GAME_OVER, "Player " + currentPlayer.getNumber() + " won the game!", currentPlayer));
        }
    }
    public void removeToken(int[] coords) {
        board.removeToken(coords[0], coords[1]);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public Player getPreviousPlayer(){
        return players[(currentPlayerIndex - 1 + numOfPlayers) % numOfPlayers];
    }

    private void notifyObservers(GameEvent gameEvent) {

        currentEvent = gameEvent;
        for (GameObserver observer : observers) {
            observer.onGameEvent(gameEvent);
        }
    }

    public Board getBoard() {
        return board;
    }
}
