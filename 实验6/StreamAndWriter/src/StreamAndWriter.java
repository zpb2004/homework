import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * @author HP
 */
public class StreamAndWriter {
    public static void main(String[] args) throws IOException {
        int numberOfDoubles = 10000000; // 1千万个数据
        String dataOutputFile = "dataOutputFile.txt"; // 用于DataOutputStream的文件
        String outputStreamFile = "outputStreamWriterFile.txt"; // 用于OutputStreamWriter的文件
        // 使用 DataOutputStream 写入数据
        long startTime = System.currentTimeMillis(); // 记录开始时间
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(dataOutputFile))) {
            for (int i = 0; i < numberOfDoubles; i++) {
                dos.writeDouble(Math.random()); // 向文件写入随机生成的double类型数据
            }
        }
        long endTime = System.currentTimeMillis(); // 记录结束时间
        System.out.println("DataOutputStream Write Time: " + (endTime - startTime) + " ms"); // 打印写入时间
        // 使用 OutputStreamWriter 写入数据
        startTime = System.currentTimeMillis(); // 重新记录开始时间
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outputStreamFile))) {
            for (int i = 0; i < numberOfDoubles; i++) {
                osw.write(Double.toString(Math.random()) + "\\n"); // 将随机生成的double类型数据转换为字符串并写入文件，每个数据后添加换行符
            }
        }
        endTime = System.currentTimeMillis(); // 记录结束时间
        System.out.println("OutputStreamWriter Write Time: " + (endTime - startTime) + " ms"); // 打印写入时间
        // 使用 DataInputStream 读取数据
        startTime = System.currentTimeMillis(); // 重新记录开始时间
        try (DataInputStream dis = new DataInputStream(new FileInputStream(dataOutputFile))) {
            for (int i = 0; i < numberOfDoubles; i++) {
                double data = dis.readDouble(); // 从文件中读取double类型数据
            }
        }
        endTime = System.currentTimeMillis(); // 记录结束时间
        System.out.println("DataInputStream Read Time: " + (endTime - startTime) + " ms"); // 打印读取时间
        // 使用 InputStreamReader 读取数据
        startTime = System.currentTimeMillis(); // 重新记录开始时间
        try (Scanner scanner = new Scanner(new File(outputStreamFile))) {
            while (scanner.hasNextDouble()) {
                double data = scanner.nextDouble();
            }
        }
        endTime = System.currentTimeMillis(); // 记录结束时间
        System.out.println("InputStreamReader Read Time: " + (endTime - startTime) + " ms"); // 打印读取时间
    }
}
