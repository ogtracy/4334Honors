
/**
 * Write a description of class Cluster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import org.jsoup.*;
import org.jsoup.helper.*;
import java.io.File;
import java.io.BufferedReader;
import java.lang.Exception;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.regex.MatchResult;
public class Cluster
{

    final class Rating {
        float quality;
        String av_grade;
        float helpfulness;
        float clarity;
        float easiness;
    }
    /**
     * Constructor for objects of class Cluster
     */
    public Cluster()
    {

    }

    public static void main(String [] args){
        Cluster obj = new Cluster();
        obj.parseFiles();
    }

    void parseFiles(){

        BufferedReader br = null;

        File f = new File("ratemyprofessor"); 
        File[] files = f.listFiles();
        try{
            for (File file : files) {
                //System.out.println(file.getPath());
                //String input = new Scanner(file).useDelimiter("\\A").next();
                Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");

                //Element content = doc.getElementById("content");
                Elements classes = doc.getElementsByClass("rating-breakdown");
                for (Element c : classes) {
                    String text = c.text();
                    System.out.println(text);
                    Scanner s = new Scanner(text);
                    s.findInLine("Overall Quality (\\S+) Average Grade (\\S+) Hotness Helpfulness (\\S+) Clarity (\\S+) Easiness (\\S+)"); 
                    MatchResult result = s.match();
                    Rating rating = new Rating();
                    if (!(result.group(1).equals("N/A"))){
                        rating.quality = Float.parseFloat(result.group(1));
                    } else {
                        rating.quality = -1;
                    }
                    if (!(result.group(2).equals("N/A"))){
                        rating.av_grade = result.group(2);
                    } else {
                        rating.av_grade = "N/A";
                    }
                    if (!(result.group(3).equals("N/A"))){
                        rating.helpfulness = Float.parseFloat(result.group(3));
                    } else {
                        rating.helpfulness = -1;
                    }
                    if (!(result.group(4).equals("N/A"))){
                        rating.clarity = Float.parseFloat(result.group(4));
                    } else {
                        rating.clarity = -1;
                    }
                    if (!(result.group(5).equals("N/A"))){
                        rating.easiness = Float.parseFloat(result.group(5));
                    } else {
                        rating.easiness = -1;
                    }
                    //System.out.println(rating);
                    s.close(); 
                }
            }
        } catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
