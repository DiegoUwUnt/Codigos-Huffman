import java.io.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * Clase principal que maneja la interacción con el usuario y la compresión de
 * archivos de texto, ADN e imágenes BMP.
 * Utiliza la clase Huffman para realizar la compresión.
 */
public class CompressMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nSelecciona una opción:");
            System.out.println("1. Salir");
            System.out.println("2. Comprimir texto");
            System.out.println("3. Comprimir ADN");
            System.out.println("4. Comprimir imagen BMP");

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
                    compressImageBMP(scanner);
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }

        scanner.close();
    }

    // Método para comprimir texto, tomando entrada del usuario a través de Scanner
    private static void compressText(Scanner scanner) {
        System.out.println("Ingresa el nombre del archivo de texto a comprimir:");
        String inputFile = scanner.nextLine(); // Recibe el nombre del archivo de entrada
        System.out.println("Ingresa el nombre del archivo de salida:");
        String outputFile = scanner.nextLine(); // Recibe el nombre del archivo de salida

        try {
            // Lee el contenido del archivo
            String content = new String(Files.readAllBytes(Paths.get(inputFile)));

            // Comprime el texto utilizando la compresión Huffman y la subdivisión y
            // simbolización
            Huffman.CompressionResult result = Huffman.compressText(content);

            // Guarda el resultado de la compresión en un archivo
            saveCompressionResult(outputFile, result);

            // Muestra los resultados de la compresión
            displayCompressionResults(result);

            // Genera y guarda el archivo de clave ASCII
            Huffman.generateAndSaveAsciiKey("Text/ascii_key.txt");
            System.out.println("Archivo de clave ASCII generado: ascii_key.txt");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo o al generar la clave ASCII: " + e.getMessage());
        }
    }

    private static void compressDNA(Scanner scanner) {
        System.out.println("Ingresa el nombre del archivo de ADN a comprimir:");
        String inputFile = scanner.nextLine();
        System.out.println("Ingresa el nombre del archivo de salida:");
        String outputFile = scanner.nextLine();

        try {
            String content = new String(Files.readAllBytes(Paths.get(inputFile)));
            // Comprime el ADN
            Huffman.CompressionResult result = Huffman.compressDNA(content);
            // Guarda el resultado de la compresión en un archivo
            saveCompressionResult(outputFile, result);
            // Muestra los resultados de la compresión
            displayCompressionResults(result);

            // Genera y guarda el archivo de clave ASCII
            Huffman.generateAndSaveAsciiKey("ADN/ascii_key_dna.txt");
            System.out.println("Archivo de clave ASCII generado: ascii_key_dna.txt");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo o al generar la clave ASCII: " + e.getMessage());
        }
    }

    private static void compressImageBMP(Scanner scanner) {
        System.out.println("Ingresa el nombre del archivo de imagen BMP a comprimir:");
        String inputFile = scanner.nextLine();
        System.out.println("Ingresa el nombre del archivo de salida:");
        String outputFile = scanner.nextLine();

        try {
            String imageData = readImageBMP(inputFile);
            // Comprime la imagen BMP en escala de grises
            Huffman.CompressionResult result = Huffman.compressGrayscaleImage(imageData);
            // Guarda el resultado de la compresión en un archivo
            saveCompressionResult(outputFile, result);
            // Muestra los resultados de la compresión
            displayCompressionResults(result);

            // Genera y guarda el archivo de clave ASCII
            Huffman.generateAndSaveAsciiKey("Bmp/ascii_key_image.txt");
            System.out.println("Archivo de clave ASCII generado: ascii_key_image.txt");

        } catch (IOException e) {
            System.err.println("Error al leer o escribir el archivo: " + e.getMessage());
        }
    }

    // Guarda el resultado de la compresión en un archivo
    private static void saveCompressionResult(String outputFile, Huffman.CompressionResult result) throws IOException {
        // Guarda el resultado de la compresión en un archivo
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            // Escribe el objeto result en el archivo
            oos.writeObject(result);
        }
        System.out.println("Resultado de compresión guardado en " + outputFile);
    }

    // Muestra los resultados de la compresión
    private static void displayCompressionResults(Huffman.CompressionResult result) {
        System.out.println("Datos comprimidos: " + result.compressedData);
        System.out.println("Porcentaje de compresión: " + result.compressionPercentage + "%");
        System.out.println("Códigos de Huffman y frecuencias:");
        // Muestra los códigos de Huffman y las frecuencias de los caracteres
        for (Map.Entry<Character, String> entry : result.huffmanCodes.entrySet()) {
            // Obtiene el carácter, el código de Huffman y la frecuencia
            char character = entry.getKey();
            String code = entry.getValue();
            int frequency = result.frequencies.get(character);
            System.out.println("Carácter: " + character + ", Frecuencia: " + frequency + ", Código Huffman: " + code);
        }
    }

    // Lee una imagen BMP y la convierte a una cadena de caracteres
    private static String readImageBMP(String filename) throws IOException {
        try {
            // Lee la imagen BMP
            BufferedImage image = ImageIO.read(new File(filename));

            if (image == null) {
                // No se pudo leer el archivo o el formato no es compatible
                throw new IOException("No se pudo leer el archivo BMP o el formato no es compatible.");
            }
            // Convierte la imagen a escala de grises
            StringBuilder imageData = new StringBuilder();
            // Recorre la imagen por filas y columnas
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    int gray = (rgb >> 16) & 0xff;
                    char grayChar = (char) gray;
                    imageData.append(grayChar);
                }
            }

            return imageData.toString();
        } catch (Exception e) {
            throw new IOException("Error al leer el archivo BMP: " + e.getMessage(), e);
        }
    }
}
