package com.alihan.chess.board;

import com.alihan.chess.board.Piece.PieceType;

public class Move implements Comparable<Move>
{
	public int i0;
	public int j0;
	public int i;
	public int j;
	
	private boolean castle;
	private boolean enPassant;
	private PieceType promote;
	
	public boolean isCastle() { return castle; }
	public boolean isEnPassant() { return enPassant; }
	public boolean isPromote() { return promote != null; }
	public PieceType getPromoteType() { return promote; }
	
	private int orderingScore = 0;
	public void setOrderingScore(int score) { orderingScore = score; }
	public void addOrderingScore(int score) { orderingScore += score; }
	public int compareTo(Move other) { return orderingScore - other.orderingScore; }
	
	public Move(int i0, int j0, int i, int j)
	{
		this.i0 = i0;
		this.j0 = j0;
		this.i = i;
		this.j = j;
		
		castle = false;
		enPassant = false;
		promote = null;
	}
	
	public Move(int i0, int j0, int i, int j, int special)
	{
		this(i0, j0, i, j);
		
		if(special == 1) castle = true;
		else if(special == 2) enPassant = true;
		else if(special == 10) promote = PieceType.QUEEN;
		else if(special == 11) promote = PieceType.ROOK;
		else if(special == 12) promote = PieceType.BISHOP;
		else if(special == 13) promote = PieceType.KNIGHT;
	}
	
	public Move(String string)
	{
		i0 = toColumn(string.charAt(0));
		j0 = toRow(string.charAt(1));
		i = toColumn(string.charAt(2));
		j = toRow(string.charAt(3));
		
		castle = false;
		promote = null;
		
		if(string.length() > 4 && string.charAt(4) == ',')
		{
			if(string.charAt(5) == 'c') castle = true;
			else if(string.charAt(5) == 'p')
			{
				if(string.charAt(6) == 'q') promote = PieceType.QUEEN;
				else if(string.charAt(6) == 'r') promote = PieceType.ROOK;
				else if(string.charAt(6) == 'b') promote = PieceType.BISHOP;
				else if(string.charAt(6) == 'n') promote = PieceType.KNIGHT;
			}
			else if(string.substring(5, 7).equals("ep")) enPassant = true;
				
		}
	}
	
	public String toString()
	{
		String string = "";
		
		string += toFile(i0);
		string += toRank(j0);
		string += toFile(i);
		string += toRank(j);
		
		if(castle) string += ",c";
		else if(enPassant) string += ",ep";
		else if(promote != null)
		{
			string += ",p";
			if(promote ==  PieceType.QUEEN) string += "q";
			if(promote ==  PieceType.ROOK) string += "r";
			if(promote ==  PieceType.BISHOP) string += "b";
			if(promote ==  PieceType.KNIGHT) string += "n";
		}
		
		return string;
	}
	
	private static final char[] fileLUT = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
	private static final char[] rankLUT = { '1', '2', '3', '4', '5', '6', '7', '8' };
	
	private static char toFile(int column)
	{
		if(0 <= column && column < 8)
			return fileLUT[column];
		return '-';
	}
	
	private static int toColumn(char file)
	{
		for(int i = 0; i < 8; i++)
			if(fileLUT[i] == file) return i;
		return -1;
	}
	
	private static char toRank(int row)
	{
		if(0 <= row && row < 8)
			return rankLUT[row];
		return '-';
	}
	
	private static int toRow(char rank)
	{
		for(int i = 0; i < 8; i++)
			if(rankLUT[i] == rank) return i;
		return -1;
	}
	
}
