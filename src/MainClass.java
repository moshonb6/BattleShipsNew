import Project.UI.UserIteration;
import Project.UI.XmlLoader;
import Project.modules.GameManager;
import Resources.BattleShipGame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

public class MainClass {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        int userChoice = UserIteration.MainMsg();

        switch (userChoice) {
            case 1:
                try {
                    XmlLoader xml = new XmlLoader();
                    xml.loadBattelShipsConfig();
                    gameManager.initPlayers();
                }catch (Exception e){

                }


                break;
            case 2:
                gameManager.playGame();
                break;
            case 6:
                exit(1);
                break;
        }
    }
}
