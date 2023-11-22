import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DescompressMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nSelecciona una opción:");
            System.out.println("1. Descomprimir archivo de texto");
            System.out.println("2. Descomprimir cadena de ADN");
            System.out.println("3. Descomprimir imagen BMP");
            System.out.println("4. Salir");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume la nueva línea

            switch (choice) {
                case 1:
                    decompressText(scanner);
                    break;
                case 2:
                    decompressDNA(scanner);
                    break;
                case 3:
                    decompressImageBMP(scanner);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }

        scanner.close();
    }

    private static void decompressText(Scanner scanner) {

        String compressedFile = "Text/compressed_text.txt"; // Archivo en Base64 o ASCII
        String huffmanFile = "Text/huffman_text.txt"; // Archivo con frecuencias de Huffman
        String asciiFile = "Text/ascii_key.txt"; // Archivo con clave ASCII
        String binaryOutputFile = "Text/binary_output.txt"; // Salida de archivo binario
        String outputFile = "Text/decompressed_output.txt"; // Archivo de salida descomprimido

        try {
            // Leer la clave ASCII y las frecuencias
            Map<Character, String> asciiKey = readAsciiKey(asciiFile);
            Map<Character, Integer> frequencies = readFrequencies(huffmanFile);

            // Reconstruir el árbol de Huffman
            Huffman.Node root = rebuildHuffmanTree(frequencies);

            // Convertir el archivo comprimido a texto binario
            convertCompressedToBinary(compressedFile, binaryOutputFile, asciiKey);

            // Descomprimir el archivo binario
            decompressBinaryFile(binaryOutputFile, root, outputFile);
            System.out.println("Texto descomprimido guardado en: " + outputFile);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Lee la clave ASCII desde un archivo y la almacena en un mapa
    private static Map<Character, String> readAsciiKey(String filename) throws IOException {
        Map<Character, String> asciiKey = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" = ");
                if (parts.length == 2) {
                    char asciiChar = parts[0].charAt(1);
                    String binaryCode = parts[1];
                    asciiKey.put(asciiChar, binaryCode);
                }
            }
        }
        return asciiKey;
    }

    // Convierte el contenido del archivo comprimido a texto binario y lo guarda en
    // un archivo
    private static void convertCompressedToBinary(String inputFile, String outputFile, Map<Character, String> asciiKey)
            throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                char asciiChar = (char) ch;
                String binaryCode = asciiKey.getOrDefault(asciiChar, "");
                writer.write(binaryCode);
            }
        }
    }

    // Reconstruye el árbol de Huffman utilizando la tabla de frecuencias
    private static Huffman.Node rebuildHuffmanTree(Map<Character, Integer> frequencies) {
        return Huffman.buildTree(frequencies);
    }

    // Lee y descomprime el archivo binario
    private static void decompressBinaryFile(String binaryFile, Huffman.Node root, String outputFile)
            throws IOException {
        StringBuilder binaryText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(binaryFile))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                binaryText.append((char) ch);
            }
        }
        String originalText = Huffman.decode(root, binaryText.toString());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(originalText);
        }
    }

    // Implementa readFrequencies, que lee la tabla de frecuencias desde un archivo
    private static Map<Character, Integer> readFrequencies(String filename) throws IOException {
        Map<Character, Integer> frequencies = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // Omitir la cabecera si existe
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    char character = getCharFromRepresentation(parts[0]);
                    int frequency = Integer.parseInt(parts[1]);
                    frequencies.put(character, frequency);
                }
            }
        }
        return frequencies;
    }

    private static char getCharFromRepresentation(String representation) {
        switch (representation) {
            case "\\n":
                return '\n';
            case "\\r":
                return '\r';
            case "\\t":
                return '\t';
            case "[espacio]":
                return ' ';
            // Agrega más casos si es necesario
            default:
                return representation.charAt(0); // Asumiendo que es un carácter común
        }
    }

    private static void decompressDNA(Scanner scanner) {
        // Definir los archivos de entrada y salida
        String compressedFile = "ADN/ascii_key.txt"; // Archivo comprimido en Base64 o ASCII
        String huffmanFile = "ADN/huffman_dna.txt"; // Archivo con frecuencias de Huffman
        String asciiFile = "ADN/ascii_key.txt"; // Archivo con clave ASCII
        String binaryOutputFile = "ADN/binary_dna_output.txt"; // Salida de archivo binario
        String outputFile = "ADN/decompressed_dna.txt"; // Archivo de salida descomprimido

        try {
            // Leer la clave ASCII y las frecuencias
            Map<Character, String> asciiKey = readAsciiKey(asciiFile);
            Map<Character, Integer> frequencies = readFrequencies(huffmanFile);

            // Reconstruir el árbol de Huffman
            Huffman.Node root = rebuildHuffmanTree(frequencies);

            // Convertir el archivo comprimido a texto binario
            convertCompressedToBinary(compressedFile, binaryOutputFile, asciiKey);

            // Descomprimir el archivo binario
            decompressBinaryFile(binaryOutputFile, root, outputFile);
            System.out.println("ADN descomprimido guardado en: " + outputFile);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void decompressImageBMP(Scanner scanner) {
        // Definir los archivos de entrada y salida
        String compressedFile = "Bmp/ascii_key.txt"; // Archivo comprimido en Base64 o ASCII
        String huffmanFile = "Bmp/huffman_image.txt"; // Archivo con frecuencias de Huffman
        String asciiFile = "Bmp/ascii_key.txt"; // Archivo con clave ASCII
        String binaryOutputFile = "Bmp/binary_image_output.txt"; // Salida de archivo binario
        String outputFile = "Bmp/decompressed_image.bmp"; // Archivo de imagen descomprimido en formato BMP

        try {
            // Leer la clave ASCII y las frecuencias
            Map<Character, String> asciiKey = readAsciiKey(asciiFile);
            Map<Character, Integer> frequencies = readFrequencies(huffmanFile);

            // Reconstruir el árbol de Huffman
            Huffman.Node root = rebuildHuffmanTree(frequencies);

            // Convertir el archivo comprimido a texto binario
            convertCompressedToBinary(compressedFile, binaryOutputFile, asciiKey);

            // Descomprimir el archivo binario
            decompressBinaryFile(binaryOutputFile, root, outputFile);
            System.out.println("Imagen BMP descomprimida guardada en: " + outputFile);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}