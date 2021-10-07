package newFCJ;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.tools.Tool;
import javax.xml.crypto.Data;

import org.omg.CORBA.DynamicImplementation;
//画图得到的所有数据保存在data数组中。
public class Draw extends JFrame implements KeyListener{
	Dframe dff;
	JLabel jLabel;
	int x, y, x1 = 100, y1 = 100, x2 = x1 + 100, y2 = y1 + 100;
	int L;
	int topx = 500;// 盖心顶点横纵坐标
	int topy = 40;
	int gaix = 50;
	int gaiy = 300;
	boolean isan = false;
	ArrayList<Dframe> dfs=new ArrayList<>();

	String LL16 = "2150";// 16根料的长度
	String LL6 = "540";// 回头的长度
	String[] name = { "01盖心", "02盖左1", "03盖左2", "04盖右1", "05盖右2", "06侧左上",
			"07侧左中", "08侧左下", "09侧右上", "10侧右中", "11侧右下", "12底左2", "13底左1",
			"14底中", "15底右1", "16底右2", "17大回头1", "18大回头2", "19大回头3", "20大回头4",
			"21小回头1", "22小回头2", "23小回头3", "24小回头4" };

	double dx[] = { 0, -120, -120, -120, 60, -30, -25, 5, 5, 10, -10, -20, 150,//相对数据
			140, 0, -140, -10, 10, 10, 10, 10, 120 };// 大致画出棺材截面形状
	double dy[] = { 0, 80, 80, 80, 100, 12, 120, 75, 75, 150, 0, 170, 0, 0,
			-170, 0, 0, -150, -150, -172, -4, -22 };
	double[] xx = { 500.0, 380.0, 260.0, 140.0, 200.0, 170.0, 145.0, 150.0,//绝对数据
			155.0, 165.0, 155.0, 135.0, 285.0, 425.0, 425.0, 285.0, 275.0,
			285.0, 295.0, 305.0, 315.0, 435.0 };
	double[] yy = { 40.0, 120.0, 200.0, 280.0, 380.0, 392.0, 512.0, 587.0,
			662.0, 812.0, 812.0, 982.0, 982.0, 982.0, 812.0, 812.0, 812.0,
			662.0, 512.0, 340.0, 336.0, 314.0 };
	double[] jxxx = { 500.0, 620.0, 740.0, 860.0, 800.0, 830.0, 855.0, 850.0,//镜像数据
			845.0, 835.0, 845.0, 865.0, 715.0, 575.0, 575.0, 715.0, 725.0,
			715.0, 705.0, 695.0, 685.0, 565.0 };
	double[] jxyy = { 40.0, 120.0, 200.0, 280.0, 380.0, 392.0, 512.0, 587.0,
			662.0, 812.0, 812.0, 982.0, 982.0, 982.0, 812.0, 812.0, 812.0,
			662.0, 512.0, 340.0, 336.0, 314.0 };

	double jxdx[] = new double[dx.length];// 镜像以后的数据，不需要进行

	double jxdy[] = new double[dx.length];
	boolean[] flag = new boolean[dx.length];
	boolean[] jxflag = new boolean[dx.length];
	int r = 5;
	
	int[] htx=new int[10],hty=new int[10];
	int[] xhtx=new int[10],xhty=new int[10];
	
	
	
	boolean[] htflag=new boolean[10];
	boolean[] xhtflag=new boolean[10];
	boolean htrepaint=false;//回头没有TXT数据为true
	double htqinjiao=Math.PI*75/180;
	double xhtqinjiao=Math.PI*75/180;

	int[][] line = { { 0, 1 }, { 20, 21 }, { 21, 1 }, { 1, 2 }, { 2, 20 },
			{ 4, 20 }, {20,2},{ 2, 3 }, { 3, 4 }, 
			{ 6, 18 }, { 18, 19 },{ 19, 5 }, { 5, 6 }, 
			{ 8, 17 }, { 17, 18 }, { 18, 6 }, { 6, 7 },{ 7, 8 }, 
			{ 9, 16 }, { 16, 17 }, { 17, 8 }, { 8, 9 }, 
			{ 11, 12 },{ 12, 15 }, { 15, 10 }, { 10, 11 }, 
			{ 12, 13 }, { 13, 14 },{ 14, 15 }, { 15, 12 } };// 所有点的连线
	//22个点中，属于凹凸角点的为：0,3,4,5,6,7,9,10,11,16,19,21，其中7比较特殊，如果第七根料为5边形，则该点必须对称，否则不用考虑。
	int[][] jiao = { { 0, 1, 21 }, { 2, 20, 21 }, { 20, 21, 1 }, { 21, 1, 2 },
			{ 1, 2, 20 }, { 3, 4, 20 }, { 4, 20, 2 }, { 20, 2, 3 },
			{ 2, 3, 4 }, { 5, 6, 18 }, { 6, 18, 19 }, { 18, 19, 5 },
			{ 19, 5, 6 }, { 7, 8, 17 }, { 8, 17, 18 }, { 17, 18, 6 },
			{ 18, 6, 7 }, { 6, 7, 8 }, { 8, 9, 16 }, { 9, 16, 17 },
			{ 16, 17, 8 }, { 17, 8, 9 }, { 10, 11, 12 }, { 11, 12, 15 },
			{ 12, 15, 10 }, { 15, 10, 11 }, { 15, 12, 13 }, { 12, 13, 14 },
			{ 13, 14, 15 }, { 14, 15, 12 } };// 所有需要标注的夹角。
	int[][] rjiao={ { 0, 1, 21 }, { 2, 20, 21 }, { 20, 21, 1 }, { 21, 1, 2 },
			{ 1, 2, 20 }, { 3, 4, 20 }, { 4, 20, 2 }, { 20, 2, 3 },
			{ 2, 3, 4 }, { 5, 6, 18 }, { 6, 18, 19 }, { 18, 19, 5 },
			{ 19, 5, 6 }, { 7, 8, 17 }, { 8, 17, 18 }, { 17, 18, 6 },
			{ 18, 6, 7 }, { 6, 7, 8 }, { 8, 9, 16 }, { 9, 16, 17 },
			{ 16, 17, 8 }, { 17, 8, 9 }, { 10, 11, 12 }, { 11, 12, 15 },
			{ 12, 15, 10 }, { 15, 10, 11 }, { 15, 12, 13 }, { 12, 13, 14 },
			{ 13, 14, 15 }, { 14, 15, 12 } };
	
	int[] wn = { 5, 4, 4, 4, 4, 4, 5, 4, 4, 5, 4, 4, 4, 4, 4, 4 };// 每根材料的边数
	//int[] n = { 1, 4, 4, 4, 5, 4, 4, 4 };//？
	double[][] w = new double[24][5];
	double[][] a = new double[24][5];
	double[] jiaodu = new double[jiao.length];
	double[] rjiaodu = new double[jiao.length];
	double[] bianchang = new double[line.length];
	double[] rbianchang = new double[line.length];
	int hh;
	int nnn;
	
	
	
	double[][] htline = new double[4][4];
	double[][] xhtline = new double[4][4];
	
	int[][] htjiao = { { 0, 1, 8 }, { 1, 8, 9 }, { 8, 9, 0 },{9,0,1},
			{ 1, 2, 7 }, { 2, 7, 8}, { 7, 8, 1 }, { 8, 1, 2 },
			{ 2, 3, 6 }, { 3, 6, 7 }, { 6, 7, 2 }, { 7, 2, 3 },
			{ 3, 4, 5 }, { 4, 5, 6 }, { 5, 6, 3 }, { 6, 3, 4 }};// 所有需要标注的夹角。
	
	double[] htjiaodu = new double[htjiao.length];
	double[] xhtjiaodu = new double[htjiao.length];
	double yjdxtc=25;
	double scdxtzc;

	// double jiaodu[]=new double[(jiao.length+4)*2-1];
	DecimalFormat df = new DecimalFormat("######0.00");
	protected double huitouH = 150;
	protected double datiaotoucha = 30;// 大小头差
	protected double[] l = new double[24];
	double[] R = new double[24];
	double[][] data = new double[24][12];//画图所得到的数据最终保存在这里。
	private PrintWriter pw;
	private PrintWriter pwxy;
	double ll;
	double rr;
	protected int mousecouter=0;
	protected int distance;
	double x11,y11;
	int x22,y22;
	int d;
	String tablepathString="D:\\fcjFile\\历史数据\\table";
	File tableFile=new File(tablepathString);
	File[] tableFiles=tableFile.listFiles();
	ArrayList<File> tableFileArrayList=new ArrayList<>();
	String pointspathString="D:\\fcjFile\\历史数据\\points";
	File pointsFile=new File(pointspathString);
	File[] pointsFiles=pointsFile.listFiles();
	ArrayList<File> pointsFileArrayList=new ArrayList<>();
	String huitoupointspathString="D:\\fcjFile\\历史数据\\huitoupoints";
	File huitoupointsFile=new File(huitoupointspathString);
	File[] huitoupointsFiles=huitoupointsFile.listFiles();
	ArrayList<File> huitoupointsFileArrayList=new ArrayList<>();
	JTextField zhiField=null;
	/*
	 * public static void main(String[] args) { new Draw(); }
	 */
	
	private void drawdata2file(File file) {// 主要功能是将画图得到的值写到TXT文档中去
		PrintWriter pw1 = null;
		try {
			pw1 = new PrintWriter(new FileWriter(file));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				pw1.print(data[i][j] + "\t");
			}
		}

