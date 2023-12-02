import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Main {
    private static final int SIZE = 9;

    public static void main(String[] args) {
        int[][] sudoku = generateSudoku();
        Random random = new Random();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (random.nextDouble() > 0.5) {
                    sudoku[i][j] = 0;
                }
            }
        }

        int[][] sudokuEmpty = copySudoku(sudoku);

        if (!solveSudoku(sudokuEmpty)) {
            System.out.println("Sudoku konnte nicht gelöst werden.");
        }

        createSudokuImage(sudoku);
        System.out.println("Sudoku erfolgreich erstellt und als 'sudoku.png' gespeichert.");
    }

    public static int[][] generateSudoku() {
        int[][] grid = new int[SIZE][SIZE];
        fillSudokuGrid(grid);
        return grid;
    }

    public static boolean fillSudokuGrid(int[][] grid) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random random = new Random();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    shuffleArray(numbers, random);
                    for (int num : numbers) {
                        if (isValidMove(grid, i, j, num)) {
                            grid[i][j] = num;
                            if (fillSudokuGrid(grid)) {
                                return true;
                            } else {
                                grid[i][j] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidMove(int[][] grid, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false;
            }
        }
        int boxRow = row / 3 * 3;
        int boxCol = col / 3 * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[boxRow + i][boxCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean solveSudoku(int[][] grid) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValidMove(grid, i, j, num)) {
                            grid[i][j] = num;
                            if (solveSudoku(grid)) {
                                return true;
                            } else {
                                grid[i][j] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static void createSudokuImage(int[][] sudokuGrid) {
        int imageSize = 450;
        BufferedImage img = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageSize, imageSize);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 40));

        for (int i = 0; i <= SIZE; i++) {
            int lineWidth = (i % 3 == 0) ? 3 : 1;
            g.setStroke(new BasicStroke(lineWidth));
            g.drawLine(i * 50, 0, i * 50, imageSize);
            g.drawLine(0, i * 50, imageSize, i * 50);
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (sudokuGrid[i][j] != 0) {
                    g.drawString(String.valueOf(sudokuGrid[i][j]), j * 50 + 15, i * 50 + 45);
                }
            }
        }

        try {
            ImageIO.write(img, "png", new File("sudoku.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shuffleArray(int[] array, Random random) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static int[][] copySudoku(int[][] sudoku) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(sudoku[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }
}