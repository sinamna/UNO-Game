import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner input=new Scanner(System.in);
        CardStorage storage=new CardStorage();
        PlayTable playTable=new PlayTable(storage);
        for(int i=1;i<=4;i++)playTable.addPlayer(new Player(i,storage,playTable));
        playTable.playGame();
    }
}
