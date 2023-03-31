package main;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

import entity.Card;

public class Main {
	// Declaring the deck of cards as a static and final member
	public static final Stack<Card> deck = new Stack<>();

	// Declaring the static block to get the deck complete with all different cards
	// as soon as the class gets loaded to the memory
	static {
		// Completing all cards under Diamonds suit
		for (int i = 1; i <= 13; i++) {
			Card card = new Card("Diamonds", i);

			deck.push(card);
		}

		// Completing all cards under Spades suit
		for (int i = 1; i <= 13; i++) {
			Card card = new Card("Spades", i);

			deck.push(card);
		}

		// Completing all cards under Hearts suit
		for (int i = 1; i <= 13; i++) {
			Card card = new Card("Hearts", i);

			deck.push(card);
		}

		// Completing all cards under Clubs suit
		for (int i = 1; i <= 13; i++) {
			Card card = new Card("Clubs", i);

			deck.push(card);
		}
	}

	public static void main(String[] args) {
		// Shuffling the deck of cards before starting the game
		Collections.shuffle(deck);

		// Taking the number of players as input
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the number of players. Minimum - 2 and Maximum - 4.");
		int noOfPlayers = sc.nextInt();

		// The player count must be between 2 and 4 (both inclusive)
		if (noOfPlayers >= 2 && noOfPlayers <= 4) {
			// Hand of cards of all players in a stack of stacks
			Stack<Stack<Card>> allPlayerHands = new Stack<>();

			// Distributing cards among all the players
			for (int i = 0; i < noOfPlayers; i++) {
				Stack<Card> singlePlayerCards = new Stack<>();

				// Each player starts with a hand of 5 cards
				for (int j = 0; j < 5; j++) {
					singlePlayerCards.push(deck.pop());
				}
				allPlayerHands.push(singlePlayerCards);
			}

			// Creating the draw pile for the game
			Stack<Card> drawPile = new Stack<>();

			// Copying all the rest cards in deck after distribution, to the draw pile
			drawPile.addAll(deck);

			// Filtering the draw pile to get only the number cards except the action cards
			List<Card> onlyNumberCards = drawPile.stream().filter(card -> (card.getRank() != 1 && card.getRank() != 11
					&& card.getRank() != 12 && card.getRank() != 13)).collect(Collectors.toList());

			// Creating discard pile
			Stack<Card> discardPile = new Stack<>();

			// Adding a number card from the draw pile to the discard pile to start the game
			discardPile.push(onlyNumberCards.get(onlyNumberCards.size() - 1));

			boolean noCardsPresent = false;

			System.out.println("Which player you want to start the game from?");
			int startingPlayer = sc.nextInt();
			System.out.println();
			
			// Closing the Scanner class here
			sc.close();

			if (startingPlayer >= 1 && startingPlayer <= noOfPlayers) {
				int currPlayer = startingPlayer - 1;

				// Marking the first round to be 1
				int round = 1;

				// Loop will run till any one player runs out of cards
				while (noCardsPresent == false) {
					boolean isCardPresent = false;
					System.out.println("The suit and rank of the discard pile's top card are: "
							+ discardPile.peek().getSuit() + " and " + discardPile.peek().getRank()
							+ " respectively, for round " + (round++) + ".");

					// Searching for the availability of the required type of card in the current
					// user hand
					for (int i = 0; i < allPlayerHands.get(currPlayer).size(); i++) {
						if (allPlayerHands.get(currPlayer).get(i).getSuit().equals(discardPile.peek().getSuit())
								|| (allPlayerHands.get(currPlayer).get(i).getRank() == discardPile.peek().getRank())) {
							isCardPresent = true;
							discardPile.push(allPlayerHands.get(currPlayer).remove(i));
							currPlayer++;
							if (currPlayer == noOfPlayers) {
								currPlayer = 0;
							}
							break;
						}
					}

					// If the required type of card is not present in the current user hand
					if (isCardPresent == false) {
						if (!drawPile.isEmpty()) {
							Card drawedCard = drawPile.pop();
							if (drawedCard.getSuit().equals(discardPile.peek().getSuit())
									|| drawedCard.getRank() == discardPile.peek().getRank()) {
								discardPile.push(drawedCard);
							} else {
								allPlayerHands.get(currPlayer).push(drawedCard);
							}

							currPlayer++;
							if (currPlayer == noOfPlayers) {
								currPlayer = 0;
							}
						}

						// If the draw pile is empty for an instance
						else {
							System.out.println("Draw pile became empty. Match stopped. No one is the winner...!!!");
							break;
						}
					}

					// If current player player played all his cards and ran out of card
					if (allPlayerHands.get(currPlayer).isEmpty()) {
						noCardsPresent = true;
						System.out.println();
						System.out.println("******************************************************");
						System.out.println("No of rounds played: "+(--round));
						System.out.println();
						System.out.println("Player " + (currPlayer + 1) + " is the winner...!!!");
						System.out.println("******************************************************");
						System.out.println();
						
					}
				}
			} else {
				System.out.println("Please select a player between 1 and " + noOfPlayers
						+ ", because you have only selected " + noOfPlayers + " players for this game.");
			}

		} else {
			System.out.println("Number of players cannot be less than 2 and more than 4.");
		}
	}

}
