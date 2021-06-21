import java.util.Map;

public class WallaRobot extends BaseRobot{
    public WallaRobot(String rootWebsiteUrl) {
        super(rootWebsiteUrl);
    }

    @Override
    public Map<String, Integer> getWordsStatistics() {
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
