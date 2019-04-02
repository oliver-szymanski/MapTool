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

class SecurityManagerPackageAccess extends SecurityManager {

  // checkPackageAccess needs to be overridden
  @Override
  public void checkPackageAccess(String pkg) {
    try {
      // super will not check the AccessControlContext for permission
      // so needs to be overridden and more checks added.
      // otherwise too much is granted (it only checks based on java modules)
      super.checkPackageAccess(pkg);

      // restrict access to MapTool packages, except some.
      // core will be allowed as it has all permissions anyway.
      // in case class access is required check the other permission methods
      // to override.
      if (pkg.startsWith("net.rptools.")
          && !pkg.equals("net.rptools.maptool.client")
          && !pkg.equals("net.rptools.maptool.client.ui.zone")
          && !pkg.equals("net.rptools.maptool.client.ui.commandpanel")
          && !pkg.equals("net.rptools.maptool.client.functions")
          && !pkg.equals("net.rptools.maptool.client.functions.frameworkfunctions")
          && !pkg.equals("net.rptools.maptool.model")) {
        checkPermission(new RuntimePermission("accessClassInPackage." + pkg));
      }
    } catch (Exception e) {
      e.printStackTrace();
      // MapTool.addLocalMessage("Permission error: " + e.getMessage());
      throw e;
    }
  }
}
