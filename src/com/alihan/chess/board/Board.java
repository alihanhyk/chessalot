package com.alihan.chess.board;

import java.util.LinkedList;
import java.util.List;

import com.alihan.chess.board.Piece.PieceColor;
import com.alihan.chess.board.Piece.PieceType;

public class Board
{
	private Piece[][] pieceArray;
	public Piece[][] getPieceArray() { return pieceArray; }
	
	private PieceColor colorToMove;
	public PieceColor getColorToMove() { return colorToMove; }
	
	private List<Move> moveList;
	public List<Move> getMoveList() { return moveList; }
	
	private boolean whiteKingCastle;
	private boolean whiteQueenCastle;
	private boolean blackKingCastle;
	private boolean blackQueenCastle;
	
	private boolean whiteKingCastle_lock;
	private boolean whiteQueenCastle_lock;
	private boolean blackKingCastle_lock;
	private boolean blackQueenCastle_lock;
	
	private boolean[] whiteEnPassant;
	private boolean[] blackEnPassant;
	
	public int getCastleIndex() //16
	{
		int castle = 0;
		if(whiteKingCastle) castle += 1;
		if(whiteQueenCastle) castle += 2;
		if(blackKingCastle) castle += 4;
		if(blackQueenCastle) castle += 8;
		return castle;
	}
	
	public int getEnPassantIndex(int file) //4
	{
		int enPassant = 0;
		if(whiteEnPassant[file]) enPassant += 1;
		if(blackEnPassant[file]) enPassant += 2;
		return enPassant;
	}
	
	public Board()
	{
		pieceArray = new Piece[8][8];
		
		pieceArray[0][0] = Piece.WHITE_ROOK;
		pieceArray[1][0] = Piece.WHITE_KNIGHT;
		pieceArray[2][0] = Piece.WHITE_BISHOP;
		pieceArray[3][0] = Piece.WHITE_QUEEN;
		pieceArray[4][0] = Piece.WHITE_KING;
		pieceArray[5][0] = Piece.WHITE_BISHOP;
		pieceArray[6][0] = Piece.WHITE_KNIGHT;
		pieceArray[7][0] = Piece.WHITE_ROOK;
		for(int j = 0; j < 8; j++) pieceArray[j][1] = Piece.WHITE_PAWN;
		
		pieceArray[0][7] = Piece.BLACK_ROOK;
		pieceArray[1][7] = Piece.BLACK_KNIGHT;
		pieceArray[2][7] = Piece.BLACK_BISHOP;
		pieceArray[3][7] = Piece.BLACK_QUEEN;
		pieceArray[4][7] = Piece.BLACK_KING;
		pieceArray[5][7] = Piece.BLACK_BISHOP;
		pieceArray[6][7] = Piece.BLACK_KNIGHT;
		pieceArray[7][7] = Piece.BLACK_ROOK;
		for(int j = 0; j < 8; j++) pieceArray[j][6] = Piece.BLACK_PAWN;
		
		colorToMove = PieceColor.WHITE;
		
		whiteKingCastle = false;
		whiteQueenCastle = false;
		blackKingCastle = false;
		blackQueenCastle = false;
		
		whiteKingCastle_lock = false;
		whiteQueenCastle_lock = false;
		blackKingCastle_lock = false;
		blackQueenCastle_lock = false;
		
		whiteEnPassant = new boolean[8];
		for(int i = 0; i < 8; i++) whiteEnPassant[i] = false;
		
		blackEnPassant = new boolean[8];
		for(int i = 0; i < 8; i++) blackEnPassant[i] = false;
	}
	
	public Board clone()
	{
		Board board = new Board();
		
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
			board.pieceArray[i][j] = this.pieceArray[i][j];
		
		board.colorToMove = this.colorToMove;
		
		board.whiteKingCastle = this.whiteKingCastle;
		board.whiteQueenCastle = this.whiteQueenCastle;
		board.blackKingCastle = this.blackKingCastle;
		board.blackQueenCastle = this.blackQueenCastle;
		
		board.whiteKingCastle_lock = this.whiteKingCastle_lock;
		board.whiteQueenCastle_lock = this.whiteQueenCastle_lock;
		board.blackKingCastle_lock = this.blackKingCastle_lock;
		board.blackQueenCastle_lock = this.blackQueenCastle_lock;
		
		for(int i = 0; i < 8; i++) board.whiteEnPassant[i] = this.whiteEnPassant[i];
		for(int i = 0; i < 8; i++) board.blackEnPassant[i] = this.blackEnPassant[i];
		
		return board;
	}
	
