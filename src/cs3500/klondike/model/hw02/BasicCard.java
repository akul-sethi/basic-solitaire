package cs3500.klondike.model.hw02;

/**
 * An implementation of the Card interface which represents a traditional playing card.
 */
public class BasicCard implements Card {
  private int value;
  private Suit suit;

  /**
   * Creates a BasicCard with given numerical value and Suit.
   * Ace has value 1 and King as 13.
   *
   * @throws IllegalArgumentException If the value does not fall within the bounds.
   */
  public BasicCard(int value, Suit suit) {
    if (value < 1 || value > 13) {
      throw new IllegalArgumentException("Card value must be within 1 and 13 inclusive");
    }
    this.value = value;
    this.suit = suit;
  }

  @Override
  public boolean isRed() {
    switch (this.suit) {
      case HEART:
        return true;
      case DIAMOND:
        return true;
      case SPADE:
        return false;
      case CLUB:
        return false;
      default:
        return false;
    }
  }


  @Override
  public boolean buildLegalBelow(Card other) {
    return !other.isRed() == this.isRed()
            && other.buildLegalAbove(this.value);
  }

  @Override
  public boolean foundationLegalAbove(Card other) {
    return other.sameSuit(this.suit)
            && other.foundationLegalBelow(this.value);
  }


  @Override
  public boolean buildLegalAbove(int value) {
    return this.value - value == 1;
  }


  @Override
  public boolean foundationLegalBelow(int value) {
    return value - this.value == 1;
  }


  @Override
  public boolean sameSuit(Suit suit) {
    return this.suit == suit;
  }


  @Override
  public String toString() {
    String front = "";
    switch (this.value) {
      case 1: {
        front = "A";
        break;
      }
      case 11: {
        front = "J";
        break;
      }
      case 12: {
        front = "Q";
        break;
      }
      case 13: {
        front = "K";
        break;
      }
      default: {
        front = Integer.toString(this.value);
      }
    }
    return front + this.suit.toString();
  }

  @Override
  public boolean hasValue(int val) {
    return this.value == val;
  }


  @Override
  public boolean equals(int value, Suit suit) {
    return this.value == value && this.suit == suit;
  }

}
