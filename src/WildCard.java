public class WildCard extends Card {
    private String color;
    //it can be normal normal card or DrawFour
    private String type;
    private String nextCardColor;

    public WildCard(String type) {
        super(50);
        color = "Black";
        nextCardColor = null;
    }
    // a method to take user input for choosing nextCardColor


    public String getNextCardColor() {
        return nextCardColor;
    }

    @Override
    public boolean checkPlacingCondition(Card card) {
        return true;
    }
}
