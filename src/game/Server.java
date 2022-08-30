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
	Hashtable<Socket, String> clientList = new Hashtable<Socket, String>(); //����, ip
	TreeSet<String> tempNick = new TreeSet<String>(); //�г��� �������ٲ��� (�������)
	ArrayList<String> clientNick = new ArrayList<String>();
	//Hashtable<Socket, Integer> clientResult = new Hashtable<Socket, Integer>(); //����, ������

	public void init(){ //��÷���� �̾��ٰ���
//------------------------------------------------------------------------------ // UI ����
		setTitle("NeWtRo BiNgO");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 600);
		setLocationRelativeTo(null);
//------------------------------------------------------------------------------ // ��� ���̾ƿ� ����
		cp = new JPanel();
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(cp);
		cp.setLayout(new BorderLayout());

		mainP = new JPanel();
		cp.add(mainP);
		mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS)); //�ڽ����̾ƿ� ������ �Ʒ��� ���̾ƿ� ��ġ

		server = new JLabel("{ �� N E W - T R O  B I N G O �� S E R V E R �� }");
		server.setAlignmentX(Component.CENTER_ALIGNMENT);
		server.setPreferredSize(new Dimension(100, 50));
		mainP.add(server);
		//server.setHorizontalTextPosition(SwingConstants.CENTER);
		server.setHorizontalAlignment(SwingConstants.CENTER); //�� �������
//------------------------------------------------------------------------------ // JTextArea ���� �г� ����
		txtP = new JPanel();
		mainP.add(txtP);
		txtP.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane(); //�г� �ȿ� ���� ��ũ��
		scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		txtP.add(scrollPane);
//------------------------------------------------------------------------------ // ������ ����, ����� �Բ� Ŭ���̾�Ʈ�� ���� ���¸� ���� �� �ִ� JTextArea ����
		txtArea = new JTextArea();
		txtArea.setLineWrap(false);
		txtArea.setEditable(false);
		scrollPane.setViewportView(txtArea);
//------------------------------------------------------------------------------ // ����, ���� ��ư ���� ��ư �г�
		btnP = new JPanel();
		btnP.setPreferredSize(new Dimension(10, 50));
		btnP.setAutoscrolls(true);
		mainP.add(btnP);
		btnP.setLayout(new GridLayout(0,2));
//------------------------------------------------------------------------------ // ���� ��ư
		btnStart = new JButton("���� ON",new ImageIcon("temp_img/dangsu.jpg"));
		//btnStart.setHorizontalTextPosition(SwingConstants.LEFT);
		btnStart.setPreferredSize(new Dimension(110, 40));
		btnStart.setFocusPainted(false);
		btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnStart.setForeground(Color.BLACK);
		btnStart.setBackground(Color.LIGHT_GRAY);
		btnStart.setOpaque(false);
		btnStart.setBorder(null);

		btnP.add(btnStart); //��ư �гο� ���� ��ư �ֱ�
		btnStart.addActionListener(this); // ���� ��ư �׼Ǹ�����
//------------------------------------------------------------------------------ // ���� ��ư
		btnClose = new JButton(new ImageIcon("temp_img/dangsu.jpg"));
		//btnClose.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnClose.setFocusPainted(false);
		btnClose.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnClose.setForeground(Color.WHITE);
		btnClose.setBackground(Color.LIGHT_GRAY);
		btnClose.setOpaque(false);
		btnClose.setBorder(null);
		
		btnP.add(btnClose); //��ư �гο� ���� ��ư �ֱ�
		btnClose.addActionListener(this); // ���� ��ư �׼Ǹ�����
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
			int select = JOptionPane.showConfirmDialog(null, "���� �����Ͻðڽ��ϱ�?", "NewtroBing Server", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("temp_img/dangsu.jpg"));
			try{
				if(select == JOptionPane.YES_OPTION){
					ss.close();
					s.close();
					server.setText("Server Close!");
					txtArea.append(" [ ================= ��Ʈ�� ���� ���� ����! ================= ]"+"\n");
					btnStart.setEnabled(true); //��ŸƮ ��ư Ȱ��ȭ
					btnClose.setEnabled(false); //���� ��ư ��Ȱ��ȭ
				}
			}catch(IOException ie){}
		}
	}
	public void run() {
					try{
						ss = new ServerSocket(port);
						server.setText("Server Start!"); //������������
						txtArea.append(" [ ================= ��Ʈ�� ���� ���� ����! ================= ]"+"\n"); //������������
						btnStart.setEnabled(false); //��ŸƮ ��ư ��Ȱ��ȭ
						btnClose.setEnabled(true); //���� ��ư Ȱ��ȭ
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
//------------------------------------------------------------------------------ // ����, ���� ��ư ���� ��ư �г�
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