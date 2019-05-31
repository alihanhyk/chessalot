package com.alihan.chess.board;

public class Piece
{
	public enum PieceType {KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN};
	public enum PieceColor {WHITE, BLACK};
	
	private PieceType type;
	private PieceColor color;
	
	public PieceType getType() { return type; }
	public PieceColor getColor() { return color; }
	
	private int[] fileOffset;
	private int[] rankOffset;
	private boolean slidable;
	
	public int getOffsetLength() { return fileOffset.length; };
	public int getFileOffset(int dir) { return fileOffset[dir]; };
	public int getRankOffset(int dir) { return rankOffset[dir]; };
	public boolean isSlidable() { return slidable; }
	
	private String string;
	private int index;
	
	public String toString() { return string; }
	public int toIndex() { return index; }
	
	private Piece(PieceType type, PieceColor color)
	{
		this.type = type;
		this.color = color;
		
		if(type == PieceType.KING)
		{
			fileOffset = new int[] {-1, -1, -1,  0, 0,  1,  1,  1};
			rankOffset = new int[] {-1,  0,  1, -1, 1, -1,  0,  1};
			slidable = false;
			
			if(color == PieceColor.WHITE) { string = "K"; index = 11; }
			else { string = "k"; index = 10; }
		}
		else if(type == PieceType.QUEEN)
		{
			fileOffset = new int[] {-1, -1, -1,  0, 0,  1,  1,  1};
			rankOffset = new int[] {-1,  0,  1, -1, 1, -1,  0,  1};
			slidable = true;
			
			if(color == PieceColor.WHITE) { string = "Q"; index = 9; }
			else { string = "q"; index = 8; }
		}
		else if(type == PieceType.ROOK)
		{
			fileOffset = new int[] {-1,  0,  0,  1};
			rankOffset = new int[] { 0, -1,  1,  0};
			slidable = true;
			
			if(color == PieceColor.WHITE) { string = "R"; index = 7; }
			else { string = "r"; index = 6; }
		}
		else if(type == PieceType.BISHOP)
		{
			fileOffset = new int[] {-1, -1,  1,  1};
			rankOffset = new int[] {-1,  1, -1,  1};
			slidable = true;
			
			if(color == PieceColor.WHITE) { string = "B"; index = 5; }
			else { string = "b"; index = 4; }
		}
		else if(type == PieceType.KNIGHT)
		{
			fileOffset = new int[] {-2, -2, -1, -1,  1,  1,  2,  2};
			rankOffset = new int[] {-1,  1, -2,  2, -2,  2, -1,  1};
			slidable = false;
			
			if(color == PieceColor.WHITE) { string = "N"; index = 3; }
			else { string = "n"; index = 2; }
		}
		else
		{
			if(color == PieceColor.WHITE) { string = "P"; index = 1; }
			else { string = "p"; index = 0; }
		}
	}
	
	public static Piece WHITE_PAWN = new Piece(PieceType.PAWN, PieceColor.WHITE);
	public static Piece WHITE_ROOK = new Piece(PieceType.ROOK, PieceColor.WHITE);
	public static Piece WHITE_KNIGHT = new Piece(PieceType.KNIGHT, PieceColor.WHITE);
	public static Piece WHITE_BISHOP = new Piece(PieceType.BISHOP, PieceColor.WHITE);
	public static Piece WHITE_QUEEN = new Piece(PieceType.QUEEN, PieceColor.WHITE);
	public static Piece WHITE_KING = new Piece(PieceType.KING, PieceColor.WHITE);
	
	public static Piece BLACK_PAWN = new Piece(PieceType.PAWN, PieceColor.BLACK);
	public static Piece BLACK_ROOK = new Piece(PieceType.ROOK, PieceColor.BLACK);
	public static Piece BLACK_KNIGHT = new Piece(PieceType.KNIGHT, PieceColor.BLACK);
	public static Piece BLACK_BISHOP = new Piece(PieceType.BISHOP, PieceColor.BLACK);
	public static Piece BLACK_QUEEN = new Piece(PieceType.QUEEN, PieceColor.BLACK);
	public static Piece BLACK_KING = new Piece(PieceType.KING, PieceColor.BLACK);
	
	public static Piece getPiece(PieceColor color, PieceType type)
	{
		if(color == PieceColor.WHITE)
		{
			if(type == PieceType.KING) return Piece.WHITE_KING;
			else if(type == PieceType.QUEEN) return Piece.WHITE_QUEEN;
			else if(type == PieceType.ROOK) return Piece.WHITE_ROOK;
			else if(type == PieceType.BISHOP) return Piece.WHITE_BISHOP;
			else if(type == PieceType.KNIGHT) return Piece.WHITE_KNIGHT;
			else return Piece.WHITE_PAWN;
		}
		else
		{
			if(type == PieceType.KING) return Piece.BLACK_KING;
			else if(type == PieceType.QUEEN) return Piece.BLACK_QUEEN;
			else if(type == PieceType.ROOK) return Piece.BLACK_ROOK;
			else if(type == PieceType.BISHOP) return Piece.BLACK_BISHOP;
			else if(type== PieceType.KNIGHT) return Piece.BLACK_KNIGHT;
			else return Piece.BLACK_PAWN;
		}
	}
}