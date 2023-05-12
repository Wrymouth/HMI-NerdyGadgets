import com.fazecast.jSerialComm.SerialPort; // library voor arduino communicatie

import java.io.IOException;
import java.io.PrintWriter;

public class ArduinoComm {

    private Order order;

    public ArduinoComm(Order order){
        this.order = order;
    }

    // gets x and y position of every product in selected order and saves them in set pattern in a variable.
    // Then sends the pattern to the arduino.
    public void sendMessage() throws InterruptedException {
        StringBuilder positie = null;

        for (Orderline orderline: this.order.getOrderlines()) {
            positie.append(orderline.getProduct().getPositionX()).append(",").append(orderline.getProduct().getPositionY()).append(" ");
        }

        // opens connection on defined commport
        SerialPort sp = SerialPort.getCommPort("COM8"); //define comport arduino
        sp.setComPortParameters(9600, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        if (sp.openPort()) {
            System.out.println("Port is open :)");
        } else {
            System.out.println("Failed to open port :(");
            return;
        }

        PrintWriter output = new PrintWriter(sp.getOutputStream()); // Output variable declared.

        output.println(positie); // Print short naar serial comm van arduino
        output.flush(); // Java --> Arduino
        Thread.sleep(1000);

        if (sp.closePort()) {
            System.out.println("Port is closed :)");
        } else {
            System.out.println("Failed to close port :(");
        }
    }
}