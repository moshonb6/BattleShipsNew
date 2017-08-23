package Project.UI;

import Project.modules.BattleShipConfig;
import Project.modules.GameManager;
import Resources.BattleShipGame;

import javax.xml.bind.*;
import java.io.File;
import java.util.ArrayList;


public class XmlLoader {
    private static final String XSD_NAME = "BattelShipsGame.xsd";
    private static final String XML_NAME = "BattelShipsGame.xml";
    private static final String SLASH = "/";
    private static final String ROW = "ROW";
    private static final String COLUMN = "COLUMN";
    private static final int MAX_BOARD_SIZE = 20;
    private static final int MIN_BOARD_SIZE = 5;
    private static final int AROUND_SHIP = 5;
    private static int boardSize = 0;
    private static ArrayList<BattleShipGame.Boards.Board.Ship> battleShipsPlayer1 = new ArrayList<>();
    private static ArrayList<BattleShipGame.Boards.Board.Ship> battleShipsPlayer2 = new ArrayList<>();

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
        GameManager.setBoardSize(boardSize);
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
        String msg = new String();

        try {
            for (BattleShipGame.Boards.Board.Ship currentShip : battleShipsPlayer) {
                direction = currentShip.getDirection();
                position = currentShip.getPosition();
                int positionX = position.getX();
                int positionY = position.getY();
                length = BattleShipConfig.getShipLengthByShipType(currentShip.getShipTypeId());
                if (direction.equals(ROW) && valid) {
                    if (positionY - 1 >= 0) {
                        if (boardMat[positionX][positionY - 1] == 0 ||
                                boardMat[positionX][positionY - 1] == AROUND_SHIP) {
                            boardMat[positionX][positionY - 1] = AROUND_SHIP; // check around the battelship
                        } else {
                            valid = false;
                            int tmpY = positionY-1;
                            msg = "There is a battle ship near square ["+positionX+","+tmpY+"].";
                        }
                    }

                    for (int i = 0; i < length; i++) {
                        if (boardMat[positionX][positionY] == 0) {
                            boardMat[positionX][positionY] = indexShip;
                            if (positionX - 1 >= 0) {
                                if (boardMat[positionX - 1][positionY] == 0 ||
                                        boardMat[positionX - 1][positionY] == AROUND_SHIP) {
                                    boardMat[positionX - 1][positionY] = AROUND_SHIP;
                                } else {
                                    valid = false;
                                    int tmpX = positionX-1;
                                    msg = "There is a battle ship near square ["+tmpX+","+positionY+"].";

                                }
                            }
                            if (positionX + 1 <= boardSize) {
                                if (boardMat[positionX + 1][positionY] == 0 ||
                                        boardMat[positionX + 1][positionY] == AROUND_SHIP) {
                                    boardMat[positionX + 1][positionY] = AROUND_SHIP;
                                } else {
                                    valid = false;
                                    int tmpX = positionX+1;
                                    msg = "There is a battle ship near square ["+tmpX+","+positionY+"].";

                                }
                            }

                            if (positionY + 1 <= boardSize + 1) {
                                positionY++;
                            } else {
                                valid = false;
                                msg = "There is a battle ship near square ["+positionX+","+positionY+"].";
                            }
                        } else {
                            valid = false;
                            msg = "There is a battle ship near square ["+positionX+","+positionY+"].";
                        }
                    }
                    if (positionY <= boardSize + 1) {
                        boardMat[positionX][positionY] = AROUND_SHIP;
                    }
                } else if (direction.equals(COLUMN) && valid) {
                    if (positionX - 1 >= 0) {
                        if (boardMat[positionX - 1][positionY] == 0 ||
                                boardMat[positionX - 1][positionY] == AROUND_SHIP) {
                            boardMat[positionX - 1][positionY] = AROUND_SHIP; // check around the battelship
                        } else {
                            valid = false;
                            int tmpX = positionX-1;
                            msg = "There is a battle ship near square ["+tmpX+","+positionY+"].";
                        }
                    }
                    for (int i = 0; i < length; i++) {
                        if (boardMat[positionX][positionY] == 0) {
                            boardMat[positionX][positionY] = indexShip;
                            if (positionY - 1 >= 0) {
                                if (boardMat[positionX][positionY - 1] == 0 ||
                                        boardMat[positionX][positionY - 1] == AROUND_SHIP) {
                                    boardMat[positionX][positionY - 1] = AROUND_SHIP;
                                } else {
                                    valid = false;
                                    int tmpY = positionY-1;
                                    msg = "There is a battle ship near square ["+positionX+","+tmpY+"].";
                                }
                            }
                            if (positionY + 1 <= boardSize) {
                                if (boardMat[positionX][positionY + 1] == 0 ||
                                        boardMat[positionX][positionY + 1] == AROUND_SHIP) {
                                    boardMat[positionX][positionY + 1] = AROUND_SHIP;
                                } else {
                                    valid = false;
                                    int tmpY = positionY+1;
                                    msg = "There is a battle ship near square ["+positionX+","+tmpY+"].";
                                }
                            }

                            if (positionX + 1 <= boardSize + 1) {
                                positionX++;
                            } else {
                                valid = false;
                            }
                        } else {
                            valid = false;
                        }
                    }
                    if (positionX <= boardSize) {
                        boardMat[positionX][positionY] = AROUND_SHIP;
                    }
                }
            }


        }catch (Exception e) {
            if (!valid) {
                System.out.println("Battelships location is invalid! " + msg);//exeption;
            }
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
            if(boardSize < MIN_BOARD_SIZE)
                System.err.println("Exception : you'r board size is " +boardSize +" it should be at least " + MIN_BOARD_SIZE);
            else
            {
                System.err.println("Exception : you'r board size is " +boardSize +" it should maximum " + MAX_BOARD_SIZE);
            }
        }
    }
}
