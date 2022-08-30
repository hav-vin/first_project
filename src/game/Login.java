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

//------------------------------------------------------------------------------ //���� �ǾƷ� �� �г� ����
		Play("sounds/bgm2.wav");
		cp = new JPanel();
		cp.setBorder(null);
		setContentPane(cp);
		cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS)); //����->������ ������� ��ġ
		cp.setOpaque(true); //�����ϰ�
//------------------------------------------------------------------------------ //ū�гε� ��Ƶ� �г� ����
		pOut = new JPanel();			
		pOut.setOpaque(true);		
		cp.add(pOut);
		pOut.setLayout(null);
//------------------------------------------------------------------------------ //���ȭ�� ����
		pBackground = new JLabel(new ImageIcon("temp_img/loginbackground.png"));
		pBackground.setBounds(0, 0, 320, 440); //(0.0)�� 950*850 �������
		pBackground.setOpaque(true);
		pOut.add(pBackground);
		//pOut.setLayout(null);
//------------------------------------------------------------------------------ //ū �гε� ��ġ
		start = new JButton(new ImageIcon("temp_img/��ư.png"));	 //���۹�ư	
		start.setOpaque(false);		
		start.setBorderPainted(false);
		start.setContentAreaFilled(false);
		start.setBounds(255, 298, 50, 80); //(x.y)�� x2*y2 �������
		start.addActionListener(this);
		pBackground.add(start);
		
//----------------------------------------------------------------------------- //idâ
      jtfId = new JTextField();
	  jtfId.setBounds(140, 348, 115, 23);
	  jtfId.setOpaque(false);
	  jtfId.setBorder(BorderFactory.createEmptyBorder());
      jtfId.setText("");     
      pBackground.add(jtfId);
      jtfId.setColumns(10); //���ڼ� �ִ� �Է�: 10����
//----------------------------------------------------------------------------- //ipâ
      jtfIp = new JTextField();
	  jtfIp.setBounds(140, 301, 115, 23);
	  jtfIp.setOpaque(false);
	  jtfIp.setBorder(BorderFactory.createEmptyBorder());      
	  jtfIp.setText("127.0.0.1");
      pBackground.add(jtfIp);
      jtfIp.setColumns(10); //���ڼ� �ִ� �Է�: 10����
	  
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
		ImageIcon fail = new ImageIcon("�α��� �Ǽ� �̹�������");
        ImageIcon success = new ImageIcon("�α��� ���� �̹�������");
		  Object obj = e.getSource(); //��Ŀ���� ���̵� ����
		  jtfId.requestFocus();
		  if(obj == start){
			 Play("sounds/loginn.wav");
			 if (jtfId.getText().equals("") || jtfIp.getText().equals("")){         
				JOptionPane.showMessageDialog(null, "ID Ȥ�� IP�� ����� �Է��� �ּ���", "ID_ERROR", JOptionPane.QUESTION_MESSAGE, fail);
			 }else if (jtfId.getText().trim().length()>6){
				JOptionPane.showMessageDialog(null, "ID�� �ִ� 10���ڱ��� �Է��� �ּ���!", "IP_ERROR", JOptionPane.QUESTION_MESSAGE, fail);
				jtfId.setText("");
				jtfId.requestFocus(); //�ٽ� ��Ŀ���� ���̵� ����
			 }else{
				this.nick = jtfId.getText().trim();
				String temp = jtfIp.getText();
				if(temp.matches("(^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$)")){ //IPv4�� ����
				   ip = temp;
				   //playSound("����bgm.wav"); 
				   
				   JOptionPane.showMessageDialog(null, " �α��� ����! ", "NeWtRo BiNgO LOGIN", JOptionPane.INFORMATION_MESSAGE, success);
				   start.setEnabled(false);
				   jtfId.setEnabled(false);
				   jtfIp.setEnabled(false);				   
				   setVisible(false);    
				  // Playpage pp = new Playpage(ip, nick);  //Playpage�� ip�� nick�Ѱ���., 
					 Runnable r = this;
					Thread th = new Thread(r);
					th.start();	
				 
				   
				}else{
				   JOptionPane.showMessageDialog(null, "IP �ּҸ� ��Ȯ�ϰ� �Է��ϼ���! ", "ERROR!", JOptionPane.WARNING_MESSAGE, fail);
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
	  //playSound("�α���5.wav");
      lg.init();      
   }
}
