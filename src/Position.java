public class Position {
    private int face;

    private int line;

    private int column;

    public Position(int face, int line, int column) {
        this.face = face;
        this.line = line;
        this.column = column;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
