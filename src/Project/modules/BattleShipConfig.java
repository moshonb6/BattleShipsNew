package Project.modules;

public class BattleShipConfig {
    private String shipType;
    private int shipAmount = 0;
    private int shipLength = 0;
    private int shipScore = 0;
    private static int minesAmount = 0;

    public BattleShipConfig(){

    }

    public static void setMinesAmount(int amount){minesAmount = amount;}
    public static int getMinesAmount(){return  minesAmount;}

    public int getShipAmount(){return  shipAmount;}
    public int getShipLength(){return  shipLength;}
    public int getShipScore(){return  shipScore;}
    public String getShipType(){return  shipType;}

    public void setShipAmount(int i_shipAmount){shipAmount = i_shipAmount;}
    public void setShipLength(int i_shipLength){shipLength = i_shipLength;}
    public void setShipScore(int i_shipScore){shipScore = i_shipScore;}
    public void setShipType(String i_shipType){shipType = i_shipType;}
}
