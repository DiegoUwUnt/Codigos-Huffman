import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class CompressMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nSelecciona una opción:");
            System.out.println("1. Salir");
            System.out.println("2. Comprimir texto");
            System.out.println("3. Comprimir ADN");
            System.out.println("4. Comprimir imágenes");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume la nueva línea

            switch (choice) {
                case 1:
                    exit = true;
                    break;
                case 2:
                    compressText(scanner);
                    break;
                case 3:
                    compressDNA(scanner);
                    break;
                case 4:
                    compressImage(scanner);
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intenta de nuevo.");
            }
        }

        scanner.close();
    }

    private static void compressText(Scanner scanner) {
        System.out.println("Ingresa el nombre del archivo de texto a comprimir:");
        String inputFile = scanner.nextLine();
        System.out.println("Ingresa el nombre del archivo de salida:");
        String outputFile = scanner.nextLine();

        try {
            String content = new String(Files.readAllBytes(Paths.get(inputFile)));
            Huffman.CompressionResult result = Huffman.compressText(content);

            saveToFile(result.compressedData, outputFile);
            System.out.println("Porcentaje de compresión: " + result.compressionPercentage + "%");

            System.out.println("Códigos de Huffman y frecuencias:");
            for (Map.Entry<Character, String> entry : result.huffmanCodes.entrySet()) {
                char character = entry.getKey();
                String code = entry.getValue();
                int frequency = result.frequencies.get(character);
                System.out
                        .println("Carácter: " + character + ", Frecuencia: " + frequency + ", Código Huffman: " + code);
            }

        } catch (IOException e) {
            System.err.println("Error al leer o escribir archivos: " + e.getMessage());
        }
    }

    private static void compressDNA(Scanner scanner) {
        System.out.println("Ingresa el nombre del archivo de ADN a comprimir:");
        String inputFile = scanner.nextLine();
        System.out.println("Ingresa el nombre del archivo de salida:");
        String outputFile = scanner.nextLine();

        try {
            String content = new String(Files.readAllBytes(Paths.get(inputFile)));
            Huffman.CompressionResult result = Huffman.compressDNA(content);

            saveToFile(result.compressedData, outputFile);
            System.out.println("Porcentaje de compresión: " + result.compressionPercentage + "%");

            System.out.println("Códigos de Huffman para ADN y frecuencias:");
            for (Map.Entry<Character, String> entry : result.huffmanCodes.entrySet()) {
                char character = entry.getKey();
                String code = entry.getValue();
                int frequency = result.frequencies.get(character);
                System.out
                        .println("Carácter: " + character + ", Frecuencia: " + frequency + ", Código Huffman: " + code);
            }

        } catch (IOException e) {
            System.err.println("Error al leer o escribir archivos: " + e.getMessage());
        }
    }

    private static void compressImage(Scanner scanner) {
        // ... (Este método permanece igual que en el código anterior)
    }

    private static void saveToFile(String content, String outputFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(content);
        }
        System.out.println("Archivo comprimido guardado en " + outputFile);
    }
}
