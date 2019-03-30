package net.rptools.maptool.client.functions.frameworkfunctions.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.functions.frameworkfunctions.ExtensionFunctionButton;

public class TranslucentFrame {
  private TranslucentFrame rootFrame;
  private JFrame actualFrame;
  private JTabbedPane tabbedPane;
  private JPanel subTab;
  private String frameName;
  private String group;
  private List<TranslucentFrame> subFrames = new LinkedList<>();
  private String prefixedFrameName;
  @SuppressWarnings("unused")
  private String prefixedFrameId;
  
  public TranslucentFrame(String frameName, String prefixedFrameName, String prefixedFrameId) {
    this.frameName = frameName;
    this.prefixedFrameName = prefixedFrameName;
    this.prefixedFrameId = prefixedFrameId;
    initRootFrame();
  }
  
  public TranslucentFrame(String frameName, String prefixedFrameName, String prefixedFrameId, Component... components ) {
    this.frameName = frameName;
    this.prefixedFrameName = prefixedFrameName;
    this.prefixedFrameId = prefixedFrameId;
    initRootFrame();
    initWithComponents(components);
  }

  public TranslucentFrame(String frameName, String prefixedFrameName, String prefixedFrameId, String group, TranslucentFrame root) {
    this.frameName = frameName;
    this.prefixedFrameName = prefixedFrameName;
    this.prefixedFrameId = prefixedFrameId;
    this.group = group;
    this.rootFrame = root;
    root.subFrames.add(this);
    initSubTab();
  }
  
  private void initWithComponents(Component ... components) {
    JPanel container = new JPanel(new BorderLayout());

    for (Component component : components) {
      container.add(component);
      component.setVisible(true);
      component.invalidate();
    }    
    
    Container content = tabbedPane.getParent();
    content.remove(tabbedPane);
    content.add(container);
  }
  
