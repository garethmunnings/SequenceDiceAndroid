package SequenceDice;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static Cell[][] board;
    private int[][] numbers = {
            {2, 3, 4, 5, 6, 2},
            {6, 7, 8, 9, 7, 3},
            {5, 6, 12, 12, 8, 4},
            {4, 8, 12, 12, 9, 5},
            {3, 7, 9, 8, 7, 6},
            {2, 6, 5, 4, 3, 2}
    };
    public Board() {
        board = new Cell[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                board[i][j] = new Cell(numbers[i][j]);
            }
        }
    }

    /**
     * Finds all valid cells on the board that match a given number and are currently unoccupied.
     *
     * @param number the number to search for on the board
     * @return a list of coordinate pairs (row, column) representing valid, unoccupied cells with the given number
     */
    public List<int[]> findValidCells(int number) {
        List<int[]> validCells = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j].getNumber() == number && board[i][j].isEmpty()){
                    validCells.add(new int[]{i, j});
                }
            }
        }
        return validCells;
    }

    public boolean validCellExists(int number)  {
        List<int[]> validCells = findValidCells(number);
        if(validCells.size() > 0) return true;
        return false;
    }

    /**
     * Checks if the given coordinates (row, column) are valid for placing a token on the board.
     *
     * @param number
     * @return
     */
    public boolean isValidCellCoordinates(int[] coords, int number, Player currentPlayer) {
        int row = coords[0];
        int col = coords[1];
        if(validCellExists(number)) {
            return board[row][col].getNumber() == number && board[row][col].isEmpty();
        }
        else if(!currentPlayerHasAllCellsOfNumber(number, currentPlayer)){
            return board[row][col].getNumber() == number && !board[row][col].getOccupant().isOnSameTeam(currentPlayer);
        }
        else{
            return false;
        }
    }

    public boolean currentPlayerIsAbleToPlay(Player currentPlayer, int number){
        if(number == 10){
            //if opponent has token on board
            return opponentHasTokenOnBoard(currentPlayer);

        }
        if(number == 11){
            //if empty cell exists
            return !findAllEmptyCells().isEmpty();
        }
        if(validCellExists(number)) return true;
        if(!currentPlayerHasAllCellsOfNumber(number, currentPlayer)) return true;
        return false;
    }

    public boolean opponentHasTokenOnBoard(Player currentPlayer){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j].getOccupant() != null && !board[i][j].getOccupant().isOnSameTeam(currentPlayer))
                    return true;
            }
        }

        return false;
    }
    public boolean currentPlayerHasAllCellsOfNumber(int number, Player currentPlayer){
        if(validCellExists(number))
        {
            return false;
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(!board[i][j].isEmpty() && !board[i][j].getOccupant().isOnSameTeam(currentPlayer) && board[i][j].getNumber() == number){
                    return false;
                }
            }
        }
        return true;
    }
    public List<int[]> findAllEmptyCells(){
        List<int[]> validCells = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j].isEmpty()){
                    validCells.add(new int[]{i, j});
                }
            }
        }
        return validCells;
    }

    public boolean isEmptyCellCoordinates(int[] coords) {
        int row = coords[0];
        int col = coords[1];
        return board[row][col].isEmpty();
    }

    public boolean isOppenentsCellsNotOnGrey(int[] coords, Player currentPlayer){
        int row = coords[0];
        int col = coords[1];
        return board[row][col].getOccupant() != null && !board[row][col].getOccupant().isOnSameTeam(currentPlayer) && board[row][col].getNumber() != 2 && board[row][col].getNumber() != 12;
    }

    public List<int[]> findOpponentsCellsNotOnGrey(Player currentPlayer){
        List<int[]> opponentsCells = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                //add team functionality
                if(board[i][j].getOccupant() != null && !board[i][j].getOccupant().equals(currentPlayer) && board[i][j].getNumber() != 2 && board[i][j].getNumber() != 12){
                    opponentsCells.add(new int[]{i, j});
                }
            }
        }
        return opponentsCells;
    }

    public void placeToken(int row, int col, Player currentPlayer) {
        board[row][col].setOccupant(currentPlayer);
    }
    public void removeToken(int row, int col) {
        board[row][col].setOccupant(null);
    }

    public boolean checkWinCondition(int row, int col, int numOfTokensInARow) {
        Cell cell = board[row][col];
        Player player = cell.getOccupant();

        return(checkRow(row, player, numOfTokensInARow) || checkCol(col, player, numOfTokensInARow) || checkDiagonal(row, col, player, numOfTokensInARow));
    }

    public boolean checkRow(int row, Player player, int numOfTokensInARow){
        int streak = 0;

        for (int col = 0; col < board[row].length; col++) {
            Player occupant = board[row][col].getOccupant();

            if (occupant != null && occupant.isOnSameTeam(player)) {
                streak++;
                if (streak == numOfTokensInARow) {
                    return true;
                }
            } else {
                streak = 0;
            }
        }

        return false;
    }

    public boolean checkCol(int col, Player player,int numOfTokensInARow){
        int streak = 0;

        for (int row = 0; row < board[col].length; row++) {
            Player occupant = board[row][col].getOccupant();

            if (occupant != null && occupant.isOnSameTeam(player)) {
                streak++;
                if (streak == numOfTokensInARow) {
                    return true;
                }
            } else {
                streak = 0;
            }
        }

        return false;
    }

    public boolean checkDiagonal(int row, int col, Player player, int numOfTokensInARow) {
        // Down-right (↘)
        if (countDirection(row, col, 1, 1, player) >= numOfTokensInARow) {
            return true;
        }

        // Down-left (↙)
        if (countDirection(row, col, 1, -1, player) >= numOfTokensInARow) {
            return true;
        }

        return false;
    }

    private int countDirection(int row, int col, int dRow, int dCol, Player player) {
        int streak = 0;
        int rows = board.length;
        int cols = board[0].length;

        // Go backward in direction first
        int r = row;
        int c = col;
        while (r >= 0 && r < rows && c >= 0 && c < cols &&
                board[r][c].getOccupant() != null &&
                board[r][c].getOccupant().isOnSameTeam(player)) {
            streak++;
            r -= dRow;
            c -= dCol;
        }

        // Go forward in direction
        r = row + dRow;
        c = col + dCol;
        while (r >= 0 && r < rows && c >= 0 && c < cols &&
                board[r][c].getOccupant() != null &&
                board[r][c].getOccupant().isOnSameTeam(player)) {
            streak++;
            r += dRow;
            c += dCol;
        }

        return streak;
    }
    public Cell[][] getBoard() {
        return board;
    }

    public int[][] getNumbers(){return numbers;}
}
