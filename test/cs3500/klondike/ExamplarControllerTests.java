package cs3500.klondike;

import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.view.KlondikeTextualView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * A class for testing the KlondikeTextualController.
 * Specifically tests used for the Examplar. */
public class ExamplarControllerTests {

  private final String h = "♡";
  private final String d = "♢";
  private final String c = "♣";
  private final String s = "♠";
  private MockBasicKlondike mock;
  private BasicKlondike b;

  /**
   * Runs the given command on a mock which logs the information
   * sent from the controller to the model.
   *     @param command The command to run.
   */
  private void run(String command) {
    Readable in = new StringReader(command);
    Appendable out = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.mock, b.getDeck(), false, 6, 1);
  }


  /**
   * Creates a deck of Cards from the given strings representing cards.
   *     @param cards The cards to put in the list.
   */
  private List<Card> createDeck(String... cards) {
    List<Card> list = new ArrayList<>();
    for (String s : cards) {
      for (Card c : b.getDeck()) {
        if (c.toString().equals(s)) {
          list.add(c);
        }
      }
    }
    return list;
  }

  /**
   * Initializes global fields before each test.*/
  @Before
  public void init() {
    this.mock = new MockBasicKlondike();
    this.b = new BasicKlondike();
  }

  @Test
  public void testControllerSendMovePile() {
    run("mpp 1 1 2 q");
    Assert.assertEquals("movePile(1, 1, 2)", this.mock.log);
  }

  @Test
  public void testControllerSendMovePileInvalidInput() {
    run("mpp 1 1 random 2 q");
    Assert.assertEquals("movePile(1, 1, 2)", this.mock.log);
  }

  @Test
  public void testControllerSendMovePileInvalidInputBefore() {
    run("b b b b mpp 1 1 random 2 q");
    Assert.assertEquals("movePile(1, 1, 2)", this.mock.log);
  }




  @Test
  public void testInvalidInputMPPStateException() {
    Readable in  = new StringReader("mpp 1 1 2 q");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("2" + d, "2" + s, "A" + s, "A" + d), false, 2, 1);

    Assert.assertTrue(out.toString().contains("Invalid move"));
  }

  @Test
  public void testInputInputMDStateException() {
    Readable in  = new StringReader("md 1 q");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("2" + d, "2" + s, "A" + s, "A" + d), false, 2, 1);

    Assert.assertTrue(out.toString().contains("Invalid move"));
  }






  @Test
  public void testGameOverLost() {
    Readable in  = new StringReader("mdf 1");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    BasicKlondike b2 = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("3" + s, "2" + s, "4" + s, "A" + s), false, 2, 1);
    String expected = "Game over. Score: 1";
    String actual = out.toString().split("\n")[9];
    Assert.assertEquals(expected, actual);
  }


  @Test
  public void testSendState() {
    Readable in  = new StringReader("q");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    BasicKlondike b2 = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    KlondikeTextualView view = new KlondikeTextualView(b2);
    b2.startGame(createDeck("A" + s, "2" + s), false, 1, 1);
    String expected = view.toString();
    String[] lines = out.toString().split("\n");
    String actual = lines[0] + "\n" + lines[1] + "\n" + lines[2];
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testQuitMessage() {
    Readable in  = new StringReader("q");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    BasicKlondike b2 = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    KlondikeTextualView view = new KlondikeTextualView(b2);
    b2.startGame(createDeck("A" + s, "2" + s), false, 1, 1);
    String expected = "Game quit!\nState of game when quit:\n" + view.toString() + "\nScore: 0";
    String[] lines = out.toString().split("\n");
    String actual = lines[4] + "\n" + lines[5] + "\n" + lines[6] + "\n" + lines[7] + "\n" + lines[8]
            + "\n" + lines[9];
    Assert.assertEquals(expected, actual);
  }


  @Test
  public void testScoreAfterScoring() {
    Readable in  = new StringReader("mpf 1 1 q");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    BasicKlondike b2 = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    KlondikeTextualView view = new KlondikeTextualView(b2);
    b2.startGame(createDeck("A" + s, "2" + s), false, 1, 1);
    b2.moveToFoundation(0, 0);
    String expectedScore1 = "Score: 0";
    String expectedScore2 = "Score: 1";
    String[] lines = out.toString().split("\n");
    String actual1 = lines[3];
    String actual2 = lines[7];
    Assert.assertEquals(expectedScore1, actual1);
    Assert.assertEquals(expectedScore2, actual2);
  }

  @Test
  public void testControllerSendMoveDraw() {
    run("md 40 q");
    assertEquals("moveDraw(40)", this.mock.log);
  }







}

