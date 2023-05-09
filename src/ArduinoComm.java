import com.fazecast.jSerialComm.SerialPort; // library voor arduino communicatie

import java.io.IOException;
import java.io.PrintWriter;

public class ArduinoComm {
    public static void main(String[] args) throws IOException, InterruptedException {
         // Port declareren en initialiseren uit jSerialComm dependency.
        SerialPort sp = SerialPort.getCommPort("COM8"); // Verander name nog! (te vinden in arduino code)
        sp.setComPortParameters(9600, 8, 1, 0); // Standaard voor arduino
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        PrintWriter output = new PrintWriter(sp.getOutputStream()); // Output variabele gedeclareerd.

        if (sp.openPort()) {
            System.out.println("Port is open :)");
        } else {
            System.out.println("Failed to open port :(");
            return;
        }

        // Vervang dit met wat je wilt sturen (dit was voor mijn demo)
        for (short i = 0; i < 10; i++) {
            System.out.println(i);
            output.println(i); // Print short naar serial comm van arduino
            output.flush(); // Java --> Arduino
            Thread.sleep(1000);

            if (i == 9) {
                i = 0;
            }
        }

        if (sp.closePort()) {
            System.out.println("Port is closed :)");
        } else {
            System.out.println("Failed to close port :(");
            return;
        }
    }
}