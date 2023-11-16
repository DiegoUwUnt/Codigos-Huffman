import java.io.Serializable;
import java.util.*;

public class Huffman {

    static class Node implements Comparable<Node>, Serializable {
        char character;
        int frequency;
        Node left = null, right = null;

        Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        Node(Node left, Node right) {
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node that) {
            return this.frequency - that.frequency;
        }
    }

    protected static Node buildTree(String data) {
        Map<Character, Integer> frequency = new HashMap<>();
        for (char character : data.toCharArray()) {
            frequency.put(character, frequency.getOrDefault(character, 0) + 1);
        }

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            priorityQueue.add(new Node(left, right));
        }

        return priorityQueue.poll();
    }

    protected static Map<Character, String> generateCodes(Node root) {
        Map<Character, String> huffmanCode = new HashMap<>();
        encode(root, "", huffmanCode);
        return huffmanCode;
    }

    private static void encode(Node node, String str, Map<Character, String> huffmanCode) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                huffmanCode.put(node.character, str);
            }
            encode(node.left, str + "0", huffmanCode);
            encode(node.right, str + "1", huffmanCode);
        }
    }

    public static String encode(String data, Map<Character, String> huffmanCodes) {
        StringBuilder builder = new StringBuilder();
        for (char character : data.toCharArray()) {
            builder.append(huffmanCodes.get(character));
        }
        return builder.toString();
    }

    public static CompressionResult compressText(String text) {
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char ch : text.toCharArray()) {
            frequencies.put(ch, frequencies.getOrDefault(ch, 0) + 1);
        }

        Node root = buildTree(text);
        Map<Character, String> huffmanCodes = generateCodes(root);
        String encodedText = encode(text, huffmanCodes);

        return new CompressionResult(encodedText, huffmanCodes, calculateCompressionPercentage(text, encodedText),
                frequencies);
    }

    public static CompressionResult compressDNA(String dna) {
        String filteredDNA = dna.replaceAll("[^ACGT]", "");
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char ch : filteredDNA.toCharArray()) {
            frequencies.put(ch, frequencies.getOrDefault(ch, 0) + 1);
        }

        Node root = buildTree(filteredDNA);
        Map<Character, String> huffmanCodes = generateCodes(root);
        String encodedDNA = encode(filteredDNA, huffmanCodes);

        return new CompressionResult(encodedDNA, huffmanCodes, calculateCompressionPercentage(filteredDNA, encodedDNA),
                frequencies);
    }

    private static double calculateCompressionPercentage(String original, String compressed) {
        return (1 - ((double) original.length() / compressed.length())) * 100;
    }

    static class CompressionResult {
        String compressedData;
        Map<Character, String> huffmanCodes;
        double compressionPercentage;
        Map<Character, Integer> frequencies;

        CompressionResult(String compressedData, Map<Character, String> huffmanCodes, double compressionPercentage,
                Map<Character, Integer> frequencies) {
            this.compressedData = compressedData;
            this.huffmanCodes = huffmanCodes;
            this.compressionPercentage = compressionPercentage;
            this.frequencies = frequencies;
        }
    }
}
