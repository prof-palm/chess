package ui;

import server.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PreLoginUI {
    private final ServerFacade server;



    public PreLoginUI(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run(){
        System.out.println("Welcome to 240 chess. Type Help to get started.");
        System.out.print(help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_BLACK + ">>> " + SET_TEXT_COLOR_WHITE);
    }
        }













}
