package com.alihan.chess.search;

import java.util.Collections;
import java.util.HashMap;

import com.alihan.chess.board.Board;
import com.alihan.chess.board.Move;

public class Scholar
{
	private Evaluator evaluator;
	private Hasher hasher;
	
	private HashMap<Long, TT_Entry> transpositionTable;
	private enum TT_Flag {EXACT, LOWERBOUND, UPPERBOUND};
	private class TT_Entry
	{
		private int value;
		private TT_Flag flag;
		private int depth;
	}
	
	private int[][][][] historyHeuristic;
	
	private Move bestMove;
	private int bestScore;
	public Move getBestMove() { return bestMove; }
	public int getBestScore() { return bestScore; }
	
	private int evaluated;
	private int evaluatedCapture;
	public int getEvalauted() { return evaluated; }
	public int getEvalautedCapture() { return evaluatedCapture; }
	
	private long time;
	private long timeLimit;
	public long getTime() { return time; }
	public void setTimeLimit(long limit) { timeLimit = limit; }
	
	private int depth;
	private int depthLimit;
	public int getDepth() { return depth; }
	public void setDepthLimit(int limit) { depthLimit = limit; }
	
	private int depthCapture;
	private int depthCapture_temp;
	public int getDepthCapture() { return depthCapture; }
		
	public Scholar()
	{
		evaluator = new Evaluator();
		hasher = new Hasher();
		
		transpositionTable = new HashMap<Long, TT_Entry>();
		historyHeuristic = new int[8][8][8][8];
		
		timeLimit = 1000000000L;
		depthLimit = 10;
	}
	
	public Move compute(Board board)
	{
		transpositionTable.clear();
		for(int i0 = 0; i0 < 8; i0++)
		for(int j0 = 0; j0 < 8; j0++)
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
			historyHeuristic[i0][j0][i][j] = 0;
		
		bestMove = null;
		bestScore = 0;
		
		evaluated = 0;
		evaluatedCapture = 0;
		
		time = System.nanoTime();
		
		Move lastBestMove = null;
		int lastBestScore = 0;
		int lastDepth = 0;
		
		long hash = hasher.hash(board);
		for(depth = 1; depth <= depthLimit; depth++)
		{
			bestScore = search(board, hash, -Integer.MAX_VALUE, Integer.MAX_VALUE, depth, 0);
			if(System.nanoTime() - time > timeLimit && depth > 1) break;
			
			//records the results of the last uninterrupted search 
			lastBestMove = bestMove;
			lastBestScore = bestScore;
			lastDepth = depth;
		}
		
		time = System.nanoTime() - time;
		
		bestMove = lastBestMove;
		bestScore = lastBestScore;
		depth = lastDepth;
		
		return bestMove;
	}
	
	private int search(Board board, long hash, int alpha, int beta, int depth, int root)
	{
		if(System.nanoTime() - time > timeLimit && this.depth > 1)
			return 0;
		
		TT_Entry entry;
		int alphaOriginal = alpha;
		
		entry = transpositionTable.get(hash);
		if(entry != null && entry.depth >= depth)
		{
			if(entry.flag == TT_Flag.EXACT) return entry.value;
			else if(entry.flag == TT_Flag.LOWERBOUND) alpha = Math.max(entry.value, alpha);
			else if(entry.flag == TT_Flag.UPPERBOUND) beta = Math.min(entry.value, beta);
			
			if(alpha >= beta) return entry.value;
		}
		
		if(depth == 0)
		{
			int value = quick_search(board, alpha, beta, this.depth);
			evaluated++;
			
			if(depthCapture_temp > depthCapture) depthCapture = depthCapture_temp;
			depthCapture_temp = 0;
			
			return value;
		}
		
		int bestValue = -Integer.MAX_VALUE;
		boolean noLegalMoves = true;
		
		board.generateMoveList();
		
		for(Move move : board.getMoveList())
			move.addOrderingScore(historyHeuristic[move.i0][move.j0][move.i][move.j]);
		
		Collections.sort(board.getMoveList());
		Collections.reverse(board.getMoveList());
		
		for(Move move : board.getMoveList())
		{
			Board nextBoard = board.clone();
			nextBoard.makeMove(move);
			
			long nextHash = hasher.updateHash(hash, board, nextBoard, move);
			
			if(nextBoard.isInCheck(board.getColorToMove())) continue;
			noLegalMoves = false;
			
			int value = -search(nextBoard, nextHash, -beta, - alpha, depth - 1, root + 1);
			
			if(value > bestValue)
			{
				bestValue = value;
				if(root == 0) bestMove = move;
			}
			
			if(value > alpha)
			{
				alpha = value;
				historyHeuristic[move.i0][move.j0][move.i][move.j] += depth;
			}
			if(alpha >= beta) break;
		}
		
		if(noLegalMoves)
		{
			if(board.isInCheck(board.getColorToMove())) bestValue = -Integer.MAX_VALUE + root;
			else bestValue = 0; //stalemate
		}
		
		entry = new TT_Entry();
		
		entry.value = bestValue;
		if(bestValue <= alphaOriginal) entry.flag = TT_Flag.UPPERBOUND;
		else if(bestValue >= beta) entry.flag = TT_Flag.LOWERBOUND;
		else entry.flag = TT_Flag.EXACT;
		entry.depth = depth;
		
		transpositionTable.put(hash, entry);
		
		return bestValue;
	}
	
	private int quick_search(Board board, int alpha, int beta, int depth)
	{	
		if(depth > depthCapture_temp) depthCapture_temp = depth;
		
		int value = evaluator.evaluate(board, false);
		evaluatedCapture++;
		
	    if(value >= beta) return beta;
	    if(alpha < value) alpha = value;
		
	    board.generateMoveList(true);
	    Collections.sort(board.getMoveList());
		Collections.reverse(board.getMoveList());
		
		for(Move move : board.getMoveList())
		{
			Board nextBoard = board.clone();
			nextBoard.makeMove(move);
			
			if(nextBoard.isInCheck(board.getColorToMove())) continue;
			
	        value = -quick_search(nextBoard, -beta, -alpha, depth + 1);
	 
	        if(value >= beta) return beta;
	        if(value > alpha) alpha = value;
	    }
		
	    return alpha;
	}
	
	public String toString()
	{
		String string = "";
		
		string += "time = " + time / 1000000L + " ms";
		string += ",    depth = " + depth;
		string += ",    evaluated = " + evaluated;
		string += ",    score = " + bestScore;
		
		return string;
	}
}
