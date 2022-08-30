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

	InputStream is; //추가 
	BufferedReader br; //추가 
	/////////////////////////////////////////////////////////
	Playpage pp;
	String nick;


	Client(String nick){
		
		//this.ip = ip;
		this.nick = nick;
		try{
            s = new Socket(host, port);
			pp = new Playpage(s,nick);
			new Thread(this).start(); //추가	
			os = s.getOutputStream();
			pw = new PrintWriter(os, true);
			pw.println(this.nick);
			System.out.println("채팅시작");//추가

		}catch(UnknownHostException un){
			System.out.println("해당 주소의 서버를 찾을 수가 없음");
		}catch(IOException ie){ //서버 안열음
			System.out.println("해당 주소의 서버가 열리지 않았습니다");
		}
	}

	public void run(){  //추가
		listen();
	}

	void listen(){ //Socket --> Moniter

		try{
			is = s.getInputStream();  //추가
			br = new BufferedReader(new InputStreamReader(is)); //추가
			while(true){
				String msg = br.readLine();
				if(msg.startsWith("%당첨수%")){ //확률
					int start = msg.lastIndexOf("%") + 1;
					int end = msg.length();
					String dang_su = msg.substring(start, end);						
					pp.dangsu_rd.setText(dang_su);
					pp.dangsu_rd.setFont(pp.f2);
					pp.efferSound(dang_su);
				}
				else if(msg.startsWith("%참여%")){	
					int start = msg.lastIndexOf("%") + 3;
					int end = msg.length();
					String playername = msg.substring(start, end);	//입장한사람 닉네임
					String numbertemp = msg.substring(start-2, start-1);
					int indexnumber = Integer.parseInt(numbertemp);
					if(indexnumber==0){
						pp.player1_nick.setText(playername);
						pp.player1_nick.setFont(pp.f3);
						pp.player1_nick.setHorizontalAlignment(0);
						pp.player1_pic.setIcon(new ImageIcon("temp_img/혜빈2.png"));
					}
					else if(indexnumber==1){
						pp.player2_nick.setText(playername);
						pp.player2_nick.setFont(pp.f3);
						pp.player2_nick.setHorizontalAlignment(0);
						pp.player2_pic.setIcon(new ImageIcon("temp_img/수빈.png"));

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
						pp.player4_pic.setIcon(new ImageIcon("temp_img/희영.png"));

					}
					else if(indexnumber==4){
						pp.player5_nick.setText(playername);
						pp.player5_nick.setFont(pp.f3);
						pp.player5_nick.setHorizontalAlignment(0);
						pp.player5_pic.setIcon(new ImageIcon("temp_img/혜원2.png"));

					}
					else if(indexnumber==5){
						pp.player6_nick.setText(playername);
						pp.player6_nick.setFont(pp.f3);
						pp.player6_nick.setHorizontalAlignment(0);
						pp.player6_pic.setIcon(new ImageIcon("temp_img/avatar2.png"));

					}
				}
				else if(msg.startsWith("%게임종료%")){
					pp.txtArea.append("게임이 종료됩니다"+"\n");
					for(int i = 5; i>0; i--){
						try{
							Thread.sleep(1000L);
							pp.txtArea.append(" [ ▶ "+ i +"  ] " + "\n");
							if(i==1){
								System.exit(0);
							}
						}
						catch(InterruptedException ie){}
					}					
				}
				
				else{					
					if(msg.indexOf("▶") == 3){
						pp.txtArea.append(msg+"\n");
					}
					else if(msg.indexOf("▷") == 1){
						pp.txtArea.append(msg+"\n"); //다른 인간들 메세지
					}
				}
			}
		}catch(IOException ie){
			System.out.println("서버와 연결 끊김" + ie);
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
