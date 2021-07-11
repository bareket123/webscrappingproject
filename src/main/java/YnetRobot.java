import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YnetRobot extends BaseRobot {
    private static final ArrayList<String> allArticlesLinks = new ArrayList<>();
    private static Document site;

    public YnetRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
    }

    @Override
    public Map<String, Integer> getWordsStatistics()  {
        HashMap <String,Integer> map = new HashMap<>();
        String words;
        String[] wordsSplit;
        try {
            initLinks();
            for (String url : allArticlesLinks) {
                site = Jsoup.connect(url).get();
                words = (site.getElementsByClass("mainTitle").text()) + " ";
                words += (site.getElementsByClass("subTitle").text()) + " ";
                words += (site.getElementsByClass("text_editor_paragraph rtl").text());
                wordsSplit = words.split(" ");
                for (String word : wordsSplit) {
                    if (map.containsKey(word)) {
                        map.put(word, map.get(word) + 1);
                    } else {
                        map.put(word, 1);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return map;

    }


    @Override
    public int countInArticlesTitles(String text) {
     int textCounter=0;
     if (returnAllTitle().contains(text)){
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
            for (Element text_editor_paragraph_rtl : article.getElementsByClass("text_editor_paragraph rtl")) {
                articleBody.append(text_editor_paragraph_rtl.getElementsByTag("span").text());
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
/*
    public StringBuilder returnAllTitle() {
        StringBuilder fullText = new StringBuilder();
        try {
            Document ynetWebsite = Jsoup.connect(this.getRootWebsiteUrl()).get();
            Elements allLinksArticle = ynetWebsite.getElementsByAttribute("href");
            for (int i = 0; i < allLinksArticle.size(); i++) {
                Element currentLink = allLinksArticle.get(i);
                String link = currentLink.attr("href");
                if (i != allLinksArticle.size() - 1) {
                    if (link.contains("https://www.ynet.co.il/") && !link.equals(allLinksArticle.get(i + 1).attr("href"))) {
                        Document ynetHomePage = Jsoup.connect(link).get();
                        Elements mainTitle = ynetHomePage.getElementsByClass("mainTitleWrapper");
                        Elements secondaryTitle = ynetHomePage.getElementsByClass("subTitleWrapper");

                        fullText.append(" " + mainTitle.text());
                        fullText.append(" " +secondaryTitle.text());

                }

            }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
         return fullText;
    }

 */
    private void initLinks() throws IOException {
        site = Jsoup.connect(this.getRootWebsiteUrl()).get();
        allArticlesLinks.add(site.getElementsByClass("slotTitle").get(0).child(0).attributes().get("href"));
        Element teasers = site.getElementsByClass("YnetMultiStripComponenta oneRow multiRows").get(0);
        for (Element mediaItems : teasers.getElementsByClass("mediaItems")) {
            allArticlesLinks.add(mediaItems.child(0).child(0).attributes().get("href"));
        }
        Element news = site.getElementsByClass("MultiArticleRowsManualComponenta").get(0);
        for (Element mediaItems : news.getElementsByClass("mediaItems")) {
            allArticlesLinks.add(mediaItems.child(0).child(0).attributes().get("href"));
        }
        for (Element slotTitle_small : news.getElementsByClass("slotTitle small")) {
            allArticlesLinks.add(slotTitle_small.child(0).attributes().get("href"));
        }

    }  private ArrayList <String> returnAllTitle(){
     String allTitles;
     ArrayList<String> wordsOfTitles=new ArrayList<>();
       try {
           initLinks();
           for (String url : allArticlesLinks) {
               site = Jsoup.connect(url).get();
               allTitles = (site.getElementsByClass("mainTitle").text()) + " ";
               allTitles += (site.getElementsByClass("subTitle").text()) + " ";
               wordsOfTitles.add(allTitles);

           }
       }catch (IOException e){
           e.printStackTrace();
       }
         return wordsOfTitles;
    }private String correctWords(String siteText) {
        siteText = siteText.replaceAll("[-–•<>@&_%():,.?0-9]", " ");
        siteText = siteText.replaceAll("\"\\s|\\s\"", " ");
        siteText = siteText.replaceAll("\\s+", " ");
        return siteText;
    }





    }



