package com.epistimis.uddl;

import org.eclipse.emf.common.util.EList;

import com.epistimis.uddl.uddl.ConceptualBasisEntity;
import com.epistimis.uddl.uddl.ConceptualEntity;
import com.epistimis.uddl.uddl.LogicalMeasurement;

/**
 * Containts methods used to filter model elements that are generally useful
 * 
 * @author stevehickman
 *
 */
public class ModelFilters {

	/**
	 * Determine if the specified ConceptualEntity has a reference to the
	 * BasisEntity.
	 * 
	 * TODO: Do we need to have a qualified name that includes Domain? Domains don't
	 * contain BasisEntities, so it is technically possible for the same BasisEntity
	 * to be in multiple Domains. For now, ignore Domain.
	 * 
	 * @param ce the ConceptualEntity to check
	 * @param be the BasisEntity to check for 
	 * @return
	 */
	public static boolean hasBasis(ConceptualEntity ce, ConceptualBasisEntity be) {
		if (ce.getBasisEntity().contains(be))
			return true;
		if (ce.getSpecializes() != null) {
			return hasBasis(ce.getSpecializes(), be);
		}
		// If we get here, it doesn't have this basis.
		return false;
	}

	/**
	 * Determine if the specified ConceptualEntity has a reference to the
	 * BasisEntity.
	 * 
	 * @param ce the ConceptualEntity to check
	 * @param basisName the name of the BasisEntity to check for
	 * @return
	 */
	public static boolean hasBasis(ConceptualEntity ce, String basisName) {
		if (ce.getBasisEntity().stream().anyMatch(be -> be.getName().equalsIgnoreCase(basisName)))
			return true;
		if (ce.getSpecializes() != null) {
			return hasBasis(ce.getSpecializes(), basisName);
		}
		// If we get here, it doesn't have this basis.
		return false;
	}

	/**
	 * Does this Measurement realize this ConceptualObservable?
	 * 
	 * @param lm             The measurement in question
	 * @param observableName The observable that should be associated with this
	 *                       measurement
	 * @return true if the Measurement is associated with this Observable
	 */
	public static boolean measurementOf(LogicalMeasurement lm, String observableName) {
		return lm.getRealizes().getName().equalsIgnoreCase(observableName);
	}

}
