import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MakoRobot extends BaseRobot implements FixableText {
   private final ArrayList<String> allArticlesLinks=new ArrayList<>();

    public MakoRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
        initLinks();

    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        Map<String,Integer> wordsMap=new HashMap<>();
        int value;
        try {
            for (String link : allArticlesLinks) {
                String correctText=correctWords(returnAllLinkText(link).toString());
                String[] fullText=correctText.split(" ");
                for (String word : fullText) {
                    if (wordsMap.containsKey(word)) {
                        value = wordsMap.get(word) + 1;
                    } else {
                        value = 1;
                    }
                    wordsMap.put(word, value);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordsMap;
    }

        @Override
    public int countInArticlesTitles(String text) {
        int textCount = 0;
        try {
            Document makoWebsite = Jsoup.connect(getRootWebsiteUrl()).get();
            for (Element spanElements : makoWebsite.getElementsByTag("span")) {
                for (Element title : spanElements.getElementsByAttributeValue("data-type", "title")) {
                    if (title.text().contains(text)) {
                        textCount++;
                    }
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
            for (String link : allArticlesLinks) {
            article = Jsoup.connect(link).get();
            String title = article.getElementsByTag("h1").get(0).text();
            StringBuilder articleText = new StringBuilder();
            Element articleBody = article.getElementsByClass("article-body").get(0);
            for (Element element : articleBody.getElementsByTag("p")) {
                articleText.append(element.text());
            }
            if (longestArticleLength < articleText.length()) {
                longestArticleLength = articleText.length();
                longestArticleTitle = title;
            }
        }
        }catch (IOException e){
            e.printStackTrace();
        }
        return longestArticleTitle;
    }

    private void initLinks(){
        String links="";
        String makoWebsiteLink="https://www.mako.co.il/";
        try {
          Document makoWebsite=Jsoup.connect(this.getRootWebsiteUrl()).get();
            for (Element link:makoWebsite.getElementsByClass("teasers")) {
                for (Element child:link.children()) {
                  links=child.child(0).child(0).attributes().get("href");
                    if (links.contains(makoWebsiteLink)) {
                        allArticlesLinks.add(links);
                    } else {
                        allArticlesLinks.add(makoWebsiteLink + links);
                    }

                }

            }for (Element news : makoWebsite.getElementsByClass("neo_ordering scale_image horizontal news")) {
                for (Element element : news.getElementsByTag("h5")) {
                    links = element.child(0).attributes().get("href");
                    if (links.contains(makoWebsiteLink)) {
                        allArticlesLinks.add(links);
                    } else {
                        allArticlesLinks.add(makoWebsiteLink + links);
                    }
                }
            }




    }catch (IOException e){
            e.printStackTrace();
        }
    }
    public String correctWords(String siteText) {
        siteText = siteText.replaceAll("[-–•<>@&_%():,.?0-9]", " ");
        siteText = siteText.replaceAll("\"\\s|\\s\"", " ");
        siteText = siteText.replaceAll("\\s+", " ");
        return siteText;
    }

    private StringBuilder returnAllLinkText(String link){
        StringBuilder allText = new StringBuilder();
        Document article;
        try {
        article= Jsoup.connect(link).get();
        allText.append(article.getElementsByTag("h1").get(0).text());
        allText.append(" ");
        allText.append(article.getElementsByTag("h2").text());
        Element articleBody = article.getElementsByClass("article-body").get(0);
        for (Element element : articleBody.getElementsByTag("p")) {
            allText.append(" ");
            allText.append(element.text());
        }

    }catch (IOException e){

        }

        return allText;
    }

    public ArrayList<String> getAllArticlesLinks() {
        return allArticlesLinks;
    }
}
