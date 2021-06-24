import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YnetRobot extends BaseRobot {
    public YnetRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        HashMap<String, Integer> mapOfWords = new HashMap<>();
        try {
            String currentWord = "";
            //        k        v
            Document yentWebsite = Jsoup.connect(this.getRootWebsiteUrl()).get();
            Elements allLinksArticle = yentWebsite.getElementsByAttribute("href");
            System.out.println("found: " + allLinksArticle.size());
            StringBuilder fullText = new StringBuilder();
            for (int i = 0; i < allLinksArticle.size(); i++) {
                Element currentLink = allLinksArticle.get(i);
                String link = currentLink.attr("href");
                if (i != allLinksArticle.size() - 1) {
                    if (link.contains("https://www.ynet.co.il") && !link.equals(allLinksArticle.get(i + 1).attr("href"))) {
                        Document ynetHomePage = Jsoup.connect(link).get();
                        Elements mainTitle = ynetHomePage.getElementsByClass("mainTitle");
                        Elements secondaryTitle = ynetHomePage.getElementsByClass("subTitle");
                        Elements articleBody = ynetHomePage.getElementsByAttribute("data-text");
                        fullText.append(mainTitle.text());
                        fullText.append(secondaryTitle.text());
                        for (Element element : articleBody) {
                            fullText.append(element.text());

                        }
                    }
                }
            }
            int counterOfWord=1;
           String[] allWords=fullText.toString().split(" ");

              try {

                  for (int i = 0; i < allWords.length; i++) {
                      currentWord = allWords[i];
                      try {
                          if (mapOfWords.get(currentWord)!=null){
                             Integer newCounterOfWord=mapOfWords.get(currentWord)+1;
                             mapOfWords.put(currentWord,newCounterOfWord);

                          }else{
                              mapOfWords.put(currentWord,counterOfWord);
                          }
                      }catch (NullPointerException e){
                          e.printStackTrace();
                      }

                  }
                  System.out.println(mapOfWords.get("שטיפת"));

              }catch (ArrayIndexOutOfBoundsException e){
                  e.printStackTrace();
              }


        }catch (IOException e){
            e.printStackTrace();
        }

        return mapOfWords;
    }

    @Override
    public int countInArticlesTitles(String text) {
        return 0;
    }

    @Override
    public String getLongestArticleTitle() {
        return null;
    }
    /*private void insert(HashMap<String,Integer> wordsMap,String word,int value ){
        wordsMap.put(word,value);


    }

     */
}
