import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner input=new Scanner(System.in);
        System.out.println("Please enter number of players : ");
        int numberOfPlayers=input.nextInt();
        CardStorage storage=new CardStorage(numberOfPlayers);
        PlayTable playTable=new PlayTable(storage);
        for(int i=1;i<=numberOfPlayers;i++)playTable.addPlayer(new Player(i,storage,playTable));
        playTable.playGame();

        playTable.printScoreBoard();
    }
}
