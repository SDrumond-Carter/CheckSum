/*=============================================================================
| pa02 - Calculating an 8, 16, or 32 bit
| checksum on an ASCII input file
|
| Author: Samuel Matos Drumond
| Language: Java
|
| To Compile: javac pa02.java
|
| To Execute: java -> java pa02 inputFile.txt 8
| where inputFile.txt is an ASCII input file
| and the number 8 could also be 16 or 32
| which are the valid checksum sizes, all
| other values are rejected with an error message
| and program termination
|
| Note: All input files are simple 8 bit ASCII input
|
| Date: April 23, 2023
|
+=============================================================================*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;


public class pa02 {
    private static final int[] VALIDS = {8, 16, 32};

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Incorrect number of arguments.");
            return;
        }

        String fileName = args[0];
        int bitsize = Integer.parseInt(args[1]);

        // Tries to open the input file
        BufferedReader inFile = null;
        try {
            inFile = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.err.println(fileName + " not found. Please check the file name.");
            return;
        }

        // Checks to see if bitsize is valid
        boolean validbitsize = false;
        for (int i = 0; i < VALIDS.length; i++) {
            if (bitsize == VALIDS[i]) {
                validbitsize = true;
                break;
            }
        }
        if (!validbitsize) {
            System.err.println("Valid checksum sizes are 8, 16, or 32");
            return;
        }

        String inputText = "";
        String line;
        try {
            while ((line = inFile.readLine()) != null) {
                inputText += line;
            }
            inFile.close();
        } catch (IOException e) {
            System.err.println("Error reading file.");
            return;
        }

        // Pads with X if needed
        if(inputText.length() % (bitsize / 8) != 0){
            inputText += '\n';
            while (inputText.length() % (bitsize / 8) != 0)
            {
                inputText += "X";
            }
        }
        

        // Prints 80 chars per line
        System.out.println();
        for (int i = 0; i < inputText.length(); i++) {
            System.out.print(inputText.charAt(i));
            if ((i + 1) % 80 == 0) {
                System.out.println();
            }
        }
        System.out.println();

        int checkSum = calculate(inputText, bitsize);

        System.out.printf("%2d bit checksum is %8s for all%4d chars\n", bitsize, Integer.toHexString(checkSum), inputText.length());
    }

    private static int calculate(String message, int bitsize) {
        int checkSum = 0;
        int string = 0;
        int overflow = ((int) Math.pow(2, bitsize)) - 1;
        int shift = bitsize / 8;
    
        for (int i = 0; i < message.length(); i++) {
            shift--;
            string += ((int) message.charAt(i)) << (shift* 8);
            if (shift == 0) {
                checkSum += string;
                checkSum &= overflow;
    
                string = 0;
                shift = bitsize / 8;
            }
        }
    
        return checkSum;
    }
}
