package com.alihan.chess.search;

import java.util.Random;

import com.alihan.chess.board.Board;
import com.alihan.chess.board.Move;
import com.alihan.chess.board.Piece;
import com.alihan.chess.board.Piece.PieceColor;

public class Hasher
{
	private long[][][] pieceHash; //file, rank, piece
	private long colorHash;	
	private long[] castleHash;
	private long[][] enPassantHash; //file, index
	
	public Hasher() { this(0); }
	public Hasher(long seed)
	{
		Random random = new Random(seed);
		
		pieceHash = new long[8][8][12];
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
		for(int p = 0; p < 12; p++)
			pieceHash[i][j][p] = random.nextLong();
		
		colorHash = random.nextLong();
		
		castleHash = new long[16];
		for(int i = 0; i < 16; i++)
			castleHash[i] = random.nextLong();
		
		enPassantHash = new long[8][4];
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 4; j++)
			enPassantHash[i][j] = random.nextLong();
	}
	
	public long hash(Board board)
	{		
		long hash = 0;
		
		Piece[][] pieceArray = board.getPieceArray();
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
			if(pieceArray[i][j] != null)
				hash ^= pieceHash[i][j][pieceArray[i][j].toIndex()];
		
		if(board.getColorToMove() == PieceColor.WHITE)
			hash ^= colorHash;
		
		hash ^= castleHash[board.getCastleIndex()];
		
		for(int i = 0; i < 8; i++)
			hash ^= enPassantHash[i][board.getEnPassantIndex(i)];
		
		return hash;
	}
	
	public long updateHash(long hash, Board oldBoard, Board board,  Move move)
	{
		Piece[][] pieceArray = oldBoard.getPieceArray();
		
		if(pieceArray[move.i][move.j] != null)
			hash ^= pieceHash[move.i][move.j][pieceArray[move.i][move.j].toIndex()];
		
		hash ^= pieceHash[move.i0][move.j0][pieceArray[move.i0][move.j0].toIndex()];
		hash ^= pieceHash[move.i][move.j][pieceArray[move.i0][move.j0].toIndex()];
		
		if(move.isCastle())
		{
			if(move.i == 6 && move.j == 0)
			{
				hash ^= pieceHash[7][0][pieceArray[7][0].toIndex()];
				hash ^= pieceHash[5][0][pieceArray[7][0].toIndex()];
			}
			else if(move.i == 2 && move.j == 0)
			{
				hash ^= pieceHash[0][0][pieceArray[0][0].toIndex()];
				hash ^= pieceHash[3][0][pieceArray[0][0].toIndex()];
				
			}
			else if(move.i == 6 && move.j == 7)
			{
				hash ^= pieceHash[7][7][pieceArray[7][7].toIndex()];
				hash ^= pieceHash[5][7][pieceArray[7][7].toIndex()];
			}
			else if(move.i == 2 && move.j == 7)
			{
				hash ^= pieceHash[0][7][pieceArray[0][7].toIndex()];
				hash ^= pieceHash[3][7][pieceArray[0][7].toIndex()];
			}
		}
		else if(move.isEnPassant())
		{
			if(pieceArray[move.i0][move.j0] == Piece.WHITE_PAWN)
				hash ^= pieceHash[move.i][move.j - 1][pieceArray[move.i][move.j - 1].toIndex()];
			else if(pieceArray[move.i0][move.j0] == Piece.BLACK_PAWN)
				hash ^= pieceHash[move.i][move.j + 1][pieceArray[move.i][move.j + 1].toIndex()];
		}
		else if(move.isPromote())
		{
			hash ^= pieceHash[move.i][move.j][pieceArray[move.i0][move.j0].toIndex()];
			hash ^= pieceHash[move.i][move.j][Piece.getPiece(oldBoard.getColorToMove(), move.getPromoteType()).toIndex()];
		}
		
		hash ^= colorHash;
		
		hash ^= castleHash[oldBoard.getCastleIndex()];
		hash ^= castleHash[board.getCastleIndex()];
		
		for(int i = 0; i < 8; i++) hash ^= enPassantHash[i][oldBoard.getEnPassantIndex(i)];
		for(int i = 0; i < 8; i++) hash ^= enPassantHash[i][board.getEnPassantIndex(i)];
		
		return hash;
	}
	
	public long updateHash(long hash, Board oldBoard,  Move move)
	{
		Board board = oldBoard.clone();
		board.makeMove(move);
		
		return updateHash(hash, oldBoard, board, move);
	}
}
