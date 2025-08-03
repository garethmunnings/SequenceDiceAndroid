package SequenceDice;

public class GameEvent {
    private GameEventType type;
    private String message;
    private Object cargo;

    public GameEvent(GameEventType type, String message, Object cargo) {
        this.type = type;
        this.message = message;
        this.cargo = cargo;
    }

    public GameEventType getType() { return type; }
    public String getMessage() { return message; }
    public Object getCargo() { return cargo; }
}
