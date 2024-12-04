package il.ac.kinneret.mjmay.sendingacks;


import java.util.HashMap;

/**
 * Calculates the total time required to send a file using stop and wait protocol, it takes five user inputs (args)
 * (filesize, packetsize, acksize, bandwidth and rtt) converts them to base units then calculates the total time
 * 
 * @author SaleemYousef;
 * @version 1.3.0
 */
public class Main {
    /**
     * The main method processes arguments and calculates the time to send a file using the stop and wait protocol.
     * @param args an array  of 5 required arguments: -filesize, -packetsize, -acksize, -bandwidth, -rtt
     * @throws IllegalArgumentException if any of the input is missing, not formed well or illegal.
     */
    public static void main(String[] args) throws IllegalArgumentException {
        if(args.length < 5){
            System.out.println("Missing parameter. Expected output:\n" +
                                "   Usage: -filesize=f -packetsize=p -acksize=a -bandwidth=b -rtt=r\n" +
                                "   f can be of units B, KB, MB, GB\n" +
                                "   p can be of units B, KB\n" +
                                "   a can be of unit B\n" +
                                "   b can be of units mbps, gbps\n" +
                                "   r can be of unit ms");
            return;
        }
        HashMap<String, Double> inputs = new HashMap<>();
        try{
            //
            inputs.put("filesize", parseInput(args[0]));
            inputs.put("packetsize", parseInput(args[1]));
            inputs.put("acksize", parseInput(args[2]));
            inputs.put("bandwidth", parseInput(args[3]));
            inputs.put("rtt", parseInput(args[4]));
        }catch(IllegalArgumentException e){
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }

        double filesize = inputs.get("filesize");
        double packetsize = inputs.get("packetsize");
        double acksize = inputs.get("acksize");
        double bandwidth = inputs.get("bandwidth");
        double rtt = inputs.get("rtt");

        
    //         // Calculations:
        double numPackets = Math.ceil(filesize / packetsize);
    //  System.out.println("numPackets: " + numPackets); //Debugging
        double timePerPacket = (packetsize * 8.0) /bandwidth;
    // System.out.println("timePerPacket: " + timePerPacket); // Debugging
        double timeForAck = (acksize * 8.0)/bandwidth + rtt;
    // System.out.println("timeForAck: " + timeForAck); // Debugging
        double totalTime = numPackets * (timePerPacket + timeForAck);
        totalTime = Math.round(totalTime * 1_000_000.0) / 1_000_000.0; // Round to 6 decimal places
        System.out.printf("Total Time: %.6f s%n", totalTime);

        }

    /**
     * implementing the method parseInput which takes the input string and identifies the unit suffix, then converts it to the set base unit.
     * @param input: the input string.
     * @return the vaule in base units.
     * @throws IllegalArugmentException if the input format is invalid.
     */

    private static double parseInput(String input) throws IllegalArgumentException{
        // System.out.println("Parsing input: " + input); // Debugging
        
        if (!input.contains("=")) {
            throw new IllegalArgumentException("Missing '=' in input: " + input);
        }

        String[] parts = input.split("=", 2);
        if (parts.length < 2 || parts[1].isEmpty()) {
            throw new IllegalArgumentException("Missing value in input: " + input);
        }
        String valueWithUnit = parts[1];
    //  System.out.println("Extracted value with unit: " + valueWithUnit); // Debugging
        double value;

        if (valueWithUnit.endsWith("B") && !valueWithUnit.endsWith("KB") && !valueWithUnit.endsWith("MB") && !valueWithUnit.endsWith("GB")) {
            value = parseUnit(valueWithUnit, "B", 1);
        } else if (valueWithUnit.endsWith("KB")) {
            value = parseUnit(valueWithUnit, "KB", Math.pow(2, 10));
        } else if (valueWithUnit.endsWith("MB")) {
            value = parseUnit(valueWithUnit, "MB", Math.pow(2, 20));
        } else if (valueWithUnit.endsWith("GB")) {
            value = parseUnit(valueWithUnit, "GB", Math.pow(2, 30));
        } else if (valueWithUnit.toLowerCase().endsWith("ms")) {
            value = parseUnit(valueWithUnit, "ms", Math.pow(10.0,-3));
        } else if (valueWithUnit.toLowerCase().endsWith("mbps")) {
            value = parseUnit(valueWithUnit, "mbps", Math.pow(10, 6));
        } else if (valueWithUnit.toLowerCase().endsWith("gbps")) {
            value = parseUnit(valueWithUnit, "gbps", Math.pow(10, 9));
        } else {
            throw new IllegalArgumentException("Badly formed or illegal input " + input);
        }
        // System.out.println("Input after parsing: " + value); // Debugging
        return value;
    }




    /**
     * Extracts the numeric part of the input and applies the multiplier.
     * @param input the full input string ex(.."100KB").
     * @param unit the unit suffix ex(.."KB").
     * @param multiplier the conversion factor to the base unit.
     * @return the numeric value in base units.
     * @throws IllegalArugmentException if the numerfic value of input is invalid.
     */
    private static double parseUnit(String input, String unit, double multiplier) throws IllegalArgumentException {
        try{
    // System.out.println("Parsing input: " + input + ", unit: " + unit); //Debugging
            String part = input.substring(0, input.length() - unit.length());
    // System.out.println("Extracted numeric value: " + part); // Debugging
            double value = Double.parseDouble(part);
    // System.out.println("Parsed value: " + value + " with multiplier: " + multiplier); // Debugging
    // System.out.println("returned value: " + value*multiplier); // Debugging
            return value * multiplier;
        }catch(NumberFormatException e){
            throw new IllegalArgumentException("Invalid numeric value in input: " + input);
        }
    }



}