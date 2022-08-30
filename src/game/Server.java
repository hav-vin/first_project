package game;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*; 
//import javafx.scene.media.*; 

public class Server extends JFrame implements ActionListener, Runnable{
	JPanel cp, mainP, txtP, btnP;
	JScrollPane scrollPane;
	JTextArea txtArea;
	JLabel server;
	JButton btnStart, btnClose;
	ServerSocket ss;
	Socket s;
	int port = 3333;
	int score;
	boolean gameStart;
	String line = "";

	int count = 0;
	int index = 0;

	GameManager gamemaganer;

	String userIP = "";	
	Hashtable<Socket, String> clientList = new Hashtable<Socket, String>(); //소켓, ip
	TreeSet<String> tempNick = new TreeSet<String>(); //닉네임 저장해줄꺼임 (입장관리)
	ArrayList<String> clientNick = new ArrayList<String>();
	//Hashtable<Socket, Integer> clientResult = new Hashtable<Socket, Integer>(); //소켓, 빙고여부

	public void init(){ //당첨숫자 뽑아줄거임
//------------------------------------------------------------------------------ // UI 설정
		setTitle("NeWtRo BiNgO");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 600);
		setLocationRelativeTo(null);
//------------------------------------------------------------------------------ // 배경 레이아웃 설정
		cp = new JPanel();
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(cp);
		cp.setLayout(new BorderLayout());

		mainP = new JPanel();
		cp.add(mainP);
		mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS)); //박스레이아웃 위에서 아래로 레이아웃 배치

		server = new JLabel("{ ★ N E W - T R O  B I N G O ☆ S E R V E R ★ }");
		server.setAlignmentX(Component.CENTER_ALIGNMENT);
		server.setPreferredSize(new Dimension(100, 50));
		mainP.add(server);
		//server.setHorizontalTextPosition(SwingConstants.CENTER);
		server.setHorizontalAlignment(SwingConstants.CENTER); //라벨 가운데정렬
//------------------------------------------------------------------------------ // JTextArea 넣을 패널 설정
		txtP = new JPanel();
		mainP.add(txtP);
		txtP.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane(); //패널 안에 넣을 스크롤
		scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		txtP.add(scrollPane);
//------------------------------------------------------------------------------ // 서버의 오픈, 종료와 함께 클라이언트의 게임 상태를 받을 수 있는 JTextArea 설정
		txtArea = new JTextArea();
		txtArea.setLineWrap(false);
		txtArea.setEditable(false);
		scrollPane.setViewportView(txtArea);
//------------------------------------------------------------------------------ // 오픈, 종료 버튼 넣을 버튼 패널
		btnP = new JPanel();
		btnP.setPreferredSize(new Dimension(10, 50));
		btnP.setAutoscrolls(true);
		mainP.add(btnP);
		btnP.setLayout(new GridLayout(0,2));
//------------------------------------------------------------------------------ // 오픈 버튼
		btnStart = new JButton("서버 ON",new ImageIcon("temp_img/dangsu.jpg"));
		//btnStart.setHorizontalTextPosition(SwingConstants.LEFT);
		btnStart.setPreferredSize(new Dimension(110, 40));
		btnStart.setFocusPainted(false);
		btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnStart.setForeground(Color.BLACK);
		btnStart.setBackground(Color.LIGHT_GRAY);
		btnStart.setOpaque(false);
		btnStart.setBorder(null);

		btnP.add(btnStart); //버튼 패널에 오픈 버튼 넣기
		btnStart.addActionListener(this); // 오픈 버튼 액션리스너
//------------------------------------------------------------------------------ // 종료 버튼
		btnClose = new JButton(new ImageIcon("temp_img/dangsu.jpg"));
		//btnClose.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnClose.setFocusPainted(false);
		btnClose.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnClose.setForeground(Color.WHITE);
		btnClose.setBackground(Color.LIGHT_GRAY);
		btnClose.setOpaque(false);
		btnClose.setBorder(null);
		
		btnP.add(btnClose); //버튼 패널에 종료 버튼 넣기
		btnClose.addActionListener(this); // 종료 버튼 액션리스너
		btnClose.setEnabled(false);
	}
//-----------------------------------------------------------------------------// Start ActionPerformed
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnStart){ 
			Runnable r = this;
			Thread th = new Thread(r);
			th.start();	
				
//-----------------------------------------------------------------------------// Close ActionPerformed			
		}else if(e.getSource() == btnClose){
			int select = JOptionPane.showConfirmDialog(null, "정말 종료하시겠습니까?", "NewtroBing Server", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("temp_img/dangsu.jpg"));
			try{
				if(select == JOptionPane.YES_OPTION){
					ss.close();
					s.close();
					server.setText("Server Close!");
					txtArea.append(" [ ================= 뉴트로 빙고 서버 종료! ================= ]"+"\n");
					btnStart.setEnabled(true); //스타트 버튼 활성화
					btnClose.setEnabled(false); //종료 버튼 비활성화
				}
			}catch(IOException ie){}
		}
	}
	public void run() {
					try{
						ss = new ServerSocket(port);
						server.setText("Server Start!"); //서버에서보임
						txtArea.append(" [ ================= 뉴트로 빙고 서버 오픈! ================= ]"+"\n"); //서버에서보임
						btnStart.setEnabled(false); //스타트 버튼 비활성화
						btnClose.setEnabled(true); //종료 버튼 활성화
						while(true){
							s = ss.accept();						
							if((clientList.size() + 1) > 7 || gameStart == true){ 								
								s.close();
							}else{								
								userIP = s.getInetAddress().getHostAddress();
								clientList.put(s,userIP);
								new GameManager(s, this);
							}
						}
					}catch(IOException ie){}
				}			
//------------------------------------------------------------------------------ // 오픈, 종료 버튼 넣을 버튼 패널
	public static void main(String [] args){
		EventQueue.invokeLater(new Runnable(){// => https://m.blog.naver.com/PostView.nhn?blogId=shimchan2&logNo=70172294371&proxyReferer=https:%2F%2Fwww.google.com%2F
			public void run(){
				try{
					Server sserver = new Server();
					sserver.init();
					sserver.setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}