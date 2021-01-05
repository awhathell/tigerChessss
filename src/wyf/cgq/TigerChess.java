package wyf.cgq;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
public class TigerChess extends JFrame implements ActionListener
{
	public static final Color bgColor=new Color(98,207,115);//���̵ı���ɫ
	public static final Color focusbg=new Color(0,0,0);//����ѡ�к�ı���ɫ
	public static final Color focuschar=new Color(96,95,91);//����ѡ�к���ַ���ɫ
        public static final Color color0=new Color(255,255,255);
	public static final Color color1=new Color(249,183,173);//�췽����ɫ
	public static final Color color2=Color.white;//�׷�����ɫ
	JLabel jlHost=new JLabel("������");//������ʾ�����������ı�ǩ
	JLabel jlPort=new JLabel("�˿ں�");////������ʾ����˿ںű�ǩ
	JLabel jlNickName=new JLabel("��    ��");//������ʾ�����ǳƵı�ǩ
        JLabel jlTime=new JLabel(":");//������ʾʱ��ð�ű�ǩ
        JLabel jlMinute=new JLabel("00");//������ʾʱ��ð�ű�ǩ
        JLabel jlSecond=new JLabel("00");//������ʾʱ��ð�ű�ǩ
	JTextField jtfHost=new JTextField("127.0.0.1");//�����������������ı���Ĭ��ֵ��"127.0.0.1"
	JTextField jtfPort=new JTextField("9999");//��������˿ںŵ��ı���Ĭ��ֵ��9999
	JTextField jtfNickName=new JTextField("Play1");//���������ǳƵ��ı���Ĭ��ֵ��Play1
        JButton jbregive=new JButton("�� ��");//����"����"��ť
	JButton jbConnect=new JButton("��  ��");//����"����"��ť
	JButton jbDisconnect=new JButton("��  ��");//����"�Ͽ�"��ť
	JButton jbFail=new JButton("��  ��");//����"����"��ť
	JButton jbChallenge=new JButton("��  ս");//����"��ս"��ť
	JComboBox jcbNickList=new JComboBox();//������ŵ�ǰ�û��������б��
	JButton jbYChallenge=new JButton("������ս");//����"������ս"��ť
	JButton jbNChallenge=new JButton("�ܾ���ս");//����"�ܾ���ս"��ť
	int width=60;//������������֮��ľ���
	QiZi[][] qiZi=new QiZi[5][7];//������������
	QiPan jpz=new QiPan(qiZi,width,this);//��������
	//JPanel jpz=new JPanel();//����һ��JPanel����ʱ��������
	JPanel jpy=new JPanel();//����һ��JPanel
	JSplitPane jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jpz,jpy);//����һ��JSplitPane
	int walknum=0;//��¼�Լ��ߵĲ���
        int opsitewalknum=0;//��¼�Է��ߵĲ���
	boolean caiPan=false;//�ɷ�����ı�־λ
	int color=0;//0 ������壬1�������
	Socket sc;//����Socket����
	ClientAgentThread cat;//�����ͻ��˴����̵߳�����
        Clock clockthread;
        Image tiger;
        Image dog;
	public TigerChess()
	{
		this.initialComponent();//��ʼ���ؼ�
		this.addListener();//Ϊ��Ӧ�ؼ�ע���¼�������
		this.initialState();//��ʼ��״̬
		this.initialQiZi();//��ʼ������
		this.initialFrame();//��ʼ������
                
	}
	public void initialComponent()
	{
		jpy.setLayout(null);//��Ϊ�ղ���
		this.jlHost.setBounds(10,10,50,20);
		jpy.add(this.jlHost);//���"������"��ǩ
		this.jtfHost.setBounds(70,10,80,20);
		jpy.add(this.jtfHost);//��������������������ı���
		this.jlPort.setBounds(10,40,50,20);
		jpy.add(this.jlPort);//���"�˿ں�"��ǩ
		this.jtfPort.setBounds(70,40,80,20);
		jpy.add(this.jlMinute);//��ӷ��ӱ�ǩ
		this.jlMinute.setBounds(35,220,40,40);
                this.jlMinute.setFont(new Font("����",Font.BOLD,30));
                this.jlMinute.setBackground(bgColor);
                this.jlMinute.setVerticalAlignment(SwingConstants.CENTER);
		jpy.add(this.jlTime);//��ӣ�
		this.jlTime.setBounds(80,220,20,40);
                this.jlTime.setFont(new Font("����",Font.BOLD,30));
                this.jlTime.setBackground(bgColor);
                this.jlTime.setVerticalAlignment(SwingConstants.CENTER);
		jpy.add(this.jlSecond);//������ǩ
		this.jlSecond.setBounds(105,220,40,40);
                this.jlSecond.setFont(new Font("����",Font.BOLD,30));
                this.jlSecond.setBackground(bgColor);
                this.jlSecond.setVerticalAlignment(SwingConstants.CENTER);
		jpy.add(this.jtfPort);//�����������˿ںŵ��ı���
		this.jlNickName.setBounds(10,70,50,20);
		jpy.add(this.jlNickName);//���"�ǳ�"��ǩ
		this.jtfNickName.setBounds(70,70,80,20);
		jpy.add(this.jtfNickName);//������������ǳƵ��ı���
		this.jbConnect.setBounds(10,100,80,20);
                jpy.add(jbregive);//���"����"��ť
                this.jbregive.setBounds(50,270,80,20);
		jpy.add(this.jbConnect);//���"����"��ť
		this.jbDisconnect.setBounds(100,100,80,20);
		jpy.add(this.jbDisconnect);//���"�Ͽ�"��ť
		this.jcbNickList.setBounds(20,130,130,20);
		jpy.add(this.jcbNickList);//���������ʾ��ǰ�û��������б��
		this.jbChallenge.setBounds(10,160,80,20);
		jpy.add(this.jbChallenge);//���"��ս"��ť
		this.jbFail.setBounds(100,160,80,20);
		jpy.add(this.jbFail);//���"����"��ť
		this.jbYChallenge.setBounds(5,190,86,20);
		jpy.add(this.jbYChallenge);//���"������ս"��ť
		this.jbNChallenge.setBounds(100,190,86,20);
		jpy.add(this.jbNChallenge);//���"�ܾ���ս"��ť
		jpz.setLayout(null);//��������Ϊ�ղ���
		jpz.setBounds(0,0,700,700);//���ô�С
	}
	public void addListener()
	{
		this.jbConnect.addActionListener(this);//Ϊ"����"��ťע���¼�������
		this.jbDisconnect.addActionListener(this);//Ϊ"�Ͽ�"��ťע���¼�������
		this.jbChallenge.addActionListener(this);//Ϊ"��ս"��ťע���¼�������
		this.jbFail.addActionListener(this);//Ϊ"����"��ťע���¼�������
		this.jbYChallenge.addActionListener(this);//Ϊ"ͬ����ս"��ťע���¼�������
		this.jbNChallenge.addActionListener(this);//Ϊ"�ܾ���ս"��ťע���¼�������
                this.jbregive.addActionListener(this);//Ϊ"����"��ťע���¼�������
                
	}
	public void initialState()
	{
		this.jbDisconnect.setEnabled(false);//��"�Ͽ�"��ť��Ϊ������
		this.jbChallenge.setEnabled(false);//��"��ս"��ť��Ϊ������
		this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
		this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
		this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
                this.jbregive.setEnabled(false);//��"����"��ť��Ϊ������
	}
	public void initialQiZi()
	{//��ʼ����������
		qiZi[0][2]=new QiZi(color1,"��",0,2);
		qiZi[1][2]=new QiZi(color1,"��",1,2);
		qiZi[2][2]=new QiZi(color1,"��",2,2);
		qiZi[3][2]=new QiZi(color1,"��",3,2);
		qiZi[4][2]=new QiZi(color1,"��",4,2);
		qiZi[0][3]=new QiZi(color1,"��",0,3);
		qiZi[0][4]=new QiZi(color1,"��",0,4);
		qiZi[0][5]=new QiZi(color1,"��",0,5);
		qiZi[0][6]=new QiZi(color1,"��",0,6);
		qiZi[1][6]=new QiZi(color1,"��",1,6);
		qiZi[2][6]=new QiZi(color1,"��",2,6);
		qiZi[3][6]=new QiZi(color1,"��",3,6);
		qiZi[4][6]=new QiZi(color1,"��",4,6);
		qiZi[4][5]=new QiZi(color1,"��",4,5);
		qiZi[4][4]=new QiZi(color1,"��",4,4);
		qiZi[4][3]=new QiZi(color1,"��",4,3);
		qiZi[2][4]=new QiZi(color2,"��",2,4);
	}
	public void initialFrame()
	{
		this.setTitle("������--�ͻ���");//���ô������
		Image image=new ImageIcon("ico.gif").getImage(); 
		this.setIconImage(image); //����ͼ��
		this.add(this.jsp);//���JSplitPane
		jsp.setDividerLocation(730);//���÷ָ���λ�ü����
		jsp.setDividerSize(4);
		this.setBounds(30,30,930,730);//���ô����С
		this.setVisible(true);//���ÿɼ���
		this.addWindowListener(//Ϊ������Ӽ�����
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					if(cat==null)//�ͻ��˴����߳�Ϊ�գ�ֱ���˳�
					{
						System.exit(0);//�˳�
						return;
					}
					try
					{
						if(cat.tiaoZhanZhe!=null)//����������
						{
							try
							{
								//����������Ϣ
								cat.dout.writeUTF("<#RENSHU#>"+cat.tiaoZhanZhe);
							}
							catch(Exception ee)
							{
								ee.printStackTrace();
							}
						}
						cat.dout.writeUTF("<#CLIENT_LEAVE#>");//������������뿪��Ϣ
						cat.flag=false;//��ֹ�ͻ��˴����߳�
						cat=null;
						
					}
					catch(Exception ee)
					{
						ee.printStackTrace();
					}
					System.exit(0);//�˳�
				}
				
			}
			);
	}
      
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==this.jbConnect)
		{//����"����"��ť
			this.jbConnect_event();
		}
		else if(e.getSource()==this.jbDisconnect)
		{//������"�Ͽ�"��ťʱ
			this.jbDisconnect_event();
		}
		else if(e.getSource()==this.jbChallenge)
		{//������"��ս"��ťʱ
			this.jbChallenge_event();
		}
		else if(e.getSource()==this.jbYChallenge)
		{//������"ͬ����ս"��ťʱ
			this.jbYChallenge_event();
		}
		else if(e.getSource()==this.jbNChallenge)
		{//������"�ܾ���ս"��ťʱ
			this.jbNChallenge_event();
		}
		else if(e.getSource()==this.jbFail)
		{//������"����"��ťʱ
			this.jbFail_event();
		}
                else if(e.getSource()==this.jbregive)
		{//������"����"��ťʱ
			this.jbregive_event();
		}
	}
	public void jbConnect_event()
	{//�Ե���"����"��ť�¼���ҵ�������
		int port=0;
		try
		{//����û�����Ķ˿ںŲ�ת��Ϊ����
			port=Integer.parseInt(this.jtfPort.getText().trim());
		}
		catch(Exception ee)
		{//��������������������ʾ
			JOptionPane.showMessageDialog(this,"�˿ں�ֻ��������","����",
			                              JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(port>65535||port<0)
		{//�˿ںŲ��Ϸ�������������ʾ
			JOptionPane.showMessageDialog(this,"�˿ں�ֻ����0-65535������","����",
			                              JOptionPane.ERROR_MESSAGE);
			return;
		}
		String name=this.jtfNickName.getText().trim();//����ǳ�
		if(name.length()==0)
		{//�ǳ�Ϊ�գ�����������ʾ��Ϣ
			JOptionPane.showMessageDialog(this,"�����������Ϊ��","����",
			                              JOptionPane.ERROR_MESSAGE);
			return;
		}
		try
		{
			sc=new Socket(this.jtfHost.getText().trim(),port);//����Socket����
			cat=new ClientAgentThread(this);//�����ͻ��˴����߳�	
			cat.start();//�����ͻ��˴����߳�	
			this.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);//��"����"��ť��Ϊ������
			this.jbDisconnect.setEnabled(true);//��"�Ͽ�"��ť��Ϊ����
			this.jbChallenge.setEnabled(true);//��"��ս"��ť��Ϊ����
			this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
			this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
			this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
			JOptionPane.showMessageDialog(this,"�����ӵ�������","��ʾ",
			            JOptionPane.INFORMATION_MESSAGE);//���ӳɹ���������ʾ��Ϣ	
		}
		catch(Exception ee)
		{
			JOptionPane.showMessageDialog(this,"���ӷ�����ʧ��","����",
			        JOptionPane.ERROR_MESSAGE);//����ʧ�ܣ�������ʾ��Ϣ
			return;
		}
	}
	public void jbDisconnect_event()
	{//�Ե���"�Ͽ�"��ť�¼���ҵ�������
		try
		{
			this.cat.dout.writeUTF("<#CLIENT_LEAVE#>");//������������뿪����Ϣ
			this.cat.flag=false;//��ֹ�ͻ��˴����߳�
			this.cat=null;
			this.jtfHost.setEnabled(!false);//�������������������ı�����Ϊ����
			this.jtfPort.setEnabled(!false);//����������˿ںŵ��ı�����Ϊ����
			this.jtfNickName.setEnabled(!false);//�����������ǳƵ��ı�����Ϊ����
			this.jbConnect.setEnabled(!false);//��"����"��ť��Ϊ����
			this.jbDisconnect.setEnabled(!true);//��"�Ͽ�"��ť��Ϊ������
			this.jbChallenge.setEnabled(!true);//��"��ս"��ť��Ϊ������
			this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
			this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
			this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	public void jbChallenge_event(){
		//������ս��ť�������¼�
		Object o=this.jcbNickList.getSelectedItem();//����û�ѡ�е���ս����
		if(o==null||((String)o).equals("")) {
			JOptionPane.showMessageDialog(this,"��ѡ��Է�����","����",
			JOptionPane.ERROR_MESSAGE);//��δѡ����ս���󣬸���������ʾ��Ϣ
		}
		else{
			String name2=(String)this.jcbNickList.getSelectedItem();//�����ս���������
			try{
				this.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
				this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
				this.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
				this.jbConnect.setEnabled(false);//��"����"��ť��Ϊ������
				this.jbDisconnect.setEnabled(!true);//��"�Ͽ�"��ť��Ϊ������
				this.jbChallenge.setEnabled(!true);//��"��ս"��ť��Ϊ������
				this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
				this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
				this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
				this.cat.tiaoZhanZhe=name2;//������ս����
				this.caiPan=true;//��caiPan��Ϊtrue
				this.color=0;//��color��Ϊ0
				this.cat.dout.writeUTF("<#TIAO_ZHAN#>"+name2);//������ս��Ϣ
				JOptionPane.showMessageDialog(this,"�������ս,��ȴ��ָ�...","��ʾ",
				           JOptionPane.INFORMATION_MESSAGE);//������Ϣ��������ʾ��Ϣ
			}
			catch(Exception ee){ee.printStackTrace();}
		}
	}
	public void jbYChallenge_event(){
		try{	//����ͬ����ս����Ϣ
			this.cat.dout.writeUTF("<#TONG_YI#>"+this.cat.tiaoZhanZhe);
			this.caiPan=false;//��caiPan��Ϊfalse
			this.color=1;//��color��Ϊ1
			this.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);//��"����"��ť��Ϊ������
			this.jbDisconnect.setEnabled(!true);//��"�Ͽ�"��ť��Ϊ������
			this.jbChallenge.setEnabled(!true);//��"��ս"��ť��Ϊ������
			this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
			this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
			this.jbFail.setEnabled(!false);//��"����"��ť��Ϊ����
                        clockthread=new Clock(this);
                        clockthread.start();
                            
		}
		catch(Exception ee){ee.printStackTrace();}
	}
	public void jbNChallenge_event(){
		try{   //���;ܾ���ս����Ϣ
			this.cat.dout.writeUTF("<#BUTONG_YI#>"+this.cat.tiaoZhanZhe);
			this.cat.tiaoZhanZhe=null;//��tiaoZhanZhe��Ϊ��
			this.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);//��"����"��ť��Ϊ������
			this.jbDisconnect.setEnabled(true);//��"�Ͽ�"��ť��Ϊ����
			this.jbChallenge.setEnabled(true);//��"��ս"��ť��Ϊ����
			this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
			this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
			this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
		}
		catch(Exception ee){ee.printStackTrace();}
	}
	public void jbFail_event(){
		try{   //�����������Ϣ
			this.cat.dout.writeUTF("<#RENSHU#>"+this.cat.tiaoZhanZhe);
			this.cat.tiaoZhanZhe=null;//��tiaoZhanZhe��Ϊ��
			this.color=0;//��color��Ϊ0
			this.caiPan=false;//��caiPan��Ϊfalse
			this.next();//��ʼ����һ��
			this.jtfHost.setEnabled(false);//�������������������ı�����Ϊ������
			this.jtfPort.setEnabled(false);//����������˿ںŵ��ı�����Ϊ������
			this.jtfNickName.setEnabled(false);//�����������ǳƵ��ı�����Ϊ������
			this.jbConnect.setEnabled(false);//��"����"��ť��Ϊ������
			this.jbDisconnect.setEnabled(true);//��"�Ͽ�"��ť��Ϊ����
			this.jbChallenge.setEnabled(true);//��"��ս"��ť��Ϊ����
			this.jbYChallenge.setEnabled(false);//��"������ս"��ť��Ϊ������
			this.jbNChallenge.setEnabled(false);//��"�ܾ���ս"��ť��Ϊ������
			this.jbFail.setEnabled(false);//��"����"��ť��Ϊ������
                        clockthread.stop();
                        this.jlMinute.setText("00");
                        this.jlSecond.setText("00");
                        this.walknum=0;
                        this.opsitewalknum=0;
                        
		}
		catch(Exception ee){ee.printStackTrace();}	
	}
        public void jbregive_event() {//���ɻ�һ��
               try{
                   if(this.walknum!=0&&this.opsitewalknum!=0)
                   {
                        this.cat.dout.writeUTF("<#REGIVE#>"+this.cat.tiaoZhanZhe);
                        this.jbregive.setEnabled(false);//��"����"��ť��Ϊ������
                        this.walknum--;
                        this.opsitewalknum--;
                              
                        if(this.color==1)//��������ǻ�
                         {
                             this.jpz.qiZi[this.jpz.posbefore[1][0]][this.jpz.posbefore[1][1]]=this.jpz.qiZi[this.jpz.posbefore[1][2]][this.jpz.posbefore[1][3]];
                             this.jpz.qiZi[this.jpz.posbefore[1][2]][this.jpz.posbefore[1][3]]=null;//����
                             if(this.jpz.posbefore[1][4]==1){
                                 this.qiZi[this.jpz.posbefore[1][2]][this.jpz.posbefore[1][3]-1]=new QiZi(color1,"��",this.jpz.posbefore[1][2],this.jpz.posbefore[1][3]-1);
                                 this.qiZi[this.jpz.posbefore[1][2]][this.jpz.posbefore[1][3]+1]=new QiZi(color1,"��",this.jpz.posbefore[1][2],this.jpz.posbefore[1][3]+1);
                             }
                             if(this.jpz.posbefore[1][5]==1){
                                 this.qiZi[this.jpz.posbefore[1][2]-1][this.jpz.posbefore[1][3]]=new QiZi(color1,"��",this.jpz.posbefore[1][2]-1,this.jpz.posbefore[1][3]);
                                 this.qiZi[this.jpz.posbefore[1][2]+1][this.jpz.posbefore[1][3]]=new QiZi(color1,"��",this.jpz.posbefore[1][2]+1,this.jpz.posbefore[1][3]);
                             }
                             if(this.jpz.posbefore[1][7]==1){
                                 this.qiZi[this.jpz.posbefore[1][2]-1][this.jpz.posbefore[1][3]-1]=new QiZi(color1,"��",this.jpz.posbefore[1][2]-1,this.jpz.posbefore[1][3]-1);
                                 this.qiZi[this.jpz.posbefore[1][2]+1][this.jpz.posbefore[1][3]+1]=new QiZi(color1,"��",this.jpz.posbefore[1][2]+1,this.jpz.posbefore[1][3]+1);
                             }
                             if(this.jpz.posbefore[1][6]==1){
                                 this.qiZi[this.jpz.posbefore[1][2]+1][this.jpz.posbefore[1][3]-1]=new QiZi(color1,"��",this.jpz.posbefore[1][2]+1,this.jpz.posbefore[1][3]-1);
                                 this.qiZi[this.jpz.posbefore[1][2]-1][this.jpz.posbefore[1][3]+1]=new QiZi(color1,"��",this.jpz.posbefore[1][2]-1,this.jpz.posbefore[1][3]+1);
                             }
                            this.jpz.qiZi[this.jpz.opsiteposbefore[1][0]][this.jpz.opsiteposbefore[1][1]]=this.jpz.qiZi[this.jpz.opsiteposbefore[1][2]][this.jpz.opsiteposbefore[1][3]];
                            this.jpz.qiZi[this.jpz.opsiteposbefore[1][2]][this.jpz.opsiteposbefore[1][3]]=null;//���� 
                         }
                         else
                         {
                             
                            this.jpz.qiZi[this.jpz.opsiteposbefore[1][0]][this.jpz.opsiteposbefore[1][1]]=this.jpz.qiZi[this.jpz.opsiteposbefore[1][2]][this.jpz.opsiteposbefore[1][3]];
                            this.jpz.qiZi[this.jpz.opsiteposbefore[1][2]][this.jpz.opsiteposbefore[1][3]]=null;//���� 
                             if(this.jpz.opsiteposbefore[1][4]==1){
                                 this.qiZi[this.jpz.opsiteposbefore[1][2]][this.jpz.opsiteposbefore[1][3]-1]=new QiZi(color1,"��",this.jpz.opsiteposbefore[1][2],this.jpz.opsiteposbefore[1][3]-1);
                                 this.qiZi[this.jpz.opsiteposbefore[1][2]][this.jpz.opsiteposbefore[1][3]+1]=new QiZi(color1,"��",this.jpz.opsiteposbefore[1][2],this.jpz.opsiteposbefore[1][3]+1);
                             }
                             if(this.jpz.opsiteposbefore[1][5]==1){
                                 this.qiZi[this.jpz.opsiteposbefore[1][2]-1][this.jpz.opsiteposbefore[1][3]]=new QiZi(color1,"��",this.jpz.opsiteposbefore[1][2]-1,this.jpz.opsiteposbefore[1][3]);
                                 this.qiZi[this.jpz.opsiteposbefore[1][2]+1][this.jpz.opsiteposbefore[1][3]]=new QiZi(color1,"��",this.jpz.opsiteposbefore[1][2]+1,this.jpz.opsiteposbefore[1][3]);
                             }
                             if(this.jpz.opsiteposbefore[1][7]==1){
                                 this.qiZi[this.jpz.opsiteposbefore[1][2]-1][this.jpz.opsiteposbefore[1][3]-1]=new QiZi(color1,"��",this.jpz.opsiteposbefore[1][2]-1,this.jpz.opsiteposbefore[1][3]-1);
                                 this.qiZi[this.jpz.opsiteposbefore[1][2]+1][this.jpz.opsiteposbefore[1][3]+1]=new QiZi(color1,"��",this.jpz.opsiteposbefore[1][2]+1,this.jpz.opsiteposbefore[1][3]+1);
                             }
                             if(this.jpz.opsiteposbefore[1][6]==1){
                                 this.qiZi[this.jpz.opsiteposbefore[1][2]+1][this.jpz.opsiteposbefore[1][3]-1]=new QiZi(color1,"��",this.jpz.opsiteposbefore[1][2]+1,this.jpz.opsiteposbefore[1][3]-1);
                                 this.qiZi[this.jpz.opsiteposbefore[1][2]-1][this.jpz.opsiteposbefore[1][3]+1]=new QiZi(color1,"��",this.jpz.opsiteposbefore[1][2]-1,this.jpz.opsiteposbefore[1][3]+1);
                             }
                             this.jpz.qiZi[this.jpz.posbefore[1][0]][this.jpz.posbefore[1][1]]=this.jpz.qiZi[this.jpz.posbefore[1][2]][this.jpz.posbefore[1][3]];
                             this.jpz.qiZi[this.jpz.posbefore[1][2]][this.jpz.posbefore[1][3]]=null;//����
                         }
                         
                         this.repaint();
                         for(int i=0;i<8;i++)
                             this.jpz.posbefore[1][i]=this.jpz.posbefore[0][i];
                         for(int i=0;i<8;i++)
                             this.jpz.opsiteposbefore[1][i]=this.jpz.opsiteposbefore[0][i];
                   }
                   else
                   {
                       JOptionPane.showMessageDialog(this,"�㻹û������...","��ʾ",JOptionPane.INFORMATION_MESSAGE);//������Ϣ��������ʾ��Ϣ
                   }
                   this.jpz.focus=false;
               }catch(Exception ee){ee.printStackTrace();}	
        }
	public void next(){
		for(int i=0;i<5;i++){//���������鶼�ÿ�
			for(int j=0;j<7;j++){
				this.qiZi[i][j]=null;
			}
		}
		this.caiPan=false;
		this.initialQiZi();//���³�ʼ������ 
		this.repaint();//�ػ�
	}
	public static void main(String args[])
	{
		new TigerChess();
	}

    
}