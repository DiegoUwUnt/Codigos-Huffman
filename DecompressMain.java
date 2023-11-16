
/*
 * Chi
 * import java.io.*;
 * import java.nio.file.Files;
 * import java.nio.file.Paths;
 * 
 * public class DecompressMain {
 * 
 * public static void main(String[] args) {
 * String inputFile = "salida.txt"; // Nombre del archivo comprimido
 * String outputFile = "textoDescomprimido.txt"; // Nombre del archivo de salida
 * para el texto descomprimido
 * 
 * try {
 * // Leer el contenido comprimido y el árbol de Huffman desde el archivo de
 * // entrada
 * String encodedContent;
 * Huffman.Node root;
 * try (FileInputStream fis = new FileInputStream(inputFile);
 * ObjectInputStream ois = new ObjectInputStream(fis)) {
 * encodedContent = (String) ois.readObject();
 * root = (Huffman.Node) ois.readObject();
 * }
 * 
 * // Descomprimir el contenido
 * String decodedContent = Huffman.decode(encodedContent, root);
 * 
 * // Guardar el contenido descomprimido en el archivo de salida
 * Files.write(Paths.get(outputFile), decodedContent.getBytes());
 * 
 * System.out.println("Archivo descomprimido guardado en " + outputFile);
 * 
 * } catch (IOException | ClassNotFoundException e) {
 * System.err.println("Error al procesar el archivo: " + e.getMessage());
 * }
 * }
 * }
 */