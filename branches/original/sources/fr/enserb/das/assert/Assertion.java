package fr.enserb.das.assert;

public class Assertion
{
  public static void verify(boolean condition, 
			    String assertion, Object uneInstance) 
  {
    if (debogage) 
    {
	if (condition)
	  return;
	else 
	  throw new AssertionException("Assertion -> "+ assertion + " in Class " 
				       + uneInstance.getClass().getName());
    }
    else
      return;
  }

  private static final boolean debogage = true;
}


  


class AssertionException extends RuntimeException
{
  AssertionException() 
  {
    super("Assertion Failed");
  }

  AssertionException(String message) 
  {
    super(message);
  }
}
