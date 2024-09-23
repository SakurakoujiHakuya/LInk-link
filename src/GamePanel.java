import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener ,MouseListener,KeyListener{
    private Image[] pics;//图片数组
    private int n;//行列数
    int[][] map;//存储地图信息
    private int leftX ,leftY;//图片偏移量

    private boolean isClick = false;//标记是否第一次选中图片
    private int clickId,clickX,clickY;//记录首次选中图片的id,以及数组下标
    private int linkMethod;//连接方式
    private Node z1,z2;//存储拐角点的信息
    Map mapUtil;//地图工具类
    public static int count = 0;//存储消去图案的个数
    private int imageWidth,imageHeight;//图片大小
    private ImageIcon backgroundImage=new ImageIcon("Picture/earth.png");//背景图片
    private ImageIcon blank=new ImageIcon("Picture/0.png");//棋盘
    JButton backButton=new JButton("返回");
    JButton pauseButton =new JButton("暂停");
    JButton helpButton =new JButton("帮助");
    JButton turnButton=new JButton("打乱棋盘");
    StopWatch stopWatch;//计时器
    RankPanel rankPanel =new RankPanel();
    private int difficulty;//难度系数
    public static final int EASY=0,ORDINARY=1,HARD=2;//难度系数具体的值
    public static final int ZEROCORNER = 0,ONECORNER = 1,TWOCORNER = 2;  //连接方法

    public static final int BLANK_STATE = -1; //空格子的map值位-1

    public boolean Enabled=true;//是否暂停，false为暂停状态
    public int HELPCNT;//帮助次数
    public JLabel cntLabel;//显示当前剩余帮助次数


    public GamePanel(int m){

        count=0;
        n = m;
        HELPCNT=3;
        mapUtil = new Map(n,n);
        map = mapUtil.getMap(n);//获取初始时，图片种类为n,行列数为n的地图信息
        //添加监听
        this.addKeyListener(this);
        this.addMouseListener(this);
        //添加控件
        addAndSetcontrol();
        setDifficulty();
        //this.setFocusable(true);//这里是设置获得焦点，但之前有了登录界面后不行，在GameClient中添加了反而解决了
        getPics();//读取图片信息
        repaint();//第一次画

    }
    void addAndSetcontrol(){
        cntLabel=new JLabel("帮助机会：3");
        cntLabel.setFont(new Font("宋体", Font.PLAIN, 20)); // 使用宋体字体
        cntLabel.setForeground(Color.white);

        stopWatch=new StopWatch();
        stopWatch.timeReset();
        stopWatch.timeStart();

        add(backButton);
        add(stopWatch);
        add(pauseButton);
        add(turnButton);
        add(helpButton);
        add(cntLabel);
    }

    public void startNewGame(int m) {
        //参数初始化
        count = 0;
        n=m;
        mapUtil = new Map(n,n);
        map = mapUtil.getMap(n);//获取初始时，图片种类为count,行列数为n的地图信息
        isClick = false;
        clickId = -1;
        clickX = -1;
        clickY = -1;
        linkMethod = -1;
        HELPCNT=3;
        helpButton.setEnabled(true);
        cntLabel.setText("帮助机会：3");

        //计时器归零，并开始计时
        stopWatch.timeReset();
        stopWatch.timeStart();

        setDifficulty();
        getPics();
        repaint();
    }
    public class Node{
        int x,y;

        public Node(int x,int y){
            this.x = x;
            this.y = y;
        }
    }

    //将行列数转化为对用的难度
    void setDifficulty(){
        if(n==8)
            difficulty=EASY;
        else if(n==10)
            difficulty=ORDINARY;
        else
            difficulty=HARD;
    }

    //从Picture里面读取图片

    private void getPics() {
        pics = new Image[n];
        for(int i=0;i<n;i++){
            pics[i] = Toolkit.getDefaultToolkit().getImage("Picture/"+(i+1)+".png");
        }
    }

    //画出棋盘和棋盘上的图片
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //图片长宽
        imageWidth =getWidth()/(4+n);
        imageHeight =getHeight()/(2+n);
        //偏移量
        leftX = getWidth()/(n-2);
        leftY = getHeight()/(n+2);
        //画背景图片
        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                g.drawImage(blank.getImage(),leftX+j*imageWidth,leftY+i*imageHeight,imageWidth,imageHeight,this);
                if(map[i][j]!= BLANK_STATE){
                    g.drawImage(pics[map[i][j]],leftX+j*imageWidth,leftY+i*imageHeight,imageWidth,imageHeight,this);
                }
            }
        }
    }

    //在以下连接判断中，是在map这个二维数组中判断的，二维数组中的坐标是[y][x]
    //写法是[clickX][clickY]。把ClickX当作y，把ClickY当作x
    private boolean zeroCornerLink(int clickX1, int clickY1, int clickX2, int clickY2){

        if(clickX1==clickX2){//如果两个选中图片的所在行数相同，说明可能可以水平相联

            if(clickY1>clickY2) {//保证y1<y2
                int temp=clickY1;
                clickY1=clickY2;
                clickY2=temp;
            }

            for(int i=clickY1+1;i<clickY2;i++){
                if(map[clickX1][i]!= BLANK_STATE){//如果两图片中间不空则无法相连
                    return false;
                }
            }
            linkMethod = ZEROCORNER;
            return true;
        }
         if(clickY1==clickY2){//两个图片处于同一列

            if(clickX1>clickX2) {//让第一个图片在上方
                int temp=clickX1;
                clickX1=clickX2;
                clickX2=temp;
            }
            for(int i=clickX1+1;i<clickX2;i++){
                if(map[i][clickY1]!= BLANK_STATE){//如果两图片中间还有其他图片，说明不能直接垂直相连
                    return false;
                }
            }
            linkMethod = ZEROCORNER;
            return true;
        }
        return false;
    }

    //一个拐点的情况，拐点横坐标与一个相同，纵坐标与另一个相同
    private boolean oneCornerLink(int clickX1, int clickY1, int clickX2, int clickY2) {
        if(map[clickX1][clickY2] == BLANK_STATE&&zeroCornerLink(clickX1,clickY2,clickX1,clickY1)&&zeroCornerLink(clickX1,clickY2,clickX2,clickY2))
        {
            linkMethod = ONECORNER;
            z1 = new Node(clickX1, clickY2);
            return true;
        }
        if (map[clickX2][clickY1]== BLANK_STATE&&zeroCornerLink(clickX2,clickY1,clickX1,clickY1)&&zeroCornerLink(clickX2,clickY1,clickX2,clickY2)){
            linkMethod = ONECORNER;
            z1 = new Node(clickX2, clickY1);
            return true;
        }
        return  false;
    }

    //判断是否可以通过两个拐点相连，思路是通过上下左右走直线，每走一步便用这一步的地点和目标地点用单拐点连接
    private boolean twoCornerLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        //向上查找
        for(int i=clickX1-1;i>=-1;i--){

            if(i>-1){
                if(map[i][clickY1]== BLANK_STATE) {
                    if (oneCornerLink(i, clickY1, clickX2, clickY2)) {
                        linkMethod = TWOCORNER;
                        z1 = new Node(i, clickY1);
                        z2 = new Node(i, clickY2);
                        return true;
                    }
                }else
                    break;

            }
            else if(i==-1&&zeroCornerLink(i,clickY2,clickX2,clickY2)) {
                linkMethod=TWOCORNER;
                z1=new Node(i,clickY1);
                z2=new Node(i,clickY2);
                return true;
            }
        }

        //向下查找
        for(int i=clickX1+1;i<=n;i++){
            if(i<n){
                if(map[i][clickY1]== BLANK_STATE) {
                    if (oneCornerLink(i, clickY1, clickX2, clickY2)) {
                        linkMethod = TWOCORNER;
                        z1 = new Node(i, clickY1);
                        z2 = new Node(i, clickY2);
                        return true;
                    }
                }else
                    break;
            }
            else if(i==n&&zeroCornerLink(i,clickY2,clickX2,clickY2)) {
                linkMethod=TWOCORNER;
                z1=new Node(i,clickY1);
                z2=new Node(i,clickY2);
                return true;
            }
        }

        //向左查找
        for(int i=clickY1-1;i>=-1;i--){

            if(i>-1) {
                if (map[clickX1][i] == BLANK_STATE) {
                    if (oneCornerLink(clickX1, i, clickX2, clickY2)) {
                        linkMethod = TWOCORNER;
                        z1 = new Node(clickX1, i);
                        z2 = new Node(clickX2, i);
                        return true;
                    }
                }else
                    break;
            }
            else if(i==-1&&zeroCornerLink(clickX2,i,clickX2,clickY2)) {
                linkMethod=TWOCORNER;
                z1 = new Node(clickX1,i);
                z2 = new Node(clickX2,i);
                return true;
            }
        }

        //向右查找
        for(int i=clickY1+1;i<=n;i++){

            if(i<n){
                if (map[clickX1][i]== BLANK_STATE) {
                    if (oneCornerLink(clickX1, i, clickX2, clickY2)) {
                        linkMethod = TWOCORNER;
                        z1 = new Node(clickX1, i);
                        z2 = new Node(clickX2, i);
                        return true;
                    }
                }else
                    break;
            }
            else if(i==n&&zeroCornerLink(clickX2,i,clickX2,clickY2)) {
                linkMethod=TWOCORNER;
                z1 = new Node(clickX1,i);
                z2 = new Node(clickX2,i);
                return true;
            }
        }
        return false;
    }
    //画选中框
    private void drawSelectedBlock(int x, int y, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;//生成Graphics对象
        BasicStroke s = new BasicStroke(3);//宽度为3的画笔
        g2.setStroke(s);
        g2.setColor(new Color(152, 32, 222));
        g.drawRect(x+2, y+2, imageWidth-5, imageHeight-5);
    }

    //画线，此处的x1,y1,x2,y2二维数组下标
    @SuppressWarnings("static-access")
    private void drawLink(int x1, int y1, int x2, int y2) {

        Graphics g = this.getGraphics();
        Graphics2D g2 = (Graphics2D) g;//生成Graphics对象
        BasicStroke s = new BasicStroke(3);//宽度为3的画笔
        g2.setColor(new Color(243, 220, 35));
        g2.setStroke(s);

        //从图片中间开始连好看些
        Point p1 = new Point(y1*imageWidth+leftX+imageWidth/2,x1*imageHeight+leftY+imageHeight/2);
        Point p2 = new Point(y2*imageWidth+leftX+imageWidth/2,x2*imageHeight+leftY+imageHeight/2);

        if(linkMethod == ZEROCORNER){
            g.drawLine(p1.x, p1.y,p2.x, p2.y);

        }else if(linkMethod ==ONECORNER){
            Point point_z1 = new Point(z1.y*imageWidth+leftX+imageWidth/2,z1.x*imageHeight+leftY+imageHeight/2);//将拐点转换成像素坐标
            g.drawLine(p1.x, p1.y,point_z1.x, point_z1.y);
            g.drawLine(p2.x, p2.y,point_z1.x, point_z1.y);

        }else{
            Point point_z1 = new Point(z1.y*imageWidth+leftX+imageWidth/2,z1.x*imageHeight+leftY+imageHeight/2);
            Point point_z2 = new Point(z2.y*imageWidth+leftX+imageWidth/2,z2.x*imageHeight+leftY+imageHeight/2);

            if(p1.x!=point_z1.x&&p1.y!=point_z1.y){//保证(x1,y1)与拐点z1在同一列或者同一行
                Point temp;
                temp = point_z1;
                point_z1 = point_z2;
                point_z2 = temp;
            }

            g.drawLine(p1.x, p1.y, point_z1.x, point_z1.y);
            g.drawLine(p2.x, p2.y, point_z2.x, point_z2.y);
            g.drawLine(point_z1.x,point_z1.y, point_z2.x, point_z2.y);

        }

        count+=2;//消去的方块数目+2
        try {
            Thread.currentThread().sleep(50);//延时50ms
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        repaint();//主要用来擦除连线
        //图片地图位置标空
        map[x1][y1] = BLANK_STATE;
        map[x2][y2] = BLANK_STATE;
        isWin();//判断游戏是否结束
    }

    //清除选中框，用棋盘的格子把原来的地方盖住，然后再在上面画图片
    public void clearSelectBlock(int i,int j,Graphics g){
        g.drawImage(blank.getImage(),leftX+j*imageWidth,leftY+i*imageHeight,imageWidth,imageHeight,this);
        g.drawImage(pics[map[i][j]],leftX+j*imageWidth,leftY+i*imageHeight,imageWidth,imageHeight,this);
    }

    //获得帮助
    public boolean checkLink() {
        if(isClick){//如果之前玩家选中了一个方块，清空该选中框
            clearSelectBlock(clickX, clickY, this.getGraphics());
            isClick = false;
        }

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(map[i][j]!= BLANK_STATE){
                    for(int p=i;p<n;p++){
                        for(int q=0;q<n;q++){
                            if(map[p][q]!=map[i][j]||(p==i&&q==j)){//如果图案不相等
                                continue;
                            }
                            if(zeroCornerLink(p,q,i,j) ||oneCornerLink(p,q,i,j)||twoCornerLink(p,q,i,j)){
                                drawSelectedBlock(j*imageWidth+leftX, i*imageHeight+leftY, this.getGraphics());
                                drawSelectedBlock(q*imageWidth+leftX, p*imageHeight+leftY, this.getGraphics());
                                drawLink(p, q, i, j);
                                repaint();
                                return true;
                            }

                        }
                    }
                }
            }
        }
        isWin();

        map = mapUtil.getResetMap();
        repaint();
        return false;

    }
    //判断胜利条件并作后续处理
    private void isWin() {
        if (count == n * n) {
            stopWatch.timeStop();
            String record = rankPanel.getTimeData(difficulty);
            String time = stopWatch.timeArea.getText();
            String msg = new String();

            // 判断是否突破记录
            if (rankPanel.compareTime(time, record)) {
                // 突破了记录，显示输入框让玩家输入姓名
                String playerName = JOptionPane.showInputDialog(null, "请输入您的大名：", "恭喜您打破纪录", JOptionPane.PLAIN_MESSAGE);
                if (playerName != null && !playerName.isEmpty()) {
                    rankPanel.setTimeData(playerName, time, difficulty);
                    msg = "恭喜您打破了记录，排行榜上已留下您的光辉记录:"+ rankPanel.getData(difficulty)+"!";
                } else {
                    rankPanel.setTimeData("谦虚者", time, difficulty);
                    msg = "刚刚有位不愿透露姓名的玩家留下了谦虚的记录："+ rankPanel.getData(difficulty)+"!";
                }
            } else {
                msg = "你离" + rankPanel.getData(difficulty) + "就差一点点了，加油!";
            }

            //按钮内容
            String[] options = {"来点简单的", "来点普通的", "我要挑战最难的", "回到开始菜单"};
            int choice = JOptionPane.showOptionDialog(null, msg, "过关", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == JOptionPane.CLOSED_OPTION) {
                // 用户点击了关闭按钮，执行相应的操作（例如不执行任何操作或者执行其他适当的操作）
            } else {
                if (choice == 0) {
                    startNewGame(8);
                } else if (choice == 1) {
                    startNewGame(10);
                } else if (choice == 2) {
                    startNewGame(12);
                } else if (choice == 3) {//返回主菜单
                    backButton.doClick();
                }
            }
        }
    }

    //用于模拟暂停效果
    void setEnabled(){
        if(Enabled) {
            Enabled = false;
        }
        else
            Enabled = true;
        pauseButton.setFocusable(false);
    }

    /////////////////开发者作弊功能
    @Override
    public void keyPressed(KeyEvent e)
    {

        if (Enabled) {
            //打乱棋盘
            if (e.getKeyCode() == KeyEvent.VK_A) {
                map = mapUtil.getResetMap();
                repaint();
            }

            //按键盘开启作弊
            if (e.getKeyCode() == KeyEvent.VK_Z) {
                if (!checkLink() && count != n * n) {
                    JOptionPane.showMessageDialog(this, "没有可以连通的方块了");
                }

            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                backButton.doClick();
        }
    }
    /////////////////开发者调试模块

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

        Graphics g = this.getGraphics();
        int x = e.getX()-leftX;//点击位置x-偏移量x
        int y = e.getY()-leftY;//点击位置y-偏移量y
        int i = y/imageHeight;//对应数组行数,根据像素坐标转换成数组下标坐标
        int j = x/imageWidth;//对应数组列数
//        if(i<0||j<0||i>n||j>n)//超出地图范围
//            return;
        if(Enabled) {
            if (!isClick) {//如果是第一次点击

                if (map[i][j] != BLANK_STATE) {
                    //选中图片并画框
                    clickId = map[i][j];
                    isClick = true;
                    clickX = i;
                    clickY = j;
                    drawSelectedBlock(j * imageWidth + leftX, i * imageHeight + leftY, g);
                }

            } else {//第二次点击了
                if (map[i][j] != BLANK_STATE) {
                    if (map[i][j] == clickId) {//点击的是同一种图片
                        //两次选同一位置的图片解除选中状态
                        if (i == clickX && j == clickY) {
                            clearSelectBlock(clickX, clickY, g);
                            isClick = false;
                        }
                        //如果可以连通，画线连接，然后消去选中图片并重置第一次选中标识
                        else if (zeroCornerLink(clickX, clickY, i, j) || oneCornerLink(clickX, clickY, i, j) || twoCornerLink(clickX, clickY, i, j)) {
                            drawSelectedBlock(j * imageWidth + leftX, i * imageHeight + leftY, g);
                            drawLink(clickX, clickY, i, j);//画线连接
                            isClick = false;

                        } else {//相同图片但连接失败，把选定框给新选的
                            clearSelectBlock(clickX, clickY, g);
                            clickId = map[i][j];
                            clickX = i;
                            clickY = j;
                            drawSelectedBlock(j * imageWidth + leftX, i * imageHeight + leftY, g);
                        }

                    } else {//选的图片都不是同一种，把选定框给新的
                        clearSelectBlock(clickX, clickY, g);
                        clickId = map[i][j];//重新选中图片并画框
                        clickX = i;
                        clickY = j;
                        drawSelectedBlock(j * imageWidth + leftX, i * imageHeight + leftY, g);
                    }

                }

            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }


    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}
