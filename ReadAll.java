import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.awt.Desktop;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

public class ReadAll {
    public CountDownLatch startSignal;
    public CountDownLatch doneSignal;

    public ReadAll (Object args[]) {
        try {
            startSignal = new CountDownLatch(1);
            doneSignal = new CountDownLatch(0);
            readWeb(args[0].toString(), args[1].toString(), (Main) args[2]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void readWeb(String url, String type, Main m) {   
        boolean repeat = false;

        String out = "";
        String line = "";

        UrlValidator v = new UrlValidator();
        try {
            ArrayList<String> current = new ArrayList<String>();
            if (url.indexOf("http") == -1) {
                url = "http://" + url;
            }
            if (url.indexOf("/", url.indexOf(".")) == -1) {
                url = url + "/";
            }
            BufferedReader in=new BufferedReader(new InputStreamReader(new URL(url).openStream()));

            //reads each line
            while ((line = in.readLine())!= null) {
                if (line.indexOf("http://") != -1 && line.charAt(line.indexOf("http") - 1) == '\"') {
                    out += line.substring(line.indexOf("http"), 
                        line.indexOf("\"", line.indexOf("http") + 1)); //grabs the url 
                    while (StringUtils.countMatches(out, "http") > 1) {
                        out = out.substring(out.indexOf("http", out.indexOf("http") + 1), out.indexOf("\"", out.indexOf("http") + 1));
                    }

                    if (out.indexOf("w3.org") == -1 && out.indexOf("https") == -1) {
                        for (int x = 0; x < current.size(); x++) {
                            if (out.equals(current.get(x))){ 
                                repeat = true;
                                break;
                            }
                        }
                        if (repeat) {
                            repeat = false;
                        }
                        else {
                            current.add(out);
                        }
                    }
                    out = "";
                }
            }

            for (int x = 0; x < current.size(); x++) {
                String test = current.get(x);
                if (!(v.isValid(current.get(x)))) {
                    current.remove(x);
                    x--;
                    continue;
                }
                System.out.println(current.get(x));
                System.out.println();
            }

            System.out.println("DONE RECEIVING LINKS FOR " + url);
            in.close();

            doneSignal = new CountDownLatch(current.size());

            //for (int x = 0; x < current.size(); x++) {
            //if (current.get(x).indexOf(url) == -1) {
            //current.remove(x);
            //}
            //}

            String nUrl = url.substring(url.indexOf("/", url.indexOf("/") + 1) + 1, url.indexOf("/", url.indexOf(".") + 1));
            Path path = Paths.get(System.getProperty("user.dir") + "/" + nUrl + "/");
            Files.createDirectories(path);
            ArrayList<Thread> listOfThreads = new ArrayList<Thread>();
            for (int x = 0; x < current.size(); x++) {
                MyThread th = new MyThread(current.get(x), type, nUrl + "/", "th" + Integer.toString(x + 1),
                        startSignal, doneSignal);
                th.start();
            }

            startSignal.countDown();
            doneSignal.await();
            System.out.println();
            System.out.println("DONE DOWNLOADING FILES FOR THE ENIRE WEBSITE OF " + url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR FOR: " + line);
        }
    }
}