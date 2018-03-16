package rps;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;


public class Server {
	private ServerSocket serverSocket;
	private Socket socket;
	
	//private GUI gui;
	final String name = "SERVER";
	Server(final String ip, final int port){
		try{
			serverSocket = new ServerSocket(port,10,InetAddress.getByName(ip));
			System.out.println("This thread has made the server.");
		} catch(IOException e){
			System.err.println("Failed to make the server.");
			e.printStackTrace();
		}
	}
	public boolean waitingForCilent(){
		try{
			socket = serverSocket.accept();
			RPS.output = new DataOutputStream(socket.getOutputStream());
			RPS.input = new DataInputStream(socket.getInputStream());
			System.out.println("Server has accepted the cilent's request to join. Proceeding to play the game.");
		} catch(IOException e){
			System.err.println("Server has failed to accept cilent's request or have not seen the request.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
