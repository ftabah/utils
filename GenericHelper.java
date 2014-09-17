import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GenericHelper {
	
	private final static String EMPTY = "" ;
	private final static String LINE_BREAK = "\n" ;
	
	/**
	 * @param originalValue
	 * @param finalSize
	 * @return the original value plus zeroes on the left considering the given final size
	 */
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
	
	/**
	 * @param value
	 * @return value without any zero on the left
	 */
	public static String removeZeroesOnTheLeft ( String value ) {
		if ( value == null ) {
			return EMPTY ;
		}
		while ( value.length ( ) > 0 ) {
			if ( value.charAt ( 0 ) == '0' ) {
				value = value.substring ( 1 ) ;
			}
			else {
				break ;
			}
		}
		return value ;
	}
	
	/**
	 * @param e - the exception itself
	 * @param optionalReference - parameter not required
	 * @return String with the formatted message
	 */
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
	
	/**
	 * @param object to verify
	 * @return boolean
	 */
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

	/**
	 * @param object to verify
	 * @return boolean
	 */
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
	
	/**
	 * @return Date formatted as required in Brazilian reports
	 */
	public static String getDateForBR() {
        Calendar cal = Calendar.getInstance();
        Locale alocale[] = SimpleDateFormat.getAvailableLocales();
        int i = 0;
        for(int j = alocale.length; i < j; i++) {
            Locale l = alocale[i];
            if("BR".equals(l.getCountry())) {
                String dia = (new StringBuilder()).append(cal.get(5)).toString();
                String mes = cal.getDisplayName(2, 2, l);
                String ano = (new StringBuilder()).append(cal.get(1)).toString();
                return (new StringBuilder(String.valueOf(dia))).append(" de ").append(mes).append(" de ").append(ano).toString();
            }
        }
        return "";
    }
	
}
