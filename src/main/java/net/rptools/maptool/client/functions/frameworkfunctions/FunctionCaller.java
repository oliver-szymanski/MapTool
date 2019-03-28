package net.rptools.maptool.client.functions.frameworkfunctions;

import java.math.BigDecimal;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.List;

import net.rptools.maptool.client.MapToolVariableResolver;
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
}