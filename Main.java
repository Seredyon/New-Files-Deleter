import com.formdev.flatlaf.FlatDarkLaf;

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
        customizeUI();
    }

    private void initUI() {
        JLabel directoryLabel = new JLabel("Directory Path:");
        directoryEntry = new JTextField();

        applyButtonStyle("Browse", "path/to/browse/icon.png", e -> browseDirectory());
        applyButtonStyle("Save Files", "path/to/save/icon.png", e -> saveFiles());

        JButton deleteButton = new JButton("Delete New Files");
        applyButtonStyle("Delete New Files", "path/to/delete/icon.png", e -> deleteNewFiles());
        deleteButton.setEnabled(false);

        JPanel spacer = new JPanel();
        messageLabel = new JLabel("");
        messageLabel.setVisible(false);

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        add(directoryLabel);
        add(directoryEntry);
        add(deleteButton);
        add(spacer);
        add(messageLabel);

        setDarkTheme();
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void customizeUI() {
        Font font = new Font("Segoe UI", Font.PLAIN, 16);
        UIManager.put("controlFont", font);
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("TextField.font", font);

        UIManager.put("nimbusBase", new Color(45, 45, 45));
        UIManager.put("text", new Color(200, 200, 200));
    }

    private void applyButtonStyle(String buttonText, String iconPath, ActionListener actionListener) {
        JButton button = new JButton(buttonText);
        button.addActionListener(actionListener);

        if (iconPath != null && !iconPath.isEmpty()) {
            button.setIcon(new ImageIcon(iconPath));
        }

        button.setBackground(new Color(71, 120, 197));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(87, 142, 231));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(71, 120, 197));
            }
        });

        add(button);
    }

    private void setDarkTheme() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());

            Font font = new Font("Segoe UI", Font.PLAIN, 14);
            UIManager.put("controlFont", font);

            UIManager.put("nimbusBase", new Color(30, 30, 30));
            UIManager.put("text", new Color(200, 200, 200));

            UIManager.put("Button.background", new Color(71, 120, 197));
            UIManager.put("Button.foreground", Color.WHITE);

            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        JButton deleteButton = getDeleteButton();
        deleteButton.setEnabled(true);
        deleteButton.setForeground(Color.WHITE);
    }

    private void deleteNewFiles() {
        String directoryPath = directoryEntry.getText().trim();
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
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        Main main = new Main();
        main.setTitle("New Files Deleter");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setIconImage(new ImageIcon("path/to/your/icon.png").getImage());
        main.setVisible(true);
    }
}
