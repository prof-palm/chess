package ui;

import Exceptions.ResponseException;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.ListGamesResult;
import results.LoginResult;
import results.RegisterResult;
import server.ServerFacade;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ui.EscapeSequences.*;

public class UserInterface {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken = null;
    private ArrayList<GameInfoDisplay> globalList = new ArrayList<>();
    private HashMap<Integer, Integer> idMapper = new HashMap<>();




    public UserInterface(String url) {
        server = new ServerFacade(url);
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

    public String eval(String input) {
        try {
            //I only want the initial token to be lowercase, so I have to change that.
            String[] tokens = input.split(" ");
            tokens[0] = tokens[0].toLowerCase();
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "list" -> listGames();
                case "quit" -> "quit";
                case "help" -> help();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String observeGame(String... params)throws ResponseException {
        assertSignedIn();
        if(params.length == 1){
            if(idMapper.containsKey(Integer.valueOf(params[0])));
            printboard();
        }



    }

    //possible errors - invalid number of arguments, I will also, return exceptions kind of
    public String register(String... params)throws ResponseException{
        if(params.length == 3 ) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterResult result = server.register(new RegisterRequest(username, password, email));
            authToken = result.authToken();
            return String.format("you have registered as %s", result.username());

        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <USERNAME> <PASSWORD> <EMAIL>");



    }
    //possible errors - invalid number of arguments, unauthorized,
    public String login(String... params)throws ResponseException{
        if(params.length == 2){
        String username = params[0];
        String password = params[1];
        LoginResult result = server.login(new LoginRequest(username, password));
        authToken = result.authToken();
        return String.format("you have signed in as %s", username);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <USERNAME> <PASSWORD>");
    }

    public String logout()throws ResponseException{
        assertSignedIn();
        state = State.SIGNEDOUT;
        authToken = null;
        return "you have signed out";
    }

    public String createGame(String... params)throws ResponseException{
        assertSignedIn();
        if(params.length == 1){
        server.createGame(new CreateGameRequest(params[0]), authToken);
        return "game created";}

        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <NAME>");
    }

    public String joinGame(String... params)throws ResponseException{
        assertSignedIn();
        //UI id
        //what if the ID entered is not an integer, handle that case
        if(params.length == 2 && (params[1].equals("WHITE") || params[1].equals("BLACK"))){
        try{
        server.joinGame(new JoinGameRequest(params[0], idMapper.get(Integer.valueOf(params[1]))), authToken);
        //code that displays chessboard
        return String.format("You have joined %s game as %s", params[0], params[1]);
        }
        catch(NumberFormatException ex){
            throw new ResponseException(ResponseException.Code.ClientError, "ID must be integer value");
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <ID> <WHITE|BLACK>");

    }

    public String listGames()throws ResponseException{
        globalList.clear();
        idMapper.clear();
        assertSignedIn();
        ListGamesResult object = server.listGame(authToken);
        Collection<GameData> list = object.games();

        int i = 1;
        //so now I have a list of all the data, but I only want to print the gameName, and players, so I am going to add another method that iterates and creates a new list
        for(GameData game : list){
            globalList.add(new GameInfoDisplay(i, game.gameName(), game.whiteUsername(), game.blackUsername()));
            idMapper.put(i, game.gameID());
            i+=1;
        }
        //need to add for the case that the usernames are null, and handle those by providing a mapping opt
        //need to ensure that the UI game ID is stored so that people Users can use it to join that game.
        String result = globalList.stream()
                .flatMap(gameInfoDisplay -> gameInfoDisplay == null
                ? Stream.of("NoPlayer")
                : Stream.of(String.valueOf(gameInfoDisplay.id()), gameInfoDisplay.gameName(), gameInfoDisplay.whiteUsername(), gameInfoDisplay.blackUsername()))
                .collect(Collectors.joining(",", "GameInfo: [","]"));
        return result;
    }






    public String help() {
        if(state == State.SIGNEDOUT){
            return """
                        register <USERNAME> <PASSWORD> <EMAIL>
                        login <USERNAME> <PASSWORD>
                        quit
                        help
                        
                        """;}
            return """
                    create <NAME>
                    list
                    join <ID> <WHITE|BLACK>
                    observe <ID>
                    logout
                    quit
                    help
                    """;
        }





    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }
}









