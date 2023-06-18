package com.epistimis.uddl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.epistimis.uddl.uddl.LogicalEnumeratedBase;
import com.epistimis.uddl.uddl.UddlPackage;

public class LogicalEnumeratedProcessor extends TaxonomyProcessor<LogicalEnumeratedBase> {

	@Override
	public EClass getBaseMetaClass() {
		return UddlPackage.eINSTANCE.getLogicalEnumeratedBase();
	}

	@Override
	public boolean isCastableToBase(EObject obj) {
		return (obj instanceof LogicalEnumeratedBase);
	}

	@Override
	public String getBaseName(EObject obj) {
		return ((LogicalEnumeratedBase)obj).getName();
	}

}
