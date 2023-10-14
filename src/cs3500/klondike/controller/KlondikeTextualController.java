package cs3500.klondike.controller;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * An implementation of KlondikeController which uses a text based view to interact with the model.
 */
public class KlondikeTextualController implements KlondikeController {
  private final Scanner scan;
  private final Appendable out;

  /**
   * Creates a KlondikeTextualController which reads from readable and appends to appendable.
   *
   * @param r Given Readable to read from
   * @param a Given Appendable to write to
   * @throws IllegalArgumentException If r or a are <code>null</code>
   */
  public KlondikeTextualController(Readable r, Appendable a) throws IllegalArgumentException {
    if (r == null || a == null) {
      throw new IllegalArgumentException("Readable and Appendable must be non-null");
    }
    this.scan = new Scanner(r);
    this.out = a;
  }

  @Override
  public void playGame(KlondikeModel model, List<Card> deck, boolean shuffle, int numPiles,
                       int numDraw)
          throws IllegalStateException, IllegalArgumentException {

    if (model == null) {
      throw new IllegalArgumentException("Model can not be null");
    }

    try {
      model.startGame(deck, shuffle, numPiles, numDraw);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Game can not be played");
    }

    TextualView view = new KlondikeTextualView(model, this.out);

    boolean quit = false;
    try {
      do {

        write(view.toString());
        write("Score: " + model.getScore());

        if (model.isGameOver()) {
          break;
        }

        switch (this.scan.next()) {
          case "mpp": {
            int src = nextValid() - 1;
            int numCards = nextValid();
            int dest = nextValid() - 1;
            runModelMethod(() -> {
              model.movePile(src, numCards, dest);
            });
            break;
          }
          case "md": {
            int dest = nextValid() - 1;
            runModelMethod(() -> {
              model.moveDraw(dest);
            });
            break;
          }
          case "mpf": {
            int src = nextValid() - 1;
            int foundation = nextValid() - 1;
            runModelMethod(() -> {
              model.moveToFoundation(src, foundation);
            });
            break;
          }
          case "mdf": {
            int foundation = nextValid() - 1;
            runModelMethod(() -> {
              model.moveDrawToFoundation(foundation);
            });
            break;
          }
          case "dd": {
            runModelMethod(() -> {
              model.discardDraw();
            });
            break;
          }
          case "q":
          case "Q": {
            quit = true;
            break;
          }
          default: {
            write("Invalid move. Play again. Input command unknown");
            break;
          }
        }
      }
      while (!model.isGameOver() && !quit);

    } catch (QuitException e) {
      quit = true;
    } catch (NullPointerException e) {
      throw new IllegalStateException("Null pointer was thrown");
    }  catch (NoSuchElementException e) {
      throw new IllegalStateException("Nothing in readable");
    } catch (Exception e) {
      throw new IllegalStateException("Something else broke");
    }

    if (quit) {
      write("Game quit!");
      write("State of game when quit:");
      write(view.toString());
      write("Score: " + model.getScore());
    } else {
      write(view.toString());
      if (model.getScore() == deck.size()) {
        write("You win!");
      } else {
        write("Game over. Score: " + model.getScore());
      }
    }

  }


  /**
   * Writes to the Appendable and adds a new line at the end.*/
  private void write(String s) throws IllegalStateException {
    try {
      out.append(s + "\n");
    } catch (IOException e) {
      throw new IllegalStateException("IO did not work");
    }
  }

  /**
   * Used to run a command on the model and write "Invalid move. Play again. "
   * with the correct message to this controllers appendable if the model throws an error.
   *
   * @param runnable Runnable which is going to be run.
   */
  private void runModelMethod(Runnable runnable) {
    try {
      runnable.run();
    } catch (IllegalArgumentException | IllegalStateException e) {
      write("Invalid move. Play again. " + e.getLocalizedMessage());
    }
  }

  /**
   * Continues to consume the next input unless it is a number in which case it returns it. Throws a
   * QuitException if the user inputs a "q" or "Q".
   *
   * @throws QuitException If the user inputs a "q" or "Q".
   */
  private int nextValid() throws QuitException {
    while (true) {
      if (this.scan.hasNextInt()) {
        return this.scan.nextInt();
      } else {
        String nextString = this.scan.next();
        if (nextString.equals("q") || nextString.equals("Q")) {
          throw new QuitException();
        }
        write("Please enter another value");
      }
    }
  }

  /**
   * An Exception which represents when a user quits the program.
   */
  static private class QuitException extends Exception {
  }


}


