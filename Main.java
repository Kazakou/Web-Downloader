import java.util.concurrent.CountDownLatch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    //buttons
    public JButton btnPage;
    public JButton btnSite;

    //textfields
    public JTextField txtURL;
    public JTextField txtFILE;

    //labels
    public JLabel lblInfo;
    public JLabel lblURL;
    public JLabel lblType;

    //coordinates
    public Insets insets;

    //stuff
    private boolean threadsAlive = true;

    public Main(int width, int height) {
        //create frame
        setTitle("HTML Reader!");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //create panel
        Container pane = getContentPane();
        insets = pane.getInsets();

        //set a layout. NOT A GRID
        pane.setLayout(null);

        //buttons
        btnPage = new JButton("Single Page");
        btnSite = new JButton("Entire Website");

        //add button actions
        btnPage.addActionListener(new Page());
        btnSite.addActionListener(new Site());

        //label
        lblInfo = new JLabel("<html>This program will gather and download files from a website."
            +" Simply input a url and a filetype below."+"</html>");
        lblURL = new JLabel("URL");
        lblType = new JLabel("Type");

        //textfields
        txtURL = new JTextField();
        txtFILE = new JTextField();
        txtURL.addMouseListener(new TextClicked1());
        txtFILE.addMouseListener(new TextClicked2());

        //add action if window is close
        this.addWindowListener(new Close());

        //make bounds
        makeBounds(width, height);

        //add components
        pane.add(btnPage);
        pane.add(btnSite);
        pane.add(lblInfo);
        pane.add(lblURL);
        pane.add(lblType);
        pane.add(txtURL);
        pane.add(txtFILE);

        //JFrame finalization stuff
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public void makeBounds(int cW, int cH) {
        //setBounds(x, y, width, height)
        txtURL.setBounds(55, 100, 200, 30);
        txtFILE.setBounds(55, 170, 200, 30);
        btnPage.setBounds(55, 220, 200, 30);
        btnSite.setBounds(55, 270, 200, 30);
        lblInfo.setBounds(insets.left + 5, insets.top + 5, cW, lblInfo.getPreferredSize().height * 2);
        lblURL.setBounds(55, 80, lblURL.getPreferredSize().width, lblURL.getPreferredSize().height);
        lblType.setBounds(55, 150, lblType.getPreferredSize().width, lblType.getPreferredSize().height);
    }

    public class Close extends WindowAdapter {
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            String ask = "Are you sure to close this program?";
            String title = "Program Shutdown";
            if (JOptionPane.showConfirmDialog(Main.this, ask, title, JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                setVisible(false);
                dispose();
            }
        } 
    }

    public class TextClicked1 extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            txtURL.setText("");
        }
    }

    public class TextClicked2 extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            txtFILE.setText("");
        }
    }

    public class Page implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                CountDownLatch startSignal = new CountDownLatch(1);
                CountDownLatch doneSignal = new CountDownLatch(1);
                MyThread th = new MyThread(txtURL.getText(), txtFILE.getText(), "", "th1", 
                        startSignal, doneSignal);
                th.start();
                startSignal.countDown();
                System.out.println();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public class Site implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Object [] arguments = new Object [] {txtURL.getText(), txtFILE.getText(), Main.this};
                ReadAll r = new ReadAll(arguments);
                System.out.println();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}