package org.ac.bluemix;

import org.ac.bluemix.ui.DeviceConfigFrame;

/**
 * @author ac010168
 *
 */
public class SimpleDevice {

  /**
   * @param args
   */
  public static void main(String[] args) {
    new SimpleDevice().doSomeStuff();
  }
  
  public void doSomeStuff() {
    new DeviceConfigFrame();
  }
}
