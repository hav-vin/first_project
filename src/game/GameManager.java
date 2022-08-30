package game;
import java.net.*;
import java.io.*;
import java.util.*;

class GameManager extends Thread{ //하나의 클라이언트랑 연결될 서버클래스 (클라이언트가 생길때마다 걔한테 실행되어줌)

	Socket soc; //소켓 써야됨(연결통로)
	String userName; //입력한 닉네임 (초기화값: 닉네임 미등록자들)
	
	InputStream is; //이름, 메세지 읽어들일꺼임
	BufferedReader br; //이름, 메세지 읽을꺼임
	OutputStream os; //메세지 뿌릴꺼임
	PrintWriter pw; //메세지 뿌릴꺼임

	Server srv; //Server클래스꺼 사용해야함
	Gamestart gamestart = new Gamestart(this); //Gamestart 꺼

	///////////////////////////////////////////////////
	ArrayList<Integer> dangsu_list = new ArrayList<Integer>();

	int count = 0; //레디한 사람 수
	
	///////////////////////////////////////////////////

	GameManager(Socket soc, Server srv){
		
		this.soc = soc; //Server에서 생긴 soc 고대로 사용
		this.srv = srv;
		
		try{ //사용할 스트림들 켜줌
			is = soc.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
		}
		catch(IOException ie){}

		for (int i=0; i<50; i++){
			dangsu_list.add(i+1);
		}
		start();
	}

	public void run(){ //입장메세지, 클라이언트가 입력한 메세지 생길때마다 읽기

		String msg = ""; //메세지 옮겨 받을 변수
		String nick = "";
		try{
			nick = br.readLine();
			if(nick.equals("")){
				userName =  "User(" + srv.userIP + ")";
			}
			else{
				userName = nick;
			}
			srv.txtArea.append(" [ ▶ 클라이언트("+userName+") 입장! ] " + "\n"); //
			srv.txtArea.append(" [ ===================== " + srv.clientList.size() + "명 접속중!! ===================== ]"+"\n");			
			sendMsg(" [ ▶ "+userName+"입장!	   ＊접속자 " + srv.clientList.size() + "명 ] "); //뿌리기

				srv.tempNick.add(userName);
				Iterator<String> iter = srv.tempNick.iterator();
				while(iter.hasNext()){
					String temp = iter.next();
					if(temp.equals(userName)){
						sendMsg("%참여%"+ srv.index + srv.index + temp);
						srv.index=0;
					}
					else{
						sendMsg("%참여%"+ srv.index + srv.index + temp);
						srv.index++;
					}
				}
			
			Enumeration<Socket> e = srv.clientList.keys();
				while(e.hasMoreElements()){
					Socket key = e.nextElement();
					String val = srv.clientList.get(key);
					System.out.println(key + "val: " + val);
				}

			while(true){
				msg = br.readLine(); //메세지 취급
				
				if(msg.length() != 0 && msg.equals("%승리%")){					
					gamestart.gamestatus = "false";
					sendMsg(" [ ▶ "+userName+"승리! ] "); //뿌리기
					sendMsg("%게임종료%");

				}
				if(msg.length() != 0 && msg.equals("#준비완료")){
					srv.count++;
				}
				else if(msg.length() != 0 ){
					srv.txtArea.append(" ▷ "+userName+" >> " + msg + "\n"); //서버 UI에 출력
					sendMsg(" ▷ "+userName+" >> " + msg); //뿌리기
				}
				if(srv.count>=2 && srv.count == srv.clientList.size()){
					sendMsg(" [ ▶ 5초 뒤 게임을 시작합니다  ] " + "\n");	
					for(int i = 5; i>0; i--){
						try{
							Thread.sleep(1000L);
							sendMsg(" [ ▶ "+ i +"  ] " + "\n");
							if(i==1){
								Thread.sleep(1000L);
								sendMsg(" [ ▶ 게임 시작  ] " + "\n");
							}
						}
						catch(InterruptedException ie){}
					}
					
					gamestart.start();
					srv.count=6;
				}

			}
		}
		catch(IOException ie){
			srv.clientList.remove(soc);
			srv.txtArea.append(" [ ▶ 클라이언트("+userName+") 퇴장!	    ＊접속자 : " + srv.clientList.size() + "명 ] " + "\n"); //출력
			sendMsg(" [ ▶ "+userName+" 퇴장!	    ＊접속자 " + srv.clientList.size() + "명 ] "); //뿌리기
		}
		finally{
			try{
				closeAll();
			}
			catch(IOException ie){}
		}
	}

	public void sendMsg(String msg){ //메세지 받은거 가지고 들어와서 다른 클라이언트에 메세지 뿌릴꺼임

		Enumeration<Socket> e = srv.clientList.keys(); //s_list의 key값들 검색할꺼
		while(e.hasMoreElements()){
			Socket key = e.nextElement(); //검색해서 key에 넣어주자
					try{
						 //true: autoflush()
						os = key.getOutputStream();
						pw = new PrintWriter(os, true);
						pw.println(msg); //메세지 뿌리기
					}
					catch(IOException ie){
						System.out.println("sendMsg의 Exception");
						return; //다른사람이 ctrl+c 누르면 발생되고 클라이언트들 같이 퇴장됨
					}
			}
		}
	
	/*public void randomSu(){
		1. 랜덤수 뽑음
		2. 뽑힌거 따로 배열에 저장 (array1) -> 중복 제거위해
		3. 랜덤수 뽑음
		4. array1 이랑 값 비교해서 일치하지 않으면 뿌려줌
		5. 뿌려주고 따로 배열에 저장 (array1)
		6. 게임 끝날때까지 3->4->5 반복
	}
	*/
	
	private void closeAll() throws IOException {
		if(pw != null) pw.close();
		if(os != null) os.close();
		if(br != null) br.close();
	    if(is != null) is.close();
		if(soc != null) soc.close();
	}

}
