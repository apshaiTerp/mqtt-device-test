package org.ac.bluemix;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.swing.JTextArea;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ac010168
 *
 */
public class DeviceThread extends Thread {

  public static final DecimalFormat df = new DecimalFormat("###.#");
  
  private static final String UNIQUE_ID = UUID.randomUUID().toString();
  
  public static int totalcount = 0;
  
  private MQTTHandler handler = null;
  private boolean stopThread = false;

  private int pulseValue    = 90;
  private int systolicBP    = 110;
  private int diastolicBP   = 70;
  private double tempValue  = 98.6;
  private double o2SatValue = 100.0;
  
  private String deviceChoice;
  private String patientID;
  
  private JTextArea debugArea;
  
  private Random random;
  
  public DeviceThread(String deviceChoice, String patientID, JTextArea debugArea) {
    this.deviceChoice = deviceChoice;
    this.patientID    = patientID;
    this.debugArea    = debugArea;
    
    random = new Random();
  }
  
  @Override
  public void run() {
    //Read properties from the conf file
    Properties props = MQTTUtil.readProperties(deviceChoice + ".conf");
    
    String orgID      = props.getProperty("org");
    String deviceID   = props.getProperty("deviceid");
    String authMethod = "use-token-auth";
    String authToken  = props.getProperty("token");
    //isSSL property
    String sslStr     = props.getProperty("isSSL");
    boolean isSSL = false;
    if (sslStr.equals("T")) {
      isSSL = true;
    }

    System.out.println("org:         " + orgID);
    System.out.println("deviceID:    " + deviceID);
    System.out.println("authmethod:  " + authMethod);
    System.out.println("authtoken:   " + authToken);
    System.out.println("isSSL:       " + isSSL);
    
    String serverHost = orgID + MQTTUtil.SERVER_SUFFIX;
    String clientID   = "d:" + orgID + ":" + MQTTUtil.DEFAULT_DEVICE_TYPE + ":" + deviceID;
    handler           = new DeviceMQTTHandler();
    
    System.out.println ("serverHost: " + serverHost);
    System.out.println ("clientID:   " + clientID);
    
    handler.connect(serverHost, clientID, authMethod, authToken, isSSL);
    
    //Subscribe the Command events
    //iot-2/cmd/<cmd-type>/fmt/<format-id>
    handler.subscribe("iot-2/cmd/" + MQTTUtil.DEFAULT_CMD_ID + "/fmt/json", 0);

    while (!stopThread) {
      //Format the Json String
      JSONObject contObj = new JSONObject();
      JSONObject jsonObj = new JSONObject();
      try {
        contObj.put("customID", UNIQUE_ID);
        contObj.put("type", "device");
        contObj.put("totalCount", totalcount);
        
        //Give the pulse some variety, make it jump a bit from the sim setting
        int randomPulse = pulseValue + random.nextInt(9) - 4;
        
        //Add our medical readings
        contObj.put("patientID", patientID);
        contObj.put("pulse", randomPulse);
        contObj.put("systolicBP", systolicBP);
        contObj.put("diastolicBP", diastolicBP);
        contObj.put("temp", df.format(tempValue));
        contObj.put("o2Sat", df.format(o2SatValue));
        
        contObj.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        jsonObj.put("d", contObj);
      } catch (JSONException e1) {
        e1.printStackTrace();
      }

      System.out.println("Message: " + jsonObj);
      debugArea.setText(jsonObj.toString());

      //Publish device events to the app
      //iot-2/evt/<event-id>/fmt/<format> 
      handler.publish("iot-2/evt/" + MQTTUtil.DEFAULT_EVENT_ID + "/fmt/json", jsonObj.toString(), false, 0);

      totalcount++;

      try {
        Thread.sleep(990);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.println("Thread asked to shutdown.  Closing connection.");
    
    //Send a ShutDown Message through the handler
    JSONObject contObj = new JSONObject();
    JSONObject jsonObj = new JSONObject();
    contObj.put("customID", UNIQUE_ID);
    contObj.put("patientID", patientID);
    contObj.put("type", "stop");
    jsonObj.put("d", contObj);

    System.out.println("Message: " + jsonObj);
    debugArea.setText(jsonObj.toString());

    totalcount = 0;
    
    //Publish device events to the app
    //iot-2/evt/<event-id>/fmt/<format> 
    handler.publish("iot-2/evt/" + MQTTUtil.DEFAULT_EVENT_ID + "/fmt/json", jsonObj.toString(), false, 0);

    handler.disconnect();
  }
  
  public void shutDown() {
    stopThread = true;
  }

  /**
   * @return the pulseValue
   */
  public int getPulseValue() {
    return pulseValue;
  }

  /**
   * @param pulseValue the pulseValue to set
   */
  public void setPulseValue(int pulseValue) {
    this.pulseValue = pulseValue;
  }

  /**
   * @return the systolicBP
   */
  public int getSystolicBP() {
    return systolicBP;
  }

  /**
   * @param systolicBP the systolicBP to set
   */
  public void setSystolicBP(int systolicBP) {
    this.systolicBP = systolicBP;
  }

  /**
   * @return the diastolicBP
   */
  public int getDiastolicBP() {
    return diastolicBP;
  }

  /**
   * @param diastolicBP the diastolicBP to set
   */
  public void setDiastolicBP(int diastolicBP) {
    this.diastolicBP = diastolicBP;
  }

  /**
   * @return the tempValue
   */
  public double getTempValue() {
    return tempValue;
  }

  /**
   * @param tempValue the tempValue to set
   */
  public void setTempValue(double doubleValue) {
    try {
      
    } catch (Throwable t) {
      
    }
  }

  /**
   * @return the o2SatValue
   */
  public double getO2SatValue() {
    return o2SatValue;
  }

  /**
   * @param o2SatValue the o2SatValue to set
   */
  public void setO2SatValue(double o2SatValue) {
    this.o2SatValue = o2SatValue;
  }

}
