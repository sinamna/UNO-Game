import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static int getInput(){
        Scanner input=new Scanner(System.in);
        int returnResult;
        while(true){
            try{
                returnResult=input.nextInt();
                break;
            }catch(InputMismatchException e){
                System.out.println("Please enter number");
            }
            input.nextLine();
        }
        return returnResult;
    }
    public static void main(String[] args) throws InterruptedException {
        Scanner input=new Scanner(System.in);
        System.out.println("1-Play with AIs\n2-Play with other players\n3-Mixed\n choose : ");
        int gameMode;
        while(true){
            try{
                gameMode=input.nextInt();
               // input.nextLine();
                if(gameMode<=3 &&gameMode>=1)
                    break;
            }catch(Exception e) {
            }
            input.nextLine();
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
                aiNum=getInput();
                input.nextLine();
                storage=new CardStorage(aiNum+1) ;
                playTable=new PlayTable(storage);
                playTable.addPlayer(new Player(1,storage,playTable));
                for(int i=2;i<=aiNum+1;i++)playTable.addPlayer(new Ai(i,storage,playTable));
                break;
            case 2:
                System.out.println("How many players want to play game?");
                playerNum=getInput();
                input.nextLine();
                storage=new CardStorage(playerNum);
                playTable=new PlayTable(storage);
                for(int i=1;i<=playerNum;i++)playTable.addPlayer(new Player(i,storage,playTable));
                break;
            case 3:
                System.out.println("How many AIs will be playing ? ");
                aiNum=getInput();
                input.nextLine();
                System.out.println("How many human want to play?");
                playerNum=getInput();
                //input.nextLine();
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
