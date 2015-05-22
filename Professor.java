
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
    
    private int CDEcount;
    private int GTMcount;
    private int SPcount;
    private int WPcount;
    private int availableCount;
    
    public Professor(){
        
    }
    
    public Professor(String name, double CDE, double GTM, double SP, double WP, double available){
        String[] nameSplit = name.split("\"");
        this.name = nameSplit[1];
        this.CDE = CDE;
        this.GTM = GTM;
        this.SP = SP;
        this.WP = WP;
        this.available = available;
    }
    
    public void setName (String name){
        String[] nameSplit = name.split("\"");
        this.name = nameSplit[1];
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
    
    public void setCDECount(int CDE){
        this.CDEcount = CDE;
    }
    public void setGTMCount (int GTM){
        this.GTMcount = GTM;
    }
    public void setSPCount(int SP){
        this.SPcount =SP;
    }
    public void setWPCount(int WP){
        this.WPcount = WP;
    }
    public void setAvailableCount(int available){
        this.availableCount = available;
    }
    
    public String getName(){
        return name;
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
    
    public int getCDECount(){
        return CDEcount;
    }
    public int getGTMCount(){
        return GTMcount;
    }
    public int getSPCount(){
        return SPcount;
    }
    public int getWPCount(){
        return WPcount;
    }
    public int getAvailableCount(){
        return availableCount;
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
        return distance;
    }
    
    public Point getPoint(){
        return new Point(CDE, GTM, SP, WP, available);
    }
    
    public double getLength(){
        return (CDE+GTM+SP+WP+available)/5;
    }
    
    public Rating getRating(){
        Rating r = new Rating();
        r.setHelpfulness(available);
        r.setClarity(CDE);
        r.setEasiness(GTM);
        return r;
    }
    
    public boolean isValid(){
        if (CDE > -1 && GTM > -1 && SP > -1 && WP > -1 && available > -1){
            return true;
        }
        else {
            return false;
        }
    }
    
    public void add(Professor p){
        CDE = ((CDE*CDEcount) + (p.getCDE() * p.getCDECount())) / (CDEcount + p.getCDECount());
        CDEcount = CDEcount + p.getCDECount();
        GTM = ((GTM*GTMcount) + (p.getGTM() * p.getGTMCount())) / (GTMcount + p.getGTMCount());
        GTMcount = GTMcount + p.getGTMCount();
        SP = ((SP*SPcount) + (p.getSP() * p.getSPCount())) / (SPcount + p.getSPCount());
        SPcount = SPcount + p.getSPCount();
        WP = ((WP*WPcount) + (p.getWP() * p.getWPCount())) / (WPcount + p.getWPCount());
        WPcount = WPcount + p.getWPCount();
        available = ((available*availableCount) + (p.getAvailable() * p.getAvailableCount())) / (availableCount + p.getAvailableCount());
        availableCount = availableCount + p.getAvailableCount();
    }
    
    public boolean isName(String s){
        String[] nameArray = s.trim().toLowerCase().split(" ");
        int size = nameArray.length;
        String name = nameArray[size-1] +",";
        for (int x=0; x<size-1; x++){
            name +=  nameArray[x];
        }
        if (name.equals(this.name)){
            return true;
        }
        //return name.equals(this.name);
        return false;
    }
}
