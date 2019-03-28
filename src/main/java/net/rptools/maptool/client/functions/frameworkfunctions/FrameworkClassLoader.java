package net.rptools.maptool.client.functions.frameworkfunctions;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

class FrameworkClassLoader extends URLClassLoader {

  private final List<CodeSource> origins = new LinkedList<>();
  private final PermissionCollection perms = new Permissions();
  private final PermissionCollection allPermissions = new Permissions();

  public FrameworkClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
    allPermissions.add(new AllPermission());

    try {
      for (URL url : urls) {
        CodeSource origin = new CodeSource(Objects.requireNonNull(url), (Certificate[]) null);
        copyPermissions(super.getPermissions(origin), perms);
        origins.add(origin);
      }
      // perms.setReadOnly();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void copyPermissions(PermissionCollection src, PermissionCollection dst) {
    for (Enumeration<Permission> e = src.elements(); e.hasMoreElements(); ) {
      dst.add(e.nextElement());
    }
  }
  
  @Override
  public void addURL(URL url) {
    super.addURL(url);
    CodeSource origin = new CodeSource(Objects.requireNonNull(url), (Certificate[]) null);
    copyPermissions(super.getPermissions(origin), perms);
    origins.add(origin);
  }

  @Override
  protected PermissionCollection getPermissions(CodeSource cs) {
    for (CodeSource origin : origins) {
      if (origin.implies(cs)) {
        return perms;
      }
    }

    return super.getPermissions(cs);
  }
}