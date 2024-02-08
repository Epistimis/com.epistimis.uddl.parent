/**
 * 
 */
package com.epistimis.uddl

import com.google.inject.Inject
import com.epistimis.uddl.uddl.PlatformLongDouble
import com.epistimis.uddl.uddl.LogicalEnumerated
import com.epistimis.uddl.uddl.PlatformDouble
import com.epistimis.uddl.uddl.PlatformLongLong
import com.epistimis.uddl.uddl.PlatformULongLong
import com.epistimis.uddl.uddl.PlatformChar
import org.eclipse.xtext.naming.QualifiedName
import com.epistimis.uddl.uddl.PlatformSequence
import com.epistimis.uddl.uddl.PlatformOctet
import com.epistimis.uddl.uddl.PlatformFloat
import java.util.List
import com.epistimis.uddl.uddl.PlatformStruct
import com.epistimis.uddl.uddl.PlatformEnumeration
import com.epistimis.uddl.uddl.PlatformULong
import com.epistimis.uddl.uddl.PlatformBoundedString
import com.epistimis.uddl.uddl.PlatformCharArray
import com.epistimis.uddl.uddl.PlatformShort
import com.epistimis.uddl.uddl.PlatformUShort
import com.epistimis.uddl.uddl.PlatformString
import com.epistimis.uddl.uddl.PlatformLong
import com.epistimis.uddl.uddl.PlatformBoolean
import com.epistimis.uddl.uddl.PlatformArray
import com.epistimis.uddl.uddl.LogicalValueTypeUnit
import com.epistimis.uddl.uddl.LogicalEnumeratedBase

import java.text.MessageFormat

/**
 * We can extract landmarks from the Measurement system associated with the measurement and use those for default values, if 
 * there are any. For enumerations, just take the enumeration name.
 */
class DefaultValue {
	@Inject UddlQNP qnp;
	@Inject ModelFilters modelFilters;

	def dispatch Boolean 		getDefaultValue( PlatformBoolean 	value) 		{return Boolean.FALSE;}
	def dispatch Character 		getDefaultValue( PlatformChar 		value) 		{return Character.valueOf('*');}
	def dispatch Character 		getDefaultValue( PlatformOctet 		value) 		{return Character.valueOf('*');}
	def dispatch Float			getDefaultValue( PlatformFloat 		value) 		{return Float.valueOf(0);}
	def dispatch Double    		getDefaultValue( PlatformDouble 	value) 		{return Double.valueOf(0);}
	def dispatch Double 		getDefaultValue( PlatformLongDouble value) 		{return Double.valueOf(0);}
	def dispatch Short	 		getDefaultValue( PlatformShort 		value) 		{return Short.valueOf( 0 as short);}
	def dispatch Long	 		getDefaultValue( PlatformLong 		value) 		{return Long.valueOf(0);}
	def dispatch Long	 		getDefaultValue( PlatformLongLong 	value) 		{return Long.valueOf(0);}
	def dispatch Short	 		getDefaultValue( PlatformUShort 	value) 		{return Short.valueOf( 0 as short);}
	def dispatch Long	 		getDefaultValue( PlatformULong 		value) 		{return Long.valueOf(0);}
	def dispatch Long	 		getDefaultValue( PlatformULongLong 	value) 		{return Long.valueOf(0);}
	def dispatch String 		getDefaultValue( PlatformString 	value) 		{return String.valueOf("*");}
	def dispatch String 		getDefaultValue( PlatformBoundedString 	value) 	{return String.valueOf("*");}
	def dispatch Character 		getDefaultValue( PlatformCharArray 	value) 		{return Character.valueOf('*');}

	def dispatch Character 		getDefaultValue( PlatformSequence 	value) 		{return Character.valueOf('*');}
	def dispatch Character 		getDefaultValue( PlatformArray 		value) 		{return Character.valueOf('*');}

	def dispatch QualifiedName	getDefaultValue( PlatformEnumeration value) 	
	{
		val LogicalEnumeratedBase leb = getEnumValue(value);
		return (leb  === null) ?  QualifiedName.EMPTY : qnp.relativeQualifiedName(leb,value);
	}

