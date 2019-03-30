package net.rptools.maptool.client.functions.frameworkfunctions.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.rptools.lib.swing.PositionalLayout;
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
    if (e.getComponent().equals(MapTool.getFrame())) {
      for(Entry<Component, Container> entry : originalParentContainer.entrySet()) {
        Component component = entry.getKey();
        Container container = entry.getValue();
        //component.getParent().remove(component);
        if (component.equals(MapTool.getFrame().getChatActionLabel())) {
          container.add(component, PositionalLayout.Position.SW);
        } else  if (component.equals(MapTool.getFrame().getChatTypingPanel())) {
          container.add(component, PositionalLayout.Position.NW);
        } else  if (component.equals(MapTool.getFrame().getCommandPanel())) {
          container.add(component);
        } else {
          container.add(component);
        }
//        component.setVisible(true);
  //      component.invalidate();
    //    container.setVisible(true);
      //  container.invalidate();
      }
      originalParentContainer.clear();
      MapTool.getFrame().showCommandPanel();
      chatFrame.close();
    }
  }

  @Override
  public void componentHidden(ComponentEvent e) {
    if (e.getComponent().equals(MapTool.getFrame())) {
      originalParentContainer.put(MapTool.getFrame().getChatActionLabel(), MapTool.getFrame().getChatActionLabel().getParent());
      originalParentContainer.put(MapTool.getFrame().getChatTypingPanel(), MapTool.getFrame().getChatTypingPanel().getParent());
      originalParentContainer.put(MapTool.getFrame().getCommandPanel(), MapTool.getFrame().getCommandPanel().getParent());

      chatFrame = new TranslucentFrame("Chat", "Chat", "Chat",
            MapTool.getFrame().getChatActionLabel(),
            MapTool.getFrame().getChatTypingPanel(),
            MapTool.getFrame().getCommandPanel()          
          );
      chatFrame.show();
    }
  }

}
