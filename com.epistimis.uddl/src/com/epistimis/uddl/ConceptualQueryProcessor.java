package com.epistimis.uddl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import com.epistimis.uddl.uddl.ConceptualAssociation;
import com.epistimis.uddl.uddl.ConceptualCharacteristic;
import com.epistimis.uddl.uddl.ConceptualCompositeQuery;
import com.epistimis.uddl.uddl.ConceptualComposition;
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

	protected List<ConceptualCharacteristic> getCharacteristics(ConceptualEntity obj) {
		
		List<ConceptualCharacteristic> characteristics = new ArrayList<>();
		for (ConceptualComposition pc: obj.getComposition()) {
			characteristics.add(pc);
		}
		if (obj instanceof ConceptualAssociation) {
			for (ConceptualParticipant pp: ((ConceptualAssociation)obj).getParticipant()) {
				characteristics.add(pp);
			}
		}
		return characteristics;
	}
	protected String getCharacteristicRolename(ConceptualCharacteristic obj) { return obj.getRolename(); }

}
