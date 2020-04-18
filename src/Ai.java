import java.util.ArrayList;

public class Ai extends Player {
    /**
     * constructs AI with specified fields
     * @param playerId the id of Ai
     * @param storage the storage of cards
     * @param playTable the table with players play On
     */
    public Ai (int playerId,CardStorage storage,PlayTable playTable){
        super(playerId,storage,playTable);
    }

    /**
     * chooses card
     * @return the card that Ai had chosen
     */
    @Override
    public Card chooseCard() throws InterruptedException {
         /*
         if player doesn't have card to place it will add new one from storage
         otherwise it goes throw the list and puts the first card possible to be placed
          */
        ArrayList<Card> cards=super.getCards();
        Card cardOnTable=super.getPlayTable().getCardOnTable();
        PlayTable playTable=super.getPlayTable();
        if(mustAddCard()){
            cards.add(getCardStorage().randomPicking());
            System.out.println("card added");
            return null;
        }
        Card cardToReturn=null;
        int index=1;
        for(Card card:cards){
            if(card instanceof WildCard){
                WildCard wildcard = (WildCard) card;
                if (wildcard.getType().equals("drawFour") && playTable.getWildDrawInRow() > 0)
                    {
                        cardToReturn=wildcard;
                        break;
                    }
                else if (wildcard.checkPlacingCondition(cards, cardOnTable))
                    {
                        cardToReturn=wildcard;
                        break;
                    }
            }else if(card instanceof ColoredCard){
                ColoredCard coloredCard = (ColoredCard) card;
                // handles that player choose only draw card in specific condition
                if (playTable.getDrawInRow() > 0) {
                    if (coloredCard instanceof DrawCard)
                        {
                            cardToReturn=coloredCard;
                            break;
                        }
                } else {
                    if (coloredCard.checkPlacingCondition(cardOnTable))
                    {
                        cardToReturn=coloredCard;
                        break;
                    }
                }
            }
            index++;
        }
        System.out.printf("card %d is chosen\n",index);
        Thread.sleep(1000);
        return cardToReturn;
    }
}
