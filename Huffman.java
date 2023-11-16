import java.io.Serializable;
import java.util.*;

public class Huffman {

    // Clase interna para representar un nodo en el árbol de Huffman
    static class Node implements Comparable<Node>, Serializable {
        char character; // el carácter del nodo
        int frequency; // la frecuencia de aparición del carácter
        Node left = null, right = null; // nodos hijo izquierdo y derecho

        // Constructor para hojas del árbol
        Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        // Constructor para nodos internos (no hojas)
        Node(Node left, Node right) {
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
        }

        // Comparar nodos por frecuencia para la cola de prioridad
        @Override
        public int compareTo(Node that) {
            return this.frequency - that.frequency;
        }
    }

    // Método para construir el árbol de Huffman a partir de una cadena de datos
    protected static Node buildTree(String data) {
        // Contar la frecuencia de cada carácter
        Map<Character, Integer> frequency = new HashMap<>();
        for (char character : data.toCharArray()) {
            // Incrementar la frecuencia del carácter
            frequency.put(character, frequency.getOrDefault(character, 0) + 1);
        }

        // Crear una cola de prioridad para construir el árbol de Huffman
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            // Agregar cada carácter como un nodo hoja en la cola de prioridad
            priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }

        // Construir el árbol combinando los nodos con menor frecuencia
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            // Crear un nodo interno con los nodos izquierdo y derecho
            priorityQueue.add(new Node(left, right));
        }

        // Devolver la raíz del árbol
        return priorityQueue.poll();
    }

    // Método para generar los códigos Huffman a partir del árbol
    protected static Map<Character, String> generateCodes(Node root) {
        // Recorrer el árbol de Huffman para generar los códigos
        Map<Character, String> huffmanCode = new HashMap<>();
        // Llamar al método recursivo para generar los códigos
        encode(root, "", huffmanCode);
        return huffmanCode;
    }

    // Método recursivo para asignar un código binario a cada carácter
    private static void encode(Node node, String str, Map<Character, String> huffmanCode) {
        if (node != null) {
            // Si es una hoja, asignar el código acumulado
            if (node.left == null && node.right == null) {
                huffmanCode.put(node.character, str);
            }
            // Recorrer el árbol hacia la izquierda y derecha
            encode(node.left, str + "0", huffmanCode);
            encode(node.right, str + "1", huffmanCode);
        }
    }

    // Método para codificar una cadena de datos usando los códigos de Huffman
    public static String encode(String data, Map<Character, String> huffmanCodes) {
        StringBuilder builder = new StringBuilder();
        for (char character : data.toCharArray()) {
            // Concatenar el código de Huffman para cada carácter
            builder.append(huffmanCodes.get(character));
        }
        return builder.toString();
    }

    // Método para comprimir un texto usando Huffman
    public static CompressionResult compressText(String text) {
        // Contar la frecuencia de cada carácter
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char ch : text.toCharArray()) {
            // Incrementar la frecuencia del carácter
            frequencies.put(ch, frequencies.getOrDefault(ch, 0) + 1);
        }
        // Construir el árbol de Huffman
        Node root = buildTree(text);
        // Generar los códigos de Huffman
        Map<Character, String> huffmanCodes = generateCodes(root);
        // Codificar el texto usando los códigos de Huffman
        String encodedText = encode(text, huffmanCodes);
        return new CompressionResult(encodedText, huffmanCodes, calculateCompressionPercentage(text, encodedText),
                frequencies);
    }

    // Método para comprimir ADN usando Huffman
    public static CompressionResult compressDNA(String dna) {
        // Filtrar los caracteres que no sean A, C, G o T
        String filteredDNA = dna.replaceAll("[^ACGT]", "");
        // Contar la frecuencia de cada carácter
        Map<Character, Integer> frequencies = new HashMap<>();
        // Incrementar la frecuencia del carácter
        for (char ch : filteredDNA.toCharArray()) {
            frequencies.put(ch, frequencies.getOrDefault(ch, 0) + 1);
        }
        // Construir el árbol de Huffman
        Node root = buildTree(filteredDNA);
        // Generar los códigos de Huffman
        Map<Character, String> huffmanCodes = generateCodes(root);
        // Codificar el texto usando los códigos de Huffman
        String encodedDNA = encode(filteredDNA, huffmanCodes);

        return new CompressionResult(encodedDNA, huffmanCodes, calculateCompressionPercentage(filteredDNA, encodedDNA),
                frequencies);
    }

    // Método para calcular el porcentaje de compresión
    private static double calculateCompressionPercentage(String original, String compressed) {
        return (1 - ((double) original.length() / compressed.length())) * 100;
    }

    // Clase interna para almacenar el resultado de la compresión
    static class CompressionResult {
        // Atributos para almacenar los datos de la compresión
        String compressedData;
        // Mapa para almacenar los códigos de Huffman para cada carácter
        Map<Character, String> huffmanCodes;
        double compressionPercentage;
        // Mapa para almacenar las frecuencias de cada carácter
        Map<Character, Integer> frequencies;

        // Constructor para inicializar los datos del resultado
        CompressionResult(String compressedData, Map<Character, String> huffmanCodes, double compressionPercentage,
                Map<Character, Integer> frequencies) {
            this.compressedData = compressedData;
            this.huffmanCodes = huffmanCodes;
            this.compressionPercentage = compressionPercentage;
            this.frequencies = frequencies;
        }
    }
}
