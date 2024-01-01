# New Files Deleter

A simple Java Swing application to manage and delete new files in a specified directory.

## Features

- **Browse Directory:** Select the directory to monitor for new files.
- **Save Files:** Save the current files in the selected directory to a whitelist.
- **Delete New Files:** Delete files that are not in the whitelist.

## How to Use

1. **Browse Directory:**
    - Click the "Browse" button to select the directory you want to monitor.

2. **Save Files:**
    - After selecting the directory, click the "Save Files" button to save the current files in the directory to the whitelist.

3. **Delete New Files:**
    - Once files are saved to the whitelist, click the "Delete New Files" button to remove any files in the directory that are not in the whitelist.

## Dark Theme

The application features a dark theme for a modern and visually appealing look.

## Dependencies

- [FlatLaf](https://www.formdev.com/flatlaf/) - Used for applying the dark theme to the Swing UI components.

## How to Build and Run

1. Clone the repository.
2. Compile the `Main.java` file using your preferred Java compiler.
3. Run the compiled class file.

```bash
javac Main.java
java Main
```

## Screenshots

*Include screenshots of the application in action (optional).*

## Notes

- This application uses a timer to display error and success messages for a short duration.
- Make sure to replace `"path/to/your/icon"` with the actual path to your application icon in the `createAndShowGUI()` method.

---

Feel free to contribute, report issues, or suggest improvements!
