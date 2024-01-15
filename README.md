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
- Players take turns placing coins on the board.
- Chose where you wanna place the coin by indicating its coordinates.
- The valid coordinates are recognisable by a yellow dot.
    - A move is valid if it traps a coin of the opposite player.
- The game continues until one players wins.

## Code Structure
- `Reversi`: The main class that initializes the game and handles the graphics.
- `Coin`: Represents the coins on the board.
- `GameManager`: Manages the game logic and player turns.

## Screenshots

<img src="res/ReversiGame.png" alt="drawing" width="500"/>




