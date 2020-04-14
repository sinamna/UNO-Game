import java.util.ArrayList;
import java.util.Collections;

public class ReverseCard extends ActionCard {
    public ReverseCard(String color){
        super(color);
    }

    /**
     * checks if card can be placed on table or not
     * @param card the card which is on table
     * @return returns true if it is possible to place card on table
     */
    @Override
    public boolean checkPlacingCondition(Card card){
        if(card instanceof Numerical){
            Numerical numCard=(Numerical)card;
            return this.getColor().equals(numCard.getColor());
        }else if(card instanceof ActionCard){
            if(card instanceof ReverseCard) return true;
            else{
                ActionCard actionCard=(ActionCard) card;
                return actionCard.getColor().equals(this.getColor());
            }

        }else if(card instanceof WildCard){
            WildCard wildCard=(WildCard) card;
            return wildCard.getNextCardColor()==null?true:wildCard.getNextCardColor().equals(this.getColor());
        }
        return false;
    }

    /**
     * prints the card table
     */
//    @Override
//    public void print() {
//        System.out.println(this.getColor()+" Reverse card");
//    }

    /**
     * reverse the game playing turns
     * @param playerIndex the index of player in the players list of game table
     * @param players the list of players of game table
     */
    @Override
    public void action(Integer playerIndex, ArrayList<Player> players){
        String resetColor = "\u001B[0m";
        String textColor = "\u001B[96m";
        System.out.println(textColor+"Play order reversed "+resetColor);
        Player tempPlayer=players.get(playerIndex);
        Collections.reverse(players);
        playerIndex=players.indexOf(tempPlayer);
        players.get((playerIndex+1)%players.size()).setPlayTurn(true);
    }

    /**
     * reverse the game playing turns if it hits the first player who starts the game
     * @param playerIndex the index of player in the players list
     * @param players the list of player of game table
     */
    @Override
    public void firstAct(Integer playerIndex, ArrayList<Player> players) {
        String resetColor = "\u001B[0m";
        String textColor = "\u001B[96m";
        System.out.println(textColor+"Play order reversed "+resetColor);
        players.get(playerIndex).setPlayTurn(false);
        Player tempPlayer=players.get(playerIndex);
        Collections.reverse(players);
        playerIndex=players.indexOf(tempPlayer);
        players.get((playerIndex+1)%players.size()).setPlayTurn(true);
    }
}
