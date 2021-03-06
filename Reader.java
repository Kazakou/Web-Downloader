import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.awt.Desktop;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

public class Reader {
    /**
     * Runs the program off of arguments inputted
     */
    public static void main(String args[]) throws Exception {
        readWeb(args[0], args[1], args[2]);
    }

    /**
     * Reads the entire webpage URL and downloads any file of a specified type
     * @param: String url of any website, String type, String output destination
     */
    public static void readWeb(String url, String type, String output) {   
        boolean repeat = false;
        
        String out = "";
        String line = "";
        
        ArrayList<String> current = new ArrayList<String>();
        
        UrlValidator v = new UrlValidator();
        
        try {
            if (url.indexOf("http") == -1) {
                url = "http://" + url;
            }
            if (url.indexOf("/", url.indexOf(".")) == -1) {
                url = url + "/";
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            
            //reads each line
            while ((line = in.readLine())!= null) {
                if (line.indexOf("http://") != -1 && line.indexOf(type) != -1) {
                    if (line.indexOf("\"") != line.length() - 1) {
                        out += line.substring(line.indexOf("\"http") + 1, 
                            line.indexOf("\"", line.indexOf("\"http") + 1) +1);
                        if (out.indexOf("w3.org") == -1 && out.indexOf("https") == -1) {
                            out = out.replace("\"", "");
                            for (int x = 0; x < current.size(); x++) {
                                if (out.equals(current.get(x))) {
                                    repeat = true;
                                    break;
                                }
                            }
                            if (repeat) {
                                repeat = false;
                            } else {
                                current.add(out);
                            }
                        }
                    }
                    out = "";
                }
                else if (line.indexOf("/") != -1 && line.indexOf(type) != -1) {
                    if (line.indexOf("\"") != line.length() - 1) {
                        out += url + line.substring(line.indexOf("/") + 1, line.indexOf("\"", line.indexOf("/") + 1) + 1);
                        if (out.indexOf("w3.org") == -1 && out.indexOf("https") == -1) {
                            out = out.replace("\"", "");
                            for (int x = 0; x < current.size(); x++) {
                                if (out.equals(current.get(x))) {
                                    repeat = true;
                                    break;
                                }
                            }
                            if (repeat) {
                                repeat = false;
                            } else {
                                current.add(out);
                            }
                        }
                    }
                }
            }

            for (int x = 0; x < current.size(); x++) {
                String test = current.get(x);
                if (current.get(x).indexOf(type) == -1 || !(v.isValid(current.get(x)))) {
                    current.remove(x);
                    x--;
                    continue;
                }
                System.out.println(current.get(x));
                System.out.println();
            }
            
            System.out.println("DONE SEARCHING FOR " + url);
            in.close();

            String fileSource = "/" + output + url.substring(url.indexOf("/", url.indexOf("/") + 1) + 1, url.indexOf("/", url.indexOf(".") + 1));

            for (int x = 0; x < current.size(); x++) {
                String fileName = current.get(x).substring(current.get(x).lastIndexOf("/") + 1);
                String directory = System.getProperty("user.dir") + "/" + fileSource + "/";
                Path path = Paths.get(directory);
                Files.createDirectories(path);
                saveFile(current.get(x), directory + fileName);
            }
            
            System.out.println("DONE DOWNLOADING FILES FOR " + url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR FOR: " + line);
            System.out.println("ERROR FOR: " + out);
        }
    }

    /**
     * Saves a specified file from a URL to a place in the desination folder
     * @param: String URL of file, String destionationFile of folder for output
     */
    public static void saveFile(String fileURL, String destinationFile) {
        try {
            URL url = new URL(fileURL);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println("\t FILE NOT FOUND OR MOVED");
        }
    }
}