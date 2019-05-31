# CHESSALOT

Chessalot is a simple chess engine that was developed to explore the fundamentals of chess programming. It features a simple GUI that is meant to replicate the GUI of [lichess.org](https://lichess.org/).

## The Algorithm

The engine uses the Negamax algorithm to search for moves. Specifically, the variation with alpha-beta pruning and transposition tables is used, which is given in a pseudo code form on [Negamax Wikipedia Page](https://en.wikipedia.org/wiki/Negamax). Further improvements are made to this variation:

- *Iterative Deepening:* The engine progressively makes deeper searches as long as only a predetermined amount of time has passed since the start of the search. This ensures execution time to stay relatively constant by dynamically changing search depths as the game simplifies or get more complicated.
- *Heuristics and History Heuristics:* Captures of high-valued pieces and successful moves from earlier shallow iterations of the algorithm are searched first to maximize the number of alpha and beta cutoffs during the execution.
- *Quiescence Search:* Only stable states of the board, meaning the states without any possible captures) are evaluated. This is done by continuing the search by considering only the capturing moves even if the algorithm reaches the predetermined maximum depth.

The evaluation function of the engine closely follows the one in [Tom Kerrigan's TSCP](http://www.tckerrigan.com/Chess/TSCP/). It considers material values of the pieces, pawn structure of the files (passed and isolated pawns) and piece-square tables, which are tables with additional scores given to the pieces based on their positioning on the board.
