import java.util.*;

public class CardStorage {
    private ArrayList<Card> cards;
    public CardStorage(){
        cards=new ArrayList<>();
        setInitialState();
    }
    //adding to the storage
    public void addToStorage(Card card){
        cards.add(card);
    }
    //random picking from storage
    public Card randomPicking(){
        Random randomGenerator=new Random();
        int indexOfCard=randomGenerator.nextInt(cards.size());
        Card cardChosen=cards.get(indexOfCard);
        cards.remove(indexOfCard);
        return cardChosen;
    }
    //creating first state of storage
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
