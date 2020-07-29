package pgn;

public class Piece {
	String value;
	int x,y;
	
	public Piece(String value, int i, int j){
		this.value= value;
		this.x = i;
		this.y = j;
	}
	
	
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getX() {
		return x;
	}
	public void setX(int i) {
		this.x = i;
	}
	public int getY() {
		return y;
	}
	public void setY(int j) {
		this.y = j;
	}

}
