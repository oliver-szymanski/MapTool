package net.rptools.maptool.client.functions.frameworkfunctions;

import java.util.Arrays;
import java.util.List;

import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.function.Function;
import net.rptools.parser.function.ParameterException;

public abstract class ExtensionFunctionBridge extends ExtensionFunction implements Function {

  private final String[] aliases;
  private final int minParameters;
  private final int maxParameters;
  private final boolean deterministic;
  
  public ExtensionFunctionBridge(boolean trustedRequired, String... aliases)
  {
    this(trustedRequired, 0, -1, aliases);
  }
  
  public ExtensionFunctionBridge(boolean trustedRequired, int minParameters, int maxParameters, String... aliases)
  {
    this(trustedRequired, minParameters, maxParameters, true, aliases);
  }
  
  public ExtensionFunctionBridge(boolean trustedRequired, int minParameters, int maxParameters, boolean deterministic, String... aliases)
  {
    super(trustedRequired, Arrays.stream(aliases).map(a -> Alias.create(a, minParameters, maxParameters)).toArray(Alias[]::new));
    this.minParameters = minParameters;
    this.maxParameters = maxParameters;
    this.deterministic = deterministic;
    this.aliases = aliases;
  }
  
  public final String[] getAliases()
  {
    return aliases;
  }
  
  public final Object evaluate(Parser parser, String functionName, List<Object> parameters)    throws ParserException
  {
    checkParameters(parameters);
    
    return childEvaluate(parser, functionName, parameters);
  }
  
  public final int getMinimumParameterCount()
  {
    return minParameters;
  }
  
  public final int getMaximumParameterCount()
  {
    return maxParameters;
  }
  
  public final boolean isDeterministic()
  {
    return deterministic;
  }

  protected boolean containsString(List<Object> parameters)
  {
    for (Object param : parameters) {
      if ((param instanceof String)) {
        return true;
      }
    }
    return false;
  }
  
  public abstract Object childEvaluate(Parser parser, String functionName, List<Object> parameters)
    throws ParserException;
  
  @Override
  public void checkParameters(List<Object> parameters) throws ParameterException {
    this.checkParameters(null, null, parameters);
  }

  @Override
  public Object run(Parser parser, String functionName, List<Object> parameters) throws ParserException {
    return childEvaluate(parser, functionName, parameters);
  }
  
}
