import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WallaRobot extends BaseRobot implements FixableText {

    private final ArrayList<String> allArticlesLinks=new ArrayList<>();

    public WallaRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
        initLinks();

    }

    @Override
    public Map<String, Integer> getWordsStatistics()  {
        Map<String,Integer > wordsMap =new HashMap<>();
        int value;

        try {

            for (String site : allArticlesLinks) {
                String siteText;
                siteText = returnLinkText(site);
                siteText = correctWords(siteText);
                String[] wordsOfArticle = siteText.split(" ");
                for (String word : wordsOfArticle) {
                    if (wordsMap.containsKey(word)) {
                        value = wordsMap.get(word) + 1;
                    } else {
                        value = 1;
                    }
                    wordsMap.put(word, value);
                }

            }
        }catch (Exception e){
            //e.printStackTrace();
        }
        return wordsMap ;

    }

    @Override
    public int countInArticlesTitles(String text){
        int textCount = 0;String title;
        try {
            Document walla = Jsoup.connect(getRootWebsiteUrl()).get();
        for (Element element : walla.getElementsByClass("with-roof ")) {
            title = element.getElementsByTag("h2").text();
            System.out.println(title);
            if (title.contains(text)) {
                textCount++;
            }
        }
        Element subtitlesElements = walla.getElementsByClass("css-1ugpt00 css-a9zu5q css-rrcue5 ").get(0);
        for (Element smallTeasers : subtitlesElements.getElementsByTag("a")) {
            title = smallTeasers.getElementsByTag("h3").text();
            if (title.contains(text)){
                textCount++;
            }
        }
        }catch (IOException e){
            e.printStackTrace();
        }
        return textCount;
    }



    @Override
    public String getLongestArticleTitle() {
        Document article;
        String longestArticleTitle = "";
        int longestArticleLength = 0;
        try {
            for (String site : allArticlesLinks) {
                article = Jsoup.connect(site).get();
                try {
                    Element titleElements = article.getElementsByClass("item-main-content").get(0);
                    String title = titleElements.getElementsByTag("h1").get(0).text();
                    StringBuilder allText = new StringBuilder();
                    for (Element articleBody : article.getElementsByClass("css-onxvt4")) {
                        allText.append(articleBody.text());
                        allText.append(" ");
                    }
                    if (longestArticleLength < allText.length()) {
                        longestArticleLength = allText.length();
                        longestArticleTitle = title;
                    }
                } catch (IndexOutOfBoundsException e) {
                     //e.printStackTrace();
                }


            }
        } catch (IOException e) {
           // e.printStackTrace();
        }

        return longestArticleTitle;


    }
    private void initLinks(){
        try {
            Document wallaWebsite=Jsoup.connect(this.getRootWebsiteUrl()).get();
        for (Element links : wallaWebsite.getElementsByClass("with-roof")) {
                allArticlesLinks.add(links.child(0).attributes().get("href"));

            }
            Element section = wallaWebsite.getElementsByClass("css-1ugpt00 css-a9zu5q css-rrcue5 ").get(0);
            for (Element smallTeasers : section.getElementsByTag("a")) {
                allArticlesLinks.add(smallTeasers.attributes().get("href"));
            }

        }catch (Exception e){
           e.printStackTrace();
        }

    }  private String returnLinkText(String link)  {
        Document article;
        String siteText = "";
        StringBuilder allText = new StringBuilder(siteText);
        try {
            article = Jsoup.connect(link).get();
            Element mainTitle = article.getElementsByClass("item-main-content").get(0);
            allText.append(mainTitle.getElementsByTag("h1").get(0).text());
            allText.append(" ");
            for (Element subTitle : article.getElementsByClass("css-onxvt4")) {
                allText.append(" ");
                allText.append(subTitle.text());
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return allText.toString();
    }


    @Override
    public String correctWords(String siteText) {
        siteText = siteText.replaceAll("[-–•<>@&_%():,.?0-9]", " ");
        siteText = siteText.replaceAll("\"\\s|\\s\"", " ");
        siteText = siteText.replaceAll("\\s+", " ");
        return siteText;
    }

    public ArrayList<String> getAllArticlesLinks() {
        return allArticlesLinks;
    }
}
