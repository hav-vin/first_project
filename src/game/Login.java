package game;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*; 

class Login extends JFrame implements ActionListener, Runnable{

	JButton start;
	JPanel cp, pOut;
	JLabel pBackground, dangsu, binggo_pan;
	JLabel labelId, labelIp;
	JTextField jtfId, jtfIp;

	String ip, nick;
	Client client;
	/////////////////////////////////////////////////////

	public void init(){

//------------------------------------------------------------------------------ //가장 맨아래 깔린 패널 설정
		Play("sounds/bgm2.wav");
		cp = new JPanel();
		cp.setBorder(null);
		setContentPane(cp);
		cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS)); //왼쪽->오른쪽 순서대로 배치
		cp.setOpaque(true); //투명하게
//------------------------------------------------------------------------------ //큰패널들 깔아둘 패널 설정
		pOut = new JPanel();			
		pOut.setOpaque(true);		
		cp.add(pOut);
		pOut.setLayout(null);
//------------------------------------------------------------------------------ //배경화면 설정
		pBackground = new JLabel(new ImageIcon("temp_img/loginbackground.png"));
		pBackground.setBounds(0, 0, 320, 440); //(0.0)에 950*850 사이즈로
		pBackground.setOpaque(true);
		pOut.add(pBackground);
		//pOut.setLayout(null);
//------------------------------------------------------------------------------ //큰 패널들 배치
		start = new JButton(new ImageIcon("temp_img/버튼.png"));	 //시작버튼	
		start.setOpaque(false);		
		start.setBorderPainted(false);
		start.setContentAreaFilled(false);
		start.setBounds(255, 298, 50, 80); //(x.y)에 x2*y2 사이즈로
		start.addActionListener(this);
		pBackground.add(start);
		
//----------------------------------------------------------------------------- //id창
      jtfId = new JTextField();
	  jtfId.setBounds(140, 348, 115, 23);
	  jtfId.setOpaque(false);
	  jtfId.setBorder(BorderFactory.createEmptyBorder());
      jtfId.setText("");     
      pBackground.add(jtfId);
      jtfId.setColumns(10); //글자수 최대 입력: 10글자
//----------------------------------------------------------------------------- //ip창
      jtfIp = new JTextField();
	  jtfIp.setBounds(140, 301, 115, 23);
	  jtfIp.setOpaque(false);
	  jtfIp.setBorder(BorderFactory.createEmptyBorder());      
	  jtfIp.setText("127.0.0.1");
      pBackground.add(jtfIp);
      jtfIp.setColumns(10); //글자수 최대 입력: 10글자
	  
	  setUI();
	}


	public void setUI(){
		setTitle("NeWtRo BiNgO");
		setSize(326, 469);
		setVisible(true);
		setLocation(200,100);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

//-----------------------------------------------------------------------------// ActionPerformed
	@Override
	public void actionPerformed(ActionEvent e){
		ImageIcon fail = new ImageIcon("로그인 실수 이미지파일");
        ImageIcon success = new ImageIcon("로그인 성공 이미지파일");
		  Object obj = e.getSource(); //포커스를 아이디에 맞춤
		  jtfId.requestFocus();
		  if(obj == start){
			 Play("sounds/loginn.wav");
			 if (jtfId.getText().equals("") || jtfIp.getText().equals("")){         
				JOptionPane.showMessageDialog(null, "ID 혹은 IP를 제대로 입력해 주세요", "ID_ERROR", JOptionPane.QUESTION_MESSAGE, fail);
			 }else if (jtfId.getText().trim().length()>6){
				JOptionPane.showMessageDialog(null, "ID는 최대 10글자까지 입력해 주세요!", "IP_ERROR", JOptionPane.QUESTION_MESSAGE, fail);
				jtfId.setText("");
				jtfId.requestFocus(); //다시 포커스를 아이디에 맞춤
			 }else{
				this.nick = jtfId.getText().trim();
				String temp = jtfIp.getText();
				if(temp.matches("(^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$)")){ //IPv4의 절댓값
				   ip = temp;
				   //playSound("시작bgm.wav"); 
				   
				   JOptionPane.showMessageDialog(null, " 로그인 성공! ", "NeWtRo BiNgO LOGIN", JOptionPane.INFORMATION_MESSAGE, success);
				   start.setEnabled(false);
				   jtfId.setEnabled(false);
				   jtfIp.setEnabled(false);				   
				   setVisible(false);    
				  // Playpage pp = new Playpage(ip, nick);  //Playpage에 ip랑 nick넘겨줌., 
					 Runnable r = this;
					Thread th = new Thread(r);
					th.start();	
				 
				   
				}else{
				   JOptionPane.showMessageDialog(null, "IP 주소를 정확하게 입력하세요! ", "ERROR!", JOptionPane.WARNING_MESSAGE, fail);
				}
			 }
		  }
	}

	public void Play(String fileName){
	try{
		AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
		Clip clip = AudioSystem.getClip();
		clip.stop();
		clip.open(ais);
		clip.start();
	}
	catch (Exception ex){}
	}

	public void run(){
		client = new Client(this.nick);
	}

   public static void main(String[] args){
      Login lg = new Login();
	  //playSound("로그인5.wav");
      lg.init();      
   }
}
