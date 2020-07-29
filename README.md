# chessAnalysis
Extraction of spatio-sequential phenomena from chess databases
<br><br>

I used ChessLib (https://github.com/bhlangonijr/chesslib) library to convert PGN games to FEN format

"files" contains some PGN games used to test the programs

"src" contains 3 folders :
* "pgn" contains my main work (chess analysis)
* "com" and "resources" contains the ChessLib library
	
"pgn" classes explanation :
* LineChartDemo : demo class, statistical analysis of several game features (amount of safe moves, power of pieces remaining...) accross time to see if it has an impact on the outcome
* Game : game information (date, player informations, etc)
* Piece : piece information (type, board coordinates, color, etc)
* Data : features value on turn X (used for graphic)
* GameParser : replays game to compute all features
