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

    public Position getEndPosition() {
        return endPosition;
    }

}
