package net.rptools.maptool.client.functions.frameworkfunctions;

import java.math.BigDecimal;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.List;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolVariableResolver;
import net.rptools.maptool.client.functions.frameworkfunctions.FrameworksFunctions.Run;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.Zone;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.function.Function;

public class FunctionCaller {

  
  public static boolean hasVariable(Parser parser, String name)
      throws ParserException {
    try {
      return AccessController.doPrivileged(
          new PrivilegedExceptionAction<>() {
            public Boolean run() throws Exception {
              MapToolVariableResolver resolver =((MapToolVariableResolver)parser.getVariableResolver());
              boolean autoPrompt = resolver.getAutoPrompt();
              resolver.setAutoPrompt(false);
              Object result;
              try {
                result = resolver.getVariable(name);
              } catch (Exception e) {
                // safe to ignore
                result = null;
              }
              resolver.setAutoPrompt(autoPrompt);
              return result != null;
            }
          });
    } catch (PrivilegedActionException e) {
      throw new ParserException(e);
    }
  }
  
  public static Object getVariable(Parser parser, String name, Object defaultValue)
      throws ParserException {
    try {
      return AccessController.doPrivileged(
          new PrivilegedExceptionAction<>() {
            public Object run() throws Exception {
              MapToolVariableResolver resolver =((MapToolVariableResolver)parser.getVariableResolver());
              boolean autoPrompt = resolver.getAutoPrompt();
              resolver.setAutoPrompt(false);
              Object result;
              try {
                result = resolver.getVariable(name);
              } catch (Exception e) {
                // safe to ignore
                result = null;
              }
              resolver.setAutoPrompt(autoPrompt);
              if (result == null && defaultValue != null) {
                result = defaultValue;
              }
              return result;
            }
          });
    } catch (PrivilegedActionException e) {
      throw new ParserException(e);
    }
  }
  
  public static Object askForVariable(Parser parser, String name, Object defaultValue)
      throws ParserException {
    try {
      return AccessController.doPrivileged(
          new PrivilegedExceptionAction<>() {
            public Object run() throws Exception {
              MapToolVariableResolver resolver =((MapToolVariableResolver)parser.getVariableResolver());
              boolean autoPrompt = resolver.getAutoPrompt();
              resolver.setAutoPrompt(true);
              Object result;
              try {
                result = resolver.getVariable(name);
              } catch (Exception e) {
                // safe to ignore
                result = null;
              }
              resolver.setAutoPrompt(autoPrompt);
              if (result == null && defaultValue != null) {
                result = defaultValue;
              }
              return result;
            }
          });
    } catch (PrivilegedActionException e) {
      throw new ParserException(e);
    }
  }
  
  public static void setVariable(Parser parser, String name, Object value)
      throws ParserException {
    try {
      AccessController.doPrivileged(
          new PrivilegedExceptionAction<>() {
            public Object run() throws Exception {
              MapToolVariableResolver resolver =((MapToolVariableResolver)parser.getVariableResolver());
              resolver.setVariable(name, value);
              return value;
            }
          });
    } catch (PrivilegedActionException e) {
      throw new ParserException(e);
    }
  }
  
  public static Object callFunction(
      String functionName, Function f, Parser parser, Object... parameters)
      throws ParserException {
    try {
      return AccessController.doPrivileged(
          new PrivilegedExceptionAction<>() {
            public Object run() throws Exception {
              return f.evaluate(parser, functionName, Arrays.asList(parameters));
            }
          });
    } catch (PrivilegedActionException e) {
      throw new ParserException(e);
    }
  }

  public static Object callFunction(String functionName, Parser parser, Object... parameters)
      throws ParserException {
    Function f = parser.getFunction(functionName);
    try {
      return AccessController.doPrivileged(
          new PrivilegedExceptionAction<>() {
            public Object run() throws Exception {
              return f.evaluate(parser, functionName, Arrays.asList(parameters));
            }
          });
    } catch (PrivilegedActionException e) {
      throw new ParserException(e);
    }
  }
  
  public static Object callFunction(
      String functionName, ExtensionFunction f, Parser parser, Object... parameters)
      throws ParserException {
    try {
      return AccessController.doPrivileged(
          new PrivilegedExceptionAction<>() {
            public Object run() throws Exception {
              return f.execute(parser, functionName, Arrays.asList(parameters));
            }
          });
    } catch (PrivilegedActionException e) {
      throw new ParserException(e);
    }
  }

