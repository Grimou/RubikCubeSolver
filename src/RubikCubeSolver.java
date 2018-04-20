import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class RubikCubeSolver {

    public static final int SIDELEN = 2;
    public static final int NBMOVES = 9 * SIDELEN;
    public enum Color {BLANC,BLEU,VERT,JAUNE,ROUGE,ORANGE};

    public static void main(String[] args) {
        Model model = new Model("RubikCubeSolver");
        int varDepth = 0;
        boolean solved = false;
        ArrayList<IntVar[][][]> rubikCubets = new ArrayList<>();

        while (!solved){
            //Définitions des mouvements solutions
            IntVar[] movet = model.intVarArray("Move", varDepth, 0, NBMOVES - 1);

            //Définition des cubes rubik
            rubikCubets.ensureCapacity(varDepth + 1);
            for (int i = 0; i <= varDepth; i++) {
                IntVar[][][] currentCubet = new IntVar[6][SIDELEN][SIDELEN];
                for (int j = 0; j < 6; j++) {
                    currentCubet[j] = model.intVarMatrix("Cube :" + i + " Face :" + j, SIDELEN, SIDELEN, 0, 5 );
                }
                rubikCubets.add(i,currentCubet);
            }
            IntVar[][][] rubikCubeT = rubikCubets.get(varDepth);
            for (int j = 0; j < 6; j++) {
                model.allEqual(flattenIntVarMatrix(rubikCubeT[j])).post();
            }
            model.allDifferent(rubikCubeT[0][0][0], rubikCubeT[1][0][0], rubikCubeT[2][0][0], rubikCubeT[3][0][0], rubikCubeT[4][0][0], rubikCubeT[5][0][0]);

            IntVar[][] sames = model.intVarMatrix("Sames", varDepth, NBMOVES, 0, 1, true);

            for (int t = 0; t < varDepth; t++) {
                for (int i = 0; i < NBMOVES; i++) {
                    model.ifOnlyIf(model.arithm(sames[t][i], "=", 1),model.arithm(movet[i], "=", i));
                }
            }

            ArrayList<IntVar[][][]> unmoveds = new ArrayList<>();
            unmoveds.ensureCapacity(varDepth + 1);
            for (int i = 0; i <= varDepth; i++) {
                IntVar[][][] currentUnmoved = new IntVar[6][SIDELEN][SIDELEN];
                for (int j = 0; j < 6; j++) {
                    currentUnmoved[j] = model.intVarMatrix("Unmoved :" + i + " Face :" + j, SIDELEN, SIDELEN, 0, 1, true );
                }
                unmoveds.add(i,currentUnmoved);
            }
        }



        Solver solver = model.getSolver();
        solver.findSolution();
        solver.printStatistics();
    }

    public static IntVar[] flattenIntVarMatrix(IntVar[][] matrix){
        IntVar[] result = new IntVar[matrix.length * matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[j + (i * matrix.length)] = matrix[i][j];
            }
        }
        return result;
    }

}
