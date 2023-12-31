import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    // Logger for logging messages
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> directories = new ArrayList<>();

        // Prompt the user to enter directories for scanning
        System.out.println("Enter directories to scan (type 'done' when finished):");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done")) {
                break;
            }
            directories.add(input);
        }

        // Loop through each directory
        for (String directory : directories) {
            // Get the file object for the directory
            File dir = new File(directory);

            // Check if the directory exists and is readable
            if (dir.exists() && dir.canRead()) {
                // Log the start of the scanning process
                LOGGER.info("Scanning directory: " + directory);

                // Get the list of files in the directory
                File[] files = dir.listFiles();

                // Check if the directory is not empty
                if (files != null && files.length > 0) {
                    // Loop through each file
                    for (File file : files) {
                        // Check if the file is a regular file and is writable
                        if (file.isFile() && file.canWrite()) {
                            // Log the deletion of the file
                            LOGGER.info("Deleting file: " + file.getName());

                            // Delete the file
                            file.delete();
                        }
                    }
                }

                // Log the end of the scanning process
                LOGGER.info("Finished scanning directory: " + directory);
            } else {
                // Log the error message
                LOGGER.severe("Directory does not exist or is not readable: " + directory);
            }
        }

        scanner.close();  // Close the scanner
    }
}
