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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunctionButton;

public class ButtonFrame {
  private List<ExtensionFunctionButton> functionButtons = new LinkedList<>();
  private Map<String, ExtensionFunctionButton> functionButtonsMap = new HashMap<>();
  private Map<String, ButtonFrame> buttonFrames = new HashMap<>();;
  private TranslucentFrame frame;

  private String name;
  private String group;
  private String prefixedFrameName;
  private String prefixedFrameId;

  public ButtonFrame(String name, String prefixedFrameName, String prefixedFrameId) {
    this.name = name;
    this.prefixedFrameName = prefixedFrameName;
    this.prefixedFrameId = prefixedFrameId;
    this.frame = new TranslucentFrame(name, prefixedFrameName, prefixedFrameId);
  }

  public ButtonFrame(
      String name,
      String prefixedFrameName,
      String prefixedFrameId,
      String group,
      ButtonFrame root) {
    this.name = name;
    this.prefixedFrameName = prefixedFrameName;
    this.prefixedFrameId = prefixedFrameId;
    this.group = group;
    this.frame = new TranslucentFrame(name, prefixedFrameName, prefixedFrameId, group, root.frame);
  }

  public boolean remove(ExtensionFunctionButton functionButton) {
    if (functionButton.getGroup() != null && functionButton.getGroup().length()>0 && this.group == null) {
      ButtonFrame subFrame = buttonFrames.get(functionButton.getGroup());
      subFrame.frame.remove(functionButton);
      subFrame.functionButtonsMap.remove(functionButton.getName());
      return subFrame.functionButtons.remove(functionButton);
    } else {
      frame.remove(functionButton);
      functionButtonsMap.remove(functionButton.getName());
      return functionButtons.remove(functionButton);
    }
  }

  public void enable(ExtensionFunctionButton functionButton) {
    TranslucentFrame frame = this.frame;
    if (functionButton.getGroup() != null) {
      frame = buttonFrames.get(functionButton.getGroup()).frame;
    }
    frame.enable(functionButton);
  }

  public void disable(ExtensionFunctionButton functionButton) {
    TranslucentFrame frame = this.frame;
    if (functionButton.getGroup() != null) {
      frame = buttonFrames.get(functionButton.getGroup()).frame;
    }
    frame.disable(functionButton);
  }

  public boolean isEnabled(ExtensionFunctionButton functionButton) {
    TranslucentFrame frame = this.frame;
    if (functionButton.getGroup() != null) {
      frame = buttonFrames.get(functionButton.getGroup()).frame;
    }
    return frame.isEnabled(functionButton);
  }

  public void hide(ExtensionFunctionButton functionButton) {
    TranslucentFrame frame = this.frame;
    if (functionButton.getGroup() != null) {
      frame = buttonFrames.get(functionButton.getGroup()).frame;
    }
    frame.hide(functionButton);
  }

  public void show(ExtensionFunctionButton functionButton) {
    TranslucentFrame frame = this.frame;
    if (functionButton.getGroup() != null) {
      frame = buttonFrames.get(functionButton.getGroup()).frame;
    }
    frame.show(functionButton);
  }

  public boolean isHidden(ExtensionFunctionButton functionButton) {
    TranslucentFrame frame = this.frame;
    if (functionButton.getGroup() != null) {
      frame = buttonFrames.get(functionButton.getGroup()).frame;
    }
    return frame.isHidden(functionButton);
  }

  public void add(ExtensionFunctionButton functionButton) {
    String group = functionButton.getGroup();
    if (group != null && group.length() > 0) {
      ButtonFrame subButtonFrame = buttonFrames.get(group);
      if (subButtonFrame == null) {
        subButtonFrame = new ButtonFrame(name, prefixedFrameName, prefixedFrameId, group, this);
        buttonFrames.put(subButtonFrame.group, subButtonFrame);
      }

      if (subButtonFrame.functionButtonsMap.containsKey(functionButton.getName())) {
        // remove to override if that name already exists
        subButtonFrame.remove(functionButton);
      }
      subButtonFrame.functionButtons.add(functionButton);
      subButtonFrame.functionButtonsMap.put(functionButton.getName(), functionButton);
      subButtonFrame.frame.add(functionButton);
    } else {
      if (functionButtonsMap.containsKey(functionButton.getName())) {
        // remove to override if that name already exists
        remove(functionButton);
      }
      functionButtons.add(functionButton);
      functionButtonsMap.put(functionButton.getName(), functionButton);
      frame.add(functionButton);
    }
  }

  public ExtensionFunctionButton getExtensionFunctionButton(String name, String group) {
    if (group == null) {
      return functionButtonsMap.get(name);
    } else {
      ButtonFrame subButtonFrame = buttonFrames.get(group);
      if (subButtonFrame == null) {
        return null;
      }
      return subButtonFrame.functionButtonsMap.get(name);
    }
  }

  public boolean isVisible() {
    if (frame == null) {
      return false;
    }
    return frame.isVisble();
  }

  public boolean show() {
    if (frame == null) {
      return false;
    }
    boolean result = !frame.isVisble();
    Runnable openInspector =
        new Runnable() {
          @Override
          public void run() {
            frame.show();
          }
        };
    SwingUtilities.invokeLater(openInspector);
    return result;
  }

  public boolean hide() {
    if (frame == null) {
      return false;
    }
    boolean result = frame.isVisble();
    Runnable openInspector =
        new Runnable() {
          @Override
          public void run() {
            frame.hide();
          }
        };
    SwingUtilities.invokeLater(openInspector);
    return result;
  }

  public void clear() {
    functionButtons.clear();
    buttonFrames.clear();
    frame.close();
  }
}
