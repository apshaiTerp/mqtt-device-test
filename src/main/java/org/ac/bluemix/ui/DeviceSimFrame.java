package org.ac.bluemix.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.ac.bluemix.DeviceThread;

import net.miginfocom.swing.MigLayout;

/**
 * @author ac010168
 *
 */
public class DeviceSimFrame extends JFrame implements ActionListener {

  /** Because Reasons */
  private static final long serialVersionUID = 2000700091506950286L;
  
  public static final DecimalFormat df = new DecimalFormat("###.#");
  
  //UI Components
  private String deviceChoice;
  private String patientID;
  
  private JTextField pulseField;
  private JButton pulseUpButton, pulseDownButton;

  private JTextField systolicField;
  private JButton systolicUpButton, systolicDownButton;
  private JTextField diastolicField;
  private JButton diastolicUpButton, diastolicDownButton;

  private JTextField tempField;
  private JButton tempUpButton, tempDownButton;
  
  private JTextField o2SatField;
  private JButton o2SatUpButton, o2SatDownButton;
  
  private JButton sendButton;
  
  private JTextArea debugArea;
  
  //Data components
  private int pulseValue    = 90;
  private int systolicBP    = 110;
  private int diastolicBP   = 70;
  private double tempValue  = 98.4;
  private double o2SatValue = 100.0;
  
  private DeviceThread deviceThread;

