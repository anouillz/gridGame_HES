# Reversi Game with Graphics in Scala

## Introduction
This is a school project for the _School of engineering_ HES-SO Valais//Wallis. It is a Scala implementation of the classic board game Reversi, also known as Othello. It's a strategy game for two players, played on an 8x8 board with graphics.

## Features
- Graphical gameplay.
- Support for two-player mode.
- Dynamic updates of the game board and scores.

## Requirements
- Java JDK 11 or higher.
- Scala 2.13.

## How to Play
1. Run `Reversi` object. 
2. Players take turns placing coins on the board.
3. Chose where you wanna place the coin by indicating its coordinates.
   - The valid coordinates are recognisable by a yellow dot.
    - A move is valid if it traps a coin of the opposite player.
4. The game continues until one players wins.

## Code Structure

### 1. Reversi Class
`Reversi` is the main game class. It handles the overall game logic for Reversi, a strategy board game. Key responsibilities include:

- Managing the game board (8x8 grid by default) and its state.
- Handling game flow, including initializing the game, accepting player moves, validating the legality of moves, updating the board, and determining the game end.
- Displaying the game board and coins graphically using FunGraphics.
- Keeping track of each player's turn and determining the winner based on the number of coins.
- Offering the option to replay the game.

### 2. Coin Class
`Coin` class represents a coin in the Reversi game. Each coin has the following properties:

- row and col: Coordinates of the coin on the board.
- c: The color of the coin (Black, White, or by default darkGreen).
- busy: A boolean indicating whether the position is occupied.

### 3. GameManager Class
`GameManager` class manages the game state and player turns. Its responsibilities include:

- Tracking which player's turn it is (black or white).
- Displaying the current score.
- Prompting players for their next move and ensuring the input format is correct.


## Screenshots

<img src="res/ReversiGame.png" alt="drawing" width="500"/>




