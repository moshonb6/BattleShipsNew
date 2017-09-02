package Project.modules;

public class Board {
    private static int boardSize;
    private static final String ROW = "row";
    private static final String COLUMN = "column";

    Board(int i_boardSize){
        boardSize = i_boardSize;
    }

    public void printMyBoard(Player player){
        int[][] myBoardMat = player.getMyBoardMat();
        int rowNumber = 1;
        char columnNumber = 'A';
        int mat_x = 0;
        int mat_y = 0;

        for(int i = 0; i < boardSize ;i++) {
            for (int j = 0; j < boardSize ; j++) {
                System.out.print( " " + myBoardMat[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.print("------------moshe---------------------\n");

        System.out.print(player.getName() + " Score: " + player.getScore() + " Time: \n");
        System.out.print("--------My Board--------\n");

        for(int i = 0; i < boardSize + 1;i++){

            for(int j = 0;j<boardSize + 1;j++){
                if(i==0 &&  j==0){
                    System.out.print("___|");
                }
                else if(i == 0){
                    System.out.print("_" + columnNumber++ + "_|");
                }
                else if(j == 0){
                    System.out.print("_" + rowNumber++ + "_|");
                }
                else if(myBoardMat[mat_x][mat_y] > 0 ){
                    System.out.print("_V_|");
                    mat_y++;

                }
                else if(myBoardMat[mat_x][mat_y] == -1 ){
                    System.out.print("_X_|");
                    mat_y++;

                }
                else if(myBoardMat[mat_x][mat_y] == -2 ){
                    System.out.print("_M_|");
                    mat_y++;

                }
                else {
                    System.out.print("___|");
                    mat_y++;

                }
                if(j==boardSize){
                    System.out.print("|");
                }

            }
            if(i != 0) {
                mat_x++;
                mat_y = 0;
            }
            System.out.println("\n");

        }
        System.out.println("\n");
    }

    public void printOponentBoard(int[][] oponentBoardMat){
        int rowNumber = 1;
        char columnNumber = 'A';
        int mat_x = 0;
        int mat_y = 0;

        System.out.print("------Oponent Board------\n");

        for(int i = 0; i < boardSize + 1;i++) {

            for (int j = 0; j < boardSize + 1; j++) {
                if (i == 0 && j == 0) {
                    System.out.print("___|");
                } else if (i == 0) {
                    System.out.print("_" + columnNumber++ + "_|");
                } else if (j == 0) {
                    System.out.print("_" + rowNumber++ + "_|");
                } else if (oponentBoardMat[mat_x][mat_y] == 1) {
                    System.out.print("_G_|");
                    mat_y++;
                } else if (oponentBoardMat[mat_x][mat_y] == 2) {
                    System.out.print("_B_|");
                    mat_y++;

                } else {
                    System.out.print("___|");
                    mat_y++;
                }
                if (j == boardSize) {
                    System.out.print("|");
                }

            }
            if (i != 0) {
                mat_x++;
                mat_y = 0;
            }
            System.out.println("\n");
        }
    }
}
