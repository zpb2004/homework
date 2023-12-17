import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author HP
 */
public class Notepad extends JFrame {
    private final JTextArea textArea;
    private final JFileChooser fileChooser;
    private boolean textChanged = false; // 标记文本是否被修改

    public Notepad() {
        // 窗口设置
        setTitle("记事本");
        setSize(800, 600);
        setLocationRelativeTo(null); // 窗口居中
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 创建菜单
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件(F)");
        JMenuItem newItem = new JMenuItem("新建(N)");
        JMenuItem openItem = new JMenuItem("打开(O)...");
        JMenuItem saveItem = new JMenuItem("保存(S)");
        JMenuItem exitItem = new JMenuItem("退出(X)");
        JMenuItem fontItem = new JMenuItem("字体(F)...");
        fileMenu.add(fontItem);
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        // 创建工具栏
        JToolBar toolBar = new JToolBar();
        JButton newButton = null;
        JButton openButton = null;
        JButton saveButton = null;
        try {
            Image newImg = ImageIO.read(new File("E:\\java\\Text\\src\\img\\new.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            newButton = new JButton(new ImageIcon(newImg));
            toolBar.add(newButton);

            Image openImg = ImageIO.read(new File("E:\\java\\Text\\src\\img\\open.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            openButton = new JButton(new ImageIcon(openImg));
            toolBar.add(openButton);

            Image saveImg = ImageIO.read(new File("E:\\java\\Text\\src\\img\\save.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            saveButton = new JButton(new ImageIcon(saveImg));
            toolBar.add(saveButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
        add(toolBar, BorderLayout.NORTH);

        // 创建文本区域
        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        // 添加键盘监听器来检测文本更改
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                textChanged = true;
            }
        });

        // 窗口监听器
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (textChanged) {
                    int option = JOptionPane.showConfirmDialog(
                            Notepad.this,
                            "您的文件尚未保存，是否保存？",
                            "保存文件？",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    switch (option) {
                        case JOptionPane.YES_OPTION:
                            saveFile(); // 如果用户选择保存，则保存文件
                            if (!textChanged) {
                                dispose(); // 关闭窗口
                            }
                            break;
                        case JOptionPane.NO_OPTION:
                            dispose(); // 不保存直接关闭窗口
                            break;
                        // 如果用户取消，不做任何事情，保持窗口打开状态
                        case JOptionPane.CANCEL_OPTION:
                        case JOptionPane.CLOSED_OPTION:
                            break;
                    }
                } else {
                    dispose(); // 如果没有更改，直接关闭
                }
            }
        });
        // 文件选择器
        fileChooser = new JFileChooser();
        fontItem.addActionListener(e -> chooseFont());

        // 事件监听
        newItem.addActionListener(e -> textArea.setText(""));
        if (newButton != null) {
            newButton.addActionListener(e -> textArea.setText(""));
        }

        openItem.addActionListener(e -> openFile());
        if (openButton != null) {
            openButton.addActionListener(e -> openFile());
        }

        saveItem.addActionListener(e -> saveFile());
        if (saveButton != null) {
            saveButton.addActionListener(e -> saveFile());
        }

        exitItem.addActionListener(e -> System.exit(0));
    }

    private void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                textArea.read(reader, null);
                reader.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "文件打开失败");
            }
        }
    }

    private void saveFile() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                textArea.write(writer);
                writer.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "文件保存失败");
            }
        }
    }

    private void chooseFont() {
        FontChooser fontChooser = new FontChooser(this, textArea.getFont());
        fontChooser.setVisible(true);
        Font selectedFont = fontChooser.getSelectedFont();
        if (selectedFont != null) {
            textArea.setFont(selectedFont);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Notepad notepad = new Notepad();
            notepad.setVisible(true);
        });
    }
}
class FontChooser extends JDialog {
    private JComboBox<String> fontNameComboBox;
    private JComboBox<Integer> fontSizeComboBox;
    private Font selectedFont;
    private JButton okButton;
    private JButton cancelButton;

    public FontChooser(JFrame parent, Font currentFont) {
        super(parent, "字体选择", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 字体名称标签和下拉框
        JLabel fontNameLabel = new JLabel("字体名称:");
        fontNameComboBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontNameComboBox.setSelectedItem(currentFont.getName());
        gbc.insets = new Insets(4, 4, 4, 4); // 设置组件之间的间距

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(fontNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(fontNameComboBox, gbc);

        // 字体大小标签和下拉框
        JLabel fontSizeLabel = new JLabel("字体大小:");
        Integer[] sizes = {8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
        fontSizeComboBox = new JComboBox<>(sizes);
        fontSizeComboBox.setSelectedItem(currentFont.getSize());

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(fontSizeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(fontSizeComboBox, gbc);

        // 确定按钮
        okButton = new JButton("确定");
        okButton.addActionListener(e -> {
            String fontName = (String) fontNameComboBox.getSelectedItem();
            int fontSize = (Integer) fontSizeComboBox.getSelectedItem();
            selectedFont = new Font(fontName, Font.PLAIN, fontSize);
            setVisible(false);
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(okButton, gbc);

        // 取消按钮
        cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> {
            selectedFont = null;
            setVisible(false);
        });
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(cancelButton, gbc);

        pack(); // 自动调整窗口大小
        setLocationRelativeTo(parent);
    }

    public Font getSelectedFont() {
        return selectedFont;
    }
}
