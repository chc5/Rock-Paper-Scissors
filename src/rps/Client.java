package rps;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.IOException;

public class Client {
	Socket socket;
	final String name = "CLIENT";
	//private GUI gui;
	public Client(){
		
	};
	public void play(){
		
	}
	public boolean connectToServer(final String ip, final int port){
		try{
			socket = new Socket(ip,port);
			RPS.output = new DataOutputStream(socket.getOutputStream());
			RPS.input = new DataInputStream(socket.getInputStream());
			System.out.println("The client has connected to the server.");
		} catch(IOException e){
			System.out.println("The client has not connected to the server.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