  public static List<Object> toObjectList(Object... parameters) {
    return Arrays.asList(parameters);
  }

  public static <T> T getParam(List<Object> parameters, int i) {
    return getParam(parameters, i, null);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getParam(List<Object> parameters, int i, T defaultValue) {
    if (parameters != null && parameters.size() > i) {
      return (T) parameters.get(i);
    } else {
      return defaultValue;
    }
  }

  public static boolean toBoolean(Object val) {
    if (val instanceof BigDecimal) {
      return BigDecimal.ZERO.equals(val) ? false : true;
    } else if (val instanceof Boolean) {
      return (Boolean) val;
    }

    return false;
  }
  
  /**
   * This ensure the token that you modify in the run command is put/save on it's correct
   * map. Do not change the map/zone/zoneRenderer in the run command.    
   * @param token
   * @param run
   * @throws Exception 
   */
  public static <T> T doWithToken(Parser parser, String token, Run<T> run) throws Exception {
    // this will ensure we are on the right zone before manipulating the token
    // and then switch back
    TokenWrapper tokenWrapper = findToken(token, null, true);
    return doWithToken(parser, tokenWrapper, run);
  }
  
  /**
   * This ensure the token that you modify in the run command is put/save on it's correct
   * map. Do not change the map/zone/zoneRenderer in the run command.    
   * @param token
   * @param run
   * @throws Exception 
   */
  public static <T> T doWithToken(Parser parser, TokenWrapper token, Run<T> run) throws Exception {
    // this will ensure we are on the right zone before manipulating the token
    // and then switch back
    ZoneRenderer currentZoneRenderer = MapTool.getFrame().getCurrentZoneRenderer();
    Zone tokenZone = token.getZone();
    for (ZoneRenderer zr : MapTool.getFrame().getZoneRenderers()) {
      if (tokenZone.getName().equals(zr.getZone().getName())) {
        MapTool.getFrame().setCurrentZoneRenderer(zr);
      }
    }
    Token currentToken = ((MapToolVariableResolver)parser.getVariableResolver()).getTokenInContext();
    try {
      ((MapToolVariableResolver)parser.getVariableResolver()).setTokenIncontext(token.getToken());
      T result = run.run();
      return result;
    } finally {
      ((MapToolVariableResolver)parser.getVariableResolver()).setTokenIncontext(currentToken);  
      // switch back
      MapTool.getFrame().setCurrentZoneRenderer(currentZoneRenderer);
    }    
  }
  
  public static TokenWrapper getCurrentToken(Parser parser) {
    Token currentToken = ((MapToolVariableResolver)parser.getVariableResolver()).getTokenInContext();
    return findToken(currentToken.getId().toString(), null, false);
  }
  
  public static TokenWrapper findToken(String identifier, String zoneName, boolean scanAllZones) {
    Zone currentZone = MapTool.getFrame().getCurrentZoneRenderer().getZone();
    if (!scanAllZones && (zoneName == null || zoneName.length() == 0)) {
        // only check current
        Token token = currentZone.resolveToken(identifier);
        if (token == null) { return null; }
        return new TokenWrapper(token, currentZone);
    }
        
    // check current map first if no map specified
    if (!scanAllZones && (zoneName == null || zoneName.length() == 0)) {
      Token token = currentZone.resolveToken(identifier);
      if (token != null) {
        return new TokenWrapper(token, currentZone);
      }
    }
      
    // check all maps
    List<ZoneRenderer> zrenderers = MapTool.getFrame().getZoneRenderers();
    for (ZoneRenderer zr : zrenderers) {
      Zone zone = zr.getZone();
      if (zoneName == null || zoneName.length() == 0 || zone.getName().equalsIgnoreCase(zoneName)) {
        Token token = zone.resolveToken(identifier);
        if (token != null) {
          return new TokenWrapper(token, zone);
        }
      }
    }
    
    return null;
  }
  
  public static class TokenWrapper {
    private Token token;
    private Zone zone;
    
    private TokenWrapper(Token token, Zone zone) {
      this.token = token;
      this.zone = zone;
    }

    public Token getToken() {
      return token;
    }

    public Zone getZone() {
      return zone;
    }
  }
}