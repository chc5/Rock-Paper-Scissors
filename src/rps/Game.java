package rps;

public class Game{
	private static final byte WIN = 1;
	private static final byte TIE = 0;
	private static final byte LOSE = -1;

	private static final byte ROCK = 0;
	private static final byte PAPER = 1;
	private static final byte SCISSOR = 2;
	private static final byte NOTHING = Byte.MIN_VALUE;
	
	private byte result = NOTHING;
	
	private byte yourHand;
	private byte theirHand;
	public Game(){
		yourHand = NOTHING;
		theirHand = NOTHING;
	}
	public byte getYourHand(){
		return yourHand;
	}
	public void setYourHand(final byte hand){
		yourHand = hand;
	}
	public byte getTheirHand(){
		return theirHand;
	}
	public void setTheirHand(final byte hand){
		theirHand = hand;
		checkCondition();
	}
	public String getResult(){
			if(result == WIN){
				return "YOU WIN";
			}
			if(result == TIE){
				return "IT'S A TIE";
			}
			if(result == LOSE){
				return "YOU LOSE";
			}
			return "IDKKK";
	}
	public void checkCondition(){
		if(yourHand == theirHand) 
			result = TIE;
		if((yourHand == ROCK && theirHand == SCISSOR) || (yourHand == PAPER && theirHand == ROCK) ||(yourHand == SCISSOR && theirHand == PAPER))
			result = WIN;
		if((yourHand == SCISSOR && theirHand == ROCK) || (yourHand == ROCK && theirHand == PAPER) ||(yourHand == PAPER && theirHand == SCISSOR))
			result = LOSE;
	}
}