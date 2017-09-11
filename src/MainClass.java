import Project.UI.UserIteration;
import Project.UI.XmlLoader;
import Project.modules.GameManager;

import static java.lang.System.exit;

public class MainClass {
    private static Boolean gameLoaded = false;
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        int userChoice = UserIteration.MainMsg();

        switch (userChoice) {
            case 1:
                try {
                    XmlLoader xml = new XmlLoader();
                    gameLoaded = xml.loadBattelShipsConfig();
                    UserIteration.loadGameCompleteMsg();
                    main(args);
                }catch (Exception e){
                    System.out.println("Exception: " + e.getMessage());
                    main(args);
                }
                break;
            case 2:
                try {
                    if (gameLoaded) {
                        gameManager.playGame();
                        main(args);
                    } else {
                        UserIteration.loadGameBeforeStartMsg();
                        main(args);
                    }
                }catch (Exception e){

                }
                break;
            case 6:
                exit(1);
                break;
        }
    }
}
