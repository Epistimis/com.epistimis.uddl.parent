package com.epistimis.uddl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import com.epistimis.uddl.uddl.ConceptualAssociation;
import com.epistimis.uddl.uddl.ConceptualCharacteristic;
import com.epistimis.uddl.uddl.ConceptualCompositeQuery;
import com.epistimis.uddl.uddl.ConceptualEntity;
import com.epistimis.uddl.uddl.ConceptualParticipant;
import com.epistimis.uddl.uddl.ConceptualQuery;
import com.epistimis.uddl.uddl.ConceptualQueryComposition;
import com.epistimis.uddl.uddl.ConceptualView;
import com.epistimis.uddl.uddl.UddlPackage;

public class ConceptualQueryProcessor extends
		QueryProcessor<ConceptualCharacteristic, ConceptualEntity, ConceptualAssociation, ConceptualParticipant, ConceptualView, ConceptualQuery, ConceptualCompositeQuery, ConceptualQueryComposition> {

	protected EList<ConceptualQueryComposition> getComposition(ConceptualCompositeQuery ent) {
		return ent.getComposition();
	}

	protected ConceptualView getType(ConceptualQueryComposition obj) {
		return obj.getType();
	}

	
	protected boolean isCompositeQuery(ConceptualView obj) {
		return (obj instanceof ConceptualCompositeQuery);
	}

	protected EClass getRelatedPackageEntityInstance(ConceptualQuery obj) {
		return 	UddlPackage.eINSTANCE.getConceptualEntity();
	}

}
