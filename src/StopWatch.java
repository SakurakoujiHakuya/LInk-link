import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class StopWatch extends JPanel implements ActionListener{

    JLabel timeArea;

    private int mins=0,sec=0,mSec=0;
    private long currentTime;//记录系统当前时间。
    private long difference;//计时器实际显示时间
    private long timeOfStart;//记录按下"开始"时的系统时间。
    private long timeLengthOfPause;///按下"暂停"时记录累计计时时间。
    Timer timer;
    private String timeStr,str1,str2,str3;
    private boolean first=true;//第一次按下后，first=false.
    private boolean start=false;//是否开始start=true.
    boolean pause=false;//是否按下
    private ImageIcon backgroundImage=new ImageIcon("Picture/earth.png");

    public StopWatch(){
        timeArea=new JLabel("00:00:00");
        timeArea.setFont(new Font("隶书",Font.ITALIC,24));
        timeArea.setForeground(Color.white);
        timer=new Timer(10,this);			//设定定时器周期为10毫秒
        setVisible(true);
        add(timeArea);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    void timeStart(){
        start=true;//设置已经启动标志。
        pause=false;//清除暂停标志。
        //开始第一次或复位后
        if(first){//按"开始"才执行。
            timeLengthOfPause=0;
            first=false;
        }
        timeOfStart=System.currentTimeMillis();		//按开始的时候的时刻记录.
        timer.start();					//启动计时。
    }
    void timeStop(){

        pause=true;//设置暂停标志。
        start=false;//清除启动标志。
        timeLengthOfPause=mins*60*1000+sec*1000+mSec*10;//暂停后记录已累计计时的毫秒数。
        timer.stop();//停止计时。
    }
    void timeReset(){
        mins=0;
        sec=0;
        mSec=0;
        timer.stop();
        first=true;
        start=false;
        timeLengthOfPause=0;
        timeArea.setText("00:00:00");
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==timer)

        {
            currentTime=System.currentTimeMillis();//当前时间
            difference=timeLengthOfPause+(currentTime-timeOfStart);//计时时间。
            difference=difference/10;//以10ms为单位。
            mins=(int)(difference/(60*100));
            sec=(int)((difference/100)%60);
            mSec=(int)difference%100;//1(10)毫秒数。
            //显示新时间
            str1=(mins<10)?("0"+mins+":"):(mins+":");
            str2=(sec<10)?("0"+sec+":"):(sec+":");
            str3=(mSec<10)?("0"+mSec):(mSec+"");
            timeStr=str1+str2+str3;
            timeArea.setText(timeStr);
        }

    }
}