  private void initRootFrame() {
    actualFrame = new JFrame(frameName);
    actualFrame.setLayout(new BorderLayout());
   // actualFrame.setLayout(new GridLayout(30,30,30,30));
    actualFrame.setSize(300,200);

    Integer x      = PreferenceManager.loadPreference(actualFrame.getLocation().x, "FrameworkFunctions", "ButtonFrame", prefixedFrameId, group, "x");
    Integer y      = PreferenceManager.loadPreference(actualFrame.getLocation().y, "FrameworkFunctions", "ButtonFrame", prefixedFrameId, group, "y");
    Integer width  = PreferenceManager.loadPreference(actualFrame.getWidth(), "FrameworkFunctions", "ButtonFrame", prefixedFrameId, group, "width");
    Integer height = PreferenceManager.loadPreference(actualFrame.getHeight(), "FrameworkFunctions", "ButtonFrame", prefixedFrameId, group, "height");
    if (x != null && y != null && width != null && height != null) {
      actualFrame.setLocation(x, y);
      actualFrame.setSize(width, height);
    } else {
      actualFrame.setLocationRelativeTo(null);
    }
    
    actualFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    actualFrame.setUndecorated(true);
    actualFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    actualFrame.setOpacity(0.55f);
    actualFrame.setAlwaysOnTop(true);
    actualFrame.setResizable(true);
    JLabel label = new JLabel(prefixedFrameName);
    label.setFont(label.getFont().deriveFont(14f));
    //label.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
    JPanel title = new JPanel();
    title.add(label);
    actualFrame.add(title, BorderLayout.NORTH);
    
    tabbedPane = new JTabbedPane();
    tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
    tabbedPane.setUI(new BasicTabbedPaneUI() {
      @Override
      protected void installDefaults() {
          super.installDefaults();
          highlight = Color.cyan;
          lightHighlight = Color.lightGray;
          shadow = Color.darkGray;
          darkShadow = Color.black;
          focus = Color.lightGray;
      }
    });
    
    JPanel tabbedPaneContainer = new JPanel(){
      private static final long serialVersionUID = 1L;

      @Override
      public Insets getInsets() {
        return new Insets(10, 10, 10, 10);
      }
    };
    tabbedPaneContainer.setLayout(new GridLayout());
    tabbedPaneContainer.add(tabbedPane);
    actualFrame.add(tabbedPaneContainer);
    actualFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        savePreferences();
      }
    });
    
    FrameDragListener frameDragListener = new FrameDragListener(actualFrame);
    actualFrame.addMouseListener(frameDragListener);
    actualFrame.addMouseMotionListener(frameDragListener);
    
    ComponentResizer cr = new ComponentResizer();
    cr.setSnapSize(new Dimension(10, 10));
    cr.registerComponent(actualFrame);
    cr.setFrameDragListener(frameDragListener);
    cr.setResizeThis(actualFrame);
  }
  
  private void initSubTab() {
    this.subTab = new JPanel(new WrapLayout());
    JScrollPane scrollPane = new JScrollPane(this.subTab);
    rootFrame.tabbedPane.addTab(this.group, scrollPane);
  }
  
  protected JComponent makeTextPanel(String text) {
    JPanel panel = new JPanel(false);
    JLabel filler = new JLabel(text);
    filler.setHorizontalAlignment(JLabel.CENTER);
    panel.setLayout(new GridLayout(1, 1));
    panel.add(filler);
    return panel;
  }
  
  private JButton createButton(ExtensionFunctionButton functionButton) {
    JButton button;
    ImageIcon icon = null;
    
    if (functionButton.getImageFile() != null) {
      icon = createImageIcon(functionButton, functionButton.getImageFile());
    }
    
    if (functionButton.isNameAndImage() || icon == null) {
      button = new JButton(functionButton.getName());
    } else {
      button = new JButton();
    }
    
    button.setToolTipText(functionButton.getTooltip());
    button.setFont(new Font("Serif",Font.PLAIN,16));
    button.setBackground(new Color(50,50,50));//import java.awt.Color;
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    if (icon != null) { button.setIcon(icon); }
    return button;
  }
  
  private void savePreferences() {
    PreferenceManager.savePreference(actualFrame.getLocation().x, "FrameworkFunctions", "ButtonFrame", prefixedFrameId, group, "x");
    PreferenceManager.savePreference(actualFrame.getLocation().y, "FrameworkFunctions", "ButtonFrame", prefixedFrameId, group, "y");
    PreferenceManager.savePreference(actualFrame.getWidth(), "FrameworkFunctions", "ButtonFrame", prefixedFrameId, group, "width");
    PreferenceManager.savePreference(actualFrame.getHeight(), "FrameworkFunctions", "ButtonFrame", prefixedFrameId, group, "height");
  }

  public void hide() {
    savePreferences();
    actualFrame.setVisible(false);
  }
  
  public void close() {
    hide();
    actualFrame.dispose();
  }
  
  public void show() {
    if (actualFrame == null && this.group == null) {
      initRootFrame();
    }
    
    for (TranslucentFrame subFrame : subFrames) {
      if (subFrame.subTab == null) {
        subFrame.initSubTab();
      }
    }
    
    if (actualFrame != null && !actualFrame.isVisible()) {
      // only pack the frame if it size does not come from saved preferences
      if (null == PreferenceManager.loadPreference((String)null, "FrameworkFunctions", "ButtonFrame", frameName, group, "x")) {
        actualFrame.pack();        
      }

      actualFrame.setVisible(true); // this opens the dialog
    }      
  }
  
  public void add(ExtensionFunctionButton functionButton) {
    JButton jButton = createButton(functionButton);
    
    if (this.group == null) {
      actualFrame.add(jButton);
    } else {
      subTab.add(jButton);
    }
    jButton.addActionListener(e -> {
      try {
        functionButton.execute();
      } catch (Exception e1) {
        MapTool.addLocalMessage(e.toString());
      }
    });
  }
  
  /** Returns an ImageIcon, or null if the path was invalid. */
  protected ImageIcon createImageIcon(ExtensionFunctionButton functionButton, String path) {
    
    if (path == null) {
      return null;
    }

    Object objectForClassload = functionButton;      
    if (functionButton == null) {
      objectForClassload = this;
    }
    
    URL imgURL = objectForClassload.getClass().getResource(path);
    if (imgURL != null) {
        ImageIcon icon = new ImageIcon(imgURL, functionButton.getName());
        Image img = icon.getImage() ;  
        Image newImg = img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newImg );
        return icon;

    } else {
        System.err.println("Couldn't find file: " + path);
        return null;
    }
  }

  public boolean isVisble() {
    if (this.actualFrame == null) {
      return false;
    }
    
    return this.actualFrame.isVisible();
  }
}