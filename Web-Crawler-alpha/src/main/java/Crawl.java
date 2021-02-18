import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Crawl {
    private static int iteration = 0;
    //private HashSet<String> lh = new HashSet<String>();
    private static ArrayList l = new ArrayList();
    private static int MAX_DEPTH = 50000;
    private static boolean detector = false;

    //test function
    public static void test(){
        String site = "https://www.bravebird.de/";
        String url = site;
        int depth = 0;
        new Crawl(site, url, depth);
    }


    public Crawl(String site, String url, int depth){
        crawler(site, url, depth);
        System.out.println("crawled URL´s: " + l.size());

        //output to file
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream("list.txt"), StandardCharsets.UTF_8));
            {

                /*write per iterator
                Iterator <String> list = l.iterator();
                while (list.hasNext()) {
                    String o = list.next();
                    writer.println(o);
                    //writer.flush();
                }*/

                //write per Index
                for (int i = l.size() - 1; i >= 0; i--){
                    writer.println(l.get(i).toString());
                }
                //Important!
                writer.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //recursive crawler
    // Filtering soll unwichige Seiten herausnehmen
    // ungefähre Seitenanzahl: 1512
    // Anzahl Iterationen über nützliche und nicht  nützliche Seiten:20.000

    public static void crawler(String site, String url, int depth){
        if (url.endsWith(".jpg")
                || url.endsWith(".png")
                || url.endsWith("#")
                || url.contains("wp-content")
                || url.endsWith("pdf")){
            //System.out.println("excluded, depth: " + depth + " " + url);
        } else {
            if(url.contains(site)
                    && !l.contains(url)
                    && !url.contains("#")
                    && !url.contains("img_")
                    && !url.contains("page")
                    && !url.contains("tag")
                    && !(url.matches("0-9" + "/"))){
                //add to url list
                l.add(url);
                iteration++;

                //For Analyses
                //System.out.println(">> Depth: " + depth + " [" + url + "] ");

                //stopp in test mode
                if (depth >= MAX_DEPTH){
                    if (detector == false){
                        System.err.println("---------------------> ERROR MAX_DEPTH REACHED");
                        detector = true;
                    }
                    //System.exit(-2);
                }
                else {
                    try {
                        //live analysis - not work recursively
                        //collect analyse = new collect();
                        //analyse.analyse(url, iteration, "data.xls");

                        Document document = Jsoup.connect(url).get();
                        Elements linksOnPage = document.select("a[href]");

                        for (Element page : linksOnPage) {
                            if (page.attr("abs:href").startsWith(site))
                            crawler(site, page.attr("abs:href"), depth++);
                        }
                    }catch (IOException e) {
                        //e.printStackTrace();
                        //System.err.println("For '" + url + "': " + e.getMessage());
                    }
                }
            }
        }
    }
}