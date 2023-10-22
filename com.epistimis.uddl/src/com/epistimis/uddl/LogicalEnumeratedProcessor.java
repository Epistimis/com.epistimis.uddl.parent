package com.epistimis.uddl;

import org.eclipse.emf.ecore.EClass;

import com.epistimis.uddl.uddl.LogicalEnumeratedBase;
import com.epistimis.uddl.uddl.UddlPackage;

public class LogicalEnumeratedProcessor extends TaxonomyProcessor<LogicalEnumeratedBase> {

	@Override
	public EClass getBaseMetaClass() {
		return UddlPackage.eINSTANCE.getLogicalEnumeratedBase();
	}

}
