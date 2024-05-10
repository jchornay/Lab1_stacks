/**
 * Lab 7: An inventory control program that implements stacks, queues, iterable lists, recursion, trees, heaps, sets,
 * and maps in order to allow the user to access and modify TV inventory, customer accounts, transactions, and
 * deliveries at a TV warehouse.
 *
 * @author Jonathan Chornay
 * @version 1.7
 * date May 9th, 2024
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class TVReturn {

    private HashMap<String, TV> hashMap;
    private static Path filePath;

    public TVReturn() {

        this.hashMap = new HashMap<>();

        if (filePath == null) {
            boolean invalidFile = true;
            while (invalidFile) {

                Scanner input = new Scanner(System.in);
                System.out.print("Please enter file path for return data (.txt): ");
                filePath = Paths.get(input.nextLine());

                try (Scanner input_file = new Scanner(filePath)) {
                    invalidFile = false;
                } catch (IOException error) {
                    System.out.printf("Error: File not found in local directory! Try again.%n");
                }
            }
        }

        try (Scanner input_file = new Scanner(filePath)) {
            // takes each line of input file and stores it as a separate TV
            while (input_file.hasNext()) {

                String id_number = input_file.nextLine();
                String brand = input_file.nextLine();
                String model = input_file.nextLine();
                double price = Double.valueOf(input_file.nextLine());

                TV temp = new TV(id_number, new TVType(brand, model, price));
                this.hashMap.put(id_number, temp);

            }
        } catch (IOException error) {
            System.out.printf("Error: File not found!%n");
        }
    }

    public TV lookup() {

        String id_number = null;
        TV result = null;

        Scanner input = new Scanner(System.in);

        // validation loop to make sure user chooses existing ID
        boolean return_loop = true;
        while (return_loop) {
            System.out.print("Please enter in the id number of the returned TV: ");
            id_number = input.nextLine();

            result = this.hashMap.get(id_number);

            // if not found, repeat loop until found
            if (result == null) {
                System.out.print("TV not found, try again! ");
                // if found, exit loop
            } else {
                return_loop = false;
            }
        }

        System.out.println();
        // prints formatted summary of purchase
        System.out.printf("%-26s%s%n", String.format("TV ID: %s", result.getId_number()), "Refund");
        System.out.printf("%-26s%s%n", "-------------------", "------");
        System.out.printf("%-26s%s%n", String.format("%s - %s", result.getTvType().getBrand(), result.getTvType().getModel()), String.format("$%-4.2f", result.getTvType().getPrice() * 1.06));

        TV returnTV = this.hashMap.remove(id_number);

        // creates stringbuilder object as basis for output file
        StringBuilder output = new StringBuilder();

        // adds remaining TV info to file, plus newline character

        for (String key : this.hashMap.keySet()) {
            TV appendTv = this.hashMap.get(key);
            output.append(appendTv.getId_number());
            output.append("\n");
            output.append(appendTv.getTvType().getBrand());
            output.append("\n");
            output.append(appendTv.getTvType().getModel());
            output.append("\n");
            output.append(appendTv.getTvType().getPrice());
            output.append("\n");
        }

        // checks if last character is a newline and removes if necessary
        if (output.length() > 0 && output.charAt(output.length() - 1) == '\n') {
            output.deleteCharAt(output.length() - 1);
        }
        // saves output file
        try (PrintWriter outputFile = new PrintWriter(filePath.toString())) {
            outputFile.print(output);
        } catch (IOException e) {
            System.out.println("Failed to write file!");
        }

        return returnTV;
    }

}
