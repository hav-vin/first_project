package game;
import java.net.*;
import java.io.*;
import java.util.*;

class GameManager extends Thread{ //�ϳ��� Ŭ���̾�Ʈ�� ����� ����Ŭ���� (Ŭ���̾�Ʈ�� ���涧���� ������ ����Ǿ���)

	Socket soc; //���� ��ߵ�(�������)
	String userName; //�Է��� �г��� (�ʱ�ȭ��: �г��� �̵���ڵ�)
	
	InputStream is; //�̸�, �޼��� �о���ϲ���
	BufferedReader br; //�̸�, �޼��� ��������
	OutputStream os; //�޼��� �Ѹ�����
	PrintWriter pw; //�޼��� �Ѹ�����

	Server srv; //ServerŬ������ ����ؾ���
	Gamestart gamestart = new Gamestart(this); //Gamestart ��

	///////////////////////////////////////////////////
	ArrayList<Integer> dangsu_list = new ArrayList<Integer>();

	int count = 0; //������ ��� ��
	
	///////////////////////////////////////////////////

	GameManager(Socket soc, Server srv){
		
		this.soc = soc; //Server���� ���� soc ���� ���
		this.srv = srv;
		
		try{ //����� ��Ʈ���� ����
			is = soc.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
		}
		catch(IOException ie){}

		for (int i=0; i<50; i++){
			dangsu_list.add(i+1);
		}
		start();
	}

	public void run(){ //����޼���, Ŭ���̾�Ʈ�� �Է��� �޼��� ���涧���� �б�

		String msg = ""; //�޼��� �Ű� ���� ����
		String nick = "";
		try{
			nick = br.readLine();
			if(nick.equals("")){
				userName =  "User(" + srv.userIP + ")";
			}
			else{
				userName = nick;
			}
			srv.txtArea.append(" [ �� Ŭ���̾�Ʈ("+userName+") ����! ] " + "\n"); //
			srv.txtArea.append(" [ ===================== " + srv.clientList.size() + "�� ������!! ===================== ]"+"\n");			
			sendMsg(" [ �� "+userName+"����!	   �������� " + srv.clientList.size() + "�� ] "); //�Ѹ���

				srv.tempNick.add(userName);
				Iterator<String> iter = srv.tempNick.iterator();
				while(iter.hasNext()){
					String temp = iter.next();
					if(temp.equals(userName)){
						sendMsg("%����%"+ srv.index + srv.index + temp);
						srv.index=0;
					}
					else{
						sendMsg("%����%"+ srv.index + srv.index + temp);
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
				msg = br.readLine(); //�޼��� ���
				
				if(msg.length() != 0 && msg.equals("%�¸�%")){					
					gamestart.gamestatus = "false";
					sendMsg(" [ �� "+userName+"�¸�! ] "); //�Ѹ���
					sendMsg("%��������%");

				}
				if(msg.length() != 0 && msg.equals("#�غ�Ϸ�")){
					srv.count++;
				}
				else if(msg.length() != 0 ){
					srv.txtArea.append(" �� "+userName+" >> " + msg + "\n"); //���� UI�� ���
					sendMsg(" �� "+userName+" >> " + msg); //�Ѹ���
				}
				if(srv.count>=2 && srv.count == srv.clientList.size()){
					sendMsg(" [ �� 5�� �� ������ �����մϴ�  ] " + "\n");	
					for(int i = 5; i>0; i--){
						try{
							Thread.sleep(1000L);
							sendMsg(" [ �� "+ i +"  ] " + "\n");
							if(i==1){
								Thread.sleep(1000L);
								sendMsg(" [ �� ���� ����  ] " + "\n");
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
			srv.txtArea.append(" [ �� Ŭ���̾�Ʈ("+userName+") ����!	    �������� : " + srv.clientList.size() + "�� ] " + "\n"); //���
			sendMsg(" [ �� "+userName+" ����!	    �������� " + srv.clientList.size() + "�� ] "); //�Ѹ���
		}
		finally{
			try{
				closeAll();
			}
			catch(IOException ie){}
		}
	}

	public void sendMsg(String msg){ //�޼��� ������ ������ ���ͼ� �ٸ� Ŭ���̾�Ʈ�� �޼��� �Ѹ�����

		Enumeration<Socket> e = srv.clientList.keys(); //s_list�� key���� �˻��Ҳ�
		while(e.hasMoreElements()){
			Socket key = e.nextElement(); //�˻��ؼ� key�� �־�����
					try{
						 //true: autoflush()
						os = key.getOutputStream();
						pw = new PrintWriter(os, true);
						pw.println(msg); //�޼��� �Ѹ���
					}
					catch(IOException ie){
						System.out.println("sendMsg�� Exception");
						return; //�ٸ������ ctrl+c ������ �߻��ǰ� Ŭ���̾�Ʈ�� ���� �����
					}
			}
		}
	
	/*public void randomSu(){
		1. ������ ����
		2. ������ ���� �迭�� ���� (array1) -> �ߺ� ��������
		3. ������ ����
		4. array1 �̶� �� ���ؼ� ��ġ���� ������ �ѷ���
		5. �ѷ��ְ� ���� �迭�� ���� (array1)
		6. ���� ���������� 3->4->5 �ݺ�
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
