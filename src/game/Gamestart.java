package game;
import java.net.*;
import java.io.*;
import java.util.*;

class Gamestart  extends Thread {

	GameManager gm2;
	Random rd = new Random();
	String gamestatus = "true";

	Gamestart(GameManager gm2){
		this.gm2 = gm2;
	}
	
	public void run(){
		try{
			for(int i = 0; i<50; i++){
				if(gamestatus.equals("true")){
					int temp = rd.nextInt(gm2.dangsu_list.size());
					String temp_s = "%´çÃ·¼ö%" + gm2.dangsu_list.get(temp);
					gm2.dangsu_list.remove(temp);				
					gm2.sendMsg(temp_s);
					Thread.sleep(5000L);
				}
				else{
					System.exit(0);
				}
			}
		}
		catch(InterruptedException ie){}
	}
}