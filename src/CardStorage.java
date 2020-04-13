import java.util.*;

public class CardStorage {
    private ArrayList<Card> cards;

    /**
     * constructs card storage with list of cards
     */
    public CardStorage(){
        cards=new ArrayList<>();
        setInitialState();
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
     * adds the first 108 card to storage
     */
    private void setInitialState(){
        String[] colors={"Red","Green","Blue","Yellow"};
        for(String color:colors){
            for(int i=0,j=1;i<10;i++,j++){
                cards.add(new Numerical(color,i));
                if(j<10) cards.add(new Numerical(color,j));
            }
            //this part can be summerized using above loop and %5 trick
            for(int i=1;i<=2;i++){
                cards.add(new DrawCard(color));
                cards.add(new ReverseCard(color));
                cards.add(new SkipCard(color));
            }
        }
        for (int i=0;i<4;i++){
            cards.add(new WildCard("normal"));
            cards.add(new WildCard("drawFour"));
        }
    }
    public void printStorage(){
        System.out.println(cards.size());
    }
}
