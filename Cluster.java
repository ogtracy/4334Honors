
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
import java.util.ArrayList;
import java.util.Iterator;
public class Cluster
{
    private ArrayList<Professor> professors;
    private ArrayList<Rating> ratings;
    private ArrayList<Rating> profRatings;
    private ArrayList<Professor> cluster1;
    private ArrayList<Professor> cluster2;
    private ArrayList<Professor> cluster3;
    private ArrayList<Professor> cluster4;
    private ArrayList<Professor> cluster5;
    private ArrayList<Rating> RMPCluster1;
    private ArrayList<Rating> RMPCluster2;
    private ArrayList<Rating> RMPCluster3;
    private ArrayList<Rating> RMPCluster4;
    private ArrayList<Rating> RMPCluster5;
    
    /**
     * Constructor for objects of class Cluster
     */
    public Cluster()
    {
        professors = new ArrayList<Professor>();
        ratings = new ArrayList<Rating>();
        profRatings = new ArrayList<Rating>();
        cluster1 = new ArrayList<Professor>();
        cluster2 = new ArrayList<Professor>();
        cluster3 = new ArrayList<Professor>();
        cluster4 = new ArrayList<Professor>();
        cluster5 = new ArrayList<Professor>();
        RMPCluster1 = new ArrayList<Rating>();
        RMPCluster2 = new ArrayList<Rating>();
        RMPCluster3 = new ArrayList<Rating>();
        RMPCluster4 = new ArrayList<Rating>();
        RMPCluster5 = new ArrayList<Rating>();
    }

    public static void main(String [] args){
        Cluster obj = new Cluster();
        obj.parseRMPFiles();
        obj.parseSFSFiles();
        obj.clusterSFS();
        obj.clusterRMP();
        obj.clusterSFSandRMP();
    }

