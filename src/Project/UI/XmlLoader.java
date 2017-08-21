package Project.UI;

import Project.modules.BattelShip;
import Project.modules.BattleShipConfig;
import Project.modules.Board;
import Resources.BattleShipGame;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlLoader {
    private static final String RESOURCES = "mypackage";
    private static final String XSD_NAME = "BattelShipsGame.xsd";
    private static final String XML_NAME = "BattelShipsGame.xml";
    private static final String SLASH = "/";
    private static final String ROW = "ROW";
    private static final String COLUMN = "COLUMN";
    private static final int MAX_BOARD_SIZE = 20;
    private static final int MIN_BOARD_SIZE = 5;
    private static int boardSize = 0;
    private ArrayList<BattleShipGame.Boards.Board.Ship> battleShipsPlayer1 = new ArrayList<>();
    private ArrayList<BattleShipGame.Boards.Board.Ship> battleShipsPlayer2 = new ArrayList<>();

    public XmlLoader() {

    }

    public ArrayList<BattleShipGame.Boards.Board.Ship> getBattleShipsPlayer1(){return battleShipsPlayer1;}
    public ArrayList<BattleShipGame.Boards.Board.Ship> getBattleShipsPlayer2(){return battleShipsPlayer2;}


    public Boolean loadBattelShipsConfig() throws Exception{
        try {
            Boolean checkIfValidXml;
            //String fullPath = UserIteration.getFullPathMsg();
            JAXBContext jc = JAXBContext.newInstance(BattleShipGame.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            File xml = new File("src\\Resources\\BattleShipGame.xml");
            BattleShipGame battleShip = (BattleShipGame) unmarshaller.unmarshal(xml);
            inputValidation(battleShip);

            return true;

        }catch (Exception e){
            if (e.getClass() != JAXBException.class && e.getClass() != UnmarshalException.class){
                throw new Exception("Failed to load schema file", e);
            }
            else{
                throw new Exception("Failed to load xml file", e);
            }
        }

    }

    private Boolean inputValidation(BattleShipGame battleShip) {
        boolean valid = false;
        boolean player1 = true;
        boardSize = battleShip.getBoardSize();


        checkIfBoardSizeLiggal(boardSize);

        BattleShipGame.Boards boards = battleShip.getBoards();

        setBattelShipConfiguration(battleShip);

        for(BattleShipGame.Boards.Board board : boards.getBoard() ){
            if(player1) {
                for (BattleShipGame.Boards.Board.Ship ship : board.getShip()) {
                    battleShipsPlayer1.add(ship);
                }
                player1 = false;
            }
            else{
                for (BattleShipGame.Boards.Board.Ship ship : board.getShip()) {
                    battleShipsPlayer2.add(ship);
                }
            }
        }

        setAndCheckBattleShipsLocation(battleShipsPlayer1);
        setAndCheckBattleShipsLocation(battleShipsPlayer2);


        return true;
    }

    private void setAndCheckBattleShipsLocation(ArrayList<BattleShipGame.Boards.Board.Ship> battleShipsPlayer) {
        int[][] boardMat = new int[boardSize][boardSize];
        String direction;
        int length;
        BattleShipGame.Boards.Board.Ship.Position position;
        int indexShip = 1;
        Boolean valid = true;

        for (BattleShipGame.Boards.Board.Ship currentShip : battleShipsPlayer){
            direction = currentShip.getDirection();
            position = currentShip.getPosition();
            length = BattleShipConfig.getShipLengthByShipType(currentShip.getShipTypeId());
            if(direction.equals(ROW) && valid) {
                if (position.getY() - 1 >= 0) {
                    if (boardMat[position.getX()][position.getY() - 1] == 0 ||
                            boardMat[position.getX()][position.getY() - 1] == 5) {
                        boardMat[position.getX()][position.getY() - 1] = 5; // check around the battelship
                    } else {
                        valid = false;
                    }
                }

                for(int i = 0; i < length; i++){
                    if( boardMat[position.getX()][position.getY()] == 0 ) {
                        boardMat[position.getX()][position.getY()] = indexShip;
                        if(position.getX() - 1 >= 0) {
                            if (boardMat[position.getX() - 1][position.getY()] == 0 ||
                                    boardMat[position.getX() - 1][position.getY()] == 5) {
                                boardMat[position.getX() - 1][position.getY()] = 5;
                            } else {
                                valid = false;
                            }
                        }
                        if( position.getX() + 1 <= boardSize) {
                            if (boardMat[position.getX() + 1][position.getY()] == 0 ||
                                    boardMat[position.getX() + 1][position.getY()] == 5) {
                                boardMat[position.getX() + 1][position.getY()] = 5;
                            } else {
                                valid = false;
                            }
                        }

                        if(position.getY() + 1 <= boardSize + 1) {
                            position.setY(position.getY() + 1);
                        }
                        else{
                            valid = false;
                        }
                    }
                    else{
                        valid = false;
                    }
                }
                if( position.getY() <= boardSize + 1){
                    boardMat[position.getX()][position.getY()] = 5;
                }
            }
            else if(direction.equals(COLUMN) && valid){
                if( position.getX() - 1 >= 0) {
                    if (boardMat[position.getX() - 1][position.getY()] == 0 ||
                            boardMat[position.getX() - 1][position.getY()] == 5) {
                                boardMat[position.getX() - 1][position.getY()] = 5; // check around the battelship
                    } else {
                        valid = false;
                    }
                }
                for(int i = 0; i < length; i++){
                    if( boardMat[position.getX()][position.getY()] == 0 ) {
                        boardMat[position.getX()][position.getY()] = indexShip;
                        if(position.getY() - 1 >= 0) {
                            if (boardMat[position.getX()][position.getY() - 1] == 0 ||
                                    boardMat[position.getX()][position.getY() - 1] == 5) {
                                boardMat[position.getX()][position.getY() - 1] = 5;
                            } else {
                                valid = false;
                            }
                        }
                        if( position.getY() + 1 <= boardSize) {
                            if (boardMat[position.getX()][position.getY() + 1] == 0 ||
                                    boardMat[position.getX()][position.getY() + 1] == 5) {
                                boardMat[position.getX()][position.getY() + 1] = 5;
                            } else {
                                valid = false;
                            }
                        }

                        if(position.getX() + 1 <= boardSize + 1) {
                            position.setX(position.getX() + 1);
                        }
                        else{
                            valid = false;
                        }
                    }
                    else{
                        valid = false;
                    }
                }
                if( position.getX() <= boardSize + 1){
                    boardMat[position.getX()][position.getY()] = 5;
                }
            }
        }

        if(!valid){
            System.out.println("Battelships location invalid!");//exeption;
        }

//        for(int i = 0; i < boardSize ;i++) {
//            for (int j = 0; j < boardSize ; j++) {
//                System.out.print( " " + boardMat[i][j] + " ");
//            }
//            System.out.print("\n");
//        }
//        System.out.print("---------------------------------\n");

    }


    private void setBattelShipConfiguration(BattleShipGame battleShip) {
        BattleShipGame.ShipTypes shipTypes = battleShip.getShipTypes();

        try {

            for (BattleShipGame.ShipTypes.ShipType shipType : shipTypes.getShipType()) {
                if (shipType.getId().equals("A")) {
                    BattleShipConfig.setShipAmountTypeA(shipType.getAmount());
                    BattleShipConfig.setShipLengthTypeA(shipType.getLength());
                    BattleShipConfig.setShipScoreTypeA(shipType.getScore());
                } else if (shipType.getId().equals("B")) {
                    BattleShipConfig.setShipAmountTypeB(shipType.getAmount());
                    BattleShipConfig.setShipLengthTypeB(shipType.getLength());
                    BattleShipConfig.setShipScoreTypeB(shipType.getScore());
                } else if (shipType.getId().equals("L")) {
                    BattleShipConfig.setShipAmountTypeL(shipType.getAmount());
                    BattleShipConfig.setShipLengthTypeL(shipType.getLength());
                    BattleShipConfig.setShipScoreTypeL(shipType.getScore());
                } else {
                    throw new Exception("ship type " + shipType.getId() + " not exist in our game");
                }
            }
        }catch (Exception e){
            System.err.println("Exception: " + e);
        }
    }

    private void checkIfBoardSizeLiggal(int boardSize) {
        try {
            if (boardSize > MAX_BOARD_SIZE || boardSize < MIN_BOARD_SIZE) {
                throw new Exception("Illegal board size");
            }
        } catch (Exception e) {
                System.err.println("Exception : " + e);
        }

    }
}
