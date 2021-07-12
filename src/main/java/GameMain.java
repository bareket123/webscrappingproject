import java.util.Map;
import java.util.Scanner;

public class GameMain {

    private static YnetRobot ynetRobot;
    private static MakoRobot makoRobot;
    private static WallaRobot wallaRobot;

    public static void main(String[] args) {
        //Game itself
        Scanner scanner = new Scanner(System.in);
        int userScore=0;
        String guessedWord="";
        System.out.println("which website would you like to check out? (please enter a number 1-3) \n1.walla \n2.mako\n3.ynet ");
        try {
        int userChoice=scanner.nextInt();
            userChoice=invalidChoice(userChoice,Def.FIRST_OPTION,Def.LAST_OPTION);
            System.out.println("Guess what are the most common words on the site ");
            System.out.print("let me give you a hint, the longest article's title is: ");
            switch (userChoice){
            case Def.WALLA_MENU:
                 wallaRobot =new WallaRobot("https://www.walla.co.il/");
                String longestTitle=wallaRobot.getLongestArticleTitle();
                System.out.print("~"+longestTitle+"~");
                Map<String,Integer>wallaMap= wallaRobot.getWordsStatistics();
                 userScore+=guessedTheWords(wallaMap);
                 userScore+=winBonusPointForLuckyGuess(Def.WALLA_MENU);
                   break;
                case Def.MAKO_MENU:
                     makoRobot=new MakoRobot("https://www.mako.co.il/");
                     longestTitle=makoRobot.getLongestArticleTitle();
                     System.out.print("~'"+longestTitle+"'~");
                    Map<String,Integer> makoMap =makoRobot.getWordsStatistics();
                      userScore+=guessedTheWords(makoMap);
                      userScore+=winBonusPointForLuckyGuess(Def.MAKO_MENU);
                    break;
                    case Def.YNET_MENU:
                        try {
                         ynetRobot = new YnetRobot("https://www.ynet.co.il/home/0,7340,L-8,00.html");
                        longestTitle= ynetRobot.getLongestArticleTitle();
                        System.out.println("--"+longestTitle+"--");
                        scanner.nextLine();
                        Map<String, Integer> ynetMap = ynetRobot.getWordsStatistics();
                            userScore=+guessedTheWords(ynetMap);
                            userScore+=winBonusPointForLuckyGuess(Def.YNET_MENU);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;


        }

    }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("\nyour total score is: " + userScore + " points, great job!!");

    }public static int guessedTheWords(Map<String,Integer>wordsMap){
        Scanner scanner=new Scanner(System.in);
        int points=0;
        System.out.println();
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
    public static int winBonusPointForLuckyGuess(int caseNumber){
        Scanner scanner=new Scanner(System.in);
        int bonusPoint=0;
        System.out.println("Enter a text you think will appear in the titles of the website, (20 characters maximum)");
        String guessedText=scanner.nextLine();
        System.out.println("....wait for it.....");
            while (countCharactersWithoutSpace(guessedText) > Def.MAX_CHARACTERS) {
                System.out.println("you enter more than 20 characters, please enter again)");
                guessedText = scanner.nextLine();

            }
            System.out.println("Guess how many times the text you write appeared in the titles");
            int userGuess=scanner.nextInt();
            switch (caseNumber){
                case Def.WALLA_MENU:
                    bonusPoint=winBonusPointsWalla(guessedText,userGuess);
                  break;
                case Def.MAKO_MENU:
                    bonusPoint=winBonusPointMako(guessedText,userGuess);
                    break;
                    case Def.YNET_MENU:
                        bonusPoint=winBonusPointYnet(guessedText,userGuess);
                        break;
            }


        return bonusPoint;
    }
    public static int invalidChoice(int useChoice, int firstOptionInMenu,int lastOptionInMenu) {
        Scanner scanner=new Scanner(System.in);
        while (useChoice > lastOptionInMenu || useChoice<firstOptionInMenu) {
            System.out.println("you enter invalid value, please try again");
            useChoice = scanner.nextInt();

        }
        return useChoice;
    }
 public static int countCharactersWithoutSpace(String text){
       int userCharacters=0;
       char [] characters = text.toCharArray();
        userCharacters =characters.length;
         for (int i = 0; i < characters.length; i++) {
             if (characters[i]==' '){
                 userCharacters--;
             }
     }


    return userCharacters;

 }public static int winBonusPointYnet(String guessedText,int userGuess){
        int ynetBonus=0;
        int textAppeared=ynetRobot.countInArticlesTitles(guessedText);
        int range=textAppeared-userGuess;
        if (range<0){
            range*=-1;
        }
        if (userGuess==textAppeared || (range<=Def.RANGE && range>0) || range==userGuess+Def.RANGE ){
            ynetBonus=Def.BONUS_FOR_RIGHT_GUESS;
            System.out.println("Congratulations you won 250 bonus points");
        }else
            System.out.println("unfortunately your guess isn't accurate");


        return ynetBonus;


    }public static int  winBonusPointMako(String guessedText,int userGuess){
        int makoBonus=0;
        int textCounter=makoRobot.countInArticlesTitles(guessedText);
        int rangeGuesses=textCounter-userGuess;
        if (rangeGuesses<0){
            rangeGuesses*=-1;
        }
        if (userGuess==textCounter || (rangeGuesses<=2 && rangeGuesses>0) || rangeGuesses==textCounter+Def.RANGE ){
            System.out.println("Congratulation, you won 250 bonus points");
            makoBonus=Def.BONUS_FOR_RIGHT_GUESS;
        }else
            System.out.println("unfortunately, your guess isn't accurate, you didn't get the bonus points ");
        return makoBonus;

    }
    public static int winBonusPointsWalla(String guessedText,int userGuess){
        int wallaBonus=0;
        try {
            int textCounter=wallaRobot.countInArticlesTitles(guessedText);
            int rangeGuesses=textCounter-userGuess;
            if (rangeGuesses<0){
                rangeGuesses*=-1;
            }
            if (userGuess==textCounter || (rangeGuesses>0 && rangeGuesses<=Def.RANGE) || rangeGuesses==textCounter+Def.RANGE){
                System.out.println("Congratulation, you won 250 bonus points");
                wallaBonus=Def.BONUS_FOR_RIGHT_GUESS;
            }else
                System.out.println("unfortunately, your guess isn't accurate, you didn't get the bonus points ");

        }catch (Exception e){
            e.printStackTrace();
        }

        return wallaBonus;
    }


}
