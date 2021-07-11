import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MakoRobot extends BaseRobot {
   private final ArrayList<String> allArticlesLinks=new ArrayList<>();



    public MakoRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);

    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        Map<String,Integer> makoMap=new HashMap<>();
        int value;
        try {
            initLinks();
            StringBuilder allSiteText = new StringBuilder();
            for (String link : allArticlesLinks) {
                Document article = Jsoup.connect(link).get();
                allSiteText.append(article.getElementsByTag("h1").get(0).text());
                allSiteText.append(" ");
                allSiteText.append(article.getElementsByTag("h2").text());
                Element articleBody = article.getElementsByClass("article-body").get(0);
                for (Element p : articleBody.getElementsByTag("p")) {
                    allSiteText.append(" ");
                    allSiteText.append(p.text());
                }
                String correctText=correctWords(allSiteText.toString());
                String[] fullText=correctText.split(" ");

                for (String word : fullText) {
                    if (makoMap.containsKey(word)) {
                        value = makoMap.get(word) + 1;
                    } else {
                        value = 1;
                    }
                    makoMap.put(word, value);
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return makoMap;
    }

        @Override
    public int countInArticlesTitles(String text) {
        int count = 0;
        try {
            Document mako = Jsoup.connect(getRootWebsiteUrl()).get();
            for (Element spanElements : mako.getElementsByTag("span")) {
                for (Element title : spanElements.getElementsByAttributeValue("data-type", "title")) {
                    if (title.text().contains(text)) {
                        count++;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
            return count;
        }


    @Override
    public String getLongestArticleTitle() {
        return null;
    }

    private void initLinks(){
        String links="";
        String makoWebsiteLink="https://www.mako.co.il/";
        try {
          Document mako=Jsoup.connect(this.getRootWebsiteUrl()).get();
            for (Element link:mako.getElementsByClass("teasers")) {
                for (Element child:link.children()) {
                  links=child.child(0).child(0).attributes().get("href");
                    if (links.contains(makoWebsiteLink)) {
                        allArticlesLinks.add(links);
                    } else {
                        allArticlesLinks.add(makoWebsiteLink + links);
                    }

                }

            }for (Element news : mako.getElementsByClass("neo_ordering scale_image horizontal news")) {
                for (Element h5 : news.getElementsByTag("h5")) {
                    links = h5.child(0).attributes().get("href");
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
    private String correctWords(String siteText) {
        siteText = siteText.replaceAll("[-–•<>@&_%():,.?0-9]", " ");
        siteText = siteText.replaceAll("\"\\s|\\s\"", " ");
        siteText = siteText.replaceAll("\\s+", " ");
        return siteText;
    }
}
