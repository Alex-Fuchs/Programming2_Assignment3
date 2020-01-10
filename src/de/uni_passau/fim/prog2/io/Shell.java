package de.uni_passau.fim.prog2.io;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * {@code Shell} ist eine Utilityklasse, die den Input in der Konsole und
 * somit alle Kommandos des Nutzers verarbeitet und diese darauf an
 * {@code ShellToBoard} weiterleitet. Dort werden auch, falls vorhanden, die
 * Parameter geprüft. Falls zu viele Parameter angehängt werden, wird ein
 * Fehler in der Konsole ausgegeben. Zudem kann statt den Kommandos der Form
 * "c" auch ein Wort mit den Anfangsbuchstaben c geschrieben werden.
 *
 * @version 21.12.19
 * @author ------
 */
public final class Shell {

    private Shell() { }

    /**
     * Startet das Programm.
     *
     * @param   args            Entspricht der Startübergabe des Programms.
     * @throws  IOException     Falls I/O Probleme bei dem Benutzer bestehen,
     *                          wird die {@code IOException} zur JVM
     *                          weitergeleitet und das Programm wird beendet.
     * @see                     #execute(BufferedReader)
     */
    public static void main(String[] args) throws IOException {
        BufferedReader stdin
                = new BufferedReader(new InputStreamReader(System.in));
        execute(stdin);
    }

    /**
     * Liest Befehle inkl Parameter des Benutzers ein, reagiert auf Befehle
     * mit einer Fehlerausgabe oder leitet diese ggf mit Parameter weiter.
     * Die Parameter werden erst nach Weiterleitung geprüft. Statt den
     * Kommandos "c" werden auch Wörter mit dem Anfangsbuchstaben c
     * akzeptiert. Bei zu vielen Parametern wird dies in der Konsole
     * ausgegeben.
     *
     * @param   stdin           Wird zum Lesen der Kommandos benötigt.
     * @throws  IOException     Falls I/O Probleme bei dem Benutzer bestehen,
     *                          wird eine {@code IOException} zu
     *                          {@code main} geleitet.
     * @see                     ShellToBoard
     */
    private static void execute(BufferedReader stdin) throws IOException {
        final String prompt = "othello> ";
        boolean quit = false;
        while (!quit) {
            System.out.print(prompt);
            String input = stdin.readLine();
            if (input == null) {
                break;
            }
            String[] tokens = input.trim().split("\\s+");
            tokens[0] = tokens[0].toLowerCase();

            if (!tokens[0].isEmpty()) {
                switch (tokens[0].charAt(0)) {
                case 'n':
                    final int parameterNumberNew = 0;
                    if (tokens.length == parameterNumberNew + 1) {
                        ShellToBoard.createNewBoard();
                        ShellToBoard.machineMove();
                    } else {
                        ShellToBoard.printError("Too much parameters!");
                    }
                    break;
                case 'h':
                    final int parameterNumberHelp = 0;
                    if (tokens.length == parameterNumberHelp + 1) {
                        ShellToBoard.help();
                    } else {
                        ShellToBoard.printError("Too much parameters!");
                    }
                    break;
                case 'q':
                    final int parameterNumberQuit = 0;
                    if (tokens.length == parameterNumberQuit + 1) {
                        quit = true;
                    } else {
                        ShellToBoard.printError("Too much parameters!");
                    }
                    break;
                case 'm':
                    final int parameterNumberMove = 2;
                    if (tokens.length == parameterNumberMove + 1) {
                        String[] parametersForMove = {tokens[1], tokens[2]};
                        ShellToBoard.move(parametersForMove);
                        ShellToBoard.machineMove();
                    } else {
                        ShellToBoard.printError("Too much or"
                                + " not enough parameters!");
                    }
                    break;
                case 'l':
                    final int parameterNumberLevel = 1;
                    if (tokens.length == parameterNumberLevel + 1) {
                        String[] parametersForLevel = {tokens[1]};
                        ShellToBoard.setLevel(parametersForLevel);
                    } else {
                        ShellToBoard.printError("Too much or"
                                + " not enough parameter!");
                    }
                    break;
                case 's':
                    final int parameterNumberSwitch = 0;
                    if (tokens.length == parameterNumberSwitch + 1) {
                        ShellToBoard.switchPlayerOrder();
                        ShellToBoard.machineMove();
                    } else {
                        ShellToBoard.printError("Too much parameters!");
                    }
                    break;
                case 'p':
                    final int parameterNumberPrint = 0;
                    if (tokens.length == parameterNumberPrint + 1) {
                        ShellToBoard.print();
                    } else {
                        ShellToBoard.printError("Too much parameters!");
                    }
                    break;
                default:
                    ShellToBoard.printError("Type help for overview!");
                    break;
                }
            } else {
                ShellToBoard.printError("Please enter a command!");
            }
        }
    }
}

