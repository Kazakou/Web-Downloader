import java.net.*;
import java.io.*;

public class OtherTest {

    public static void main(String[] args, String url) throws Exception {
        BufferedReader in=new BufferedReader(new InputStreamReader(new URL(url).openStream()));

        String out="";
        String line="";
        int l = 0; 

        while ((line = in.readLine())!= null)
        {
            out += line;
            out += "\n";
        }
        
        BufferedWriter w = null;
        try 
        {
            w = new BufferedWriter(new FileWriter("test.txt"));
            w.write(out);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        in.close();
        System.out.println(out);
    }
}