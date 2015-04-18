package org.ac.bluemix;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ac010168
 *
 */
public class SimpleDevice {

  public static int count = 0;
  public static int totalcount = 0;
  private MQTTHandler handler = null;

  /**
   * @param args
   */
  public static void main(String[] args) {
    new SimpleDevice().doSomeStuff();
  }
  
  public void doSomeStuff() {
    //Read properties from the conf file
    Properties props = MQTTUtil.readProperties("device.conf");
    
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

    while (totalcount < 20) {
      
      //Format the Json String
      JSONObject contObj = new JSONObject();
      JSONObject jsonObj = new JSONObject();
      try {
        contObj.put("count", count);
        contObj.put("blood-pressure", "120/80");
        contObj.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new Date()));
        jsonObj.put("d", contObj);
      } catch (JSONException e1) {
        e1.printStackTrace();
      }

      System.out.println("Send count as " + count);

      //Publish device events to the app
      //iot-2/evt/<event-id>/fmt/<format> 
      handler.publish("iot-2/evt/" + MQTTUtil.DEFAULT_EVENT_ID + "/fmt/json", jsonObj.toString(), false, 0);

      count++;
      totalcount++;

      try {
        Thread.sleep(2 * 1000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    System.out.println("Max Count reached, try to disconnect");
    handler.disconnect();
  }
}
