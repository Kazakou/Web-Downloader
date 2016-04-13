import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class GatherURLs {

    public static void main(String[] args) throws Exception 
    {
        readWeb(args[0]);
    }

    public static void readWeb(String url)
    {   
        try
        {
            ArrayList<String> current = new ArrayList<String>();
            BufferedReader in=new BufferedReader(new InputStreamReader(new URL(url).openStream()));

            String out="";
            String line="";
            int o = 0;

            while ((line = in.readLine())!= null)
            {
                if (line.indexOf("http://") != -1)
                {
                    if (line.indexOf("\"") != line.length() - 1)
                    {
                        out += line.substring(line.indexOf("\"http") + 1, 
                            line.indexOf("\"", line.indexOf("\"http") + 1) +1);
                        if (out.indexOf("w3.org") == -1 && out.indexOf("https") == -1)
                        {
                            out = out.replace("\"", "");
                            for (int x = 0; x < current.size(); x++)
                            {
                                if (out.equals(current.get(x)))
                                {
                                    o = 1;
                                    break;
                                }
                            }
                            if (o == 0)
                            {
                                current.add(out);
                                out += "\n";
                                System.out.println(out);
                            }
                            else
                            {
                                o = 0;
                            }
                        }
                    }
                    out = "";
                }
            }

            in.close();

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}