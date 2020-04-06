import java.util.ArrayList;
import java.util.Scanner;

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
    public void takeNextCardColor(){
        Scanner input=new Scanner(System.in);
        String[] colors={"Green","Blue","Yellow","Red"};
        System.out.println("Which one you Choose? ");
        int index=1;
        for(String color:colors){
            System.out.printf("%d - %s",index,color);
            index++;
        }
        nextCardColor=colors[input.nextInt()-1];
    }

    public String getNextCardColor() {
        return nextCardColor;
    }
    // action -> type comes handy
    @Override
    public boolean checkPlacingCondition(Card card) {
        return true;
    }
}
