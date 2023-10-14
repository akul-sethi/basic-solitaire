package cs3500.klondike.model.hw02;

import java.util.Stack;

/**
 * Represents a Foundation in the game of Klondike.
 */
class Foundation {
  private final Stack<Card> pile;

  /**
   * Creates an empty Foundation object.
   */
  Foundation() {
    this.pile = new Stack<>();
  }

  /**
   * Asserts that the given card is valid when placed on top of the previous one.
   * (Sequentially increasing and same suit)
   * NOTE: Expects non-null input
   *
   *     @throws IllegalStateException If card is not valid on top of the previous one
   */
  public void requireValidCard(Card c) throws IllegalStateException {
    if (pile.isEmpty()) {
      if (!c.hasValue(1)) {
        throw new IllegalStateException("First card must be ace");
      }
    } else {
      if (!c.foundationLegalAbove(pile.peek())) {
        throw new IllegalStateException("Cannot place card on foundation");
      }
    }
  }

  /**
   * Places a card on the top of the pile.
   *
   *     @throws IllegalStateException If the card is not valid on top of the previous one
   */
  public void place(Card c) throws IllegalStateException {
    requireValidCard(c);
    this.pile.add(c);
  }

  /**
   * Calculates the score of this foundation by returning the value of the top card.*/
  public int score() {
    if (this.pile.isEmpty()) {
      return 0;
    }
    for (int i = 1; i <= 13; i++) {
      if (this.pile.peek().hasValue(i)) {
        return i;
      }
    }
    return 0;
  }

  /**
   * Returns the top card of this Foundation but does not remove it.
   * Returns <code>null</code> if the pile is empty. */
  public Card peek() {
    if (this.pile.isEmpty()) {
      return null;
    }
    return this.pile.peek();
  }


}
