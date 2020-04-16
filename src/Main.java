import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner input=new Scanner(System.in);
        System.out.println("Please enter number of players : ");
        //int numberOfPlayers=input.nextInt();
        CardStorage storage=new CardStorage(4);
        PlayTable playTable=new PlayTable(storage);
        //for(int i=1;i<=numberOfPlayers;i++)playTable.addPlayer(new Player(i,storage,playTable));
        playTable.addPlayer(new Player(1,storage,playTable));
        playTable.addPlayer(new Player(2,storage,playTable));
        playTable.addPlayer(new Ai(3,storage,playTable));
        playTable.addPlayer(new Ai(4,storage,playTable));
        playTable.playGame();

        playTable.printScoreBoard();
    }
}
