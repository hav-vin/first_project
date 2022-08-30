package game;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Client implements Runnable 
{
	String host = "127.0.0.1"; //localhost
	int port = 3333;

	Socket s;
	OutputStream os;
	PrintWriter pw;
	BufferedReader brKey = new BufferedReader(new InputStreamReader(System.in));

	InputStream is; //�߰� 
	BufferedReader br; //�߰� 
	/////////////////////////////////////////////////////////
	Playpage pp;
	String nick;


	Client(String nick){
		
		//this.ip = ip;
		this.nick = nick;
		try{
            s = new Socket(host, port);
			pp = new Playpage(s,nick);
			new Thread(this).start(); //�߰�	
			os = s.getOutputStream();
			pw = new PrintWriter(os, true);
			pw.println(this.nick);
			System.out.println("ä�ý���");//�߰�

		}catch(UnknownHostException un){
			System.out.println("�ش� �ּ��� ������ ã�� ���� ����");
		}catch(IOException ie){ //���� �ȿ���
			System.out.println("�ش� �ּ��� ������ ������ �ʾҽ��ϴ�");
		}
	}

	public void run(){  //�߰�
		listen();
	}

	void listen(){ //Socket --> Moniter

		try{
			is = s.getInputStream();  //�߰�
			br = new BufferedReader(new InputStreamReader(is)); //�߰�
			while(true){
				String msg = br.readLine();
				if(msg.startsWith("%��÷��%")){ //Ȯ��
					int start = msg.lastIndexOf("%") + 1;
					int end = msg.length();
					String dang_su = msg.substring(start, end);						
					pp.dangsu_rd.setText(dang_su);
					pp.dangsu_rd.setFont(pp.f2);
					pp.efferSound(dang_su);
				}
				else if(msg.startsWith("%����%")){	
					int start = msg.lastIndexOf("%") + 3;
					int end = msg.length();
					String playername = msg.substring(start, end);	//�����ѻ�� �г���
					String numbertemp = msg.substring(start-2, start-1);
					int indexnumber = Integer.parseInt(numbertemp);
					if(indexnumber==0){
						pp.player1_nick.setText(playername);
						pp.player1_nick.setFont(pp.f3);
						pp.player1_nick.setHorizontalAlignment(0);
						pp.player1_pic.setIcon(new ImageIcon("temp_img/����2.png"));
					}
					else if(indexnumber==1){
						pp.player2_nick.setText(playername);
						pp.player2_nick.setFont(pp.f3);
						pp.player2_nick.setHorizontalAlignment(0);
						pp.player2_pic.setIcon(new ImageIcon("temp_img/����.png"));

					}
					else if(indexnumber==2){
						pp.player3_nick.setText(playername);
						pp.player3_nick.setFont(pp.f3);
						pp.player3_nick.setHorizontalAlignment(0);
						pp.player3_pic.setIcon(new ImageIcon("temp_img/avatar1.png"));

					}
					else if(indexnumber==3){
						pp.player4_nick.setText(playername);
						pp.player4_nick.setFont(pp.f3);
						pp.player4_nick.setHorizontalAlignment(0);
						pp.player4_pic.setIcon(new ImageIcon("temp_img/��.png"));

					}
					else if(indexnumber==4){
						pp.player5_nick.setText(playername);
						pp.player5_nick.setFont(pp.f3);
						pp.player5_nick.setHorizontalAlignment(0);
						pp.player5_pic.setIcon(new ImageIcon("temp_img/����2.png"));

					}
					else if(indexnumber==5){
						pp.player6_nick.setText(playername);
						pp.player6_nick.setFont(pp.f3);
						pp.player6_nick.setHorizontalAlignment(0);
						pp.player6_pic.setIcon(new ImageIcon("temp_img/avatar2.png"));

					}
				}
				else if(msg.startsWith("%��������%")){
					pp.txtArea.append("������ ����˴ϴ�"+"\n");
					for(int i = 5; i>0; i--){
						try{
							Thread.sleep(1000L);
							pp.txtArea.append(" [ �� "+ i +"  ] " + "\n");
							if(i==1){
								System.exit(0);
							}
						}
						catch(InterruptedException ie){}
					}					
				}
				
				else{					
					if(msg.indexOf("��") == 3){
						pp.txtArea.append(msg+"\n");
					}
					else if(msg.indexOf("��") == 1){
						pp.txtArea.append(msg+"\n"); //�ٸ� �ΰ��� �޼���
					}
				}
			}
		}catch(IOException ie){
			System.out.println("������ ���� ����" + ie);
			System.exit(0);
		}finally{
		 try{
				closeAll();
			}catch(IOException ie){}
		}
	}

	private void closeAll() throws IOException {
		if(pw != null) pw.close();
		if(os != null) os.close();
		if(br != null) br.close();
	    if(is != null) is.close();
		if(s != null) s.close();
	}

}
