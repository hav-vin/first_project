package game;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.net.*;
import javax.sound.sampled.*; 

class Playpage extends JFrame implements ActionListener{
	Container ct;
	JPanel cp, pOut, chat_pan;
	JButton bReady, bExit, bBingo, ready_l, ready_l2, exit_b, dangsu_rd; //1.���� 2.������ 3.���� ��ư
	JLabel lReady, lExit, lBingo; //1.���� 2.������ 3.���� ��ư��
	JLabel pBackground, dangsu, binggo_outpan, binggo_inpan, player_fpan; //1.���ȭ�� 2.��÷���� 3.�������� 4.���������
	JLabel readyANDbingo, bingo_l; //1.����/�����ư 2.����۾� 3.����۾�
	JLabel player_bpan, chat_lb; //1.�÷��̾�â ���� 2.�÷��̾�â ���� 3.ä��â 4.������ ��ư
	ImageIcon bingcon = new ImageIcon("temp_img/twomyung.png"); //������ ������
	
	JLabel player1, player2, player3, player4, player5, player6; 
	JTextField player1_nick, player2_nick, player3_nick, player4_nick, player5_nick, player6_nick;
	JLabel player1_pic, player2_pic, player3_pic, player4_pic, player5_pic, player6_pic; //�÷��̾� ����

	JTextField txtf;
	JTextArea txtArea;
	JScrollPane scroll;
	///////////////////////////////////////////////////////
	char check[][] = new char[5][5];
	JButton bpsu[][] = new JButton[5][5];
	int yesbingo=0;
	boolean bingo;
	String bingoclick = "no";
	String yesyes = "no";

	Random rd = new Random();
	int total = 49;
	String realbingsu = ""; //����� �������
	Font f1 = new Font("Segoe UI Black", Font.PLAIN, 40);
	Font f2 = new Font("Segoe UI Black", Font.BOLD, 70);
	Font f3 = new Font("", Font.PLAIN, 12);

//////////////////////////////////////////////////////////////////////
	Socket s;
	String nick;
	OutputStream os;
	PrintWriter pw;
	Client client;
	int k = 0;
	JLabel tempplayer[] = new JLabel[6];

	ArrayList<String> player_list = new ArrayList<String>();

	Playpage(Socket s, String nick){
		try{
		os = s.getOutputStream();
		pw = new PrintWriter(os, true);
		}
		catch(IOException ie){}
		init();
		this.s = s;
		this.nick = nick;
	}


	void init(){
		efferSound("bgm");
//------------------------------------------------------------------------------ //���� �ǾƷ� �� �г� ����
		cp = new JPanel();
		cp.setBorder(null);
		//setContentPane(cp);
		cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS)); //����->������ ������� ��ġ
		cp.setOpaque(true); //�����ϰ�
//------------------------------------------------------------------------------ //ū�гε� ��Ƶ� �г� ����
		pOut = new JPanel();			
		pOut.setOpaque(true);		
		cp.add(pOut);
		pOut.setLayout(null);
//------------------------------------------------------------------------------ //���ȭ�� ����
		pBackground = new JLabel(new ImageIcon("temp_img/pBackground_3.png"));
		pBackground.setBounds(0, 0, 950, 850); //(0.0)�� 950*850 �������
		//pBackground.setOpaque(true);
		pOut.add(pBackground);
		pOut.setLayout(null);
		pBackground.setLayout(null);
