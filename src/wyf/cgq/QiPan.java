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
	private int width;//棋盘两线之间的距离
	boolean focus=false;//棋子的状态
	int startI=-1;//棋子的开始位置
	int startJ=-1;
	int endI=-1;//棋子的终止位置
	int endJ=-1;
	public QiZi qiZi[][];//棋子的数组
        int[][] posbefore={{-1,-1,-1,-1,0,0,0,0},{-1,-1,-1,-1,0,0,0,0}};//记录之前位置的数组，前面两个是起始位置，后面两个是终止位置,后面4个依次代表上下、左右、左斜、右斜方向是否有被吃掉狗
                                                                        //更改，第一行代表前一步存储的数据，因为编程后发现还需要保存上一步数据，所以升级为二维数组
        int[][] opsiteposbefore={{-1,-1,-1,-1,0,0,0,0},{-1,-1,-1,-1,0,0,0,0}};//记录对手之前位置的数组，前面两个是起始位置，后面两个是终止位置,后面4个依次代表上下、左右、左斜、右斜方向是否有被吃掉狗
	TigerChess xq=null;//声明XiangQi的引用
	GuiZe guiZe;//声明GuiZe的引用
	public QiPan(QiZi qiZi[][],int width,TigerChess xq){
		this.xq=xq;
		this.qiZi=qiZi;
		this.width=width;
		guiZe=new GuiZe(qiZi);
		this.addMouseListener(this);//为棋盘添加鼠标事件监听器
		this.setBounds(0,0,700,700);//设置棋盘的大小
		this.setLayout(null);//设为空布局
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
		Graphics2D g=(Graphics2D)g1;//获得Graphics2D对象
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                   RenderingHints.VALUE_ANTIALIAS_ON);//打开抗锯齿
		Color c=g.getColor();//获得画笔颜色
		g.setColor(TigerChess.bgColor);//将画笔设为背景色
		g.fill3DRect(100,10,500,660,false);//绘制一个矩形棋盘
		g.setColor(Color.black);//画笔颜色设为黑
		for(int i=230;i<=630;i=i+100){//绘制棋盘中的横线
			g.drawLine(150,i,550,i);
		}
		
		for(int i=150;i<=550;i=i+100){//绘制中间的竖线
			g.drawLine(i,230,i,630);
		}
		g.drawLine(150,230,550,630);//绘制斜线
		g.drawLine(550,230,150,630);
		g.drawLine(250,130,550,430);
		g.drawLine(450,130,150,430);
                g.drawLine(550,430,350,630);
                g.drawLine(150,430,350,630);
                g.drawLine(350,30,350,230);
		g.drawLine(250,130,450,130);
                g.drawLine(350,30,450,130);
                g.drawLine(350,30,250,130);
                g.drawOval(150+2*100-25,15,50,50);//绘制陷阱
                g.setColor(TigerChess.color0);
                g.fillOval(150+2*100-25+1,16,48,48);//绘制陷阱

		g.setColor(Color.black);
		Font font=new Font("宋体",Font.BOLD,30);
		g.setFont(font);//设置字体
		for(int i=0;i<5;i++){
			for(int j=0;j<7;j++){//绘制棋子
				if(qiZi[i][j]!=null){
					if(this.qiZi[i][j].getFocus()!=false){//被选中
						g.setColor(TigerChess.focusbg);//选中后的背景色
                                                Stroke sTtroke=new BasicStroke(3.0f);
                                                g.setStroke(sTtroke);
						g.drawImage(this.qiZi[i][j].imag, 150+i*100-25,30+j*100-25,null);
                                                g.drawOval(150+i*100-25,30+j*100-25,50,50);//绘制该棋子
                                               
						g.setColor(TigerChess.focuschar);//字符的颜色
					}
					else{
                                                g.drawImage(this.qiZi[i][j].imag, 150+i*100-25,30+j*100-25,null);
						//g.fillOval(150+i*100-25,30+j*100-25,50,50);//绘制该棋子
						g.setColor(qiZi[i][j].getColor());//设置画笔颜色
					}
				   // g.drawString(qiZi[i][j].getName(),150+i*100-15,30+j*100+10);
				    g.setColor(Color.black);//设为黑色
				}
			}
		}
		g.setColor(c);//还原画笔颜色
	}
     
        @Override
	public void mouseClicked(MouseEvent e){
		if(this.xq.caiPan==true){//判断是否轮到该玩家走棋
			int i=-1,j=-1;
			int[] pos=getPos(e);
			i=pos[0];
			j=pos[1];
			if((i>=0&&i<=4&&j>=2&&j<=6)||(j==1&&(i==1||i==2||i==3))||(j==0&&i==2))
                        {//如果在棋盘范围内
				if(focus==false){//如果棋面没有选中棋子
					this.noFocus(i,j);
				}
				else{//如果已经有棋子被选中
					if(qiZi[i][j]!=null)//如果该处有棋子
                                        {
						if(qiZi[i][j].getColor()==qiZi[startI][startJ].getColor())
						{//如果是自己的棋子
							qiZi[startI][startJ].setFocus(false);
							qiZi[i][j].setFocus(true);//更改选中对象
							startI=i;startJ=j;//保存修改
						}
						
					}
					else{//如果没有棋子                                
						endI=i;
						endJ=j;//保存终点
						boolean canMove=guiZe.canMove(startI,startJ,endI,endJ);//判断是否可走
						if(canMove){//如果可以移动
                                                        this.noQiZi();
                                                   
						}
                                                
					}
				}
                     }
		     this.xq.repaint();//重绘      
		}
	}
      
	public int[] getPos(MouseEvent e){
		int[] pos=new int[2];
		pos[0]=-1;
		pos[1]=-1;
		Point p=e.getPoint();//获得事件发生的坐标点-
		double x=p.getX();
		double y=p.getY();
		if(Math.abs((x-150)/1%100)<=45){//获得对应于数组x下标的位置
			pos[0]=Math.round((float)(x-150))/100;
		}
		else if(Math.abs((x-150)/1%100)>=55){
			pos[0]=Math.round((float)(x-150))/100+1;
		}
		if(Math.abs((y-30)/1%100)<=45){//获得对应于数组y下标的位置
			pos[1]=Math.round((float)(y-30))/100;
		}
		else if(Math.abs((y-100)/1%100)>=55){
			pos[1]=Math.round((float)(y-30))/100+1;
		}
		return pos;
	}
	public void noFocus(int i,int j){
		if(this.qiZi[i][j]!=null)//如果该位置有棋子
		{
			if(this.xq.color==0)//如果是红方
			{
				if(this.qiZi[i][j].getColor().equals(TigerChess.color1))//如果棋子是红色
				{
					this.qiZi[i][j].setFocus(true);//将该棋子设为选中状态
					focus=true;//将focus设为true
					startI=i;//保存该坐标点
					startJ=j;
				}
			}
			else//如果是白方
			{
				if(this.qiZi[i][j].getColor().equals(TigerChess.color2))//如果该棋子是白色
				{
					this.qiZi[i][j].setFocus(true);//将该棋子设为选中状态
					focus=true;//将focus设为true
					startI=i;//保存该坐标点
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
		JOptionPane.showMessageDialog(this.xq,"恭喜您，您获胜了","提示",
		                JOptionPane.INFORMATION_MESSAGE);//给出获胜信息
		this.xq.cat.tiaoZhanZhe=null;
		this.xq.color=0;
		this.xq.caiPan=false;
		this.xq.next();//还原棋盘，进入下一盘
		this.xq.jtfHost.setEnabled(false);
		this.xq.jtfPort.setEnabled(false);//设置各控件的状态
		this.xq.jtfNickName.setEnabled(false);
		this.xq.jbConnect.setEnabled(false);
		this.xq.jbDisconnect.setEnabled(true);
		this.xq.jbChallenge.setEnabled(true);
		this.xq.jbYChallenge.setEnabled(false);
		this.xq.jbNChallenge.setEnabled(false);
		this.xq.jbFail.setEnabled(false);
		startI=-1;//还原保存点
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
            JOptionPane.showMessageDialog(this.xq,"很遗憾，您输了","提示",JOptionPane.INFORMATION_MESSAGE);//给出失败信息
            this.xq.cat.tiaoZhanZhe=null;
            this.xq.color=0;//还原棋盘，进入下一盘
            this.xq.caiPan=false;
            this.xq.next();
            this.xq.jtfHost.setEnabled(false);
            this.xq.jtfPort.setEnabled(false);//设置各空间位置
            this.xq.jtfNickName.setEnabled(false);
            this.xq.jbConnect.setEnabled(false);
            this.xq.jbDisconnect.setEnabled(true);
            this.xq.jbChallenge.setEnabled(true);
            this.xq.jbYChallenge.setEnabled(false);
            this.xq.jbNChallenge.setEnabled(false);
            this.xq.jbFail.setEnabled(false);
            startI=-1;//还原保存点
            startJ=-1;
            endI=-1;
            endJ=-1;
            focus=false;
        }
        public boolean atqipan(int i,int j){//判断（i，j）是否在棋盘上
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
		try{//将该移动信息发送给对方
			this.xq.cat.dout.writeUTF("<#MOVE#>"+this.xq.cat.tiaoZhanZhe+startI+startJ+endI+endJ);
                        this.xq.walknum++;
                        copyposbefore();
                        posbefore[1][0]=startI;posbefore[1][1]=startJ;posbefore[1][2]=endI;posbefore[1][3]=endJ;
                        posbefore[1][4]=0;posbefore[1][5]=0;posbefore[1][6]=0;posbefore[1][7]=0;
                        this.xq.jbregive.setEnabled(false);
			this.xq.caiPan=false;
			qiZi[endI][endJ]=qiZi[startI][startJ];
			qiZi[startI][startJ]=null;//走棋
			qiZi[endI][endJ].setFocus(false);//将该棋设为非选中状态
                        this.xq.repaint();//重绘
                        if(this.qiZi[endI][endJ].getColor().equals(TigerChess.color2))//如果该棋子是白色
                        {  //判断是否可以吃子
                            if((endI==0||endI==4)&&endJ>2&&endJ<6)//落点在左右边界6个点
                            {
                                    if(qiZi[endI][endJ+1]!=null&&qiZi[endI][endJ-1]!=null)//若上下方向均有棋子
                                    {
                                        qiZi[endI][endJ+1]=null;
                                        qiZi[endI][endJ-1]=null;
                                        posbefore[1][4]=1;
                                    }
                            }
                            else 
                            {
                                if((endJ==6)&&endI>0&&endI<4)//落点在下边界3个点
                                {
                                    if(qiZi[endI+1][endJ]!=null&&qiZi[endI-1][endJ]!=null)//若左右方向均有棋子
                                    {
                                        qiZi[endI+1][endJ]=null;
                                        qiZi[endI-1][endJ]=null;
                                        posbefore[1][5]=1;
                                    }
                                }
                                else
                                {
                                    if((endI>0&&endI<4)&&(endJ>1&&endJ<6)||(endI==2&&endJ==1))//落在中心12个点和陷阱前一个点
                                    {
                                        if(qiZi[endI+1][endJ]!=null&&qiZi[endI-1][endJ]!=null)//左右
                                        {
                                            qiZi[endI+1][endJ]=null;
                                            qiZi[endI-1][endJ]=null;
                                            posbefore[1][5]=1;
                                        }
                                        if(qiZi[endI][endJ+1]!=null&&qiZi[endI][endJ-1]!=null)//上下
                                        {
                                            qiZi[endI][endJ+1]=null;
                                            qiZi[endI][endJ-1]=null;
                                            posbefore[1][4]=1;
                                        }
                                        if(qiZi[endI+1][endJ+1]!=null&&qiZi[endI-1][endJ-1]!=null&&(guiZe.canMove(endI,endJ,endI+1,endJ+1))&&(guiZe.canMove(endI,endJ,endI-1,endJ-1)))
                                        {//右斜
                                            qiZi[endI+1][endJ+1]=null;
                                            qiZi[endI-1][endJ-1]=null;
                                            posbefore[1][7]=1;
                                        }
                                        if(qiZi[endI-1][endJ+1]!=null&&qiZi[endI+1][endJ-1]!=null&&(guiZe.canMove(endI,endJ,endI-1,endJ+1))&&(guiZe.canMove(endI,endJ,endI+1,endJ-1)))
                                        {//左斜
                                            qiZi[endI-1][endJ+1]=null;
                                            qiZi[endI+1][endJ-1]=null;
                                            posbefore[1][6]=1;
                                        }
                                    }
                                }
                            }
                            if(endI==2&&endJ==0)//白色棋子走进陷阱，失败
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
                            if(count<=2)//狗剩俩
                            {
                                this.success();
                            }
                        }
                        else //若该棋是红色
                        {//获得虎的位置，判断是否把虎堵住
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
                            
                           if(cannotmove(I,J,I+1,J)&&cannotmove(I,J,I-1,J)&&cannotmove(I,J,I,J+1)&&cannotmove(I,J,I,J-1))//上下左右都不可以走
                           {
                               if(cannotmove(I,J,I+1,J+1)&&cannotmove(I,J,I-1,J-1)&&cannotmove(I,J,I-1,J+1)&&cannotmove(I,J,I+1,J-1))//斜线不可以走
                                   this.success();                              
                           }
                         
                         }
                         startI=-1;
			startJ=-1;//还原保存点
			endI=-1;
			endJ=-1;
			focus=false;      
                }catch(Exception ee){ee.printStackTrace();}
	}
        
public void move(int startI,int startJ,int endI,int endJ)
{//收到move信息后，客户端代理线程调用的move
	try{
            
			qiZi[endI][endJ]=qiZi[startI][startJ];
			qiZi[startI][startJ]=null;//走棋
                        this.xq.repaint();//重绘
                        this.xq.opsitewalknum++;
                        copyopsiteposbefore();
                        opsiteposbefore[1][0]=startI;opsiteposbefore[1][1]=startJ;opsiteposbefore[1][2]=endI;opsiteposbefore[1][3]=endJ;
                        opsiteposbefore[1][4]=0;opsiteposbefore[1][5]=0;opsiteposbefore[1][6]=0;opsiteposbefore[1][7]=0;
                        if(this.qiZi[endI][endJ].getColor().equals(TigerChess.color2))//如果该棋子是白色
                        {  //判断是否可以吃子
                            if((endI==0||endI==4)&&endJ>2&&endJ<6)//落点在左右边界6个点
                            {
                                    if(qiZi[endI][endJ+1]!=null&&qiZi[endI][endJ-1]!=null)//若上下方向均有棋子
                                    {
                                        qiZi[endI][endJ+1]=null;
                                        qiZi[endI][endJ-1]=null;
                                        opsiteposbefore[1][4]=1;
                                    }
                            }
                            else 
                            {
                                if((endJ==6)&&endI>0&&endI<4)//落点在下边界3个点
                                {
                                    if(qiZi[endI+1][endJ]!=null&&qiZi[endI-1][endJ]!=null)//若左右方向均有棋子
                                    {
                                        qiZi[endI+1][endJ]=null;
                                        qiZi[endI-1][endJ]=null;
                                        opsiteposbefore[1][5]=1;
                                    }
                                }
                                else
                                {
                                    if((endI>0&&endI<4)&&(endJ>1&&endJ<6)||(endI==2&&endJ==1))//落在中心12个点和陷阱前一个点
                                    {
                                        if(qiZi[endI+1][endJ]!=null&&qiZi[endI-1][endJ]!=null)//左右
                                        {
                                            qiZi[endI+1][endJ]=null;
                                            qiZi[endI-1][endJ]=null;
                                            opsiteposbefore[1][5]=1;
                                        }
                                        if(qiZi[endI][endJ+1]!=null&&qiZi[endI][endJ-1]!=null)//上下
                                        {
                                            qiZi[endI][endJ+1]=null;
                                            qiZi[endI][endJ-1]=null;
                                            opsiteposbefore[1][4]=1;
                                        }
                                        if(qiZi[endI+1][endJ+1]!=null&&qiZi[endI-1][endJ-1]!=null&&(guiZe.canMove(endI,endJ,endI+1,endJ+1))&&(guiZe.canMove(endI,endJ,endI-1,endJ-1)))
                                        {//右斜
                                            qiZi[endI+1][endJ+1]=null;
                                            qiZi[endI-1][endJ-1]=null;
                                            opsiteposbefore[1][7]=1;
                                        }
                                        if(qiZi[endI-1][endJ+1]!=null&&qiZi[endI+1][endJ-1]!=null&&(guiZe.canMove(endI,endJ,endI-1,endJ+1))&&(guiZe.canMove(endI,endJ,endI+1,endJ-1)))
                                        {//左斜
                                            qiZi[endI-1][endJ+1]=null;
                                            qiZi[endI+1][endJ-1]=null;
                                            opsiteposbefore[1][6]=1;
                                        }
                                    }
                                }
                            }
                            if(endI==2&&endJ==0)//白色棋子走进陷阱，失败
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
                            if(count<=2)//狗剩俩
                            {
                                this.lose();
                            }
                        }
                        else //若该棋是红色
                        {//获得虎的位置，判断是否把虎堵住
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
                             if(cannotmove(I,J,I+1,J)&&cannotmove(I,J,I-1,J)&&cannotmove(I,J,I,J+1)&&cannotmove(I,J,I,J-1))//上下左右都不可以走
                           {
                               if(cannotmove(I,J,I+1,J+1)&&cannotmove(I,J,I-1,J-1)&&cannotmove(I,J,I-1,J+1)&&cannotmove(I,J,I+1,J-1))//斜线不可以走
                                   this.lose();                              
                           }
                         }
                         startI=-1;
			startJ=-1;//还原保存点
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