    //this method is not used 
    void parseRMPFiles(){

        BufferedReader br = null;

        File f = new File("ratemyprofessor"); 
        File[] files = f.listFiles();
        try{
            for (File file : files) {
                String input = new Scanner(file).useDelimiter("\\A").next();
                Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");

                Elements classes = doc.getElementsByClass("rating-breakdown");
                for (Element c : classes) {
                    String text = c.text();
                    Scanner s = new Scanner(text);
                    s.findInLine("Overall Quality (\\S+) Average Grade (\\S+) Hotness Helpfulness (\\S+) Clarity (\\S+) Easiness (\\S+)"); 
                    MatchResult result = s.match();
                    Rating rating = new Rating();
                    if (!(result.group(1).equals("N/A"))){
                        rating.setQuality(Float.parseFloat(result.group(1))) ;
                    } else {
                        rating.setQuality (-1);
                    }
                    if (!(result.group(2).equals("N/A"))){
                        rating.setAv_grade(result.group(2));
                    } else {
                        rating.setAv_grade ("N/A");
                    }
                    if (!(result.group(3).equals("N/A"))){
                        rating.setHelpfulness (Float.parseFloat(result.group(3)));
                    } else {
                        rating.setHelpfulness (-1);
                    }
                    if (!(result.group(4).equals("N/A"))){
                        rating.setClarity (Float.parseFloat(result.group(4)));
                    } else {
                        rating.setClarity (-1);
                    }
                    if (!(result.group(5).equals("N/A"))){
                        rating.setEasiness (Float.parseFloat(result.group(5)));
                    } else {
                        rating.setEasiness (-1);
                    }
                    ratings.add(rating);
                    s.close(); 
                }
            }
            Iterator<Rating> itr = ratings.iterator();
            while(itr.hasNext()) {
                Rating r = itr.next();
                if (!r.isValid()){
                    itr.remove();
                }
            }
        } catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    void parseSFSFiles(){

        BufferedReader br = null;

        File f = new File("SFS"); 
        File[] files = f.listFiles();
        try{
            for (File file : files) {
                Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");
                Element table = doc.select("table").get(1); 
                Elements rows = table.select("tr");

                for (int i = 2; i < rows.size()-2; i+=2) { 
                    Element row = rows.get(i);
                    String text = row.text();
                    Elements cols = row.select("td");
                    Professor p = new Professor();
                    professors.add(p);
                    for (int x =0; x <cols.size(); x++){
                        Element col = cols.get(x);
                        String value = col.text();
                        float fValue =-1;

                        if (x >1){
                            try
                            {
                                fValue = Float.valueOf(value.trim()).floatValue();
                            }
                            catch (NumberFormatException nfe)
                            {
                                System.out.println("NumberFormatException: " + nfe.getMessage());
                            }
                        }

                        switch (x){
                            case 0:
                            p.setName(value);
                            break;

                            case 1:
                            //do nothing. this is a header
                            break;

                            case 2:
                            p.setCDE(fValue);
                            break;

                            case 3:
                            p.setGTM(fValue);
                            break;

                            case 4:
                            p.setSP(fValue);
                            break;

                            case 5:
                            p.setWP(fValue);
                            break;

                            case 6:
                            p.setAvailable(fValue);
                            break;

                            case 7:
                            p.setRecommended(fValue);
                            break;
                        }

                    }
                }
                break;
            }
            
            Iterator<Professor> itr = professors.iterator();
            while(itr.hasNext()) {
                Professor r = itr.next();
                if (!r.isValid()){
                    itr.remove();
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    private void clusterSFSandRMP(){
        for (Rating rat : ratings){
            profRatings.add(rat);
        }
        for (Professor prof : professors){
            profRatings.add(prof.getRating());
        }
        ArrayList<Rating> centroids = new ArrayList<Rating>();
        centroids.add(profRatings.get(0));
        centroids.add(profRatings.get(1));
        centroids.add(profRatings.get(2));
        centroids.add(profRatings.get(3));
        centroids.add(profRatings.get(4));
        boolean change = false;

        do
        {
            RMPCluster1 = new ArrayList<Rating>();
            RMPCluster2 = new ArrayList<Rating>();
            RMPCluster3 = new ArrayList<Rating>();
            RMPCluster4 = new ArrayList<Rating>();
            RMPCluster5 = new ArrayList<Rating>();
            change = false;
            
            for (int x =0; x<profRatings.size(); x++){
                Rating rating = profRatings.get(x);
                Rating nearest = null;
                Rating r = centroids.get(0);
                double leastDistance = rating.findDistance(r);
                nearest = centroids.get(0);
                for (int y =1; y< centroids.size(); y++){
                    r = centroids.get(y);
                    double distance = rating.findDistance(r);
                    if (distance < leastDistance){
                        leastDistance = distance;
                        nearest = r;
                    }
                }

                if (nearest == centroids.get(0)){
                    RMPCluster1.add(rating);
                } else if (nearest == centroids.get(1)){
                    RMPCluster2.add(rating);
                } else if (nearest == centroids.get(2)){
                    RMPCluster3.add(rating);
                } else if (nearest == centroids.get(3)){
                    RMPCluster4.add(rating);
                } else {
                    RMPCluster5.add(rating);
                }
            }
            ArrayList<Rating> newCentroids = computeRMPChange(centroids);
            if (newCentroids != null){
                change = true;
                centroids = newCentroids;
            } else {
                System.out.println("The following are the results for the combined RMP and SFS clustering");
                System.out.println("The first cluster is as follows:");
                System.out.println(centroids.get(0));
                System.out.println("There are " + RMPCluster1.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The second cluster is as follows:");
                System.out.println(centroids.get(1));
                System.out.println("There are " + RMPCluster2.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The third cluster is as follows:");
                System.out.println(centroids.get(2));
                System.out.println("There are " + RMPCluster3.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The fourth cluster is as follows:");
                System.out.println(centroids.get(3));
                System.out.println("There are " + RMPCluster4.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The fifth cluster is as follows:");
                System.out.println(centroids.get(4));
                System.out.println("There are " + RMPCluster5.size() +" professors in this cluster");
                System.out.println();
            }

        }while(change);

    }
    
    private void clusterSFS(){
        //select initial centroids
        ArrayList<Point> centroids = new ArrayList<Point>();
        centroids.add(professors.get(0).getPoint());
        centroids.add(professors.get(1).getPoint());
        centroids.add(professors.get(2).getPoint());
        centroids.add(professors.get(3).getPoint());
        centroids.add(professors.get(4).getPoint());
        boolean change = false;

        do
        {
            cluster1 = new ArrayList<Professor>();
            cluster2 = new ArrayList<Professor>();
            cluster3 = new ArrayList<Professor>();
            cluster4 = new ArrayList<Professor>();
            cluster5 = new ArrayList<Professor>();
            change = false;
            for (int x =0; x<professors.size(); x++){
                Professor prof = professors.get(x);
                Point nearest = null;
                Point p = centroids.get(0);
                double leastDistance = prof.findDistance(p);
                nearest = centroids.get(0);
                for (int y =1; y< centroids.size(); y++){
                    p = centroids.get(y);
                    double distance = prof.findDistance(p);
                    if (distance < leastDistance){
                        leastDistance = distance;
                        nearest = p;
                    }
                }

                if (nearest == centroids.get(0)){
                    cluster1.add(prof);
                } else if (nearest == centroids.get(1)){
                    cluster2.add(prof);
                } else if (nearest == centroids.get(2)){
                    cluster3.add(prof);
                } else if (nearest == centroids.get(3)){
                    cluster4.add(prof);
                } else {
                    cluster5.add(prof);
                }
            }
            ArrayList<Point> newCentroids = computeChange(centroids);
            if (newCentroids != null){
                change = true;
                centroids = newCentroids;
            } else {
                System.out.println("The following are the results for the SFS clustering");
                System.out.println("The first cluster is as follows:");
                System.out.println(centroids.get(0));
                System.out.println("There are " + cluster1.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The second cluster is as follows:");
                System.out.println(centroids.get(1));
                System.out.println("There are " + cluster2.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The third cluster is as follows:");
                System.out.println(centroids.get(2));
                System.out.println("There are " + cluster3.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The fourth cluster is as follows:");
                System.out.println(centroids.get(3));
                System.out.println("There are " + cluster4.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The fifth cluster is as follows:");
                System.out.println(centroids.get(4));
                System.out.println("There are " + cluster5.size() +" professors in this cluster");
                System.out.println();
            }

        }while(change);

    }
    private void clusterRMP(){
        ArrayList<Rating> centroids = new ArrayList<Rating>();
        centroids.add(ratings.get(0));
        centroids.add(ratings.get(1));
        centroids.add(ratings.get(2));
        centroids.add(ratings.get(3));
        centroids.add(ratings.get(4));
        boolean change = false;

        do
        {
            RMPCluster1 = new ArrayList<Rating>();
            RMPCluster2 = new ArrayList<Rating>();
            RMPCluster3 = new ArrayList<Rating>();
            RMPCluster4 = new ArrayList<Rating>();
            RMPCluster5 = new ArrayList<Rating>();
            change = false;
            
            for (int x =0; x<ratings.size(); x++){
                Rating rating = ratings.get(x);
                Rating nearest = null;
                Rating r = centroids.get(0);
                double leastDistance = rating.findDistance(r);
                nearest = centroids.get(0);
                for (int y =1; y< centroids.size(); y++){
                    r = centroids.get(y);
                    double distance = rating.findDistance(r);
                    if (distance < leastDistance){
                        leastDistance = distance;
                        nearest = r;
                    }
                }

                if (nearest == centroids.get(0)){
                    RMPCluster1.add(rating);
                } else if (nearest == centroids.get(1)){
                    RMPCluster2.add(rating);
                } else if (nearest == centroids.get(2)){
                    RMPCluster3.add(rating);
                } else if (nearest == centroids.get(3)){
                    RMPCluster4.add(rating);
                } else {
                    RMPCluster5.add(rating);
                }
            }
            ArrayList<Rating> newCentroids = computeRMPChange(centroids);
            if (newCentroids != null){
                change = true;
                centroids = newCentroids;
            } else {
                System.out.println("The following is the results for the RMP clustering");
                System.out.println("The first cluster is as follows:");
                System.out.println(centroids.get(0));
                System.out.println("There are " + RMPCluster1.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The second cluster is as follows:");
                System.out.println(centroids.get(1));
                System.out.println("There are " + RMPCluster2.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The third cluster is as follows:");
                System.out.println(centroids.get(2));
                System.out.println("There are " + RMPCluster3.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The fourth cluster is as follows:");
                System.out.println(centroids.get(3));
                System.out.println("There are " + RMPCluster4.size() +" professors in this cluster");
                System.out.println();
                
                System.out.println("The fifth cluster is as follows:");
                System.out.println(centroids.get(4));
                System.out.println("There are " + RMPCluster5.size() +" professors in this cluster");
                System.out.println();
            }

        }while(change);

    }   
    
    private ArrayList<Rating> computeRMPChange(ArrayList<Rating> centroids){
        boolean change = false;
        ArrayList<Rating> newCentroids = new ArrayList<Rating>();

        for (int x =0; x< centroids.size(); x++){
            Rating oldCentroid = centroids.get(x);
            Rating newCentroid = calculateRMPCentroid(x);
            if (newCentroid == null){
                newCentroid = oldCentroid;
            }
            if (!newCentroid.valueEquals(oldCentroid)){
                change = true;
                newCentroids.add(newCentroid);
            } else {
                newCentroids.add(oldCentroid);
            }

        }
        if( change){
            return newCentroids;
        } else {
            return null;
        }
    }

    private ArrayList<Point> computeChange(ArrayList<Point> centroids){
        boolean change = false;
        ArrayList<Point> newCentroids = new ArrayList<Point>();

        for (int x =0; x< centroids.size(); x++){
            Point oldCentroid = centroids.get(x);
            Point newCentroid = calculateCentroid(x);
            if (newCentroid == null){
                newCentroid = oldCentroid;
            }
            if (!newCentroid.valueEquals(oldCentroid)){
                change = true;
                newCentroids.add(newCentroid);
            } else {
                newCentroids.add(oldCentroid);
            }

        }
        if( change){
            return newCentroids;
        } else {
            return null;
        }
    }

    private Point calculateCentroid(int index){
        Point newPoint = null;
        ArrayList<Professor> profs = null;
        double sumA =0;
        double sumB =0;
        double sumC =0;
        double sumD =0;
        double sumE =0;
        double sumF =0;
        switch (index){
            case 0:
            profs = cluster1;
            break;
            case 1:
            profs = cluster2;
            break;
            case 2:
            profs = cluster3;
            break;
            case 3:
            profs = cluster4;
            break;
            case 4:
            profs = cluster5;
            break;
        }

        for (int y=0; y <profs.size(); y++){
            Professor p = profs.get(y);
            sumA = sumA+ p.getCDE();
            sumB = sumB + p.getGTM();
            sumC = sumC + p.getSP();
            sumD = sumD + p.getWP();
            sumE = sumE + p.getAvailable();
            sumF = sumF + p.getRecommended();
        }
        if (profs.size() >0){
            double newA = sumA/profs.size(); 
            double newB = sumB/profs.size(); 
            double newC = sumC/profs.size(); 
            double newD = sumD/profs.size(); 
            double newE = sumE/profs.size(); 
            double newF = sumF/profs.size(); 
            newPoint = new Point(newA, newB, newC, newD, newE, newF);
        } else {
            newPoint = null;
        }
        return newPoint;
    }
    
    private Rating calculateRMPCentroid(int index){
        Rating newRating = null;
        ArrayList<Rating> rs = null;
        double sumA =0;
        double sumB =0;
        double sumC =0;
        double sumD =0;
        switch (index){
            case 0:
            rs = RMPCluster1;
            break;
            case 1:
            rs = RMPCluster2;
            break;
            case 2:
            rs = RMPCluster3;
            break;
            case 3:
            rs = RMPCluster4;
            break;
            case 4:
            rs = RMPCluster5;
            break;
        }

        for (int y=0; y <rs.size(); y++){
            Rating p = rs.get(y);
            sumA = sumA + p.getQuality();
            sumB = sumB + p.getHelpfulness();
            sumC = sumC + p.getClarity();
            sumD = sumD + p.getEasiness();
        }
        if (rs.size() >0){
            newRating = new Rating();
            double newA = sumA/rs.size(); 
            newRating.setQuality(newA);
            double newB = sumB/rs.size(); 
            newRating.setHelpfulness(newB);
            double newC = sumC/rs.size(); 
            newRating.setClarity(newC);
            double newD = sumD/rs.size(); 
            newRating.setEasiness(newD);
            
        } else {
            newRating = null;
        }
        return newRating;
    }
}
