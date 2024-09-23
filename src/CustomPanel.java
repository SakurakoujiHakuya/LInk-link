import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomPanel extends JPanel {

    public CustomPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] data = {"第一行数据", "第二行数据", "第三行数据"};
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE}; // 每行数据的背景颜色

        for (int i = 0; i < data.length; i++) {
            JLabel label = new JLabel(data[i]);
            label.setFont(new Font("Arial", Font.PLAIN, 20)); // 设置字体大小
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 设置边框
            label.setBackground(colors[i]); // 设置背景颜色
            label.setOpaque(true); // 设置为不透明以显示背景颜色
            label.setPreferredSize(new Dimension(200, 30)); // 设置每行数据的大小
            label.setHorizontalAlignment(SwingConstants.CENTER); // 文本居中显示
            add(label);
        }

        // 创建按钮并添加到面板
        JButton surpassButton = new JButton("我会超过它们");
        surpassButton.setFont(new Font("Arial", Font.BOLD, 24)); // 设置按钮字体大小
        surpassButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(0, 10))); // 添加一些垂直间距
        add(surpassButton);

        // 按钮的点击事件
        surpassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理按钮点击事件
            }
        });
    }

    public static void main(String[] args) {
        // 创建窗口并添加CustomPanel
        JFrame frame = new JFrame("Custom Panel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new CustomPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
