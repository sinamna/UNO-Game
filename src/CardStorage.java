import java.util.*;

public class CardStorage {
    private ArrayList<Card> cards;

    /**
     * constructs card storage with list of cards
     */
    public CardStorage(int numberOfPlayers){
        cards=new ArrayList<>();
        while(numberOfPlayers>0){
            addSetOfCard();
            numberOfPlayers-=15;
        }

    }

    /**
     * adds card to storage
     * @param card the card to be add to storage
     */
    public void addToStorage(Card card){
        cards.add(card);
    }

    /**
     * picks a random card from storage and returns it
     * @return the randomly picked card
     */
    public Card randomPicking(){
        Random randomGenerator=new Random();
        int indexOfCard=randomGenerator.nextInt(cards.size());
        Card cardChosen=cards.get(indexOfCard);
        cards.remove(indexOfCard);
        return cardChosen;
    }

    /**
     * adds the a set of 108 cards to storage
     */
    private void addSetOfCard(){
        String[] colors={"Red","Green","Blue","Yellow"};
        for(String color:colors){
            // adding numerical cards with specified color
            for(int i=0,j=1;i<10;i++,j++){
                cards.add(new Numerical(color,i));
                if(j<10) cards.add(new Numerical(color,j));
            }
            //adds a pair of each action cards with specified color
            for(int i=1;i<=2;i++){
                cards.add(new DrawCard(color));
                cards.add(new ReverseCard(color));
                cards.add(new SkipCard(color));
            }
        }
        // adds 8 wild cards
        for (int i=0;i<4;i++){
            cards.add(new WildCard("normal"));
            cards.add(new WildCard("drawFour"));
        }
    }

    /**
     * gives the number of cards available in the storage
     * @return the number of cards in storage
     */
    public int getSize(){
        return cards.size();
    }
}
