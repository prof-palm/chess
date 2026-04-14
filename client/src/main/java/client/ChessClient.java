package client;

import chess.*;
import exceptions.ResponseException;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.ListGamesResult;
import results.LoginResult;
import results.RegisterResult;
import ui.ChessBoardUI;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;

import static java.util.Map.entry;
import static ui.EscapeSequences.*;

public class ChessClient implements MessageHandler {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken = null;
    private ArrayList<GameInfo> globalList = new ArrayList<>();
    private HashMap<Integer, Integer> idMapper = new HashMap<>();
    private ChessBoardUI boardUI = new ChessBoardUI(new ChessGame());
    private Integer gameID = null;
    private final Map<String, Integer> stringToInteger = Map.ofEntries(
            entry("a", 1),
            entry("b", 2),
            entry("c", 3),
            entry("d", 4),
            entry("e", 5),
            entry("f", 6),
            entry("g", 7),
            entry("h", 8)
    );




    public ChessClient(String url) throws ResponseException {
        server = new ServerFacade(url);
    }

    //need to make methods for each of these.
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(message);
            case ERROR -> displayError(message);
            case LOAD_GAME -> loadGame(message);
        }
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
                if(state == State.WHITEPLAYER || state == State.BLACKPLAYER || state == State.OBSERVER){
                    evalGame(line);
                }
                else{
                    result = eval(line);
                }
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_GREEN + ">>> " + SET_TEXT_COLOR_WHITE);
    }

    public String eval(String input) {
        try {
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
                case "quit" ->  "quit";
                 case "help" -> help();
                default -> help();
            };
        } catch (ResponseException | IOException ex) {
            return ex.getMessage();
        }
    }

    public void evalGame(String input) throws ResponseException {
        try{
        String[] tokens = input.split(" ");
        tokens[0] = tokens[0].toLowerCase();
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        switch(cmd) {
            case "resign" -> resign();
            case "makeMove" -> makeMove(params);
            case "leave" -> leave();
            case "redrawBoard" -> redrawBoard();
            case "highlightMoves" -> highlight(params);
            case "help" -> help();
        }
        }catch (ResponseException ex) {
            throw new ResponseException(ResponseException.Code.ClientError, ex.getMessage());
        }
    }


