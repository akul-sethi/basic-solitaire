package cs3500.klondike.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel model;
  private final Appendable out;
  // ... any other fields you need

  /**
   * Creates a KlondikeTextualView Object.
   *
   *     @param model The model which is being displayed by this view.
   *     @throws NullPointerException If the model provided is null.
   */
  public KlondikeTextualView(KlondikeModel model) {
    this.model = Objects.requireNonNull(model);
    this.out = new StringBuilder();
  }

  public KlondikeTextualView(KlondikeModel model, Appendable out) {
    this.model = Objects.requireNonNull(model);
    this.out = Objects.requireNonNull(out);
  }

  @Override
  public void render() throws IOException {
    this.out.append(this.toString());
  }


  /**
   * Returns a textual representation of this provided Klondike model.
   *
   *     @throws IllegalStateException If the game has not been started
   */
  public String toString() throws IllegalStateException {
    String firstLine = "Draw: " + this.model.getDrawCards().stream()
            .map(c -> c.toString()).collect(Collectors.joining(", ")) + "\n";

    ArrayList<String> list = new ArrayList<>();
    for (int i = 0; i < this.model.getNumFoundations(); i++) {
      if (this.model.getCardAt(i) == null) {
        list.add("<none>");
      } else {
        list.add(this.model.getCardAt(i).toString());
      }
    }
    String secondLine = "Foundation: " + list.stream().collect(Collectors.joining(", ")) + "\n";

    ArrayList<String> rows = new ArrayList<>();
    for (int i = 0; i < this.model.getNumRows(); i++) {
      String row = "";
      for (int j = 0; j < this.model.getNumPiles(); j++) {
        if (this.model.getPileHeight(j) == 0) {
          if (i == 0) {
            row += "  X";
          } else {
            row += "   ";
          }
        } else {
          try {
            if (this.model.isCardVisible(j, i)) {
              row += " " + this.model.getCardAt(j, i).toString();
            } else {
              row += "  ?";
            }
          } catch (IllegalArgumentException e) {
            row += "   ";
          }
        }
      }
      rows.add(row);
    }
    return firstLine + secondLine + String.join("\n", rows);
  }
}