	//does not consider en passant moves
	public boolean isAttackedBy(int posI, int posJ, PieceColor color)
	{		
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
		if(pieceArray[i][j] != null && pieceArray[i][j].getColor() == color)
		{
			if(pieceArray[i][j] == Piece.WHITE_PAWN)
			{
				if(i != 0 && i - 1 == posI && j + 1 == posJ) return true;
				if(i != 7 && i + 1 == posI && j + 1 == posJ) return true;
			}
			else if(pieceArray[i][j] == Piece.BLACK_PAWN)
			{
				if(i != 0 && i - 1 == posI && j - 1 == posJ) return true;
				if(i != 7 && i + 1 == posI && j - 1 == posJ) return true;
			}
			else
			{
				for(int dir = 0; dir < pieceArray[i][j].getOffsetLength(); dir++)
				{
					int i1 = i;
					int j1 = j;
					
					while(true)
					{
						i1 += pieceArray[i][j].getFileOffset(dir);
						j1 += pieceArray[i][j].getRankOffset(dir);
						
						if(i1 == posI && j1 == posJ) return true;
						
						if(i1 < 0 || i1 >= 8) break;
						if(j1 < 0 || j1 >= 8) break;
						
						if(pieceArray[i1][j1] != null) break;
						if(!pieceArray[i][j].isSlidable()) break;
					}
				}
			}
		}
			
		return false;
	}
	
	public boolean isInCheck(PieceColor color)
	{
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
		{
			if(color == PieceColor.WHITE && pieceArray[i][j] == Piece.WHITE_KING)
				return isAttackedBy(i, j, PieceColor.BLACK);
			
			if(color == PieceColor.BLACK && pieceArray[i][j] == Piece.BLACK_KING)
				return isAttackedBy(i, j, PieceColor.WHITE);
		}
		
		return false;
	}
	
	public void generateMoveList() { generateMoveList(false); }
	public void generateMoveList(boolean capturesOnly)
	{
		if(moveList == null) moveList = new LinkedList<Move>();
		moveList.clear();
		
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
		if(pieceArray[i][j] != null && pieceArray[i][j].getColor() == colorToMove)
		{
			if(pieceArray[i][j] == Piece.WHITE_PAWN)
			{
				if(i != 0 && pieceArray[i - 1][j + 1] != null && pieceArray[i - 1][j + 1].getColor() == PieceColor.BLACK)
					addPawnMove(i, j, i - 1, j + 1);
				
				if(i != 7 && pieceArray[i + 1][j + 1] != null && pieceArray[i + 1][j + 1].getColor() == PieceColor.BLACK)
					addPawnMove(i, j, i + 1, j + 1);
				
				if(!capturesOnly && pieceArray[i][j + 1] == null)
				{
					addPawnMove(i, j, i, j + 1);
					if(j == 1 && pieceArray[i][j + 2] == null)
						addMove(i, j, i, j + 2);
				}
				
				if(i != 0 && j == 4 && whiteEnPassant[i - 1]) addMove(i, j, i - 1, j + 1, 2);
				if(i != 7 && j == 4 && whiteEnPassant[i + 1]) addMove(i, j, i + 1, j + 1, 2);
				
			}
			else if(pieceArray[i][j] == Piece.BLACK_PAWN)
			{
				if(i != 0 && pieceArray[i - 1][j - 1] != null && pieceArray[i - 1][j - 1].getColor() == PieceColor.WHITE)
					addPawnMove(i, j, i - 1, j - 1);
				
				if(i != 7 && pieceArray[i + 1][j - 1] != null && pieceArray[i + 1][j - 1].getColor() == PieceColor.WHITE)
					addPawnMove(i, j, i + 1, j - 1);
				
				if(!capturesOnly && pieceArray[i][j - 1] == null)
				{
					addPawnMove(i, j, i, j - 1);
					if(j == 6 && pieceArray[i][j - 2] == null)
						addMove(i, j, i, j - 2);
				}
				
				if(i != 0 && j == 3 && blackEnPassant[i - 1]) addMove(i, j, i - 1, j - 1, 2);
				if(i != 7 && j == 3 && blackEnPassant[i + 1]) addMove(i, j, i + 1, j - 1, 2);
			}
			else
			{
				for(int dir = 0; dir < pieceArray[i][j].getOffsetLength(); dir++)
				{
					int i1 = i;
					int j1 = j;
					
					while(true)
					{
						i1 += pieceArray[i][j].getFileOffset(dir);
						j1 += pieceArray[i][j].getRankOffset(dir);
												
						if(i1 < 0 || i1 >= 8) break;
						if(j1 < 0 || j1 >= 8) break;
						
						if(pieceArray[i1][j1] != null)
						{
							if(pieceArray[i1][j1].getColor() != colorToMove)
								addMove(i, j, i1, j1);
							break;
						}
						
						if(!capturesOnly) addMove(i, j, i1, j1);
						if(!pieceArray[i][j].isSlidable()) break;
					}
				}
			}
		}
		
		if(!capturesOnly)
		{
			if(colorToMove == PieceColor.WHITE)
			{
				if(whiteKingCastle) addMove(4, 0, 6, 0, 1);
				if(whiteQueenCastle) addMove(4, 0, 2, 0, 1);
			}
			else
			{
				if(blackKingCastle) addMove(4, 7, 6, 7, 1);
				if(blackQueenCastle) addMove(4, 7, 2, 7, 1);
			}
		}
	}
	
