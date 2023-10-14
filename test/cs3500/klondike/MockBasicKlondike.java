package cs3500.klondike;

import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A class to log inputs to a KlondikeModel from a controller.
 */
public class MockBasicKlondike implements KlondikeModel {
  String log;

  /**
   * Creates a new MockBasicKlondike with an empty log.
   */
  MockBasicKlondike() {
    this.log = "";
  }

  @Override
  public List<Card> getDeck() {
    return null;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {
    this.log = String.format("startGame(" + deck.toString() + ", %b, %d, %d)", shuffle, numPiles,
            numDraw);
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) throws IllegalArgumentException,
          IllegalStateException {
    this.log = String.format("movePile(%d, %d, %d)", srcPile + 1, numCards, destPile + 1);
  }


  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    this.log = String.format("moveDraw(%d)", destPile + 1);
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) throws IllegalArgumentException,
          IllegalStateException {
    this.log = String.format("moveToFoundation(%d, %d)", srcPile + 1, foundationPile + 1);
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) throws IllegalArgumentException,
          IllegalStateException {
    this.log = String.format("moveDrawToFoundation(%d)", foundationPile + 1);
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    this.log = "discardDraw()";
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    return 6;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    return 6;
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    return 1;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    return false;
  }

  @Override
  public int getScore() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    return pileNum + 1;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) throws IllegalArgumentException,
          IllegalStateException {
    return false;
  }

  @Override
  public Card getCardAt(int pileNum, int card) throws IllegalArgumentException,
          IllegalStateException {
    throw new IllegalArgumentException("Not visible");
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    return List.of(new MockCard());
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    return 4;
  }
}
