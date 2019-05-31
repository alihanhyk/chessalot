package com.alihan.chess.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.alihan.chess.Chess;
import com.alihan.chess.board.Board;
import com.alihan.chess.board.Move;
import com.alihan.chess.board.Piece;

public class Panel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static final Color COLOR_LIGHT = new Color(240, 217, 181);
	private static final Color COLOR_DARK = new Color(181, 136, 99);
	
	private static final Color COLOR_MOVE_LIGHT = new Color(206, 210, 107);
	private static final Color COLOR_MOVE_DARK = new Color(170, 162, 59);
	
	private static Image image;
	private static int imageSource[][] = 
	{
		{1665, 333, 1998, 666},
		{1665,   0, 1998, 333},
		{ 999, 333, 1332, 666},
		{ 999,   0, 1332, 333},
		{ 666, 333,  999, 666},
		{ 666,   0,  999, 333},
		{1332, 333, 1665, 666},
		{1332,   0, 1665, 333},
		{ 333, 333,  666, 666},
		{ 333,   0,  666, 333},
		{   0, 333,  333, 666},
		{   0,   0,  333, 333}
	};
	
	private Board board;
	private Move move;
	
	public void setBoard(Board board) { this.board = board; }
	public void setMove(Move move) { this.move = move; }
	
	private int iMark = -1, jMark = -1;
	public void setMark(int i, int j) { iMark = i; jMark = j; }
	public void clearMark() { iMark = jMark = -1; }
	
	private float tileSize;
	public float getTileSize() { return tileSize; }
	
	public Panel()
	{
		setBackground(new Color(26, 26, 26));
		
		try
		{ image = ImageIO.read(Chess.class.getClassLoader().getResource("piece.png")); }
		catch (IOException e)
		{ e.printStackTrace(); }
		
		setPreferredSize(new Dimension(720, 720));
		tileSize = 90;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		tileSize = (float)Math.min(getWidth(), getHeight()) / 8;
		
		int xOffset = (int)(getWidth() - tileSize * 8) / 2;
		int yOffset = (int)(getHeight() - tileSize * 8) / 2;
		
		int[] position = new int[9];
		for(int i = 0; i < 9; i++)
			position[i] = (int)(i * tileSize);
		
		for(int i = 0; i < 8; i++)
		for(int j = 0; j < 8; j++)
		{
			if((i + j) % 2 == 0) g.setColor(COLOR_LIGHT);
			else g.setColor(COLOR_DARK);
			
			g.fillRect(position[i] + xOffset, position[j] + yOffset, position[i + 1] - position[i], position[j + 1] - position[j]);
		}
		
		if(iMark != -1)
		{
			if((iMark + 7 - jMark) % 2 == 0) g.setColor(COLOR_MOVE_LIGHT);
			else g.setColor(COLOR_MOVE_DARK);
			
			g.fillRect(position[iMark] + xOffset, position[7 - jMark] + yOffset, 
					position[iMark + 1] - position[iMark], position[7 - jMark + 1] - position[7 - jMark]);
		}
		
		if(move != null)
		{
			if((move.i + 7 - move.j) % 2 == 0) g.setColor(COLOR_MOVE_LIGHT);
			else g.setColor(COLOR_MOVE_DARK);
			
			g.fillRect(position[move.i] + xOffset, position[7 - move.j] + yOffset, 
					position[move.i + 1] - position[move.i], position[7 - move.j + 1] - position[7 - move.j]);
			
			if((move.i0 + 7 - move.j0) % 2 == 0) g.setColor(COLOR_MOVE_LIGHT);
			else g.setColor(COLOR_MOVE_DARK);
			
			g.fillRect(position[move.i0] + xOffset, position[7 - move.j0] + yOffset, 
					position[move.i0 + 1] - position[move.i0], position[7 - move.j0 + 1] - position[7 - move.j0]);
		}
		
		if(board != null)
		{
			Piece[][] pieceArray = board.getPieceArray();
			
			for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
			{
				if(pieceArray[i][7 - j] != null)
				{
					int p = pieceArray[i][7 - j].toIndex();
					g.drawImage(image, 
							position[i] + xOffset, position[j] + yOffset, position[i + 1] + xOffset, position[j + 1] + yOffset,
							imageSource[p][0], imageSource[p][1], imageSource[p][2], imageSource[p][3],
							null);
				}
			}
		}
	}
}
