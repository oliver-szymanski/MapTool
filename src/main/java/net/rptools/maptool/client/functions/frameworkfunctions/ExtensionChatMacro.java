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
package net.rptools.maptool.client.functions.frameworkfunctions;

import net.rptools.maptool.client.MapToolMacroContext;
import net.rptools.maptool.client.macro.Macro;
import net.rptools.maptool.client.macro.MacroContext;

public abstract class ExtensionChatMacro implements Macro {

  private final boolean trustedRequired;
  private String prefix = null;

  public ExtensionChatMacro(boolean trustedRequired) {
    this.trustedRequired = trustedRequired;
  }

  public abstract void run(
      MacroContext context, String macro, MapToolMacroContext executionContext);

  @Override
  public final void execute(
      MacroContext context, String macro, MapToolMacroContext executionContext) {
    if (trustedRequired && !executionContext.isTrusted()) {
      return;
    }

    FrameworksFunctions.executeExtensionChatMacroWithAccessControl(
        this, context, macro, executionContext);
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
