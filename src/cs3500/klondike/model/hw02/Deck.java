package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * Represents a Deck in the game of Klondike.
 * NOTE: Maintains the invariant that if this.visibleCards is empty,
 * then this.nonVisibleCards must also be empty
 */
class Deck {
  private final Queue<Card> visibleCards;
  private final Queue<Card> nonVisibleCards;

  /**
   * Creates a valid deck for the game of Klondike which is equivalent to a
   * "traditional" card deck with no jokers. Flips one card.
   */
  Deck() {
    this.visibleCards = new LinkedList<>();
    this.nonVisibleCards = new LinkedList<>();

    for (int i = 1; i <= 13; i++) {
      nonVisibleCards.add(new BasicCard(i, Suit.CLUB));
      nonVisibleCards.add(new BasicCard(i, Suit.DIAMOND));
      nonVisibleCards.add(new BasicCard(i, Suit.HEART));
      nonVisibleCards.add(new BasicCard(i, Suit.SPADE));
    }

    this.visibleCards.add(this.nonVisibleCards.remove());
  }

  /**
   * Creates a deck using the given list of cards and flips over a given amount.
   * The order is maintained from the original list (top card in deck is index 0 in list)
   *     @param cards The cards which should be used to make the deck
   *     @param numDraw The number of cards which should be flipped
   *     @throws IllegalArgumentException If numDraw <= 0 or deck contains null values.
   *     @throws NullPointerException If the deck is null
   */
  public Deck(List<Card> cards, int numDraw) throws IllegalArgumentException {
    Objects.requireNonNull(cards);
    if (numDraw <= 0) {
      throw new IllegalArgumentException("NumDraw must be greater than 0");
    }
    this.visibleCards = new LinkedList<>();
    this.nonVisibleCards = new LinkedList<>();
    for (Card c : cards) {
      if (c == null) {
        throw new IllegalArgumentException("Card cannot be null");
      }
      this.nonVisibleCards.add(c);
    }
    int i = 0;
    while (i < numDraw && !this.nonVisibleCards.isEmpty()) {
      this.visibleCards.add(this.nonVisibleCards.remove());
      i += 1;
    }
  }

  /**
   * Returns a List of cards representing this deck.
   * Maintains the order of the deck. (top card in deck has index 0 in list)
   */
  public List<Card> getCards() {
    List<Card> list = new ArrayList<>(0);
    for (Card c : visibleCards) {
      list.add(c);
    }

    for (Card c : nonVisibleCards) {
      list.add(c);
    }

    return list;
  }

  /**
   * Draws a card from the top of the deck and returns it.
   *     @throws IllegalStateException If the deck is empty.
   */
  public Card draw() throws IllegalStateException {
    if (this.visibleCards.isEmpty()) {
      throw new IllegalStateException("Deck is empty");
    }
    if (this.nonVisibleCards.isEmpty()) {
      return this.visibleCards.remove();
    } else {
      this.visibleCards.add(this.nonVisibleCards.remove());
      return this.visibleCards.remove();
    }

  }

  /**
   * Takes the card from the top of the deck and puts it on the bottom.
   *     @throws IllegalStateException If the deck is empty.
   */
  public void discard() throws IllegalStateException {
    this.nonVisibleCards.add(this.draw());
  }

  /**
   * Returns the top card of the deck but does not remove it from the deck.
   *     @throws IllegalStateException If the deck is empty.*/
  public Card peek() throws IllegalStateException {
    if (visibleCards.isEmpty()) {
      throw new IllegalStateException("Empty deck");
    }
    return this.visibleCards.peek();
  }

  /**
   * Returns a list of Cards which is equivalent to the visible cards on this deck.
   * The order is maintained. */
  public List<Card> getDrawCards() {
    return new ArrayList<>(this.visibleCards);
  }

}
