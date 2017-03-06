package rps;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RockPaperScissors implements Runnable{

	Scanner scanner = new Scanner(System.in);

	private String ip;
	private int port;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private boolean accepted = false;
	private Thread thread;

	private ServerSocket serversocket;

	private BufferedImage paper;
	private BufferedImage rock;
	private BufferedImage scissors;
	
	private byte hand = 0;
	private byte enemyhand = 0;

	private boolean win = false;
	private boolean enemywin = false;
	private boolean tie = false;

	private boolean gameover = false;
	
	private Painter painter;
	private JFrame frame;
	private int WIDTH = 1050;
	private int HEIGHT = 600;

	private int MARGAIN = 0;
	private int counter = 0;
	
	private int testcount = 0;
	public static void main(String[] args) {
		RockPaperScissors game = new RockPaperScissors();
	}
	public RockPaperScissors(){
		//System.out.println("Testing");
		inputtingIPPort();
		loadImages();
		initializingPainter();
		if(!connectToServer())
			makingTheServer();
		initializingFrame();
		initializingThread();
	}
	public void inputtingIPPort(){
		System.out.println("Please input the IP: ");
		ip = scanner.nextLine();
		System.out.println("Please input the port: ");
		port = scanner.nextInt();
		while (port < 1 || port > 65535) {
			System.out.println("The port you entered was invalid, please input another port: ");
			port = scanner.nextInt();
		}
	}
	public void loadImages(){
		try {
			paper = ImageIO.read(getClass().getResourceAsStream("/paper.png"));
			rock = ImageIO.read(getClass().getResourceAsStream("/rock.png"));
			scissors = ImageIO.read(getClass().getResourceAsStream("/scissors.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initializingPainter(){
		painter = new Painter();
		painter.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	private boolean connectToServer(){
		try{
			socket = new Socket(ip,port);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			accepted = true;
			System.out.println("The client has connected to the server.");
		} catch(IOException e){
			System.out.println("The client has not connected to the server.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private void makingTheServer(){
		try{
			serversocket = new ServerSocket(port,10,InetAddress.getByName(ip));
			System.out.println("This thread has made the server.");
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	private void waitingForCilent(){
		try{
			socket = serversocket.accept();
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			accepted = true;
			System.out.println("Server has accepted the cilent's request to join. Proceeding to play the game.");
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	public void initializingFrame(){
		frame = new JFrame();
		frame.setTitle("Tic-Tac-Toe");
		frame.setContentPane(painter);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	public void initializingThread(){
		thread = new Thread(this, "TicTacToe");
		thread.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			painter.repaint();
			if(!accepted)
				waitingForCilent();
			play();
		}
	}

	private void play(){
		if(!gameover){
			if(enemyhand == 0){
				try{
					enemyhand = dis.readByte();
					System.out.println("DATA WAS RECIEVED");
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
			if(hand != 0 && enemyhand != 0)
				checkForWin();
		}
	}
	private void checkForWin(){
		float calculation = ((float)hand/enemyhand)%3f;
		if(hand-enemyhand==-2)
			win = true;
		else{
			if(calculation>1)
					win = true;
			if(calculation==1)
					tie = true;
			if(calculation<1)
				enemywin = true;
			gameover = true;
		}
	}
	private void reset(){
		enemyhand = 0;
		hand = 0;
		tie = false;
		win = false;
		enemywin = false;
		gameover = false;
	}
	
	private class Painter extends JPanel implements MouseListener{
		
		public Painter(){
			setFocusable(true);
			requestFocus();
			setBackground(Color.WHITE);
			addMouseListener(this);
		}
		
		@Override 
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			drawPanel(g);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if(accepted && hand == 0 && e.getY()>100 && e.getY()<450){
				
				hand = (byte)((e.getX()/350)+1);
				//System.out.println(hand);
				repaint();
				Toolkit.getDefaultToolkit().sync();
				try{
					dos.writeByte(hand);
					dos.flush();
				}
				catch(IOException ioe){
					ioe.printStackTrace();
				}
				System.out.println("Data was sent.");
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}
	private void drawPanel(Graphics g){
		g.setFont(new Font("Verdana",Font.BOLD,20));
		if(hand  == 0){
			g.drawString("Pick your hand.", 50, 50);
			g.drawImage(rock,MARGAIN,100,null);
			g.drawImage(paper, MARGAIN*2+rock.getWidth(), 100, null);
			g.drawImage(scissors,MARGAIN*3+rock.getWidth()+paper.getWidth(),100,null);
			for(int i = 1;i<=3;i++)
			g.drawRect(MARGAIN*i+350*(i-1), 100, 350, 350);
		}
		else{
			String s = "Waiitng for Opponent to Pick a Hand";
			if(enemyhand == 0){
				//Graphics2D g2 = (Graphics2D) g;
				for(int j = -1; j<counter;j++)
				s+=".";
				g.drawString(s, 100, 100);
				counter = (counter+1)%3;
			}
			else{
				g.drawString("Winner has been found.",100,100);
				if(win)
				g.drawString("You win", 200, 100);
				if(enemywin)
					g.drawString("Enemy win", 200, 100);
				if(tie)
					g.drawString("It is a tie", 200, 100);
			}
		}
	}
	
}