//------------------------------------------------------------------------------ //���� ū �гε� ��ġ
		/*dangsu = new JLabel(new ImageIcon("temp_img/dangsu.jpg")); //��÷�� ���� ���� ����		
		dangsu.setOpaque(false);
		dangsu.setLayout(null);
		dangsu.setBounds(20, 20, 600, 90); //(20.20)�� 600*90 �������*/
		dangsu_rd = new JButton(" ",new ImageIcon("temp_img/dangsu.png"));
		dangsu_rd.setHorizontalTextPosition(JButton.CENTER);
		dangsu_rd.setBounds(20, 20, 600, 90);
		pBackground.add(dangsu_rd);

		//pBackground.add(dangsu);

		
		binggo_outpan = new JLabel(new ImageIcon("temp_img/binggo_outpan.png")); //������ ������		
		binggo_outpan.setOpaque(true);
		binggo_outpan.setBounds(20, 130, 600, 600); //(20.130)�� 600*90 �������
		pBackground.add(binggo_outpan);
		

		binggo_inpan = new JLabel(new ImageIcon("temp_img/binggo_inpan.png"));
		binggo_inpan.setOpaque(false);
		binggo_inpan.setBounds(26, 31, 550, 500); //(25.50)
		binggo_inpan.setLayout(new GridLayout(5,5));
		binggo_outpan.add(binggo_inpan);
		
			//----------------------------------------------------------- //������ �ȿ� ����
			int bsu[] = new int[25]; //�����Լ� �迭

			for(int i = 0; i<25; i++){
				bsu[i] = rd.nextInt(total)+1;
					for(int j = 0; j<i; j++){		
							if(bsu[i] == bsu[j]){
								i--;
							}	
						} 
					}

			for(int i = 0 ; i<5; i++){
				for(int j = 0 ; j<5; j++){
					try{
						check[i][j] = 'x';
					}
					catch(ArrayIndexOutOfBoundsException ae){}
				}
			}


				for(int i = 0; i<5; i++){
					for(int j = 0; j<5; j++){
						int temp = bsu[this.k];
							realbingsu = Integer.toString(temp);
							///////////////////////////////////////////����
							bpsu[i][j] = new JButton(realbingsu, new ImageIcon("temp_img/twomyung.png"));
							bpsu[i][j].setBorderPainted(false);
							bpsu[i][j].setContentAreaFilled(false);
							bpsu[i][j].setFocusPainted(false);
							bpsu[i][j].setOpaque(false);
							//////////////////////////////////////////����
							bpsu[i][j].setFont(f1);
							bpsu[i][j].setHorizontalTextPosition(JButton.CENTER);
							bpsu[i][j].addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e1){									
									try{										
										JButton jbtemp = (JButton)e1.getSource();
											for(int i2=0;i2<5;i2++){
												for(int j2=0;j2<5;j2++){
													if(jbtemp == bpsu[i2][j2]){ //�����ǿ� �ִ� ���� ��������
														if((bpsu[i2][j2].getText()).equals(dangsu_rd.getText())){ //��ġ�Ҷ�
															ImageIcon correct = new ImageIcon("temp_img/correct.gif");
															correct.getImage().flush();
															jbtemp.setIcon(correct);
															check[i2][j2] = 'o';
															checkBingo(check);	
															if(bingo){
																if(yesbingo>=1){																	
																	yesyes = "yes";
																}
																else{																
																	yesbingo = 0;
																	bingoclick = "no";
																	yesyes = "no";
																}
															}
														}
													}															
												}
											}	
										}
									catch(NullPointerException nu){}
									}													
								});
								this.k++;
					}
				}
			
			for(int i = 0; i<5; i++){
				for(int j = 0; j<5; j++){
					binggo_inpan.add(bpsu[i][j]);
				}
			}
			//-----------------------------------------------------------

		readyANDbingo = new JLabel(new ImageIcon("temp_img/readyANDbingo.png")); //����/���� �� ��ư		
		readyANDbingo.setOpaque(false);		
		readyANDbingo.setBounds(120, 750, 400, 90); //(120.730)�� 600*90 �������
		readyANDbingo.setLayout(null);
		pBackground.add(readyANDbingo);


		ready_l = new JButton(new ImageIcon("temp_img/ready_b.png")); //��ư�� "Ready" ��		
		//ready_l.setPressedIcon(new ImageIcon("temp_img/readyANDbingo.png"));
		ready_l.setOpaque(false);		
		ready_l.setBounds(50, 15, 300, 60); //(220.740)�� 200*70 �������
		ready_l.addActionListener(this);
		readyANDbingo.add(ready_l);

		/*bingo_l = new JLabel("Bingo"); //��ư�� "Bingo" ��		
		bingo_l.setOpaque(false);		
		bingo_l.setBounds(220, 740, 200, 70); //(220.740)�� 200*70 �������
		readyANDbingo.add(bingo_l);*/
//------------------------------------------------------------------------------ //������ ū �гε� ��ġ
		player_bpan = new JLabel(new ImageIcon("temp_img/player_bpan.png")); //�÷��̾� �ǵ� ��		
		player_bpan.setOpaque(false);		
		player_bpan.setBounds(640, 15, 290, 355); //(640.15)�� 290*345 �������
		player_bpan.setLayout(null);
		pBackground.add(player_bpan);

		player_fpan = new JLabel(); //�÷��̾� ����	//*�ٸ� ��ġ*//	
		player_fpan.setLayout(new GridLayout(3,2));
		player_fpan.setOpaque(true);
		player_fpan.setBounds(10, 15, 270, 325); //player_bpan��(10.15)�� 270*325 �������
		player_bpan.add(player_fpan);
