package client;

public record GameInfo(Integer id, String gameName, String whiteUsername, String blackUsername) {

    public GameInfo{
        if(whiteUsername == null){
            whiteUsername = "--";

    }
        if(blackUsername == null){
            blackUsername = "--";
        }

}
}