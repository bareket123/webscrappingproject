import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

public class MakoRobot extends BaseRobot {


    public MakoRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
        try {
            Document makoWebsite= Jsoup.connect(this.getRootWebsiteUrl()).get();
            Elements mainArticles = makoWebsite.getElementsByAttribute("Article-");
            System.out.println(mainArticles.size());
          /*  System.out.println("Found "+ mainArticles.size());
                System.out.println("main title");
             //   Element container=element.parent().parent();
               // String classValue=container.attr("class");
                System.out.println(element.text());


            }

            Elements secondaryTitle = makoWebsite.getElementsContainingOwnText("article-body");
            for (Element element:secondaryTitle) {
                System.out.println("element:" + element.text());
            }

           */

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
