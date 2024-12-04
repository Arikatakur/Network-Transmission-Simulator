package il.ac.kinneret.mjmay;

import java.io.*;
import java.net.*;

/**
 *  A UDP client implementing the stop and wait protocol for file transfer.
 * This class is responsible for sending a file to a server over UDP by splitting it into packets
 * and ensuring each packet is correct before sending the next.
 * @author mohamedkhalil
 * @version 2.3.4 #adding documentation
 */
public class StopWaitClient {

    /**
     * A sending program that sends a file to the receiving program using UDP
     *
     * @param args Command Line Prompt (input must be 4) if not return.
     * 
     * @throws Exception If there's an error throws an exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println("Syntax: UdpStopWaitClient-5784 -dest=ip -port=p -f=filename -packetsize=s");
            return;
        }

        String dest = null;
        int port = 0;
        int packetSize = 0;
        File file = null;

        try {
            for (String arg : args) {
                if (arg.startsWith("-dest=")) {
                    dest = arg.substring(6);
                    if (!dest.equals("localhost") && !dest.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
                        throw new IllegalArgumentException("Error parsing destination address: " + dest + ": Name or service not known");
                    }
                } else if (arg.startsWith("-port=")) {
                    port = Integer.parseInt(arg.substring(6));
                    if (port < 1025 || port > 65535) {
                        throw new IllegalArgumentException("Error: port must be between 1025 and 65535");
                    }
                } else if (arg.startsWith("-packetsize=")) {
       
                    String packetSizeStr = arg.substring(12); // Extract the value of the packet size

                    try {
                        // Check if the value contains only valid integer characters
                        if (packetSizeStr.contains(".") || packetSizeStr.contains("-") || !packetSizeStr.matches("\\d+")) {
                            throw new NumberFormatException("Invalid packet size: " + packetSizeStr);
                        }
                
                        packetSize = Integer.parseInt(packetSizeStr); // Parse the integer value
                
                        // Ensure the packet size is greater than 0
                        if (packetSize <= 0 || packetSize >= Integer.MAX_VALUE)  {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Error parsing packet size: For input string: \"" + packetSizeStr + "\"", e);
                    }
                } else if (arg.startsWith("-f=")) {
                    file = new File(arg.substring(3));
                    if (!file.exists()) {
                        throw new FileNotFoundException("");
                    }
                }
            }

            if (dest == null || port == 0 || packetSize == 0 || file == null) {
                throw new IllegalArgumentException("");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Syntax: UdpStopWaitClient-5784 -dest=ip -port=p -f=filename -packetsize=s");
            return;
        }

        try (DatagramSocket socket = new DatagramSocket();
             FileInputStream fis = new FileInputStream(file)) {

            InetAddress receiverAddress = InetAddress.getByName(dest);
            byte[] buffer = new byte[packetSize];
            int packetNumber = 1;

            socket.setSoTimeout(2000);

            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                boolean ackReceived = false;

                while (!ackReceived) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, bytesRead, receiverAddress, port);
                        socket.send(packet);
                        System.out.println("Sent packet " + packetNumber);

                        byte[] ackBuffer = new byte[4];
                        DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
                        socket.receive(ackPacket);

                        System.out.println("Received ACK on " + packetNumber);
                        ackReceived = true;

                    } catch (SocketTimeoutException e) {
                        System.out.println("Timeout: Resending packet " + packetNumber);
                    }
                }
                packetNumber++;
            }

            byte[] endSignal = {-1};
            DatagramPacket finalPacket = new DatagramPacket(endSignal, endSignal.length, receiverAddress, port);
            socket.send(finalPacket);
            System.out.println("Sent final packet for " + file.getName() + ".");

            byte[] ackBuffer = new byte[4];
            DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
            socket.receive(ackPacket);
            System.out.println("Received ACK on " + packetNumber);

        } catch (Exception e) {
            System.out.println("Error during transmission: " + e.getMessage());
        }
    }
}
