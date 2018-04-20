public class PossibleMovement {

    public int movement;

    public Position endPosition;

    public PossibleMovement(int movement, Position endPosition) {
        this.movement = movement;
        this.endPosition = endPosition;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }
}
