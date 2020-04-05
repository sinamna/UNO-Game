public class Card {
    private int score;
    public Card (int score){
        this.score=score;
    }

    public boolean checkPlacingCondition(Card card){
        return true;
    }
    public int getScore() {
        return score;
    }
}
