package com.alihan.chess.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;

import com.alihan.chess.board.Board;
import com.alihan.chess.board.Move;
import com.alihan.chess.board.Piece.PieceColor;
import com.alihan.chess.board.Piece.PieceType;
import com.alihan.chess.search.Scholar;

public class Frame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private Scholar scholar;
	private Board board;
	
	private boolean computerTurn;
	private int i0, j0;
	private PieceType promote;
	
	private Scholar checkScholar;
	private Stack<Board> boardHistory;
	private Stack<Move> moveHistory;
	
	private Panel panel;
	private JLabel status;
	
	public Frame()
	{
		scholar = new Scholar();
		scholar.setTimeLimit(1500 * 1000000L);
		scholar.setDepthLimit(6);
		
		computerTurn = true;
		i0 = j0 = -1;
		promote = PieceType.QUEEN;
		
		checkScholar = new Scholar();
		checkScholar.setTimeLimit(0);
		checkScholar.setDepthLimit(1);
		
		boardHistory = new Stack<Board>();
		moveHistory = new Stack<Move>();
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu, menu1;
		JMenuItem item;
		
		menu = new JMenu("Game");
		menuBar.add(menu);
		
		item = new JMenuItem("New Game as White");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				i0 = j0 = -1;
				board = new Board();
				
				panel.setBoard(board);
				panel.setMove(null);
				panel.clearMark();
				panel.repaint();
				
				boardHistory.clear();
				moveHistory.clear();
				
				panel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				computerTurn = true;
				
				new Thread()
				{
					public void run()
					{
						Move move = scholar.compute(board);
						status.setText("  " + scholar);
						
						board.makeMove(move);
						panel.setMove(move);
						panel.repaint();
						
						boardHistory.push(board.clone());
						moveHistory.push(move);
						board.generateMoveList();
						
						panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						computerTurn = false;
					}
				}.start();
			}
		});
		menu.add(item);
		
		item = new JMenuItem("New Game as Black");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				i0 = j0 = -1;
				board = new Board();
				
				panel.setBoard(board);
				panel.setMove(null);
				panel.clearMark();
				panel.repaint();
				
				boardHistory.clear();
				moveHistory.clear();
				
				boardHistory.push(board.clone());
				moveHistory.push(null);
				board.generateMoveList();
				
				panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				computerTurn = false;
			}
		});
		menu.add(item);
		
		menu.addSeparator();
		
		item = new JMenuItem("Undo");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{				
				if(boardHistory.size() >= 2)
				{					
					boardHistory.pop();
					moveHistory.pop();
					
					board = boardHistory.peek().clone();
					board.generateMoveList();
					
					panel.setBoard(board);
					panel.setMove(moveHistory.peek());
					panel.clearMark();
					panel.repaint();
					
					i0 = j0 = -1;
					computerTurn = false;
				}
			}
			
		});
		menu.add(item);
		
		menu = new JMenu("Engine");
		menuBar.add(menu);
		
		item = new JMenuItem("Set Time Limit");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input = (String)JOptionPane.showInputDialog(null, "Time Limit (ms)", "Chessalot", JOptionPane.PLAIN_MESSAGE, null, null, "");
				if(input != null) scholar.setTimeLimit(Integer.parseInt(input) * 1000000L);
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Set Depth Limit");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input = (String)JOptionPane.showInputDialog(null, "Depth Limit (ms)", "Chessalot", JOptionPane.PLAIN_MESSAGE, null, null, "");
				if(input != null) scholar.setDepthLimit(Integer.parseInt(input));
			}
		});
		menu.add(item);
		
		menu.addSeparator();
		
		menu1 = new JMenu("Preset");
		menu.add(menu1);
		
		item = new JMenuItem("Quick  (1.5 sn)");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				scholar.setTimeLimit(1500 * 1000000L);
				scholar.setDepthLimit(6);
			}
		});
		menu1.add(item);
		
		item = new JMenuItem("Careful  (15 sn)");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				scholar.setTimeLimit(15000 * 1000000L);
				scholar.setDepthLimit(12);
			}
		});
		menu1.add(item);
		
		menu = new JMenu("Promote");
		menuBar.add(menu);
		
		ButtonGroup group = new ButtonGroup();
		
		item = new JRadioButtonMenuItem("Queen");
		item.addActionListener(new ActionListener()
		{ public void actionPerformed(ActionEvent e) { promote = PieceType.QUEEN; }});
		item.setSelected(true);
		group.add(item);
		menu.add(item);
		
		item = new JRadioButtonMenuItem("Rook");
		item.addActionListener(new ActionListener()
		{ public void actionPerformed(ActionEvent e) { promote = PieceType.ROOK; }});
		group.add(item);
		menu.add(item);
		
		item = new JRadioButtonMenuItem("Bishop");
		item.addActionListener(new ActionListener()
		{ public void actionPerformed(ActionEvent e) { promote = PieceType.BISHOP; }});
		group.add(item);
		menu.add(item);
		
		item = new JRadioButtonMenuItem("Knight");
		item.addActionListener(new ActionListener()
		{ public void actionPerformed(ActionEvent e) { promote = PieceType.KNIGHT; }});
		group.add(item);
		menu.add(item);
		
		setJMenuBar(menuBar);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setPreferredSize(new Dimension(getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		add(statusPanel, BorderLayout.SOUTH);
		
		status = new JLabel("  " + "ready");
		status.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(status);
		
		
		panel = new Panel();
		add(panel, BorderLayout.CENTER);
		
		panel.addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(MouseEvent e)
			{				
				if(e.getButton() != MouseEvent.BUTTON1) return;
				
				int i = (int)(e.getX() / panel.getTileSize());
				int j = 7 - (int)(e.getY() / panel.getTileSize());
				
				if(0 <= i && i < 8 && 0 <= j && j < 8 && !computerTurn && isPossibleInput(i, j))
				{
					if(i0 == -1)
					{
						i0 = i;
						j0 = j;
						
						panel.setMove(null);
						panel.setMark(i, j);
						panel.repaint();
					}
					else if(i0 == i && j0 == j)
					{
						i0 = j0 = -1;
						
						panel.clearMark();
						panel.repaint();
					}
					else
					{
						for(Move move : board.getMoveList())
						{
							if(move.i0 == i0 && move.j0 == j0 && move.i == i && move.j == j)
							{
								if(move.isPromote() && move.getPromoteType() != promote) continue;
																
								board.makeMove(move);								
								panel.setMove(move);
								panel.repaint();
								
								panel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
								computerTurn = true;
								
								new Thread()
								{
									public void run()
									{
										Move move = scholar.compute(board);
										status.setText("  " + scholar);
										
										if(move != null)
										{
											board.makeMove(move);
											panel.setMove(move);
											panel.repaint();
										}
										
										if(move == null || checkScholar.compute(board) == null)
										{
											//GAME OVER
											panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
											
											if(board.isInCheck(PieceColor.WHITE))
												JOptionPane.showMessageDialog(null, "CHECKMATE (black wins)", "Chessalot", JOptionPane.PLAIN_MESSAGE);
											else if(board.isInCheck(PieceColor.BLACK))
												JOptionPane.showMessageDialog(null, "CHECKMATE (white wins)", "Chessalot", JOptionPane.PLAIN_MESSAGE);
											else
												JOptionPane.showMessageDialog(null, "STALEMATE", "Chessalot", JOptionPane.PLAIN_MESSAGE);
											
											return;
										}
										
										boardHistory.push(board.clone());
										moveHistory.push(move);
										board.generateMoveList();
										
										panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
										computerTurn = false;
									}
								}.start();
								
								break;
							}
						}
						
						i0 = j0 = -1;
						panel.clearMark();
						panel.repaint();
					}
				}
			}
		});
		
		panel.addMouseMotionListener(new MouseAdapter()
		{
			public void mouseMoved(MouseEvent e)
			{
				int i = (int)(e.getX() / panel.getTileSize());
				int j = 7 - (int)(e.getY() / panel.getTileSize());
				
				if(0 <= i && i < 8 && 0 <= j && j < 8 && !computerTurn)
				{
					if(isPossibleInput(i, j)) panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
					else panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
		
	}
	
	private boolean isPossibleInput(int i, int j)
	{
		if(i0 == -1)
		{
			return board.getPieceArray()[i][j] != null && 
					board.getPieceArray()[i][j].getColor() == board.getColorToMove();
		}
		
		if(i == i0 && j == j0) return true;
		
		for(Move move : board.getMoveList())
		{
			if(move.i0 == i0 && move.j0 == j0 && move.i == i && move.j == j)
			{
				Board testBoard = board.clone();
				testBoard.makeMove(move);
				if(testBoard.isInCheck(board.getColorToMove())) continue;
				
				return true;
			}
		}
		
		return false;
	}
}
