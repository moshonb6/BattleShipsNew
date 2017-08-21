package Project.modules;

import Project.UI.UserIteration;
import java.awt.*;
import java.util.Scanner;

import static java.lang.System.exit;

public class GameManager {
    private static final int MAX_PLAYERS = 2;
    private static final int BOARD_MAX_SIZE = 20;
    private static final int BOARD_MIN_SIZE = 5;
    private static Player currentPlayer;
    private static Player previousPlayer;
    private static int boardSize = 6;
    private static boolean endGame = false;
    private static int numOfTurns = 0;
    private static long timeStart;
    private static double totalTime;
    private static int battleShipsAmount;

    public void playGame(){
        //TODO: if no game is loaded, write a massage to user
        initPlayers();
        timeStart = System.currentTimeMillis();
        startGame();
    }

    public void startGame(){
        Point hit;
        boolean goodHit;
        int userChoice;

        while(!endGame) {
            userChoice = UserIteration.gameManuMsg();

            switch (userChoice){
                case 3:
                    currentPlayer.getMyBoard().printMyBoard(currentPlayer);
                    currentPlayer.getOponentBoard().printOponentBoard(currentPlayer.getOponentBoardMat());
                    startGame();
                    break;
                case 4:
                    hit = UserIteration.getPointFromPlayer(currentPlayer, boardSize);
                    goodHit = checkHit(currentPlayer, hit);
                    numOfTurns++;

                    if(!goodHit){
                        switchPlayers();
                    }
                    break;
                case 5:
                    showStatistics(currentPlayer);
                    break;
                case 6:
                    UserIteration.printResultsAndStatistics(currentPlayer, previousPlayer, numOfTurns, calculateTotalTime(timeStart));
                    exit(1);
                    break;
                case 7:
                    putMine();
            }
        }

    }

    private void putMine() {
        if(currentPlayer.getMinesLeft() < 1){
            UserIteration.noMoreMinesMsg();
        }
        else {
            currentPlayer.putMineOnBoard();
            switchPlayers();
        }

    }

    private void showStatistics(Player player) {
        totalTime = calculateTotalTime(timeStart);
        UserIteration.showStatisticsMsg(numOfTurns, totalTime, player.getScore(), player.getMissed(), player.getAvgTimeForMove());
    }

    public static double calculateTotalTime(long timeStart) {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - timeStart;
        double total = tDelta / 1000.0;

        return total;
    }

    private void switchPlayers(){
        Player temp;
        temp = currentPlayer;
        currentPlayer = previousPlayer;
        previousPlayer = temp;
    }

    private boolean checkHit(Player playerTurn, Point hit){
        int[][] attackedMat;
        boolean goodHit;
        int battelshipAmmount = 0;
        attackedMat = previousPlayer.getMyBoardMat();
        int[][] oponentBoardMat = playerTurn.getOponentBoardMat();

        if(oponentBoardMat[hit.x][hit.y] != 0){
            goodHit = true;
            UserIteration.alreadyAttackedMsg();
        }
        else if(attackedMat[hit.x][hit.y] > 0){
            goodHit = true;
            battelshipAmmount = attackedMat[hit.x][hit.y];
            UserIteration.goodShotMsg();
            attackedMat[hit.x][hit.y] = -1;
            playerTurn.updateHit(goodHit, hit, battelshipAmmount);

            if(playerWin(attackedMat)){
                UserIteration.printWinMsg(currentPlayer, previousPlayer);
                endGame = true;
            }
        }
        else if(attackedMat[hit.x][hit.y] == -2){
            goodHit = false;
            UserIteration.mineMsg(playerTurn.getName());
            attackedMat[hit.x][hit.y] = 0; //delete the mine from board
            playerTurn.selfAttack(hit);
            playerTurn.signAttackOnTrackingBoard(hit.x,hit.y);
        }
        else{
            goodHit =  false;
            UserIteration.badShotMsg();
            playerTurn.updateHit(goodHit, hit, battelshipAmmount);
        }

        return goodHit;
    }

    private boolean playerWin(int[][] attackedMat) {
        for(int i= 0; i<boardSize;i++){
            for(int j =0; j<boardSize;j++){
                if(attackedMat[i][j] >= 1)
                    return false;
            }
        }
        return true;
    }

    public void initPlayers(){
        battleShipsAmount = BattleShipConfig.getShipAmountTypeA() + BattleShipConfig.getShipLengthTypeB();
        currentPlayer = new Player("player1", boardSize, 3);////////////
        previousPlayer = new Player("player2", boardSize, 3);////////////
    }
}