//how does the display message get handled
//all these method need a username and gameID passed in within my serverFacade
    //how do I pass in the ServerMessage?
    public void resign() throws ResponseException {
        assertInGame();
        try{server.resign(authToken, gameID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //handle poor inputs, handle observer
    public void makeMove(String... params) throws ResponseException {
        assertInGame();
        try{
        if(params.length == 5){
            int startCol;
            int endCol;
            if(stringToInteger.containsKey(params[0].toLowerCase()) && stringToInteger.containsKey(params[2])){
                 startCol = stringToInteger.get(params[0].toLowerCase());
                 endCol = stringToInteger.get(params[2].toLowerCase());
            }
            else{
                throw new ResponseException(ResponseException.Code.ClientError, "columns must be string value a-h");

            }

        int startRow = Integer.parseInt(params[1]);
        int endRow = Integer.parseInt(params[3]);
        ChessPosition startPosition = new ChessPosition(startRow, startCol);
        ChessPosition endPosition = new ChessPosition(endRow, endCol);

        ChessPiece.PieceType piece = switch(params[4].toLowerCase()){
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> throw new ResponseException(ResponseException.Code.ClientError, "Enter valid promotion piece");

        };
        ChessMove move = new ChessMove(startPosition, endPosition, piece);
        try {
            server.makeMove(authToken, gameID, move);
        } catch (IOException e) {
            throw new ResponseException(ResponseException.Code.ServerError, "AAHHH!");
        }
        }
        else{
            throw new ResponseException(ResponseException.Code.ClientError, "Expected: START POSITION(<column> <row>) END POSITION (<letter number>) PROMOTION <pieceType>");

        }
        }catch(NumberFormatException exe){
            throw new ResponseException(ResponseException.Code.ClientError, "row entries must be integer values 1-8");

        }


    }


    public void leave() throws ResponseException {
        assertInGame();
        try {
            server.leave(authToken, gameID);
        } catch (IOException e) {
            throw new ResponseException(ResponseException.Code.ServerError, "AAHHH!");
        }
        gameID = null;
        state = State.SIGNEDIN;
        boardUI = new ChessBoardUI(new ChessGame());


    }

    public void redrawBoard() throws ResponseException {
        assertInGame();
        boardUI.printBoard(state);


    }

    public void highlight(String... params)throws ResponseException{
        assertInGame();


    }


    private String observeGame(String... params) throws ResponseException, IOException {
        assertSignedIn();
        if(params.length == 1){
            if(idMapper.containsKey(Integer.valueOf(params[0]))){
            boardUI.printBoard(state);
                server.connectToServer();
                gameID = idMapper.get(Integer.valueOf(params[0]));
                server.connectToGame(authToken, idMapper.get(Integer.valueOf(params[0])));
                state = State.OBSERVER;
                return " ";

            }
            else{
                throw new ResponseException(ResponseException.Code.ClientError, "Enter valid game ID");
            }
        }
        else{
            throw new ResponseException(ResponseException.Code.ClientError, "Expected <ID>");
        }
    }

    public String register(String... params)throws ResponseException{
        if(params.length == 3 ) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterResult result = server.register(new RegisterRequest(username, password, email));
            authToken = result.authToken();
            state = State.SIGNEDIN;
            return String.format("you have registered as %s", result.username());

        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <USERNAME> <PASSWORD> <EMAIL>");

    }
    public String login(String... params)throws ResponseException{
        if(params.length == 2){
        String username = params[0];
        String password = params[1];
        LoginResult result = server.login(new LoginRequest(username, password));
        authToken = result.authToken();
            state = State.SIGNEDIN;
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
        return "game created";
        }

        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <NAME>");
    }

    public String joinGame(String... params)throws ResponseException{
        assertSignedIn();
        if(params.length == 2 && (params[1].equals("WHITE") || params[1].equals("BLACK"))){
        try{
        server.joinGame(new JoinGameRequest(params[1], idMapper.get(Integer.valueOf(params[0]))), authToken);
        if(params[1].equals("WHITE")){
            state = State.WHITEPLAYER;
        }
        else{
            state = State.BLACKPLAYER;
        }
        boardUI.printBoard(state);
        server.connectToServer();
        gameID = idMapper.get(Integer.valueOf(params[0]));
        //need to check if the gameID is actually in the database
        server.connectToGame(authToken, gameID);
        state = State.WHITEPLAYER;


        return String.format("You have joined %s game as %s", params[0], params[1]);
        }

        catch(NumberFormatException ex){
            throw new ResponseException(ResponseException.Code.ClientError, "ID must be integer value");
            }
        catch (IOException e) {
            throw new ResponseException(ResponseException.Code.ServerError, "IO exception");
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
        StringBuilder builder = new StringBuilder(200);
        for(GameData game : list){
            globalList.add(new GameInfo(i, game.gameName(), game.whiteUsername(), game.blackUsername()));
            idMapper.put(i, game.gameID());
            i+=1;
        }
        for(GameInfo info : globalList){
             String resultString = String.format("Game id: %d Name: %s White: %s Black: %s \n", info.id(), info.gameName(),
                    info.whiteUsername(), info.blackUsername());
             builder.append(resultString);

        }

        return builder.toString();
    }

    public void displayNotification(ServerMessage message){
        System.out.print(message.getMessage());

    }
    public void displayError(ServerMessage message){
        System.out.print(message.getMessage());
    }
    public void loadGame(ServerMessage message){
        ChessGame game = message.getGame();
        boardUI = new ChessBoardUI(game);
        boardUI.printBoard(state);

    }




    public String help() {
        if(state == State.SIGNEDOUT){
            return """
                        register <USERNAME> <PASSWORD> <EMAIL>
                        login <USERNAME> <PASSWORD>
                        quit
                        help
                        
                        """;}
        if(state == State.WHITEPLAYER || state == State.OBSERVER || state == State.BLACKPLAYER){
            return """
                    help
                    redraw chessboard
                    leave
                    makeMove START POSITION(<column> <row>) END POSITION (<letter number>) PROMOTION <pieceType>
                    resign
                    highlight moves
                    """;

        }
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
    private void assertInGame() throws ResponseException {
        if (state != State.WHITEPLAYER ||  state != State.BLACKPLAYER) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must join a game");
        }
    }
    private void assertObserver()throws ResponseException{
        if (state == State.OBSERVER) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must join game as player");
        }
    }


}