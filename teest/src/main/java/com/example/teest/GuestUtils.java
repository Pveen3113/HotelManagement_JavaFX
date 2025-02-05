package com.example.teest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GuestUtils {

    private static final String GUESTS_FILE = "guest_details.txt";

    public static void saveGuest(String name, String id, String contact) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GUESTS_FILE, true))) {
            writer.write(name + "," + id + "," + contact);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving guest: " + e.getMessage());
        }
    }

    public static void updateGuest(String oldName, String newName, String newId, String newContact) {
        File inputFile = new File(GUESTS_FILE);
        File tempFile = new File("guest_details_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3 && details[0].equals(oldName)) {
                    writer.write(newName + "," + newId + "," + newContact);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating guest: " + e.getMessage());
        }

        // Replace the original file with the updated file
        if (!inputFile.delete()) {
            System.out.println("Failed to delete the original file.");
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Failed to rename the temp file.");
        }
    }



    // Load all guests (returns a list of names)
    public static List<String> loadGuests() {
        List<String> guests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(GUESTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3) {
                    guests.add(details[0]); // Only add the name to the list
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading guests: " + e.getMessage());
        }
        return guests;
    }

    // Get details of a specific guest by name
    public static String getGuestDetails(String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader(GUESTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3 && details[0].equals(name)) {
                    return "Name: " + details[0] + "\nID: " + details[1] + "\nContact: " + details[2];
                }
            }
        } catch (IOException e) {
            System.out.println("Error retrieving guest details: " + e.getMessage());
        }
        return "Guest not found.";
    }

    // Delete a guest from the file by name
    public static void deleteGuest(String name) {
        File inputFile = new File(GUESTS_FILE);
        File tempFile = new File("guest_details_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3 && details[0].equals(name)) {
                    continue; // Skip writing this line to the temp file
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error deleting guest: " + e.getMessage());
        }

        // Replace the original file with the updated file
        if (!inputFile.delete()) {
            System.out.println("Failed to delete the original file.");
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Failed to rename the temp file.");
        }
    }
}
