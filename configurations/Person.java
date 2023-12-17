package models ;
import java.sql.Date ;
    
public class Person {
    
    private String name ;
    
    private Integer status ;
    
    private Integer personality ;
    
    private Date birthday ;
    
    private double size ;
    

    public void setname(String name){
        this.name = name;
    }
    

    public void setstatus(Integer status){
        this.status = status;
    }
    

    public void setpersonality(Integer personality){
        this.personality = personality;
    }
    

    public void setbirthday(Date birthday){
        this.birthday = birthday;
    }
    

    public void setsize(double size){
        this.size = size;
    }
    

    public String getname(){
        return name;
    }
    

    public Integer getstatus(){
        return status;
    }
    

    public Integer getpersonality(){
        return personality;
    }
    

    public Date getbirthday(){
        return birthday;
    }
    

    public double getsize(){
        return size;
    }
    
}
    