package org.ac.bluemix.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author ac010168
 *
 */
public class DeviceConfigFrame extends JFrame implements ActionListener {

  /** Just because */
  private static final long serialVersionUID = -6323081776151568760L;

  private JComboBox<String> deviceBox;
  private JTextField patientText;
  
  boolean moveOn = false;
  
  public DeviceConfigFrame() {
    super("Device Simulator Setup");
    
    setLayout(new BorderLayout(10, 10));
    
    JPanel mainPanel = new JPanel();
    GridLayout layout = new GridLayout(6, 1, 10, 2);
    mainPanel.setLayout(layout);
    
    JLabel titleLabel = new JLabel("Device Configuration:");
    JLabel deviceLabel = new JLabel("Choose the Device Config:");
    JLabel patientLabel = new JLabel("Enter the Patient ID:");
    
    String[] devices = {"<Choose a Device Config>", "hackdevice1", "hackdevice2", "hackdevice3", "hackdevice4", 
        "hackdevice5", "hackdevice6", "hackdevice7", "hackdevice8", "hackdevice9", "hackdevice10" };
    
    deviceBox = new JComboBox<String>(devices);
    patientText = new JTextField();
    
    JButton startButton = new JButton ("Start Simulation");
    startButton.addActionListener(this);
    
    mainPanel.add(titleLabel);
    mainPanel.add(deviceLabel);
    mainPanel.add(deviceBox);
    mainPanel.add(patientLabel);
    mainPanel.add(patientText);
    mainPanel.add(startButton);
    
    getContentPane().add(mainPanel, BorderLayout.CENTER);
    getContentPane().add(new JLabel(""), BorderLayout.EAST);
    getContentPane().add(new JLabel(""), BorderLayout.WEST);
    getContentPane().add(new JLabel(""), BorderLayout.SOUTH);
    
    setSize(370,200);
    setResizable(false);
    setVisible(true);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        if (!moveOn)
          System.exit(0);
      }
    });
  }

  public void actionPerformed(ActionEvent e) {
    if (deviceBox.getSelectedIndex() == 0)
      return;
    String deviceChoice = (String) deviceBox.getSelectedItem();

    String patientID    = patientText.getText().trim();
    if (patientID.length() == 0)
      return;
    
    System.out.println ("The device choice is: " + deviceChoice);
    System.out.println ("The patientID is:     " + patientID);
    moveOn = true;
    
    setVisible(false);
    dispose();
    
    System.out.println("Do Something here...");
    new DeviceSimFrame(deviceChoice, patientID);
  }
}