		pw1.close();		

	}
    public int getfilesnumb(String path){
    	int n=0;
    	File file=new File(path);
		File[] files=file.listFiles();
		n=files.length;
    	return n;
    }
	public double d2(double a) {// 保留2位小数
		double b = a * 100;
		double c = Math.round(b);
		a = c / 100;
		return a;
	}

	public void jxsj() {// 还余下0和12没有处理镜像数据，0和13是自身对称。这里是指料的序号。
		int[][] duiyin = { { 1, 3 }, { 2, 4 }, { 5, 8 }, { 6, 9 }, { 7, 10 },
				{ 11, 15 }, { 12, 14 } };
		for (int i = 0; i < duiyin.length; i++) {
			jxw(w[duiyin[i][1]], w[duiyin[i][0]]);
			jxa(a[duiyin[i][1]], a[duiyin[i][0]]);
		}
	}
	public double[] string2double(String[] str){
		double[] tt=new double[str.length];
		for (int i = 0; i < tt.length; i++) {
			tt[i]=Double.parseDouble(str[i]);
		}
		return tt;
	}
	public int[] string2int(String[] str){
		int[] tt=new int[str.length];
		for (int i = 0; i < tt.length; i++) {
			tt[i]=Integer.parseInt(str[i]);
		}
		return tt;
	}
	public void fromD2Rom24(BufferedReader fr){//从D盘写入到dx dy jxdx jxdy数组中
		String temp;
		String ss[];
		int i=0;
		try {
			while ((temp=fr.readLine())!=null) {
				ss=temp.split("\t");
				i++;
					int t=ss.length;
					int m=t/12;
					int j=0;
					for (int k = 0; k < m; k++) {
						for (int k2 = 0; k2 <data[0].length; k2++) {
							data[k][k2]=Double.parseDouble(ss[j]);
							j++;
						}
					}
			}
		} catch (IOException e1) {
		}
	}
	public void fromD2Rom(BufferedReader fr){//从D盘写入到dx dy jxdx jxdy数组中
		 String temp;
		 String ss[][] = new String[4][];
		 int i=0;
			 try {
				 while ((temp=fr.readLine())!=null) {
					 ss[i]=temp.split(",");
					 i++;
				 }
				 for (int j = 0; j < ss.length; j++) {
					
						if (j==0) {
							dx=string2double(ss[j]);
						}
						if (j==1) {
							dy=string2double(ss[j]);
						}
						if (j==2) {
							jxdx=string2double(ss[j]);
						}
						if (j==3) {
							jxdy=string2double(ss[j]);
						}
				}
			 } catch (IOException e1) {
			 }
	}
	public void htfromD2Rom(BufferedReader fr){//从D盘写入到dx dy jxdx jxdy数组中
		String temp;
		String ss[][] = new String[4][];
		int i=0;
		try {
			while ((temp=fr.readLine())!=null) {
				ss[i]=temp.split(",");
				i++;
			}
			for (int j = 0; j < ss.length; j++) {
				
				if (j==0) {
					htx=string2int(ss[j]);
				}
				if (j==1) {
					hty=string2int(ss[j]);
				}
				if (j==2) {
					xhtx=string2int(ss[j]);
				}
				if (j==3) {
					xhty=string2int(ss[j]);
				}
			}
		} catch (IOException e1) {
		}
	}
   public void diejiashuju(){
	   dx[0] = topx;
		dy[0] = topy;
		for (int i = 1; i < dx.length; i++) {
		   	dx[i] = dx[i] + dx[i - 1];
			dy[i] = dy[i] + dy[i - 1];

		}
		for (int i = 1; i < dx.length; i++) {
			jxdx[i] = 2 * topx - dx[i];
			jxdy[i] = dy[i];
		}
		jxdx[0] = topx;
		jxdy[0] = topy;
   }
  
	public Draw(final File ff,Dframe df) {
		dff=df;
		dfs.add(dff);
		String headerString=ff.getName();
		String num=fromStrGetNumb(headerString);
		String pointsname=pointspathString+"\\points"+num+".txt";
		String htpointsname=huitoupointspathString+"\\huitoupoints"+num+".txt";
		
		for (int i = 0; i < pointsFiles.length; i++) {
			pointsFileArrayList.add(pointsFiles[i]);
		}
		for (int i = 0; i < tableFiles.length; i++) {
			tableFileArrayList.add(tableFiles[i]);
		}
		for (int i = 0; i < huitoupointsFiles.length; i++) {
			huitoupointsFileArrayList.add(huitoupointsFiles[i]);
		}
		String drawPointsFileNameString=null;
		if (tableFileArrayList.size()>0) {
			drawPointsFileNameString=pointsname;
		}else {
			drawPointsFileNameString="D:\\fcjFile//xy1.txt";
			
		}
		final File drawpointsFile=new File(drawPointsFileNameString);//此处为点集数据
		
		BufferedReader fr = null;
		
		if (drawpointsFile.exists()&&drawpointsFile.length()>0) {
			//System.out.println("把数据从物理储存器写入到内存中来"+f.length());
			//假设原先存贮器上有数据，就将数据读出来，假设没有数据文件，就将上面dx dy数据叠加得到。保存到dx dy jxdx jxdy数组中
			try {
				 fr=new BufferedReader(new FileReader(drawpointsFile));
				 fromD2Rom(fr);//从D盘写入到dx dy jxdx jxdy数组中
				 try {
					fr.close();
				} catch (IOException e1) {
				}
				
			} catch (FileNotFoundException e1) {
			}
		}else{
			diejiashuju();
		}
		String huitouPointsFileNameString=null;
		if (tableFileArrayList.size()>0) {
			huitouPointsFileNameString=htpointsname;
		}else {
			huitouPointsFileNameString="D:\\fcjFile//htxy.txt";
			
		}
		final File htf=new File(huitouPointsFileNameString);
		
		BufferedReader htfr = null;
		if (htf.exists()&&htf.length()>0) {
			//System.out.println("把数据从物理储存器写入到内存中来"+f.length());
			//假设原先存贮器上有数据，就将数据读出来，假设没有数据文件，就将上面dx dy数据叠加得到。保存到dx dy jxdx jxdy数组中
			try {
				htfr=new BufferedReader(new FileReader(htf));
				htfromD2Rom(htfr);
				try {
					htfr.close();
				} catch (IOException e1) {
				}
				
			} catch (FileNotFoundException e1) {
			}
		}else{
			htrepaint=true;
		}
		
      
//至此，数据来了，存放于dx dy jxdx jxdy中
		
		int ww = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		hh = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		JButton baocunjb = new JButton("保存数据");
		
		setTitle(headerString+"图形编辑界面");
		setLayout(null);
		baocunjb.setBounds((int)(ww*0.8)-150, 600, 100, 50);
        JButton fuzhiButton=new JButton("统一赋值");
       
        fuzhiButton.setFont(new java.awt.Font("黑体", 10, 15));
        String LLfile="D:\\fcjFile\\fastfuzhi.txt";
       String tempString=fromFileCopyData(LLfile);
        zhiField=new JTextField(tempString);
        zhiField.setFont(new java.awt.Font("黑体", 10, 20));
        fuzhiButton.setBounds((int)(ww*0.8)-180, 15, 100, 20);
        zhiField.setBounds((int)(ww*0.8-70), 15, 50, 20);
        add(fuzhiButton);
        add(zhiField);
		JLabel[] jls = new JLabel[24];
		final JTextField[] jts = new JTextField[24];
		
		for (int i = 0; i < name.length; i++) {
			jls[i] = new JLabel(name[i]);
			jts[i] = new JTextField();
			jls[i].setBounds((int)(ww*0.8)-150, 20 * i + 50, 100, 20);
			jts[i].setBounds((int)(ww*0.8-70), 20 * i + 50, 50, 20);
			if (df.data[i][data[i].length-1]!=null) {
				jts[i].setText(df.data[i][data[i].length-1]+"");
			}else if(i<16){
				jts[i].setText("2150");
			}else {
				jts[i].setText("550");
			}
			
			add(jls[i]);
			add(jts[i]);
		}

		add(baocunjb);
		
		setBounds(0, 0, (int) (ww * 0.8), hh);
		fuzhiButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("点击了保存按钮");
				String LLv=zhiField.getText();
				for (int i = 0; i < 16; i++) {
					jts[i].setText(LLv);
				}
			}
		});
		this.validate();
		setVisible(true);
	//	问题出在根据图形保存数据时，没有将6和9的180度保存进表格中。

		
		baocunjb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//拿到统一赋值的值，然后保存到txt中。
				String LLLL=zhiField.getText();
				xierutxt("D:\\fcjFile\\fastfuzhi.txt", LLLL);
				int[] n = { 4, 4, 4, 5, 4, 4, 4 };//
				int[] m = { 1, 2, 5, 6, 7, 11, 12 };

				int counter = 0;
				for (int i = 0; i < m.length; i++) {
					for (int j = 0; j < n[i]; j++) {
						counter++;
						a[m[i]][j] = jiaodu[counter];
						if (counter<line.length) {
							
							w[m[i]][j] = bianchang[counter];
						}
						w[12][3]=bianchang[line.length-1];
					}
				}
				a[0][4] = jiaodu[0];
				/*a[0][1] = a[0][0];
				a[0][2] = a[0][4];*/
				a[0][3]=getjiajiao(dx[1], dy[1], dx[0], dy[0], jxdx[1], jxdy[1]);
				a[13][0] = 90;
				a[13][1] = 90;
				a[13][2] = 90;
				a[13][3] = 90;
				a[0][1]=getjiajiao(dx[21], dy[21], jxdx[21], jxdy[21], jxdx[1], jxdy[1]);
				a[0][2]=getjiajiao(jxdx[21], dy[21], jxdx[1], jxdy[1], jxdx[0], jxdy[0]);
				a[3][0]=getjiajiao(jxdx[1], jxdy[1], jxdx[21], jxdy[21], jxdx[20], jxdy[20]);
				a[3][1]=getjiajiao(jxdx[21], jxdy[21], jxdx[20], jxdy[20], jxdx[2], jxdy[2]);
				a[3][2]=getjiajiao(jxdx[20], jxdy[20], jxdx[2], jxdy[2], jxdx[1], jxdy[1]);
				a[3][3]=getjiajiao(jxdx[2], jxdy[2], jxdx[1], jxdy[1], jxdx[21], jxdy[21]);
				a[4][0]=getjiajiao(jxdx[2], jxdy[2], jxdx[20], jxdy[20], jxdx[4], jxdy[4]);
				a[4][3]=getjiajiao(jxdx[3], jxdy[3], jxdx[2], jxdy[2], jxdx[20], jxdy[20]);
				
				w[0][1] =getP2P(jxdx[21],jxdy[21],jxdx[1],jxdy[1]);
				w[0][2] =getP2P(jxdx[0],jxdy[0],jxdx[1],jxdy[1]);
				w[0][3] = bianchang[0];
				w[0][4] = getP2P(dx[1],dy[1],dx[21],dy[21]);
				w[3][1] =getP2P(jxdx[20],jxdy[20],jxdx[2],jxdy[2]);
				w[3][2] =getP2P(jxdx[2],jxdy[2],jxdx[1],jxdy[1]);
				w[3][3] =w[0][1];
				w[4][2] =getP2P(jxdx[2],jxdy[2],jxdx[3],jxdy[3]);
				w[4][3] =w[3][1];
				
				w[13][1] = w[12][1];
				w[13][2] = w[13][0];
				w[13][3] = w[12][3];
				//处理第6根料如果为4边形的问题。
				if (a[6][4]>175&&a[6][4]<181) {
					w[6][3]=w[6][3]+w[6][4];
					w[6][4]=0;
					a[6][4]=0;
				}
				//jxsj();
				
				
				int[] rn = { 4, 4, 4, 5, 4, 4, 4 };//以下为将右边数据写入主页面数据文件
				int[] rm = { 3, 4, 8, 9, 10, 14, 15 };

				int counter1 = 0;
				for (int i = 0; i < rm.length; i++) {
					for (int j = 0; j < rn[i]; j++) {
						counter1++;
						a[rm[i]][j] = rjiaodu[counter1];
						if (counter1<line.length) {
							
							w[rm[i]][j] = rbianchang[counter1];
						}
					}
				}
				for (int i = 0; i < rm.length; i++) {
					changJiaoDu(a[rm[i]]);
					changBianChang(w[rm[i]]);
				}
				exchange(a[rm[5]], a[rm[6]]);
				exchange(w[rm[5]], w[rm[6]]);
				
				w[12][3]=bianchang[line.length-1];
				
				if (a[9][2]>175&&a[9][2]<181) {
					w[9][1]=w[9][1]+w[9][2];
					w[9][2]=w[9][3];
					w[9][3]=w[9][4];
					w[9][4]=0;
					a[9][2]=a[9][3];
					a[9][3]=a[9][4];
					a[9][4]=0;
				}
				
				//图形编辑界面点击保存时，需要将当前的数据保存，同时，要将table中的数据依据图形界面予以更新。
				BufferedReader fr = null;
				File dirfFile=new File("D:\\fcjFile\\历史数据\\table");//这里要保存的是table数据。
				File[] files=dirfFile.listFiles();
			if (files.length>0&&files[0].length()>10) {//如果个性化data数据存在，就读取个性化数据
				String lastFileString=files[files.length-1].getName();
				try {
					fr = new BufferedReader(new FileReader(new File("D:\\fcjFile\\历史数据\\table\\"+files[files.length-1].getName())));
				} catch (FileNotFoundException e) {
				}
				
			}else{//就读取固定data数据
				try {
					fr = new BufferedReader(new FileReader(new File("D:\\fcjFile\\data1.txt")));
				} catch (FileNotFoundException e) {
					System.out.println("使用备用文件生成");
				}
			}
							
				
				fromD2Rom24(fr);//从txt中读到data数组中来。
				
				try {
					fr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						a[16+i][j]=htjiaodu[i*4+j];
						a[20+i][j]=xhtjiaodu[i*4+j];
						w[16+i][j]=htline[i][j];
						w[20+i][j]=xhtline[i][j];
					}
				}
				
				
				for (int i = 0; i < name.length; i++) {
					l[i] = Double.parseDouble(jts[i].getText());
				}
				for (int j = 0; j < data.length; j++) {
					for (int i = 0; i < w[j].length; i++) {
						data[j][i] = bl2ws(w[j][i]);
						data[j][i + 5] = bl2ws(a[j][i]);
					}
					data[j][data[j].length - 2] = l[j];//为调试节约成本，先统一将木料长度定位50
				//	data[j][data[j].length - 2] = 350;
				//	data[j][data[j].length - 1] = 100;
				}
				//print2d(data);
				nnn=tableFileArrayList.size()+1;
				String dataFileNameString="样式"+nnn+".txt";
				String newfilename=tablepathString+"\\样式"+nnn+".txt";
					drawdata2file(new File(newfilename));//数据写到TXT中去，写的data数组中的数,data，写到一个新文件中去。
				// 
				String newpointsString=pointspathString+"\\points"+nnn+".txt";
				
				xieruwulicunchu(newpointsString);//将寿材42点坐标数据写入到TXT中,xy1
				String htpointpathString="D:\\fcjFile\\历史数据\\huitoupoints\\aaaa.txt";
				
				String newhtpointsString=huitoupointspathString+"\\htpoints"+nnn+".txt";
				htxieruwulicunchu(newhtpointsString);
				String fileNameString="D:\\fcjFile\\initFileNameString.txt";
				xierutxt(fileNameString,dataFileNameString);
				//System.out.print("文件名：    "+dataFileNameString);
				//还有一个数据没有保存，就是料的长度数据，需要拿到这些值并且保存到一个硬盘文档中
				PrintWriter LLpw = null;
				try {
					LLpw = new PrintWriter(new FileWriter(new File("D:\\fcjFile\\LLdata.txt")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (int i = 0; i < jts.length; i++) {
					LLpw.print(jts[i].getText()+"\t");
				}
				LLpw.close();
				Draw.this.dispose();
				Dframe df1 = null;
				dispose();
				df1=	new Dframe(newfilename);
				dfs.add(df1);
				for (int i = 0; i < dfs.size()-1; i++) {
					dfs.get(i).dispose();
				}
				
			}
		
		});
		addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
				
			}

			public void mousePressed(MouseEvent e) {
				x = e.getX();
				y = e.getY();
				isan = false;
				for (int i = 0; i < dx.length; i++) {
					flag[i] = false;
					jxflag[i] = false;
				}
				for (int i = 0; i < htx.length; i++) {
					htflag[i] = false;
					xhtflag[i] = false;
				}
				for (int i = 0; i < dx.length; i++) {
					if (x > dx[i] * rr - 5 && x < dx[i] * rr + 5
							&& y > dy[i] * rr - 5 && y < dy[i] * rr + 5) {
						flag[i] = true;
						break;//11111111111111111111111111111111111111111111111111111111
					}
					if (x > jxdx[i] * rr - 5 && x < jxdx[i] * rr + 5
							&& y > jxdy[i] * rr - 5 && y < jxdy[i] * rr + 5) {
						jxflag[i] = true;
					}
				}
				for (int i = 0; i < htx.length; i++) {
					if (x > htx[i] * rr - 5 && x < htx[i] * rr + 5
							&& y > hty[i] * rr - 5 && y < hty[i] * rr + 5) {
						htflag[i] = true;
					}
					if (x > xhtx[i] * rr - 5 && x < xhtx[i] * rr + 5
							&& y > xhty[i] * rr - 5 && y < xhty[i] * rr + 5) {
						xhtflag[i] = true;
					}
				}
			
				repaint();
				if(e.getButton()==e.BUTTON3){
					mousecouter++;
					System.out.println("counter="+mousecouter);
					if (mousecouter%2==1) {
						x11=e.getX();y11=e.getY();
						System.out.println("起点x="+e.getX()+" y="+e.getY());
					}
					if (mousecouter%2==0&&mousecouter>1) {
						 x22=e.getX();y22=e.getY();
						System.out.println("终点x="+e.getX()+" y="+e.getY());
					  distance=(int) Math.pow(((x22-x11)*(x22-x11)+(y22-y11)*(y22-y11)), 0.5);
						System.out.println("距离为："+distance);
						repaint();
					}
				}
				
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseClicked(MouseEvent arg0) {
			}
		});
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				
			}

			public void mouseDragged(MouseEvent e) {
				x = e.getX();
				y = e.getY();
				for (int i = 0; i < dx.length; i++) {
					if (flag[i]||jxflag[i]) {
						repaint();
					}
				}
				for (int i = 0; i < htx.length; i++) {
					if (htflag[i]) {
						repaint();
					}
				}
				for (int i = 0; i < htx.length; i++) {
					if (xhtflag[i]) {
						repaint();
					}
				}
			}
		});
		this.setFocusable(true);
		addKeyListener(this);
		repaint();
	}
	 public  void xierutxt(String filename,String datafilename){//前一个是待打开流的文件名，后一个是待写入的内容
	    	PrintWriter pw;
			try {
				pw = new PrintWriter(new File(filename));
				pw.print(datafilename);
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	    }
	public void DL(Graphics g) {// drawLine,和drawString线段长度值.画左右两边线段和线段长度数字、
		//rr = hh / (dy[11] - topy + 150);//随屏幕大小改变显示尺寸。暂时关闭，令其值为1.
		rr=1;
		for (int i = 0; i < line.length; i++) {//标记左边边长
			g.drawLine((int) (dx[line[i][0]] * rr),
					(int) (dy[line[i][0]] * rr), (int) (dx[line[i][1]] * rr),
					(int) (dy[line[i][1]] * rr));
			g.drawLine((int) (jxdx[line[i][0]] * rr),
					(int) (jxdy[line[i][0]] * rr),
					(int) (jxdx[line[i][1]] * rr),
					(int) (jxdy[line[i][1]] * rr));

			int cx = (int) getcenter(dx[line[i][0]], dy[line[i][0]],
					dx[line[i][1]], dy[line[i][1]])[0];
			int cy = (int) getcenter(dx[line[i][0]], dy[line[i][0]],
					dx[line[i][1]], dy[line[i][1]])[1];
			g.setColor(Color.red);
			bianchang[i] = getP2P(dx[line[i][0]], dy[line[i][0]],
					dx[line[i][1]], dy[line[i][1]]);
			if (i == 11 || i == 24) {
				g.drawString((int) (bianchang[i]) + "", (int) (cx * rr),
						(int) (cy * rr + 10));
			} else {
				g.drawString((int) (bianchang[i]) + "", (int) (cx * rr),
						(int) (cy * rr));
			}
		}
		for (int i = 0; i < line.length; i++) {//标记右边边长
			
			g.drawLine((int) (jxdx[line[i][0]] * rr),
					(int) (jxdy[line[i][0]] * rr),
					(int) (jxdx[line[i][1]] * rr),
					(int) (jxdy[line[i][1]] * rr));

			int cx = (int) getcenter(jxdx[line[i][0]], jxdy[line[i][0]],
					jxdx[line[i][1]], jxdy[line[i][1]])[0];
			int cy = (int) getcenter(jxdx[line[i][0]], jxdy[line[i][0]],
					jxdx[line[i][1]], jxdy[line[i][1]])[1];
			g.setColor(Color.red);
			rbianchang[i] = getP2P(jxdx[line[i][0]], jxdy[line[i][0]],
					jxdx[line[i][1]], jxdy[line[i][1]]);
			if (i == 11 || i == 24) {
				g.drawString((int) (rbianchang[i]) + "", (int) (cx * rr),
						(int) (cy * rr + 10));
			} else {
				g.drawString((int) (rbianchang[i]) + "", (int) (cx * rr),
						(int) (cy * rr));
			}
		}
		
		int cx = (int) getcenter(dx[21], dy[21], jxdx[21], jxdy[21])[0];
		int cy = (int) getcenter(dx[21], dy[21], jxdx[21], jxdy[21])[1];
		w[0][0] = getP2P(dx[21], dy[21], jxdx[21], dy[21]);
		g.drawString((int) (w[0][0]) + "", (int) (cx * rr), (int) (cy * rr));
		cx = (int) getcenter(dx[13], dy[13], jxdx[13], jxdy[13])[0];
		cy = (int) getcenter(dx[13], dy[13], jxdx[13], jxdy[13])[1];
		w[13][0] = getP2P(dx[13], dy[13], jxdx[13], dy[13]);
		g.drawString((int) (w[13][0]) + "", (int) (cx * rr), (int) (cy * rr));
		g.setColor(Color.black);
		g.drawLine((int) (dx[21] * rr), (int) (dy[21] * rr),
				(int) (jxdx[21] * rr), (int) (jxdy[21] * rr));
		g.drawLine((int) (dx[14] * rr), (int) (dy[14] * rr),
				(int) (jxdx[14] * rr), (int) (jxdy[14] * rr));
		g.drawLine((int) (dx[13] * rr), (int) (dy[13] * rr),
				(int) (jxdx[13] * rr), (int) (jxdy[13] * rr));
	}

//draw 中按保存数据时，对于两个侧边没有将180度导入。
	public void paint(Graphics g) {
		super.paint(g);
		for (int i = 0; i < dx.length; i++) {
			if (!flag[i]) {
				g.drawOval((int) ((dx[i] - r / 2) * rr), (int) ((dy[i] -r/2)* rr), r,
						r);
				g.drawOval((int) ((jxdx[i] - r / 2) * rr),
						(int) ((jxdy[i]-r/2) * rr), r, r);
			} else {
				dx[i] = x;
				dy[i] = y;
				if (i == 0) {
					topy = y;
					jxdy[0] = y;
					jxdx[0] = dx[0];
					dx[0] = dx[0];
					dx[2] = getjd(dx[3], dy[3], dx[0], dy[0], dx[2], dy[2],
							dx[20], dy[20])[0];
					dy[2] = getjd(dx[3], dy[3],  dx[0], dy[0], dx[2], dy[2],
							dx[20], dy[20])[1];
					dx[1] = getjd(dx[3], dy[3],  dx[0], dy[0], dx[1], dy[1],
							dx[21], dy[21])[0];
					dy[1] = getjd(dx[3], dy[3], dx[0], dy[0], dx[1], dy[1],
							dx[21], dy[21])[1];
				}
				if (i == 3) {
					dx[3] = x;
					dy[3] = y;
					jxdx[3]=2*dx[0]-dx[3];
					jxdy[3]=dy[3];
					dx[2] = getjd(dx[3], dy[3],  dx[0], dy[0], dx[2], dy[2],
							dx[20], dy[20])[0];
					dy[2] = getjd(dx[3], dy[3],  dx[0], dy[0], dx[2], dy[2],
							dx[20], dy[20])[1];
					dx[1] = getjd(dx[3], dy[3],  dx[0], dy[0], dx[1], dy[1],
							dx[21], dy[21])[0];
					dy[1] = getjd(dx[3], dy[3],  dx[0], dy[0], dx[1], dy[1],
							dx[21], dy[21])[1];
					jxdx[2] = getjd(jxdx[3], jxdy[3],  jxdx[0], jxdy[0], jxdx[2], jxdy[2],
							jxdx[20], jxdy[20])[0];
					jxdy[2] = getjd(jxdx[3], jxdy[3],  jxdx[0], jxdy[0], jxdx[2], jxdy[2],
							jxdx[20], jxdy[20])[1];
					jxdx[1] = getjd(jxdx[3], jxdy[3],  jxdx[0], jxdy[0], jxdx[1], jxdy[1],
							jxdx[21], jxdy[21])[0];
					jxdy[1] = getjd(jxdx[3], jxdy[3],  jxdx[0], jxdy[0], jxdx[1], jxdy[1],
							jxdx[21], jxdy[21])[1];
				}
				if (i == 4) {
					jxdx[4]=2*dx[0]-dx[4];
					jxdy[4]=dy[4];
				}
				if (i == 11) {
					dy[11] = y;
					dy[12] = y;
					dy[13] = y;
					jxdy[11]=y;
					jxdy[12]=y;
					jxdy[13]=y;
					jxdx[11]=2*dx[0]-dx[11];
				}
				
				/*
				
				if (i == 4) {
					dx[4] = getjd(dx[5], dy[5], dx[19], dy[19], dx[4], dy[4],
							dx[3], dy[3])[0];
					dy[4] = getjd(dx[5], dy[5], dx[19], dy[19], dx[4], dy[4],
							dx[3], dy[3])[1];
					dx[19] = getjd(dx[4], dy[4], dx[20], dy[20], dx[19],
							dy[19], dx[18], dy[18])[0];
					dy[19] = getjd(dx[4], dy[4], dx[20], dy[20], dx[19],
							dy[19], dx[18], dy[20])[1];
				}*/
				if (i == 5) {
					jxdx[5]=2*dx[0]-dx[5];
					jxdy[5]=dy[5];
					dx[4] = getjd(dx[5], dy[5], dx[20], dy[20], dx[4], dy[4],
							dx[3], dy[3])[0];
					dy[4] = getjd(dx[5], dy[5], dx[20], dy[20], dx[4], dy[4],
							dx[3], dy[3])[1];
					dx[19] = getjd(dx[5], dy[5], dx[20], dy[20], dx[19],
							dy[19], dx[18], dy[18])[0];
					dy[19] = getjd(dx[5], dy[5], dx[20], dy[20], dx[19],
							dy[19], dx[18], dy[20])[1];
				}
				if (i == 6) {
					dx[6] = x;
					dy[6] = y;
					
					double tempa=getjiajiao(dx[6], dy[6], dx[7], dy[7], dx[8], dy[8]);
						if (tempa>170) {
							dx[7] = getjd(x, y, dx[9], dy[9], dx[7], dy[7], dx[17],
									dy[17])[0];
							dy[7] = getjd(x, y, dx[9], dy[9], dx[7], dy[7], dx[17],
									dy[17])[1];
							dx[8] = getjd(x, y,dx[9],dy[9], dx[8], dy[8], dx[17],
									dy[17])[0];
							dy[8] = getjd(x, y,dx[9],dy[9], dx[8], dy[8], dx[17],
									dy[17])[1];
							dx[18] = getjd(dx[6], dy[6],dx[18],dy[18], dx[16], dy[16], dx[19],
									dy[19])[0];
							dy[18] = y;
							
							jxdx[6] = 2*dx[0]-dx[6];
							jxdy[6] = dy[6];
							jxdx[7] =2*dx[0]-dx[7];
							jxdy[7] = dy[7];
							jxdx[8] =  getjd(jxdx[6], jxdy[6],jxdx[9],jxdy[9], jxdx[17], jxdy[17], jxdx[8],
									jxdy[8])[0];
							jxdy[8] = jxdy[17];
							jxdy[18]=dy[6];
							jxdx[18]=getjd(jxdx[16], jxdy[16],jxdx[19],jxdy[19], jxdx[18], jxdy[18], jxdx[6],
									jxdy[6])[0];
							
						}else{
							dx[6] = getjd(dx[7], dy[7], dx[5], dy[5], dx[6], dy[6], dx[18],
									dy[18])[0];
							dy[6] = getjd(dx[7], dy[7], dx[5], dy[5], dx[6], dy[6], dx[18],
									dy[18])[1];
							dx[8] = getjd(dx[7], dy[7],dx[9],dy[9], dx[8], dy[8], dx[17],
									dy[17])[0];
							dy[8] = getjd(dx[7], dy[7],dx[9],dy[9], dx[8], dy[8], dx[17],
									dy[17])[1];
							dx[18] = getjd(dx[6], dy[6],dx[18],dy[18], dx[16], dy[16], dx[19],
									dy[19])[0];
							dy[18] = y;
							
							jxdx[6] = 2*dx[0]-dx[6];
							jxdy[6] = dy[6];
							
							jxdx[7] = 2*dx[0]-dx[7];
							jxdy[7] = dy[7];
							jxdx[8] = getjd(jxdx[7], jxdy[7],jxdx[9],jxdy[9], jxdx[8], jxdy[8], jxdx[17],
									jxdy[17])[0];
							jxdy[8] = getjd(jxdx[7], jxdy[7],jxdx[9],jxdy[9], jxdx[8], jxdy[8], jxdx[17],
									jxdy[17])[1];
							jxdx[18] = 2*dx[0]-dx[18];
							jxdy[18] = dy[18];
						}
				}
				if (i == 7) {
					dx[7] = x;
					dy[7] = y;
					jxdx[7] = 2*dx[0]-dx[7];
					jxdy[7] = y;
					dx[6] = getjd(dx[6], dy[6], dx[17], dy[17], dx[7], dy[7], dx[5],
							dy[5])[0];
					dy[6] = getjd(dx[6], dy[6], dx[17], dy[17], dx[7], dy[7], dx[5],
							dy[5])[1];
					dx[8] = getjd(dx[7], dy[7],dx[9],dy[9], dx[8], dy[8], dx[17],
							dy[17])[0];
					dy[8] = getjd(dx[7], dy[7],dx[9],dy[9], dx[8], dy[8], dx[17],
							dy[17])[1];
					jxdx[6] = getjd(jxdx[6], jxdy[6], jxdx[17], jxdy[17], jxdx[7], jxdy[7], jxdx[5],
							jxdy[5])[0];
					jxdy[6] = getjd(jxdx[6], jxdy[6], jxdx[17], jxdy[17], jxdx[7], jxdy[7], jxdx[5],
							jxdy[5])[1];
					jxdx[8] = getjd(jxdx[7], jxdy[7],jxdx[9],jxdy[9], jxdx[8], jxdy[8], jxdx[17],
							jxdy[17])[0];
					jxdy[8] = getjd(jxdx[7], jxdy[7],jxdx[9],jxdy[9], jxdx[8], jxdy[8], jxdx[17],
							jxdy[17])[1];
				}
				if (i == 8) {
					dx[8] = x;
					dy[8] = y;
					dx[7] = getjd(dx[7], dy[7], dx[9], dy[9], dx[7], dy[7], dx[17],
							dy[17])[0];
					dy[7] = getjd(dx[7], dy[7], dx[9], dy[9], dx[7], dy[7], dx[17],
							dy[17])[1];
					dx[8] = getjd(dx[7], dy[7],dx[9],dy[9], dx[8], dy[8], dx[17],
							dy[17])[0];
					dy[8] = getjd(dx[7], dy[7],dx[9],dy[9], dx[8], dy[8], dx[17],
							dy[17])[1];
					dx[17] = getjd(dx[8], dy[8],dx[17],dy[17], dx[16], dy[16], dx[19],
							dy[19])[0];
					dy[17] = y;
					
				}
				if (i == 9) {
					dx[9] = getjd(dx[10], dy[10], dx[16], dy[16], dx[6],
							dy[6], dx[9], dy[9])[0];
					dy[9] = getjd(dx[10], dy[10], dx[16], dy[16], dx[6],
							dy[6], dx[9], dy[9])[1];
					jxdx[9]=dx[0]*2-dx[9];
					jxdy[9]=dy[9];
					dx[7] = getjd(dx[7], dy[7], dx[9], dy[9], dx[7], dy[7], dx[17],
							dy[17])[0];
					dy[7] = getjd(dx[7], dy[7], dx[9], dy[9], dx[7], dy[7], dx[17],
							dy[17])[1];
					dx[8] = getjd(dx[7], dy[7],dx[9],dy[9], dx[8], dy[8], dx[17],
							dy[17])[0];
					dy[8] = getjd(dx[7], dy[7],dx[9],dy[9], dx[8], dy[8], dx[17],
							dy[17])[1];
				}
				if (i == 10) {
					dy[9] = y;
					dy[10] = y;
					dy[16] = y;
					dy[15] = y;
					dy[14] = y;
					jxdy[9] = y;
					jxdy[10] = y;
					jxdy[16] = y;
					jxdy[15] = y;
					jxdy[14] = y;
					jxdx[10]=2*dx[0]-dx[10];
				}
				if (i == 12) {
					dx[12] = x;
					dy[12] = dy[11];
					dx[15] = x;
				}
				if (i == 13) {
					dx[13] = x;
					dy[13] = dy[11];
					dx[14] = x;
				}
				if (i == 16) {
					dx[16] = getjd(dx[19],dy[19],dx[16],dy[16],dx[10],
							dy[10],dx[15],dy[15])[0];
					dy[16] = getjd(dx[19],dy[19],dx[16],dy[16],dx[10],
							dy[10],dx[15],dy[15])[1];
					dx[18] = getjd(x, y, dx[19], dy[19], dx[6], dy[6], dx[18],
							dy[18])[0];
					dy[18] = getjd(x, y, dx[19], dy[19], dx[6], dy[6], dx[18],
							dy[18])[1];
					dx[17] = getjd(x, y, dx[19], dy[19], dx[7], dy[7], dx[17],
							dy[17])[0];
					dy[17] = getjd(x, y, dx[19], dy[19], dx[7], dy[7], dx[17],
							dy[17])[1];
					jxdx[16]=2*dx[0]-dx[16];
					jxdy[16]=dy[16];
					jxdx[18] = getjd(jxdx[16],jxdy[16], jxdx[19], jxdy[19], jxdx[6], jxdy[6], jxdx[18],
							jxdy[18])[0];
					jxdy[18] = getjd(jxdx[16],jxdy[16], jxdx[19], jxdy[19], jxdx[6], jxdy[6], jxdx[18],
							jxdy[18])[1];
					jxdx[17] = getjd(jxdx[16],jxdy[16], jxdx[19], jxdy[19], jxdx[7], jxdy[7], jxdx[17],
							jxdy[17])[0];
					jxdy[17] = getjd(jxdx[16],jxdy[16], jxdx[19], jxdy[19], jxdx[7], jxdy[7], jxdx[17],
							jxdy[17])[1];
				}
				if (i==17) {
					dx[17]=getjd(dx[8], dy[8], dx[17], dy[17], 
							dx[19], dy[19], dx[16], dy[16])[0];
					dy[17]=getjd(dx[8], dy[8], dx[17], dy[17], 
							dx[19], dy[19], dx[16], dy[16])[1];
					dx[8]=getjd(dx[8], dy[8], dx[17], dy[17], 
							dx[7], dy[7], dx[9], dy[9])[0];
					dy[8]=y;
					
				}
				if (i==18) {
					
					dx[18]=getjd(dx[6], dy[6], dx[18], dy[18], 
							dx[19], dy[19], dx[16], dy[16])[0];
					dy[18]=getjd(dx[6], dy[6], dx[18], dy[18], 
							dx[19], dy[19], dx[16], dy[16])[1];
					dx[6]=getjd(dx[6], dy[6], dx[18], dy[18], 
							dx[7], dy[7], dx[5], dy[5])[0];
					dy[6]=y;
				}
				if (i == 19) {
					dx[19] = getjd(dx[4], dy[4], dx[20], dy[20], dx[19],
							dy[19], dx[16], dy[16])[0];
					dy[19] =  getjd(dx[4], dy[4], dx[20], dy[20], dx[19],
							dy[19], dx[16], dy[16])[1];
					dx[18] = getjd(dx[19], dy[19], dx[16], dy[16], dx[6], dy[6], dx[18],
							dy[18])[0];
					dy[18] = getjd(dx[19], dy[19], dx[16], dy[16], dx[6], dy[6], dx[18],
							dy[18])[1];
					dx[17] = getjd(dx[19], dy[19], dx[16], dy[16], dx[7], dy[7], dx[17],
							dy[17])[0];
					dy[17] = getjd(dx[19], dy[19], dx[16], dy[16], dx[7], dy[7], dx[17],
							dy[17])[1];
					jxdx[19]=2*dx[0]-dx[19];
					jxdy[19]=dy[19];
					jxdx[18] = getjd(jxdx[19], jxdy[19], jxdx[16], jxdy[16], jxdx[6], jxdy[6], jxdx[18],
							jxdy[18])[0];
					jxdy[18] = getjd(jxdx[19], jxdy[19], jxdx[16], jxdy[16], jxdx[6], jxdy[6], jxdx[18],
							jxdy[18])[1];
					jxdx[17] = getjd(jxdx[19], jxdy[19], jxdx[16], jxdy[16], jxdx[7], jxdy[7], jxdx[17],
							jxdy[17])[0];
					jxdy[17] = getjd(jxdx[19], jxdy[19], jxdx[16], jxdy[16], jxdx[7], jxdy[7], jxdx[17],
							jxdy[17])[1];
					
				}
				
				if (i == 20) {
					dx[4] = getjd(dx[5], dy[5], dx[20], dy[20], dx[4], dy[4],
							dx[3], dy[3])[0];
					dy[4] = getjd(dx[5], dy[5], dx[20], dy[20], dx[4], dy[4],
							dx[3], dy[3])[1];
					dx[19] = getjd(dx[5], dy[5], dx[20], dy[20], dx[19],
							dy[19], dx[18], dy[18])[0];
					dy[19] = getjd(dx[5], dy[5], dx[20], dy[20], dx[19],
							dy[19], dx[18], dy[20])[1];
					jxdx[20]=2*dx[0]-dx[20];
					jxdy[20]=dy[20];
				}
				if (i==21) {
					jxdx[21]=2*dx[0]-dx[21];
					jxdy[21]=dy[21];
				}
				g.drawOval((int) ((dx[i] - r / 2) * rr),
						(int) ((dy[i] - r / 2) * rr), r, r);
				g.drawOval((int) ((jxdx[i] - r / 2) * rr),
						(int) ((jxdy[i] - r / 2) * rr), r, r);
			}
		}
		// double k1=(double)(topy-gaiy)/(topx-gaix);
		// double gaixina3=2*(90-180*Math.atan(-k1)/Math.PI);
		
		
			if (x>jxdx[17]-15&&x<jxdx[17]+15&&y>jxdy[17]-15&&y<jxdy[17]+15) {
				
				jxdy[17]=y;
				jxdy[8]=y;
				jxdx[17]=getjd(jxdx[19], jxdy[19], jxdx[16], jxdy[16],
						jxdx[17], jxdy[17], jxdx[8], jxdy[8])[0];
				jxdy[17]=getjd(jxdx[19], jxdy[19], jxdx[16], jxdy[16],
						jxdx[17], jxdy[17], jxdx[8], jxdy[8])[1];

				double tempa=getjiajiao(dx[6], dy[6], dx[7], dy[7], dx[8], dy[8]);
				if (tempa>175) {
					jxdx[8]=getjd(jxdx[9], jxdy[9], jxdx[6], jxdy[6],
							jxdx[8], jxdy[8], jxdx[17], jxdy[17])[0];
					jxdy[8]=getjd(jxdx[9], jxdy[9], jxdx[6], jxdy[6],
							jxdx[8], jxdy[8], jxdx[17], jxdy[17])[1];
				}else{
					jxdx[8]=getjd(jxdx[9], jxdy[9], jxdx[7], jxdy[7],
							jxdx[8], jxdy[8], jxdx[17], jxdy[17])[0];
					jxdy[8]=getjd(jxdx[9], jxdy[9], jxdx[7], jxdy[7],
							jxdx[8], jxdy[8], jxdx[17], jxdy[17])[1];
				}
			}
			if (x>jxdx[18]-15&&x<jxdx[18]+15&&y>jxdy[18]-15&&y<jxdy[18]+15) {
				double tempa=getjiajiao(dx[6], dy[6], dx[7], dy[7], dx[8], dy[8]);
				if (tempa<170) {
					jxdy[18]=y;
					jxdx[18]=getjd(jxdx[19], jxdy[19], jxdx[16], jxdy[16],
							jxdx[18], jxdy[18], jxdx[6], jxdy[6])[0];
					
				}
				if (tempa>175) {
					jxdx[6]=2*dx[0]-dx[6];
					jxdy[6]=dy[6];
				}else{
					jxdx[6]=getjd(jxdx[5], jxdy[5], jxdx[7], jxdy[7],
							jxdx[6], jxdy[6], jxdx[18], jxdy[18])[0];
					jxdy[6]=jxdy[18];
				}
			}
			if (x>jxdx[13]-15&&x<jxdx[13]+15&&y>jxdy[13]-15&&y<jxdy[13]+15) {
				jxdx[13]=x;
				jxdx[14]=x;
			}
			if (x>jxdx[12]-15&&x<jxdx[12]+15&&y>jxdy[12]-15&&y<jxdy[12]+15) {
				jxdx[12]=x;
				jxdx[15]=x;
			}
			int nb[]={0,3,4,5,7,9,10,11,16,19,20,21};
			for (int j = 0; j < nb.length; j++) {
				jxdx[nb[j]]=2*dx[0]-dx[nb[j]];
				jxdy[nb[j]]=dy[nb[j]];
			}
			if (x>jxdx[1]-15&&x<jxdx[1]+15&&y>jxdy[1]-15&&y<jxdy[1]+15) {
				jxdx[1]=x;
				jxdy[1]=getjd(jxdx[0], jxdy[0], jxdx[3], jxdy[3],
						jxdx[1], jxdy[1], jxdx[21], jxdy[21])[1];
			}
			if (x>jxdx[2]-15&&x<jxdx[2]+15&&y>jxdy[2]-15&&y<jxdy[2]+15) {
				jxdx[2]=x;
				jxdy[2]=getjd(jxdx[0], jxdy[0], jxdx[3], jxdy[3],
						jxdx[2], jxdy[2], jxdx[20], jxdy[20])[1];
			}
		
		DL(g);
		g.drawString("盖心高度" + (dy[21] - dy[0]), (int) (dx[0] * rr),
				(int) (((dy[11] - dy[0]) / 2) * rr));
		g.drawString("内腔上口宽度" + (Math.round(2*(dx[0] - dx[19]))), (int) (dx[0] * rr),
				(int) (((dy[11] - dy[0]) / 2+20) * rr));
		g.drawString("总高度" + (dy[11] - dy[0]), (int) (dx[0] * rr),
				(int) (((dy[11] - dy[0]) / 2 + 60) * rr));
		g.drawString("盖边到边宽度" + (jxdx[3] - dx[3]), (int) (dx[0] * rr),
				(int) (((dy[11] - dy[0]) / 2 + 80) * rr));
		g.drawString("鼓宽度" + (jxdx[7] - dx[7]), (int) (dx[0] * rr),
				(int) (((dy[11] - dy[0]) / 2 + 100) * rr));
		g.drawString("墙子高度" + (dy[16] - dy[19]), (int) (dx[0] * rr),
				(int) (((dy[11] - dy[0]) / 2 + 140) * rr));
		g.drawString("内腔下口宽度" + Math.round((jxdx[16] - dx[16])), (int) (dx[0] * rr),
				(int) ((dy[17] + 50) * rr));
		g.drawString("下底宽度" + Math.round((jxdx[11] - dx[11])), (int) (dx[0] * rr),
				(int) ((dy[17] + 120) * rr));
		a[0][3] = getjiajiao(dx[1], dy[1], dx[0], dy[0], jxdx[1], jxdy[1]);
		g.drawString("" + (int) (a[0][3]) + "", (int) ((dx[0] - 15) * rr),
				(int) ((dy[0] + 30) * rr));

		for (int i = 0; i < jiao.length; i++) {// 在角平分线位置画角度。还差21点的角度。
			int lx = 0, ly = 0;
			int mx = (int) getjpfd(dx[jiao[i][0]], dy[jiao[i][0]],
					dx[jiao[i][2]], dy[jiao[i][2]], dx[jiao[i][1]],
					dy[jiao[i][1]])[0];
			int my = (int) getjpfd(dx[jiao[i][0]], dy[jiao[i][0]],
					dx[jiao[i][2]], dy[jiao[i][2]], dx[jiao[i][1]],
					dy[jiao[i][1]])[1];
			jiaodu[i] = getjiajiaon(jiao[i][0], jiao[i][1], jiao[i][2]);
			g.drawString("" + (int) (jiaodu[i]) + "", (int) (mx * rr),
					(int) (my * rr));
		}
		for (int i = 0; i < jiao.length; i++) {// 标记右边的角度。
			int mx = (int) getjpfd(jxdx[jiao[i][0]], jxdy[jiao[i][0]],
					jxdx[jiao[i][2]], jxdy[jiao[i][2]], jxdx[jiao[i][1]],
					jxdy[jiao[i][1]])[0];
			int my = (int) getjpfd(jxdx[jiao[i][0]], jxdy[jiao[i][0]],
					jxdx[jiao[i][2]], jxdy[jiao[i][2]], jxdx[jiao[i][1]],
					jxdy[jiao[i][1]])[1];
			rjiaodu[i] = getjxajiaon(jiao[i][0], jiao[i][1], jiao[i][2]);
			g.drawString("" + (int) (rjiaodu[i]) + "", (int) (mx * rr),
					(int) (my * rr));
		}
		int lx = 0, ly = 0;
		int mx = (int) getjpfd(dx[1], dy[1], jxdx[21], jxdy[21], dx[21], dy[21])[0];
		int my = (int) getjpfd(dx[1], dy[1], jxdx[21], jxdy[21], dx[21], dy[21])[1];
		a[0][0] = getjiajiao(dx[1], dy[1], dx[21], dy[21], jxdx[21], jxdy[21]);
		g.drawString("" + (int) (a[0][0]) + "", (int) (mx * rr),
				(int) (my * rr));
		int mx1 = (int) getjpfd(dx[21], dy[21], jxdx[1], jxdy[1], jxdx[21], jxdy[21])[0];
		int my1 = (int) getjpfd(dx[21], dy[21], jxdx[1], jxdy[1], jxdx[21], jxdy[21])[1];
		a[0][1] = getjiajiao(dx[21], dy[21], jxdx[21], jxdy[21], jxdx[1], jxdy[1]);
		g.drawString("" + (int) (a[0][1]) + "", (int) (mx1 * rr),
				(int) (my1 * rr));
		/*
		 * g.drawOval(topx, topy, 5, 5); g.drawOval(topx-100, topy+50, 5, 5);
		 * g.drawOval(topx-70, topy+150, 5, 5); g.drawOval(topx+70, topy+150, 5,
		 * 5); g.drawOval(topx+100, topy+50, 5, 5);
		 */
		 g.drawLine((int)x11, (int)y11,x22,y22);
		  String str=distance+"";
		  g.drawString(str, (int)(x11+x22)/2, (int)(y11+y22)/2);
		  //以下为画大回头尺寸，其思路为：先
		  hty[0]=(int) (dy[21]-52);
		  hty[4]=(int)((dy[21]+(dy[14]-dy[21])/Math.sin(htqinjiao)+30));
		  //以下为计算大小回头高度差
		 double a1=Math.PI*(jiaodu[2]-(180-a[0][0]))/180;
		 double a2=Math.PI*(jiaodu[19]-jiaodu[11])/180;
		 double a4=Math.PI*(180-jiaodu[19])/180;
		 
		 double a5=Math.PI*(jiaodu[0]-(180-a[0][0]))/180;
		 double a6=Math.PI*(180-a[0][0])/180;
		 double d1920;
		 if (dx[19]<dx[20]) {
			d1920=getP2P(dx[19], dy[19], dx[20], dy[20]);
		}else{
			d1920=-getP2P(dx[19], dy[19], dx[20], dy[20]);
		}
		 double w20=getP2P(dx[4], dy[4], dx[19], dy[19]);
		  double dxthtgdc=(yjdxtc*d1920*Math.sin(a2)/w20+yjdxtc*Math.sin(a1))+(3*yjdxtc*Math.sin(a4));//大小头回头高度差
		 scdxtzc=dxthtgdc+yjdxtc*(Math.sin(a5+Math.sin(a6)+1));//寿材大小头总差
		 
		  xhty[0]=(int) (dy[21]-32)+(int)dxthtgdc-52;
		  xhty[4]=(int)((dy[21]+(dy[14]-dy[21])/Math.sin(xhtqinjiao)+30));
		 if (htrepaint) {
			  htx[0]=(int)(jxdx[3]+70);htx[9]=(int) (jxdx[3]+150); hty[4]=(int)((dy[21]+(dy[14]-dy[21])/Math.sin(htqinjiao)+20));
			  hty[1]=hty[0]+(hty[4]-hty[0]+27)/4-10;
			  hty[2]=hty[1]+(hty[4]-hty[0]+27)/4+10;
			  hty[3]=hty[2]+(hty[4]-hty[0]+27)/4+10;
			  htx[1]=htx[0];htx[2]=htx[0];htx[3]=htx[0];htx[4]=htx[0];htx[5]=htx[9];htx[6]=htx[9];htx[7]=htx[9];htx[8]=htx[9];
			  hty[5]=hty[4]; hty[6]=hty[3]; hty[7]=hty[2]; hty[8]=hty[1]; hty[9]=hty[0];
			  
			  
			  xhtx[0]=(int)(jxdx[3]+270);xhtx[9]=(int) (jxdx[3]+350);xhty[4]=(int)((dy[21]+(dy[14]-dy[21])/Math.sin(xhtqinjiao)+20));
			  xhty[1]=xhty[0]+(xhty[4]-xhty[0]+27)/4-10;
			  xhty[2]=xhty[1]+(xhty[4]-xhty[0]+27)/4+10;
			  xhty[3]=xhty[2]+(xhty[4]-xhty[0]+27)/4+10;
			  xhtx[1]=xhtx[0];xhtx[2]=xhtx[0];xhtx[3]=xhtx[0];xhtx[4]=xhtx[0];xhtx[5]=xhtx[9];xhtx[6]=xhtx[9];xhtx[7]=xhtx[9];xhtx[8]=xhtx[9];
			  xhty[5]=xhty[4]; xhty[6]=xhty[3]; xhty[7]=xhty[2]; xhty[8]=xhty[1]; xhty[9]=xhty[0];
		}
		   for (int i = 0; i < htx.length; i++) {
			if (htflag[i]) {
				htx[i]=x;
				hty[i]=y;
				if (i == 1) {
					hty[8] = hty[1];
					for (int j = 0; j <5; j++) {
						htx[j]=x;
					}
				}
				if (i == 2) {
					hty[7] = hty[2];
					for (int j = 0; j <5; j++) {
						htx[j]=x;
					}
				}
				if (i == 3) {
					hty[6] = hty[3];
					for (int j = 0; j <5; j++) {
						htx[j]=x;
					}
				}
				if (i == 4) {
					for (int j = 0; j <5; j++) {
						htx[j]=x;
					}
				}
				
				if (i == 8) {
					hty[1] = hty[8];
				}
				if (i == 7) {
					hty[2] = hty[7];
				}
				if (i == 6) {
					hty[3] = hty[6];
				}
			}
		}
		   for (int i = 0; i < xhtx.length; i++) {
			   if (xhtflag[i]) {
				   xhtx[i]=x;
				   xhty[i]=y;
				   if (i == 1) {
					   xhty[8] = xhty[1];
					   for (int j = 0; j <5; j++) {
						   xhtx[j]=x;
					   }
				   }
				   if (i == 2) {
					   xhty[7] = xhty[2];
					   for (int j = 0; j <5; j++) {
						   xhtx[j]=x;
					   }
				   }
				   if (i == 3) {
					   xhty[6] = xhty[3];
					   for (int j = 0; j <5; j++) {
						   xhtx[j]=x;
					   }
				   }
				   if (i == 4) {
					   for (int j = 0; j <5; j++) {
						   xhtx[j]=x;
					   }
				   }
				   
				   if (i == 8) {
					   xhty[1] = xhty[8];
				   }
				   if (i == 7) {
					   xhty[2] = xhty[7];
				   }
				   if (i == 6) {
					   xhty[3] = xhty[6];
				   }
			   }
		   }
		      hty[0]=(int) (dy[21]-52);
		      hty[4]=(int)((dy[21]+(dy[14]-dy[21])/Math.sin(htqinjiao)+20));
			  hty[9]=hty[0];
			  hty[5]=hty[4];
			  

			  xhty[0]=(int) (dy[21]-12)+(int)dxthtgdc-52;
			  xhty[4]=(int)((dy[21]+(dy[14]-dy[21])/Math.sin(xhtqinjiao)+20));
			 xhty[9]=xhty[0];
			  xhty[5]=xhty[4];
			/*for (int i = 0; i < htx.length; i++) {
				g.drawOval(htx[i]-2, hty[i]-2, 4, 4);
				//htPoints[i].x=htx[i];htPoints[i].y=hty[i];
			}
			for (int i = 0; i < xhtx.length; i++) {
				g.drawOval(xhtx[i]-2, xhty[i]-2, 4, 4);
			}*/
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 2; j++) {
					if (j<1) {
						g.drawOval(htx[i]-2, hty[i]-2, 4, 4);
						g.drawOval(htx[i+1]-2, hty[i+1]-2, 4, 4);
						g.drawLine(htx[i], hty[i], htx[i+1], hty[i+1]);
						htline[i][3]=(int)getP2P(htx[i], hty[i], htx[i+1], hty[i+1]);
						g.drawString(""+htline[i][3], (int)getcenter(htx[i], hty[i], htx[i+1], hty[i+1])[0],(int)getcenter(htx[i], hty[i], htx[i+1], hty[i+1])[1]);
					}else{
						int a=8-i;
						g.drawOval(htx[a]-2, hty[a]-2, 4, 4);
						g.drawOval(htx[a+1]-2, hty[a+1]-2, 4, 4);
						g.drawLine(htx[i+1], hty[i+1], htx[a], hty[a]);
						htline[i][0]=(int)getP2P(htx[i+1], hty[i+1], htx[a], hty[a]);
						g.drawString(""+htline[i][0], (int)getcenter(htx[i+1], hty[i+1], htx[a], hty[a])[0],(int)getcenter(htx[i+1], hty[i+1], htx[a], hty[a])[1]);
						g.drawLine(htx[a], hty[a], htx[a+1], hty[a+1]);
						htline[i][1]=(int)getP2P(htx[a], hty[a], htx[a+1], hty[a+1]);
						g.drawString(""+htline[i][1], (int)getcenter(htx[a], hty[a], htx[a+1], hty[a+1])[0],(int)getcenter(htx[a], hty[a], htx[a+1], hty[a+1])[1]);
						g.drawLine(htx[a+1], hty[a+1], htx[i], hty[i]);
						htline[i][2]=(int)getP2P(htx[a+1], hty[a+1], htx[i], hty[i]);
						g.drawString(""+htline[i][2], (int)getcenter(htx[a+1], hty[a+1], htx[i], hty[i])[0],(int)getcenter(htx[a+1], hty[a+1], htx[i], hty[i])[1]);
					}
				}
				
			}
			//画小回头
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 2; j++) {
					if (j<1) {
						g.drawOval(xhtx[i]-2, xhty[i]-2, 4, 4);
						g.drawOval(xhtx[i+1]-2, xhty[i+1]-2, 4, 4);
						g.drawLine(xhtx[i], xhty[i], xhtx[i+1], xhty[i+1]);
						xhtline[i][3]=(int)getP2P(xhtx[i], xhty[i], xhtx[i+1], xhty[i+1]);
						g.drawString(""+xhtline[i][3], (int)getcenter(xhtx[i], xhty[i], xhtx[i+1], xhty[i+1])[0],(int)getcenter(xhtx[i], xhty[i], xhtx[i+1], xhty[i+1])[1]);
					}else{
						int a=8-i;
						g.drawOval(xhtx[a]-2, xhty[a]-2, 4, 4);
						g.drawOval(xhtx[a+1]-2, xhty[a+1]-2, 4, 4);
						g.drawLine(xhtx[i+1], xhty[i+1], xhtx[a], xhty[a]);
						xhtline[i][0]=(int)getP2P(xhtx[i+1], xhty[i+1], xhtx[a], xhty[a]);
						g.drawString(""+xhtline[i][0], (int)getcenter(xhtx[i+1], xhty[i+1], xhtx[a], xhty[a])[0],(int)getcenter(xhtx[i+1], xhty[i+1], xhtx[a], xhty[a])[1]);
						g.drawLine(xhtx[a], xhty[a], xhtx[a+1], xhty[a+1]);
						xhtline[i][1]=(int)getP2P(xhtx[a], xhty[a], xhtx[a+1], xhty[a+1]);
						g.drawString(""+xhtline[i][1], (int)getcenter(xhtx[a], xhty[a], xhtx[a+1], xhty[a+1])[0],(int)getcenter(xhtx[a], xhty[a], xhtx[a+1], xhty[a+1])[1]);
						g.drawLine(xhtx[a+1], xhty[a+1], xhtx[i], xhty[i]);
						xhtline[i][2]=(int)getP2P(xhtx[a+1], xhty[a+1], xhtx[i], xhty[i]);
						g.drawString(""+xhtline[i][2], (int)getcenter(xhtx[a+1], xhty[a+1], xhtx[i], xhty[i])[0],(int)getcenter(xhtx[a+1], xhty[a+1], xhtx[i], xhty[i])[1]);
					}
				}
				
			}
			//角平分线上标注角度。
			g.setColor(Color.blue);
		//	System.out.println("夹角1"+getjiajiao();
			for (int i = 0; i < htjiao.length; i++) {// 在角平分线位置画角度。
				
				int htmx = (int) getjpfd(htx[htjiao[i][0]], hty[htjiao[i][0]],
						htx[htjiao[i][2]], hty[htjiao[i][2]], htx[htjiao[i][1]],
						hty[htjiao[i][1]])[0];
				int htmy = (int) getjpfd(htx[htjiao[i][0]], hty[htjiao[i][0]],
						htx[htjiao[i][2]], hty[htjiao[i][2]], htx[htjiao[i][1]],
						hty[htjiao[i][1]])[1];
				htjiaodu[i] = gethtjiajiaon(htjiao[i][0], htjiao[i][1], htjiao[i][2]);                                           
				g.drawString("" + (int) (htjiaodu[i]) + "", (int) (htmx * rr),
						(int) (htmy * rr));
			}
			
			for (int i = 0; i < htjiao.length; i++) {// 在角平分线位置画角度。
				
				int htmx = (int) getjpfd(xhtx[htjiao[i][0]], xhty[htjiao[i][0]],
						xhtx[htjiao[i][2]], xhty[htjiao[i][2]], xhtx[htjiao[i][1]],
						xhty[htjiao[i][1]])[0];
				int htmy = (int) getjpfd(xhtx[htjiao[i][0]], xhty[htjiao[i][0]],
						xhtx[htjiao[i][2]], xhty[htjiao[i][2]], xhtx[htjiao[i][1]],
						xhty[htjiao[i][1]])[1];
				xhtjiaodu[i] = getxhtjiajiaon(htjiao[i][0], htjiao[i][1], htjiao[i][2]);                                           
				g.drawString("" + (int) (xhtjiaodu[i]) + "", (int) (htmx * rr),
						(int) (htmy * rr));
			}
	}

	public double[] getjd(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		double[] jd = new double[2];
		double x = (x1 * x3 * y2 - x1 * x3 * y4 - x1 * x4 * y2 + x1 * x4 * y3
				- x2 * x3 * y1 + x2 * x3 * y4 + x2 * x4 * y1 - x2 * x4 * y3)
				/ (x1 * y3 - x1 * y4 - x2 * y3 + x2 * y4 - x3 * y1 + x3 * y2
						+ x4 * y1 - x4 * y2);
		double y = (x1 * y2 * y3 - x1 * y2 * y4 - x2 * y1 * y3 + x2 * y1 * y4
				- x3 * y1 * y4 + x3 * y2 * y4 + x4 * y1 * y3 - x4 * y2 * y3)
				/ (x1 * y3 - x1 * y4 - x2 * y3 + x2 * y4 - x3 * y1 + x3 * y2
						+ x4 * y1 - x4 * y2);
		jd[0] = x;
		jd[1] = y;
		return jd;
	}

	public double[] getjpfd(double x1, double y1, double x2, double y2,//解角平分线
			double x0, double y0) {
		double t[] = new double[2];
		double t1[] = new double[2];
		double x;
		double y;
		double v = 0.5;
		double s = getP2P(x1, y1, x0, y0) / getP2P(x0, y0, x2, y2);
		double a0 = getP2P(x1, y1, x2, y2);
		x = (x1 + s * x2) / (1 + s);
		y = (y1 + s * y2) / (1 + s);
		t1[0] = x;
		t1[1] = y;
		t[0] = (x0 + v * x) / (1 + v);
		t[1] = (y0 + v * y) / (1 + v);
		return t;
	}

	public double getjiajiaon(int p1, int p0, int p3) {
		double ang = 0;
		double a = getP2P(dx[p1], dy[p1], dx[p3], dy[p3]);
		double b = getP2P(dx[p1], dy[p1], dx[p0], dy[p0]);
		double c = getP2P(dx[p3], dy[p3], dx[p0], dy[p0]);
		double z = (b * b + c * c - a * a) / (2 * b * c);
		double e = 180 * Math.acos(z) / Math.PI;
		double cc=bl2ws(e);
		return e;
	}
	public double getjxajiaon(int p1, int p0, int p3) {
		double ang = 0;
		double a = getP2P(jxdx[p1], jxdy[p1], jxdx[p3], jxdy[p3]);
		double b = getP2P(jxdx[p1], jxdy[p1], jxdx[p0], jxdy[p0]);
		double c = getP2P(jxdx[p3], jxdy[p3], jxdx[p0], jxdy[p0]);
		double z = (b * b + c * c - a * a) / (2 * b * c);
		double e = 180 * Math.acos(z) / Math.PI;
		double cc=bl2ws(e);
		return e;
	}
	public double gethtjiajiaon(int p1, int p0, int p3) {
		double ang = 0;
		double a = getP2P(htx[p1], hty[p1], htx[p3], hty[p3]);
		double b = getP2P(htx[p1], hty[p1], htx[p0], hty[p0]);
		double c = getP2P(htx[p3], hty[p3], htx[p0], hty[p0]);
		double z = (b * b + c * c - a * a) / (2 * b * c);
		double e = 180 * Math.acos(z) / Math.PI;
		double cc=bl2ws(e);
		return e;
	}
	public double getxhtjiajiaon(int p1, int p0, int p3) {
		double ang = 0;
		double a = getP2P(xhtx[p1], xhty[p1], xhtx[p3], xhty[p3]);
		double b = getP2P(xhtx[p1], xhty[p1], xhtx[p0], xhty[p0]);
		double c = getP2P(xhtx[p3], xhty[p3], xhtx[p0], xhty[p0]);
		double z = (b * b + c * c - a * a) / (2 * b * c);
		double e = 180 * Math.acos(z) / Math.PI;
		double cc=bl2ws(e);
		return e;
	}

	public double getjiajiao(double x1, double y1, double x0, double y0,
			double x3, double y3) {
		double ang = 0;
		double a = getP2P(x1, y1, x3, y3);
		double b = getP2P(x1, y1, x0, y0);
		double c = getP2P(x3, y3, x0, y0);
		double z = (b * b + c * c - a * a) / (2 * b * c);
		double e = 180 * Math.acos(z) / Math.PI;
		double cc = bl2ws(e);
		return e;

	}

	public double[] getChuiCu(double x1, double y1, double x2, double y2,
			double i, double j) {// 需要铣槽的边，必须拿到垂足，再拿到边中点到垂足的距离
		double x = (i * x1 * x1 - 2 * i * x1 * x2 + i * x2 * x2 + j * x1 * y1
				- j * x1 * y2 - j * x2 * y1 + j * x2 * y2 - x1 * y1 * y2 + x1
				* y2 * y2 + x2 * y1 * y1 - x2 * y1 * y2)
				/ (x1 * x1 - 2 * x1 * x2 + x2 * x2 + y1 * y1 - 2 * y1 * y2 + y2
						* y2);

		double y = (i * x1 * y1 - i * x1 * y2 - i * x2 * y1 + i * x2 * y2 + j
				* y1 * y1 - 2 * j * y1 * y2 + j * y2 * y2 + x1 * x1 * y2 - x1
				* x2 * y1 - x1 * x2 * y2 + x2 * x2 * y1)
				/ (x1 * x1 - 2 * x1 * x2 + x2 * x2 + y1 * y1 - 2 * y1 * y2 + y2
						* y2);
		double[] f = new double[2];
		f[0] = x;
		f[1] = y;
		return f;
	}

	public Icon getSizePicture(String str, int w, int h) {
		ImageIcon icon = new ImageIcon(str);
		icon.setImage((icon).getImage().getScaledInstance(w, h,
				Image.SCALE_DEFAULT));
		return icon;
	}

	public void print2d(String a[][]) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(i + "--------");
			for (int j = 0; j < a[i].length; j++) {
				System.out.print("  " + a[i][j]);
			}
			System.out.println();
		}
	}

	public double getP2P(double x1, double y1, double x2, double y2) {// 拿到两点间的距离
		double d = Math.pow(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)),
				0.5);
		//d=bl2ws(d);
		return d;
	}

	public double[] getcenter(double x1, double y1, double x2, double y2) {
		double f[] = new double[2];
		f[0] = (x1 + x2) / 2;
		f[1] = (y1 + y2) / 2;
		return f;
	}

	public void print2d(double a[][]) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(i + "--------");
			for (int j = 0; j < a[i].length; j++) {
				System.out.print("  " + a[i][j]);
			}
			System.out.println();
		}
	}
	public double bl2ws(double d){
		double x=0.0;
		d=Math.round(d*10000);
		x=d/10000;
		return x;
	}

	public void jxw(double[] w, double[] temp) {
		if ((temp[w.length - 1]) == 0) {
			w[0] = temp[0];
			w[1] = temp[3];
			w[2] = temp[2];
			w[3] = temp[1];
		}
		if ((temp[w.length - 1]) != 0) {
			w[0] = temp[0];
			w[1] = temp[4];
			w[2] = temp[3];
			w[3] = temp[2];
			w[4] = temp[1];
		}
	}

	public void jxa(double[] a, double[] temp) {
		if ((temp[a.length - 1]) == 0) {
			a[0] = temp[1];
			a[1] = temp[0];
			a[2] = temp[3];
			a[3] = temp[2];
		}
		if ((temp[a.length - 1]) != 0) {
			a[0] = temp[1];
			a[1] = temp[0];
			a[2] = temp[4];
			a[3] = temp[3];
			a[4] = temp[2];
		}
	}
	public void xieruwulicunchu(String pointpath) {
		try {
			pwxy=new PrintWriter(pointpath);//points数据
		//	System.out.print("写入到       "+pointpath);
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("有问题");
			e1.printStackTrace();
		}
		for (int i = 0; i < dx.length; i++) {
			if (i < dx.length - 1) {
				pwxy.print(dx[i] + ",");
			} else {
				pwxy.print(dx[i]);
			}
		}
		pwxy.println();
		for (int i = 0; i < dx.length; i++) {
			if (i < dx.length - 1) {
				pwxy.print(dy[i] + ",");
			} else {
				pwxy.print(dy[i]);
			}
		}
		pwxy.println();
		for (int i = 0; i < dx.length; i++) {
			if (i < dx.length - 1) {
				pwxy.print(jxdx[i] + ",");
			} else {
				pwxy.print(jxdx[i]);
			}
		}
		pwxy.println();
		for (int i = 0; i < dx.length; i++) {
			if (i < dx.length - 1) {
				pwxy.print(jxdy[i] + ",");
			} else {
				pwxy.print(jxdy[i]);
			}
		}
		pwxy.println();
		pwxy.close();
	}
	public void htxieruwulicunchu(String path) {
		PrintWriter htpWriter=null;
		try {
			htpWriter=new PrintWriter(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < htx.length; i++) {
			if (i < htx.length - 1) {
				htpWriter.print(htx[i] + ",");
			} else {
				htpWriter.print(htx[i]);
			}
		}
		htpWriter.println();
		for (int i = 0; i < htx.length; i++) {
			if (i < htx.length - 1) {
				htpWriter.print(hty[i] + ",");
			} else {
				htpWriter.print(hty[i]);
			}
		}
		htpWriter.println();
		for (int i = 0; i < htx.length; i++) {
			if (i < htx.length - 1) {
				htpWriter.print(xhtx[i] + ",");
			} else {
				htpWriter.print(xhtx[i]);
			}
		}
		htpWriter.println();
		for (int i = 0; i < htx.length; i++) {
			if (i < htx.length - 1) {
				htpWriter.print(xhty[i] + ",");
			} else {
				htpWriter.print(xhty[i]);
			}
		}
		htpWriter.println();
		htpWriter.close();
	}
	public void changJiaoDu(double[] a){
		double[] temp=new double[a.length];
		for (int i = 0; i < temp.length; i++) {
			if (i==0) {
				temp[i]=a[1];
			}
			else if (i==1) {
				temp[i]=a[0];
			}else{
				temp[i]=a[a.length-i+1];
			}
		}
		for (int i = 0; i < temp.length; i++) {
			a[i]=temp[i];
		}
		diuDiaoDiNGe(a, 2);
	}
	
	public static void diuDiaoDiNGe(double[] a,int n){
		double[] tt=a;
		if (a[n]==0.0) {
			for (int i = 0; i <n; i++) {
				a[i]=tt[i];
			}
			for (int i = n; i < tt.length-1; i++) {
				a[i]=tt[i+1];
			}
			a[a.length-1]=0;
		}
	}
	public static void exchange(double[]a,double[] b){
		double[] t=new double[a.length];
		for (int i = 0; i < t.length; i++) {
			t[i]=a[i];
			a[i]=b[i];
			b[i]=t[i];
		}
	}
	
	public void changBianChang(double[] a){
		double[] temp=new double[a.length];
		for (int i = 0; i < temp.length; i++) {
			if (i==0) {
				temp[i]=a[i];
			}else{
				temp[i]=a[a.length-i];
			}
		}
		for (int i = 0; i < temp.length; i++) {
			a[i]=temp[i];
		}
		diuDiaoDiNGe(a, 1);
	//	exchange(a[14], a[15]);
	}
	/*class paintthread implements Runnable {

		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}*/

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					y--;
					repaint();
					break;
				case KeyEvent.VK_DOWN:
					y++;
					repaint();
					break;
				case KeyEvent.VK_RIGHT:
					x++;
					repaint();
					break;
				case KeyEvent.VK_LEFT:
					x--;
					repaint();
					break;
				default:
					break;
				}
			}
	public void keyReleased(KeyEvent arg0) {
	}
	public void keyTyped(KeyEvent arg0) {
	}


	public String fromStrGetNumb(String str) {
		String str2="";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
				 str2 += str.charAt(i);
			}
		}
		return str2;
	}
	private String fromFileCopyData(String file)  {// 主要功能是将TXT文档中的数据写到data中来。返回一个单一的值
		String[] ss=null;
		// File file = new File("D:\\fcjFile\\data1.txt");
		
		BufferedReader br1;
		try {
			br1 = new BufferedReader(new FileReader(new File(
					file)));
			String line = null;
			while ((line = br1.readLine()) != null) {
				ss = line.split("\\t");
			}
			
			br1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ss[0];
	}
}
