package com.alihan.chess.search;

import com.alihan.chess.board.Board;
import com.alihan.chess.board.Piece;
import com.alihan.chess.board.Piece.PieceColor;

public class Evaluator
{
	private static final int ROOK_PAWN_RANK = 20;
	private static final int ROOK_OPEN = 15;
	private static final int ROOK_SEMI_OPEN = 10;
	
	private static final int PAWN_PASSED = 20;
	private static final int PAWN_BACKWARD = -8;
	private static final int PAWN_DOUBLED = -10;
	private static final int PAWN_ISOLATED = -20;
	
	private static final int QUEEN_VALUE = 900;
	private static final int ROOK_VALUE = 500;
	private static final int BISHOP_VALUE = 300;
	private static final int KNIGHT_VALUE = 300;
	private static final int PAWN_VALUE = 100;
	
	private static final int[][] BISHOP_POSITION = 
	{
		{-10, -10, -10, -10, -10, -10, -10, -10},
		{-10,   0,   0,   0,   0,   0,   0, -10},
		{-10,   0,   5,   5,   5,   5,   0, -10},
		{-10,   0,   5,  10,  10,   5,   0, -10},
		{-10,   0,   5,  10,  10,   5,   0, -10},
		{-10,   0,   5,   5,   5,   5,   0, -10},
		{-10,   0,   0,   0,   0,   0,   0, -10},
		{-10, -10, -20, -10, -10, -20, -10, -10}
	};
	
	private static final int[][] KNIGHT_POSITION =
	{
		{-10, -10, -10, -10, -10, -10, -10, -10},
		{-10,   0,   0,   0,   0,   0,   0, -10},
		{-10,   0,   5,   5,   5,   5,   0, -10},
		{-10,   0,   5,  10,  10,   5,   0, -10},
		{-10,   0,   5,  10,  10,   5,   0, -10},
		{-10,   0,   5,   5,   5,   5,   0, -10},
		{-10,   0,   0,   0,   0,   0,   0, -10},
		{-10, -30, -10, -10, -10, -10, -30, -10}
	};
	
	private static final int[][] PAWN_POSITION = 
	{
		{  0,   0,   1,   2,   3,   4,   5,   0},
		{  0,   0,   2,   4,   6,   8,  10,   0},
		{  0,   0,   3,   6,   9,  12,  15,   0},
		{  0, -40, -10,   8,  12,  16,  20,   0},
		{  0, -40, -10,   8,  12,  16,  20,   0},
		{  0,   0,   3,   6,   9,  12,  15,   0},
		{  0,   0,   2,   4,   6,   8,  10,   0},
		{  0,   0,   1,   2,   3,   4,   5,   0}
	};
	
	private static final int[][] KING_POSITION = 
	{
		{  0, -20, -40, -40, -40, -40, -40, -40},
		{ 20, -20, -40, -40, -40, -40, -40, -40},
		{ 40, -20, -40, -40, -40, -40, -40, -40},
		{-20, -20, -40, -40, -40, -40, -40, -40},
		{  0, -20, -40, -40, -40, -40, -40, -40},
		{-20, -20, -40, -40, -40, -40, -40, -40},
		{ 40, -20, -40, -40, -40, -40, -40, -40},
		{ 20, -20, -40, -40, -40, -40, -40, -40}
	};

	private static final int[][] KING_POSITION_END =
	{
		{  0,  10,  20,  30,  30,  20,  10,   0},
		{ 10,  20,  30,  40,  40,  30,  20,  10},
		{ 20,  30,  40,  50,  50,  40,  30,  20},
		{ 30,  40,  50,  60,  60,  50,  40,  30},
		{ 30,  40,  50,  60,  60,  50,  40,  30},
		{ 20,  30,  40,  50,  50,  40,  30,  20},
		{ 10,  20,  30,  40,  40,  30,  20,  10},
		{  0,  10,  20,  30,  30,  20,  10,   0}
	};
	
	public Evaluator()
	{
		
	}
	
