import java.io.*;
import java.util.*;

/**
 * @author HP
 */
public class MyTxtReader {
    public static void main(String[] args) throws IOException {
        // 读取文件
        List<Student> students = readStudentsFromFile("班级名单.txt");
        // 排序
        Collections.sort(students);
        // 保存排序后的名单
        saveStudentsToFile(students, "班级名单-sorted.txt");
        // 用户输入学号，查找学生名字
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入学号（输入 'exit' 退出）：");
            String id = scanner.nextLine();
            if ("exit".equalsIgnoreCase(id)) {
                break; // 用户输入 exit 时退出循环
            }
            String name = findStudentName(students, id);
            if (name != null) {
                System.out.println("学生名字：" + name);
            } else {
                System.out.println("未找到该学号的学生。");
            }
        }
        scanner.close();
        System.out.println("程序结束。");
    }
    private static List<Student> readStudentsFromFile(String fileName) throws IOException {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 2) {
                    String originalId = parts[0];
                    String numericId = originalId.replaceAll("\\D", ""); // 移除非数字字符
                    String name = line.substring(line.indexOf(parts[1])); // 获取包含名字和可能的其他信息的字符串
                    students.add(new Student(numericId, originalId, name));
                }
            }
        }
        return students;
    }
    private static void saveStudentsToFile(List<Student> students, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Student student : students) {
                writer.write(student.getOriginalId() + " " + student.getName());
                writer.newLine();
            }
        }
    }
    private static String findStudentName(List<Student> students, String id) {
        String numericId = id.replaceAll("\\D", ""); // 移除输入学号中的非数字字符
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(numericId)) { // 与去除字母后的学号比较
                return student.getName();
            }
        }
        return null;
    }
    static class Student implements Comparable<Student> {
        private final String id; // 存储去除字母后的学号
        private final String originalId; // 存储原始学号，包括 "L"
        private final String name;
        public Student(String id, String originalId, String name) {
            this.id = id;
            this.originalId = originalId;
            this.name = name;
        }
        public String getId() {
            return id;
        }
        public String getOriginalId() {
            return originalId;
        }
        public String getName() {
            return name;
        }
        @Override
        public int compareTo(Student other) {
            // 比较时只比较数字部分
            return this.id.compareTo(other.id);
        }
    }
}