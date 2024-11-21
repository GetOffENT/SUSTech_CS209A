package application;


//这是Player类
public class Player {
//    public static int id=0;
    public String username;
    public String password;
//    public int playerNum;
    public int totalGame,wonGame;

    public Player(String username, String password, int totalGame, int wonGame) {
        this.username = username;
        this.password = password;
        this.totalGame = totalGame;
        this.wonGame = wonGame;
    }
}
