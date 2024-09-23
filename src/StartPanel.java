import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    JButton easyButton = new JButton("简单");
    JButton ordinaryButton = new JButton("普通");
    JButton hardButton = new JButton("困难");
    JButton rankButton=new JButton("排行榜");
    JButton exitButton = new JButton("退出游戏");


    private ImageIcon backgroundImage;

    public StartPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 使用垂直布局

        // 设置按钮样式
        Font buttonFont = new Font("宋体", Font.PLAIN, 20); // 使用宋体字体
        easyButton.setFont(buttonFont);
        easyButton.setBackground(new Color(50, 205, 50));
        easyButton.setForeground(Color.white);
        easyButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        ordinaryButton.setFont(buttonFont);
        ordinaryButton.setBackground(new Color(255, 165, 0));
        ordinaryButton.setForeground(Color.white);
        ordinaryButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        hardButton.setFont(buttonFont);
        hardButton.setBackground(new Color(255, 69, 0));
        hardButton.setForeground(Color.white);
        hardButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        rankButton.setFont(buttonFont);
        rankButton.setBackground(new Color(255, 0, 153));
        rankButton.setForeground(Color.white);
        rankButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));



        exitButton.setFont(buttonFont);
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.white);
        exitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));



        // 添加按钮到面板
        add(Box.createVerticalStrut(50)); // 增加垂直间距
        add(easyButton);
        add(Box.createVerticalStrut(20));
        add(ordinaryButton);
        add(Box.createVerticalStrut(20));
        add(hardButton);
        add(Box.createVerticalStrut(20));
        add(rankButton);
        add(Box.createVerticalStrut(20));
        add(exitButton);

        backgroundImage = new ImageIcon("Picture/mc.png");

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制背景图片
        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

}
