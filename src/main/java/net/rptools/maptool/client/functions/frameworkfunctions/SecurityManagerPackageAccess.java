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
      //MapTool.addLocalMessage("Permission error: " + e.getMessage());
      throw e;
    }
  }
}