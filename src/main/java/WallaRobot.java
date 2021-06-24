import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

public class WallaRobot extends BaseRobot{
    public WallaRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        try {
            Document wallaWebsite= Jsoup.connect(this.getRootWebsiteUrl()).get();
            Elements mainArticles = wallaWebsite.getElementsByClass("with-roof ");
            System.out.println("secondary item Found "+ mainArticles.size());
            for(Element element : mainArticles) {
                System.out.println("secondary title:");
                //   Element container=element.parent().parent();
                // String classValue=container.attr("class");
                System.out.println(element.text());

            }

            Elements secondaryTitle = wallaWebsite.getElementsByClass("roof");
            System.out.println("found: " + secondaryTitle.size());
            for (Element element:secondaryTitle) {
                System.out.println("main title:" + element.text());
            }
               Elements title1=wallaWebsite.getElementsByTag("item-main-content");
            System.out.println(title1);
            Elements articleBody=wallaWebsite.getElementsByClass("p");
            System.out.println("secondary title found: "+ articleBody.size());
            System.out.println(articleBody.text());





        }catch (IOException e){
            e.printStackTrace();
        }



        return null;
    }

    @Override
    public int countInArticlesTitles(String text) {
        return 0;
    }

    @Override
    public String getLongestArticleTitle() {
        return null;
    }
}
