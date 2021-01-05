package wyf.cgq;

public class GuiZe
{
	QiZi[][] qiZi;//声明棋子的数组
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
            if((a==1)&&(b==1))//走斜线
            {
                    if(d>0)//向下
                    {
                        if(c>0)//向右下走
                        {
                            if((canmove[startI][startJ]==4)||(canmove[startI][startJ]==8)||(canmove[startI][startJ]==6)||(canmove[startI][startJ]==9))
                                canMove=true;
                        }
                        else//向左下走
                        {
                            if((canmove[startI][startJ]==3)||(canmove[startI][startJ]==7)||(canmove[startI][startJ]==6)||(canmove[startI][startJ]==9))
                                canMove=true;
                        }
                    }
                    else//向上走
                    {
                        if(c>0)//向右上走
                        {
                            if((canmove[startI][startJ]==2)||(canmove[startI][startJ]==8)||(canmove[startI][startJ]==5)||(canmove[startI][startJ]==9))
                                canMove=true;
                        }
                        else//向左上走
                        {
                            if((canmove[startI][startJ]==1)||(canmove[startI][startJ]==7)||(canmove[startI][startJ]==5)||(canmove[startI][startJ]==9))
                                canMove=true;
                        }
                    }
            }
		
		return canMove;
	}
	
	
 }