
/**
 * Write a description of class Professor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Professor
{
    // instance variables - replace the example below with your own
    private String name;
    private double CDE;
    private double GTM;
    private double SP;
    private double WP;
    private double available;
    private double recommended;
    
    public void setName (String name){
        this.name = name;
    }
    public void setCDE(double CDE){
        this.CDE = CDE;
    }
    public void setGTM(double GTM){
        this.GTM = GTM;
    }
    public void setSP(double SP){
        this.SP =SP;
    }
    public void setWP(double WP){
        this.WP = WP;
    }
    public void setAvailable(double available){
        this.available = available;
    }
    public void setRecommended(double recommended){
        this.recommended = recommended;
    }
    
    public double getCDE(){
        return CDE;
    }
    public double getGTM(){
        return GTM;
    }
    public double getSP(){
        return SP;
    }
    public double getWP(){
        return WP;
    }
    public double getAvailable(){
        return available;
    }
    public double getRecommended(){
        return recommended;
    }
    
    public Point findNearest(Point a, Point b, Point c, Point d, Point e){
        Point nearest = null;
        return nearest;
    }
    
    public double findDistance(Point p){
        double distance = Math.abs(p.getA() - CDE);
        distance += Math.abs(p.getB() - GTM);
        distance += Math.abs(p.getC() - SP);
        distance += Math.abs(p.getD() - WP);
        distance += Math.abs(p.getE() - available);
        distance += Math.abs(p.getF() - recommended);
        return distance;
    }
    
    public Point getPoint(){
        return new Point(CDE, GTM, SP, WP, available, recommended);
    }
    
    public double getLength(){
        return (CDE+GTM+SP+WP+available+recommended)/6;
    }
    
    public Rating getRating(){
        Rating r = new Rating();
        r.setQuality(recommended);
        r.setHelpfulness(available);
        r.setClarity(CDE);
        r.setEasiness(GTM);
        return r;
    }
    
    public boolean isValid(){
        if (CDE > -1 && GTM > -1 && SP > -1 && WP > -1 && available > -1 && recommended > -1){
            return true;
        }
        else {
            return false;
        }
    }
}
