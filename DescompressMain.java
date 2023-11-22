import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DescompressMain {

    public static void main(String[] args) {
        String asciiKeyFile = "Text/ascii_key.txt";
        String compressedFile = "out.txt";
        String binaryOutputFile = "chi.txt";

        try {
            Map<Character, String> asciiKey = readAsciiKey(asciiKeyFile);
            convertCompressedToBinary(compressedFile, binaryOutputFile, asciiKey);
            System.out.println("Texto binario guardado en: " + binaryOutputFile);

            // Suponiendo que tienes un método para leer la tabla de frecuencias
            Map<Character, Integer> frequencies = readFrequencies("Text/huffman_text.txt");
            Huffman.Node root = rebuildHuffmanTree(frequencies);
            decompressBinaryFile(binaryOutputFile, root, "decompressed_output.txt");
            System.out.println("Texto descomprimido guardado en: decompressed_output.txt");
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

}
