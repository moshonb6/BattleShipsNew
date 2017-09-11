package Project.UI;

import Project.modules.BattleShipConfig;
import Project.modules.GameManager;
import Resources.BattleShipGame;
import javax.xml.bind.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class XmlLoader {
    private static final String XSD_NAME = "BattelShipsGame.xsd";
    private static final String XML_NAME = "BattelShipsGame.xml";
    private static final String FULLPATH = "src\\Resources\\BattleShipGame.xml";
    private static final String SLASH = "/";
    private static final String ROW = "ROW";
    private static final String COLUMN = "COLUMN";
    private static final String RIGHT_UP = "RIGHT_UP";
    private static final String RIGHT_DOWN = "RIGHT_DOWN";
    private static final String UP_RIGHT = "UP_RIGHT";
    private static final String DOWN_RIGHT = "DOWN_RIGHT";
    private static final int MAX_BOARD_SIZE = 20;
    private static final int MIN_BOARD_SIZE = 5;
    private static int AROUND_SHIP = 10;
    private static int boardSize = 0;
    private static ArrayList<BattleShipGame.Boards.Board.Ship> battleShipsPlayer1 = new ArrayList<>();
    private static ArrayList<BattleShipGame.Boards.Board.Ship> battleShipsPlayer2 = new ArrayList<>();
    private static ArrayList<BattleShipConfig> battleShipConfigs;
    public static ArrayList<BattleShipGame.Boards.Board.Ship> getBattleShipsPlayer1(){return battleShipsPlayer1;}
    public static ArrayList<BattleShipGame.Boards.Board.Ship> getBattleShipsPlayer2(){return battleShipsPlayer2;}
    public static ArrayList<BattleShipConfig> getBattleShipConfigs(){return battleShipConfigs;}

    public XmlLoader() {
        battleShipsPlayer1.clear();
        battleShipsPlayer2.clear();
    }

    public Boolean loadBattelShipsConfig() throws Exception{
        Boolean valid;
        String fullPath = UserIteration.getFullPathMsg();
        JAXBContext jc = JAXBContext.newInstance(BattleShipGame.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xml = new File(fullPath);
        BattleShipGame battleShip = (BattleShipGame) unmarshaller.unmarshal(xml);
        valid = inputValidation(battleShip);

        return valid;

    }

    private Boolean inputValidation(BattleShipGame battleShip) throws  Exception {
        boolean valid = true;
        boolean player1 = true;
        boardSize = battleShip.getBoardSize();
        GameManager.setBoardSize(boardSize);
        checkIfBoardSizeLiggal(boardSize);
        Map<String,Integer> amountByIdPlayer1 = new HashMap<>();
        Map<String,Integer> amountByIdPlayer2 = new HashMap<>();

        BattleShipGame.Boards boards = battleShip.getBoards();
        int minesAmount = battleShip.getMine().getAmount();

        BattleShipConfig.setMinesAmount(minesAmount);
        battleShipConfigs = setBattelShipConfiguration(battleShip);

        for(BattleShipGame.Boards.Board board : boards.getBoard() ){
            if(player1) {
                for (BattleShipGame.Boards.Board.Ship ship : board.getShip()) {
                    battleShipsPlayer1.add(ship);
                    if(amountByIdPlayer1.containsKey(ship.getShipTypeId())){
                        amountByIdPlayer1.put(ship.getShipTypeId(), amountByIdPlayer1.get(ship.getShipTypeId()) + 1);
                    }
                    else{
                        amountByIdPlayer1.put(ship.getShipTypeId(), 1);
                    }
                }
                player1 = false;
            }
            else{
                for (BattleShipGame.Boards.Board.Ship ship : board.getShip()) {
                    battleShipsPlayer2.add(ship);
                    if(amountByIdPlayer2.containsKey(ship.getShipTypeId())){
                        amountByIdPlayer2.put(ship.getShipTypeId(), amountByIdPlayer2.get(ship.getShipTypeId()) + 1);
                    }
                    else{
                        amountByIdPlayer2.put(ship.getShipTypeId(), 1);
                    }
                }
            }
        }

        valid &= checkBattleShipsAmount(amountByIdPlayer1);
        valid &= checkBattleShipsAmount(amountByIdPlayer2);

        valid &= setAndCheckBattleShipsLocation(battleShipsPlayer1);

        valid &= setAndCheckBattleShipsLocation(battleShipsPlayer2);

        return valid;
    }

    private boolean checkBattleShipsAmount(Map<String, Integer> amountById) throws Exception{
        int amount;

        for(Map.Entry<String,Integer> entry : amountById.entrySet()){
            amount = getShipAmountByShipType(entry.getKey());
            if(amount != entry.getValue()){
                throw new Exception("battleShips amount not equal to battleShips players");
            }
        }

        return true;
    }


    public static int getShipLengthByShipType(String i_type) throws Exception{
        int res = 0;

        for(BattleShipConfig battleShip : battleShipConfigs){
            if(i_type.equals(battleShip.getShipType())){
                res = battleShip.getShipLength();
            }
        }

        if(res == 0){
            throw new Exception("BattleShip type does not exist or equal to 0");
        }

        return res;
    }

    public static int getShipScoreByShipType(String shipTypeId) {
        int res = 0;

        for(BattleShipConfig battleShip : battleShipConfigs){
            if(shipTypeId.equals(battleShip.getShipType())){
                res = battleShip.getShipScore();
            }
        }

        return res;
    }

    public static int getShipAmountByShipType(String i_type) throws Exception{
        int res = 0;

        for(BattleShipConfig battleShip : battleShipConfigs){
            if(i_type.equals(battleShip.getShipType())){
                res = battleShip.getShipAmount();
            }
        }

        if(res == 0){
            throw new Exception("BattleShip type does not exist or equal to 0");
        }

        return res;
    }

    private Boolean setAndCheckBattleShipsLocation(ArrayList<BattleShipGame.Boards.Board.Ship> battleShipsPlayer) throws Exception {
        int[][] boardMat = new int[boardSize][boardSize];
        String direction;
        int length;
        BattleShipGame.Boards.Board.Ship.Position position;
        int shipIndex = 0;
        Boolean valid = true;
        String msg = new String();


        for (BattleShipGame.Boards.Board.Ship currentShip : battleShipsPlayer) {
            direction = currentShip.getDirection();
            position = currentShip.getPosition();
            int positionX = position.getX();
            int positionY = position.getY();
            length = getShipLengthByShipType(currentShip.getShipTypeId());

            if (direction.equals(ROW) && valid) {
                shipIndex++;
                valid &= checkDirectionRow(boardMat, positionX, positionY, length, shipIndex);

            } else if (direction.equals(COLUMN) && valid) {
                shipIndex++;
                valid &= checkDirectionColumn(boardMat, positionX, positionY, length, shipIndex);

            } else if (direction.equals(RIGHT_DOWN) && valid) {
                shipIndex++;
                valid &= checkDirectionRow(boardMat, positionX, positionY - length + 1, length, shipIndex);
                valid &= checkDirectionColumn(boardMat, positionX, positionY, length, shipIndex);
            } else if (direction.equals(DOWN_RIGHT) && valid) {
                shipIndex++;
                valid &= checkDirectionColumn(boardMat, positionX - length +1, positionY, length, shipIndex);
                valid &= checkDirectionRow(boardMat, positionX, positionY, length, shipIndex);
            } else if (direction.equals(RIGHT_UP) && valid) {
                shipIndex++;
                valid &= checkDirectionRow(boardMat, positionX , positionY - length + 1, length, shipIndex);
                valid &= checkDirectionColumn(boardMat, positionX - length + 1 , positionY , length, shipIndex);

            } else if (direction.equals(UP_RIGHT) && valid) {
                shipIndex++;
                valid &= checkDirectionColumn(boardMat, positionX, positionY, length, shipIndex);
                valid &= checkDirectionRow(boardMat, positionX,  positionY, length, shipIndex);
            }
        }

        if (!valid) {
            throw new Exception("Battelships location is invalid! " + msg);
        }

//        for (int i = 0; i < boardSize; i++) {
//            for (int j = 0; j < boardSize; j++) {
//                System.out.print(" " + boardMat[i][j] + " ");
//            }
//            System.out.print("\n");
//        }
//        System.out.print("---------------------------------\n");



        return valid;
    }

    private Boolean checkDirectionColumn(int[][] boardMat, int positionX, int positionY, int length, int shipIndex) throws Exception {
        String msg = new String();
        Boolean valid = true;
        int AROUND_MY_SHIP = shipIndex * 10;

            if (positionX - 1 >= 0) {
                if (boardMat[positionX - 1][positionY] == 0 ||
                        boardMat[positionX - 1][positionY] >= AROUND_SHIP ) {
                    boardMat[positionX - 1][positionY] = AROUND_MY_SHIP; // check around the battelship
                }else if(  boardMat[positionX - 1][positionY] == shipIndex) {
                    boardMat[positionX - 1][positionY] = shipIndex;
                }
                 else {
                    valid = false;
                    int tmpX = positionX - 1;
                    msg = "There is a battle ship near square [" + tmpX + "," + positionY + "].";
                    throw new Exception(msg);
                }
            }
            for (int i = 0; i < length; i++) {
                if (boardMat[positionX][positionY] == 0 ||  boardMat[positionX][positionY] == shipIndex
                        || boardMat[positionX][positionY] == AROUND_MY_SHIP) {
                    boardMat[positionX][positionY] = shipIndex;
                    if (positionY - 1 >= 0) {
                        if (boardMat[positionX][positionY - 1] == 0 ||
                                boardMat[positionX][positionY - 1] >= AROUND_SHIP) {
                            boardMat[positionX][positionY - 1] = AROUND_MY_SHIP;
                        }else if(boardMat[positionX][positionY - 1] == shipIndex) {
                            boardMat[positionX][positionY - 1] = shipIndex;
                        }else {
                            valid = false;
                            int tmpY = positionY - 1;
                            msg = "There is a battle ship near square [" + positionX + "," + tmpY + "].";
                            throw new Exception(msg);
                        }
                    }
                    if (positionY + 1 < boardSize) {
                        if (boardMat[positionX][positionY + 1] == 0 ||
                                boardMat[positionX][positionY + 1] >= AROUND_SHIP) {
                            boardMat[positionX][positionY + 1] = AROUND_MY_SHIP;
                        }else if(boardMat[positionX][positionY + 1] == shipIndex){
                            boardMat[positionX][positionY + 1] = shipIndex;
                        }else {
                            valid = false;
                            int tmpY = positionY + 1;
                            msg = "There is a battle ship near square [" + positionX + "," + tmpY + "].";
                            throw new Exception(msg);
                        }
                    }

                    if (positionX < boardSize ) {
                        positionX++;
                    } else {
                        msg = "There is a battle ship out of the board range";
                        throw new Exception(msg);
                    }

                }else if(boardMat[positionX][positionY] == AROUND_MY_SHIP){
                    boardMat[positionX][positionY] = shipIndex;
                }else if (boardMat[positionX][positionY] >= AROUND_SHIP) {
                    valid = false;
                    msg = "There is a battle ship near square [" + positionX + "," + positionY + "].";
                    throw new Exception(msg);
                } else {
                    valid = false;
                    msg = "There is a battle ship on battle ship!";
                    throw new Exception(msg);
                }
            }
            if (positionX < boardSize && boardMat[positionX][positionY] != shipIndex ) {
                boardMat[positionX][positionY] = AROUND_SHIP;
            }

        return valid;
    }

    private Boolean checkDirectionRow(int[][] boardMat, int positionX, int positionY , int length, int shipIndex) throws  Exception{
        String msg;
        int AROUND_MY_SHIP = shipIndex * 10;
        Boolean valid = true;

            if (positionY - 1 >= 0) {
                if (boardMat[positionX][positionY - 1] == 0 ||
                        boardMat[positionX][positionY - 1] >= AROUND_SHIP) {
                    boardMat[positionX][positionY - 1] = AROUND_MY_SHIP; // check around the battelship
                }else if(boardMat[positionX][positionY - 1] == shipIndex) {
                    boardMat[positionX][positionY - 1] = shipIndex;
                } else{
                    valid = false;
                    int tmpY = positionY - 1;
                    msg = "There is a battle ship near square [" + positionX + "," + tmpY + "].";
                    throw new Exception(msg);
                }
            }

            for (int i = 0; i < length; i++) {
                if (boardMat[positionX][positionY] == 0 || boardMat[positionX][positionY] == shipIndex
                        || boardMat[positionX][positionY] == AROUND_MY_SHIP) {
                    boardMat[positionX][positionY] = shipIndex;
                    if (positionX - 1 >= 0) {
                        if (boardMat[positionX - 1][positionY] == 0 ||
                                boardMat[positionX - 1][positionY] >= AROUND_SHIP) {
                            boardMat[positionX - 1][positionY] = AROUND_MY_SHIP;
                        }else if(boardMat[positionX - 1][positionY] == shipIndex){
                            boardMat[positionX - 1][positionY] = shipIndex;
                        }else {
                            valid = false;
                            int tmpX = positionX - 1;
                            msg = "There is a battle ship near square [" + tmpX + "," + positionY + "].";
                            throw new Exception(msg);
                        }
                    }
                    if (positionX + 1 < boardSize) {
                        if (boardMat[positionX + 1][positionY] == 0 ||
                                boardMat[positionX + 1][positionY] >= AROUND_SHIP) {
                            boardMat[positionX + 1][positionY] = AROUND_MY_SHIP;
                        }else if(boardMat[positionX + 1][positionY] == shipIndex){
                            boardMat[positionX + 1][positionY] = shipIndex;
                        } else {
                            valid = false;
                            int tmpX = positionX + 1;
                            msg = "There is a battle ship near square [" + tmpX + "," + positionY + "].";
                            throw new Exception(msg);
                        }
                    }

                    if (positionY < boardSize ) {
                        positionY++;
                    } else {
                        valid = false;
                        msg = "There is a battle ship out of the board range";
                        throw new Exception(msg);
                    }
                }else if(boardMat[positionX][positionY] == AROUND_MY_SHIP){
                    boardMat[positionX][positionY] = shipIndex;
                }else if (boardMat[positionX][positionY] >= AROUND_SHIP) {
                    valid = false;
                    msg = "There is a battle ship near square [" + positionX + "," + positionY + "].";
                    throw new Exception(msg);
                } else {
                    valid = false;
                    msg = "There is a battle ship on battle ship!";
                    throw new Exception(msg);
                }
            }
            if (positionY < boardSize && boardMat[positionX][positionY]!= shipIndex) {
                boardMat[positionX][positionY] = AROUND_MY_SHIP;
            }

        return valid;
    }


    private ArrayList<BattleShipConfig> setBattelShipConfiguration(BattleShipGame battleShip) {
        BattleShipGame.ShipTypes shipTypes = battleShip.getShipTypes();
        ArrayList<BattleShipConfig> battleShipsTypes = new ArrayList<>();

        for (BattleShipGame.ShipTypes.ShipType shipType : shipTypes.getShipType()) {
            BattleShipConfig battleShipConfig = new BattleShipConfig();
            battleShipConfig.setShipAmount(shipType.getAmount());
            battleShipConfig.setShipLength(shipType.getLength());
            battleShipConfig.setShipScore(shipType.getScore());
            battleShipConfig.setShipType(shipType.getId());
            battleShipsTypes.add(battleShipConfig);
        }

        return battleShipsTypes;
    }

    private void checkIfBoardSizeLiggal(int boardSize) throws Exception{
        if (boardSize > MAX_BOARD_SIZE || boardSize < MIN_BOARD_SIZE) {
            throw new Exception("Illegal board size , the min size is: " + MIN_BOARD_SIZE + "and max size is " + MAX_BOARD_SIZE);
        }
    }


}