	def LogicalEnumeratedBase getEnumValue(PlatformEnumeration value)	 {
		/**
		 * Get the actual enumeration being used and pick a value from that.
		 */
		val List<LogicalValueTypeUnit> vtus = modelFilters.getValueTypeUnit( value);
		if (!vtus.isEmpty()) {
			try {
				val LogicalValueTypeUnit vtu = vtus.get(0);
				// Get the first value in the list
				val LogicalEnumerated le = vtu.getValueType() as LogicalEnumerated;
				return le.label.get(0);
			}
			catch (Exception excp) {
				return null;
			}
		}
		return null;
		
	}
	/**
	 * TODO: Implement this. It should collect default values for all StructMembers. This could be recursive.
	 * The returned object must have a toString() method that generates a string in the appropriate for the grammar.
	 * @param value
	 * @return
	 */
	def dispatch  Object	getDefaultValue( PlatformStruct value) 	
	{
		/**
		 * Go through all the structure members and get default values for each one. This may be recursive.
		 */
		return "";
	}
	
	/**
	 * getDefaultValueAsString provides fine grained control of the format of othe returned string. The 'toString()' method 
	 * may not provide quoted strings or ...  
	 */
	static final String QUOTED_STRING_FMT = "\"{0}\"";
	static final String QUOTED_CHAR_FMT = "\'{0}\'";
	static final String QUOTED_STRING_LIST_FMT = "[ \"{0}\" ]";
	static final String QUOTED_CHAR_LIST_FMT = "[ \'{0}\' ]";
	static final String EXPLAINED_VALUE_FMT = "{0} /* {1} */"; // In case we want to explain where the default came from (e.g. Landmark name, NAICS code description
	
	def dispatch String getDefaultValueAsString( PlatformBoolean 	value) 		{return Boolean.FALSE.toString();}
	def dispatch String getDefaultValueAsString( PlatformChar 		value) 		{return MessageFormat.format(QUOTED_CHAR_FMT,"*");}
	def dispatch String getDefaultValueAsString( PlatformOctet 		value) 		{return MessageFormat.format(QUOTED_CHAR_FMT,"*");}
	def dispatch String	getDefaultValueAsString( PlatformFloat 		value) 		{return Float.valueOf(0).toString();}
	def dispatch String getDefaultValueAsString( PlatformDouble 	value) 		{return Double.valueOf(0).toString();}
	def dispatch String getDefaultValueAsString( PlatformLongDouble value) 		{return Double.valueOf(0).toString();}
	def dispatch String	getDefaultValueAsString( PlatformShort 		value) 		{return Short.valueOf( 0 as short).toString();}
	def dispatch String	getDefaultValueAsString( PlatformLong 		value) 		{return Long.valueOf(0).toString();}
	def dispatch String	getDefaultValueAsString( PlatformLongLong 	value) 		{return Long.valueOf(0).toString();}
	def dispatch String	getDefaultValueAsString( PlatformUShort 	value) 		{return Short.valueOf( 0 as short).toString();}
	def dispatch String	getDefaultValueAsString( PlatformULong 		value) 		{return Long.valueOf(0).toString();}
	def dispatch String	getDefaultValueAsString( PlatformULongLong 	value) 		{return Long.valueOf(0).toString();}
	def dispatch String getDefaultValueAsString( PlatformString 	value) 		{return MessageFormat.format(QUOTED_STRING_FMT,"*");}
	def dispatch String getDefaultValueAsString( PlatformBoundedString 	value) 	{return MessageFormat.format(QUOTED_STRING_FMT,"*");}
	def dispatch String getDefaultValueAsString( PlatformCharArray 	value) 		{return MessageFormat.format(QUOTED_CHAR_LIST_FMT,"*");}

	def dispatch String getDefaultValueAsString( PlatformSequence 	value) 		{return MessageFormat.format(QUOTED_CHAR_LIST_FMT,"*");}
	def dispatch String getDefaultValueAsString( PlatformArray 		value) 		{return MessageFormat.format(QUOTED_CHAR_LIST_FMT,"*");}

	def dispatch String	getDefaultValueAsString( PlatformEnumeration value) 	
	{
		val LogicalEnumeratedBase leb = getEnumValue(value);
		return  (leb === null) ? QualifiedName.EMPTY.toString():  MessageFormat.format(EXPLAINED_VALUE_FMT,qnp.relativeQualifiedName(leb,value).toString(),leb.description);
	}
	
}