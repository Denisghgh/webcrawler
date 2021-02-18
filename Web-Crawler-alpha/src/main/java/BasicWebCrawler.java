import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

public class BasicWebCrawler {
    private static final int MAX_DEPTH = 2;
    private HashSet<String> links;
    private int i = 0;

    public static void main(String[] args) {
        new BasicWebCrawler().getPageLinks("https://www.bravebird.de/", 0);
    }

    public BasicWebCrawler() {
        links = new HashSet<String>();
    }

    public void start(String URL, int depth){
        getPageLinks(URL, depth);
    }


    public void getPageLinks(String URL, int depth) {
        i++;
        if ((!links.contains(URL) && (depth <= MAX_DEPTH))) {
            System.out.println(">> Depth: " + depth + " [" + URL + "]");
            try {
                links.add(URL);

                Document document = Jsoup.connect(URL).get();
                Elements linksOnPage = document.select("a[href]");

                depth++;
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"), depth);
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
        System.out.println("number of iterations: " + i);
    }
}