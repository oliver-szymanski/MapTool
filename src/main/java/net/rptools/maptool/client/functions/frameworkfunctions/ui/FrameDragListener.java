package net.rptools.maptool.client.functions.frameworkfunctions.ui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class FrameDragListener extends MouseAdapter {

  private final JFrame frameToMove;
  
  private boolean enabled;
  private Point mouseDownCompCoords = null;

  public FrameDragListener(JFrame frameToMove) {
      this.frameToMove = frameToMove;
      this.enabled = true;
  }

  public void mouseReleased(MouseEvent e) {
      mouseDownCompCoords = null;
  }

  public void mousePressed(MouseEvent e) {
      mouseDownCompCoords = e.getPoint();
  }

  public void mouseDragged(MouseEvent e) {
    if (enabled) {
      Point currCoords = e.getLocationOnScreen();
      frameToMove.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
    }
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
   
}