//------------------------------------------------------------------------------

		/*for(int i = 0; i<6; i++){
			tempplayer[i] = new JLabel("player", new ImageIcon("temp_img/player"+(i+1)+".png"),JLabel.CENTER);
			tempplayer[i].setLayout(new BorderLayout());
			tempplayer[i].setOpaque(true);
			tempplayer[i].setHorizontalTextPosition(JLabel.CENTER);
			player_fpan.add(tempplayer[i]);
		}*/

		player1 = new JLabel(new ImageIcon("temp_img/player1.png"));
		player1.setLayout(new BorderLayout());
		//player1.add(); ->���Ϳ� �׸�
		//player2.add(); ->���ʿ� �г���
		player1.setOpaque(true);
		player_fpan.add(player1);
		
		//=====================================================
		player1_pic = new JLabel(new ImageIcon(""));
		player1.add(player1_pic, BorderLayout.CENTER);
		player1_nick = new JTextField();
		player1_nick.setEditable(false);
		player1_nick.setOpaque(false);
		player1_nick.setForeground(Color.BLACK);
		player1_nick.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		player1.add(player1_nick, BorderLayout.SOUTH);
		//=====================================================

		//-------------------------------------
		player2 = new JLabel(new ImageIcon("temp_img/player2.png"));
		player2.setLayout(new BorderLayout());
		//player1.add(); ->���Ϳ� �׸�
		//player2.add(); ->���ʿ� �г���
		player2.setOpaque(true);
		player_fpan.add(player2);

		//=====================================================
		player2_pic = new JLabel(new ImageIcon(""));
		player2.add(player2_pic, BorderLayout.CENTER);
		player2_nick = new JTextField();
		player2_nick.setEditable(false);
		player2_nick.setOpaque(false);
		player2_nick.setForeground(Color.BLACK);
		player2_nick.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		player2_nick.setHorizontalAlignment(JTextField.CENTER);
		player2.add(player2_nick, BorderLayout.SOUTH);
		//=====================================================

		//-------------------------------------
		player3 = new JLabel(new ImageIcon("temp_img/player3.png"));
		player3.setLayout(new BorderLayout());
		//player1.add(); ->���Ϳ� �׸�
		//player2.add(); ->���ʿ� �г���
		player3.setOpaque(true);
		player_fpan.add(player3);

		//=====================================================
		player3_pic = new JLabel(new ImageIcon(""));
		player3.add(player3_pic, BorderLayout.CENTER);
		player3_nick = new JTextField();
		player3_nick.setOpaque(false);
		player3_nick.setForeground(Color.BLACK);
		player3_nick.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		player3_nick.setEditable(false);
		player3_nick.setHorizontalAlignment(JTextField.CENTER);
		player3.add(player3_nick, BorderLayout.SOUTH);
		//=====================================================

		//-------------------------------------
		player4 = new JLabel(new ImageIcon("temp_img/player4.png"));
		player4.setLayout(new BorderLayout());
		//player1.add(); ->���Ϳ� �׸�
		//player2.add(); ->���ʿ� �г���
		player4.setOpaque(true);
		player_fpan.add(player4);

		//=====================================================
		player4_pic = new JLabel(new ImageIcon(""));
		player4.add(player4_pic, BorderLayout.CENTER);
		player4_nick = new JTextField();
		player4_nick.setOpaque(false);
		player4_nick.setForeground(Color.BLACK);
		player4_nick.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		player4_nick.setEditable(false);
		player4_nick.setHorizontalAlignment(JTextField.CENTER);
		player4.add(player4_nick, BorderLayout.SOUTH);
		//=====================================================

		//-------------------------------------

		player5 = new JLabel(new ImageIcon("temp_img/player5.png"));
		player5.setLayout(new BorderLayout());
		//player1.add(); ->���Ϳ� �׸�
		//player2.add(); ->���ʿ� �г���
		player5.setOpaque(true);
		player_fpan.add(player5);

		//=====================================================
		player5_pic = new JLabel(new ImageIcon(""));
		player5.add(player5_pic, BorderLayout.CENTER);
		player5_nick = new JTextField();
		player5_nick.setOpaque(false);
		player5_nick.setForeground(Color.BLACK);
		player5_nick.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		player5_nick.setEditable(false);
		player5_nick.setHorizontalAlignment(JTextField.CENTER);
		player5.add(player5_nick, BorderLayout.SOUTH);
		//=====================================================

		//-------------------------------------
		player6 = new JLabel(new ImageIcon("temp_img/player6.png"));
		player6.setLayout(new BorderLayout());
		//player1.add(); ->���Ϳ� �׸�
		//player2.add(); ->���ʿ� �г���
		player6.setOpaque(true);
		player_fpan.add(player6);

		//=====================================================
		player6_pic = new JLabel(new ImageIcon(""));
		player6.add(player6_pic, BorderLayout.CENTER);
		player6_nick = new JTextField();
		player6_nick.setOpaque(false);
		player6_nick.setForeground(Color.BLACK);
		player6_nick.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		player6_nick.setEditable(false);
		player6_nick.setHorizontalAlignment(JTextField.CENTER);
		player6.add(player6_nick, BorderLayout.SOUTH);
		//=====================================================
		//-------------------------------------
