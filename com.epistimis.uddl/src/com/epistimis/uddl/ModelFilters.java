package com.epistimis.uddl;

import com.epistimis.uddl.uddl.LogicalMeasurement;

/**
 * Containts methods used to filter model elements that are generally useful
 * @author stevehickman
 *
 */
public class ModelFilters {
	


	/**
	 * T
	 * @param lm The measurement in question
	 * @param observableName The observable that should be associated with this measurement
	 * @return true if the Measurement is associated with this Observable
	 */
	public static  boolean validMeasurement(LogicalMeasurement lm, String observableName) {
		return lm.getRealizes().getName().equalsIgnoreCase(observableName);
	}

}
