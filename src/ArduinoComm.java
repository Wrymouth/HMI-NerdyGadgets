import com.fazecast.jSerialComm.SerialPort; // library voor arduino communicatie
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import java.util.*;
import java.lang.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ArduinoComm {

    private WarehousePanel wp;

    private String instruction = "";
    private String receivedData = "";

    private SerialPort sp;

    public ArduinoComm(String comPort, WarehousePanel wp) {
        this.wp = wp;
        openComPort(comPort);
        sp.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;

                int availableBytes = sp.bytesAvailable();
                if (availableBytes > 0) {
                    byte[] newData = new byte[availableBytes];
                    int numRead = sp.readBytes(newData, newData.length);
                    receivedData += new String(newData);
                    
                    // the event reads data in small chunks, so the program checks for a newline character
                    // to know if the data is complete
                    int newlineIndex = receivedData.indexOf('\n');
                    if (newlineIndex != -1) {
                        String coordinates = receivedData.substring(0, newlineIndex);
                        System.out.println("Received data: " + coordinates);
                        int index = coordinates.indexOf(','); // if a comma is found, that means these are coordinates
                        if (index != -1) {
                            try {
                                int x = Integer.parseInt(coordinates.substring(0, index));
                                int y = Integer.parseInt(coordinates.substring(index + 1));
                                wp.setRobotPosition(x, y);
                            } catch (NumberFormatException e) {
                            }
                        }
                        // set receivedData to empty string so we can start listening for new data
                        receivedData = "";
                    }
                }
            }
        });
    }

    public ArduinoComm() {
    }

    public void openComPort(String com) {
        sp = SerialPort.getCommPort(com);
        sp.setBaudRate(9600);
        sp.setNumDataBits(8);
        sp.setNumStopBits(1);
        sp.setParity(SerialPort.NO_PARITY);
        sp.setDTR();
        sp.setRTS();
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (sp.openPort()) {
            System.out.println("Port is open :)"); // if connection is open print this
        } else {
            System.out.println("Failed to open port :("); // if connection is not open print this
            return;
        }
    }

    public static void closeComPort(String com) {
        SerialPort sp = SerialPort.getCommPort(com);
        if (sp.closePort()) {
            System.out.println("Port is closed :)"); // if connection is open print this
        } else {
            System.out.println("sendmessage: Failed to close port :("); // if connection is not open print this
            return;
        }
    }

    // gets x and y position of every product in selected order and saves them in
    // set pattern in a variable.
    // Then sends the pattern to the arduino.
    public void sendCoordinates() throws InterruptedException {
        instruction.trim();
        instruction += "\n";
        instruction = "100,2000\n";

        // opens connection on defined commport

        // print string per character
        byte[] strToBytes = instruction.getBytes();
        try {
            sp.writeBytes(strToBytes, strToBytes.length, 0);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    //Only called when the emergency button is pressed in HMI, sends emergency signal to robot
    public void sendEmergencySignal(boolean emergency) throws InterruptedException {
        PrintWriter output = new PrintWriter(sp.getOutputStream()); // Output variable declared.

        if(emergency) {
            output.println("E"); //Sends char E to robot, when read by Arduino it will activate the emergency stop
            output.flush();
            Thread.sleep(1000);
        }
    }

    //loops through all boxes in order and executes calculateAndSendCoordinatesTSP each time three products from boxes have been selected
    public void TSP(Order order) throws InterruptedException {
        List<Product> productsTBC = new ArrayList<>();
        int[] productX = new int[3];
        int[] productY = new int[3];

        for (Box box : order.getBoxes()) {
            for (Product product : box.getProducts()) {
                productsTBC.add(product); //add products to list until there are 3 in the list
                if (productsTBC.size() == 3) {
                    calculateAndSendCoordinatesTSP(productsTBC, productX, productY);
                    productsTBC.clear(); //clears list to add the next 3 products
                }
            }
        }

        if (!productsTBC.isEmpty()) { // checks if list is actually empty and executes function with remaining products if it isn't
            calculateAndSendCoordinatesTSP(productsTBC, productX, productY);
        }
    }

    private void calculateAndSendCoordinatesTSP(List<Product> productsTBC, int[] productX, int[] productY)
            throws InterruptedException {
        double[] distances = new double[productsTBC.size()];
        for (int j = 0; j < productsTBC.size(); j++) { // gets X and Y coordinates for all 3 products
            Product currentProduct = productsTBC.get(j);
            productX[j] = currentProduct.getPositionX();
            productY[j] = currentProduct.getPositionY();
            distances[j] = Math.sqrt(Math.pow(productY[j], 2) + Math.pow(productX[j], 2)); //calculates distance from current product to start (X 0, Y 0)
        }

        int m = 0; // variable to make sure codeblock executes once only
        int minIndex = -1; // is the index corresponding to the product with the shortest distance, used to get all info in all arrays for this product

        for (int o = 0; o < productsTBC.size(); o++) {
            double minValue = Double.POSITIVE_INFINITY;

            for (int k = 0; k < productsTBC.size(); k++) { //determines product closest to previous location, starts at (X 0 , Y 0)
                if (distances[k] != 0.0 && distances[k] < minValue) {
                    minValue = distances[k]; //minValue contains the shortest distance
                    minIndex = k; //minIndex contains the index corresponding to the product with the shortest distance
                }
            }

            if (minIndex != -1) { // only if the product closest to start has been determined
                if (m == 0) { // executes once
                    for (int b = 0; b < productsTBC.size(); b++) { //calculates distances between closest product to start and all other products
                        if (b != minIndex) {
                            productX[b] -= productX[minIndex];
                            productY[b] -= productY[minIndex];
                            distances[b] = Math.sqrt(Math.pow(productY[b], 2) + Math.pow(productX[b], 2));
                        }
                    }
                    m++;
                }

                distances[minIndex] = 0.0; // shortest distance to start set to 0,0 to make sure next closest product to itself isn't itself
                instruction += productX[minIndex] + "," + productY[minIndex] + " "; // adds X and Y coordinates to a String in to later send to Robot
                System.out.println("x-coordinate " + productX[minIndex] + "\n" + "y-coordinate " + productY[minIndex]);
            }
        }
        sendCoordinates(); //sends instruction String to robot
    }
}