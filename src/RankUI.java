import javax.swing.*;
import java.awt.*;

public class RankUI extends JPanel {
    RankPanel rankPanel;
    JLabel easyLabel = new JLabel("简单：");
    JLabel ordinaryLabel = new JLabel("普通：");
    JLabel hardLabel = new JLabel("困难：");
    JButton exitButton = new JButton("回到主界面");

    private ImageIcon backgroundImage;

    public RankUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 使用垂直布局

        rankPanel=new RankPanel();
        easyLabel.setText("简单："+rankPanel.getData(0));
        ordinaryLabel.setText("普通："+rankPanel.getData(1));
        hardLabel.setText("困难："+rankPanel.getData(2));

        Font buttonFont = new Font("宋体", Font.PLAIN, 20); // 设置字体
        easyLabel.setFont(buttonFont);
        easyLabel.setOpaque(true);
        easyLabel.setBackground(new Color(50, 205, 50));
        easyLabel.setForeground(Color.white);
        easyLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        ordinaryLabel.setFont(buttonFont);
        ordinaryLabel.setOpaque(true);
        ordinaryLabel.setBackground(new Color(255, 165, 0));
        ordinaryLabel.setForeground(Color.white);
        ordinaryLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        hardLabel.setFont(buttonFont);
        hardLabel.setOpaque(true);
        hardLabel.setBackground(new Color(255, 69, 0));
        hardLabel.setForeground(Color.white);
        hardLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        exitButton.setFont(buttonFont);
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.white);
        exitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 添加按钮到面板
        add(Box.createVerticalStrut(50)); // 增加垂直间距
        add(easyLabel);
        add(Box.createVerticalStrut(20));
        add(ordinaryLabel);
        add(Box.createVerticalStrut(20));
        add(hardLabel);
        add(Box.createVerticalStrut(20));
        add(exitButton);

        backgroundImage = new ImageIcon("Picture/rank.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制背景图片
        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
    }


}
