import com.fazecast.jSerialComm.SerialPort; // library voor arduino communicatie
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.util.*;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ArduinoComm {

    private Order order;


    private WarehousePanel wp;
    private BoxesPanel bp;

    private String instruction = "";
    private String receivedData = "";
    private ArrayList<Product> orderProducts = new ArrayList<>();
    private ArrayList<Product> pickedProducts = new ArrayList<>();

    private SerialPort sp;

    public ArduinoComm(String comPort, WarehousePanel wp, BoxesPanel bp) {
        this.wp = wp;
        this.bp = bp;
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

                    // the event reads data in small chunks, so the program checks for a newline
                    // character
                    // to know if the data is complete

                    int newlineIndex = receivedData.indexOf('\n');
                    if (newlineIndex != -1) {
                        if (receivedData.charAt(0) == 'p') {
                            int amountPicked = Integer.parseInt(receivedData.substring(1, newlineIndex));
                            for (int i = 0; i < amountPicked; i++) {
                                if (orderProducts.isEmpty()) {
                                    order.setProcessed(true);
                                    break;
                                }
                                pickedProducts.add(orderProducts.get(0));
                                orderProducts.remove(0);
                            }
                            bp.setCount(pickedProducts.size());
                            TSP();
                            receivedData = "";
                        }
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

    public void setOrder(Order order) {
        this.order = order;
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
    public void sendCoordinates() {
        instruction.trim();
        instruction += "\n";

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

    // Only called when the emergency button is pressed in HMI, sends emergency
    // signal to robot
    public void sendEmergencySignal(boolean emergency) throws InterruptedException {
        PrintWriter output = new PrintWriter(sp.getOutputStream()); // Output variable declared.

        if (emergency) {
            output.println("E"); // Sends char E to robot, when read by Arduino it will activate the emergency
                                 // stop
            output.flush();
            Thread.sleep(1000);
        }
    }

    public void TSP() {
        List<Product> productsTBC = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (orderProducts.isEmpty()) {
                break;
            }
            productsTBC.add(orderProducts.get(i));
        }
        calculateAndSendCoordinatesTSP(productsTBC);
        productsTBC.clear();
    }

    private void calculateAndSendCoordinatesTSP(List<Product> productsTBC) {
        double[] distances = new double[productsTBC.size()];
        int[] productX = new int[3];
        int[] productY = new int[3];
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
                instruction += productX[minIndex] + "," + productY[minIndex] + " ";
                System.out.println("x-coordinate " + productX[minIndex] + "\n" + "y-coordinate " + productY[minIndex]);
            }
        }
        sendCoordinates();
    }

    public void setAllProducts() {
        for (Box box : order.getBoxes()) {
            for (Product product : box.getProducts()) {
                orderProducts.add(product);
            }
        }
    }
}