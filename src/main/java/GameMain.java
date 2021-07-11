import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class GameMain {
    public static void main(String[] args) {
        //Game itself
        Scanner scanner = new Scanner(System.in);
        int userScore=0;
        String guessedWord="";
        System.out.println("which website would you like to check out? (please enter a number 1-3) \n1.walla \n2.mako\n3.ynet ");
        try {
        int userChoice=scanner.nextInt();
            System.out.println("Guess what are the most common words on the site ");
            System.out.print("let me give you a hint, the longest article's title is: ");
            switch (userChoice){
            case Def.WALLA_MENU:
                WallaRobot wallaRobot =new WallaRobot("https://www.walla.co.il/");
                String longestTitle=wallaRobot.getLongestArticleTitle();
                System.out.print(longestTitle);
                Map<String,Integer> wallaMap =wallaRobot.getWordsStatistics();
                  userScore+=guessedTheWords(wallaMap);
                   break;
                case Def.MAKO_MENU:
                    MakoRobot makoRobot=new MakoRobot("https://www.mako.co.il/");
                     longestTitle=makoRobot.getLongestArticleTitle();
                    System.out.print(longestTitle);
                    Map<String,Integer> makoMap =makoRobot.getWordsStatistics();
                      userScore+=guessedTheWords(makoMap);
                    break;
                    case Def.YNET_MENU:
                        try {
                        YnetRobot ynetRobot = new YnetRobot("https://www.ynet.co.il/home/0,7340,L-8,00.html");
                        longestTitle=ynetRobot.getLongestArticleTitle();
                        System.out.print(longestTitle);
                        System.out.println();
                        scanner.nextLine();
                        Map<String, Integer> ynetMap = ynetRobot.getWordsStatistics();
                          //  System.out.println(ynetMap);
                         userScore=+guessedTheWords(ynetMap);
                            System.out.println("/////////////////////////////////////////////////////////////");
                            System.out.println("points: " + userScore);
                            System.out.println(ynetRobot.countInArticlesTitles("המיליארדר ריצ'רד ברנסון הגיע לחלל כתייר"));


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;



        }

    }catch (Exception e){
            e.printStackTrace();
        }




        //System.out.println("mako headlines:");
        //System.out.println(makoRobot.getWordsStatistics());
        //System.out.println(makoRobot.countInArticlesTitles("בנט"));
        //System.out.println("/////////////////////walla///////////////////");

        //System.out.println(wallaRobot.getWordsStatistics());
        //System.out.println(wallaRobot.getLongestArticleTitle());


        //System.out.println("///////////////ynet//////////////////");


          //System.out.println(ynetRobot.returnAllTitle2().toString());
        //ynetRobot.getWordsStatistics();
       // System.out.println(ynetRobot.countInArticlesTitles("הבריאות"));
      //  System.out.println(ynetRobot.returnAllTitle2().toString());
        //System.out.println(ynetRobot.getLongestArticleTitle());



    }public static int guessedTheWords(Map<String,Integer>wordsMap){
        Scanner scanner=new Scanner(System.in);
        int points=0;
        for (int i = 1; i <= Def.NUMBER_OF_GUESSES; i++) {
            System.out.println("guess number " + i);
           String guessedWord = scanner.nextLine();
            if ((wordsMap.containsKey(guessedWord))){
                int numberOfWord = wordsMap.get(guessedWord);
                points+=numberOfWord;

            }

        }


        return points;
    }
}
