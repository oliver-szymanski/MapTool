package net.rptools.maptool.client.functions.frameworkfunctions;

import java.util.Arrays;
import java.util.List;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.language.I18N;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.function.Function;
import net.rptools.parser.function.ParameterException;

public abstract class ExtensionFunction {

  private boolean trustedRequired;
  private String prefix = null;
  private String[] aliasNames;
  private Alias[] extensionFunctionAliases;
  
  public ExtensionFunction(boolean trustedRequired, String ... aliases) {
    this(trustedRequired, Arrays.stream(aliases).map(a -> Alias.create(a)).toArray(Alias[]::new));
  }
  
  public ExtensionFunction(boolean trustedRequired, Alias ... aliases) {
    this.trustedRequired = trustedRequired;
    this.extensionFunctionAliases = aliases;
    this.aliasNames = Arrays.stream(aliases).map(a -> a.getFunctionName()).toArray(String[]::new);
  }
  
  public abstract Object run(Parser parser, String functionName, List<Object> parameters) throws ParserException;
  
  protected boolean checkParameters(Parser parser, String functionName, List<Object> parameters) throws ParameterException {
    int countParameters = parameters == null ? 0 : parameters.size();
    
    for (Alias alias : extensionFunctionAliases) {
      if (functionName.equals(alias.getFunctionName())) {
        if ((countParameters < alias.getMinParameters()) || ((alias.getMaxParameters() != -1) && (parameters.size() > alias.getMaxParameters()))) {
          throw new ParameterException(String.format(
              "Invalid number of parameters %d, expected %s", 
              new Object[] { Integer.valueOf(countParameters), formatExpectedParameterString(alias) }));
        }
      }
    }
    
    return true;
  }
  
  private String formatExpectedParameterString(Alias alias) {
    if (alias.getMinParameters() == alias.getMaxParameters()) {
      return String.format("exactly %d parameter(s)", new Object[] { Integer.valueOf(alias.getMaxParameters()) });
    }
    if (alias.getMaxParameters() == -1) {
      return String.format("at least %d parameters", new Object[] { Integer.valueOf(alias.getMinParameters()) });
    }
    return String.format("between %d and %d parameters", new Object[] { Integer.valueOf(alias.getMinParameters()), Integer.valueOf(alias.getMaxParameters()) });
  }
  
  public final Object execute(Parser parser, String functionName, List<Object> parameters) throws ParserException{
    
    if (trustedRequired && !MapTool.getParser().isMacroTrusted()) {
      throw new ParserException(I18N.getText("macro.function.general.noPerm", functionName));
    }
    
    return FrameworksFunctions.executeExtensionFunctionWithAccessControl(parser, parameters, functionName, this);
  }

  protected String getPrefix() {
    return prefix;
  }

  void setPrefix(String prefix) {
    this.prefix = prefix;
  }
  
  public String[] getAliases() {
    return this.aliasNames;
  }
  
  public static class Alias {
    private String functionName;
    private int minParameters;
    private int maxParameters;
    
    public static Alias create(String functionName) {
      return new Alias(functionName);
    }
    
    public static Alias create(String functionName, int minParameters, int maxParameters) {
      return new Alias(functionName, minParameters, maxParameters);
    }
    
    public Alias(String functionName) {
      this(functionName, 0, Function.UNLIMITED_PARAMETERS);
    }
    
    public Alias(String functionName, int minParameters, int maxParameters) {
      this.functionName = functionName;
      this.minParameters = minParameters;
      this.maxParameters = maxParameters;
    }

    public String getFunctionName() {
      return functionName;
    }

    public int getMinParameters() {
      return minParameters;
    }

    public int getMaxParameters() {
      return maxParameters;
    }
  }
}