	public int evaluate(Board board) { return evaluate(board, true); }
	public int evaluate(Board board, boolean absolute)
	{
		Piece[][] pieceArray = board.getPieceArray();
		
		int whiteMaterial = 0;
		int blackMaterial = 0;
		int whiteScore = 0;
		int blackScore = 0;
		
		int[] whiteRankArray = new int[8]; //least advanced
		for(int i = 0; i < 8; i++) whiteRankArray[i] = 7; 
		
		int[] blackRankArray = new int[8]; //least advanced
		for(int i = 0; i < 8; i++) blackRankArray[i] = 7;
		
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
		{
			if(pieceArray[i][j] == Piece.WHITE_PAWN && j < whiteRankArray[i])
				whiteRankArray[i] = j; 
			else if(pieceArray[i][j] == Piece.BLACK_PAWN && 7 - j < blackRankArray[i])
				blackRankArray[i] = 7 - j; 
		}
		
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
		{
			if(pieceArray[i][j] == Piece.WHITE_PAWN) whiteMaterial += PAWN_VALUE;
			else if(pieceArray[i][j] == Piece.WHITE_ROOK) whiteMaterial += ROOK_VALUE;
			else if(pieceArray[i][j] == Piece.WHITE_BISHOP) whiteMaterial += BISHOP_VALUE;
			else if(pieceArray[i][j] == Piece.WHITE_KNIGHT) whiteMaterial += KNIGHT_VALUE;
			else if(pieceArray[i][j] == Piece.WHITE_QUEEN) whiteMaterial += QUEEN_VALUE;
			else if(pieceArray[i][j] == Piece.BLACK_PAWN) blackMaterial += PAWN_VALUE;
			else if(pieceArray[i][j] == Piece.BLACK_ROOK) blackMaterial += ROOK_VALUE;
			else if(pieceArray[i][j] == Piece.BLACK_BISHOP) blackMaterial += BISHOP_VALUE;
			else if(pieceArray[i][j] == Piece.BLACK_KNIGHT) blackMaterial += KNIGHT_VALUE;
			else if(pieceArray[i][j] == Piece.BLACK_QUEEN) blackMaterial += QUEEN_VALUE;
			
			//
			//else if(pieceArray[i][j] == Piece.WHITE_KING) whiteMaterial += 1000000;
			//else if(pieceArray[i][j] == Piece.BLACK_KING) blackMaterial += 1000000;
		}
		
		whiteScore += whiteMaterial;
		blackScore += blackMaterial;
		
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
		{
			if(pieceArray[i][j] == Piece.WHITE_PAWN)
			{
				whiteScore += PAWN_POSITION[i][j];
				
				if(isDoubledPawn(whiteRankArray, i, j)) whiteScore += PAWN_DOUBLED;
				
				if(isIsolatedPawn(whiteRankArray, i, j)) whiteScore += PAWN_ISOLATED;
				else if(isBackwardPawn(whiteRankArray, i, j)) whiteScore += PAWN_BACKWARD;
				
				if(isPassedPawn(blackRankArray, i, j)) whiteScore += j * PAWN_PASSED;
			}
			else if(pieceArray[i][j] == Piece.WHITE_KING)
			{
				if(blackMaterial <= 1200) whiteScore += KING_POSITION_END[i][j];
				else
				{
					int kingSafety = 0;
					kingSafety += KING_POSITION[i][j];
					
					if(i < 3) //castled queen side
					{
						kingSafety += kingPawnPenalty(whiteRankArray, blackRankArray, 0);
						kingSafety += kingPawnPenalty(whiteRankArray, blackRankArray, 1);
						kingSafety += kingPawnPenalty(whiteRankArray, blackRankArray, 2);
					}
					else if(i > 4) //castled king side
					{
						kingSafety += kingPawnPenalty(whiteRankArray, blackRankArray, 5);
						kingSafety += kingPawnPenalty(whiteRankArray, blackRankArray, 6);
						kingSafety += kingPawnPenalty(whiteRankArray, blackRankArray, 7);
					}
					else
					{
						if(whiteRankArray[i - 1] == 7 && blackRankArray[i - 1] == 7) kingSafety -= 10;
						if(whiteRankArray[i] == 7 && blackRankArray[i] == 7) kingSafety -= 10;
						if(whiteRankArray[i + 1] == 7 && blackRankArray[i+ 1] == 7) kingSafety -= 10;
					}
					
					whiteScore += kingSafety * blackMaterial / 3100;
				}
			}
			else if(pieceArray[i][j] == Piece.WHITE_ROOK)
			{
				if(whiteRankArray[i] == 7)
				{
					if(blackRankArray[i] == 7) whiteScore += ROOK_OPEN;
					else whiteScore += ROOK_SEMI_OPEN;
				}
				
				if(j == 6) whiteScore += ROOK_PAWN_RANK;
			}
			else if(pieceArray[i][j] == Piece.WHITE_BISHOP) whiteScore += BISHOP_POSITION[i][j];
			else if(pieceArray[i][j] == Piece.WHITE_KNIGHT) whiteScore += KNIGHT_POSITION[i][j];
			
			else if(pieceArray[i][j] == Piece.BLACK_PAWN)
			{
				blackScore += PAWN_POSITION[i][7 - j];
				
				if(isDoubledPawn(blackRankArray, i, 7 - j)) blackScore += PAWN_DOUBLED;
				
				if(isIsolatedPawn(blackRankArray, i, 7 - j)) blackScore += PAWN_ISOLATED;
				else if(isBackwardPawn(blackRankArray, i, 7 - j)) blackScore += PAWN_BACKWARD;
				
				if(isPassedPawn(whiteRankArray, i, 7 - j)) blackScore += (7 - j) * PAWN_PASSED;
			}
			else if(pieceArray[i][j] == Piece.BLACK_KING)
			{
				if(whiteMaterial <= 1200) blackScore += KING_POSITION_END[i][7 - j];
				else
				{
					int kingSafety = 0;
					kingSafety += KING_POSITION[i][7 - j];
					
					if(i < 3) //castled queen side
					{
						kingSafety += kingPawnPenalty(blackRankArray, whiteRankArray, 0);
						kingSafety += kingPawnPenalty(blackRankArray, whiteRankArray, 1);
						kingSafety += kingPawnPenalty(blackRankArray, whiteRankArray, 2);
					}
					else if(i > 4) //castled king side
					{
						kingSafety += kingPawnPenalty(blackRankArray, whiteRankArray, 5);
						kingSafety += kingPawnPenalty(blackRankArray, whiteRankArray, 6);
						kingSafety += kingPawnPenalty(blackRankArray, whiteRankArray, 7);
					}
					else
					{
						if(whiteRankArray[i - 1] == 7 && blackRankArray[i - 1] == 7) kingSafety -= 10;
						if(whiteRankArray[i] == 7 && blackRankArray[i] == 7) kingSafety -= 10;
						if(whiteRankArray[i + 1] == 7 && blackRankArray[i+ 1] == 7) kingSafety -= 10;
					}
					
					blackScore += kingSafety * whiteMaterial / 3100;
				}
			}
			else if(pieceArray[i][j] == Piece.BLACK_ROOK)
			{
				if(blackRankArray[i] == 7)
				{
					if(whiteRankArray[i] == 7) blackScore += ROOK_OPEN;
					else blackScore += ROOK_SEMI_OPEN;
				}
				
				if(7 - j == 6) blackScore += ROOK_PAWN_RANK;
			}
			else if(pieceArray[i][j] == Piece.BLACK_BISHOP) blackScore += BISHOP_POSITION[i][7 - j];
			else if(pieceArray[i][j] == Piece.BLACK_KNIGHT) blackScore += KNIGHT_POSITION[i][7 - j];
			
		}
		
		if(!absolute && board.getColorToMove() == PieceColor.BLACK)
			return blackScore - whiteScore;
		
		return whiteScore - blackScore;
	}
		
