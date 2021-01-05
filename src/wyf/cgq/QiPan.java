package wyf.cgq;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import static wyf.cgq.TigerChess.color1;
import static wyf.cgq.TigerChess.color2;
public class QiPan extends JPanel implements MouseListener{
	private int width;//��������֮��ľ���
	boolean focus=false;//���ӵ�״̬
	int startI=-1;//���ӵĿ�ʼλ��
	int startJ=-1;
	int endI=-1;//���ӵ���ֹλ��
	int endJ=-1;
	public QiZi qiZi[][];//���ӵ�����
        int[][] posbefore={{-1,-1,-1,-1,0,0,0,0},{-1,-1,-1,-1,0,0,0,0}};//��¼֮ǰλ�õ����飬ǰ����������ʼλ�ã�������������ֹλ��,����4�����δ������¡����ҡ���б����б�����Ƿ��б��Ե���
                                                                        //���ģ���һ�д���ǰһ���洢�����ݣ���Ϊ��̺��ֻ���Ҫ������һ�����ݣ���������Ϊ��ά����
        int[][] opsiteposbefore={{-1,-1,-1,-1,0,0,0,0},{-1,-1,-1,-1,0,0,0,0}};//��¼����֮ǰλ�õ����飬ǰ����������ʼλ�ã�������������ֹλ��,����4�����δ������¡����ҡ���б����б�����Ƿ��б��Ե���
	TigerChess xq=null;//����XiangQi������
	GuiZe guiZe;//����GuiZe������
	public QiPan(QiZi qiZi[][],int width,TigerChess xq){
		this.xq=xq;
		this.qiZi=qiZi;
		this.width=width;
		guiZe=new GuiZe(qiZi);
		this.addMouseListener(this);//Ϊ�����������¼�������
		this.setBounds(0,0,700,700);//�������̵Ĵ�С
		this.setLayout(null);//��Ϊ�ղ���
	}
        public void copyposbefore(){
                int i=0;
                for(i=0;i<8;i++){
                    posbefore[0][i]=posbefore[1][i];
                }
                
        }
        public void copyopsiteposbefore(){
                int i=0;
                for(i=0;i<8;i++){
                    opsiteposbefore[0][i]=opsiteposbefore[1][i];
                }
        }
	public void paint(Graphics g1){
		Graphics2D g=(Graphics2D)g1;//���Graphics2D����
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                   RenderingHints.VALUE_ANTIALIAS_ON);//�򿪿����
		Color c=g.getColor();//��û�����ɫ
		g.setColor(TigerChess.bgColor);//��������Ϊ����ɫ
		g.fill3DRect(100,10,500,660,false);//����һ����������
		g.setColor(Color.black);//������ɫ��Ϊ��
		for(int i=230;i<=630;i=i+100){//���������еĺ���
			g.drawLine(150,i,550,i);
		}
		
		for(int i=150;i<=550;i=i+100){//�����м������
			g.drawLine(i,230,i,630);
		}
		g.drawLine(150,230,550,630);//����б��
		g.drawLine(550,230,150,630);
		g.drawLine(250,130,550,430);
		g.drawLine(450,130,150,430);
                g.drawLine(550,430,350,630);
                g.drawLine(150,430,350,630);
                g.drawLine(350,30,350,230);
		g.drawLine(250,130,450,130);
                g.drawLine(350,30,450,130);
                g.drawLine(350,30,250,130);
                g.drawOval(150+2*100-25,15,50,50);//��������
                g.setColor(TigerChess.color0);
                g.fillOval(150+2*100-25+1,16,48,48);//��������

		g.setColor(Color.black);
		Font font=new Font("����",Font.BOLD,30);
		g.setFont(font);//��������
		for(int i=0;i<5;i++){
			for(int j=0;j<7;j++){//��������
				if(qiZi[i][j]!=null){
					if(this.qiZi[i][j].getFocus()!=false){//��ѡ��
						g.setColor(TigerChess.focusbg);//ѡ�к�ı���ɫ
                                                Stroke sTtroke=new BasicStroke(3.0f);
                                                g.setStroke(sTtroke);
						g.drawImage(this.qiZi[i][j].imag, 150+i*100-25,30+j*100-25,null);
                                                g.drawOval(150+i*100-25,30+j*100-25,50,50);//���Ƹ�����
                                               
						g.setColor(TigerChess.focuschar);//�ַ�����ɫ
					}
					else{
                                                g.drawImage(this.qiZi[i][j].imag, 150+i*100-25,30+j*100-25,null);
						//g.fillOval(150+i*100-25,30+j*100-25,50,50);//���Ƹ�����
						g.setColor(qiZi[i][j].getColor());//���û�����ɫ
					}
				   // g.drawString(qiZi[i][j].getName(),150+i*100-15,30+j*100+10);
				    g.setColor(Color.black);//��Ϊ��ɫ
				}
			}
		}
		g.setColor(c);//��ԭ������ɫ
	}
     
        @Override
	public void mouseClicked(MouseEvent e){
		if(this.xq.caiPan==true){//�ж��Ƿ��ֵ����������
			int i=-1,j=-1;
			int[] pos=getPos(e);
			i=pos[0];
			j=pos[1];
			if((i>=0&&i<=4&&j>=2&&j<=6)||(j==1&&(i==1||i==2||i==3))||(j==0&&i==2))
                        {//��������̷�Χ��
				if(focus==false){//�������û��ѡ������
					this.noFocus(i,j);
				}
				else{//����Ѿ������ӱ�ѡ��
					if(qiZi[i][j]!=null)//����ô�������
                                        {
						if(qiZi[i][j].getColor()==qiZi[startI][startJ].getColor())
						{//������Լ�������
							qiZi[startI][startJ].setFocus(false);
							qiZi[i][j].setFocus(true);//����ѡ�ж���
							startI=i;startJ=j;//�����޸�
						}
						
					}
					else{//���û������                                
						endI=i;
						endJ=j;//�����յ�
						boolean canMove=guiZe.canMove(startI,startJ,endI,endJ);//�ж��Ƿ����
						if(canMove){//��������ƶ�
                                                        this.noQiZi();
                                                   
						}
                                                
					}
				}
                     }
		     this.xq.repaint();//�ػ�      
		}
	}
      
	public int[] getPos(MouseEvent e){
		int[] pos=new int[2];
		pos[0]=-1;
		pos[1]=-1;
		Point p=e.getPoint();//����¼������������-
		double x=p.getX();
		double y=p.getY();
		if(Math.abs((x-150)/1%100)<=45){//��ö�Ӧ������x�±��λ��
			pos[0]=Math.round((float)(x-150))/100;
		}
		else if(Math.abs((x-150)/1%100)>=55){
			pos[0]=Math.round((float)(x-150))/100+1;
		}
		if(Math.abs((y-30)/1%100)<=45){//��ö�Ӧ������y�±��λ��
			pos[1]=Math.round((float)(y-30))/100;
		}
		else if(Math.abs((y-100)/1%100)>=55){
			pos[1]=Math.round((float)(y-30))/100+1;
		}
		return pos;
	}
	public void noFocus(int i,int j){
		if(this.qiZi[i][j]!=null)//�����λ��������
		{
			if(this.xq.color==0)//����Ǻ췽
			{
				if(this.qiZi[i][j].getColor().equals(TigerChess.color1))//��������Ǻ�ɫ
				{
					this.qiZi[i][j].setFocus(true);//����������Ϊѡ��״̬
					focus=true;//��focus��Ϊtrue
					startI=i;//����������
					startJ=j;
				}
			}
			else//����ǰ׷�
			{
				if(this.qiZi[i][j].getColor().equals(TigerChess.color2))//����������ǰ�ɫ
				{
					this.qiZi[i][j].setFocus(true);//����������Ϊѡ��״̬
					focus=true;//��focus��Ϊtrue
					startI=i;//����������
		                        startJ=j;
				}
			}
		}
	}
	public void success(){
                if(xq.clockthread.isAlive())
                            xq.clockthread.stop();
                xq.jlMinute.setText("00");
                xq.jlSecond.setText("00");
		JOptionPane.showMessageDialog(this.xq,"��ϲ��������ʤ��","��ʾ",
		                JOptionPane.INFORMATION_MESSAGE);//������ʤ��Ϣ
		this.xq.cat.tiaoZhanZhe=null;
		this.xq.color=0;
		this.xq.caiPan=false;
		this.xq.next();//��ԭ���̣�������һ��
		this.xq.jtfHost.setEnabled(false);
		this.xq.jtfPort.setEnabled(false);//���ø��ؼ���״̬
		this.xq.jtfNickName.setEnabled(false);
		this.xq.jbConnect.setEnabled(false);
		this.xq.jbDisconnect.setEnabled(true);
		this.xq.jbChallenge.setEnabled(true);
		this.xq.jbYChallenge.setEnabled(false);
		this.xq.jbNChallenge.setEnabled(false);
		this.xq.jbFail.setEnabled(false);
		startI=-1;//��ԭ�����
		startJ=-1;
		endI=-1;
		endJ=-1;
		focus=false;
	}
        public void lose()
        {
            if(xq.clockthread.isAlive())
                            xq.clockthread.stop();
            xq.jlMinute.setText("00");
            xq.jlSecond.setText("00");
            JOptionPane.showMessageDialog(this.xq,"���ź���������","��ʾ",JOptionPane.INFORMATION_MESSAGE);//����ʧ����Ϣ
            this.xq.cat.tiaoZhanZhe=null;
            this.xq.color=0;//��ԭ���̣�������һ��
            this.xq.caiPan=false;
            this.xq.next();
            this.xq.jtfHost.setEnabled(false);
            this.xq.jtfPort.setEnabled(false);//���ø��ռ�λ��
            this.xq.jtfNickName.setEnabled(false);
            this.xq.jbConnect.setEnabled(false);
            this.xq.jbDisconnect.setEnabled(true);
            this.xq.jbChallenge.setEnabled(true);
            this.xq.jbYChallenge.setEnabled(false);
            this.xq.jbNChallenge.setEnabled(false);
            this.xq.jbFail.setEnabled(false);
            startI=-1;//��ԭ�����
            startJ=-1;
            endI=-1;
            endJ=-1;
            focus=false;
        }
        public boolean atqipan(int i,int j){//�жϣ�i��j���Ƿ���������
            if(i>0&&i<=4&&j>1&&i<=6)
                return true;
            if(i==2&&j==0)
                return true;
            if(j==1)
            {
                if(i==1||i==2||i==3)
                    return true;
            }
            return false;
        }
        public boolean cannotmove(int startI,int startJ,int endI,int endJ)
        {
            if(guiZe.canMove(startI,startJ,endI,endJ)&&atqipan(endI,endJ)&&qiZi[endI][endJ]==null)
                return false;
            return true;
        }
	public void noQiZi(){
		try{//�����ƶ���Ϣ���͸��Է�
			this.xq.cat.dout.writeUTF("<#MOVE#>"+this.xq.cat.tiaoZhanZhe+startI+startJ+endI+endJ);
                        this.xq.walknum++;
                        copyposbefore();
                        posbefore[1][0]=startI;posbefore[1][1]=startJ;posbefore[1][2]=endI;posbefore[1][3]=endJ;
                        posbefore[1][4]=0;posbefore[1][5]=0;posbefore[1][6]=0;posbefore[1][7]=0;
                        this.xq.jbregive.setEnabled(false);
			this.xq.caiPan=false;
			qiZi[endI][endJ]=qiZi[startI][startJ];
			qiZi[startI][startJ]=null;//����
			qiZi[endI][endJ].setFocus(false);//��������Ϊ��ѡ��״̬
                        this.xq.repaint();//�ػ�
                        if(this.qiZi[endI][endJ].getColor().equals(TigerChess.color2))//����������ǰ�ɫ
                        {  //�ж��Ƿ���Գ���
                            if((endI==0||endI==4)&&endJ>2&&endJ<6)//��������ұ߽�6����
                            {
                                    if(qiZi[endI][endJ+1]!=null&&qiZi[endI][endJ-1]!=null)//�����·����������
                                    {
                                        qiZi[endI][endJ+1]=null;
                                        qiZi[endI][endJ-1]=null;
                                        posbefore[1][4]=1;
                                    }
                            }
                            else 
                            {
                                if((endJ==6)&&endI>0&&endI<4)//������±߽�3����
                                {
                                    if(qiZi[endI+1][endJ]!=null&&qiZi[endI-1][endJ]!=null)//�����ҷ����������
                                    {
                                        qiZi[endI+1][endJ]=null;
                                        qiZi[endI-1][endJ]=null;
                                        posbefore[1][5]=1;
                                    }
                                }
                                else
                                {
                                    if((endI>0&&endI<4)&&(endJ>1&&endJ<6)||(endI==2&&endJ==1))//��������12���������ǰһ����
                                    {
                                        if(qiZi[endI+1][endJ]!=null&&qiZi[endI-1][endJ]!=null)//����
                                        {
                                            qiZi[endI+1][endJ]=null;
                                            qiZi[endI-1][endJ]=null;
                                            posbefore[1][5]=1;
                                        }
                                        if(qiZi[endI][endJ+1]!=null&&qiZi[endI][endJ-1]!=null)//����
                                        {
                                            qiZi[endI][endJ+1]=null;
                                            qiZi[endI][endJ-1]=null;
                                            posbefore[1][4]=1;
                                        }
                                        if(qiZi[endI+1][endJ+1]!=null&&qiZi[endI-1][endJ-1]!=null&&(guiZe.canMove(endI,endJ,endI+1,endJ+1))&&(guiZe.canMove(endI,endJ,endI-1,endJ-1)))
                                        {//��б
                                            qiZi[endI+1][endJ+1]=null;
                                            qiZi[endI-1][endJ-1]=null;
                                            posbefore[1][7]=1;
                                        }
                                        if(qiZi[endI-1][endJ+1]!=null&&qiZi[endI+1][endJ-1]!=null&&(guiZe.canMove(endI,endJ,endI-1,endJ+1))&&(guiZe.canMove(endI,endJ,endI+1,endJ-1)))
                                        {//��б
                                            qiZi[endI-1][endJ+1]=null;
                                            qiZi[endI+1][endJ-1]=null;
                                            posbefore[1][6]=1;
                                        }
                                    }
                                }
                            }
                            if(endI==2&&endJ==0)//��ɫ�����߽����壬ʧ��
                            {
                               this.lose();
                            }
                            int count=0;
                            for(int i=0;i<5;i++)
                            {
                                for(int j=0;j<7;j++)
                                {
                                    if(qiZi[i][j]!=null&&qiZi[i][j].getColor()==color1)
                                        count++;
                                }
                            }
                            if(count<=2)//��ʣ��
                            {
                                this.success();
                            }
                        }
                        else //�������Ǻ�ɫ
                        {//��û���λ�ã��ж��Ƿ�ѻ���ס
                            int I=-1;
                            int J=-1;
                            for(int i=0;i<5;i++)
                            {
                                for(int j=0;j<7;j++)
                                {
                                    if(qiZi[i][j]!=null&&qiZi[i][j].getColor()==color2)
                                    {  
                                        I=i;
                                        J=j;
                                        break;
                                    }
                                }
                            }
                            
                           if(cannotmove(I,J,I+1,J)&&cannotmove(I,J,I-1,J)&&cannotmove(I,J,I,J+1)&&cannotmove(I,J,I,J-1))//�������Ҷ���������
                           {
                               if(cannotmove(I,J,I+1,J+1)&&cannotmove(I,J,I-1,J-1)&&cannotmove(I,J,I-1,J+1)&&cannotmove(I,J,I+1,J-1))//б�߲�������
                                   this.success();                              
                           }
                         
                         }
                         startI=-1;
			startJ=-1;//��ԭ�����
			endI=-1;
			endJ=-1;
			focus=false;      
                }catch(Exception ee){ee.printStackTrace();}
	}
        
