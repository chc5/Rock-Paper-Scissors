package rps;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class RPS{

	private Scanner scanner = new Scanner(System.in);
	private String ip = "localhost";
	private int port = 12;
	public boolean accepted = false;
	public boolean restarting = true;
	
	public Game game;
	private final byte ROCK = 0;
	private final byte PAPER = 1;
	private final byte SCISSOR = 2;
	public final byte NOTHING = Byte.MIN_VALUE;
	
	private Server server = null;
	private Client client = null;
	
	public static DataOutputStream output;
	public static DataInputStream input;
	
	private GUI gui = null;
	
	RPS(){
		//inputtingIPPort();
		decideIfServer();
		game = new Game();
	}
	
//	public void inputtingIPPort(){
//		System.out.println("Please input the IP: ");
//		ip = scanner.nextLine();
//		System.out.println("Please input the port: ");
//		port = scanner.nextInt();
//		while (port < 1 || port > 65535) {
//			System.out.println("The port you entered was invalid, please input another port: ");
//			port = scanner.nextInt();
//		}
//	}
	
	private void decideIfServer(){
		client = new Client();
		accepted = client.connectToServer(ip,port);
		if(!accepted){
			client = null;
			server = new Server(ip,port);
			accepted = server.waitingForCilent();
		}
	}
	
	private byte interpretString(String string){
		if(string.equals("p")){
			return PAPER;
		}
		if(string.equals("r")){
			return ROCK;
		}
		if(string.equals("s")){
			return SCISSOR;
		}
		throw new RuntimeException("Unable to interpret string");
	}
	public String interpretByte(byte bit){
		switch(bit){
			case PAPER: return "PAPER";
			case SCISSOR: return "SCISSOR";
			case ROCK: return "ROCK";
			default: throw new RuntimeException("Unable to interpret byte");
		}
	}
	public void playYourHand(){
		System.out.println(game.getYourHand());
		System.out.println("You have selected: "+interpretByte(game.getYourHand()));
		try{
			output.writeByte(game.getYourHand());
			output.flush();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		System.out.println("Successfully sent the data.");
		
	}
	public void waitForTheirHand(){
		System.out.println("Waiting for your opponent to select a hand...");
		try {
			game.setTheirHand(input.readByte());
			System.out.println("They have selected: "+interpretByte(game.getTheirHand()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Unable to read byte from input.");
			e.printStackTrace();
		}
		System.out.println("You "+game.getResult());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//RPS rps = new RPS();
		GUI gui = new GUI();
	}
	public void restart(){
		System.out.println("Your answer: "+ restarting);
		try {
			output.writeBoolean(restarting);
			System.out.println("Waiting for your opponent to respond...");
			if(input.readBoolean())
				restarting = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Unable to write restarting to boolean");
			e.printStackTrace();
			restarting = false;
		}
	}
	public void run() {
		// TODO Auto-generated method stub
		while(restarting){
			if(!accepted) accepted = server.waitingForCilent();
			System.out.println("We are: "+restarting);
		}
	}
	
}