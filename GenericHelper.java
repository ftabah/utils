import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class GenericHelper {
	
	private final static String EMPTY = "" ;
	private final static String LINE_BREAK = "\n" ;
	
	public static String insertZeroesOnTheLeft ( String originalValue , int finalSize ) {
		StringBuffer finalValue = new StringBuffer ( ) ;
		if ( isEmptyOrNull ( originalValue ) ) {
			for ( int i = 0 ; i < finalSize ; i ++ ) {
				finalValue.append ( "0" ) ;
			}
			return finalValue.toString ( ) ;
		}
		else {
			finalValue.append ( originalValue ) ;
			for ( int i = originalValue.length ( ) ; i < finalSize ; i ++ ) {
				finalValue.insert ( 0 , "0" ) ;
			}
			return finalValue.toString ( ) ;
		}
	}
	
	public static String removeZeroesOnTheLeft ( String entrada ) {
		if ( entrada == null ) {
			return EMPTY ;
		}
		while ( entrada.length ( ) > 0 ) {
			if ( entrada.charAt ( 0 ) == '0' ) {
				entrada = entrada.substring ( 1 ) ;
			}
			else {
				break ;
			}
		}
		return entrada ;
	}
	
	public static String getExceptionMessage ( Exception e , String... optionalReference ) {
		String msg = EMPTY ;
		if ( optionalReference != null && optionalReference.length == 1 ) {
			msg = optionalReference[0] + LINE_BREAK ;
		}
		msg = e.getMessage ( ) + LINE_BREAK ;
		for ( StackTraceElement ste : e.getStackTrace ( ) ) {
			msg += ste.toString ( ) + LINE_BREAK ;
		}
		return msg ;
	}
	
	static public boolean isEmptyOrNull ( Object obj ) {
		if ( obj == null ) {
			return true ;
		}
		else if ( obj instanceof String && EMPTY.equals ( obj ) ) {
			return true ;
		}
		else if ( obj instanceof List < ? > ) {
			List < ? > lista = ( List < ? > ) obj ;
			return lista.isEmpty ( ) ;
		}
		else {
			return false ;
		}
	}

	static public boolean isZeroEmptyOrNull ( Object obj ) {
		if ( isEmptyOrNull ( obj ) ) {
			return true ;
		}
		else if ( obj instanceof Integer ) {
			return ( ( Integer ) obj ).intValue ( ) == 0 ;
		}
		else if ( obj instanceof Double ) {
			return ( ( Double ) obj ).doubleValue ( ) == 0d ;
		}
		else if ( obj instanceof Long ) {
			return ( ( Long ) obj ).longValue ( ) == 0L ;
		}
		else if ( obj instanceof Number ) {
			return ( ( Number ) obj ).doubleValue ( ) == 0d ;
		}
		else if ( obj instanceof BigInteger ) {
			return ( ( BigInteger ) obj ).intValue ( ) == 0 ;
		}
		else if ( obj instanceof BigDecimal ) {
			return ( ( BigDecimal ) obj ).doubleValue ( ) == 0d ;
		}
		else if ( obj instanceof String ) {
			if ( ! EMPTY.equals ( obj ) ) {
				String zero = ( String ) obj ;
				try {
					double n = Double.parseDouble ( zero ) ;
					if ( n == 0.0 ) {
						return true ;
					}
					else {
						return false ;
					}
				}
				catch ( NumberFormatException nfe ) {
					if ( "0,00".equals ( zero ) ) {
						return true ;
					}
					return false ;
				}
			}
			else {
				return true ;
			}
		}
		else {
			return false ;
		}
	}
	
}
