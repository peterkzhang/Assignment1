//below are the libraries used at one point
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYDataset;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;       //for making files
import java.lang.*;     //math functions
import java.util.*;
import static java.lang.Math.*;

public class RLC implements ActionListener {
    private Formatter x;
    //default constructor
    public RLC() {
    }
    //initializing GUI components
    private static JLabel VLabel,CLabel,LLabel,RLabel,endTimeLabel,stepTimeLabel,dataInRangeLabel,locationLabel;
    private static JTextField VInput,CInput,LInput,RInput,endTimeInput,stepTimeInput,locationInput;
    private static JButton confirmButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("RLC Calculator");
        JPanel panel = new JPanel();

        //setting up input frame
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        //setting window placement
        panel.setLayout(null);
        //setting the input labels and textfields to our panel
        VLabel = new JLabel("V value(volts):");
        VLabel.setBounds(10,20,150,25);
        CLabel = new JLabel("C value(farads):");
        CLabel.setBounds(10,50,150,25);
        LLabel = new JLabel("L value(henrys):");
        LLabel.setBounds(10,80,150,25);
        RLabel = new JLabel("R value(ohms):");
        RLabel.setBounds(10,110,150,25);

        endTimeLabel = new JLabel("Endtime value(sec):");
        endTimeLabel.setBounds(10,140,150,25);
        stepTimeLabel = new JLabel("Step time value(sec):");
        stepTimeLabel.setBounds(10,170,150,25);
        locationLabel = new JLabel("log directory: ");
        locationLabel.setBounds(10,200,200,25);

        panel.add(VLabel);
        panel.add(CLabel);
        panel.add(LLabel);
        panel.add(RLabel);
        panel.add(endTimeLabel);
        panel.add(stepTimeLabel);
        panel.add(locationLabel);

        //setting up textfields with example inputs
        VInput= new JTextField("7");
        CInput= new JTextField("0.00000005");
        LInput= new JTextField("0.05");
        RInput= new JTextField("7");
        endTimeInput= new JTextField("1");
        stepTimeInput= new JTextField("0.002");
        locationInput= new JTextField("/home/mza96/Desktop/filename");
        VInput.setBounds(200,20,165,25);
        CInput.setBounds(200,50,165,25);
        LInput.setBounds(200,80,165,25);
        RInput.setBounds(200,110,165,25);
        endTimeInput.setBounds(200,140,165,25);
        stepTimeInput.setBounds(200,170,165,25);
        locationInput.setBounds(200,200,265,25);

        //adding textfield to panel
        panel.add(VInput);
        panel.add(CInput);
        panel.add(LInput);
        panel.add(RInput);
        panel.add(endTimeInput);
        panel.add(stepTimeInput);
        panel.add(locationInput);

        //setting up trigger button
        confirmButton = new JButton("Enter");
        confirmButton.setBounds(10,260,80,25);
        confirmButton.addActionListener(new RLC());
        panel.add(confirmButton);

        dataInRangeLabel= new JLabel("Waiting for input");
        dataInRangeLabel.setBounds(10,230,500,25);
        panel.add(dataInRangeLabel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //getting user input
        double V= Double.parseDouble(VInput.getText());
        double C= Double.parseDouble(CInput.getText());
        double L= Double.parseDouble(LInput.getText());
        double R= Double.parseDouble(RInput.getText());
        double endTime=Double.parseDouble(endTimeInput.getText());
        double stepTime=Double.parseDouble(stepTimeInput.getText());
        String fileLocation_Name=locationInput.getText();
        //check if user input is within range
        if(     (V<4||V>15)||
                (C<0.000000001&&C>0.0000001)||
                (L<0.001||L>0.1)||
                (R<5||R>10)||
                (endTime<0||endTime>500)||
                (stepTime<0||stepTime>endTime)
        ){
            dataInRangeLabel.setText("Data input(s) not in range, please try again");
        }else{
            dataInRangeLabel.setText("Input accepted");
            //calling filer function, filer.java will be uploaded
            //setting up the log file, assume user will put in correct input
            filer logFile=new filer();
            logFile.openFile(fileLocation_Name);
            double currentTime=0.0;
            double q=0.0;
            XYSeries series = new XYSeries("Readings");
            XYSeriesCollection dataset = new XYSeriesCollection(series);
            while(currentTime<endTime){
                //using "uderSqrt and beforeCos to seperate the equation to manage.
                double underSqrt=0.0;
                double beforeCos=1.0;
                underSqrt=(1/(L*C)-pow((R/(2*L)),2));
                beforeCos=(V*C*exp(-(R*currentTime/(2L))));
                q=beforeCos*cos(currentTime*sqrt(underSqrt));
                //adding the data point to log file and plot
                logFile.addLog(currentTime,q);
                series.add(currentTime,q);
                currentTime=currentTime+stepTime;
            }
            logFile.closeFile();
            //setting up plot
            JFrame plotWindow = new JFrame();
            plotWindow.setTitle("RLC plot");
            plotWindow.setSize(800,500);
            plotWindow.setLayout(new BorderLayout());
            plotWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JFreeChart chart = ChartFactory.createXYLineChart("RLC plot","t(sec)","q(t)",dataset);
            plotWindow.add(new ChartPanel(chart), BorderLayout.CENTER);

            plotWindow.setLocationRelativeTo(null);//centre the window
            plotWindow.setVisible(true);

        }

    }
}