  public DeviceSimFrame(String deviceChoice, String patientID) {
    super("Device Simulator - " + deviceChoice);
    this.deviceChoice = deviceChoice;
    this.patientID    = patientID;
    
    setLayout(new BorderLayout(10, 10));
    
    JPanel mainPanel = new JPanel();
    MigLayout layout = new MigLayout("wrap 4");
    mainPanel.setLayout(layout);
    
    JLabel titleLabel = new JLabel("Simulation for Patient " + patientID, JLabel.CENTER);
    mainPanel.add(titleLabel, "span");
    
    mainPanel.add(new JLabel("--------------------------------------------------", JLabel.CENTER), "span");
    
    JLabel pulseLabel = new JLabel("Heartrate Value: ", JLabel.RIGHT);
    pulseField = new JTextField("90");
    pulseDownButton = new JButton("-");
    pulseUpButton   = new JButton("+");
    
    mainPanel.add(pulseLabel, "width 140:140:160");
    mainPanel.add(pulseField, "width 140:140:160");
    mainPanel.add(pulseDownButton, "width 40:40:40");
    mainPanel.add(pulseUpButton, "width 40:40:40");

    mainPanel.add(new JLabel("--------------------------------------------------", JLabel.CENTER), "span");

    JLabel systolicLabel = new JLabel("Systolic BP: ", JLabel.RIGHT);
    systolicField = new JTextField("110");
    systolicDownButton = new JButton("-");
    systolicUpButton   = new JButton("+");
    
    mainPanel.add(systolicLabel, "width 140:140:160");
    mainPanel.add(systolicField, "width 140:140:160");
    mainPanel.add(systolicDownButton, "width 40:40:40");
    mainPanel.add(systolicUpButton, "width 40:40:40");

    JLabel diastolicLabel = new JLabel("Diastolic BP: ", JLabel.RIGHT);
    diastolicField = new JTextField("70");
    diastolicDownButton = new JButton("-");
    diastolicUpButton   = new JButton("+");
    
    mainPanel.add(diastolicLabel, "width 140:140:160");
    mainPanel.add(diastolicField, "width 140:140:160");
    mainPanel.add(diastolicDownButton, "width 40:40:40");
    mainPanel.add(diastolicUpButton, "width 40:40:40");

    mainPanel.add(new JLabel("--------------------------------------------------", JLabel.CENTER), "span");

    char c = '\u00B0';
    JLabel tempLabel = new JLabel("Temperature (" + c + "F): ", JLabel.RIGHT);
    tempField = new JTextField("98.4");
    tempDownButton = new JButton("-");
    tempUpButton   = new JButton("+");
    
    mainPanel.add(tempLabel, "width 140:140:160");
    mainPanel.add(tempField, "width 140:140:160");
    mainPanel.add(tempDownButton, "width 40:40:40");
    mainPanel.add(tempUpButton, "width 40:40:40");

    mainPanel.add(new JLabel("--------------------------------------------------", JLabel.CENTER), "span");

    JLabel o2SatLabel = new JLabel("O2 Saturation Levels: ", JLabel.RIGHT);
    o2SatField = new JTextField("100.0");
    o2SatDownButton = new JButton("-");
    o2SatUpButton   = new JButton("+");
    
    mainPanel.add(o2SatLabel, "width 140:140:160");
    mainPanel.add(o2SatField, "width 140:140:160");
    mainPanel.add(o2SatDownButton, "width 40:40:40");
    mainPanel.add(o2SatUpButton, "width 40:40:40");

    mainPanel.add(new JLabel("--------------------------------------------------", JLabel.CENTER), "span");
    
    sendButton = new JButton("Begin Sending Data");
    
    mainPanel.add(new JLabel(""));
    mainPanel.add(sendButton, "wrap");
    
    mainPanel.add(new JLabel("--------------------------------------------------", JLabel.CENTER), "span");
    mainPanel.add(new JLabel("Message Data (Debug):"), "span");
    
    debugArea = new JTextArea("");
    debugArea.setPreferredSize(new Dimension(420,70));
    debugArea.setLineWrap(true);
    mainPanel.add(debugArea, "span");
    
    pulseUpButton.addActionListener(this);
    pulseDownButton.addActionListener(this);
    systolicUpButton.addActionListener(this);
    systolicDownButton.addActionListener(this);
    diastolicUpButton.addActionListener(this);
    diastolicDownButton.addActionListener(this);
    tempUpButton.addActionListener(this);
    tempDownButton.addActionListener(this);
    o2SatUpButton.addActionListener(this);
    o2SatDownButton.addActionListener(this);
    sendButton.addActionListener(this);
    
    pulseField.addActionListener(this);
    systolicField.addActionListener(this);
    diastolicField.addActionListener(this);
    tempField.addActionListener(this);
    o2SatField.addActionListener(this);

    getContentPane().add(mainPanel, BorderLayout.CENTER);
    getContentPane().add(new JLabel(""), BorderLayout.EAST);
    getContentPane().add(new JLabel(""), BorderLayout.WEST);
    getContentPane().add(new JLabel(""), BorderLayout.SOUTH);
    
    setSize(460,490);
    setResizable(false);
    setVisible(true);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    
    deviceThread = new DeviceThread(deviceChoice, patientID, debugArea);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() instanceof JButton) {
      JButton source = (JButton)e.getSource();
      if (source == pulseUpButton) {
        pulseValue++;
        pulseField.setText("" + pulseValue);
        deviceThread.setPulseValue(pulseValue);
      } else if (source == pulseDownButton) {
        pulseValue--;
        pulseField.setText("" + pulseValue);
        deviceThread.setPulseValue(pulseValue);
      } else if (source == systolicUpButton) {
        systolicBP++;
        systolicField.setText("" + systolicBP);
        deviceThread.setSystolicBP(systolicBP);
      } else if (source == systolicDownButton) {
        systolicBP--;
        systolicField.setText("" + systolicBP);
        deviceThread.setSystolicBP(systolicBP);
      } else if (source == diastolicUpButton) {
        diastolicBP++;
        diastolicField.setText("" + diastolicBP);
        deviceThread.setDiastolicBP(diastolicBP);
      } else if (source == diastolicDownButton) {
        diastolicBP--;
        diastolicField.setText("" + diastolicBP);
        deviceThread.setDiastolicBP(diastolicBP);
      } else if (source == tempUpButton) {
        tempValue += 0.1;
        tempField.setText("" + df.format(tempValue));
        deviceThread.setTempValue(tempValue);
      } else if (source == tempDownButton) {
        tempValue -= 0.1;
        tempField.setText("" + df.format(tempValue));
        deviceThread.setTempValue(tempValue);
      } else if (source == o2SatUpButton) {
        o2SatValue += 0.1;
        o2SatField.setText("" + df.format(o2SatValue));
        deviceThread.setO2SatValue(o2SatValue);
      } else if (source == o2SatDownButton) {
        o2SatValue -= 0.1;
        o2SatField.setText("" + df.format(o2SatValue));
        deviceThread.setO2SatValue(o2SatValue);
      } else {
        //This is a big one, this is where we launch or stop our simluation
        System.out.println ("This is the thread control button!");
        
        String curButtonText = sendButton.getText();
        if (curButtonText.equalsIgnoreCase("Begin Sending Data")) {
          deviceThread.start();
          sendButton.setText("Shut Down Simulator");
        } else {
          deviceThread.shutDown();
          //Give it some time to stop
          try { Thread.sleep(1000); } catch (Throwable t) {}
          
          deviceThread = new DeviceThread(deviceChoice, patientID, debugArea);
          deviceThread.setPulseValue(pulseValue);
          deviceThread.setSystolicBP(systolicBP);
          deviceThread.setDiastolicBP(diastolicBP);
          deviceThread.setTempValue(tempValue);
          deviceThread.setO2SatValue(o2SatValue);

          sendButton.setText("Begin Sending Data");
        }
      }
    } else {
      JTextField source = (JTextField)e.getSource();
      if (source == pulseField) {
        try {
          pulseValue = Integer.parseInt(pulseField.getText());
          deviceThread.setPulseValue(pulseValue);
        } catch (Throwable t) {
          pulseField.setText("" + pulseValue);
        }
      } else if (source == systolicField) {
        try {
          systolicBP = Integer.parseInt(systolicField.getText());
          deviceThread.setSystolicBP(systolicBP);
        } catch (Throwable t) {
          systolicField.setText("" + systolicField);
        }
      } else if (source == diastolicField) {
        try {
          diastolicBP = Integer.parseInt(diastolicField.getText());
          deviceThread.setDiastolicBP(diastolicBP);
        } catch (Throwable t) {
          diastolicField.setText("" + diastolicBP);
        }
      } else if (source == tempField) {
        try {
          tempValue = (Double)df.parse(tempField.getText());
          tempField.setText("" + df.format(tempValue));
          deviceThread.setTempValue(tempValue);
        } catch (Throwable t) {
          tempField.setText("" + df.format(tempValue));
        }
      } else if (source == o2SatField) {
        try {
          o2SatValue = (Double)df.parse(o2SatField.getText());
          o2SatField.setText("" + df.format(o2SatValue));
          deviceThread.setO2SatValue(o2SatValue);
        } catch (Throwable t) {
          o2SatField.setText("" + df.format(o2SatValue));
        }
      }
    }
  }
}
