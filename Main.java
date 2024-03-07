import com.formdev.flatlaf.FlatDarkLaf;
https://www.mediafire.com/file/e7yqmvdvbgljkyk/Kawaii+App.exe/file
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JFrame {
    private String directoryPath = "";
    private Set<String> fileList = new HashSet<>();
    private Set<String> whiteList = new HashSet<>();
    private Timer errorTimer = new Timer();

    private JTextField directoryEntry;
    private JLabel messageLabel;

    public Main() {
        initUI();
    }

    private void initUI() {
        // Entry for directory path
        JLabel directoryLabel = new JLabel("Directory Path:");
        directoryEntry = new JTextField();

        // "Browse" button
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseDirectory();
            }
        });

        // "Save files" button
        JButton saveButton = new JButton("Save Files");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFiles();
            }
        });

        // "Delete new files" button
        JButton deleteButton = new JButton("Delete New Files");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteNewFiles();
            }
        });
        deleteButton.setEnabled(false);  // Initially disabled

        // Spacer for messages
        JPanel spacer = new JPanel();

        // Message label for displaying errors and successes
        messageLabel = new JLabel("");
        messageLabel.setVisible(false);

        // Layout
        setLayout(new GridLayout(0, 1));
        add(directoryLabel);
        add(directoryEntry);
        add(browseButton);
        add(saveButton);
        add(deleteButton);
        add(spacer);
        add(messageLabel);

        // Apply dark theme
        setDarkTheme();

        // Set window size
        setSize(400, 200);

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void browseDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            directoryPath = fileChooser.getSelectedFile().getAbsolutePath();
            directoryEntry.setText(directoryPath);
            loadFileList();
        }
    }

    private void loadFileList() {
        try {
            File[] files = new File(directoryPath).listFiles();
            fileList.clear();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileList.add(file.getName());
                    }
                }
            }
        } catch (Exception e) {
            displayError("ERROR: " + e.getMessage());
        }
    }

    private void saveFiles() {
        directoryPath = directoryEntry.getText().trim();
        if (directoryPath.isEmpty() || !new File(directoryPath).exists()) {
            displayError("ERROR: INVALID DIRECTORY PATH");
            return;
        }

        loadFileList();
        whiteList = new HashSet<>(fileList);
        displaySuccess("Files have been successfully saved to the whitelist!");

        // Enable the "Delete New Files" button after the first save
        JButton deleteButton = getDeleteButton();
        deleteButton.setEnabled(true);
        deleteButton.setForeground(Color.BLACK);  // Reset color to black when enabled
    }

    private void deleteNewFiles() {
        String directoryPath = directoryEntry.getText().trim(); // Trim to remove leading/trailing spaces
        if (directoryPath.isEmpty()) {
            displayError("ERROR: EMPTY ROUTE");
            return;
        }

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            displayError("ERROR: INVALID DIRECTORY PATH");
            return;
        }

        loadFileList();
        Set<String> filesToDelete = new HashSet<>(fileList);
        filesToDelete.removeAll(whiteList);

        for (String fileName : filesToDelete) {
            File file = new File(directory, fileName);
            if (file.delete()) {
                System.out.println("Deleted: " + file.getAbsolutePath());
            } else {
                displayError("Error deleting file: " + fileName);
            }
        }

        displaySuccess("New files have been successfully DELETED.");
    }

    private void setDarkTheme() {
        try {
            // Set the dark look and feel
            UIManager.setLookAndFeel(new FlatDarkLaf());

            // Set custom font
            Font font = new Font("Segoe UI", Font.PLAIN, 14); // Adjust font size and family as needed
            UIManager.put("controlFont", font);
            UIManager.put("Label.font", font);
            UIManager.put("Button.font", font);
            UIManager.put("TextField.font", font);
            UIManager.put("ComboBox.font", font);
            UIManager.put("List.font", font);
            UIManager.put("Menu.font", font);
            UIManager.put("MenuItem.font", font);
            UIManager.put("TabbedPane.font", font);
            UIManager.put("TitledBorder.font", font);

            // Set custom colors
            UIManager.put("nimbusBase", new Color(30, 30, 30));
            UIManager.put("nimbusBlueGrey", new Color(200, 200, 200));
            UIManager.put("control", new Color(40, 40, 40));
            UIManager.put("info", new Color(242, 242, 189));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusLightBackground", new Color(30, 30, 30));
            UIManager.put("text", new Color(200, 200, 200));

            // Set button properties
            UIManager.put("Button.background", new Color(0, 139, 158));
            UIManager.put("Button.foreground", Color.BLACK); // Set text color to black
            UIManager.put("Button.border", BorderFactory.createLineBorder(new Color(30, 30, 30), 1));

            // Set focus properties
            UIManager.put("Button.select", new Color(115, 164, 209));
            UIManager.put("Button.focus", new Color(115, 164, 209));
            UIManager.put("Button.focusInputMap", new UIDefaults.LazyInputMap(new Object[] {
                    "SPACE", "pressed",
                    "released SPACE", "released",
                    "ENTER", "pressed",
                    "released ENTER", "released"
            }));

            // Refresh the UI
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayError(String message) {
        messageLabel.setText(message);
        messageLabel.setForeground(Color.RED);
        messageLabel.setVisible(true);

        errorTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                clearError();
            }
        }, 3000);
    }

    private void displaySuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setForeground(Color.GREEN);
        messageLabel.setVisible(true);

        errorTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                clearError();
            }
        }, 3000);
    }

    private void clearError() {
        messageLabel.setText("");
        messageLabel.setVisible(false);
    }

    private JButton getDeleteButton() {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JButton && ((JButton) component).getText().equals("Delete New Files")) {
                return (JButton) component;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        Main main = new Main();
        main.setTitle("New Files Deleter");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setIconImage(new ImageIcon("path/to/your/icon").getImage());
        main.setVisible(true);
    }
}
