public class Rectboard {
    public static String[][] gameBoard;

    public static String[][] coverBoard;

    public static int bombs;

    /*parametrized constructor */
    public Rectboard(int noOfRowsInBoard, int noOfColumnsInBoard, int bombs) {
        this.bombs = bombs;
        BuildGameBoard(noOfRowsInBoard, noOfColumnsInBoard);
        cover(noOfRowsInBoard, noOfColumnsInBoard, bombs);
    }
    /*This method is used to build game board*/
    public static String[][] BuildGameBoard(int rows, int columns) {
        gameBoard = new String[rows][columns];
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                gameBoard[x][y] = "-";
            }
        }
        return gameBoard;
    }

    public static String[][] cover(int rows, int columns, int bombs) {
        coverBoard = new String[rows][columns];

        for (int x = 0; x < coverBoard.length; x++) {
            for (int y = 0; y < coverBoard[x].length; y++) {
                coverBoard[x][y] = "1";
            }
        }
        while (bombs > 0) {
            int row1 = (int) (Math.random() * rows + 0);
            int col1 = (int) (Math.random() * columns + 0);
            if (coverBoard[row1][col1] != "M") {
                coverBoard[row1][col1] = "M";
                bombs--;
            }
        }
        setCoverValue();
        return coverBoard;
    }

    private static void setCoverValue(){
        for (int x = 0; x < coverBoard.length; x++) {
            for (int y = 0; y < coverBoard[0].length; y++) {
                int count = 0;
                int xmax = 1;
                int xmin = -1;
                int ymax = 1;
                int ymin = -1;
                if (x == 0) {
                    xmin = 0;
                } else if (x == coverBoard.length - 1) {
                    xmax = 0;
                }
                if (y == 0) {
                    ymin = 0;
                } else if (y == coverBoard[0].length - 1) {
                    ymax = 0;
                }
                for (int i = xmin; i <= xmax; i++) {
                    for (int k = ymin; k <= ymax; k++) {
                        if (coverBoard[i + x][k + y].equals("M"))
                            count++;
                    }
                }
                char coverChar = setFieldValue(x, y, count);
                coverBoard[x][y] = "" + coverChar;
            }
        }
    }

    public static char setFieldValue(int x, int y, int count) {
        if (coverBoard[x][y].equals("M")) {
            return 'M';
        } else {
            return (char)count;
        }

    }

    public static void recursion(int rows, int columns) {
        if (rows < 0)
            rows = 0;
        if (columns < 0)
            columns = 0;
        if (rows >= gameBoard.length)
            rows = gameBoard.length - 1;
        if (columns >= gameBoard[rows].length)
            columns = gameBoard[rows].length - 1;

        gameBoard[rows][columns] = coverBoard[rows][columns];

        if (gameBoard[rows][columns].equals("O")) {
            setRecurValue(rows, columns);
        }
    }

    private static void setRecurValue(int rows, int columns){
        int rowMin = 1;
        int rowMax = -1;
        int columnMax = 1;
        int columnMin = -1;

        if (rows == 0) {
            rowMax = 0;
        } else if (rows == gameBoard.length - 1) {
            rowMin = 0;
        }

        if (columns == 0) {
            columnMin = 0;
        } else if (columns == gameBoard[rows].length - 1) {
            columnMax = 0;
        }
        for (int x = rows + rowMax; x <= rows + rowMin; x++) {
            for (int y = columns + columnMin; y <= columns + columnMax; y++) {
                if (gameBoard[x][y].equals("-")) {
                    recursion(x, y);
                }
            }
        }
    }

    public static String[][] changeFlag(int row, int column) {
        row--;
        column--;
        if (gameBoard[row][column].compareTo("-") == 0 || gameBoard[row][column].compareTo("!") == 0) {
            if (gameBoard[row][column].compareTo("-") == 0) {
                gameBoard[row][column] = "!";
                Minesweeper.num--;
            } else if (gameBoard[row][column].compareTo("!") == 0) {
                gameBoard[row][column] = "-";
                Minesweeper.num++;
            }
        }
        return gameBoard;
    }

    public static String[][] clickbomb(int rows, int columns) {
        rows--;
        columns--;

        if (rows < 0)
            rows = 0;
        if (columns < 0)
            columns = 0;
        if (rows >= gameBoard.length)
            rows = gameBoard.length - 1;
        if (columns >= gameBoard[rows].length)
            columns = gameBoard[rows].length - 1;

        if (gameBoard[rows][columns].equals("!")) {
            return gameBoard;
        } else {
            recursion(rows, columns);
        }

        if (gameBoard[rows][columns].equals("M")) {
            whenBombIsClicked(rows);
        }

        return gameBoard;
    }

    private static void whenBombIsClicked(int rows) {
        for (int x = 0; x < coverBoard.length; x++) {
            for (int y = 0; y < coverBoard[rows].length; y++) {
                if (coverBoard[x][y].equals("M") && !gameBoard[x][y].equals("!")) {
                    gameBoard[x][y] = "M";
                } else if (coverBoard[x][y].equals("M") && gameBoard[x][y].equals("!")) {
                    gameBoard[x][y] = "!";
                } else if (!coverBoard[x][y].equals("M") && gameBoard[x][y].equals("!")) {
                    gameBoard[x][y] = "X";
                }

                System.out.print(gameBoard[x][y]);
            }

            System.out.println();
        }

        System.out.println("You lost the game.");
        Minesweeper.loop = false;
    }

    public static String convertToString() {
        String temp = "";
        temp += "Mines remaining: " + Minesweeper.num + "\n\n";
        int charsForRow = (int) Math.log10(gameBoard.length);
        int charsForCol = (int) Math.log10(gameBoard[0].length);
        for (int i = 0; i <= charsForCol; i++) {
            for (int k = charsForRow + 1; k > 0; k--) {
                temp += " ";
            }
            temp += " ";
            for (int k = 1; k <= gameBoard[0].length; k++) {
                int exp = charsForCol + 1 - i;
                temp += (int) (k / Math.pow(10, exp - 1) % 10);
            }
            temp += "\n";
        }

        temp += "\n";

        for (int r = 0; r < gameBoard.length; r++) {
            for (int i = charsForRow + 1; i > 0; i--) {
                if (i > Math.log10(r + 1) + 1) {
                    temp += 0;
                } else {
                    temp += (r + 1);
                    break;
                }
            }
            temp += " ";
            for (int c = 0; c < gameBoard[r].length; c++) {
                temp += gameBoard[r][c];
            }
            temp += "\n";
        }
        temp += "\n";
        return temp;
    }

    public static boolean winTest() {
        int count = 0;
        for (String[] r : gameBoard) {
            for (String s : r) {
                if (s.equals("-") || s.equals("!")) {
                    count++;
                }
            }
        }
        if (count == bombs) {
            return true;
        }
        return false;
    }

}

