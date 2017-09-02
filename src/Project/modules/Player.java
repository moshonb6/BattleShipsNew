package Project.modules;

import Project.UI.UserIteration;

import java.awt.*;

public class Player {
    private String name;
    private int score = 0;
    private int shots = 0;
    private int hits = 0;
    private int missed = 0;
    private int[][] myBoardMat;
    private int[][] oponentBoardMat;
    private Board myBoard;
    private Board oponentBoard;
    private int boardSize;
    private static final String ROW = "ROW";
    private static final String COLUMN = "COLUMN";
    private static final String RIGHT_DOWN = "RIGHT_DOWN";
    private static final String DOWN_RIGHT = "DOWN_RIGHT";
    private BattelShip[] battelShipsArray;
    private int numOfBattelships;
    private long avgTimeForMove;
    private static int minesLeft;
    private static int minesAmount;

    public Player(String i_name,int i_boardSize, int i_numOfBattelships, BattelShip[] i_battelShips, int i_mines){
        name = i_name;
        boardSize = i_boardSize;
        myBoardMat = new int[boardSize][boardSize];
        oponentBoardMat = new int[boardSize][boardSize];
        numOfBattelships = i_numOfBattelships;
        minesLeft = i_mines;
        battelShipsArray = new BattelShip[numOfBattelships];
        System.arraycopy(i_battelShips, 0, battelShipsArray, 0 ,i_numOfBattelships);
        myBoard = createMyBoard();
        oponentBoard = createOpBoard();
        createBattleShips();
    }

    private void createBattleShips() {
        for(BattelShip battelShip : battelShipsArray){
            createBattleShip(battelShip);
        }
    }

    public int getScore(){
        return score;
    }
    public int getShots(){
        return shots;
    }
    public int getMissed(){
        return  missed;
    }
    public String getName(){
        return  name;
    }
    public int getMinesLeft(){return minesLeft;}
    public Board getMyBoard() {
        return myBoard;
    }

    public Board getOponentBoard() {
        return oponentBoard;
    }
    public int[][] getMyBoardMat(){
        return myBoardMat;
    }
    public int[][] getOponentBoardMat(){
        return oponentBoardMat;
    }
    public long getAvgTimeForMove(){return avgTimeForMove;}
    public void setAvgTimeForMove(double timeForMove){avgTimeForMove += timeForMove;}

    public Board createMyBoard(){
        return  new Board(boardSize);
    }

    public  Board createOpBoard(){
        return new Board(boardSize);
    }

    private  void  createBattleShip(BattelShip battelShip){
        int x = battelShip.getPosition().x;
        int y = battelShip.getPosition().y;

        if(battelShip.getDirection().equals(COLUMN)) {
            for (int i = 0; i < battelShip.getLength(); i++) {
                myBoardMat[x][y] = battelShip.getShipValue();
                x++;
            }
        }
        else if (battelShip.getDirection().equals(ROW)){
            for (int i = 0; i < battelShip.getLength(); i++) {
                myBoardMat[x][y] = battelShip.getShipValue();
                y++;
            }
        }else if (battelShip.getDirection().equals(RIGHT_DOWN)){
            for (int i = 0; i < battelShip.getLength(); i++) {
                myBoardMat[x][y] = battelShip.getShipValue();
                y++;
            }
            y--;
            for (int i = 0; i < battelShip.getLength(); i++) {
                myBoardMat[x][y] = battelShip.getShipValue();
                x++;
            }
        }else if (battelShip.getDirection().equals(DOWN_RIGHT)){
            for (int i = 0; i < battelShip.getLength(); i++) {
                myBoardMat[x][y] = battelShip.getShipValue();
                x++;
            }
            x--;
            for (int i = 0; i < battelShip.getLength(); i++) {
                myBoardMat[x][y] = battelShip.getShipValue();
                y++;
            }
        }
    }

    public void updateHit(boolean i_goodHit, Point i_hit, int battleShipValue){
        shots++;

        if(i_goodHit){
            addHitOnTrackingBoard(i_hit.x,i_hit.y);
            score ++;
        }
        else{
            signAttackOnTrackingBoard(i_hit.x,i_hit.y);
            missed++;
        }
    }

    public void addHitOnTrackingBoard(int x, int y){
        oponentBoardMat[x][y] = 1;
    }

    public void signAttackOnTrackingBoard(int x, int y){
        oponentBoardMat[x][y] = 2;
    }

    public void putMineOnBoard() {
        Point mineLocation = new Point(0,0);
        Boolean validMine = false;

        while (!validMine) {
            mineLocation = UserIteration.getPointFromPlayer(this, boardSize);
            validMine = checkMineLocation(mineLocation.x, mineLocation.y);
            if(!validMine)
                UserIteration.mineLocatedNotValidMsg();
        }

        myBoardMat[mineLocation.x][mineLocation.y] = -2;
        minesLeft--;
    }

    private boolean checkMineLocation(int x, int y) {
        if(x > 0)
            if(myBoardMat[x-1][y] != 0)
                return false;

        if(x < boardSize - 1)
            if(myBoardMat[x+1][y] != 0)
                return false;
        if(y > 0)
            if(myBoardMat[x][y-1] != 0)
                return false;

        if(y < boardSize - 1)
            if(myBoardMat[x][y+1] != 0)
                return false;

        if(myBoardMat[x][y] != 0){
            return false;
        }

        return true;
    }

    public void selfAttack(Point mine) {
        if(myBoardMat[mine.x][mine.y] > 0){
            myBoardMat[mine.x][mine.y] = -1;
        }
    }
}
