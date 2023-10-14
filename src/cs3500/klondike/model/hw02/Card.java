package cs3500.klondike.model.hw02;

/**
 * This (essentially empty) interface marks the idea of cards.  You will need to
 * implement this interface in order to use your model.
 * 
 * <p>The only behavior guaranteed by this class is its {@link Card#toString()} method,
 * which will render the card as specified in the assignment.
 * 
 * <p>In particular, you <i>do not</i> know what implementation of this interface is
 * used by the Examplar wheats and chaffs, so your tests must be defined sufficiently
 * broadly that you do not rely on any particular constructors or methods of cards.
 */
public interface Card {


  /**
   * Renders a card with its value followed by its suit as one of
   * the following symbols (♣, ♠, ♡, ♢).
   * For example, the 3 of Hearts is rendered as {@code "3♡"}.
   * @return the formatted card
   */
  String toString();

  /**
   * Returns true if this card is red (Is Diamonds or Hearts).
   */
  boolean isRed();

  /**
   * Returns true if this card can be build below another card.
   * (Sequentially decreasing and alternating in color)
   * NOTE: Given card can not be null
   *     @param other The card which this card is being built below.
   */
  boolean buildLegalBelow(Card other);

  /**
   * Returns true if this card can be put in a Foundation above another card.
   * (Sequentially increasing and same Suit)
   * NOTE: Given card can not be null
   *     @param other The card which this card is being put onto.
   */
  boolean foundationLegalAbove(Card other);

  /**
   * Returns true if this card has the given suit.
   *     @param suit Suit being compared to.
   */
  boolean sameSuit(Suit suit);

  /**
   * Returns true if the given card as the same value as this one.
   *     @param val the value of the card being compared to*/
  boolean hasValue(int val);

  /**
   * Returns true if this card can be build above another value.
   * (Sequentially increasing)
   *     @param val The value of the card which this card is being built above.
   */
  boolean buildLegalAbove(int val);

  /**
   * Returns true if this card could be in a Foundation below another value.
   * (Sequentially decreasing)
   *     @param val The value of the card which this card could be below.
   */
  boolean foundationLegalBelow(int val);

  /**
   * Returns true if this card has the same value and suit as those given.
   *     @param val The value being compared to.
   *     @param suit The suit being compared to.
   */
  boolean equals(int val, Suit suit);
}
