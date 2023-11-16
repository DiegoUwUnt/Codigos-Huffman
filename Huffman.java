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

    // Construye el árbol de Huffman a partir de las frecuencias de los caracteres
    private static Node buildTree(Map<Character, Integer> frequency) {
        // Cola de prioridad para almacenar los nodos del árbol de Huffman
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }
        // Construye el árbol de Huffman
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            priorityQueue.add(new Node(left, right));
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
        return new CompressionResult(encodedText, huffmanCodes, calculateCompressionPercentage(text, encodedText),
                frequencies);
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
        return new CompressionResult(encodedDNA, huffmanCodes, calculateCompressionPercentage(filteredDNA, encodedDNA),
                frequencies);
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
        return new CompressionResult(encodedImage, huffmanCodes,
                calculateCompressionPercentage(imageData, encodedImage), frequencies);
    }

    // Calcula el porcentaje de compresión
    private static double calculateCompressionPercentage(String original, String compressed) {
        return (1 - (original.length() / (double) compressed.length())) * 100;
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
}
