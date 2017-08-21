package Project.modules;

import Project.UI.UserIteration;
import Project.UI.XmlLoader;
import Resources.BattleShipGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

public class GameManager {
    private static final int MAX_PLAYERS = 2;
    private static final int BOARD_MAX_SIZE = 20;
    private static final int BOARD_MIN_SIZE = 5;
    private static Player currentPlayer;
    private static Player previousPlayer;
    private static int boardSize;
    private static boolean endGame = false;
    private static int numOfTurns = 0;
    private static long timeStart;
    private static double totalTime;
    private static int battleShipsAmount;

    public static void setBoardSize(int i_boardSize){ boardSize = i_boardSize;}

    public void playGame(){
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
        XmlLoader xml = new XmlLoader();
        battleShipsAmount = BattleShipConfig.getShipAmountTypeA() + BattleShipConfig.getShipAmountTypeB();
        BattelShip[] battleShipsPlayer1 = createBattleShipsFromShipsArray(xml.getBattleShipsPlayer1());
        BattelShip[] battleShipsPlayer2 = createBattleShipsFromShipsArray(xml.getBattleShipsPlayer2());

        currentPlayer = new Player("player1", boardSize, battleShipsAmount, battleShipsPlayer1);
        previousPlayer = new Player("player2", boardSize, battleShipsAmount, battleShipsPlayer2);
    }

    private BattelShip[] createBattleShipsFromShipsArray(ArrayList<BattleShipGame.Boards.Board.Ship> i_shipsArray) {
        BattelShip[] res = new BattelShip[battleShipsAmount];
        int index = 0;
        BattelShip battelShip = new BattelShip();

        for (BattleShipGame.Boards.Board.Ship ship : i_shipsArray) {
            Point point = new Point(ship.getPosition().getX(), ship.getPosition().getY());
            if (ship.getShipTypeId().equals("A")) {
                battelShip = new BattelShip(ship.getShipTypeId(), point, ship.getDirection(),
                        BattleShipConfig.getShipLengthTypeA(), BattleShipConfig.getShipScoreTypeA());
            }
            else if (ship.getShipTypeId().equals("B")) {
                battelShip = new BattelShip(ship.getShipTypeId(), point, ship.getDirection(),
                        BattleShipConfig.getShipLengthTypeB(), BattleShipConfig.getShipScoreTypeB());
            }
            else if (ship.getShipTypeId().equals("L")) {
                battelShip = new BattelShip(ship.getShipTypeId(), point, ship.getDirection(),
                        BattleShipConfig.getShipLengthTypeL(), BattleShipConfig.getShipScoreTypeL());
            }

            res[index++] = battelShip;
        }

        return res;

    }
}
