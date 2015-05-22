
/**
 * Write a description of class Point here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Point
{
    double a;
    double b;
    double c;
    double d;
    double e;
    public Point(double a, double b, double c, double d, double e){
        this.a =a;
        this.b = b;
        this.c = c;
        this.e = e;
        this.d = d;
    }
    public double getA(){
        return a;
    }
    public double getB(){
        return b;
    }
    public double getC(){
        return c;
    }
    public double getD(){
        return d;
    }
    public double getE(){
        return e;
    }
    
    public boolean valueEquals(Point p){
        if (a == p.getA() && b == p.getB() && c == p.getC() && d == p.getD() && e == p.getE() ){
            return true;
        }
        return false;
    }
    
    public double getLength(){
        return (a+b+c+d+e)/5;
    }
    
    public String toString(){
        String stringrep = "";
        stringrep += "CDE is " + a; 
        stringrep += "\nGTM is " + b;
        stringrep += "\nSP is " + c;
        stringrep += "\nWP is " + d;
        stringrep += "\navailable is " + e;
        //stringrep += "\nTotal Score is " + (a+b+c+d+e+f);
        return stringrep;
    }
}
