package wyf.cgq;

public class GuiZe
{
	QiZi[][] qiZi;//�������ӵ�����
	boolean canMove=false;
	int i;
	int j;
        int[][] canmove={{0,0,4,0,8,0,2},{0,8,0,9,0,9,0},{6,0,9,0,9,0,5},{0,7,0,9,0,9,0},{0,0,3,0,7,0,1}};
	public GuiZe(QiZi[][] qiZi)
	{
		this.qiZi=qiZi;
	}
	public boolean canMove(int startI,int startJ,int endI,int endJ)
	{
            canMove=false;
            int a=Math.abs(endI-startI);
            int b=Math.abs(endJ-startJ);
            int c=endI-startI;
            int d=endJ-startJ;
            if((a==0&&b==1)||(a==1&&b==0))
            {
                if((startI==1&&startJ==2&&c==0&&d==-1)||(startI==3&&startJ==2&&c==0&&d==-1))
                    canMove=false;
                else
                    canMove=true;
            }
            if((a==1)&&(b==1))//��б��
            {
                    if(d>0)//����
                    {
                        if(c>0)//��������
                        {
                            if((canmove[startI][startJ]==4)||(canmove[startI][startJ]==8)||(canmove[startI][startJ]==6)||(canmove[startI][startJ]==9))
                                canMove=true;
                        }
                        else//��������
                        {
                            if((canmove[startI][startJ]==3)||(canmove[startI][startJ]==7)||(canmove[startI][startJ]==6)||(canmove[startI][startJ]==9))
                                canMove=true;
                        }
                    }
                    else//������
                    {
                        if(c>0)//��������
                        {
                            if((canmove[startI][startJ]==2)||(canmove[startI][startJ]==8)||(canmove[startI][startJ]==5)||(canmove[startI][startJ]==9))
                                canMove=true;
                        }
                        else//��������
                        {
                            if((canmove[startI][startJ]==1)||(canmove[startI][startJ]==7)||(canmove[startI][startJ]==5)||(canmove[startI][startJ]==9))
                                canMove=true;
                        }
                    }
            }
		
		return canMove;
	}
	
	
 }