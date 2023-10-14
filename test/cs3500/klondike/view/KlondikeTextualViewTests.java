package cs3500.klondike.view;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A class for testing KlondikeTextualView objects.
 */
public class KlondikeTextualViewTests {
  @Test
  public void testRender() {
    Appendable out = new StringBuilder();
    KlondikeModel m = new BasicKlondike();
    KlondikeTextualView view = new KlondikeTextualView(m, out);
    try {
      view.render();
    } catch (IOException e) {
      return;
    }
    Assert.assertEquals(view.toString(), out.toString());
  }


}