//------------------------------------------------------------------------------	
		chat_pan = new JPanel();
		chat_pan.setLayout(new BorderLayout());
		chat_pan.setBounds(640, 380, 290, 355);
		pBackground.add(chat_pan);

		txtArea = new JTextArea();
		txtArea.setSize(300, 500);
		txtArea.setBackground(new Color(255,206,11));
		txtArea.setEditable(false);
		txtArea.setLineWrap(true);
		chat_pan.add(txtArea, BorderLayout.CENTER);

		scroll = new JScrollPane(txtArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(0, 0, 290, 340);
		scroll.setBorder(null);
		scroll.setViewportView(txtArea);
		chat_pan.add(scroll);

		txtf = new JTextField();
		txtf.setBackground(Color.WHITE); //new Color(255,204,0)
		txtf.setBounds(640, 400, 100, 30);
		txtf.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String tfmsg = txtf.getText();
				pw.println(tfmsg);
				txtf.setText("");
			}
		});
		
		chat_pan.add(txtf, BorderLayout.SOUTH);
		txtf.setColumns(30);
//------------------------------------------------------------------------------
		exit_b = new JButton(new ImageIcon("temp_img/exit_b.png")); //������ ��ư	
		exit_b.setOpaque(false);		
		exit_b.setBounds(640, 750, 290, 90); //(640.750)�� 290*90 �������
		exit_b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(e.getSource() == exit_b){
					System.exit(0);
				}
			}
		});
		pBackground.add(exit_b);

		ct = new Container();
		ct = getContentPane();
		setContentPane(ct);
		ct.setLayout(new BorderLayout()); 
		ct.add(cp);

		setUI();

	}


	void setUI(){
		setTitle("NeWtRo BiNgO");
		setSize(956, 879); //950*850(6,29)
		setVisible(true);
		setLocation(0,0);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == ready_l){//�����ư ��������
			ready_l2 = new JButton(new ImageIcon("temp_img/bingo_b.png")); //��ư�� "Ready" ��		
			//ready_l2.setPressedIcon(new ImageIcon("temp_img/player_bpan.jpg"));
			readyANDbingo.remove(ready_l);
			ready_l2.setOpaque(false);		
			ready_l2.setBounds(50, 15, 300, 60); //(400.90)�� 300*760 �������
			readyANDbingo.add(ready_l2);
			ready_l2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(e.getSource() == ready_l2){
						bingoclick = "yes";
						if(yesyes.equals("yes") && bingoclick.equals("yes") && yesbingo>=3){
							efferSound("Bingo");
							ImageIcon bingoo = new ImageIcon("temp_img/binggo_inpan2.png");
							bingoo.getImage().flush();
							binggo_inpan.setIcon(bingoo);
							pw.println("%�¸�%");
						}
					}
				}
			});
			pw.println("#�غ�Ϸ�");
		}
	}
///////////////////////////////////////////////////////////////ȿ����
	public void efferSound(String soundname){
		File soundFileName = new File("sounds/" + soundname + ".wav");

		if(soundFileName.exists()){ 
		try{
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFileName);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.loop(0);
			 clip.start();
	  } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
	  } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
	}else{ 
		 System.out.println("File Not Found1!");
		}
	}
//////////////////////////////////////////////////////////////////////
	


	public void checkBingo(char[][] check){
		char g = 'o';
		int diagonal_1 = 0;
		int diagonal_2 = 0;
		yesbingo = 0;
		for(int j=0;j<5;j++){ //������
			if(check[0][j]==g&&check[1][j]==g&&check[2][j]==g&&check[3][j]==g&&check[4][j]==g){	
				bingo = true;
				yesbingo++;
			}
			if(check[j][0]==g&&check[j][1]==g&&check[j][2]==g&&check[j][3]==g&&check[j][4]==g){
				bingo = true;
				yesbingo++;
			}
			if(check[0][0]==g&&check[1][1]==g&&check[2][2]==g&&check[3][3]==g&&check[4][4]==g && diagonal_1==0){//�밢��(��->��)
				bingo = true;
				yesbingo++;
				diagonal_1 = 1;
			}
			if(check[0][4]==g&&check[1][3]==g&&check[2][2]==g&&check[3][1]==g&&check[4][0]==g && diagonal_2==0){//�밢��(��->��)
				bingo = true;
				yesbingo++;
				diagonal_2 = 1;
			}
		}
		if(yesbingo<1){
			bingo = false;
		}
	}
}