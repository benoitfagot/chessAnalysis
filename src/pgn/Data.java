package pgn;

public class Data {
	private int turn;
	private int value;
	
	public Data(int turn, int value){
		this.turn= turn;
		this.value=value;
	}
	
	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
