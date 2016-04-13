import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.Desktop;

public class GrabPics {

    public static void main(String[] args, String url, int p) throws Exception 
    {
        readWeb(url, p);
    }

    public static void readWeb(String url, int p)
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
                if (line.indexOf("http://") != -1 && line.indexOf(".jpg") != -1)
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

            if (p < current.size())
            {
                saveImage(current.get(p), "image.jpg");
            }
            else
            {
                saveImage(current.get(current.size() - 1), "image.jpg");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }
}