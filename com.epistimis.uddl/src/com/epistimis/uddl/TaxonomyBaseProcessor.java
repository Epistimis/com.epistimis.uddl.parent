package com.epistimis.uddl;

import org.eclipse.emf.ecore.EClass;

import com.epistimis.uddl.uddl.Taxonomy;
import com.epistimis.uddl.uddl.UddlPackage;

public class TaxonomyBaseProcessor extends TaxonomyProcessor<Taxonomy> {

	@Override
	public EClass getBaseMetaClass() {
		return UddlPackage.eINSTANCE.getTaxonomy();
	}

}
