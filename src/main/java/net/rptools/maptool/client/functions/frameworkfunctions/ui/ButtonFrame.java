package net.rptools.maptool.client.functions.frameworkfunctions.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunctionButton;

public class ButtonFrame {
  private List<ExtensionFunctionButton> functionButtons = new LinkedList<>();
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
  
  public ButtonFrame(String name, String prefixedFrameName, String prefixedFrameId, String group, ButtonFrame root) {
    this.name = name;
    this.prefixedFrameName = prefixedFrameName;
    this.prefixedFrameId = prefixedFrameId;
    this.group = group;
    this.frame = new TranslucentFrame(name, prefixedFrameName, prefixedFrameId, group, root.frame);
  }
  
  public void add(ExtensionFunctionButton functionButton) {
    String group = functionButton.getGroup();
    if (group != null && group.length()>0) {
      ButtonFrame subButtonFrame = buttonFrames.get(group);
      if (subButtonFrame == null) {
        subButtonFrame = new ButtonFrame(name, prefixedFrameName, prefixedFrameId, group, this);
        buttonFrames.put(subButtonFrame.group, subButtonFrame);
      }
      
      subButtonFrame.functionButtons.add(functionButton);
      subButtonFrame.frame.add(functionButton);
    } else {
      functionButtons.add(functionButton);
      frame.add(functionButton);
    }
  }
  
  public boolean isVisible() {
    if (frame == null) { return false; }
    return frame.isVisble();
  }
  
  public boolean show() {
    if (frame == null) { return false; }
    boolean result = !frame.isVisble();
    Runnable openInspector = new Runnable() {
      @Override
      public void run() {
        frame.show();
      }
    };
    SwingUtilities.invokeLater(openInspector);
    return result;
  }
  
  public boolean hide() {
    if (frame == null) { return false; }
    boolean result = frame.isVisble();
    Runnable openInspector = new Runnable() {
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