import java.util.ArrayList;

public class Ai extends Player {
    public Ai (int playerId,CardStorage storage,PlayTable playTable){
        super(playerId,storage,playTable);
    }
    @Override
    public Card chooseCard() {
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
        return cardToReturn;
    }
}
