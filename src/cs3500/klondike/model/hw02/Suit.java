package cs3500.klondike.model.hw02;

/**
 * Represents a suit on a Card.
 */
public enum Suit {
 HEART("♡"), DIAMOND("♢"), CLUB("♣"), SPADE("♠");
  private String value;

  /**
   * Creates a Suit.
   *     @param value The string representation of this Suit.
   */
  Suit(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }



}
