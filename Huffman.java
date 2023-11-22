import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Huffman {

    // Clase interna que representa un nodo en el árbol de Huffman.
    // Implementa Comparable para la cola de prioridad y Serializable para la
    // serialización de objetos.
    static class Node implements Comparable<Node>, Serializable {
        char character; // Carácter almacenado en el nodo (para nodos hoja)
        int frequency; // Frecuencia del carácter
        Node left = null, right = null; // Nodos hijos izquierdo y derecho

        // Constructor para nodos hoja
        Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        // Constructor para nodos internos
        Node(Node left, Node right) {
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
        }

        // Comparación de nodos por frecuencia
        @Override
        public int compareTo(Node that) {
            return this.frequency - that.frequency;
        }
    }

    // Este método ya existe en tu implementación actual, pero es crucial para la
    // descompresión
    public static Node buildTree(Map<Character, Integer> frequency) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            Node parent = new Node(left, right); // Suponiendo que tienes este constructor
            priorityQueue.add(parent);
        }
        return priorityQueue.poll();
    }

    // Genera los códigos de Huffman a partir del árbol de Huffman
    protected static Map<Character, String> generateCodes(Node root) {
        // Mapa para almacenar los códigos de Huffman
        Map<Character, String> huffmanCode = new HashMap<>();
        // Recorre el árbol de Huffman para generar los códigos
        encode(root, "", huffmanCode);
        return huffmanCode;
    }

    // Recorre el árbol de Huffman para generar los códigos
    private static void encode(Node node, String str, Map<Character, String> huffmanCode) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                // Nodo hoja: almacena el carácter y su código
                huffmanCode.put(node.character, str);
            }
            encode(node.left, str + "0", huffmanCode);
            encode(node.right, str + "1", huffmanCode);
        }
    }

    // Codifica el texto a partir de los códigos de Huffman
    public static String encode(String data, Map<Character, String> huffmanCodes) {
        // Codifica el texto a partir de los códigos de Huffman
        StringBuilder builder = new StringBuilder();
        // Recorre el texto y concatena los códigos de Huffman
        for (char character : data.toCharArray()) {
            builder.append(huffmanCodes.get(character));
        }
        return builder.toString();
    }

    // Decodifica el texto a partir del árbol de Huffman
    private static Map<Character, Integer> calculateFrequencies(String data) {
        // Calcula las frecuencias de los caracteres
        Map<Character, Integer> frequency = new HashMap<>();
        // Recorre el texto y cuenta las frecuencias de los caracteres
        for (char character : data.toCharArray()) {
            frequency.put(character, frequency.getOrDefault(character, 0) + 1);
        }
        return frequency;
    }

    // Decodifica el texto a partir del árbol de Huffman
    public static CompressionResult compressText(String text) {
        // Calcula las frecuencias de los caracteres
        Map<Character, Integer> frequencies = calculateFrequencies(text);
        // Construye el árbol de Huffman
        Node root = buildTree(frequencies);
        // Genera los códigos de Huffman
        Map<Character, String> huffmanCodes = generateCodes(root);
        // Codifica el texto a partir de los códigos de Huffman
        String encodedText = encode(text, huffmanCodes);
        // Subdivide y simboliza el texto binario
        String symbolizedText = subdivideAndSymbolize(encodedText);

        // Retorna el resultado de la compresión con el texto simbolizado
        return new CompressionResult(symbolizedText, huffmanCodes, calculateCompressionPercentage(text, symbolizedText),
                frequencies);
    }

    public static String subdivideAndSymbolize(String binaryText) {
        StringBuilder symbolizedText = new StringBuilder();
        for (int i = 0; i < binaryText.length(); i += 8) {
            String byteSegment = binaryText.substring(i, Math.min(i + 8, binaryText.length()));
            byteSegment = String.format("%-8s", byteSegment).replace(' ', '0');
            int byteValue = Integer.parseInt(byteSegment, 2);
            char asciiChar = (char) byteValue;
            symbolizedText.append(asciiChar);
        }
        return symbolizedText.toString();
    }

    public static void generateAndSaveAsciiKey(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < 256; i++) {
                char asciiChar = (char) i;
                String charRepresentation = getCharRepresentation(asciiChar);
                String binaryString = String.format("%8s", Integer.toBinaryString(i)).replace(' ', '0');
                writer.write(charRepresentation + " = " + binaryString + "\n");
            }
        }
    }

    private static String getCharRepresentation(char asciiChar) {
        switch (asciiChar) {
            case '\n':
                return "'\\n'";
            case '\r':
                return "'\\r'";
            case '\t':
                return "'\\t'";
            // Agrega más casos según sea necesario
            default:
                return "'" + asciiChar + "'";
        }
    }

    // Comprime datos de ADN (representados como una cadena de bases nitrogenadas)
    public static CompressionResult compressDNA(String dna) {
        // Filtra las bases nitrogenadas inválidas
        String filteredDNA = dna.replaceAll("[^ACGT]", "");

        // Calcula las frecuencias de las bases nitrogenadas
        Map<Character, Integer> frequencies = calculateFrequencies(filteredDNA);

        // Construye el árbol de Huffman
        Node root = buildTree(frequencies);

        // Genera los códigos de Huffman
        Map<Character, String> huffmanCodes = generateCodes(root);

        // Codifica el texto a partir de los códigos de Huffman
        String encodedDNA = encode(filteredDNA, huffmanCodes);

        // Subdivide y simboliza el texto binario
        String symbolizedDNA = subdivideAndSymbolize(encodedDNA);

        // Retorna el resultado de la compresión con el texto simbolizado
        return new CompressionResult(symbolizedDNA, huffmanCodes,
                calculateCompressionPercentage(filteredDNA, symbolizedDNA), frequencies);
    }

    // Comprime imágenes BMP en escala de grises
    public static CompressionResult compressGrayscaleImage(String imageData) {
        // Calcula las frecuencias de los niveles de gris
        Map<Character, Integer> frequencies = calculateFrequencies(imageData);

        // Construye el árbol de Huffman
        Node root = buildTree(frequencies);

        // Genera los códigos de Huffman
        Map<Character, String> huffmanCodes = generateCodes(root);

        // Codifica el texto a partir de los códigos de Huffman
        String encodedImage = encode(imageData, huffmanCodes);

        // Subdivide y simboliza el texto binario
        String symbolizedImage = subdivideAndSymbolize(encodedImage);

        // Retorna el resultado de la compresión con el texto simbolizado
        return new CompressionResult(symbolizedImage, huffmanCodes,
                calculateCompressionPercentage(imageData, symbolizedImage), frequencies);
    }

    // Calcula el porcentaje de compresión
    private static double calculateCompressionPercentage(String original, String compressed) {
        return ((original.length() - compressed.length()) / original.length()) * 100;
    }

    // Clase interna para almacenar el resultado de la compresión
    static class CompressionResult implements Serializable {
        String compressedData;
        // Mapa para almacenar los códigos de Huffman
        Map<Character, String> huffmanCodes;
        // Porcentaje de compresión
        double compressionPercentage;
        // Mapa para almacenar las frecuencias de los caracteres
        Map<Character, Integer> frequencies;

        // Constructor
        CompressionResult(String compressedData, Map<Character, String> huffmanCodes, double compressionPercentage,
                Map<Character, Integer> frequencies) {
            this.compressedData = compressedData;
            this.huffmanCodes = huffmanCodes;
            this.compressionPercentage = compressionPercentage;
            this.frequencies = frequencies;
        }
    }

    public static String decode(Node root, String binaryText) {
        StringBuilder decodedText = new StringBuilder();
        Node current = root;
        for (int i = 0; i < binaryText.length(); i++) {
            current = binaryText.charAt(i) == '0' ? current.left : current.right;
            if (current.left == null && current.right == null) {
                decodedText.append(current.character);
                current = root;
            }
        }
        return decodedText.toString();
    }

    // Método para imprimir el árbol de Huffman
    public static void printTree(Node root) {
        printTreeHelper(root, "");
    }

    // Método auxiliar para imprimir el árbol
    private static void printTreeHelper(Node node, String indent) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                // Nodo hoja
                System.out.println(indent + "Char: " + node.character + " Freq: " + node.frequency);
            } else {
                // Nodo interno
                System.out.println(indent + "Freq: " + node.frequency);
                // Recorre el subárbol izquierdo y derecho
                printTreeHelper(node.left, indent + "    ");
                printTreeHelper(node.right, indent + "    ");
            }
        }
    }

}