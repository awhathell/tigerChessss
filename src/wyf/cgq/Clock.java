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
    TigerChess father;//����XiangQi������
    long startTime;
    public Clock(TigerChess father){
        this.father=father;
    }
    public void run() {
        startTime=System.currentTimeMillis();
            while ((System.currentTimeMillis()-startTime)<=600000) {//һ����Ϸ�10����
                paint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){ }
            }
            father.jlMinute.setText("00");
            father.jlSecond.setText("00");
            JOptionPane.showMessageDialog(this.father,"��ʱ","��ʾ",JOptionPane.INFORMATION_MESSAGE);//������ʱ��Ϣ
		father.cat.tiaoZhanZhe=null;
		father.color=0;
		father.caiPan=false;
		father.next();//��ԭ���̣�������һ��
		father.jtfHost.setEnabled(false);
		father.jtfPort.setEnabled(false);//���ø��ؼ���״̬
		father.jtfNickName.setEnabled(false);
		father.jbConnect.setEnabled(false);
		father.jbDisconnect.setEnabled(true);
		father.jbChallenge.setEnabled(true);
		father.jbYChallenge.setEnabled(false);
		father.jbNChallenge.setEnabled(false);
		father.jbFail.setEnabled(false);
		father.jpz.startI=-1;//��ԭ�����
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


