package cs3500.klondike.model.hw02;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Represents a Build in the game of Klondike.
 * NOTE: Maintains the invariant that if this.faceUp is empty, then so is this.faceDown*/
class Build {
  private final Stack<Card> faceDown;
  private final LinkedList<Card> faceUp;

  /**
   * Creates an empty Build.
   * */
  Build() {
    this.faceDown = new Stack<>();
    this.faceUp = new LinkedList<>();
  }

  /**
   * Places a card face up at the bottom of this build while making sure that
   * it is valid. (Decreasing sequentially and alternating in color while the first card is a King).
   * NOTE: Card can not be null.
   *     @throws IllegalStateException If the card is invalid.
   */
  public void place(Card card) throws IllegalStateException {
    requireValidCard(card);
    faceUp.addFirst(card);
  }

  /**
   * Asserts that the give card is valid to continue this build.
   * (Decreasing sequentially and alternating in color while the first card is a King).
   * NOTE: Card can not be null
   *     @throws IllegalStateException If card is invalid.
   */
  public void requireValidCard(Card c) throws IllegalStateException {
    if (this.faceUp.isEmpty()) {
      if (!c.hasValue(13)) {
        throw new IllegalStateException("Must be a king first");
      }
    } else {
      if (!c.buildLegalBelow(this.faceUp.element())) {
        throw new IllegalStateException("Must be alternating colors and sequentially decreasing");
      }
    }
  }

  /**
   * Deals a card at the bottom of this build. Maintains the above invariant by flipping
   * the top visible card over and putting the new card at the bottom of the build face up.
   * NOTE: Card can not be null.
   * NOTE: Cards are placed regardless of their value and suit.
   */
  public void deal(Card card) {
    if (faceUp.isEmpty()) {
      this.faceUp.addFirst(card);
    } else {
      this.faceDown.add(this.faceUp.removeLast());
      this.faceUp.add(card);
    }
  }



  /**
  *   Moves a portion of this build to another build. Makes sure that the final build maintains
  *   correct construction.
  *   NOTE: Build can not be null.
  *      @param num The number of cards from the bottom of the build which should be moved
  *      @param build The target build which will be added to
  *      @throws IllegalArgumentException If the build number is invalid or
  *      <code> num > this.faceUp.size() </code>
  *      @throws  IllegalStateException If the move creates an invalid final build
  */
  public void movePile(int num, Build build) throws IllegalArgumentException,
          IllegalStateException {
    if (num > this.faceUp.size() || num <= 0) {
      throw new IllegalArgumentException("Invalid number of cards");
    }
    build.requireValidCard(this.faceUp.get(num - 1));
    Stack<Card> movingCards = new Stack<>();
    for (int i = 0; i < num; i++) {
      movingCards.add(this.faceUp.removeFirst());
    }
    if (!this.faceDown.isEmpty()) {
      this.faceUp.addLast(this.faceDown.pop());
    }

    for (int i = 0; i < num; i++) {
      build.place(movingCards.pop());
    }
  }

  /**
   * * Returns true if moving a portion of this build onto another is a valid move.
   *     @param num Number of cards to be moved
   *     @param build The build which cards are being moved to
   */
  public boolean validPileMove(Build build, int num) {
    if (num > this.faceUp.size() || num <= 0) {
      return false;
    }
    try {
      build.requireValidCard(this.faceUp.get(num - 1));
    } catch (IllegalStateException e) {
      return false;
    }

    return true;
  }

  /**
   * Returns the card at the bottom of this build but does not remove it.
   *     @throws IllegalStateException If the build is empty
   */
  public Card peek() throws IllegalStateException {
    if (this.faceUp.isEmpty()) {
      throw new IllegalStateException("Build empty");
    }
    return this.faceUp.peek();
  }

  /**
   * Returns the card at the bottom of this build and removes it.
   * If this exposes a face down card flip it over.
   *     @throws IllegalStateException If build is empty*/
  public Card draw() throws IllegalStateException {
    if (this.faceUp.isEmpty()) {
      throw new IllegalStateException("Build empty");
    }

    if (!this.faceDown.isEmpty()) {
      this.faceUp.addLast(this.faceDown.pop());
    }

    return this.faceUp.removeFirst();
  }

  /**
   * Returns the height of this build.*/
  public int height() {
    return this.faceUp.size() + this.faceDown.size();
  }

  /**
   * Returns true if the card at given coordinates is visible. Index 0 is at the top.
   *     @param card Index of card which is being checked
   *     @throws IllegalArgumentException If given index is not valid*/
  public boolean isCardVisible(int card) throws IllegalArgumentException {
    if (card >= this.height() || card < 0) {
      throw new IllegalArgumentException("Invalid coordinate");
    }
    return card >= this.faceDown.size();
  }


  /**
   * Returns true if the card at given coordinates is visible. Index 0 is at the top.
   *     @param card Index of card which is being checked
   *     @throws IllegalArgumentException If given index is not valid
   */
  public Card getCardAt(int card) throws IllegalArgumentException {
    if (this.isCardVisible(card)) {
      return this.faceUp.get(this.height() - card - 1);
    }
    throw new IllegalArgumentException("Card is not visible");
  }

}
