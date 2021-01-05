package wyf.cgq;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Date;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author w1585
 */
public class Clock extends Thread {
    TigerChess father;//声明XiangQi的引用
    long startTime;
    public Clock(TigerChess father){
        this.father=father;
    }
    public void run() {
        startTime=System.currentTimeMillis();
            while ((System.currentTimeMillis()-startTime)<=600000) {//一局游戏最长10分钟
                paint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){ }
            }
            father.jlMinute.setText("00");
            father.jlSecond.setText("00");
            JOptionPane.showMessageDialog(this.father,"超时","提示",JOptionPane.INFORMATION_MESSAGE);//给出超时信息
		father.cat.tiaoZhanZhe=null;
		father.color=0;
		father.caiPan=false;
		father.next();//还原棋盘，进入下一盘
		father.jtfHost.setEnabled(false);
		father.jtfPort.setEnabled(false);//设置各控件的状态
		father.jtfNickName.setEnabled(false);
		father.jbConnect.setEnabled(false);
		father.jbDisconnect.setEnabled(true);
		father.jbChallenge.setEnabled(true);
		father.jbYChallenge.setEnabled(false);
		father.jbNChallenge.setEnabled(false);
		father.jbFail.setEnabled(false);
		father.jpz.startI=-1;//还原保存点
		father.jpz.startJ=-1;
		father.jpz.endI=-1;
		father.jpz.endJ=-1;
		father.jpz.focus=false;
        }
      public void paint() 
    {
            
    /* New Calendar,Date and DateFormat class should be better here */
            int minute=0;
            int second=0;
            long time=(System.currentTimeMillis()-startTime);
            time=time/1000;
            minute=(int) (time/60);
            second=(int) (time%60);
            father.jlMinute.setText(String.valueOf(minute));
            father.jlSecond.setText(String.valueOf(second));
            
        }
}


