import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WallaRobot extends BaseRobot{
    private final ArrayList<String> allArticlesLinks=new ArrayList<>();
    public WallaRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        Map<String, Integer> wallaMap = new HashMap<>();
        int value;
        String mainTitle,subTitle,articleBody;
        StringBuilder fullText=new StringBuilder();
        Document currentLink;
        initLinks();
        try {
            for (String link : allArticlesLinks) {
                currentLink= Jsoup.connect(link).get();
                mainTitle =currentLink.getElementsByTag("h1").text() + " ";
                subTitle=currentLink.getElementsByTag("p").get(0).text() + " ";
                articleBody=currentLink.getElementsByClass("article-content").get(0).getElementsByTag("p").get(0).text() + " ";
               // fullText.append(currentLink.getElementsByClass("css-onxvt4").get(0).getElementsByTag("p").get(0).text());
                fullText.append(mainTitle);
                fullText.append(subTitle);
                fullText.append(articleBody);

                String[] allTextOfArticles=fullText.toString().split(" ");
                for (String word:allTextOfArticles) {
                        if (wallaMap.containsKey(word)) {
                            value = wallaMap.get(word) + 1;
                        } else {
                            value = 1;
                        }
                        wallaMap.put(word, value);
                    }
                }

            }catch (IOException e){
            e.printStackTrace();
        }

           return wallaMap;

    }

    @Override
    public int countInArticlesTitles(String text) throws IOException {
        Document currentLink;
        String mainTitles,subtitles;
        String [] titlesArray;
        StringBuilder allTitles=new StringBuilder();
        initLinks();
        int textCounter=0;
        for (String link:allArticlesLinks) {
            currentLink=Jsoup.connect(link).get();
            mainTitles =currentLink.getElementsByTag("h1").text() + " ";
            subtitles=currentLink.getElementsByTag("p").get(0).text() + " ";
            allTitles.append(mainTitles);
            allTitles.append(subtitles);
        }
        titlesArray=allTitles.toString().split(" ");
        for (String s : titlesArray) {
            if (s.equals(text)) {
                textCounter++;
            }
        }

        return textCounter;
    }

    @Override
    public String getLongestArticleTitle() {
        Document currentLink;
        StringBuilder articleText = new StringBuilder();
        String longestArticleTitle = "";
        int longestArticlesText = 0;
        for (String link : allArticlesLinks) {
            try {
                currentLink = Jsoup.connect(link).get();
                String title = currentLink.getElementsByTag("h1").text();
                String article= currentLink.getElementsByClass("article-content").get(0).getElementsByTag("p").get(0).text();
                /*for (Element articleBody : currentLink.getElementsByClass("article-content").get(0).getElementsByTag("p")) {
                    articleText.append(articleBody.text());
                    articleText.append(" ");
                }

                 */
                articleText.append(article);
                if (longestArticlesText < articleText.length()) {
                    longestArticlesText = articleText.length();
                    longestArticleTitle = title;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return longestArticleTitle;
    }

    private void initLinks(){
        try {
            Document wallaWebsite=Jsoup.connect(this.getRootWebsiteUrl()).get();
            for (Element links : wallaWebsite.getElementsByClass("with-roof ")) {
                allArticlesLinks.add(links.child(0).attributes().get("href"));
            }
            Element section = wallaWebsite.getElementsByClass("css-1ugpt00 css-a9zu5q css-rrcue5 ").get(0);
            for (Element smallTeasers : section.getElementsByTag("a")) {
                allArticlesLinks.add(smallTeasers.attributes().get("href"));
            }



        }catch (IOException e){
            e.printStackTrace();
        }







    }













}

