import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class CompressMain {

    // Método principal
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // Ciclo principal para mostrar el menú y procesar la entrada del usuario
        while (!exit) {
            // Mostrar opciones del menú
            System.out.println("\nSelecciona una opción:");
            System.out.println("1. Salir");
            System.out.println("2. Comprimir texto");
            System.out.println("3. Comprimir ADN");
            System.out.println("4. Comprimir imágenes");

            // Leer la opción del usuario
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume la nueva línea después del número

            // Procesar la opción seleccionada
            switch (choice) {
                case 1:
                    exit = true; // Salir del programa
                    break;
                case 2:
                    compressText(scanner); // Comprimir texto
                    break;
                case 3:
                    compressDNA(scanner); // Comprimir ADN
                    break;
                case 4:
                    compressImage(scanner); // Comprimir imágenes
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intenta de nuevo.");
            }
        }

        scanner.close(); // Cerrar el scanner al finalizar
    }

    // Método para comprimir texto
    private static void compressText(Scanner scanner) {
        // Solicitar nombre del archivo de texto al usuario
        System.out.println("Ingresa el nombre del archivo de texto a comprimir:");
        String inputFile = scanner.nextLine();
        // Solicitar nombre del archivo de salida
        System.out.println("Ingresa el nombre del archivo de salida:");
        String outputFile = scanner.nextLine();

        try {
            // Leer el contenido del archivo
            String content = new String(Files.readAllBytes(Paths.get(inputFile)));
            // Comprimir el contenido usando Huffman
            Huffman.CompressionResult result = Huffman.compressText(content);

            // Guardar el resultado en un archivo
            saveToFile(result.compressedData, outputFile);
            // Mostrar información de la compresión
            System.out.println("Porcentaje de compresión: " + result.compressionPercentage + "%");

            // Mostrar los códigos de Huffman y las frecuencias
            System.out.println("Códigos de Huffman y frecuencias:");
            for (Map.Entry<Character, String> entry : result.huffmanCodes.entrySet()) {
                // Obtener el carácter y el código de Huffman
                char character = entry.getKey();
                // Obtener el código de Huffman
                String code = entry.getValue();
                // Obtener la frecuencia del carácter
                int frequency = result.frequencies.get(character);
                System.out
                        .println("Carácter: " + character + ", Frecuencia: " + frequency + ", Código Huffman: " + code);
            }

        } catch (IOException e) {
            // Manejar errores de entrada/salida
            System.err.println("Error al leer o escribir archivos: " + e.getMessage());
        }
    }

    // Método para comprimir ADN (similar a compressText)
    private static void compressDNA(Scanner scanner) {
        // ... (El funcionamiento es similar al de compressText pero específico para
        // ADN)
    }

    // Método para comprimir imágenes (no implementado en este fragmento)
    private static void compressImage(Scanner scanner) {
        // ... (Este método permanece igual que en el código anterior)
    }

    // Método para guardar el contenido comprimido en un archivo
    private static void saveToFile(String content, String outputFile) throws IOException {
        // Crear el archivo de salida
        try (FileOutputStream fos = new FileOutputStream(outputFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(content);
        }
        System.out.println("Archivo comprimido guardado en " + outputFile);
    }
}
