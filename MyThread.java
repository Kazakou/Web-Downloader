import java.util.*;
import java.util.concurrent.CountDownLatch;

public class MyThread extends Thread {
    private String url, type, output;
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;

    public MyThread() {
        startSignal = new CountDownLatch(1);
        doneSignal = new CountDownLatch(0);
    }

    public MyThread(String u, String t, String o, String n, CountDownLatch s, CountDownLatch d){
        url = u;
        type = t;
        output = o;
        setName(n);
        startSignal = s;
        doneSignal = d;
    }

    public void run() {
        try {
            startSignal.await();
            read();
            doneSignal.countDown();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void read() {
        try {
            String [] arguments = new String [] {url, type, output};
            Reader.main(arguments);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}