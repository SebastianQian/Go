import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Score {
    static Set<List<Integer>> possibleTerritory = new HashSet<List<Integer>>();
    static Set<List<Integer>> confirmedTerritory = new HashSet<List<Integer>>();
    static boolean touchesBlack = false;
    static boolean touchesWhite = false;

    //get dead stones manually, return int[] with final 2 values being removeBlackCount and then removeWhiteCount
    public static int[] markDeadStones(int[][] endingBoard) {
        /*int[] deads = new int[endingBoard.length * endingBoard.length];
        int deadsIndex = 0;
        //Scanner deadStones = new Scanner(System.in);
        boolean done = false;
        int removeBlackCount = 0;
        int removeWhiteCount = 0;
        while (!done) { //add an undo possibility (using a stack?)?
            //System.out.println("Type coordinates of dead stones in the form 'y x' without quotes, starting from top left as 0,0 or 'done' w/o quotes");
            //String point = deadStones.nextLine();
            if (point.equals("done")) {
                done = true;
            }
            else { //should check that they've actually chosen a stone rather than an empty intersection?
                String[] coords = point.split(" ");
                deads[deadsIndex] = Integer.parseInt(coords[0]);
                ++deadsIndex;
                deads[deadsIndex] = Integer.parseInt(coords[1]);
                ++deadsIndex;
                if (endingBoard[deads[deadsIndex-2]][deads[deadsIndex-1]] == 1) {
                    ++removeBlackCount;
                }
                else {
                    ++removeWhiteCount; //this assumes the removed stone is actually white!
                }
            }
        }
        int[] finalDeads = new int[removeBlackCount*2 + removeWhiteCount*2 + 2];
        for (int i = 0; i < removeBlackCount*2 + removeWhiteCount*2; ++i) finalDeads[i] = deads[i];
        finalDeads[finalDeads.length-2] = removeBlackCount;
        finalDeads[finalDeads.length-1] = removeWhiteCount;
        return finalDeads;
        */
        return new int[0];
    }
    
    public static int[][] removeDeadStones(int[][] endingBoard, int[] deadCoordinates) { //deadCoordinates includes the 2 counts at the end
        for (int i = 0; i < deadCoordinates.length-3; i += 2) {
            endingBoard[deadCoordinates[i]][deadCoordinates[i+1]] = 0;
        }
        return endingBoard;
    }
    
    public static int[] calculateTerritory(int[][] finalBoard) { //returns int[] where array[0] is black's territory and array[1] is white's
        int[] territorySums = new int[2];
        for (int i = 0; i < finalBoard.length; ++i) {
            for (int j = 0; j < finalBoard.length; ++j) {
                if (finalBoard[i][j] == 0 && !(confirmedTerritory.contains(Arrays.asList(i, j)))) {
                    possibleTerritory.clear();
                    touchesBlack = false;
                    touchesWhite = false;
                    if (isTerritory(finalBoard, i, j)) {
                        confirmedTerritory.addAll(possibleTerritory);
                        //possibleTerritory.forEach(System.out::println);
                        //System.out.println("finished " + i + " " + j);
                        if (touchesBlack) territorySums[0] += possibleTerritory.size();
                        else territorySums[1] += possibleTerritory.size();
                    }
                }
            }
        }
        return territorySums;
    }
    
    //need to reset the static variables before running?
    private static boolean isTerritory(int[][] board, int y, int x) {
        possibleTerritory.add(Arrays.asList(y, x));
        int[] adjs = GameLogic.getAdjacentCoordinates(y, x, board.length);
        for (int i = 0; i < adjs.length-1; i += 2) {
            if (board[adjs[i]][adjs[i+1]] == 1) touchesBlack = true;
            else if (board[adjs[i]][adjs[i+1]] == 2) touchesWhite = true;
        }
        if (touchesBlack && touchesWhite) return false;
        boolean isTerritory = true;
        for (int i = 0; i < adjs.length-1; i += 2) {
            if (board[adjs[i]][adjs[i+1]] == 0 && !(possibleTerritory.contains(Arrays.asList(adjs[i], adjs[i+1])))) {
                isTerritory = isTerritory(board, adjs[i], adjs[i+1]);
            }
        }
        return isTerritory;
    }
    
    public static int[] calculateFinalScores(int[] territory, int[] captures, int deadBlacks, int deadWhites) {
        int[] finalScores = new int[2]; //finalScores[0] is black's score and [1] is white's score
        finalScores[0] += territory[0] - captures[1] - deadBlacks;
        finalScores[1] += territory[1] - captures[0] - deadWhites;
        return finalScores;
    }
}