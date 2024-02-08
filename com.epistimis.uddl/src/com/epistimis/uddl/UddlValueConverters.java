/**
 * 
 */
package com.epistimis.uddl;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.nodemodel.INode;

/**
 * 
 */
public class UddlValueConverters extends DefaultTerminalConverters {

    IValueConverter<Long> longValueConverter = new IValueConverter<Long>() {

        @Override
        public Long toValue(String string, INode node) throws ValueConverterException {
            // TODO make this more robust
            return Long.parseLong(string.substring(0, string.length()-1));
        }

        @Override
        public String toString(Long value) throws ValueConverterException {
            // TODO make this more robust
            return Long.toString(value)+"L";
        }
    };

    IValueConverter<Float> floatValueConverter = new IValueConverter<Float>() {

        @Override
        public Float toValue(String string, INode node) throws ValueConverterException {
            // TODO make this more robust
            return Float.parseFloat(string);
        }

        @Override
        public String toString(Float value) throws ValueConverterException {
            // TODO make this more robust
            return Float.toString(value);
        }
    };

    IValueConverter<Double> doubleValueConverter = new IValueConverter<Double>() {

        @Override
        public Double toValue(String string, INode node) throws ValueConverterException {
            // TODO make this more robust
            return Double.parseDouble(string);
        }

        @Override
        public String toString(Double value) throws ValueConverterException {
            // TODO make this more robust
            return Double.toString(value);
        }
    };

 
    IValueConverter<Boolean> booleanValueConverter = new IValueConverter<Boolean>() {

        @Override
        public Boolean toValue(String string, INode node) throws ValueConverterException {
            // TODO make this more robust
            return Boolean.parseBoolean(string);
        }

        @Override
        public String toString(Boolean value) throws ValueConverterException {
            // TODO make this more robust
            return Boolean.toString(value);
        }
    };

    IValueConverter<Boolean> rrlowerValueConverter = new IValueConverter<Boolean>() {

        @Override
        public Boolean toValue(String string, INode node) throws ValueConverterException {
        	return string.equals("[") ? Boolean.TRUE : Boolean.FALSE;
        	
        }

        @Override
        public String toString(Boolean value) throws ValueConverterException {
        	return value? "[" : "(";
        }
    };
    
    IValueConverter<Boolean> rrupperValueConverter = new IValueConverter<Boolean>() {

        @Override
        public Boolean toValue(String string, INode node) throws ValueConverterException {
        	return string.equals("]") ? Boolean.TRUE : Boolean.FALSE;
        	
        }

        @Override
        public String toString(Boolean value) throws ValueConverterException {
        	return value? "]" : ")";
        }
    };
    

    @ValueConverter(rule = "LONG")
    public IValueConverter<Long> LONG() {
        return longValueConverter;
    }

    @ValueConverter(rule = "FLOAT")
    public IValueConverter<Float> FLOAT() {
        return floatValueConverter;
    }
    
    @ValueConverter(rule = "DOUBLE")
    public IValueConverter<Double> DOUBLE() {
        return doubleValueConverter;
    }

    @ValueConverter(rule = "BOOLEAN")
    public IValueConverter<Boolean> BOOLEAN() {
        return booleanValueConverter;
    }

    @ValueConverter(rule = "RRLOWER")
    public IValueConverter<Boolean> RRLOWER() {
        return rrlowerValueConverter;
    }

    @ValueConverter(rule = "RRUPPER")
    public IValueConverter<Boolean> RRUPPER() {
        return rrupperValueConverter;
    }

}