public void move(int startI,int startJ,int endI,int endJ)
{//�յ�move��Ϣ�󣬿ͻ��˴����̵߳��õ�move
	try{
            
			qiZi[endI][endJ]=qiZi[startI][startJ];
			qiZi[startI][startJ]=null;//����
                        this.xq.repaint();//�ػ�
                        this.xq.opsitewalknum++;
                        copyopsiteposbefore();
                        opsiteposbefore[1][0]=startI;opsiteposbefore[1][1]=startJ;opsiteposbefore[1][2]=endI;opsiteposbefore[1][3]=endJ;
                        opsiteposbefore[1][4]=0;opsiteposbefore[1][5]=0;opsiteposbefore[1][6]=0;opsiteposbefore[1][7]=0;
                        if(this.qiZi[endI][endJ].getColor().equals(TigerChess.color2))//����������ǰ�ɫ
                        {  //�ж��Ƿ���Գ���
                            if((endI==0||endI==4)&&endJ>2&&endJ<6)//��������ұ߽�6����
                            {
                                    if(qiZi[endI][endJ+1]!=null&&qiZi[endI][endJ-1]!=null)//�����·����������
                                    {
                                        qiZi[endI][endJ+1]=null;
                                        qiZi[endI][endJ-1]=null;
                                        opsiteposbefore[1][4]=1;
                                    }
                            }
                            else 
                            {
                                if((endJ==6)&&endI>0&&endI<4)//������±߽�3����
                                {
                                    if(qiZi[endI+1][endJ]!=null&&qiZi[endI-1][endJ]!=null)//�����ҷ����������
                                    {
                                        qiZi[endI+1][endJ]=null;
                                        qiZi[endI-1][endJ]=null;
                                        opsiteposbefore[1][5]=1;
                                    }
                                }
                                else
                                {
                                    if((endI>0&&endI<4)&&(endJ>1&&endJ<6)||(endI==2&&endJ==1))//��������12���������ǰһ����
                                    {
                                        if(qiZi[endI+1][endJ]!=null&&qiZi[endI-1][endJ]!=null)//����
                                        {
                                            qiZi[endI+1][endJ]=null;
                                            qiZi[endI-1][endJ]=null;
                                            opsiteposbefore[1][5]=1;
                                        }
                                        if(qiZi[endI][endJ+1]!=null&&qiZi[endI][endJ-1]!=null)//����
                                        {
                                            qiZi[endI][endJ+1]=null;
                                            qiZi[endI][endJ-1]=null;
                                            opsiteposbefore[1][4]=1;
                                        }
                                        if(qiZi[endI+1][endJ+1]!=null&&qiZi[endI-1][endJ-1]!=null&&(guiZe.canMove(endI,endJ,endI+1,endJ+1))&&(guiZe.canMove(endI,endJ,endI-1,endJ-1)))
                                        {//��б
                                            qiZi[endI+1][endJ+1]=null;
                                            qiZi[endI-1][endJ-1]=null;
                                            opsiteposbefore[1][7]=1;
                                        }
                                        if(qiZi[endI-1][endJ+1]!=null&&qiZi[endI+1][endJ-1]!=null&&(guiZe.canMove(endI,endJ,endI-1,endJ+1))&&(guiZe.canMove(endI,endJ,endI+1,endJ-1)))
                                        {//��б
                                            qiZi[endI-1][endJ+1]=null;
                                            qiZi[endI+1][endJ-1]=null;
                                            opsiteposbefore[1][6]=1;
                                        }
                                    }
                                }
                            }
                            if(endI==2&&endJ==0)//��ɫ�����߽����壬ʧ��
                            {
                               this.success();
                            }
                            int count=0;
                            for(int i=0;i<5;i++)
                            {
                                for(int j=0;j<7;j++){
                                    if(qiZi[i][j]!=null&&qiZi[i][j].getColor()==color1)
                                        count++;
                                    }
                            }
                            if(count<=2)//��ʣ��
                            {
                                this.lose();
                            }
                        }
                        else //�������Ǻ�ɫ
                        {//��û���λ�ã��ж��Ƿ�ѻ���ס
                            int I=-1;
                            int J=-1;
                            for(int i=0;i<5;i++)
                            {
                                for(int j=0;j<7;j++)
                                {
                                    if(qiZi[i][j]!=null&&qiZi[i][j].getColor()==color2)
                                    {  
                                        I=i;
                                        J=j;
                                        break;
                                    }
                                }
                            }
                             if(cannotmove(I,J,I+1,J)&&cannotmove(I,J,I-1,J)&&cannotmove(I,J,I,J+1)&&cannotmove(I,J,I,J-1))//�������Ҷ���������
                           {
                               if(cannotmove(I,J,I+1,J+1)&&cannotmove(I,J,I-1,J-1)&&cannotmove(I,J,I-1,J+1)&&cannotmove(I,J,I+1,J-1))//б�߲�������
                                   this.lose();                              
                           }
                         }
                         startI=-1;
			startJ=-1;//��ԭ�����
			endI=-1;
			endJ=-1;
			focus=false;      
                        
                        
			
		
                }catch(Exception ee){ee.printStackTrace();}
}
	
      //  public void mouseClicked(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	
}
