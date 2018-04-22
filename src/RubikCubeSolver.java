import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RubikCubeSolver {

    public static final int SIDELEN = 2;
    public static final int NBMOVES = 9 * SIDELEN;
    public static final int[][][] CUBE2 = {{{1,2},{2,1}},{{4,6},{3,2}},{{5,1},{3,5}},{{5,4},{3,3}},{{6,6},{6,2}},{{4,5},{4,1}}};

    public static void main(String[] args) {
        Model model = new Model("RubikCubeSolver");
        int varDepth = 0;
        boolean solved = false;
        Solver solver;
        ArrayList<IntVar[][][]> rubikCubets = new ArrayList<>();
        RubikCubeMovement rcm = new RubikCubeMovement(SIDELEN);
        while (!solved){

            //Définitions des mouvements solutions
            IntVar[] movet = model.intVarArray("Move", varDepth, 0, NBMOVES - 1);

            //Définition des cubes rubik
            rubikCubets.ensureCapacity(varDepth + 1);
            for (int i = 0; i <= varDepth; i++) {
                IntVar[][][] currentCubet = new IntVar[6][SIDELEN][SIDELEN];
                for (int j = 0; j < 6; j++) {
                    currentCubet[j] = model.intVarMatrix("Cube :" + i + " Face :" + j, SIDELEN, SIDELEN, 1, 6 );
                }
                rubikCubets.add(i,currentCubet);
            }

            //Cube initial
            for (int a = 0; a < 6; a++) {
                for (int i = 0; i < SIDELEN; i++) {
                    for (int j = 0; j < SIDELEN; j++) {
                        model.arithm(rubikCubets.get(0)[a][i][j],"=",CUBE2[a][i][j]);
                    }
                }
            }

            //Cube final
            IntVar[][][] rubikCubeT = rubikCubets.get(varDepth);
            for (int j = 0; j < 6; j++) {
                model.allEqual(flattenIntVarMatrix(rubikCubeT[j])).post();
            }
            model.allDifferent(rubikCubeT[0][0][0], rubikCubeT[1][0][0], rubikCubeT[2][0][0], rubikCubeT[3][0][0], rubikCubeT[4][0][0], rubikCubeT[5][0][0]).post();

            //Reste du modele
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

            for (int t = 1; t < varDepth; t++) {
                for (int a = 0; a < 6; a++) {
                    for (int i = 0; i < SIDELEN; i++) {
                        for (int j = 0; j < SIDELEN; j++) {
                            int finalT = t;
                            List<IntVar> sameVar = rcm.possibleMovement(a, i, j).stream().map(pm -> sames[finalT][pm.movement]).collect(Collectors.toList());
                            sameVar.add(unmoveds.get(t)[a][i][j]);
                            model.sum((IntVar[]) sameVar.toArray(),"+",1).post();
                        }
                    }
                }
            }
            for (int t = 0; t < varDepth; t++) {
                for (int a = 0; a < 6; a++) {
                    for (int i = 0; i < SIDELEN; i++) {
                        for (int j = 0; j < SIDELEN; j++) {
                            List<PossibleMovement> pms = rcm.possibleMovement(a, i, j);
                            for (PossibleMovement cpm : pms){
                                IntVar ls = model.intVar(0,6);
                                IntVar rs = model.intVar(0,6);
                                model.times(sames[t+1][cpm.movement],rubikCubets.get(t)[a][i][j],ls).post();
                                model.times(sames[t+1][cpm.movement],rubikCubets.get(t+1)[cpm.getEndPosition().getFace()][cpm.getEndPosition().getX()][cpm.getEndPosition().getY()],rs).post();
                                model.arithm(ls,"=",rs).post();
                            }
                            IntVar ls = model.intVar(0,6);
                            IntVar rs = model.intVar(0,6);
                            model.times(unmoveds.get(t+1)[a][i][j],rubikCubets.get(t)[a][i][j],ls).post();
                            model.times(unmoveds.get(t+1)[a][i][j],rubikCubets.get(t+1)[a][i][j],rs).post();
                            model.arithm(ls,"=",rs).post();
                        }
                    }
                }
            }
            solver = model.getSolver();
            Solution sol = solver.findSolution();
            solver.printStatistics();
            if (sol == null) {
                varDepth++;
            }
            else{
                solved = true;
            }
        }



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