	private boolean isDoubledPawn(int[] rankArray, int i, int j)
	{
		return rankArray[i] < j;
	}
	
	private boolean isIsolatedPawn(int[] rankArray, int i, int j)
	{
		boolean left = true;
		boolean right = true;
		
		if(i != 0) left = rankArray[i - 1] == 7;
		if(i != 7) right = rankArray[i + 1] == 7;
		
		return left && right;
	}
	
	private boolean isBackwardPawn(int[] rankArray, int i, int j)
	{
		boolean left = true;
		boolean right = true;
		
		if(i != 0) left = rankArray[i - 1] > j;
		if(i != 7) right = rankArray[i + 1] > j;

		return left && right;
	}
	
	private boolean isPassedPawn(int[] opponentRankArray, int i, int j)
	{				
		boolean left = true;
		boolean right = true;
		
		if(i != 0) left = 7 - opponentRankArray[i - 1] <= j;
		if(i != 7) right = 7 - opponentRankArray[i + 1] <= j;
				
		return left && right && 7 - opponentRankArray[i] <= j;
	}
	
	private int kingPawnPenalty(int[] rankArray, int[] opponentRankArray, int i)
	{
		int penalty = 0;
		
		if(rankArray[i] == 1) penalty -= 0;
		else if(rankArray[i] == 2) penalty -= 10;
		else if(rankArray[i] < 7) penalty -= 20;
		else penalty += -25;
		
		if(rankArray[i] == 7) penalty -= 15;
		else if(rankArray[i] == 5) penalty -= 10;
		else if(rankArray[i] == 4) penalty -= 5;
		
		return penalty;
	}
}
