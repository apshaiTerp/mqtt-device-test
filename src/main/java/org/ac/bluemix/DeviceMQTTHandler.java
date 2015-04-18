package org.ac.bluemix;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

/**
 * @author ac010168
 *
 */
public class DeviceMQTTHandler extends MQTTHandler {

  /*
   * (non-Javadoc)
   * @see org.ac.bluemix.MQTTHandler#messageArrived(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
   */
  @Override
  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
    super.messageArrived(topic, mqttMessage);
    
    //Check whether the event is a command event from app
    if (topic.equals("iot-2/cmd/" + MQTTUtil.DEFAULT_CMD_ID + "/fmt/json")) {
      String payload = new String(mqttMessage.getPayload());
      JSONObject jsonObject = new JSONObject(payload);
      String cmd = jsonObject.getString("cmd");
      //Reset the count
      if (cmd != null && cmd.equals("reset")) {
        int resetcount = jsonObject.getInt("count");
        DeviceThread.count = resetcount;
        System.out.println("Count is reset to " + resetcount);
      }
    }
  }
}
