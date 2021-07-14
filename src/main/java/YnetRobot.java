import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YnetRobot extends BaseRobot implements FixableText {
    private final ArrayList<String> allArticlesLinks = new ArrayList<>();
    public YnetRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
        initLinks();
    }

    @Override
    public Map<String, Integer> getWordsStatistics()  {
        HashMap <String,Integer> wordsMap = new HashMap<>();
        String words;
        String[] wordsSplit;
        Document site;
        try {

            for (String link : allArticlesLinks) {
                site = Jsoup.connect(link).get();
                words = (site.getElementsByClass("mainTitle").text()) + " ";
                words += (site.getElementsByClass("subTitle").text()) + " ";
                words += (site.getElementsByClass("text_editor_paragraph rtl").text());
                words=correctWords(words);
                wordsSplit = words.split(" ");
                for (String word : wordsSplit) {
                    if (wordsMap.containsKey(word)) {
                        wordsMap.put(word, wordsMap.get(word) + 1);
                    } else {
                        wordsMap.put(word, 1);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return wordsMap;

    }

    @Override
    public int countInArticlesTitles(String text) {
     int textCounter=0;
     if (returnAllTitle().toString().contains(text)){
         textCounter++;
     }
     String [] allTitles = returnAllTitle().toString().split(" ");
        for (int i = 0; i < allTitles.length; i++) {
            String currentTitle=allTitles[i];
            if (currentTitle.equals(text)) {
                textCounter++;
            }

     }
        return textCounter;
    }

    @Override
    public String getLongestArticleTitle() {
        Document article;
        String longestArticleTitle = "";
        int longestArticleLength = 0;
        try {
            initLinks();
        for (String link : allArticlesLinks) {
            article = Jsoup.connect(link).get();
            String title = article.getElementsByClass("mainTitle").text();
            StringBuilder articleBody = new StringBuilder();
            for (Element element : article.getElementsByClass("text_editor_paragraph rtl")) {
                articleBody.append(element.getElementsByTag("span").text());
            }
            if (longestArticleLength < articleBody.length()) {
                longestArticleLength = articleBody.length();
                longestArticleTitle = title;
            }
        }
        return longestArticleTitle;
    }catch (IOException e){
            e.printStackTrace();
        }
        return longestArticleTitle;
    }
    private void initLinks() {
        Document site;
        String url;
        try {
        site = Jsoup.connect(this.getRootWebsiteUrl()).get();
        allArticlesLinks.add(site.getElementsByClass("slotTitle").get(0).child(0).attributes().get("href"));
        Element teasers = site.getElementsByClass("YnetMultiStripComponenta oneRow multiRows").get(0);
        for (Element element : teasers.getElementsByClass("mediaItems")) {
            allArticlesLinks.add(element.child(0).child(0).attributes().get("href"));
        }
            for (Element element:site.getElementsByAttributeValue("news/article","rel")) {
                System.out.println(element.text());
            }
            for (Element slotsContent : site.getElementsByClass("MultiArticleRowsManualComponenta").get(0).getElementsByClass("slotsContent")) {
                for (Element slotTitle_medium : slotsContent.getElementsByClass("slotTitle medium")) {
                    url = slotTitle_medium.child(0).attributes().get("href");
                    allArticlesLinks.add(url);
                }
                for (Element slotTitle_small : slotsContent.getElementsByClass("slotTitle small")) {
                    url = slotTitle_small.child(0).attributes().get("href");
                    allArticlesLinks.add(url);
                }
            }

        }catch (Exception e){

        }

    }  private ArrayList <String> returnAllTitle(){
     String allTitles;
     Document site;
     ArrayList<String> wordsOfTitles=new ArrayList<>();
       try {
           for (String link : allArticlesLinks) {
               site = Jsoup.connect(link).get();
               allTitles = (site.getElementsByClass("mainTitle").text()) + " ";
               allTitles += (site.getElementsByClass("subTitle").text()) + " ";
               wordsOfTitles.add(allTitles);

           }
       }catch (IOException e){
           e.printStackTrace();
       }

         return wordsOfTitles;
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



