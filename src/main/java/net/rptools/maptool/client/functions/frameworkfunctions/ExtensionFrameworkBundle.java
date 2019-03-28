package net.rptools.maptool.client.functions.frameworkfunctions;

import java.util.Collection;

public abstract class ExtensionFrameworkBundle {

  private Version version;

  public Version version() {
    return version;
  }

  public String name() {
    return this.getClass().getName();
  }

  abstract public Collection<? extends ExtensionFunction> getFunctions();
  abstract public Collection<? extends ExtensionFunctionButton> getFunctionButtons();
  abstract public Collection<? extends ExtensionChatMacro> getChatMacros();

}
