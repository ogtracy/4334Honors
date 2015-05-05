
/**
 * Write a description of class Rating here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Rating
{
    private double quality;
    private String av_grade;
    private double helpfulness;
    private double clarity;
    private double easiness;
    public Rating(){
        
    }
    public Rating(double quality, double helpfulness, double clarity, double easiness){
        this.quality = quality;
        this.helpfulness = helpfulness;
        this.clarity = clarity;
        this.easiness = easiness;
    }
    
    public double getQuality(){
        return quality;
    }
    public double getHelpfulness(){
        return helpfulness;
    }
    public double getClarity(){
        return clarity;
    }
    public double getEasiness(){
        return easiness;
    }
    
    
    public void setQuality(double quality){
        this.quality = quality;
    }
    public void setHelpfulness(double helpfulness){
        this.helpfulness = helpfulness;
    }
    public void setClarity(double clarity){
        this.clarity = clarity;
    }
    public void setEasiness(double easiness){
        this.easiness = easiness;
    }
    public void setAv_grade(String grade){
        this.av_grade = grade;
    }
    public boolean isValid(){
        if (quality > -1 && helpfulness > -1 && clarity > -1 && easiness > -1){
            return true;
        }
        else {
            return false;
        }
    }
    
    public double findDistance(Rating r){
        double distance = Math.abs(r.quality - quality);
        distance += Math.abs(r.helpfulness - helpfulness);
        distance += Math.abs(r.clarity - clarity);
        distance += Math.abs(r.easiness - easiness);
        return distance;
    }
    
    public boolean valueEquals(Rating r){
        if (quality == r.quality && helpfulness == r.helpfulness && clarity == r.clarity && easiness == r.easiness){
            return true;
        }
        return false;
    }
    
    public String toString(){
        String stringrep = "";
        stringrep += "Quality is " + quality; 
        stringrep += "\nHelpfulness is " + helpfulness;
        stringrep += "\nClarity is " + clarity;
        stringrep += "\nEasiness is " + easiness;
        stringrep += "\nTotal Score is " + (quality + helpfulness + clarity + easiness);
        return stringrep;
    }
}
