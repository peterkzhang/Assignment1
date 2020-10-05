import java.io.*;
import java.lang.*;
import java.util.*;
public class filer {
    private Formatter x;
    //openFile fucntion will create a file with input param of directory and fileName
    public void openFile(String locationAndName){
        String fileName= locationAndName;
        try{
            x= new Formatter(fileName);
            System.out.println("you created a file with name: "+ fileName);
        }
        //throw excpetion if file is not able to be created
        catch(Exception e){
            System.out.println("DEBUG:UNABLE to create file");
        }
    }
    //adding a line of input into log, separated with a comma
    public void addLog(double t, double q){

        x.format("%s%s%s%s",t,", ",q,"\n");

    }
    //close file for proper storage
    public void closeFile(){
        x.close();
    }
}