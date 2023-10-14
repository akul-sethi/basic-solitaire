package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 A basic implementation of the Klondike model which allows the user to play a game of Solitaire.
 Its getDeck method returns a traditional 52 card deck.
 */
public class BasicKlondike implements KlondikeModel {
  private Deck deck;
  private ArrayList<Foundation> foundations;
  private ArrayList<Build> builds;
  private boolean gameStarted;
  private int numDraw;
  private Deck validDeck;


  /**
   * Creates a BasicKlondike Object.
   */
  public BasicKlondike() {

    this.gameStarted = false;
    this.validDeck = new Deck();
  }

  @Override
  public List<Card> getDeck() {
    return this.validDeck.getCards();
  }

  /**
   * Throws errors if the given params are not valid for start game.*/
  private void validParams(List<Card> deck, int numPiles, int numDraw) {
    if (deck == null || deck.isEmpty()) {
      throw new IllegalArgumentException("Deck cannot be null or empty");
    }
    if (this.gameStarted) {
      throw new IllegalStateException("Game already started");
    }
    if (numPiles > (int) (Math.sqrt(8 * deck.size() + 1) - 1) / 2) {
      throw new IllegalArgumentException("Too few piles");
    }
    if (numPiles <= 0 || numDraw < 0) {
      throw new IllegalArgumentException("No negative parameters allowed");
    }

    int numHearts = 0;
    int numDiamonds = 0;
    int numSpades = 0;
    int numClubs = 0;
    int max = 0;

    for (Card c : deck) {
      if (c == null) {
        throw new IllegalArgumentException("Can not have null card in deck");
      }
      if (c.hasValue(1)) {
        if (c.sameSuit(Suit.DIAMOND)) {
          numDiamonds += 1;
        } else if (c.sameSuit(Suit.CLUB)) {
          numClubs += 1;
        } else if (c.sameSuit(Suit.HEART)) {
          numHearts += 1;
        } else {
          numSpades += 1;
        }
      }
      for (int i = 1; i <= 13; i++) {
        if (c.hasValue(i) && i > max) {
          max = i;
        }
      }
    }

    for (int i = 1; i <= 13; i++) {
      int finalI = i;
      int countHearts = (int) deck.stream().filter(c -> c.equals(finalI, Suit.HEART)).count();
      int countClubs = (int) deck.stream().filter(c -> c.equals(finalI, Suit.CLUB)).count();
      int countDiamonds = (int) deck.stream().filter(c -> c.equals(finalI, Suit.DIAMOND)).count();
      int countSpades = (int) deck.stream().filter(c -> c.equals(finalI, Suit.SPADE)).count();
      if (i > max) {
        if (countHearts + countClubs + countSpades + countDiamonds != 0) {
          throw new IllegalArgumentException("Invalid Deck");
        }
      } else if (countHearts != numHearts
              || countDiamonds != numDiamonds
              || countClubs != numClubs
              || countSpades != numSpades) {
        throw new IllegalArgumentException("Invalid Deck");
      }

    }
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {

    validParams(deck, numPiles, numDraw);
    int numAces = (int) deck.stream().filter(c -> c.hasValue(1)).count();
    List<Card> newDeck = new ArrayList<Card>();
    for (Card c : deck) {
      newDeck.add(c);
    }

    if (shuffle) {
      Collections.shuffle(newDeck);
    }

    this.deck = new Deck(newDeck, numDraw);
    this.numDraw = numDraw;
    this.builds = new ArrayList<Build>();
    this.foundations = new ArrayList<Foundation>();

    for (int i = 0; i < numAces; i++) {
      this.foundations.add(new Foundation());
    }

    for (int i = 0; i < numPiles; i++) {
      this.builds.add(new Build());
    }

    for (int i = 0; i < numPiles; i++) {
      for (int j = i; j < numPiles; j++) {
        this.builds.get(j).deal(this.deck.draw());
      }
    }

    this.gameStarted = true;
  }


  @Override
  public void movePile(int srcPile, int numCards, int destPile)
          throws IllegalStateException, IllegalArgumentException {
    requireGameStarted();
    requireValidBuild(srcPile);
    requireValidBuild(destPile);
    if (destPile == srcPile) {
      throw new IllegalArgumentException("Source and destination cannot be the same");
    }
    this.builds.get(srcPile).movePile(numCards, this.builds.get(destPile));
  }

  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    requireGameStarted();
    requireValidBuild(destPile);
    this.builds.get(destPile).requireValidCard(this.deck.peek());
    this.builds.get(destPile).place(this.deck.draw());
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
          throws IllegalStateException, IllegalArgumentException {
    requireGameStarted();
    requireValidBuild(srcPile);
    requireValidFoundation(foundationPile);
    this.foundations.get(foundationPile).requireValidCard(this.builds.get(srcPile).peek());
    this.foundations.get(foundationPile).place(this.builds.get(srcPile).draw());
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) throws IllegalStateException,
          IllegalArgumentException {
    requireGameStarted();
    requireValidFoundation(foundationPile);
    this.foundations.get(foundationPile).requireValidCard(this.deck.peek());
    this.foundations.get(foundationPile).place(this.deck.draw());
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    requireGameStarted();
    this.deck.discard();
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    requireGameStarted();
    int max = 0;
    for (Build b : this.builds) {
      if (b.height() > max) {
        max = b.height();
      }
    }
    return max;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    requireGameStarted();
    return this.builds.size();
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    requireGameStarted();
    return this.numDraw;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    requireGameStarted();

    if (!caughtException(() -> this.deck.peek())) {
      return false;
    }

    for (Foundation foundation : this.foundations) {
      for (Build build : this.builds) {
        if (!caughtException(() -> foundation.requireValidCard(build.peek()))) {
          return false;
        }
      }
    }

    for (Build src : this.builds) {
      for (Build dest : this.builds) {
        for (int i = 0; i < src.height(); i++) {
          if (src.validPileMove(dest, i)) {
            return false;
          }
        }
      }
    }

    return true;
  }


  /**
   * Returns true if the given runnable throws an exception.
   *     @param run Given Runnable*/
  private boolean caughtException(Runnable run) {
    try {
      run.run();
      return false;
    } catch (IllegalStateException | IllegalArgumentException e) {
      return true;
    }
  }

  @Override
  public int getScore() throws IllegalStateException {
    requireGameStarted();
    return this.foundations.stream().mapToInt(f -> f.score()).sum();
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalStateException, IllegalArgumentException {
    requireGameStarted();
    requireValidBuild(pileNum);
    return this.builds.get(pileNum).height();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) throws IllegalStateException,
          IllegalArgumentException {
    requireGameStarted();
    requireValidBuild(pileNum);
    return this.builds.get(pileNum).isCardVisible(card);
  }

  @Override
  public Card getCardAt(int pileNum, int card) throws IllegalStateException,
          IllegalArgumentException {
    requireGameStarted();
    requireValidBuild(pileNum);
    return this.builds.get(pileNum).getCardAt(card);
  }


  @Override
  public Card getCardAt(int foundationPile) throws IllegalStateException, IllegalArgumentException {
    requireGameStarted();
    requireValidFoundation(foundationPile);
    return this.foundations.get(foundationPile).peek();
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    requireGameStarted();
    return this.deck.getDrawCards();
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    requireGameStarted();
    return this.foundations.size();
  }

  /**
   * Requires that a valid build number was given.*/
  private void requireValidBuild(int pile) throws IllegalArgumentException {
    if (pile < 0 || pile > this.builds.size() - 1) {
      throw new IllegalArgumentException("Invalid build number");
    }
  }

  /**
   * Requires that a valid foundation pile was given. */
  private void requireValidFoundation(int pile) throws IllegalArgumentException {
    if (pile < 0 || pile > this.foundations.size() - 1) {
      throw new IllegalArgumentException("Invalid foundation number");
    }
  }

  /**
   * Requires that the game was started. */
  private void requireGameStarted() throws IllegalStateException {
    if (!this.gameStarted) {
      throw new IllegalStateException("Game has not started");
    }
  }

}
