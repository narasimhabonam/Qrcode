import java.awt.Desktop;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class QRCodeGenerator {

    public static void generateQRCode(String text, String filePath, int width, int height) throws IOException {
        String encodedText = java.net.URLEncoder.encode(text, "UTF-8");
        String urlString = String.format(
            "https://api.qrserver.com/v1/create-qr-code/?size=%dx%d&data=%s", width, height, encodedText
        );

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            Path outputPath = Paths.get(filePath);
            Files.copy(connection.getInputStream(), outputPath);
            System.out.println("QR Code generated successfully and saved to " + filePath);

            // Attempt to open the file if supported (may not work on online compilers)
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(outputPath.toFile());
            }
        } else {
            System.err.println("Error: Failed to generate QR code. Response code: " + connection.getResponseCode());
        }
        connection.disconnect();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the text for the QR Code: ");
        String text = scanner.nextLine();

        System.out.print("Enter the path to save the QR Code (e.g., ./qrcode.png): ");
        String filePath = scanner.nextLine();

        int width = 300;
        int height = 300;

        try {
            generateQRCode(text, filePath, width, height);
        } catch (IOException e) {
            System.err.println("Could not generate QR Code, " + e.getMessage());
        }

        scanner.close();
    }
}
