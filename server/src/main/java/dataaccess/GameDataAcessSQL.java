package dataaccess;

public class GameDataAcessSQL {
    //Black and White username can be null
//How do I deal with ChessGame object, use a serializer - BLOB?
    public String createGameDataTable(){
        String gameDataTable = """
                CREATE TABLE  IF NOT EXISTS GameData (
                                id INT NOT NULL AUTO_INCREMENT,
                                gameID INT NOT NULL,
                                whiteUsername VARCHAR(255),
                                blackUsername VARCHAR(255),
                                gameName VARCHAR(255) NOT NULL,
                                game ChE
                                PRIMARY KEY (id)
                            )""";
        return gameDataTable;
    }
}
