/* 
 * John Gallagher - Towson University - 2022

 * Simple blackjack game written in Java. Blackjack pays 1:1 and player
 * wins on a tie. Dealer must hit on 16. 
 * 
 * Features to be implemented: Split, double down, surrender
 
 */
import java.util.Scanner;
import java.util.Vector;



//JAVA FINAL BLACKJACK GAME
public class BlackjackGame
{

    private static Scanner scanner = new Scanner(System.in);
    private int[] deck;   // 52 cards
    private int currentPosition; // Current position in the deck

    public static void main(String[] args)
    {
        new BlackjackGame().run();
    }

    public void run()
    {

     

        int money;          // Amount of money the user has.
        int bet;            // Amount user bets on a game.
        boolean userWins;   // Did the user win the game?
        boolean userBlackjack; //Did player get blackjack? Pays 3:2

        System.out.println("Welcome to the blackjack table.");
        System.out.println();
        System.out.println("Enter the amount of money you wish to start with: ");
        money = scanner.nextInt();  //Allow user to choose amount

        while (true)
        {
            System.out.println("You have " + money + " dollars.");
            do
            {
                System.out.println("Enter bet amount  (Enter 0 to end.)");
                System.out.print("bet: ");
                bet = scanner.nextInt();
                if (bet < 0 || bet > money)
                {
                    System.out.println("Your answer must be between 0 and " + money + '.');
                }
            } while (bet < 0 || bet > money);
            if (bet == 0)
            {
                break;
            }
            userWins = playBlackjack();
            
            if (userWins)
            {
                money = money + bet;
            }
            else
            {
                money = money - bet;
            }
            System.out.println();
            if (money == 0)
            {
                System.out.println("Player looses. Try again");
                break;
            }
        }

        System.out.println();
        System.out.println("You have left the table with $" + money + '.');

    } // end main()




    private boolean playBlackjack()
    {
        // Let the user play one game of Blackjack.
        // Return true if the user wins, false if the user loses.

        Vector dealerHand;   // The dealer's hand.
        Vector userHand;     // The user's hand.

        // Create a deck of cards.
        deck = new int[52];
        int cardCt = 0; // How many cards have been created so far.
        for (int suit = 0; suit <= 3; suit++)
        {
            for (int value = 1; value <= 13; value++)
            {
                deck[cardCt] = value;
                cardCt++;
            }
        }
        currentPosition = 0;

        dealerHand = new Vector();
        userHand = new Vector();

        /*  Shuffle the deck, then deal two cards to each player. */

        shuffle();

        dealerHand.addElement(dealCard());
        dealerHand.addElement(dealCard());
        userHand.addElement(dealCard());
        userHand.addElement(dealCard());

        System.out.println();
        System.out.println();

        // BlackJack?
         

        if (value(dealerHand) == 21)
        {
            System.out.println("Dealer has the " + showCard(getCard(dealerHand, 0)) + " and the " + showCard(getCard(dealerHand, 1)) + ".");
            System.out.println("User has the " + showCard(getCard(userHand, 0)) + " and the " + showCard(getCard(userHand, 1)) + ".");
            System.out.println();
            System.out.println("Dealer has Blackjack.  Dealer wins.");
            return false;
        }

        if (value(userHand) == 21)
        {
            System.out.println("Dealer has the " + showCard(getCard(dealerHand, 0)) + " and the " + showCard(getCard(dealerHand, 1)) + ".");
            System.out.println("User has the " + showCard(getCard(userHand, 0)) + " and the " + showCard(getCard(userHand, 1)) + ".");
            System.out.println();
            System.out.println("You have Blackjack.  You win.");
            return true;
        }

        //If nobody has blackjack, continue game

        while (true)
        {

            //Display players cards with options to hit or stay

            System.out.println();
            System.out.println();
            System.out.println("Your cards are:");
            for (int i = 0; i < userHand.size(); i++)
            {
                System.out.println("    " + showCard(getCard(userHand, i)));
            }
            System.out.println("Your total is " + value(userHand));
            System.out.println();
            System.out.println("Dealer is showing the " + showCard(getCard(dealerHand, 0)));
            System.out.println();
            System.out.print("Hit (H) or Stand (S)? ");
            char userAction;  // User's response, 'H' or 'S'.
            do
            {
                userAction = Character.toUpperCase(scanner.next().charAt(0));
                if (userAction != 'H' && userAction != 'S')
                {
                    System.out.print("Please respond H or S:  ");
                }
            } while (userAction != 'H' && userAction != 'S');

            //User decides to hit or stand

            if (userAction == 'S')
            {
                // Stand
                break;
            } else
            {   // Hit
                // Give the user a card.  If the user goes over 21, the user loses.
                int newCard = dealCard();
                userHand.addElement(newCard);
                System.out.println();
                System.out.println("User hits.");
                System.out.println("Your card is the " + showCard(newCard));
                System.out.println("Your total is now " + value(userHand));
                if (value(userHand) > 21)
                {
                    System.out.println();
                    System.out.println("You busted by going over 21.  You lose.");
                    System.out.println("Dealer's other card was the " + showCard(getCard(dealerHand, 1)));
                    return false;
                }
                
            }

        } // end while loop

        //Dealer decides to hit or stand

        System.out.println();
        System.out.println("User stands.");
        System.out.println("Dealer's cards are");
        System.out.println("    " + showCard(getCard(dealerHand, 0)));
        System.out.println("    " + showCard(getCard(dealerHand, 1)));
        while (value(dealerHand) <= 16)
        {
            int newCard = dealCard();
            System.out.println("Dealer hits and gets the " + showCard(newCard));
            dealerHand.addElement(newCard);
        }
        System.out.println("Dealer's total is " + value(dealerHand));

        //Evaluate hands

        System.out.println();
        if (value(dealerHand) > 21)
        {
            System.out.println("Dealer busted by going over 21.  You win.");
            return true;
        } else
        {
            if (value(dealerHand) == value(userHand))
            {
                System.out.println("Player wins on a tie. You win");
                return true;
            } else
            {
                if (value(dealerHand) > value(userHand))
                {
                    System.out.println("Dealer wins, " + value(dealerHand) + " points to " + value(userHand) + ".");
                    return false;
                } else
                {
                    System.out.println("You win, " + value(userHand) + " points to " + value(dealerHand) + ".");
                    return true;
                }
            }
        }

    }  //END GAME


