import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * GenericHelper is an utility class
 * @author Fabio Tabah
 */
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
		if ( null == value ) {
			return EMPTY ;
		}
		while ( 0 < value.length ( ) ) {
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
	public static boolean isEmptyOrNull ( Object obj ) {
		if ( null == obj ) {
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
	public static boolean isZeroEmptyOrNull ( Object obj ) {
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
                String day = (new StringBuilder()).append(cal.get(Calendar.DAY_OF_MONTH)).toString();
                String month = cal.getDisplayName(2, 2, l);
                String year = (new StringBuilder()).append(cal.get(Calendar.YEAR)).toString();
                return (new StringBuilder(String.valueOf(day))).append(" de ").append(month).append(" de ").append(year).toString();
            }
        }
        String day = (new StringBuilder()).append(cal.get(Calendar.DAY_OF_MONTH)).toString();
		String month = "";
		switch (cal.get(Calendar.MONTH)) {
			case 0:
				month = "Janeiro";
				break;
			case 1:
				month = "Fevereiro";
				break;
			case 2:
				month = "Março";
				break;
			case 3:
				month = "Abril";
				break;
			case 4:
				month = "Maio";
				break;
			case 5:
				month = "Junho";
				break;
			case 6:
				month = "Julho";
				break;
			case 7:
				month = "Agosto";
				break;
			case 8:
				month = "Setembro";
				break;
			case 9:
				month = "Outubro";
				break;
			case 10:
				month = "Novembro";
				break;
			default:
				month = "Dezembro";
				break;
		}
		String year = (new StringBuilder()).append(cal.get(Calendar.YEAR)).toString();
		return (new StringBuilder(String.valueOf(day))).append(" de ").append(month).append(" de ").append(year).toString();
    }
	
	
	
	/**
	 * Orders a list of a given Object using a String obtained by a method called using reflection
	 * @param list - The object that will be ordered, the class listed must have a method that returns a String type
	 * @param getterMethod - The name of the method that obtains the String that will be used to order the list
	 * @throws Exception - if the method does not exist or does not return a String
	 */
	@SuppressWarnings ( { "unchecked" , "rawtypes" } )
	public static void orderListUsingString ( List list , String getterMethod ) throws Exception {
		if ( list == null || 2 > list.size ( ) ) {
			return ;
		}
		final Method method = list.get ( 0 ).getClass ( ).getDeclaredMethod ( getterMethod ) ;
		Collections.sort(list, new Comparator <Object>() {
			public int compare(Object o1, Object o2) {
				try {
					String s1 = (String) method.invoke (o1);
					String s2 = (String) method.invoke (o2) ;
					return s1.compareTo(s2);
				} 
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					System.out.println(getExceptionMessage(e, "orderStringList"));
					return 0 ;
				}
			}
		});
	}
	
	/**
	 * Executes a MySQL command
	 * @param user (ex: root)
	 * @param pwd (ex: abc123, please use more secure passwords)
	 * @param server (ex: localhost or 127.0.0.1)
	 * @param port (ex: 3306)
	 * @param dbName (ex: databaseName)
	 * @param procedureScript (ex: "SELECT SENHA, SALT FROM USUARIO WHERE LOGINNAME = ?" )
	 * @param parameters (ex: ["param1", "param2]
	 * @return ResultSet from procedure
	 */
	public static ResultSet executeMySQLcommand ( String user , String pwd , String server , String port , String dbName ,
											  String procedureScript , String... parameters ) {
		Connection con = null;
		PreparedStatement ps = null;
	    ResultSet rs = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", user);
		connectionProps.put("password", pwd);
		try {
			con = DriverManager.getConnection(
					 (new StringBuilder("jdbc:mysql://").append(server).append(":")
							 							.append(port).append("/")
							 							.append(dbName).toString()), 
					 connectionProps ) ;
			
			ps = con.prepareStatement ( procedureScript );
			for ( int i = 1 ; i <= parameters.length ; i++ ) {
				ps.setString(i, parameters[i-1]);
			}
	        rs = ps.executeQuery();
		} 
		catch (SQLException e) {
			return null;
		}
		return rs ;
	}

	
}
