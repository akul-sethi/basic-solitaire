package cs3500.klondike;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Suit;

/**
 * A mock of the Card interface to be used in a mock implementation of the KlondikeModel.
 */
public class MockCard implements Card {
  @Override
  public String toString() {
    return "I am card";
  }

  @Override
  public boolean isRed() {
    return false;
  }

  @Override
  public boolean buildLegalBelow(Card other) {
    return false;
  }

  @Override
  public boolean foundationLegalAbove(Card other) {
    return false;
  }

  @Override
  public boolean sameSuit(Suit suit) {
    return false;
  }

  @Override
  public boolean hasValue(int val) {
    return false;
  }

  @Override
  public boolean buildLegalAbove(int val) {
    return false;
  }

  @Override
  public boolean foundationLegalBelow(int val) {
    return false;
  }

  @Override
  public boolean equals(int val, Suit suit) {
    return false;
  }
}
