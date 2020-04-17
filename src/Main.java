import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner input=new Scanner(System.in);
        System.out.println("1-Play with AIs\n2-Play with other players\n3-Mixed\n choose : ");
        int gameMode;
        while(true){
            try{
                gameMode=input.nextInt();
                if(gameMode<=3 &&gameMode>=1)
                    break;
            }catch(Exception e) {
            }
            System.out.println("Please enter correct option");
        }
        //--------------------------------------------------------------
        CardStorage storage;
        PlayTable playTable=null;
        int playerNum;
        int aiNum;
        switch(gameMode){
            case 1:
                System.out.println("How many AIs do you wish to play?");
                aiNum=input.nextInt();
                input.nextLine();
                storage=new CardStorage(aiNum+1) ;
                playTable=new PlayTable(storage);
                playTable.addPlayer(new Player(1,storage,playTable));
                for(int i=2;i<=aiNum+1;i++)playTable.addPlayer(new Ai(i,storage,playTable));
                break;
            case 2:
                System.out.println("How many players want to play game?");
                playerNum=input.nextInt();
                input.nextLine();
                storage=new CardStorage(playerNum);
                playTable=new PlayTable(storage);
                for(int i=1;i<=playerNum;i++)playTable.addPlayer(new Player(i,storage,playTable));
                break;
            case 3:
                System.out.println("How many AIs will be playing ? ");
                aiNum=input.nextInt();
                System.out.println("How many human want to play?");
                playerNum=input.nextInt();
                input.nextLine();
                storage=new CardStorage(aiNum+playerNum);
                playTable=new PlayTable(storage);
                for(int i=1;i<=playerNum;i++)playTable.addPlayer(new Player(i,storage,playTable));
                for(int i=playerNum+1;i<=aiNum+playerNum;i++)playTable.addPlayer(new Ai(i,storage,playTable));
                break;

        }
        playTable.playGame();

        playTable.printScoreBoard();
    }
}
