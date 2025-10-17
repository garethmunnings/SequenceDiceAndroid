package SequenceDice;

public enum GameEventType {
    //from server to client
    START_GAME,
    NEXT_PLAYER_TURN,
    GAME_OVER,
    DICE_ROll_OUTCOME,
    TOKEN_PLACED,
    TOKEN_REMOVED,

    //from client to server
    ROLL_DICE,
    PLACE_TOKEN,
    REMOVE_TOKEN,

    //from controller to game model
    NO_POSSIBLE_MOVE,
    DEFENSIVE_DICE_ROLL,
    WILD_DICE_ROLL,
    EXTRA_TURN_DICE_ROLL,
}
