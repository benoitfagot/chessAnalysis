package pgn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.github.bhlangonijr.chesslib.move.MoveConversionException;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class LineChartDemo extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	private static final String FILENAME = "files/championship.pgn";

	public LineChartDemo(String title,List <Data> blackMovings,
			List <Data> whiteMovings,
			List <Data> diffMovings,
			List <Data> diffScore, String name) {
		super(title);
		XYDataset dataset = createDataset(blackMovings,
				whiteMovings, diffMovings, diffScore);
		JFreeChart chart = createChart(dataset, "Event : "+name);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);

	}
	
	//retourne True si la case n'est pas occupée par une pièce
		static boolean isFree (String square){
			if (square.equals(".")){
				return true;
			}
			else return false;
		}
		
		//retourne True si la case est occupée par une pièce noire (exceptée roi)
		static boolean isBlackTaken (String square){
			switch (square){
			case "q":return true;
			case "n":return true;
			case "b":return true;
			case "r":return true;
			case "p":return true;
			default:return false;
			}
		}
		
		//retourne True si la case est occupée par une pièce noire (permet de construire liste pièces noires restantes)
		static boolean isBlackPiece (String square){
				switch (square){
				case "q":return true;
				case "n":return true;
				case "b":return true;
				case "r":return true;
				case "p":return true;
				case "k":return true;
				default:return false;
				}
			}
			
		
		//retourne True si la case est occupée par une pièece blanche (exceptée roi)
		static boolean isWhiteTaken (String square){ 
			switch (square){
			case "Q":return true;
			case "N":return true;
			case "B":return true;
			case "R":return true;
			case "P":return true;
			default:return false;
			}
		}
		
		//retourne True si la case est occupée par une pièce blanche (permet de construire liste pièces blanches restantes)
		static boolean isWhitePiece (String square){ 
			switch (square){
			case "Q":return true;
			case "N":return true;
			case "B":return true;
			case "R":return true;
			case "P":return true;
			case "K":return true;
			default:return false;
			}
		}
		
		//retourne les pièces blanches restantes sous forme de liste
		static List<Piece> getWhitePieces (String[][] board){
			List <Piece> whitePieces = new ArrayList<Piece>();
			for (int p=0; p<8;p++){
				for (int q=0; q<8;q++){
					if (isWhitePiece(board[p][q])){
						whitePieces.add(new Piece(board[p][q],p,q));
					}
				}	
			}
			return whitePieces;
			
		}
		
		
		//retourne les pièces noires restantes sous forme de liste
		static List<Piece> getBlackPieces (String[][] board){
				List <Piece> blackPieces = new ArrayList<Piece>();
				for (int p=0; p<8;p++){
					for (int q=0; q<8;q++){
						if (isBlackPiece(board[p][q])){
							blackPieces.add(new Piece(board[p][q],p,q));
						}
					}	
				}
				
				
				return blackPieces;
				
			}
		
		//retourne True si la case de coordonnées (c1,c2) est menacée par une pièce blanche
		static boolean isThreatenedByWhite (int c1,int c2, String[][]board){
			List <Piece> whitePieces = new ArrayList<Piece>();
			whitePieces = getWhitePieces(board);
			boolean threatened=false;
			String current="";
			for(Iterator iter = whitePieces.iterator(); iter.hasNext();){ 
				Piece piece = (Piece) iter.next();
				current = piece.getValue();
				int i= piece.getX();
				int j= piece.getY();
				switch(current){
					case "K":
						if (i-1>=0 && j-1>=0){
							if(c1==i-1 && c2==j-1){
								threatened=true;
							}
						}
						if (i-1>=0){
							if(c1==i-1 && c2==j){
								threatened=true;
							}
						}
						if (i-1>=0 && j+1<=7){
							if(c1==i-1 && c2==j+1){
								threatened=true;
							}
						}
						if (j-1>=0){
							if(c1==i && c2==j-1){
								threatened=true;
							}
						}
						if (j+1<=7){
							if(c1==i && c2==j+1){
								threatened=true;
							}
						}
						if (i+1<=7 && j-1>=0){
							if(c1==i+1 && c2==j-1){
								threatened=true;
							}
						}
						if (i+1<=7){
							if(c1==i+1 && c2==j){
								threatened=true;
							}
						}
						if (i+1<=7 && j+1<=7){
							if(c1==i+1 && c2==j+1){
								threatened=true;
							}
						}
						break;
					case "Q":
						if( i-1>=0){ //verticale supérieure
							int x=i, y=j;
							while( x-1>=0){
								if (c1==x-1 && c2==y){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y])){
									x--;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( i+1<=7){ //verticale infèrieure
							int x=i, y=j;
							while( x+1<=7){
								if (c1==x+1 && c2==y){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y])){
									x++;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( j-1>=0){ //horizontale gauche
							int x=i, y=j;
							while( y-1>=0){
								if (c1==x && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x][y-1])){
									y--;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( j+1<=7){ //horizontale droite
							int x=i, y=j;
							while(y+1<=7){
								if (c1==x && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x][y+1])){
									y++;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( i-1>=0 && j-1>=0){ //diagonale sup�rieure gauche
							int x=i, y=j;
							while( x-1>=0 && y-1>=0){
								if (c1==x-1 && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y-1])){
									x--;
									y--;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( i-1>=0 && j+1<=7){ //diagonale sup�rieure droite
							int x=i, y=j;
							while( x-1>=0 && y+1<=7){
								if (c1==x-1 && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y+1])){
									x--;
									y++;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( i+1<=7 && j-1>=0){ //diagonale inf�rieure gauche
							int x=i, y=j;
							while( x+1<=7 && y-1>=0){
								if (c1==x+1 && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y-1])){
									x++;
									y--;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( i+1<=7 && j+1<=7){ //diagonale sup�rieure droite
							int x=i, y=j;
							while( x+1<=7 && y+1<=7){
								if (c1==x+1 && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y+1])){
									x++;
									y++;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						break;
					case "R":
						if( i-1>=0){ //verticale sup�rieure
							int x=i, y=j;
							while( x-1>=0){
								if (c1==x-1 && c2==y){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y])){
									x--;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( i+1<=7){ //verticale inf�rieure
							int x=i, y=j;
							while( x+1<=7){
								if (c1==x+1 && c2==y){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y])){
									x++;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( j-1>=0){ //horizontale gauche
							int x=i, y=j;
							while( y-1>=0){
								if (c1==x && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x][y-1])){
									y--;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( j+1<=7){ //horizontale droite
							int x=i, y=j;
							while(y+1<=7){
								if (c1==x && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x][y+1])){
									y++;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						break;
					case "B":
						if( i-1>=0 && j-1>=0){ //diagonale sup�rieure gauche
							int x=i, y=j;
							while( x-1>=0 && y-1>=0){
								if (c1==x-1 && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y-1])){
									x--;
									y--;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( i-1>=0 && j+1<=7){ //diagonale sup�rieure droite
							int x=i, y=j;
							while( x-1>=0 && y+1<=7){
								if (c1==x-1 && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y+1])){
									x--;
									y++;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( i+1<=7 && j-1>=0){ //diagonale inf�rieure gauche
							int x=i, y=j;
							while( x+1<=7 && y-1>=0){
								if (c1==x+1 && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y-1])){
									x++;
									y--;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						if( i+1<=7 && j+1<=7){ //diagonale inf�rieure droite
							int x=i, y=j;
							while( x+1<=7 && y+1<=7){
								if (c1==x+1 && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y+1])){
									x++;
									y++;
									continue;
								}
								else{ //pièce alliée/roi ennemi/bordure de l'échiquier
									break;
								}
							}
						}
						break;
					case "N":
						if (i-2>=0 && j+1<=7){
							if(c1==i-2 && c2==j+1){
								threatened=true;
							}
						}
						if (i-1>=0 && j+2<=7){
							if(c1==i-1 && c2==j+2){
								threatened=true;
							}
						}
						if (i+1<=7 && j+2<=7){
							if(c1==i+1 && c2==j+2){
								threatened=true;
							}
						}
						if (i+2<=7 && j+1<=7){
							if(c1==i+2 && c2==j+1){
								threatened=true;
							}
						}
						if (i+2<=7 && j-1>=0){
							if(c1==i+2 && c2==j-1){
								threatened=true;
							}
						}
						if (i+1<=7 && j-2>=0){
							if(c1==i+1 && c2==j-2){
								threatened=true;
							}
						}
						if (i-1>=0 && j-2>=0){
							if(c1==i-1 && c2==j-2){
								threatened=true;
							}
						}
						if (i-2>=0 && j-1>=0){
							if(c1==i-2 && c2==j-1){
								threatened=true;
							}
						}
						break;
					case "P":
						if (i-1>=0 && j-1>=0){
							if (c1==i-1 && c2==j-1) 
								//le pion peut manger en diagonale gauche
							{
								threatened=true;
							}
						}
						if (i-1>=0 && j+1<=7){
							if (c1==i-1 && c2==j+1) 
								//le pion peut manger en diagonale droite
							{
								threatened=true;
							}
						}
						break;
					default:break;		
				}
			}
			return threatened;
		}
		
		//retourne True si la case est menacée par une pièce noire
		static boolean isThreatenedByBlack (int c1, int c2, String[][]board){
			List <Piece> blackPieces = new ArrayList<Piece>();
			blackPieces = getBlackPieces(board);
			boolean threatened=false;
			String current="";
			for(Iterator iter = blackPieces.iterator(); iter.hasNext();){
				Piece piece = (Piece) iter.next();
				current = piece.getValue();
				int i= piece.getX();
				int j= piece.getY();
				switch(current){
					case "k":
						if (i-1>=0 && j-1>=0){
							if(c1==i-1 && c2==j-1){
								threatened=true;
							}
						}
						if (i-1>=0){
							if(c1==i-1 && c2==j){
								threatened=true;
							}
						}
						if (i-1>=0 && j+1<=7){
							if(c1==i-1 && c2==j+1){
								threatened=true;
							}
						}
						if (j-1>=0){
							if(c1==i && c2==j-1){
								threatened=true;
							}
						}
						if (j+1<=7){
							if(c1==i && c2==j+1){
								threatened=true;
							}
						}
						if (i+1<=7 && j-1>=0){
							if(c1==i+1 && c2==j-1){
								threatened=true;
							}
						}
						if (i+1<=7){
							if(c1==i+1 && c2==j){
								threatened=true;
							}
						}
						if (i+1<=7 && j+1<=7){
							if(c1==i+1 && c2==j+1){
								threatened=true;
							}
						}
						break;
					case "q":
						if( i-1>=0){ //verticale sup�rieure
							int x=i, y=j;
							while( x-1>=0){
								if (c1==x-1 && c2==y){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y])){
									x--;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( i+1<=7){ //verticale inf�rieure
							int x=i, y=j;
							while( x+1<=7){
								if (c1==x+1 && c2==y){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y])){
									x++;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( j-1>=0){ //horizontale gauche
							int x=i, y=j;
							while( y-1>=0){
								if (c1==x && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x][y-1])){
									y--;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( j+1<=7){ //horizontale droite
							int x=i, y=j;
							while(y+1<=7){
								if (c1==x && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x][y+1])){
									y++;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( i-1>=0 && j-1>=0){ //diagonale sup�rieure gauche
							int x=i, y=j;
							while( x-1>=0 && y-1>=0){
								if (c1==x-1 && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y-1])){
									x--;
									y--;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( i-1>=0 && j+1<=7){ //diagonale sup�rieure droite
							int x=i, y=j;
							while( x-1>=0 && y+1<=7){
								if (c1==x-1 && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y+1])){
									x--;
									y++;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( i+1<=7 && j-1>=0){ //diagonale inf�rieure gauche
							int x=i, y=j;
							while( x+1<=7 && y-1>=0){
								if (c1==x+1 && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y-1])){
									x++;
									y--;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( i+1<=7 && j+1<=7){ //diagonale sup�rieure droite
							int x=i, y=j;
							while( x+1<=7 && y+1<=7){
								if (c1==x+1 && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y+1])){
									x++;
									y++;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						break;
					case "r":
						if( i-1>=0){ //verticale sup�rieure
							int x=i, y=j;
							while( x-1>=0){
								if (c1==x-1 && c2==y){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y])){
									x--;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( i+1<=7){ //verticale inf�rieure
							int x=i, y=j;
							while( x+1<=7){
								if (c1==x+1 && c2==y){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y])){
									x++;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( j-1>=0){ //horizontale gauche
							int x=i, y=j;
							while( y-1>=0){
								if (c1==x && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x][y-1])){
									y--;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( j+1<=7){ //horizontale droite
							int x=i, y=j;
							while(y+1<=7){
								if (c1==x && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x][y+1])){
									y++;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						break;
					case "b":
						if( i-1>=0 && j-1>=0){ //diagonale sup�rieure gauche
							int x=i, y=j;
							while( x-1>=0 && y-1>=0){
								if (c1==x-1 && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y-1])){
									x--;
									y--;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( i-1>=0 && j+1<=7){ //diagonale sup�rieure droite
							int x=i, y=j;
							while( x-1>=0 && y+1<=7){
								if (c1==x-1 && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x-1][y+1])){
									x--;
									y++;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( i+1<=7 && j-1>=0){ //diagonale inf�rieure gauche
							int x=i, y=j;
							while( x+1<=7 && y-1>=0){
								if (c1==x+1 && c2==y-1){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y-1])){
									x++;
									y--;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						if( i+1<=7 && j+1<=7){ //diagonale inf�rieure droite
							int x=i, y=j;
							while( x+1<=7 && y+1<=7){
								if (c1==x+1 && c2==y+1){
									threatened=true;
									break;
								}
								else if (isFree(board[x+1][y+1])){
									x++;
									y++;
									continue;
								}
								else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
									break;
								}
							}
						}
						break;
					case "n":
						if (i-2>=0 && j+1<=7){
							if(c1==i-2 && c2==j+1){
								threatened=true;
							}
						}
						if (i-1>=0 && j+2<=7){
							if(c1==i-1 && c2==j+2){
								threatened=true;
							}
						}
						if (i+1<=7 && j+2<=7){
							if(c1==i+1 && c2==j+2){
								threatened=true;
							}
						}
						if (i+2<=7 && j+1<=7){
							if(c1==i+2 && c2==j+1){
								threatened=true;
							}
						}
						if (i+2<=7 && j-1>=0){
							if(c1==i+2 && c2==j-1){
								threatened=true;
							}
						}
						if (i+1<=7 && j-2>=0){
							if(c1==i+1 && c2==j-2){
								threatened=true;
							}
						}
						if (i-1>=0 && j-2>=0){
							if(c1==i-1 && c2==j-2){
								threatened=true;
							}
						}
						if (i-2>=0 && j-1>=0){
							if(c1==i-2 && c2==j-1){
								threatened=true;
							}
						}
						break;
					case "p":
						if (i+1<=7 && j-1>=0){
							if (c1==i+1 && c2==j-1) 
								//le pion peut manger en diagonale gauche
							{
								threatened=true;
							}
						}
						if (i+1<=7 && j+1<=7){
							if (c1==i+1 && c2==j+1) 
								//le pion peut manger en diagonale droite
							{
								threatened=true;
							}
						}
						break;
					default:break;		
				}
			}
			return threatened;
		}
			
		//retourne True si le roi blanc est en échec
		static boolean isWhiteKingInCheck (String[][]board){
			boolean check = false;
			for(int i=0;i<8;i++){
				for(int j=0;j<8;j++){
					if(board[i][j]=="K"){
						check = isThreatenedByBlack(i,j,board); //est ce que le roi est menacé ?
					}
				}
			}
			return check;
		}
				
		//retourne True si le roi noir est en échec
		static boolean isBlackKingInCheck (String[][]board){
			boolean check = false;
			for(int i=0;i<8;i++){
				for(int j=0;j<8;j++){
					if(board[i][j]=="k"){
						check = isThreatenedByBlack(i,j,board); //est ce que le roi est menacé ?
					}
				}
			}
			return check;
		}
				
		//retourne True si le roi blanc n'est pas en échec après que la pièce blanche se soit deplacée
		static boolean isWhiteKingSafe (String piece, int oldx, int oldy, int newx, int newy, String[][]board){
			String[][] copy= board.clone();
			String memorize = copy[newx][newy]; //on stocke la valeur de la case qui va être écrasée
			boolean safe=true;
			copy[oldx][oldy]="."; //la case est libérée
			copy[newx][newy]=piece; //la pièce occupe la nouvelle case (ou remplace la piège mangée)
			for(int i=0;i<8;i++){
				for(int j=0;j<8;j++){
					if(copy[i][j]=="K"){
						safe = !isThreatenedByBlack(i,j,copy); //est ce que le roi est menacé ?
					}
				}
			}
			copy[oldx][oldy]=piece; //on remet le tableau au statut initial
			copy[newx][newy]=memorize; //on remet le tableau au statut initial
			return safe;
		}
		
		//retourne True si le roi noir n'est pas en échec après que la pièce noire se soit deplacée
		static boolean isBlackKingSafe (String piece, int oldx, int oldy, int newx, int newy, String[][]board){
			String[][] copy= board.clone();
			String memorize = copy[newx][newy]; //on stocke la valeur de la case qui va être écrasée
			boolean safe=true;
			copy[oldx][oldy]="."; //la case est libérée
			copy[newx][newy]=piece; //la pièce occupe la nouvelle case (ou remplace la piège mangée)
			for(int i=0;i<8;i++){
				for(int j=0;j<8;j++){
					if(copy[i][j]=="k"){
						safe = !isThreatenedByWhite(i,j,copy); //est ce que le roi est menacé ?
					}
				}
			}
			copy[oldx][oldy]=piece; //on remet le tableau au statut initial
			copy[newx][newy]=memorize; //on remet le tableau au statut initial
			return safe;
		}

		//retourne la liste des coups sous le format d'une Liste
		/*
		 * 
		 *Transformer la chaîne "1.d4  Nf6 2.c4 e6 3.Nf3 d5 4.g3..."
		 * en une liste {d4, Nf6, c4, e6, Nf3, d5, g3...} 
		 */
		public static List<String> return_list_of_plays( String game ){
			List<String> plays = new ArrayList<String>();
			boolean rock_play=false;
			char current;
			String play="";
			for (int i=0;i<game.length();i++){
				current = game.charAt(i);
				switch(current){
					case 'a' : play+=current;
						break;
					case 'b' : play+=current;
						break;
					case 'c' : play+=current;
						break;
					case 'd' : play+=current;
						break;
					case 'e' : play+=current;
						break;
					case 'f' : play+=current;
						break;
					case 'g' : play+=current;
						break;
					case 'h' : play+=current;
						break;
					case 'R' : play+=current;
								if( game.charAt(i+1) == ' '){ //exemple e8=R
									plays.add(play);
									play="";
								}
						break;
					case 'N' : play+=current;
								if( game.charAt(i+1) == ' '){ //exemple e8=R
									plays.add(play);
									play="";
								}
						break;
					case 'B' : play+=current;
								if( game.charAt(i+1) == ' '){ //exemple e8=R
									plays.add(play);
									play="";
								}
						break;
					case 'Q' : play+=current;
								if( game.charAt(i+1) == ' '){ //exemple e8=R
									plays.add(play);
									play="";
								}
						break;
					case 'K' : play+=current;
						break;
					case 'O' : play+=current;
								if(rock_play==false) rock_play=true;
								else if(rock_play==true && game.charAt(i+1) == ' '){ //O-O ou O-O-O fin du rock
									rock_play=false;
									plays.add(play);
									play="";
								}			
						break;
						
					case '-' : if(rock_play==true){
									play+=current;
									break;
								}
					else break;
					
					case 'x' : play+=current;
						break;
						
					case '+' : play+=current;
								plays.add(play);
								play="";
						break;
						
					case '#' : play+=current;
								plays.add(play);
								play="";
						break;
						
					case '=' : play+=current; //cas d'une promotion
						break;
						
					case '1' : if( game.charAt(i+1) == ' ' && game.charAt(i-1) != '-'){ //si le prochaine caractère est un espace c'est que c'est un coup
									play += current;
									plays.add(play);
									play="";
									break;
								}
					else if(game.charAt(i+1) == '+' || game.charAt(i+1) == '#' 
							|| game.charAt(i+1) == '='){ //si prochaine est un + ou un # le coup 
						                                                         //n'est pas fini
						play += current;
						break;
					}
					else break; //sinon c'est simplement le numéro d'un tour donc on passe au caractère suivant
					
					case '2' : if( game.charAt(i+1) == ' '){ 
									play += current;
									plays.add(play);
									play="";
									break;
								}
					else if(game.charAt(i+1) == '+' || game.charAt(i+1) == '#' 
							|| game.charAt(i+1) == '='){
					play += current;
					break;
					}
					else break; 
					
					case '3' : if( game.charAt(i+1) == ' '){ 
									play += current;
									plays.add(play);
									play="";
									break;
								}
					else if(game.charAt(i+1) == '+' || game.charAt(i+1) == '#' 
							|| game.charAt(i+1) == '='){ 
					play += current;
					break;
					}	
					else break; 

					case '4' : if( game.charAt(i+1) == ' '){ 
									play += current;
									plays.add(play);
									play="";
									break;
								}
					else if(game.charAt(i+1) == '+' || game.charAt(i+1) == '#' 
							|| game.charAt(i+1) == '='){
					play += current;
					break;
					}
					else break; 

					case '5' : if( game.charAt(i+1) == ' '){ 
									play += current;
									plays.add(play);
									play="";
									break;
								}
					else if(game.charAt(i+1) == '+' || game.charAt(i+1) == '#' 
							|| game.charAt(i+1) == '='){ 
					play += current;
					break;
					}
					else break; 
					
					case '6' : if( game.charAt(i+1) == ' '){ 
									play += current;
									plays.add(play);
									play="";
									break;
								}
					else if(game.charAt(i+1) == '+' || game.charAt(i+1) == '#' 
							|| game.charAt(i+1) == '='){
					play += current;
					break;
					}
					else break; 
					
					case '7' : if( game.charAt(i+1) == ' '){ 
									play += current;
									plays.add(play);
									play="";
									break;
								}
					else if(game.charAt(i+1) == '+' || game.charAt(i+1) == '#' 
							|| game.charAt(i+1) == '='){
					play += current;
					break;
					}
					else break; 
					
					case '8' : if( game.charAt(i+1) == ' '){ 
									play += current;
									plays.add(play);
									play="";
									break;
								}
					else if(game.charAt(i+1) == '+' || game.charAt(i+1) == '#' 
							|| game.charAt(i+1) == '='){
					play += current;
					break;
					}
					else break;
					
					case '.' : break;
					
					case ' ' : break;
					
					default: break;
				}
			}	
			
			return plays;
		}
		
		public void swap(String turn){
			if (turn=="black") turn="white";
			else turn="black";
		}
		
		//convertit une liste de coups de la forme "d4 Nf6 c4 e6 Nf3 d5 g3 Bb4+ Bd2 ... " en FEN
		public static String san_to_fen(String san){
		    MoveList list = new MoveList();
		    try {
				list.loadFromSan(san);
			} catch (MoveConversionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    //System.out.println("FEN of final position: " + list.getFen());
			return list.getFen();
			
		}
		
		
		//traduit la FEN en une matrice
		public static String[][] fen_to_board(String fen){
			String[] first = fen.split(" "); //d'abord isoler la partie qui d�crit les pi�ces
			String[] strings = first[0].split("/"); //puis isoler chaque rang�e

			String first_row = strings[0]; 
			String second_row = strings[1]; 
			String third_row = strings[2]; 
			String fourth_row = strings[3]; 
			String fifth_row = strings[4]; 
			String sixth_row = strings[5]; 
			String seventh_row = strings[6]; 
			String eighth_row = strings[7]; 

			String[][] board = new String[8][8];
			fen_parser(0,first_row, board);
			fen_parser(1,second_row, board);
			fen_parser(2,third_row, board);
			fen_parser(3,fourth_row, board);
			fen_parser(4,fifth_row, board);
			fen_parser(5,sixth_row, board);
			fen_parser(6,seventh_row, board);
			fen_parser(7,eighth_row, board);
			
			return board;
		}


		//analyse une rangée de la FEN pour remplir la matrice
		//exemple la FEN rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
		//l appel fen_parser(0,"rnbqkbnr", board); va remplir la première ligne de la matrice en analysant "rnbqkbnr"
		public static void fen_parser(int row, String row_data, String[][] board){

		char current;
		String sym = ".";
		int counter=0;

			for (int i=0; i<row_data.length();i++){
				current = row_data.charAt(i); //current est le caract�re de la string courant (on analyse un caractère à la fois)
				if(current == '1') {
					board[row][counter]=sym;
					counter++;
			 	}
				if(current == '2' ){
					for(int j=0;j<2;j++){
						board[row][counter]=sym;
						counter++;
					}
				}
				if(current == '3' ){
					for(int j=0;j<3;j++){
						board[row][counter]=sym;
						counter++;
					}
				}
				if(current == '4' ){
					for(int j=0;j<4;j++){
						board[row][counter]=sym;
						counter++;
					}
				}
				if(current == '5' ){
					for(int j=0;j<5;j++){
						board[row][counter]=sym;
						counter++;
					}
				}
				if(current == '6' ){
					for(int j=0;j<6;j++){
						board[row][counter]=sym;
						counter++;
					}
				}	
				if(current == '7' ){
					for(int j=0;j<7;j++){
						board[row][counter]=sym;
						counter++;
					}
				}
				if(current == '8' ){
					for(int j=0;j<8;j++){
						board[row][counter]=sym;
						counter++;
					}
				}
				else{ //current = "r,n,b,q,k,p,R,N,B,Q,K,P" le caractère analysé est une pièce
					switch(current){
						case 'r':
							board[row][counter]= "r";
							counter++;
							break;
						case 'n':
							board[row][counter]="n";
							counter++;
							break;
						case 'b':
							board[row][counter]="b";
							counter++;
							break;
						case 'q':
							board[row][counter]="q";
							counter++;
							break;
						case 'k':
							board[row][counter]="k";
							counter++;
							break;
						case 'p':
							board[row][counter]="p";
							counter++;
							break;
						case 'P':
							board[row][counter]="P";
							counter++;
							break;
						case 'R':
							board[row][counter]="R";
							counter++;
							break;
						case 'N':
							board[row][counter]="N";
							counter++;
							break;
						case 'B':
							board[row][counter]="B";
							counter++;
							break;
						case 'Q':
							board[row][counter]="Q";
							counter++;
							break;
						case 'K':
							board[row][counter]="K";
							counter++;
							break;
						default:break;		
					}

				}
			}
		}

		//retourne la puissance des pièces blanches restantes sur l'échiquier
		public static int calculate_white_pieces_value(String[][] board) {
			int score=0;
			String current="";
			for (int i=0;i<8;i++){
				for(int j=0;j<8;j++){
					current = board[i][j];
					switch (current){
						case "P":
							score+=1;
							break;
						case "R":
							score+=5;
							break;
						case "N":
							score+=3;
							break;
						case "B":
							score+=3;
							break;
						case "Q":
							score+=9;
							break;
						default:break;		
					}
				}
			}
			
			return score;
		}
		
		//retourne la puissance des pièces noires restantes sur l'échiquier
		public static int calculate_black_pieces_value(String[][] board) {
			int score=0;
			String current="";
			for (int i=0;i<8;i++){
				for(int j=0;j<8;j++){
					current = board[i][j];
					switch (current){
						case "p":
							score+=1;
							break;
						case "r":
							score+=5;
							break;
						case "n":
							score+=3;
							break;
						case "b":
							score+=3;
							break;
						case "q":
							score+=9;
							break;
						default:break;		
					}
				}
			}
			
			return score;
		}
		
		//retourne la différence de puissance des pièces restantes (gagnant - perdant)
		public static int piece_score_difference (int white_score, int black_score, String winner){
			int difference=0;
			if (winner.equals("white") || winner.equals("draw")){
				difference = white_score - black_score;
			}
			else {
				difference = black_score - white_score;
			}
			
			return difference;	
		}
		
		//retourne la différence de mouvements des pièces restantes (gagnant - perdant)
		public static int moving_score_difference (int white_moving, int black_moving, String winner){
			int difference=0;
			if (winner.equals("white") || winner.equals("draw")){
				difference = white_moving - black_moving;
			}
			else {
				difference = black_moving - white_moving;
			}
			
			return difference;	
		}

		//calcule le nombre de mouvements possibles des pièces blanches restantes
		public static int calculate_white_movings (String[][]board){
			int score=0;
			List <Piece> whitePieces = new ArrayList<Piece>();
			whitePieces = getWhitePieces(board);
			String current="";
			int k=0;
			for(Iterator iter = whitePieces.iterator(); iter.hasNext();){
				Piece piece = (Piece) iter.next();
				current = piece.getValue();
				int i= piece.getX();
				int j= piece.getY();
					switch (current){
						case "P":
							if (i==6){ //le pion n'a pas encore bougé (il peut donc bouger de deux cases)
								if (isWhiteKingInCheck(board)){ //si le roi blanc est en échec
									if( isFree(board[4][j]) && isFree(board[5][j]) //si les cases devant lui sont vides
											&& isWhiteKingSafe(current,i,j,4,j,board)){ //et le roi blanc n'est plus en échec après le mouvement
										score+=1; //alors le mouvement est compté
									}
								}
								else if (isFree(board[4][j]) && isFree(board[5][j]) //sinon si les cases devant lui sont vides
										&& !isThreatenedByBlack(4, j, board) //la case de destination n'est pas menacée
										&& isWhiteKingSafe(current,i,j,4,j,board)){ //le roi blanc n'est pas en échec
									score+=1; //alors le mouvement est compté
								}
							}
							if (i-1>=0){
								if (isWhiteKingInCheck(board)){
									if (isFree(board[i-1][j])
											&& isWhiteKingSafe(current,i,j,i-1,j,board)){
										score+=1;
									}
								}
								if (isFree(board[i-1][j])
										&& !isThreatenedByBlack(i-1, j, board)
										&& isWhiteKingSafe(current,i,j,i-1,j,board)){
									//le pion peut avancer verticalement
									score+=1;
								}
							}
							if (i-1>=0 && j-1>=0){
								if (isWhiteKingInCheck(board)){
									if (isBlackTaken(board[i-1][j-1])
											&& isWhiteKingSafe(current,i,j,i-1,j-1,board)) 
										//le pion peut manger en diagonale gauche
									{
										score+=1;
									}
								}
								if (isBlackTaken(board[i-1][j-1])
										&& !isThreatenedByBlack(i-1, j-1, board)
										&& isWhiteKingSafe(current,i,j,i-1,j-1,board)) 
									//le pion peut manger en diagonale gauche
								{
									score+=1;
								}
							}
							if (i-1>=0 && j+1<=7){
								if (isWhiteKingInCheck(board)){
									if (isBlackTaken(board[i-1][j+1])
											&& isWhiteKingSafe(current,i,j,i-1,j+1,board)) 
										//le pion peut manger en diagonale droite
									{
										score+=1;
									}
								}
								if (isBlackTaken(board[i-1][j+1])
										&& !isThreatenedByBlack(i-1, j+1, board)
										&& isWhiteKingSafe(current,i,j,i-1,j+1,board)) 
									//le pion peut manger en diagonale droite
								{
									score+=1;
								}
							}
							break;
						case "R":
							if( i-1>=0){ //verticale sup�rieure
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x-1>=0){
										if (isBlackTaken(board[x-1][y])
												&& isWhiteKingSafe(current, i, j, x-1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y])){
												if(isWhiteKingSafe(current, i, j, x-1, y, board)){
													score+=1;
												}
											x--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0){
										if (isBlackTaken(board[x-1][y])
												&& !isThreatenedByBlack(x-1, y, board)
												&& isWhiteKingSafe(current, i, j, x-1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y])){
												if(!isThreatenedByBlack(x-1, y, board)
														&& isWhiteKingSafe(current, i, j, x-1, y, board)){
													score+=1;
												}
											x--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7){ //verticale inf�rieure
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x+1<=7){
										if (isBlackTaken(board[x+1][y])
												&& isWhiteKingSafe(current, i, j, x+1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y])){
												if (isWhiteKingSafe(current, i, j, x+1, y, board)){
													score+=1;
												}
											x++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7){
										if (isBlackTaken(board[x+1][y])
												&& !isThreatenedByBlack(x+1, y, board)
												&& isWhiteKingSafe(current, i, j, x+1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y])){
												if (!isThreatenedByBlack(x+1, y, board)
														&& isWhiteKingSafe(current, i, j, x+1, y, board)){
													score+=1;
												}
											x++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}	
							}
							if( j-1>=0){ //horizontale gauche
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( y-1>=0){
										if (isBlackTaken(board[x][y-1])
												&& isWhiteKingSafe(current, i, j, x, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y-1])){
												if(isWhiteKingSafe(current, i, j, x, y-1, board)){
													score+=1;
												}
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( y-1>=0){
										if (isBlackTaken(board[x][y-1])
												&& !isThreatenedByBlack(x, y-1, board)
												&& isWhiteKingSafe(current, i, j, x, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y-1])){
												if( !isThreatenedByBlack(x, y-1, board)
														&& isWhiteKingSafe(current, i, j, x, y-1, board)){
													score+=1;
												}
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( j+1<=7){ //horizontale droite
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while(y+1<=7){
										if (isBlackTaken(board[x][y+1])
												&& isWhiteKingSafe(current, i, j, x, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y+1]) ){
												if(isWhiteKingSafe(current, i, j, x, y+1, board)){
													score+=1;
												}
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while(y+1<=7){
										if (isBlackTaken(board[x][y+1])
												&& !isThreatenedByBlack(x, y+1, board)
												&& isWhiteKingSafe(current, i, j, x, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y+1]) ){
												if(!isThreatenedByBlack(x, y+1, board)
														&& isWhiteKingSafe(current, i, j, x, y+1, board)){
													score+=1;
												}
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							break;
						case "N":
							if (i-2>=0 && j+1<=7){
								if (isWhiteKingInCheck(board)){
									if((isFree(board[i-2][j+1]) || 
											isBlackTaken(board[i-2][j+1]))
											&& isWhiteKingSafe(current, i, j, i-2, j+1, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i-2][j+1]) || 
										isBlackTaken(board[i-2][j+1]))
										&& !isThreatenedByBlack(i-2, j+1, board)
										&& isWhiteKingSafe(current, i, j, i-2, j+1, board) ){
									score+=1;
								}
							}
							if (i-1>=0 && j+2<=7){
								if (isWhiteKingInCheck(board)){
									if((isFree(board[i-1][j+2]) || 
											isBlackTaken(board[i-1][j+2]))
											&& isWhiteKingSafe(current, i, j, i-1, j+2, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i-1][j+2]) || 
										isBlackTaken(board[i-1][j+2]))
										&& !isThreatenedByBlack(i-1, j+2, board)
										&& isWhiteKingSafe(current, i, j, i-1, j+2, board) ){
									score+=1;
								}
							}
							if (i+1<=7 && j+2<=7){
								if (isWhiteKingInCheck(board)){
									if((isFree(board[i+1][j+2]) ||
											isBlackTaken(board[i+1][j+2]))
											&& isWhiteKingSafe(current, i, j, i+1, j+2, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i+1][j+2]) ||
										isBlackTaken(board[i+1][j+2]))
										&& !isThreatenedByBlack(i+1, j+2, board)
										&& isWhiteKingSafe(current, i, j, i+1, j+2, board) ){
									score+=1;
								}
							}
							if (i+2<=7 && j+1<=7){
								if (isWhiteKingInCheck(board)){
									if((isFree(board[i+2][j+1]) ||
											isBlackTaken(board[i+2][j+1]))
											&& isWhiteKingSafe(current, i, j, i+2, j+1, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i+2][j+1]) ||
										isBlackTaken(board[i+2][j+1]))
										&& !isThreatenedByBlack(i+2, j+1, board)
										&& isWhiteKingSafe(current, i, j, i+2, j+1, board) ){
									score+=1;
								}
							}
							if (i+2<=7 && j-1>=0){
								if (isWhiteKingInCheck(board)){
									if((isFree(board[i+2][j-1]) ||
											isBlackTaken(board[i+2][j-1]))
											&& isWhiteKingSafe(current, i, j, i+2, j-1, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i+2][j-1]) ||
										isBlackTaken(board[i+2][j-1]))
										&& !isThreatenedByBlack(i+2, j-1, board)
										&& isWhiteKingSafe(current, i, j, i+2, j-1, board) ){
									score+=1;
								}
							}
							if (i+1<=7 && j-2>=0){
								if (isWhiteKingInCheck(board)){
									if((isFree(board[i+1][j-2]) ||
											isBlackTaken(board[i+1][j-2]))
											&& isWhiteKingSafe(current, i, j, i+1, j-2, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i+1][j-2]) ||
										isBlackTaken(board[i+1][j-2]))
										&& !isThreatenedByBlack(i+1, j-2, board)
										&& isWhiteKingSafe(current, i, j, i+1, j-2, board) ){
									score+=1;
								}
							}
							if (i-1>=0 && j-2>=0){
								if (isWhiteKingInCheck(board)){
									if(isFree(board[i-1][j-2]) ||
											isBlackTaken(board[i-1][j-2])
											&& isWhiteKingSafe(current, i, j, i-1, j-2, board) ){
										score+=1;
									}
								}
								else if(isFree(board[i-1][j-2]) ||
										isBlackTaken(board[i-1][j-2])
										&& !isThreatenedByBlack(i-1, j-2, board)
										&& isWhiteKingSafe(current, i, j, i-1, j-2, board) ){
									score+=1;
								}
							}
							if (i-2>=0 && j-1>=0){
								if (isWhiteKingInCheck(board)){
									if(isFree(board[i-2][j-1]) || 
											isBlackTaken(board[i-2][j-1])
											&& isWhiteKingSafe(current, i, j, i-2, j-1, board) ){
										score+=1;
									}
								}
								else if(isFree(board[i-2][j-1]) || 
										isBlackTaken(board[i-2][j-1])
										&& !isThreatenedByBlack(i-2, j-1, board)
										&& isWhiteKingSafe(current, i, j, i-2, j-1, board) ){
									score+=1;
								}
							}
							break;
						case "B":
							if( i-1>=0 && j-1>=0){ //diagonale sup�rieure gauche
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x-1>=0 && y-1>=0){
										if (isBlackTaken(board[x-1][y-1])
												&& isWhiteKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y-1]) ){
											if(isWhiteKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											}
											x--;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0 && y-1>=0){
										if (isBlackTaken(board[x-1][y-1])
												&& !isThreatenedByBlack(x-1, y-1, board)
												&& isWhiteKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y-1]) ){
											if(!isThreatenedByBlack(x-1, y-1, board)
													&& isWhiteKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											}
											x--;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i-1>=0 && j+1<=7){ //diagonale sup�rieure droite
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x-1>=0 && y+1<=7){
										if (isBlackTaken(board[x-1][y+1])
												&& isWhiteKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y+1]) ){
											if(isWhiteKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											}
											x--;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0 && y+1<=7){
										if (isBlackTaken(board[x-1][y+1])
												&& !isThreatenedByBlack(x-1, y+1, board)
												&& isWhiteKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y+1]) ){
											if(!isThreatenedByBlack(x-1, y+1, board)
													&& isWhiteKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											}
											x--;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7 && j-1>=0){ //diagonale inf�rieure gauche
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x+1<=7 && y-1>=0){
										if (isBlackTaken(board[x+1][y-1])
												&& isWhiteKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y-1]) ){
											if(isWhiteKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											}
											x++;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7 && y-1>=0){
										if (isBlackTaken(board[x+1][y-1])
												&& !isThreatenedByBlack(x+1, y-1, board)
												&& isWhiteKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y-1]) ){
											if(!isThreatenedByBlack(x+1, y-1, board)
													&& isWhiteKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											}
											x++;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7 && j+1<=7){ //diagonale inferieure droite
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x+1<=7 && y+1<=7){
										if (isBlackTaken(board[x+1][y+1])
												&& isWhiteKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y+1]) ){
											if(isWhiteKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											}
											x++;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7 && y+1<=7){
										if (isBlackTaken(board[x+1][y+1])
												&&!isThreatenedByBlack(x+1, y+1, board)
												&& isWhiteKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y+1]) ){
											if(!isThreatenedByBlack(x+1, y+1, board)
													&& isWhiteKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											}
											x++;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							break;
						case "Q":
							if( i-1>=0){ //verticale sup�rieure
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x-1>=0){
										if (isBlackTaken(board[x-1][y])
												&& isWhiteKingSafe(current, i, j, x-1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y])){
												if(isWhiteKingSafe(current, i, j, x-1, y, board)){
													score+=1;
												}
											x--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0){
										if (isBlackTaken(board[x-1][y])
												&& !isThreatenedByBlack(x-1, y, board)
												&& isWhiteKingSafe(current, i, j, x-1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y])){
												if(!isThreatenedByBlack(x-1, y, board)
														&& isWhiteKingSafe(current, i, j, x-1, y, board)){
													score+=1;
												}
											x--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7){ //verticale inf�rieure
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x+1<=7){
										if (isBlackTaken(board[x+1][y])
												&& isWhiteKingSafe(current, i, j, x+1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y])){
												if (isWhiteKingSafe(current, i, j, x+1, y, board)){
													score+=1;
												}
											x++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7){
										if (isBlackTaken(board[x+1][y])
												&& !isThreatenedByBlack(x+1, y, board)
												&& isWhiteKingSafe(current, i, j, x+1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y])){
												if (!isThreatenedByBlack(x+1, y, board)
														&& isWhiteKingSafe(current, i, j, x+1, y, board)){
													score+=1;
												}
											x++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}	
							}
							if( j-1>=0){ //horizontale gauche
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( y-1>=0){
										if (isBlackTaken(board[x][y-1])
												&& isWhiteKingSafe(current, i, j, x, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y-1])){
												if(isWhiteKingSafe(current, i, j, x, y-1, board)){
													score+=1;
												}
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( y-1>=0){
										if (isBlackTaken(board[x][y-1])
												&& !isThreatenedByBlack(x, y-1, board)
												&& isWhiteKingSafe(current, i, j, x, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y-1])){
												if( !isThreatenedByBlack(x, y-1, board)
														&& isWhiteKingSafe(current, i, j, x, y-1, board)){
													score+=1;
												}
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( j+1<=7){ //horizontale droite
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while(y+1<=7){
										if (isBlackTaken(board[x][y+1])
												&& isWhiteKingSafe(current, i, j, x, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y+1]) ){
												if(isWhiteKingSafe(current, i, j, x, y+1, board)){
													score+=1;
												}
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while(y+1<=7){
										if (isBlackTaken(board[x][y+1])
												&& !isThreatenedByBlack(x, y+1, board)
												&& isWhiteKingSafe(current, i, j, x, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y+1]) ){
												if(!isThreatenedByBlack(x, y+1, board)
														&& isWhiteKingSafe(current, i, j, x, y+1, board)){
													score+=1;
												}
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i-1>=0 && j-1>=0){ //diagonale sup�rieure gauche
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x-1>=0 && y-1>=0){
										if (isBlackTaken(board[x-1][y-1])
												&& isWhiteKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y-1]) ){
											if(isWhiteKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											}
											x--;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0 && y-1>=0){
										if (isBlackTaken(board[x-1][y-1])
												&& !isThreatenedByBlack(x-1, y-1, board)
												&& isWhiteKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y-1]) ){
											if(!isThreatenedByBlack(x-1, y-1, board)
													&& isWhiteKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											}
											x--;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i-1>=0 && j+1<=7){ //diagonale sup�rieure droite
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x-1>=0 && y+1<=7){
										if (isBlackTaken(board[x-1][y+1])
												&& isWhiteKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y+1]) ){
											if(isWhiteKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											}
											x--;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0 && y+1<=7){
										if (isBlackTaken(board[x-1][y+1])
												&& !isThreatenedByBlack(x-1, y+1, board)
												&& isWhiteKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y+1]) ){
											if(!isThreatenedByBlack(x-1, y+1, board)
													&& isWhiteKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											}
											x--;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7 && j-1>=0){ //diagonale inf�rieure gauche
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x+1<=7 && y-1>=0){
										if (isBlackTaken(board[x+1][y-1])
												&& isWhiteKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y-1]) ){
											if(isWhiteKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											}
											x++;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7 && y-1>=0){
										if (isBlackTaken(board[x+1][y-1])
												&& !isThreatenedByBlack(x+1, y-1, board)
												&& isWhiteKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y-1]) ){
											if(!isThreatenedByBlack(x+1, y-1, board)
													&& isWhiteKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											}
											x++;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7 && j+1<=7){ //diagonale inferieure droite
								int x=i, y=j;
								if (isWhiteKingInCheck(board)){
									while( x+1<=7 && y+1<=7){
										if (isBlackTaken(board[x+1][y+1])
												&& isWhiteKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y+1]) ){
											if(isWhiteKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											}
											x++;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7 && y+1<=7){
										if (isBlackTaken(board[x+1][y+1])
												&&!isThreatenedByBlack(x+1, y+1, board)
												&& isWhiteKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y+1]) ){
											if(!isThreatenedByBlack(x+1, y+1, board)
													&& isWhiteKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											}
											x++;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							break;
						case "K":
							if (i-1>=0 && j-1>=0){
								if((isFree(board[i-1][j-1]) || 
										isBlackTaken(board[i-1][j-1]))
										&& !isThreatenedByBlack(i-1, j-1, board)
										&& isWhiteKingSafe(current, i, j, i-1, j-1, board) ){
									score+=1;
								}
							}
							if (i-1>=0){
								if((isFree(board[i-1][j]) || 
										isBlackTaken(board[i-1][j]))
										&& !isThreatenedByBlack(i-1, j, board)
										&& isWhiteKingSafe(current, i, j, i-1, j, board) ){
									score+=1;
								}
							}
							if (i-1>=0 && j+1<=7){
								if((isFree(board[i-1][j+1]) ||
										isBlackTaken(board[i-1][j+1]))
										&& !isThreatenedByBlack(i-1, j+1, board)
										&& isWhiteKingSafe(current, i, j, i-1, j+1, board) ){
									score+=1;
								}
							}
							if (j-1>=0){
								if((isFree(board[i][j-1]) ||
										isBlackTaken(board[i][j-1]))
										&& !isThreatenedByBlack(i, j-1, board)
										&& isWhiteKingSafe(current, i, j, i, j-1, board) ){
									score+=1;
								}
							}
							if (j+1<=7){
								if((isFree(board[i][j+1]) ||
										isBlackTaken(board[i][j+1]))
										&& !isThreatenedByBlack(i, j+1, board)
										&& isWhiteKingSafe(current, i, j, i, j+1, board) ){
									score+=1;
								}
							}
							if (i+1<=7 && j-1>=0){
								if((isFree(board[i+1][j-1]) ||
										isBlackTaken(board[i+1][j-1]))
										&& !isThreatenedByBlack(i+1, j-1, board)
										&& isWhiteKingSafe(current, i, j, i+1, j-1, board) ){
									score+=1;
								}
							}
							if (i+1<=7){
								if((isFree(board[i+1][j]) ||
										isBlackTaken(board[i+1][j]))
										&& !isThreatenedByBlack(i+1, j, board)
										&& isWhiteKingSafe(current, i, j, i+1, j, board) ){
									score+=1;
								}
							}
							if (i+1<=7 && j+1<=7){
								if((isFree(board[i+1][j+1]) || 
										isBlackTaken(board[i+1][j+1]))
										&& !isThreatenedByBlack(i+1, j+1, board)
										&& isWhiteKingSafe(current, i, j, i+1, j+1, board) ){
									score+=1;
								}
							}
							break;
						default:break;		
					}
				}		
			return score;
		}
		
		//calcule le nombre de mouvements possibles des pièces noires restantes
		public static int calculate_black_movings (String[][]board){
			int score=0;
			List <Piece> blackPieces = new ArrayList<Piece>();
			blackPieces = getBlackPieces(board);
			String current="";
			int k=0;
			for(Iterator iter = blackPieces.iterator(); iter.hasNext();){
				Piece piece = (Piece) iter.next();
				current = piece.getValue();
				int i= piece.getX();
				int j= piece.getY();
					switch (current){
						case "p":
							if (i==1){ //le pion n'a pas encore bougé
								if (isBlackKingInCheck(board)){
									if(isFree(board[2][j]) && isFree(board[3][j])
											&& isBlackKingSafe(current, i, j, 3, j, board)){
										score+=1;
									}
								}
								else if (isFree(board[2][j]) && isFree(board[3][j])
										&& !isThreatenedByWhite(3, j, board)
										&& isBlackKingSafe(current, i, j, 3, j, board)){
									score+=1;
								}
							}
							if (i+1<=7){
								if (isBlackKingInCheck(board)){
									if (isFree(board[i+1][j])
											&& isBlackKingSafe(current, i, j, i-1, j, board)){
										//le pion peut avancer verticalement
										score+=1;
									}
								}
								if (isFree(board[i+1][j])
										&& !isThreatenedByWhite(i+1, j, board)
										&& isBlackKingSafe(current, i, j, i-1, j, board)){
									//le pion peut avancer verticalement
									score+=1;
								}
							}
							if (i+1<=7 && j-1>=0){
								if (isBlackKingInCheck(board)){
									if (isWhiteTaken(board[i-1][j-1])
											&& !isThreatenedByWhite(i+1, j-1, board)
											&& isBlackKingSafe(current, i, j, i+1, j-1, board)) 
										//le pion peut manger en diagonale gauche
									{
										score+=1;
									}
								}
								if (isWhiteTaken(board[i-1][j-1])
										&& !isThreatenedByWhite(i+1, j-1, board)
										&& isBlackKingSafe(current, i, j, i+1, j-1, board)) 
									//le pion peut manger en diagonale gauche
								{
									score+=1;
								}
							}
							if (i+1<=7 && j+1<=7){
								if (isBlackKingInCheck(board)){
									if (isWhiteTaken(board[i-1][j+1])
											&& isBlackKingSafe(current, i, j, i+1, j+1, board)) 
										//le pion peut manger en diagonale droite
									{
										score+=1;
									}
								}
								if (isWhiteTaken(board[i-1][j+1])
										&& !isThreatenedByWhite(i+1, j+1, board)
										&& isBlackKingSafe(current, i, j, i+1, j+1, board)) 
									//le pion peut manger en diagonale droite
								{
									score+=1;
								}
							}
							break;
						case "r":
							if( i-1>=0){ //verticale sup�rieure
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x-1>=0){
										if (isWhiteTaken(board[x-1][y])
												&& isBlackKingSafe(current, i, j, x-1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y])){
												if(isBlackKingSafe(current, i, j, x-1, y, board)){
													score+=1;
												}
											x--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0){
										if (isWhiteTaken(board[x-1][y])
												&& !isThreatenedByWhite(x-1, y, board)
												&& isBlackKingSafe(current, i, j, x-1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y])){
												if(!isThreatenedByWhite(x-1, y, board)
														&& isBlackKingSafe(current, i, j, x-1, y, board)){
													score+=1;
												}
											x--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7){ //verticale inf�rieure
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x+1<=7){
										if (isWhiteTaken(board[x+1][y])
												&& isBlackKingSafe(current, i, j, x+1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y])){
												if (isBlackKingSafe(current, i, j, x+1, y, board)){
													score+=1;
												}
											x++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7){
										if (isWhiteTaken(board[x+1][y])
												&& !isThreatenedByWhite(x+1, y, board)
												&& isBlackKingSafe(current, i, j, x+1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y])){
												if (!isThreatenedByWhite(x+1, y, board)
														&& isBlackKingSafe(current, i, j, x+1, y, board)){
													score+=1;
												}
											x++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}	
							}
							if( j-1>=0){ //horizontale gauche
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( y-1>=0){
										if (isWhiteTaken(board[x][y-1])
												&& isBlackKingSafe(current, i, j, x, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y-1])){
												if(isBlackKingSafe(current, i, j, x, y-1, board)){
													score+=1;
												}
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( y-1>=0){
										if (isWhiteTaken(board[x][y-1])
												&& !isThreatenedByWhite(x, y-1, board)
												&& isBlackKingSafe(current, i, j, x, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y-1])){
												if( !isThreatenedByWhite(x, y-1, board)
														&& isBlackKingSafe(current, i, j, x, y-1, board)){
													score+=1;
												}
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( j+1<=7){ //horizontale droite
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while(y+1<=7){
										if (isWhiteTaken(board[x][y+1])
												&& isBlackKingSafe(current, i, j, x, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y+1]) ){
												if(isBlackKingSafe(current, i, j, x, y+1, board)){
													score+=1;
												}
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while(y+1<=7){
										if (isWhiteTaken(board[x][y+1])
												&& !isThreatenedByWhite(x, y+1, board)
												&& isBlackKingSafe(current, i, j, x, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y+1]) ){
												if(!isThreatenedByWhite(x, y+1, board)
														&& isBlackKingSafe(current, i, j, x, y+1, board)){
													score+=1;
												}
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							break;
						case "n":
							if (i-2>=0 && j+1<=7){
								if (isBlackKingInCheck(board)){
									if((isFree(board[i-2][j+1]) || 
											isWhiteTaken(board[i-2][j+1]))
											&& isBlackKingSafe(current, i, j, i-2, j+1, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i-2][j+1]) || 
										isWhiteTaken(board[i-2][j+1]))
										&& !isThreatenedByWhite(i-2, j+1, board)
										&& isBlackKingSafe(current, i, j, i-2, j+1, board) ){
									score+=1;
								}
							}
							if (i-1>=0 && j+2<=7){
								if (isBlackKingInCheck(board)){
									if((isFree(board[i-1][j+2]) || 
											isWhiteTaken(board[i-1][j+2]))
											&& isBlackKingSafe(current, i, j, i-1, j+2, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i-1][j+2]) || 
										isWhiteTaken(board[i-1][j+2]))
										&& !isThreatenedByWhite(i-1, j+2, board)
										&& isBlackKingSafe(current, i, j, i-1, j+2, board) ){
									score+=1;
								}
							}
							if (i+1<=7 && j+2<=7){
								if (isBlackKingInCheck(board)){
									if((isFree(board[i+1][j+2]) ||
											isWhiteTaken(board[i+1][j+2]))
											&& isBlackKingSafe(current, i, j, i+1, j+2, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i+1][j+2]) ||
										isWhiteTaken(board[i+1][j+2]))
										&& !isThreatenedByWhite(i+1, j+2, board)
										&& isBlackKingSafe(current, i, j, i+1, j+2, board) ){
									score+=1;
								}
							}
							if (i+2<=7 && j+1<=7){
								if (isBlackKingInCheck(board)){
									if((isFree(board[i+2][j+1]) ||
											isWhiteTaken(board[i+2][j+1]))
											&& isBlackKingSafe(current, i, j, i+2, j+1, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i+2][j+1]) ||
										isWhiteTaken(board[i+2][j+1]))
										&& !isThreatenedByWhite(i+2, j+1, board)
										&& isBlackKingSafe(current, i, j, i+2, j+1, board) ){
									score+=1;
								}
							}
							if (i+2<=7 && j-1>=0){
								if (isBlackKingInCheck(board)){
									if((isFree(board[i+2][j-1]) ||
											isWhiteTaken(board[i+2][j-1]))
											&& isBlackKingSafe(current, i, j, i+2, j-1, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i+2][j-1]) ||
										isWhiteTaken(board[i+2][j-1]))
										&& !isThreatenedByWhite(i+2, j-1, board)
										&& isBlackKingSafe(current, i, j, i+2, j-1, board) ){
									score+=1;
								}
							}
							if (i+1<=7 && j-2>=0){
								if (isBlackKingInCheck(board)){
									if((isFree(board[i+1][j-2]) ||
											isWhiteTaken(board[i+1][j-2]))
											&& isBlackKingSafe(current, i, j, i+1, j-2, board) ){
										score+=1;
									}
								}
								else if((isFree(board[i+1][j-2]) ||
										isWhiteTaken(board[i+1][j-2]))
										&& !isThreatenedByWhite(i+1, j-2, board)
										&& isBlackKingSafe(current, i, j, i+1, j-2, board) ){
									score+=1;
								}
							}
							if (i-1>=0 && j-2>=0){
								if (isBlackKingInCheck(board)){
									if(isFree(board[i-1][j-2]) ||
											isWhiteTaken(board[i-1][j-2])
											&& isBlackKingSafe(current, i, j, i-1, j-2, board) ){
										score+=1;
									}
								}
								else if(isFree(board[i-1][j-2]) ||
										isWhiteTaken(board[i-1][j-2])
										&& !isThreatenedByWhite(i-1, j-2, board)
										&& isBlackKingSafe(current, i, j, i-1, j-2, board) ){
									score+=1;
								}
							}
							if (i-2>=0 && j-1>=0){
								if (isBlackKingInCheck(board)){
									if(isFree(board[i-2][j-1]) || 
											isWhiteTaken(board[i-2][j-1])
											&& isBlackKingSafe(current, i, j, i-2, j-1, board) ){
										score+=1;
									}
								}
								else if(isFree(board[i-2][j-1]) || 
										isWhiteTaken(board[i-2][j-1])
										&& !isThreatenedByWhite(i-2, j-1, board)
										&& isBlackKingSafe(current, i, j, i-2, j-1, board) ){
									score+=1;
								}
							}
							break;
						case "b":
							if( i-1>=0 && j-1>=0){ //diagonale sup�rieure gauche
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x-1>=0 && y-1>=0){
										if (isWhiteTaken(board[x-1][y-1])
												&& isBlackKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y-1]) ){
											if(isBlackKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											}
											x--;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0 && y-1>=0){
										if (isWhiteTaken(board[x-1][y-1])
												&& !isThreatenedByWhite(x-1, y-1, board)
												&& isBlackKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y-1]) ){
											if(!isThreatenedByWhite(x-1, y-1, board)
													&& isBlackKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											}
											x--;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i-1>=0 && j+1<=7){ //diagonale sup�rieure droite
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x-1>=0 && y+1<=7){
										if (isWhiteTaken(board[x-1][y+1])
												&& isBlackKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y+1]) ){
											if(isBlackKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											}
											x--;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0 && y+1<=7){
										if (isWhiteTaken(board[x-1][y+1])
												&& !isThreatenedByWhite(x-1, y+1, board)
												&& isBlackKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y+1]) ){
											if(!isThreatenedByWhite(x-1, y+1, board)
													&& isBlackKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											}
											x--;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7 && j-1>=0){ //diagonale inf�rieure gauche
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x+1<=7 && y-1>=0){
										if (isWhiteTaken(board[x+1][y-1])
												&& isBlackKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y-1]) ){
											if(isBlackKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											}
											x++;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7 && y-1>=0){
										if (isWhiteTaken(board[x+1][y-1])
												&& !isThreatenedByWhite(x+1, y-1, board)
												&& isBlackKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y-1]) ){
											if(!isThreatenedByWhite(x+1, y-1, board)
													&& isBlackKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											}
											x++;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7 && j+1<=7){ //diagonale inferieure droite
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x+1<=7 && y+1<=7){
										if (isWhiteTaken(board[x+1][y+1])
												&& isBlackKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y+1]) ){
											if(isBlackKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											}
											x++;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7 && y+1<=7){
										if (isWhiteTaken(board[x+1][y+1])
												&&!isThreatenedByWhite(x+1, y+1, board)
												&& isBlackKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y+1]) ){
											if(!isThreatenedByWhite(x+1, y+1, board)
													&& isBlackKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											}
											x++;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							break;
						case "q":
							if( i-1>=0){ //verticale sup�rieure
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x-1>=0){
										if (isWhiteTaken(board[x-1][y])
												&& isBlackKingSafe(current, i, j, x-1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y])){
												if(isBlackKingSafe(current, i, j, x-1, y, board)){
													score+=1;
												}
											x--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0){
										if (isWhiteTaken(board[x-1][y])
												&& !isThreatenedByWhite(x-1, y, board)
												&& isBlackKingSafe(current, i, j, x-1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y])){
												if(!isThreatenedByWhite(x-1, y, board)
														&& isBlackKingSafe(current, i, j, x-1, y, board)){
													score+=1;
												}
											x--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7){ //verticale inf�rieure
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x+1<=7){
										if (isWhiteTaken(board[x+1][y])
												&& isBlackKingSafe(current, i, j, x+1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y])){
												if (isBlackKingSafe(current, i, j, x+1, y, board)){
													score+=1;
												}
											x++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7){
										if (isWhiteTaken(board[x+1][y])
												&& !isThreatenedByWhite(x+1, y, board)
												&& isBlackKingSafe(current, i, j, x+1, y, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y])){
												if (!isThreatenedByWhite(x+1, y, board)
														&& isBlackKingSafe(current, i, j, x+1, y, board)){
													score+=1;
												}
											x++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}	
							}
							if( j-1>=0){ //horizontale gauche
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( y-1>=0){
										if (isWhiteTaken(board[x][y-1])
												&& isBlackKingSafe(current, i, j, x, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y-1])){
												if(isBlackKingSafe(current, i, j, x, y-1, board)){
													score+=1;
												}
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( y-1>=0){
										if (isWhiteTaken(board[x][y-1])
												&& !isThreatenedByWhite(x, y-1, board)
												&& isBlackKingSafe(current, i, j, x, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y-1])){
												if( !isThreatenedByWhite(x, y-1, board)
														&& isBlackKingSafe(current, i, j, x, y-1, board)){
													score+=1;
												}
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( j+1<=7){ //horizontale droite
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while(y+1<=7){
										if (isWhiteTaken(board[x][y+1])
												&& isBlackKingSafe(current, i, j, x, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y+1]) ){
												if(isBlackKingSafe(current, i, j, x, y+1, board)){
													score+=1;
												}
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while(y+1<=7){
										if (isWhiteTaken(board[x][y+1])
												&& !isThreatenedByWhite(x, y+1, board)
												&& isBlackKingSafe(current, i, j, x, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x][y+1]) ){
												if(!isThreatenedByWhite(x, y+1, board)
														&& isBlackKingSafe(current, i, j, x, y+1, board)){
													score+=1;
												}
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i-1>=0 && j-1>=0){ //diagonale sup�rieure gauche
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x-1>=0 && y-1>=0){
										if (isWhiteTaken(board[x-1][y-1])
												&& isBlackKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y-1]) ){
											if(isBlackKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											}
											x--;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0 && y-1>=0){
										if (isWhiteTaken(board[x-1][y-1])
												&& !isThreatenedByWhite(x-1, y-1, board)
												&& isBlackKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y-1]) ){
											if(!isThreatenedByWhite(x-1, y-1, board)
													&& isBlackKingSafe(current, i, j, x-1, y-1, board)){
											score+=1;
											}
											x--;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i-1>=0 && j+1<=7){ //diagonale sup�rieure droite
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x-1>=0 && y+1<=7){
										if (isWhiteTaken(board[x-1][y+1])
												&& isBlackKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y+1]) ){
											if(isBlackKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											}
											x--;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x-1>=0 && y+1<=7){
										if (isWhiteTaken(board[x-1][y+1])
												&& !isThreatenedByWhite(x-1, y+1, board)
												&& isBlackKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x-1][y+1]) ){
											if(!isThreatenedByWhite(x-1, y+1, board)
													&& isBlackKingSafe(current, i, j, x-1, y+1, board)){
											score+=1;
											}
											x--;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7 && j-1>=0){ //diagonale inf�rieure gauche
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x+1<=7 && y-1>=0){
										if (isWhiteTaken(board[x+1][y-1])
												&& isBlackKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y-1]) ){
											if(isBlackKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											}
											x++;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7 && y-1>=0){
										if (isWhiteTaken(board[x+1][y-1])
												&& !isThreatenedByWhite(x+1, y-1, board)
												&& isBlackKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y-1]) ){
											if(!isThreatenedByWhite(x+1, y-1, board)
													&& isBlackKingSafe(current, i, j, x+1, y-1, board)){
											score+=1;
											}
											x++;
											y--;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							if( i+1<=7 && j+1<=7){ //diagonale inferieure droite
								int x=i, y=j;
								if (isBlackKingInCheck(board)){
									while( x+1<=7 && y+1<=7){
										if (isWhiteTaken(board[x+1][y+1])
												&& isBlackKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y+1]) ){
											if(isBlackKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											}
											x++;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
								else{
									while( x+1<=7 && y+1<=7){
										if (isWhiteTaken(board[x+1][y+1])
												&&!isThreatenedByWhite(x+1, y+1, board)
												&& isBlackKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											break;
										}
										else if (isFree(board[x+1][y+1]) ){
											if(!isThreatenedByWhite(x+1, y+1, board)
													&& isBlackKingSafe(current, i, j, x+1, y+1, board)){
											score+=1;
											}
											x++;
											y++;
											continue;
										}
										else{ //pi�ce alli�e/roi ennemi/bordure de l'�chiquier
											break;
										}
									}
								}
							}
							break;
						case "k":
							if (i-1>=0 && j-1>=0){
								if((isFree(board[i-1][j-1]) || 
										isWhiteTaken(board[i-1][j-1]))
										&& !isThreatenedByWhite(i-1, j-1, board)
										&& isBlackKingSafe(current, i, j, i-1, j-1, board) ){
									score+=1;
								}
							}
							if (i-1>=0){
								if((isFree(board[i-1][j]) || 
										isWhiteTaken(board[i-1][j]))
										&& !isThreatenedByWhite(i-1, j, board)
										&& isBlackKingSafe(current, i, j, i-1, j, board) ){
									score+=1;
								}
							}
							if (i-1>=0 && j+1<=7){
								if((isFree(board[i-1][j+1]) ||
										isWhiteTaken(board[i-1][j+1]))
										&& !isThreatenedByWhite(i-1, j+1, board)
										&& isBlackKingSafe(current, i, j, i-1, j+1, board) ){
									score+=1;
								}
							}
							if (j-1>=0){
								if((isFree(board[i][j-1]) ||
										isWhiteTaken(board[i][j-1]))
										&& !isThreatenedByWhite(i, j-1, board)
										&& isBlackKingSafe(current, i, j, i, j-1, board) ){
									score+=1;
								}
							}
							if (j+1<=7){
								if((isFree(board[i][j+1]) ||
										isWhiteTaken(board[i][j+1]))
										&& !isThreatenedByWhite(i, j+1, board)
										&& isBlackKingSafe(current, i, j, i, j+1, board) ){
									score+=1;
								}
							}
							if (i+1<=7 && j-1>=0){
								if((isFree(board[i+1][j-1]) ||
										isWhiteTaken(board[i+1][j-1]))
										&& !isThreatenedByWhite(i+1, j-1, board)
										&& isBlackKingSafe(current, i, j, i+1, j-1, board) ){
									score+=1;
								}
							}
							if (i+1<=7){
								if((isFree(board[i+1][j]) ||
										isWhiteTaken(board[i+1][j]))
										&& !isThreatenedByWhite(i+1, j, board)
										&& isBlackKingSafe(current, i, j, i+1, j, board) ){
									score+=1;
								}
							}
							if (i+1<=7 && j+1<=7){
								if((isFree(board[i+1][j+1]) || 
										isWhiteTaken(board[i+1][j+1]))
										&& !isThreatenedByWhite(i+1, j+1, board)
										&& isBlackKingSafe(current, i, j, i+1, j+1, board) ){
									score+=1;
								}
							}
							break;
						default:break;		
					}
				}		
			return score;
		}

	//permet de créer les valeurs des courbes et de mettre les légendes des courbes
	private XYDataset createDataset(List <Data> blackMovings,
			List <Data> whiteMovings,
			List <Data> diffMovings,
			List <Data> diffScore) {

		XYSeries series1 = new XYSeries("Black movements");
		for(Iterator iter = blackMovings.iterator(); iter.hasNext();){
			Data data = (Data) iter.next();
			series1.add(data.getTurn(),data.getValue());
		}

		XYSeries series2 = new XYSeries("White movements");
		for(Iterator iter = whiteMovings.iterator(); iter.hasNext();){
			Data data = (Data) iter.next();
			series2.add(data.getTurn(),data.getValue());
		}
		
		XYSeries series3 = new XYSeries("Movements difference");
		for(Iterator iter = diffMovings.iterator(); iter.hasNext();){
			Data data = (Data) iter.next();
			series3.add(data.getTurn(),data.getValue());
		}
		
		XYSeries series4 = new XYSeries("Pieces power difference");
		for(Iterator iter = diffScore.iterator(); iter.hasNext();){
			Data data = (Data) iter.next();
			series4.add(data.getTurn(),data.getValue());
		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		dataset.addSeries(series4);

		return dataset;

	}

	//permet de construire le graphique et de légender les abssices et les ordonnées
	private JFreeChart createChart(XYDataset dataset, String name) {
		return ChartFactory.createXYLineChart(name, 
				"Turns", "Amount", dataset, PlotOrientation.VERTICAL, 
				true, true, false);
	}

	//fonction MAIN
	//permet de lancer le programme
	/*
	 * d'abord on analyse le fichier PGN
	 * ensuite on extrait les informations qui nous intéresse
	 * à chaque coup, on met à jour la matrice et on calcule les valeurs de mouvements/puissance
	 * on stocke ses valeurs dans des listes que le graphique prendra en paramètre
	 * ensuite on créé le graphique
	 * et on peut le visualiser
	 */
	public static void main(String[] args) {
		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			
			List games = new LinkedList();
			List<String> plays = new ArrayList<String>();

			String line;
			String[] cutter;
			String event="";
			String game="";
			String year="";
			String white="";
			String black="";
			String winner="";
			int i=0;

			while ((line = br.readLine()) != null) {
				i++;
				if(line.contains("[Event ")){
					game = ""; //vider la chaîne qui contient la liste des coups
					cutter = line.split("\"");
					event = cutter[1];
				}
				if (line.contains("[Date ")){ 
					cutter = line.split("\"");
					String date = cutter[1];			
					String date1 = date.substring(0, 4);
					year = date1;
				}
				if (line.contains("Result")){
					cutter = line.split("\"");
					String result = cutter[1];
					if (result.equals("1-0")){
						winner = "white";
					}
					else if (result.equals("0-1")){
						winner = "black";
					}
					else{
						winner = "draw";
					}
				}
				if (line.contains("White ")){
					cutter = line.split("\"");
					String w_player = cutter[1];
					white = w_player;
				}
				if (line.contains("Black ")){
					cutter = line.split("\"");
					String b_player = cutter[1];
					black = b_player;
				}
				
				if(!line.contains("\"]")){		
					game += line;
					game += " ";
					if (line.contains("-") && (!line.contains("O-O")) ){ //Fin de la FEN
						plays=return_list_of_plays(game);
						String san="";
						List <Data> blackMovings = new ArrayList<Data>();
						List <Data> whiteMovings = new ArrayList<Data>();
						List <Data> differenceMovings = new ArrayList<Data>();
						List <Data> differencePieces = new ArrayList<Data>();
						int white_score=39;
						int black_score=39;
						int score_diff=0;
						int black_moving=20;
						int white_moving=20;
						int moving_diff=0;
						int turn=0;
						String[][]board = null;
						blackMovings.add(new Data(turn,black_moving));
						whiteMovings.add(new Data(turn,white_moving));
						differenceMovings.add(new Data(turn,moving_diff));
						differencePieces.add(new Data(turn,score_diff));
						for(Iterator iter = plays.iterator(); iter.hasNext();){
							turn++;
							san += (String) iter.next();
							san +=" ";
							String fen = san_to_fen(san);
							board = fen_to_board( fen );		
							
							white_score = calculate_white_pieces_value(board);
							black_score = calculate_black_pieces_value(board);
							score_diff = piece_score_difference(white_score, black_score, winner);
							black_moving = calculate_black_movings(board);
							white_moving = calculate_white_movings(board);
							moving_diff= moving_score_difference(white_moving, black_moving, winner);
							
							blackMovings.add(new Data(turn,black_moving));
							whiteMovings.add(new Data(turn,white_moving));
							differenceMovings.add(new Data(turn,moving_diff));
							differencePieces.add(new Data(turn,score_diff));
						}				
						
						LineChartDemo demo = new LineChartDemo(event,blackMovings, 
								whiteMovings, differenceMovings, differencePieces, event);
						demo.pack();
						RefineryUtilities.centerFrameOnScreen(demo);
						demo.setVisible(true);
						
						games.add(new Game(year,white,black,winner, score_diff, black_moving,
								white_moving, game, board));	

					}
				}	

			}
			
			for(Iterator iter = games.iterator(); iter.hasNext();){
				Game game1 = (Game) iter.next();
				System.out.println(game1.toString());
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	

	}

}