	private void addMove(int i0, int j0, int i, int j) { addMove(i0, j0, i, j, 0); }
	private void addMove(int i0, int j0, int i, int j, int special)
	{
		Move move = new Move(i0, j0, i, j, special);
		
		if(pieceArray[i][j] != null) //capture
			move.setOrderingScore(1000000 + 10 * pieceArray[i][j].toIndex() / 2 - pieceArray[i0][j0].toIndex() / 2);
		
		moveList.add(move);
	}
	
	private void addPawnMove(int i0, int j0, int i, int j)
	{
		if(j == 0 || j == 7)
		{
			addMove(i0, j0, i, j, 10);
			addMove(i0, j0, i, j, 11);
			addMove(i0, j0, i, j, 12);
			addMove(i0, j0, i, j, 13);
		}
		else addMove(i0, j0, i, j);
	}
	
	public void makeMove(Move move)
	{
		pieceArray[move.i][move.j] = pieceArray[move.i0][move.j0];
		pieceArray[move.i0][move.j0] = null;
		
		if(move.isCastle()) makeCastleMove(move);
		if(move.isEnPassant()) makeEnPassantMove(move);
		
		if(move.isPromote())
			pieceArray[move.i][move.j] = Piece.getPiece(colorToMove, move.getPromoteType());
		
		if(colorToMove == PieceColor.WHITE) colorToMove = PieceColor.BLACK;
		else colorToMove = PieceColor.WHITE;
		
		updateCastleFlag(move);
		updateEnPassantFlag(move);
	}
	
	private void makeCastleMove(Move move)
	{
		if(move.i == 6 && move.j == 0)
		{
			pieceArray[5][0] = pieceArray[7][0];
			pieceArray[7][0] = null;
		}
		else if(move.i == 2 && move.j == 0)
		{
			pieceArray[3][0] = pieceArray[0][0];
			pieceArray[0][0] = null;
		}
		else if(move.i == 6 && move.j == 7)
		{
			pieceArray[5][7] = pieceArray[7][7];
			pieceArray[7][7] = null;
		}
		else if(move.i == 2 && move.j == 7)
		{
			pieceArray[3][7] = pieceArray[0][7];
			pieceArray[0][7] = null;
		}
	}
	
	private void makeEnPassantMove(Move move)
	{
		if(colorToMove == PieceColor.WHITE) pieceArray[move.i][move.j - 1] = null;
		else pieceArray[move.i][move.j + 1] = null;
	}
	
