//要改的地方，把按钮丢到GamePanel里面，把StopWatch丢到GamePanel里面去启动？详尽
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GameClient extends JFrame{
    private StartPanel startPanel;

    private GamePanel gamePanel;
    private RankUI rankUI;

    public GameClient() {
        UI();
        creatStartPanel();
    }

    //界面的UI
    private void UI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("连连看");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //打开主界面
    void creatStartPanel() {
        startPanel = new StartPanel();
        add(startPanel, BorderLayout.CENTER);
        //选择难度
        startPanel.easyButton.addActionListener(createGamePanel(8));
        startPanel.ordinaryButton.addActionListener(createGamePanel(10));
        startPanel.hardButton.addActionListener(createGamePanel(12));

        //进入排行榜
        startPanel.rankButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                rankUI =new RankUI();
                startPanel.setVisible(false);
                add(rankUI, BorderLayout.CENTER);
                rankUI.exitButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        rankUI.setVisible(false);
                        startPanel.setVisible(true);
                    }
                });
            }
        });

        startPanel.exitButton.addActionListener(e -> System.exit(0));
}

    void setGamePanelFocusable(){
        gamePanel.pauseButton.setFocusable(false);
        gamePanel.backButton.setFocusable(false);
        gamePanel.helpButton.setFocusable(false);
        gamePanel.turnButton.setFocusable(false);
    }

    //进入游戏
    private ActionListener createGamePanel(int n) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel = new GamePanel(n);
                startPanel.setVisible(false);
                add(gamePanel, BorderLayout.CENTER);

                //revalidateAndRepaint(gamePanel);
                gamePanel.setFocusable(true);// 确保面板可以获取焦点（为了键盘）
                gamePanel.requestFocusInWindow();// 请求焦点

                //按返回键回到主界面
                gamePanel.backButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        gamePanel.setVisible(false);
                        creatStartPanel();
                    }
                });
                //暂停或者恢复
                gamePanel.pauseButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if(gamePanel.stopWatch.pause==false){
                            gamePanel.stopWatch.timeStop();
                            gamePanel.pauseButton.setText("继续");
                        }else {
                            gamePanel.stopWatch.timeStart();
                            gamePanel.pauseButton.setText("暂停");
                        }
                        gamePanel.turnButton.setFocusable(false);
                        gamePanel.setEnabled();//使得图片能够被点击或者不能点击
                        setGamePanelFocusable();
                    }
                });
                //获取帮助
                gamePanel.helpButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if(gamePanel.Enabled&&(gamePanel.HELPCNT)>0) {
                            gamePanel.checkLink();
                            gamePanel.HELPCNT--;
                            gamePanel.cntLabel.setText("帮助机会："+ gamePanel.HELPCNT);

                            if(gamePanel.HELPCNT==0)
                                gamePanel.helpButton.setEnabled(false);
                        }
                        setGamePanelFocusable();
                    }
                });
                //打乱地图
                gamePanel.turnButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if(gamePanel.Enabled) {
                            gamePanel.map = gamePanel.mapUtil.getResetMap();
                            repaint();
                        }
                        setGamePanelFocusable();
                    }
                });
            }
        };
    }

    //据说更安全
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameClient gameClient = new GameClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

