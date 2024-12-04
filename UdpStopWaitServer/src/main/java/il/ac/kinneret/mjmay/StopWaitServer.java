package il.ac.kinneret.mjmay;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/**
 * A UDP server implements the stop and wait protocol for reliable file reception.
 * The server listens on a specified IP and port, receives packets from a client and writes
 * the received data to an output file.It ensures reliability by sending ACKs for each received packet.
 * 
 * the server stops running after reseiving an end signal from the client.
 * @author mohamedkhalil
 * @version 2.3.2
 */

public class StopWaitServer {
    /**
     * The main method that starts the UDP Stop and Wait server.
     * This method parses command line arguments to configure the listening IP,port and output file.
     * Receiveddata from the client, writes it to the output file and sends ACKs for each packet.
     * @param args Commandline arguments must be exactly 3:
     *  ip: The IP address to listen on ('0.0.0.0' or a valid IPv4 address).
     *  port: The port number to listen on (must be between 1025 and 65535).
     *  outfile: The file path where received data will be saved.
     * @throws IllegalArgumentException If the IP, port, or output file arguments are invalid.
     * @throws NumberFormatException If the port argument is not a valid integer.
     * @throws IOException If an I/O error occurs during file writing or socket operations.
     */

    public static void main(String[] args) throws IOException, NumberFormatException, IllegalArgumentException{
         // Check if the correct number of arguments is provided
        if (args.length != 3) {
            System.out.println("Syntax: UdpStopWaitServer-5784 -ip=ip -port=p -outfile=f");
            return;
        }

        String ip = null; // Variable to store the IP address to listen on
        int port = 0; // Variable to store the listening port number
        String outputFile = null; // Variable to store the output file path
        
        try {
            for (String arg : args) {
                if (arg.startsWith("-ip=")) {
                    ip = arg.substring(4);
                    // Validate the IP address format
                    if (!ip.equals("0.0.0.0") && !ip.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
                        throw new IllegalArgumentException("Error parsing listening address: " + ip + ": Name or service not known");
                    }
                    
                } else if (arg.startsWith("-port=")) {
                    try {
                        port = Integer.parseInt(arg.substring(6));
                        // Ensure the port is within the valid range
                        if (port < 1025 || port > 65535 || arg.substring(6).contains(".")) {
                            throw new IllegalArgumentException("Error: port must be between 1025 and 65535");
                        }
                    } catch (NumberFormatException e) {
                        // Handle invalid port input
                        System.out.println("Error parsing port: For input string: \"" + arg.substring(6) + "\"");
                        System.out.println("Syntax: UdpStopWaitServer-5784 -ip=ip -port=p -outfile=f");
                        return;
                    }
                } else if (arg.startsWith("-outfile=")) {
                    outputFile = arg.substring(9); // Extract the output file path
                }
            }
            // Ensure all required parameters are provided
            if (ip == null || port == 0 || outputFile == null) {
                throw new IllegalArgumentException("Missing required parameters.");
            }

        } catch (Exception e) {
            // Handle errors in argument parsing
            System.out.println(e.getMessage());
            System.out.println("Syntax: UdpStopWaitServer-5784 -ip=ip -port=p -outfile=f");
            return;
        }

        
        // Start the server and listen for incoming packets
        try (DatagramSocket socket = new DatagramSocket(port);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[port]; // Buffer to store incoming packet data
            int packetNumber = 1; // Counter for the received packets
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            boolean flag = true;
            while (flag) {
                System.out.println("Listening...");
                socket.receive(packet);
                byte[] data = packet.getData();
                int length = packet.getLength();

                if (length <= Integer.SIZE && data[0] == -1) {
                    sendingAcks(socket, packet.getAddress(), packet.getPort(), packetNumber);
                    System.out.println("File " + outputFile + " completed.  Received " + packetNumber + " packets.");
                    flag = false;
                    socket.close();
                    fos.close();
                } else {
                    fos.write(data, 0, length);
                    sendingAcks(socket, packet.getAddress(), packet.getPort(), packetNumber++);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an acknowledgment packet back to the sender.
     * @param socket The DatagramSocket used for sending the acknowledgment.
     * @param address The address of the sender.
     * @param port The port of the sender.
     * @param packetNumber The number of the packet being acknowledged.
     * @throws IOException if an IO error occurs.
     */
    private static void sendingAcks(DatagramSocket socket, InetAddress address, int port, int packetNumber) throws IOException {
        byte[] ackData = convertToByte(packetNumber);
        DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, address, port);
        socket.send(ackPacket);
        System.out.println("Received and acked: " + packetNumber);
    }
    /**
     * Converts an integer to a byte array.
     * @param value The integer value to be converted.
     * @return A byte array representing the integer value.
     */
    private static byte[] convertToByte(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }
}
