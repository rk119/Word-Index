package F28DA_CW1;

public class WordException extends Exception{
	
	// constructor without arguments and with a default error message
    WordException()
    {
        super("The word does not exist or is invalid");
    }
    
    // constructor with a string argument used as the error message
    public WordException(String errMsg) {
    	super(errMsg);
    }
}