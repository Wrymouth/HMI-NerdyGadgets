import com.fazecast.jSerialComm.SerialPort; // library voor arduino communicatie
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.util.*;
import java.lang.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ArduinoComm {

    private Order order;
    private Robot robot;

    public ArduinoComm(Order order, Robot robot){
        this.order = order;
        this.robot = robot;
    }

    public ArduinoComm() {

    }

    public Robot getRobot() {
        return robot;
    }

    // gets x and y position of every product in selected order and saves them in set pattern in a variable.
    // Then sends the pattern to the arduino.
    public void sendCoordinates(int positionX, int positionY) throws InterruptedException {
        String positie = null;

            positie = positionX + "," + positionY + " ";


        // opens connection on defined commport
        SerialPort sp = SerialPort.getCommPort("com5"); //define comport arduino
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

    public void TSP() throws InterruptedException {
        List<Product> productsTBC = new ArrayList<>();
        int[] productX = new int[3];
        int[] productY = new int[3];

        for (Box box : order.getBoxes()) {
            for (Product product : box.getProducts()) {
                productsTBC.add(product);
                if (productsTBC.size() == 3) {
                    calculateAndSendCoordinatesTSP(productsTBC, productX, productY);
                    productsTBC.clear();
                }
            }
        }

        if (!productsTBC.isEmpty()) {
            calculateAndSendCoordinatesTSP(productsTBC, productX, productY);
        }
    }

    private void calculateAndSendCoordinatesTSP(List<Product> productsTBC, int[] productX, int[] productY) throws InterruptedException {
        double[] distances = new double[productsTBC.size()];
        for (int j = 0; j < productsTBC.size(); j++) {
            Product currentProduct = productsTBC.get(j);
            productX[j] = currentProduct.getPositionX();
            productY[j] = currentProduct.getPositionY();
            distances[j] = Math.sqrt(Math.pow(productY[j], 2) + Math.pow(productX[j], 2));
        }

        int m = 0;
        int minIndex = -1;

        for (int o = 0; o < productsTBC.size(); o++) {
            double minValue = Double.POSITIVE_INFINITY;

            for (int k = 0; k < productsTBC.size(); k++) {
                if (distances[k] != 0.0 && distances[k] < minValue) {
                    minValue = distances[k];
                    minIndex = k;
                }
            }

            if (minIndex != -1) {
                if (m == 0) {
                    for (int b = 0; b < productsTBC.size(); b++) {
                        if (b != minIndex) {
                            productX[b] -= productX[minIndex];
                            productY[b] -= productY[minIndex];
                            distances[b] = Math.sqrt(Math.pow(productY[b], 2) + Math.pow(productX[b], 2));
                        }
                    }
                    m++;
                }

                distances[minIndex] = 0.0;
                sendCoordinates(productX[minIndex], productY[minIndex]);
                System.out.println("x-coordinate " + productX[minIndex] + "\n" + "y-coordinate" + productY[minIndex]);
            }
        }
    }
}