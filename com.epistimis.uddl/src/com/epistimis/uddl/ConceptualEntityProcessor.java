/**
 * 
 */
package com.epistimis.uddl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtext.naming.IQualifiedNameProvider;

import com.epistimis.uddl.uddl.ConceptualAssociation;
import com.epistimis.uddl.uddl.ConceptualCharacteristic;
import com.epistimis.uddl.uddl.ConceptualComposition;
import com.epistimis.uddl.uddl.ConceptualEntity;
import com.epistimis.uddl.uddl.ConceptualParticipant;
import com.epistimis.uddl.uddl.UddlPackage;
import com.google.inject.Inject;

/**
 * 
 */
public class ConceptualEntityProcessor extends
		EntityProcessor<ConceptualCharacteristic, ConceptualEntity, ConceptualAssociation, ConceptualComposition, ConceptualParticipant> {

	@Inject
	IQualifiedNameProvider qnp; // = new UddlQNP();

	@Override
	public EClass getEntityEClass() {
		// TODO Auto-generated method stub
		return UddlPackage.eINSTANCE.getConceptualEntity();

	}

	public String getCharacteristicRolename(ConceptualCharacteristic obj) {
		return obj.getRolename();
	}

	@Override
	public ConceptualEntity getSpecializes(ConceptualEntity ent) {
		// TODO Auto-generated method stub
		return ent.getSpecializes();
	}

	@Override
	public boolean isAssociation(ConceptualEntity ent) {
		// TODO Auto-generated method stub
		return (ent instanceof ConceptualAssociation);
	}

	@Override
	public ConceptualAssociation conv2Association(ConceptualEntity ent) {
		// TODO Auto-generated method stub
		return (ConceptualAssociation) ent;
	}

	@Override
	public EList<ConceptualComposition> getComposition(ConceptualEntity obj) {
		// TODO Auto-generated method stub
		return obj.getComposition();
	}

	@Override
	public EList<ConceptualParticipant> getParticipant(ConceptualAssociation obj) {
		// TODO Auto-generated method stub
		return obj.getParticipant();
	}

}
