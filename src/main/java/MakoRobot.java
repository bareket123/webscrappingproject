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
            Elements mainArticles = makoWebsite.getElementsByClass("headline");
            System.out.println("Found"+ mainArticles.size());
            for(Element element : mainArticles) {
                System.out.println("main title");
                Element container=element.parent().parent();
                String classValue=container.attr("articleSection");
                if (classValue!=null){
                    System.out.println("class  " + classValue);
                }




                System.out.println(element.text());
            }
            System.out.println("article:" );
            Elements secondaryTitle = makoWebsite.getElementsByClass("article Section");

            System.out.println("Found"+ secondaryTitle.size());
            for (Element e:secondaryTitle) {

                System.out.println( e.text());


            }
            System.out.println(secondaryTitle.text());







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