    //Function to deal cards to player and dealer
    public int dealCard()
    {
        // Deals one card from the deck
        if (currentPosition == 52)
        {
            shuffle();
        }
        currentPosition++;
        return deck[currentPosition - 1];
    }


    //Function to shuffle the deck after every hand 
    public void shuffle()
    {
        // ReShuffle Deck
        for (int i = 51; i > 0; i--)
        {
            int rand = (int) (Math.random() * (i + 1));
            int temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        currentPosition = 0;
    }

    public int getCard(Vector hand, int position)
    {
        //Get card from hand

        if (position >= 0 && position < hand.size())
        {
            return ((Integer)hand.elementAt(position)).intValue();
        } else
        {
            return 0;
        }
    }

    public int value(Vector hand)
    {
        // Returns value of hand 

        int val;      // The value computed for the hand.
        boolean ace;  
        int cards;    // Number of cards in the hand.

        val = 0;
        ace = false;
        cards = hand.size();

        for (int i = 0; i < cards; i++)
        {
            
            int card;   
            int cardVal;  
            card = getCard(hand, i);
            cardVal = getCardValue(card);  // Card number
            if (cardVal > 10)
            {
                cardVal = 10;   // Face Cards
            }
            if (cardVal == 1)
            {
                ace = true;     // Ace
            }
            val = val + cardVal;
        }

        //Add value of hand, starting with Aces as 1, then testing to see if an Ace could 	//be 11

        if (ace == true && val + 10 <= 21)
        {
            val = val + 10;
        }

        return val;

    }
    
    
    //Function to assign value to face cards
    public int getCardValue(int card)
    {
        int result = card;
        switch (card)
        {
            case 11:
            case 12:
            case 13:
                result =  10;
        }
        return result;
    }

    //Function to display cards dealt
    public String showCard(int card)
    {
        switch (card)
        {
            case 1:
                return "Ace";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "10";
            case 11:
                return "Jack";
            case 12:
                return "Queen";
            case 13:
                return "King";
            default:
                return "??";
        }
    }
}