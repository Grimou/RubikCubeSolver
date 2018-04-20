public class Position {
    private int face;

    private int x;

    private int y;

    public Position(int face, int x, int y) {
        this.face = face;
        this.x = x;
        this.y = y;
    }

    public int getFace() {
        return face;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position position = (Position) obj;
            return this.face == position.face && this.x == position.y && this.y == position.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31*(31*face + x) + y;
    }
}
