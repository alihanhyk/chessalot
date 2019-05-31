package com.alihan.chess;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.alihan.chess.gui.Frame;

//import java.util.Scanner;
//
//import com.alihan.chess.board.Board;
//import com.alihan.chess.board.Move;
//import com.alihan.chess.search.Scholar;

public class Chess
{
	public static void main(String[] args)
	{
		Frame frame = new Frame();
		frame.setTitle("Chessalot");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(480, 545));
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

//		Scanner scanner;
//		scanner = new Scanner(System.in);
//		
//		Scholar scholar = new Scholar();
//		scholar.setTimeLimit(1500 * 1000000L);
//		scholar.setDepthLimit(6);
//		
//		System.out.println("CHESSALOT");
//		
//		boolean play = true;
//		boolean info = true;
//		
//		while(true)
//		{
//			String input = scanner.nextLine();
//			
//			if(input.length() >= 5 && input.substring(0, 5).equals("time "))
//				scholar.setTimeLimit(Integer.parseInt(input.substring(5)) * 1000000L);
//			
//			else if(input.length() >= 6 && input.substring(0, 6).equals("depth "))
//				scholar.setDepthLimit(Integer.parseInt(input.substring(6)));
//			
//			else if(input.length() >= 5 && input.substring(0, 5).equals("info "))
//				info = Integer.parseInt(input.substring(5)) != 0;
//				
//			else if(input.equals("white")) { play = true; break; }
//			else if(input.equals("black")) { play = false; break; }
//			else if(input.equals("exit")) return;
//		}
//		
//		System.out.println();
//		Board board = new Board();
//		
//		while(true)
//		{
//			if(play)
//			{
//				Move move = scholar.compute(board);
//				
//				System.out.print(move);
//				if(info) System.out.print("\t" + scholar);
//				System.out.println();
//				
//				board.makeMove(move);
//			}
//			play = true;
//			
//			String input = "";
//			while(true)
//			{	
//				input = scanner.nextLine();
//				if(input.equals("print")) System.out.println("\n" + board + "\n");
//				else break;
//			}
//			
//			if(input.equals("exit")) break;
//			
//			Move move = new Move(input);
//			board.makeMove(move);
//		}
//		
//		scanner.close();
	}
}
