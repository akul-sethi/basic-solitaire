package cs3500.klondike;

import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.view.KlondikeTextualView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * A class for testing the KlondikeTextualController class which includes tests which were not in
 * the Examplar.
 */
public class ControllerTests {

  private final String h = "♡";
  private final String d = "♢";
  private final String c = "♣";
  private final String s = "♠";
  private MockBasicKlondike mock;
  private BasicKlondike b;


  private void run(String command) {
    Readable in = new StringReader(command);
    Appendable out = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.mock, b.getDeck(), false, 6, 1);
  }


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

  @Before
  public void init() {
    this.mock = new MockBasicKlondike();
    this.b = new BasicKlondike();
  }


  @Test
  public void testControllerSendMovePileQuitInMiddle() {
    run("mpp 1 1 q 1");
    Assert.assertEquals("startGame(" + this.b.getDeck().toString() + ", false, 6, 1)",
            this.mock.log);
  }

  @Test
  public void testInvalidCommandBefore() {
    Readable in = new StringReader("b mpp 1 1 2 q");
    Appendable out = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.b, this.b.getDeck(), false, 6, 2);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMPPInvalidNumCards() {
    run("mpp 1 AA 1 2 q");
    Assert.assertEquals("movePile(1, 1, 2)", this.mock.log);
  }



  @Test
  public void testNullModel() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      Readable in = new StringReader("q");
      Appendable out = new StringBuilder();
      KlondikeTextualController controller = new KlondikeTextualController(in, out);
      controller.playGame(null, b.getDeck(), false, 6, 1);
    });
  }

  @Test
  public void testCorrectMessagesAfterInvalidInput() {
    Readable in = new StringReader("b q");
    Appendable out = new StringBuilder();
    BasicKlondike b2 = new BasicKlondike();
    KlondikeTextualView view = new KlondikeTextualView(b2);
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.b, this.b.getDeck(), false, 1, 2);
    b2.startGame(b2.getDeck(), false, 1, 2);
    Assert.assertTrue(out.toString().split("\n")[4].contains("Invalid move. Play again."));
    String[] lines = out.toString().split("\n");
    String actual = lines[5] + "\n" + lines[6] + "\n" + lines[7];
    Assert.assertEquals(view.toString(), actual);
  }

  @Test
  public void gameEndsInNewLineQuit() {
    Readable in = new StringReader("q");
    Appendable out = new StringBuilder();

    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.b, this.b.getDeck(), false, 1, 2);
    int stringLength = out.toString().length();
    Assert.assertTrue(out.toString().substring(stringLength - 1, stringLength).equals("\n"));
  }

  @Test
  public void testQuitInMPPFirst() {
    Readable in = new StringReader("mpp q 1 1");
    Appendable out = new StringBuilder();

    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.b, this.b.getDeck(), false, 1, 2);
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testQuitInMPPSecond() {
    Readable in = new StringReader("mpp 1 q 1");
    Appendable out = new StringBuilder();

    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.b, this.b.getDeck(), false, 1, 2);
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testQuitInMPPThird() {
    Readable in = new StringReader("mpp 1 1 q");
    Appendable out = new StringBuilder();

    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.b, this.b.getDeck(), false, 1, 2);
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testQuitInMPPFirstQ() {
    Readable in = new StringReader("mpp Q 1 1");
    Appendable out = new StringBuilder();

    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.b, this.b.getDeck(), false, 1, 2);
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testQuitInMPPSecondQ() {
    Readable in = new StringReader("mpp 1 Q 1");
    Appendable out = new StringBuilder();

    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.b, this.b.getDeck(), false, 1, 2);
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void testQuitInMPPThirdQ() {
    Readable in = new StringReader("mpp 1 1 Q");
    Appendable out = new StringBuilder();

    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(this.b, this.b.getDeck(), false, 1, 2);
    Assert.assertTrue(out.toString().contains("Game quit!"));
  }

  @Test
  public void gameEndsInNewLineWon() {
    Readable in = new StringReader("mpf 1 1 mdf 1");
    Appendable out = new StringBuilder();

    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    int stringLength = out.toString().length();
    Assert.assertTrue(out.toString().substring(stringLength - 1, stringLength).equals("\n"));
  }

  @Test
  public void fullGameWon() {
    Readable in = new StringReader("mpf 1 1 invalid mdf 1");
    Appendable out = new StringBuilder();
    BasicKlondike b2 = new BasicKlondike();
    b2.startGame(createDeck("A" + s, "2" + s), false, 1, 1);
    KlondikeTextualView view = new KlondikeTextualView(b2);
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    String expected = view.toString() + "\n";
    b2.moveToFoundation(0, 0);
    expected += "Score: 0\n";
    expected += view.toString() + "\n";
    expected += "Score: 1\n";
    expected += "Invalid move. Play again. Input command unknown\n";
    expected += view.toString() + "\n";
    b2.moveDrawToFoundation(0);
    expected += "Score: 1\n";
    expected += view.toString() + "\n";
    expected += "You win!\n";
    Assert.assertEquals(expected, out.toString());
  }

  @Test
  public void gameEndsInNewLineLost() {
    Readable in = new StringReader("mdf 1");
    Appendable out = new StringBuilder();

    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("3" + s, "2" + s, "4" + s, "A" + s), false, 2, 1);
    int stringLength = out.toString().length();
    Assert.assertTrue(out.toString().substring(stringLength - 1, stringLength).equals("\n"));
  }

  @Test
  public void testMovePileCorrectOutputInvalidMove() {
    Readable in = new StringReader("mpf 1 2 mpf 1 1 q");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    BasicKlondike b2 = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    KlondikeTextualView view = new KlondikeTextualView(b2);
    b2.startGame(createDeck("A" + s, "2" + s), false, 1, 1);
    String expected1 = "Invalid move. Play again. ";
    String invalidMoveLine = out.toString().split("\n")[4];
    String[] words = invalidMoveLine.split(" ");
    String actual1 = words[0] + " " + words[1] + " " + words[2] + " " + words[3] + " ";
    String[] lines = out.toString().split("\n");
    String expected2 = view.toString() + "\nScore: 0\n";
    b2.moveToFoundation(0, 0);
    expected2 += view.toString() + "\nScore: 1\n";
    String actual2 = "";
    for (int i = 5; i < 13; i++) {
      actual2 += lines[i] + "\n";
    }
    Assert.assertEquals(expected1, actual1);
    Assert.assertEquals(expected2, actual2);
  }

  @Test
  public void testGameOverWin() {
    Readable in = new StringReader("mpf 1 1 mdf 1");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    String expected = "You win!";
    String actual = out.toString().split("\n")[11];
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testBrokenAppendable() {
    Readable in = new StringReader("q");
    Appendable out = new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException();
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException();
      }

      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException();
      }
    };
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalStateException.class, () -> {
      controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    });

  }

  @Test
  public void testEmptyReadable() {
    Readable in = new StringReader("");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalStateException.class, () -> {
      controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    });

  }

  @Test
  public void testBrokenReadable() {
    Readable in = new Readable() {
      @Override
      public int read(CharBuffer cb) throws IOException {
        throw new IOException();
      }
    };
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalStateException.class, () -> {
      controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    });

  }

  @Test
  public void testInvalidInputMPPArgException() {
    Readable in = new StringReader("mpp 1 2 3 q");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("2" + d, "2" + s, "A" + s, "A" + d), false, 2, 1);

    Assert.assertTrue(out.toString().contains("Invalid move"));
  }


  @Test
  public void testNullDeck() {
    Readable in = new StringReader("");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalStateException.class, () -> {
      controller.playGame(b, null, false, 1, 1);
    });

  }

  @Test
  public void testInvalidDD() {
    Readable in = new StringReader("dd q");
    Appendable out = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + h), true, 1, 1);
    Assert.assertTrue(out.toString().contains("Invalid move"));
  }

  @Test
  public void testInvalidMDF() {
    Readable in = new StringReader("mdf 1 q");
    Appendable out = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + h), true, 1, 1);
    Assert.assertTrue(out.toString().contains("Invalid move"));
  }

  @Test
  public void testValidMDF() {
    Readable in = new StringReader("mdf 1 q");
    Appendable out = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("2" + h, "A" + h), true, 1, 1);
    Assert.assertFalse(out.toString().contains("Invalid move"));
  }

  @Test
  public void testTooManyPiles() {
    Readable in = new StringReader("");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalStateException.class, () -> {
      controller.playGame(b, b.getDeck(), false, 40, 1);
    });

  }

  @Test
  public void testTooFewPiles() {
    Readable in = new StringReader("");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalStateException.class, () -> {
      controller.playGame(b, b.getDeck(), false, 0, 1);
    });

  }

  @Test
  public void testTooFewDraw() {
    Readable in = new StringReader("");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    Assert.assertThrows(IllegalStateException.class, () -> {
      controller.playGame(b, b.getDeck(), false, 1, 0);
    });

  }

  @Test
  public void testNullsInControllerConstructor() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      KlondikeTextualController controller = new KlondikeTextualController(null, null);
    });
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      KlondikeTextualController controller = new KlondikeTextualController(new StringReader(" "),
              null);
    });
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      KlondikeTextualController controller = new KlondikeTextualController(null,
              new StringBuilder());
    });
  }

  @Test
  public void sendsCorrectDataWhenGameStart() {
    Readable in = new StringReader("q");
    Appendable out = new StringBuilder();
    KlondikeTextualController controller = new KlondikeTextualController(in , out);
    List<Card> deck = createDeck("A" + this.c);
    controller.playGame(this.mock, deck, false, 1, 1);
    Assert.assertEquals("startGame(" + deck.toString() + ", false, 1, 1)", this.mock.log);
  }


  @Test
  public void testQuitMessageBigQ() {
    Readable in = new StringReader("Q");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    BasicKlondike b2 = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    KlondikeTextualView view = new KlondikeTextualView(b2);
    b2.startGame(createDeck("A" + s, "2" + s), false, 1, 1);
    String expected = "Game quit!\nState of game when quit:";
    //\nState of game when quit:\n" +  view.toString() + "\n" + "Score: 0\n";
    String[] lines = out.toString().split("\n");
    String actual = lines[4] + "\n" + lines[5];
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testScoreDefault() {
    Readable in = new StringReader("q");
    Appendable out = new StringBuilder();
    BasicKlondike b = new BasicKlondike();
    BasicKlondike b2 = new BasicKlondike();
    KlondikeTextualController controller = new KlondikeTextualController(in, out);
    controller.playGame(b, createDeck("A" + s, "2" + s), false, 1, 1);
    KlondikeTextualView view = new KlondikeTextualView(b2);
    b2.startGame(createDeck("A" + s, "2" + s), false, 1, 1);
    String expected = "Score: 0";
    String[] lines = out.toString().split("\n");
    String actual = lines[3];
    Assert.assertEquals(expected, actual);
  }


  @Test
  public void testControllerSendMoveToFoundation() {
    run("mpf 40 40 q");
    assertEquals("moveToFoundation(40, 40)", this.mock.log);
  }

  @Test
  public void testControllerSendMoveDrawToFoundation() {
    run("mdf 40 q");
    assertEquals("moveDrawToFoundation(40)", this.mock.log);
  }

  @Test
  public void testControllerSendDiscardDraw() {
    run("dd q");
    assertEquals("discardDraw()", this.mock.log);
  }


}

