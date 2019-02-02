import java.util.InputMismatchException;
import java.util.Scanner;

public class Minesweeper {
    public static int num;
    public static boolean loop = true;
    public static boolean game = true;
    public String errorWhenCharInsteadOfNum = "A letter has been entered when a number was expected. The program must now exit.";

    public void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        showIntro();
        while (game) {
            showGameRules();
            loop = true;
            SelectGameOption(scan);
            loop = true;
            setFlag(scan);
            ifPlayAgain(scan);
        }
        scan.close();
    }
/*
To print Game's rules and processes
 */
    public static void showIntro() {
        System.out
                .println("Hello, this is the minesweeper game. We would now introduce to you the rules you need to know to play.\n");
        System.out
                .println("We recommend setting the console font to a truetype font, such as Courier for the best play experience\n");
        System.out
                .println("Printed below will be the board for you. \n"
                        + "Dashes (-) are spaces that you have not opened yet. \n"
                        + "O's are spaces that are clear. \n"
                        + "Numbers are the same. \n"
                        + "!'s are flags and if you hit a mine all mines will show up as M's and game over. \n"
                        + "Try not to hit a mine :)\n");
        System.out
                .println("To click on a space you will need to enter the coordinate you intend to open. Input the row number first and press enter. \n"
                        + "Then the column number and press enter again. \n\n"
                        + "You will always be asked if you would like to place a flag at the beginning of each turn. \n"
                        + "If you would like to place a flag just type in Y else type in N and press enter.\n");
        System.out.println("Good luck on your game and have fun!");
    }

    public static void showGameRules() {

        System.out.println("Please select a game mode");
        System.out.println("1) Beginner  [9x9, 10 bombs]");
        System.out.println("2) Medium    [16x16, 40 bombs]");
        System.out.println("3) Expert    [16x30, 99 bombs]");
        System.out.println("4) Custom    [Select your own size]");

    }

    public void SelectGameOption(Scanner scan) {
        while (loop) {
            String inp = "";
            try {
                inp = scan.nextLine().trim();
            } catch (InputMismatchException e) {
                System.out
                        .println(errorWhenCharInsteadOfNum);
                loop = false;
                game = false;
                break;
            }
            if (inp.charAt(0) == '1') {
                num = 10;
                new Rectboard(9, 9, num);
                loop = false;
            } else if (inp.charAt(0) == '2') {
                num = 40;
                new Rectboard(16, 16, num);
                loop = false;
            } else if (inp.charAt(0) == '3') {
                num = 99;
                new Rectboard(16, 30, num);
                loop = false;
            } else if (inp.charAt(0) == '4') {
                playCustomMinefield(scan);
            } else {
                System.out.println("Please input a number listed above");
            }
        }

    }

    public void playCustomMinefield(Scanner scan) {
        System.out.println("Please input the number of noRowsInBoard");
        int noRowsInBoard = -1, noColumnsInBoard = -1, numOfBombs = -1;
        while (loop) {
            noRowsInBoard = scan.nextInt();
            checkIfNumberIsPositive(noRowsInBoard);
        }
        loop = true;
        System.out.println("Please input the number of noColumnsInBoard");
        while (loop) {
            noColumnsInBoard = scan.nextInt();
            checkIfNumberIsPositive(noColumnsInBoard);
        }
        loop = true;
        System.out.println("Please input the number of numOfBombs");
        while (loop) {
            numOfBombs = scan.nextInt();
            if (numOfBombs > 0 && numOfBombs <= (noRowsInBoard * noColumnsInBoard)) {
                loop = false;
            } else {
                System.out
                        .println("Please input a positive number that allows for each bomb to fit on a board");
            }
        }
        num = numOfBombs;
        new Rectboard(noRowsInBoard, noColumnsInBoard, num);
    }
    public void checkIfNumberIsPositive(int number){
        if (number > 0) {
            loop = false;
        } else {
            System.out
                    .println("Please input a positive number");
        }

    }
    public void setFlag(Scanner scan) {
        while (loop) {

            System.out.println(Rectboard.convertToString());
            // user input String

            System.out
                    .println("Do you want to put a flag down or remove a flag? (yes enter Y, no enter N)");
            boolean tryIn = true;
            String flagValue = "";
            while (tryIn) {
                try {
                    String inputFromUser = scan.nextLine();
                    flagValue = inputFromUser.toUpperCase();

                } catch (Throwable e) {
                    System.out.println(e);

                } finally {
                    if (!flagValue.equals(""))
                        tryIn = false;
                }
            }

            if (flagValue.compareTo("Y") != 0 && flagValue.compareTo("N") != 0)
                System.out.println("Please enter Y or N.");

            if (flagValue.compareTo("Y") == 0) {
                ifFlagYes(scan);

            }

            if (flagValue.compareTo("N") == 0) {
                ifFlagNo(scan);
                // Remove spot


            }
            if (Rectboard.winTest() == true) {
                loop = false;
                System.out.println("You win!");
                System.out.println(Rectboard.convertToString());
            }
        }
    }





    public int setSpotValues(Scanner scan){
        int spotValues = -1;
        boolean tryIn = true;
        while(tryIn){
            try{
                spotValues = scan.nextInt();
            }catch(InputMismatchException e){
                System.out.println(errorWhenCharInsteadOfNum);
                loop = false;
                game =false;
                break;
            }finally {
                if(spotValues != -1)
                    tryIn = false;
            }
        }
        return spotValues;
    }
    public void ifFlagYes(Scanner scan){
        System.out.println("What spot do you want to flag?");
        int FLAGR = setSpotValues(scan);
        int FLAGC = setSpotValues(scan);
        if (FLAGR > 0 && FLAGR <= Rectboard.gameBoard.length && FLAGC > 0 && FLAGC <= Rectboard.gameBoard[0].length)
            Rectboard.changeFlag(FLAGR, FLAGC);
        else
            System.out.println("Please enter a valid number");
    }

    public void ifFlagNo(Scanner scan){
        System.out.println("What spot do you want to click?");

        int spotClickRows = setSpotValues(scan);;
        int spotClickColumns = setSpotValues(scan);

        if (spotClickRows > 0 && spotClickRows <= Rectboard.gameBoard.length && spotClickColumns > 0 && spotClickColumns <= Rectboard.gameBoard[0].length)
            Rectboard.clickbomb(spotClickRows, spotClickColumns);
        else
            System.out.println("Please enter a valid number");
    }


    public void ifPlayAgain(Scanner scan) {
        System.out.println("Would you like to play again?");
        boolean flag = true;
        String inputFromUser = "";
        while (flag) {

            inputFromUser = scan.nextLine();
            String playAgain = inputFromUser.trim();
            String playAginToUpperCase = playAgain.toUpperCase();
            if (!inputFromUser.equals("")) {
                if (inputFromUser.charAt(0) == 'Y') {
                    flag = false;
                } else if (inputFromUser.charAt(0) == 'N') {
                    flag = false;
                    game = false;
                } else {
                    System.out.println("Please enter either 'Yes' or 'No'");
                }
            }
        }
    }
}