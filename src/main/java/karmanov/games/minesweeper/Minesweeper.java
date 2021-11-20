package karmanov.games.minesweeper;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Minesweeper {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CIAN = "\u001B[36m";
    private static final String ANSI_WHILE = "\u001B[37m";

    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private static final int MINES_COUNT = 20;
    private static final int MINE = 9;
    private static final int EMPTY = 0;

    private static final int CELL_OPEN = 1;
    private static final int CELL_CLOSE = 0;
    private static final int CELL_FLAG = -1;

    private static boolean IN_GAME = true;

    public void start() {
        int[][] board = generateBoard();
        int[][] moves = new int[HEIGHT][WIDTH];

        do {
            printBoard(board, moves, false);
            IN_GAME = move(board, moves);
        }
        while (IN_GAME);
        printBoard(board, moves, true);

    }

    public boolean isPlayWin() {
        return IN_GAME;
    }

    private boolean move(int[][] board, int[][] moves) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Сделайте ход в формате [столбец][строка][+/-] (пример: A1): ");

            String move = sc.nextLine();
            int col = move.charAt(0) - 'A';
            int row = move.charAt(1) - '1';

            if (row >= 0 && row <= HEIGHT && col >= 0 && col <= WIDTH) {
                if (board[row][col] == MINE) return false;
                moves[row][col] = CELL_OPEN;
                return true;
            }
        }
    }

    private int[][] generateBoard() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int[][] board = new int[HEIGHT][WIDTH];
        int mines = MINES_COUNT;

        while (mines > 0) {
            int x = random.nextInt(HEIGHT), y = random.nextInt(WIDTH);
            if (board[x][y] == MINE) continue;
            board[x][y] = MINE;
            mines--;
        }

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == MINE) continue;

                int minesCount = 0;
                for (int k = i - 1; k <= i + 1; k++) {
                    for (int l = j - 1; l <= j + 1; l++) {
                        if (k >= HEIGHT || k < 0 || l < 0 || l >= WIDTH) continue;
                        if (board[k][l] == MINE) minesCount++;
                    }
                }
                board[i][j] = minesCount;
            }
        }
        return board;
    }

    private void printBoard(int[][] board, int[][] moves, boolean visibleMine) {
        System.out.println();
        System.out.print("\t");
        for (char i = 'A'; i < 'A' + WIDTH; i++) {
            System.out.print(" " + i + "\t");
        }
        System.out.println();
        for (int i = 0; i < HEIGHT; i++) {
            System.out.print((i + 1) + "\t");
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] == MINE && visibleMine) {
                    String cellColor = getCellColor(board[i][j]);
                    System.out.print(cellColor);
                    System.out.print("[*]\t");
                    System.out.print(ANSI_RESET);
                    continue;
                }
                if (moves[i][j] == CELL_CLOSE) {
                    System.out.print("[ ]\t");
                    continue;
                }
                if (moves[i][j] == CELL_FLAG) {
                    System.out.print("[+]\t");
                    continue;
                }

                String cellColor = getCellColor(board[i][j]);
                System.out.print(cellColor);
                if (board[i][j] == MINE) {
                    System.out.print("[*]\t");
                } else if (board[i][j] == EMPTY) {
                    System.out.print("[.]\t");
                } else {
                    System.out.print("[" + board[i][j] + "]\t");
                }
                System.out.print(ANSI_RESET);
            }
            System.out.println();
        }
    }

    private String getCellColor(int cell) {
        switch (cell) {
            case EMPTY:
                return ANSI_WHILE;
            case MINE:
                return ANSI_BLACK;
            case 1:
                return ANSI_GREEN;
            case 2:
                return ANSI_BLUE;
            case 3:
                return ANSI_RED;
            case 4:
                return ANSI_CIAN;
            default:
                return ANSI_PURPLE;
        }
    }
}
