package net.rptools.maptool.client.functions.frameworkfunctions.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.Map;

import net.rptools.maptool.client.MapTool;

public class BaseComponentListener implements ComponentListener {

  public static BaseComponentListener instance = new BaseComponentListener();
  
  private TranslucentFrame chatFrame;
  private Map<Component, Container> originalParentContainer = new HashMap<Component, Container>();
  
  @Override
  public void componentResized(ComponentEvent e) {
  }

  @Override
  public void componentMoved(ComponentEvent e) {
  }

  @Override
  public void componentShown(ComponentEvent e) {
    // happens when fullscreen is closed
    if (e.getComponent().equals(MapTool.getFrame())) {
      
      // restore chat to orginal frame
      Container container = originalParentContainer.get(MapTool.getFrame().getCommandPanel());
      container.add(MapTool.getFrame().getCommandPanel());

      MapTool.getFrame().showCommandPanel();
      chatFrame.setMinimized(false);
      chatFrame.close();
      chatFrame.removeComponentListener(this);
      MapTool.getFrame().getChatActionLabel().removeComponentListener(this);
      chatFrame = null;
      originalParentContainer.clear();
    } else if (e.getComponent().equals(MapTool.getFrame().getChatActionLabel())) {
      if (chatFrame != null && chatFrame.isVisble() && !chatFrame.isMinimized()) {
        // hide chat new message notification if chat frame is visible in fullscreen
        MapTool.getFrame().getChatActionLabel().setVisible(false);
      }
    }
  }

  @Override
  public void componentHidden(ComponentEvent e) {
    
    // happens when fullscreen is activate
    if (e.getComponent().equals(MapTool.getFrame())) {
      originalParentContainer.put(MapTool.getFrame().getChatActionLabel(), MapTool.getFrame().getChatActionLabel().getParent());
      originalParentContainer.put(MapTool.getFrame().getChatTypingPanel(), MapTool.getFrame().getChatTypingPanel().getParent());
      originalParentContainer.put(MapTool.getFrame().getCommandPanel(), MapTool.getFrame().getCommandPanel().getParent());

      chatFrame = new TranslucentFrame("Chat", "Chat", "Chat",
            null,
            MapTool.getFrame().getCommandPanel()          
          );
      chatFrame.show();
      chatFrame.addComponentListener(this);
      // add this as a component listener to check the chat new message notification
      MapTool.getFrame().getChatActionLabel().addComponentListener(this);
    }
  }

}
