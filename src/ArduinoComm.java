import com.fazecast.jSerialComm.SerialPort; // library voor arduino communicatie
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ArduinoComm {

    private Order order;
    private Robot robot;

    public ArduinoComm(Order order, Robot robot){
        this.order = order;
        this.robot = robot;
    }

    public ArduinoComm() {

    }


    // gets x and y position of every product in selected order and saves them in set pattern in a variable.
    // Then sends the pattern to the arduino.
    public void sendCoordinates() throws InterruptedException {
        StringBuilder positie = null;

        for (Orderline orderline: this.order.getOrderlines()) {
            positie.append(orderline.getProduct().getPositionX()).append(",").append(orderline.getProduct().getPositionY()).append(" ");
        }

        // opens connection on defined commport
        SerialPort sp = SerialPort.getCommPorts()[0]; //define comport arduino
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

    // reads message from arduino which contains x-position,y-position and then saves it in Robot class
    public void readIncomingMessage() throws InterruptedException {
        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.setComPortParameters(9600, 8, 1, 0);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;

                // Read the incoming data
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);

                // Process the received data
                String coordinates = new String(newData);
                System.out.println("Received data: " + coordinates);
                int index = coordinates.indexOf(','); // defines index to be searched for
                if (index != -1){ // if index is found
                    // makes a substring from the coordinates String until defined index, then parses it to int and saves it in x-position of robot
                    robot.setPositionX(Integer.parseInt(coordinates.substring(0, index)));
                    // makes a substring from the coordinates String from defined index onwards, then parses it to int and saves it in y-position of robot
                    robot.setPositionY(Integer.parseInt(coordinates.substring(index + 1)));
                }
            }
        });
        comPort.closePort();
    }

    public void sendEmergencySignal(boolean emergency) throws InterruptedException {
        SerialPort sp = SerialPort.getCommPorts()[0]; //define comport arduino
        sp.setComPortParameters(9600, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        if (sp.openPort()) {
            System.out.println("Port is open :)");
        } else {
            System.out.println("Failed to open port :(");
            return;
        }

        PrintWriter output = new PrintWriter(sp.getOutputStream()); // Output variable declared.

        if(emergency) {
            output.println(emergency); // Print short naar serial comm van arduino
            output.flush(); // Java --> Arduino
            Thread.sleep(1000);
        }

        if (sp.closePort()) {
            System.out.println("Port is closed :)");
        } else {
            System.out.println("Failed to close port :(");
        }
    }
}