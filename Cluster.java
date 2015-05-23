
/**
 * Write a description of class Cluster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import org.jsoup.*;
import org.jsoup.helper.*;
import java.io.*;
import java.lang.Exception;
import java.io.FileNotFoundException;
import java.util.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.regex.MatchResult;

public class Cluster
{
    private ArrayList<Professor> professors;
    private HashMap<String, Professor>professorsMap;
    private ArrayList<Rating> ratings;
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
    
    private ArrayList<Point> SFSCentroids;
    private ArrayList<Rating> RMPCentroids;

    /**
     * Constructor for objects of class Cluster
     */
    public Cluster()
    {
        professors = new ArrayList<Professor>();
        professorsMap = new HashMap<String, Professor>();
        ratings = new ArrayList<Rating>();
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
        
        ArrayList<Point> SFSCentroids = new ArrayList<Point>();
        ArrayList<Rating> RMPCentroids = new ArrayList<Rating>();
    }

    public static void main(String [] args){
        Cluster obj = new Cluster();
        obj.parseRMPFiles();
        obj.parseSFSFiles();
        obj.clusterSFS();
        obj.clusterRMP();
        //obj.writeResults();
        obj.compare();
    }

    //this method is not used 
    void parseRMPFiles(){

        BufferedReader br = null;

        File f = new File("ratemyprofessor"); 
        File[] files = f.listFiles();
        try{
            for (File file : files) {
                String input = new Scanner(file).useDelimiter("\\A").next();
                String name = file.getName();
                String[] nameItems = name.split("at");
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
                    rating.setName(nameItems[0]);
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

        File f = new File("SFStext"); 
        File[] files = f.listFiles();
        String line = null;
        try{
            for (File file : files) {
                br = new BufferedReader(new FileReader(file));
                line = br.readLine();
                while ((line = br.readLine())!= null){ 
                    String[] data = line.split("\t");
                    String name ="";
                    double GTM;
                    double CDE;
                    double available;
                    double WP;
                    double SP;
                    int GTMcount=0;
                    int CDEcount=0;
                    int WPcount=0;
                    int SPcount=0;
                    int availableCount=0;
                    if (data.length <5){
                        continue;
                    }
                    String first = data[0].toLowerCase().trim();
                    String second = data[1].toLowerCase().trim();
                    String[] nameArray =first.split(":");
                    if (nameArray.length>1 && !second.equals("mean")){
                        if (!second.equals("n")){
                            System.out.println("we have a problem");
                        } else {
                            try{
                                CDEcount = Integer.parseInt(data[2].trim());
                                GTMcount = Integer.parseInt(data[3].trim());
                                SPcount = Integer.parseInt(data[4].trim());
                                WPcount = Integer.parseInt(data[5].trim());
                                availableCount = Integer.parseInt(data[6].trim());
                            } catch (Exception e){
                                continue;
                            }
                        }
                        //System.out.println(nameArray[0]);
                        line = br.readLine();
                        data = line.split("\t");
                    } else if (nameArray.length>1 && second.equals("mean")){
                        String nextLine;
                        String[] nextSplit;
                        do {
                            nextLine = br.readLine();
                            nextSplit = nextLine.split("\t");
                        } while (nextSplit.length < 5);
                        String nextSecond = nextSplit[1].toLowerCase().trim();
                        if(!nextSecond.equals("n")){
                            System.out.println("we have a problem");
                        } else {
                            try{
                                CDEcount = Integer.parseInt(nextSplit[2].trim());
                                GTMcount = Integer.parseInt(nextSplit[3].trim());
                                SPcount = Integer.parseInt(nextSplit[4].trim());
                                WPcount = Integer.parseInt(nextSplit[5].trim());
                                availableCount = Integer.parseInt(nextSplit[6].trim());
                            } catch (Exception e){
                                continue;
                            }
                        }

                        // System.out.println(nameArray[0]);
                    } else {
                        continue;
                    }

                    name = nameArray[0];
                    try{
                        CDE = Double.parseDouble(data[2].trim());
                        GTM = Double.parseDouble(data[3].trim());
                        SP = Double.parseDouble(data[4].trim());
                        WP = Double.parseDouble(data[5].trim());
                        available = Double.parseDouble(data[6].trim());
                    } catch (NumberFormatException e){
                        continue;
                    } catch (ArrayIndexOutOfBoundsException e){
                        continue;
                    }
                    Professor p = new Professor(name, CDE, GTM, SP, WP, available);
                    p.setCDECount(CDEcount);
                    p.setGTMCount(GTMcount);
                    p.setSPCount(SPcount);
                    p.setWPCount(WPcount);
                    p.setAvailableCount(availableCount);
                    //System.out.println(p.getPoint().toString());
                    if (professorsMap.containsKey(name)){
                        Professor prof = professorsMap.get(name);
                        prof.add(p);
                    } else {
                        professors.add(p);
                        professorsMap.put(name, p);
                    }
                    
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void clusterSFS(){
        //select initial centroids
        SFSCentroids = new ArrayList<Point>();
        SFSCentroids.add(professors.get(5).getPoint());
        SFSCentroids.add(professors.get(6).getPoint());
        SFSCentroids.add(professors.get(7).getPoint());
        SFSCentroids.add(professors.get(8).getPoint());
        SFSCentroids.add(professors.get(9).getPoint());
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
                Point p = SFSCentroids.get(0);
                double leastDistance = prof.findDistance(p);
                nearest = SFSCentroids.get(0);
                for (int y =1; y< SFSCentroids.size(); y++){
                    p = SFSCentroids.get(y);
                    double distance = prof.findDistance(p);
                    if (distance < leastDistance){
                        leastDistance = distance;
                        nearest = p;
                    }
                }

                if (nearest == SFSCentroids.get(0)){
                    cluster1.add(prof);
                } else if (nearest == SFSCentroids.get(1)){
                    cluster2.add(prof);
                } else if (nearest == SFSCentroids.get(2)){
                    cluster3.add(prof);
                } else if (nearest == SFSCentroids.get(3)){
                    cluster4.add(prof);
                } else {
                    cluster5.add(prof);
                }
            }
            ArrayList<Point> newCentroids = computeChange(SFSCentroids);
            if (newCentroids != null){
                change = true;
                SFSCentroids = newCentroids;
            } else {
                System.out.println("The following are the results for the SFS clustering");
                System.out.println("The first cluster is as follows:");
                System.out.println(SFSCentroids.get(0));
                System.out.println("There are " + cluster1.size() +" professors in this cluster");
                System.out.println();

                System.out.println("The second cluster is as follows:");
                System.out.println(SFSCentroids.get(1));
                System.out.println("There are " + cluster2.size() +" professors in this cluster");
                System.out.println();

                System.out.println("The third cluster is as follows:");
                System.out.println(SFSCentroids.get(2));
                System.out.println("There are " + cluster3.size() +" professors in this cluster");
                System.out.println();

                System.out.println("The fourth cluster is as follows:");
                System.out.println(SFSCentroids.get(3));
                System.out.println("There are " + cluster4.size() +" professors in this cluster");
                System.out.println();

                System.out.println("The fifth cluster is as follows:");
                System.out.println(SFSCentroids.get(4));
                System.out.println("There are " + cluster5.size() +" professors in this cluster");
                System.out.println();
            }

        }while(change);

    }

    private void clusterRMP(){
        RMPCentroids = new ArrayList<Rating>();
        RMPCentroids.add(ratings.get(0));
        RMPCentroids.add(ratings.get(1));
        RMPCentroids.add(ratings.get(2));
        RMPCentroids.add(ratings.get(3));
        RMPCentroids.add(ratings.get(4));
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
                Rating r = RMPCentroids.get(0);
                double leastDistance = rating.findDistance(r);
                nearest = RMPCentroids.get(0);
                for (int y =1; y< RMPCentroids.size(); y++){
                    r = RMPCentroids.get(y);
                    double distance = rating.findDistance(r);
                    if (distance < leastDistance){
                        leastDistance = distance;
                        nearest = r;
                    }
                }

                if (nearest == RMPCentroids.get(0)){
                    RMPCluster1.add(rating);
                } else if (nearest == RMPCentroids.get(1)){
                    RMPCluster2.add(rating);
                } else if (nearest == RMPCentroids.get(2)){
                    RMPCluster3.add(rating);
                } else if (nearest == RMPCentroids.get(3)){
                    RMPCluster4.add(rating);
                } else {
                    RMPCluster5.add(rating);
                }
            }
            ArrayList<Rating> newCentroids = computeRMPChange(RMPCentroids);
            if (newCentroids != null){
                change = true;
                RMPCentroids = newCentroids;
            } else {
                System.out.println("The following is the results for the RMP clustering");
                System.out.println("The first cluster is as follows:");
                System.out.println(RMPCentroids.get(0));
                System.out.println("There are " + RMPCluster1.size() +" professors in this cluster");
                System.out.println();

                System.out.println("The second cluster is as follows:");
                System.out.println(RMPCentroids.get(1));
                System.out.println("There are " + RMPCluster2.size() +" professors in this cluster");
                System.out.println();

                System.out.println("The third cluster is as follows:");
                System.out.println(RMPCentroids.get(2));
                System.out.println("There are " + RMPCluster3.size() +" professors in this cluster");
                System.out.println();

                System.out.println("The fourth cluster is as follows:");
                System.out.println(RMPCentroids.get(3));
                System.out.println("There are " + RMPCluster4.size() +" professors in this cluster");
                System.out.println();

                System.out.println("The fifth cluster is as follows:");
                System.out.println(RMPCentroids.get(4));
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
            sumA = sumA + p.getCDE();
            sumB = sumB + p.getGTM();
            sumC = sumC + p.getSP();
            sumD = sumD + p.getWP();
            sumE = sumE + p.getAvailable();
        }
        if (profs.size() >0){
            double newA = sumA/profs.size(); 
            double newB = sumB/profs.size(); 
            double newC = sumC/profs.size(); 
            double newD = sumD/profs.size(); 
            double newE = sumE/profs.size(); 
            newPoint = new Point(newA, newB, newC, newD, newE);
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
    
    private void writeResults(){
        String SFSFile = "SFSResults.csv";
        String RMPFile = "RMPResults.csv";
        FileWriter fileWriter1 = null;
        FileWriter fileWriter2 = null;
        
        try{
            fileWriter1 = new FileWriter(SFSFile);
            fileWriter1.append("CDE,GTM,SP,WP,available\n");
            for (Professor p: professors){
                String toWrite =p.getCDE() +","+ p.getGTM()+","+ p.getSP()+","+ p.getWP()+"," + p.getAvailable()+"\n";
                fileWriter1.append(toWrite);
            }
            
            fileWriter2 = new FileWriter(RMPFile);
            fileWriter2.append("Quality,Helpfulness,Clarity,Easiness\n");
            int count=1;
            for (Rating r: ratings){
                String toWrite = r.getQuality()+","+r.getHelpfulness()+","+r.getClarity()+","+r.getEasiness()+"\n";
                fileWriter2.append(toWrite);
                count++;
            }
            
        } catch (FileNotFoundException e){
            System.out.println("Error" + e.getMessage());
        } catch (IOException e){
            System.out.println("Error" + e.getMessage());
        } finally {
            try {
                fileWriter1.flush();
                fileWriter1.close();
                fileWriter2.flush();
                fileWriter2.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }
    
    private ArrayList<Point> arrangeSFSCentroids(){
        double maxSize =0;
        Point largestSFS = null;
        Point secondLargestSFS = null;
        Point third = null;
        Point fourth = null;
        Point fifth = null;
        
        
        //get largest centroids
        ArrayList<Point> centroids = SFSCentroids;
        for (Point p: centroids){
            double size = p.getLength();
            if (size > maxSize){
                maxSize = size;
                largestSFS = p;
            }
        }
        maxSize =0;
        for (Point p: centroids){
            double size = p.getLength();
            if( size <largestSFS.getLength() && size > maxSize){
                maxSize = size;
                secondLargestSFS = p;
            }
        }
        maxSize =0;
        for (Point p: centroids){
            double size = p.getLength();
            if (size < secondLargestSFS.getLength() && size > maxSize){
                maxSize = size;
                third = p;
            }
        }
        maxSize =0;
        for (Point p: centroids){
            double size = p.getLength();
            if( size <third.getLength() && size > maxSize){
                maxSize = size;
                fourth = p;
            }
        }
        maxSize =0;
        for (Point p: centroids){
            double size = p.getLength();
            if (size < fourth.getLength() && size > maxSize){
                maxSize = size;
                fifth = p;
            }
        }
        
        ArrayList<Point> sortedCentroids = new ArrayList<Point>();
        sortedCentroids.add(0, largestSFS);
        sortedCentroids.add(1, secondLargestSFS);
        sortedCentroids.add(2, third);
        sortedCentroids.add(3, fourth);
        sortedCentroids.add(4, fifth);
        return sortedCentroids;
    }
    
    private ArrayList<Rating> arrangeRMPCentroids(){
        Rating largestRMP = null;
        Rating secondLargestRMP = null;
        Rating third = null;
        Rating fourth = null;
        Rating fifth = null;
        double maxSize =0;
        ArrayList<Rating> centroids = RMPCentroids;
        for (Rating r: centroids){
            double size = r.getLength();
            if (size > maxSize){
                maxSize = size;
                largestRMP = r;
            }
        }
        maxSize =0;
        for (Rating r: centroids){
            double size = r.getLength();
            if( size <largestRMP.getLength() && size > maxSize){
                maxSize = size;
                secondLargestRMP = r;
            }
        }
        maxSize =0;
        for (Rating r: centroids){
            double size = r.getLength();
            if( size <secondLargestRMP.getLength() && size > maxSize){
                maxSize = size;
                third = r;
            }
        }
        maxSize =0;
        for (Rating r: centroids){
            double size = r.getLength();
            if( size <third.getLength() && size > maxSize){
                maxSize = size;
                fourth = r;
            }
        }
        maxSize =0;
        for (Rating r: centroids){
            double size = r.getLength();
            if( size <fourth.getLength() && size > maxSize){
                maxSize = size;
                fifth = r;
            }
        }
        
        ArrayList<Rating> sortedCentroids = new ArrayList<Rating>();
        sortedCentroids.add(0, largestRMP);
        sortedCentroids.add(1, secondLargestRMP);
        sortedCentroids.add(2, third);
        sortedCentroids.add(3, fourth);
        sortedCentroids.add(4, fifth);
        return sortedCentroids;
    }
    
    private double getSFSAverage(ArrayList<Professor> array){
        double sumA =0;
        double sumB =0;
        double sumC =0;
        double sumD =0;
        double sumE =0;
        int size = array.size();
        for (Professor p: array){
            sumA += p.getGTM();
            sumB += p.getCDE();
            sumC += p.getSP();
            sumD += p.getWP();
            sumE += p.getAvailable();
        }
        double average = (sumA/size + sumB/size + sumC/size + sumD/size +sumE/size)/5;
        return average;
    }
    
    private double getRMPAverage(ArrayList<Rating> array){
        double average =0;
        double sumQ =0;
        double sumH =0;
        double sumC =0;
        double sumE =0;
        int size = array.size();
        
        for (Rating r: array){
            sumQ += r.getQuality();
            sumH += r.getHelpfulness();
            sumC += r.getClarity();
            sumE += r.getEasiness();
        }
        
        average = (sumQ/size + sumH/size + sumC/size + sumE/size)/4;
        return average;
    }
    
    private void compare(){
        ArrayList<Point> SFSCentroids = arrangeSFSCentroids();
        ArrayList<Professor> highest = null;
        ArrayList<Professor> nextHighest = null;
        ArrayList<Professor> thirdHighest = null;
        ArrayList<Professor> fourthHighest = null;
        ArrayList<Professor> lastHighest = null;
        
        ArrayList<Rating> RMPCentroids = arrangeRMPCentroids();
        ArrayList<Rating> RMPHighest = null;
        ArrayList<Rating> RMPNextHighest = null;
        ArrayList<Rating> RMPThirdHighest = null;
        ArrayList<Rating> RMPFourthHighest = null;
        ArrayList<Rating> RMPLastHighest = null;
        int total =0;
        int present =0;
        
        double first = getSFSAverage(cluster1);
        if (first == SFSCentroids.get(0).getLength()){
            highest = cluster1;
        } else if (first == SFSCentroids.get(1).getLength() ){
            nextHighest = cluster1;
        } else if (first == SFSCentroids.get(2).getLength()){
            thirdHighest = cluster1;
        } else if (first == SFSCentroids.get(3).getLength()){
            fourthHighest = cluster1;
        } else {
            lastHighest = cluster1;
        }
        
        double second = getSFSAverage(cluster2);
        if (second == SFSCentroids.get(0).getLength()){
            highest = cluster2;
        } else if (second == SFSCentroids.get(1).getLength() ){
            nextHighest = cluster2;
        } else if (second == SFSCentroids.get(2).getLength()){
            thirdHighest = cluster2;
        } else if (second == SFSCentroids.get(3).getLength()){
            fourthHighest = cluster2;
        } else {
            lastHighest = cluster2;
        }
        
        double third = getSFSAverage(cluster3);
        if (third == SFSCentroids.get(0).getLength()){
            highest = cluster3;
        } else if (third == SFSCentroids.get(1).getLength() ){
            nextHighest = cluster3;
        } else if (third == SFSCentroids.get(2).getLength()){
            thirdHighest = cluster3;
        } else if (third == SFSCentroids.get(3).getLength()){
            fourthHighest = cluster3;
        } else {
            lastHighest = cluster3;
        }
        
        double fourth = getSFSAverage(cluster4);
        if (fourth == SFSCentroids.get(0).getLength()){
            highest = cluster4;
        } else if (fourth == SFSCentroids.get(1).getLength() ){
            nextHighest = cluster4;
        }  else if (fourth == SFSCentroids.get(2).getLength()){
            thirdHighest = cluster4;
        } else if (fourth == SFSCentroids.get(3).getLength()){
            fourthHighest = cluster4;
        } else {
            lastHighest = cluster4;
        }
        
        double fifth = getSFSAverage(cluster5);
        if (fifth == SFSCentroids.get(0).getLength()){
            highest = cluster5;
        } else if (fifth == SFSCentroids.get(1).getLength() ){
            nextHighest = cluster5;
        }  else if (fifth == SFSCentroids.get(2).getLength()){
            thirdHighest = cluster5;
        } else if (fifth == SFSCentroids.get(3).getLength()){
            fourthHighest = cluster5;
        } else {
            lastHighest = cluster5;
        }
        
        double firstRMP = getRMPAverage(RMPCluster1);
        if (firstRMP == RMPCentroids.get(0).getLength()){
            RMPHighest = RMPCluster1;
        } else if (firstRMP == RMPCentroids.get(1).getLength() ){
            RMPNextHighest = RMPCluster1;
        } else if (firstRMP == RMPCentroids.get(2).getLength() ){
            RMPThirdHighest = RMPCluster1;
        } else if (firstRMP == RMPCentroids.get(3).getLength() ){
            RMPFourthHighest = RMPCluster1;
        } else {
            RMPLastHighest = RMPCluster1;
        }
        
        double secondRMP = getRMPAverage(RMPCluster2);
        if (secondRMP == RMPCentroids.get(0).getLength()){
            RMPHighest = RMPCluster2;
        } else if (secondRMP == RMPCentroids.get(1).getLength() ){
            RMPNextHighest = RMPCluster2;
        } else if (secondRMP == RMPCentroids.get(2).getLength() ){
            RMPThirdHighest = RMPCluster2;
        } else if (secondRMP == RMPCentroids.get(3).getLength() ){
            RMPFourthHighest = RMPCluster2;
        } else {
            RMPLastHighest = RMPCluster2;
        }
        
        double thirdRMP = getRMPAverage(RMPCluster3);
        if (thirdRMP == RMPCentroids.get(0).getLength()){
            RMPHighest = RMPCluster3;
        } else if (thirdRMP == RMPCentroids.get(1).getLength() ){
            RMPNextHighest = RMPCluster3;
        }  else if (thirdRMP == RMPCentroids.get(2).getLength() ){
            RMPThirdHighest = RMPCluster3;
        } else if (thirdRMP == RMPCentroids.get(3).getLength() ){
            RMPFourthHighest = RMPCluster3;
        } else {
            RMPLastHighest = RMPCluster3;
        }
        
        double fourthRMP = getRMPAverage(RMPCluster4);
        if (fourthRMP == RMPCentroids.get(0).getLength()){
            RMPHighest = RMPCluster4;
        } else if (fourthRMP == RMPCentroids.get(1).getLength() ){
            RMPNextHighest = RMPCluster4;
        }  else if (fourthRMP == RMPCentroids.get(2).getLength() ){
            RMPThirdHighest = RMPCluster4;
        } else if (fourthRMP == RMPCentroids.get(3).getLength() ){
            RMPFourthHighest = RMPCluster4;
        } else {
            RMPLastHighest = RMPCluster4;
        }
        
        double fifthRMP = getRMPAverage(RMPCluster5);
        if (fifthRMP == RMPCentroids.get(0).getLength()){
            RMPHighest = RMPCluster5;
        } else if (fifthRMP == RMPCentroids.get(1).getLength() ){
            RMPNextHighest = RMPCluster5;
        }  else if (fifthRMP == RMPCentroids.get(2).getLength() ){
            RMPThirdHighest = RMPCluster5;
        } else if (fifthRMP == RMPCentroids.get(3).getLength() ){
            RMPFourthHighest = RMPCluster5;
        } else {
            RMPLastHighest = RMPCluster5;
        }
        
        for (Rating r: RMPHighest){
            for (Professor p: professors){
                if (p.isName(r.getName())){
                    total++;
                    break;
                }
            }
        }
        for (Rating r: RMPHighest){
            for (Professor p: highest){
                if (p.isName(r.getName())){
                    present++;
                    break;
                }
            }
            for (Professor p: nextHighest){
                if (p.isName(r.getName())){
                    present++;
                    break;
                }
            }
        }
        System.out.println("Of the " + total + " professors in the highest RMP cluster (who are also in the SFS dataset), " + present + " are present in the top two SFS clusters.");
        
        int inBoth =0;
        int count =0;
        for (Professor p: highest){
            for (Rating r:ratings){
                if (p.isName(r.getName())){
                    inBoth++;
                    break;
                }
            }
        }
        for (Professor p: highest){
            for (Rating r:RMPHighest){
                if (p.isName(r.getName())){
                    count++;
                }
            }
            for (Rating r:RMPNextHighest){
                if (p.isName(r.getName())){
                    count++;
                }
            }
        }
        
        System.out.println("Of the " + inBoth + " professors in the highest SFS cluster (who are also present in the RMP dataset), " + count + " are present in the top two RMP clusters.");
        
        
        total =0;
        present=0;
        for (Rating r: RMPLastHighest){
            for (Professor p: professors){
                if (p.isName(r.getName())){
                    total++;
                    break;
                }
            }
        }
        for (Rating r: RMPLastHighest){
            for (Professor p: lastHighest){
                if (p.isName(r.getName())){
                    present++;
                    break;
                }
            }
            for (Professor p: fourthHighest){
                if (p.isName(r.getName())){
                    present++;
                    break;
                }
            }
        }
        System.out.println("Of the " + total + " professors in the lowest RMP cluster (who are also in the SFS dataset), " + present + " are present in the lowest two SFS clusters.");
        
        inBoth =0;
        count =0;
        for (Professor p: lastHighest){
            for (Rating r:ratings){
                if (p.isName(r.getName())){
                    inBoth++;
                    break;
                }
            }
        }
        
        for (Professor p: lastHighest){
            for (Rating r:RMPLastHighest){
                if (p.isName(r.getName())){
                    count++;
                }
            }
            for (Rating r:RMPFourthHighest){
                if (p.isName(r.getName())){
                    count++;
                }
            }
        }
        
        System.out.println("Of the " + inBoth + " professors in the lowest SFS cluster (who are also present in the RMP dataset), " + count + " is/are present in the lowest two RMP clusters.");
    }
    
}