/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
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
      frameToMove.setLocation(
          currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
    }
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
