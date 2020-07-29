package pgn;

import java.io.Serializable;

public class Game implements Serializable{

  private String year, white_player, black_player, white_elo, black_elo, developing_score, capture_score, winner, list;
  private int piece_score_diff, black_moving_score, white_moving_score;
  private String[][] board;
  private Game[] games;
  

    

  public Game(String year, String white_player, String black_player, String white_elo, String black_elo, String winner) {

    this.year = year;

    this.white_player = white_player;

    this.black_player = black_player;
    
    this.white_elo = white_elo;
    
    this.black_elo = black_elo;
    
    this.winner = winner;
    

  }

  public Game(String year, String white_player, String black_player, 
		  String winner,  int piece_score_diff, int black_moving_score, 
		  int white_moving_score, String list, String[][] board) {

	    this.year = year;

	    this.white_player = white_player;

	    this.black_player = black_player;
	    
	    this.winner = winner;
	    
	    this.piece_score_diff = piece_score_diff;
	    
	    this.white_moving_score = white_moving_score;
	    
	    this.black_moving_score = black_moving_score;
	    
	    this.list = list;
	    
	    this.board = board;
	 }
    

  public String toString(){
	  String endgame="";
		for (int p=0; p<8;p++){
			for (int q=0; q<8;q++){
				if (this.board[p][q]=="p"){
					endgame += "x ";
				}
				else
				endgame += this.board[p][q]+ " ";
			}
			endgame += "\n";
		}
    return "\nAnnÃ©e de la partie : " + this.year + "\nJoueur blanc : " + this.white_player + "\nJoeur noir : " +
    this.black_player + "\nGagnant : " + this.winner + 
    "\nDifférence puissance des pièces : " + this.piece_score_diff + 
    "\nMouvement blanc : " + this.white_moving_score + "\nMouvement noir : " +
    this.black_moving_score +
    "\nListe des coups : " + this.list + "\nEchiquier final: \n" + endgame;

  } 

}
