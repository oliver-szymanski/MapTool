package net.rptools.maptool.client.functions.frameworkfunctions;

import net.rptools.maptool.client.MapToolMacroContext;
import net.rptools.maptool.client.macro.Macro;
import net.rptools.maptool.client.macro.MacroContext;

public abstract class ExtensionChatMacro implements Macro {

  private final boolean trustedRequired;
  private  String prefix = null;
      
  public ExtensionChatMacro(boolean trustedRequired) {
    this.trustedRequired = trustedRequired;
  }
  
  public abstract void run(MacroContext context, String macro, MapToolMacroContext executionContext);

  @Override
  public final void execute(
      MacroContext context, String macro, MapToolMacroContext executionContext) {
    if (trustedRequired && !executionContext.isTrusted()) {
      return;
    }
    
    FrameworksFunctions.executeExtensionChatMacroWithAccessControl(this, context, macro, executionContext);
  }
  
  protected String getPrefix() {
    return prefix;
  }

  void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public final boolean isTrustedRequired() {
    return trustedRequired;
  }
}