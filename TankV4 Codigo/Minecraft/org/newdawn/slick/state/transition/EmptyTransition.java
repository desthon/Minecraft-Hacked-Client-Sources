package org.newdawn.slick.state.transition;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class EmptyTransition implements Transition {
   public boolean isComplete() {
      return true;
   }

   public void postRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
   }

   public void preRender(StateBasedGame var1, GameContainer var2, Graphics var3) throws SlickException {
   }

   public void update(StateBasedGame var1, GameContainer var2, int var3) throws SlickException {
   }

   public void init(GameState var1, GameState var2) {
   }
}
