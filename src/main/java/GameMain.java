import java.util.HashMap;

public class GameMain {
    public static void main(String[] args) {
        MakoRobot makoRobot=new MakoRobot("https://www.mako.co.il/");
        //System.out.println("mako headlines:");
       // makoRobot.getWordsStatistics();
        System.out.println("////////////////////////////////////////");
         WallaRobot wallaRobot =new WallaRobot("https://www.walla.co.il/");
        //System.out.println("walla headline:");
         //wallaRobot.getWordsStatistics();
        System.out.println("///////////////ynet//////////////////");

        YnetRobot ynetRobot = new YnetRobot("https://www.ynet.co.il/home/0,7340,L-8,00.html");
        ynetRobot.getWordsStatistics();






    }
}
