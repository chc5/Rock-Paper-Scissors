package rps;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Painter;

public class GUI implements Runnable{
	private BufferedImage paper;
	private BufferedImage rock;
	private BufferedImage scissors;

	private Thread thread;

	private Painter painter;
	private JFrame frame;
	private int WIDTH = 1050;
	private int HEIGHT = 600;

	private int MARGAIN = 0;

	private RPS rps;

	private boolean inRestartMode = false;
	private boolean waitingForOpponent = false;

	public GUI(){
		rps = new RPS();
		loadImages();
		initializingPainter();
		initializingFrame();
		initializingThread();
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
			if(rps.restarting){
				g.setFont(new Font("Verdana",Font.BOLD,20));
				if(!inRestartMode && !waitingForOpponent)
					drawHandScreen(g);
				if(inRestartMode && !waitingForOpponent)
					drawRestartScreen(g);
				if(waitingForOpponent)
					drawWaitingScreen(g);
			}
			else
				drawEndScreen(g);
		}
		private void drawEndScreen(Graphics g){
			g.drawString("Either you or your opponent have failed to restart...", 100, 50);
			g.setFont(new Font("Verdana",Font.BOLD,50));
			g.drawString("Thank you for playing",100,300);
			g.drawString("Rock Paper Scissors!!!", 100,375);
		}
		private void drawRestartScreen(Graphics g) {
			// TODO Auto-generated method stub
			g.drawRect(25, 175, 500, 350);
			g.drawRect(525, 175, 500, 350);
			g.drawString("You have picked "+rps.interpretByte(rps.game.getYourHand()), 100, 75);
			g.drawString("They have picked "+rps.interpretByte(rps.game.getTheirHand()), 100, 100);
			g.drawString(rps.game.getResult(), 100, 50);
			g.drawString("Do you want to restart?", 100, 150);
			g.setFont(new Font("Verdana",Font.BOLD,50));
			g.drawString("Yes", 225, 375);
			g.drawString("No", 750, 375);
			g.setFont(new Font("Verdana",Font.BOLD,20));
		}
		private void drawWaitingScreen(Graphics g) {
			// TODO Auto-generated method stub
			g.fillRect(0, 0, WIDTH, HEIGHT);
			//g.drawString("You have picked "+rps.interpretByte(rps.game.getYourHand()), 100, 75);
			g.drawString("Waiting for your Opponent to respond...", 100, 100);
//			painter.validate();
//			painter.repaint();
		}
		private void drawHandScreen(Graphics g) {
			// TODO Auto-generated method stub
			g.drawString("Pick your hand.",50,50);
			g.drawImage(rock,MARGAIN,100,null);
			g.drawImage(paper, MARGAIN*2+rock.getWidth(), 100, null);
			g.drawImage(scissors,MARGAIN*3+rock.getWidth()+paper.getWidth(),100,null);
			for(int i = 1;i<=3;i++)
				g.drawRect(MARGAIN*i+350*(i-1), 100, 350, 350);
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if(rps.accepted && e.getY()>100 && e.getY()<450){
				if(!inRestartMode && !waitingForOpponent){
					System.out.println("I clicked on: "+ (byte)((e.getX()/350)));
					rps.game.setYourHand((byte)((e.getX()/350)));
					rps.playYourHand();
					repaint();
					Toolkit.getDefaultToolkit().sync();
					waitingForOpponent = true;
				}
				if(inRestartMode && !waitingForOpponent){
					if(e.getX()/525 == 1){
						rps.restarting = false;
					}
					else{
						rps.restarting = true;
					}
					waitingForOpponent = true;
					
					repaint();
					Toolkit.getDefaultToolkit().sync();
				}
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
	public void play(){
		if(inRestartMode && waitingForOpponent){
			rps.restart();
			waitingForOpponent = false;
			inRestartMode = false;
			return;
		}
		if(waitingForOpponent){
			rps.waitForTheirHand();
			waitingForOpponent = false;
			inRestartMode = true;
			return;
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(rps.restarting){
			painter.repaint();
			play();
		}
		rps.game = null;
		try {
			RPS.input.close();
			RPS.output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inRestartMode = false;
		painter.repaint();
		System.out.println("Thank you for playing RockPaperScissors.");
	}
}

