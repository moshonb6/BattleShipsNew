package Project.modules;

import Resources.BattleShipGame;

import java.awt.*;
import java.util.ArrayList;

public class BattleShipConfig {
    private static int shipAmountTypeA = 0;
    private static int shipAmountTypeB = 0;
    private static int shipAmountTypeL = 0;
    private static int shipLengthTypeA = 0;
    private static int shipLengthTypeB = 0;
    private static int shipLengthTypeL = 0;
    private static int shipScoreTypeA = 0;
    private static int shipScoreTypeB = 0;
    private static int shipScoreTypeL = 0;

    private BattleShipConfig(){

    }

    public static void setShipAmountTypeA(int amount){shipAmountTypeA = amount;}
    public static void setShipAmountTypeB(int amount){shipAmountTypeB = amount;}
    public static void setShipAmountTypeL(int amount){shipAmountTypeL = amount;}

    public static void setShipScoreTypeA(int score){shipScoreTypeA = score;}
    public static void setShipScoreTypeB(int score){shipScoreTypeB = score;}
    public static void setShipScoreTypeL(int score){shipScoreTypeL = score;}

    public static void setShipLengthTypeA(int length){shipLengthTypeA = length;}
    public static void setShipLengthTypeB(int length){shipLengthTypeB = length;}
    public static void setShipLengthTypeL(int length){shipLengthTypeL = length;}

    public static int getShipAmountTypeA() {return shipAmountTypeA;}
    public static int getShipAmountTypeB() {return shipAmountTypeB;}
    public static int getShipAmountTypeL() {return shipAmountTypeL;}

    public static int getShipLengthTypeA() {return shipLengthTypeA;}
    public static int getShipLengthTypeB() {return shipLengthTypeB;}
    public static int getShipLengthTypeL() {return shipLengthTypeL;}

    public static int getShipLengthByShipType(String i_type){
        int res = 0;
        if( i_type.equals("A")){
            res =  getShipLengthTypeA();
        }
        else if( i_type.equals("B")){
            res =  getShipLengthTypeB();
        }
        if( i_type.equals("L")){
            res =  getShipLengthTypeL();
        }
        return res;
    }
}