	private void updateCastleFlag(Move move)
	{
		if((move.i0 == 4 && move.j0 == 0) || (move.i == 4 && move.j == 0))
		{
			whiteKingCastle_lock = true;
			whiteQueenCastle_lock = true;
		}
		else if((move.i0 == 4 && move.j0 == 7) || (move.i == 4 && move.j == 7))
		{
			blackKingCastle_lock = true;
			blackQueenCastle_lock = true;
		}
		else if((move.i0 == 7 && move.j0 == 0) || (move.i == 7 && move.j == 0)) whiteKingCastle_lock = true;
		else if((move.i0 == 0 && move.j0 == 0) || (move.i == 0 && move.j == 0)) whiteQueenCastle_lock = true;
		else if((move.i0 == 7 && move.j0 == 7) || (move.i == 7 && move.j == 7)) blackKingCastle_lock = true;
		else if((move.i0 == 0 && move.j0 == 7) || (move.i == 0 && move.j == 7)) blackQueenCastle_lock = true;
		
		
		whiteKingCastle = pieceArray[4][0] == Piece.WHITE_KING && pieceArray[7][0] == Piece.WHITE_ROOK && !whiteKingCastle_lock;
		whiteQueenCastle = pieceArray[4][0] == Piece.WHITE_KING && pieceArray[0][0] == Piece.WHITE_ROOK && !whiteQueenCastle_lock;
		
		if(whiteKingCastle || whiteQueenCastle)
		if(isInCheck(PieceColor.WHITE))
		{
			whiteKingCastle = false;
			whiteQueenCastle = false;
		}
		
		if(whiteKingCastle)
		if(pieceArray[5][0] != null || pieceArray[6][0] != null || 
				isAttackedBy(5, 0, PieceColor.BLACK) || isAttackedBy(6, 0, PieceColor.BLACK))
			whiteKingCastle = false;
		
		if(whiteQueenCastle)
		if(pieceArray[1][0] != null || pieceArray[2][0] != null || pieceArray[3][0] != null ||
				isAttackedBy(1, 0, PieceColor.BLACK) || isAttackedBy(2, 0, PieceColor.BLACK) || isAttackedBy(3, 0, PieceColor.BLACK))
			whiteQueenCastle = false;
		
		blackKingCastle = pieceArray[4][7] == Piece.BLACK_KING && pieceArray[7][7] == Piece.BLACK_ROOK && !blackKingCastle_lock;
		blackQueenCastle = pieceArray[4][7] == Piece.BLACK_KING && pieceArray[0][7] == Piece.BLACK_ROOK  && !blackQueenCastle_lock;;
		
		if(blackKingCastle || blackQueenCastle)
		if(isInCheck(PieceColor.BLACK))
		{
			blackKingCastle = false;
			blackQueenCastle = false;
		}
		
		if(blackKingCastle)
		if(pieceArray[5][7] != null || pieceArray[6][7] != null ||
				isAttackedBy(5, 7, PieceColor.WHITE) || isAttackedBy(6, 7, PieceColor.WHITE))
			blackKingCastle = false;
		
		if(blackQueenCastle)
		if(pieceArray[1][7] != null || pieceArray[2][7] != null || pieceArray[3][7] != null ||
				isAttackedBy(1, 7, PieceColor.WHITE) || isAttackedBy(2, 7, PieceColor.WHITE) || isAttackedBy(3, 7, PieceColor.WHITE))
			blackQueenCastle = false;
	}
	
	private void updateEnPassantFlag(Move move)
	{
		for(int i = 0; i < 8; i++) whiteEnPassant[i] = false;
		for(int i = 0; i < 8; i++) blackEnPassant[i] = false;
		
		if(pieceArray[move.i][move.j].getType() == PieceType.PAWN)
		{
			if(move.j == move.j0 + 2)
			{
				//WHITE_PAWN moved
				blackEnPassant[move.i] = blackEnPassant[move.i] || (move.i != 0 && pieceArray[move.i - 1][move.j] == Piece.BLACK_PAWN);
				blackEnPassant[move.i] = blackEnPassant[move.i] || (move.i != 7 && pieceArray[move.i + 1][move.j] == Piece.BLACK_PAWN);
				
			}
			else if(move.j == move.j0 - 2)
			{
				//BLACK_PAWN moved
				whiteEnPassant[move.i] = whiteEnPassant[move.i] || (move.i != 0 && pieceArray[move.i - 1][move.j] == Piece.WHITE_PAWN);
				whiteEnPassant[move.i] = whiteEnPassant[move.i] || (move.i != 7 && pieceArray[move.i + 1][move.j] == Piece.WHITE_PAWN);
			}
		}
	}
	
	
	public String toString()
	{
		String string = "";
		
		for(int j = 7; j >= 0; j--)
		{
			string += (j + 1) + "  ";
			for(int i = 0; i < 8; i++)
			{
				if(pieceArray[i][j] == null) string += "-";
				else string += pieceArray[i][j].toString();
				string += " ";
			}
			string += "\n";
		}
		
		string += "\n";
		string += "   A B C D E F G H ";
		
		return string;
	}
}
