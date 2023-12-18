/**
 * 
 */
package com.epistimis.uddl;

import org.eclipse.emf.common.util.EList;

import com.epistimis.uddl.uddl.PlatformAssociation;
import com.epistimis.uddl.uddl.PlatformCharacteristic;
import com.epistimis.uddl.uddl.PlatformComposition;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformParticipant;

/**
 * 
 */
public class PlatformEntityProcessor extends
		EntityProcessor<PlatformCharacteristic, PlatformEntity, PlatformAssociation, PlatformComposition, PlatformParticipant> {


	public String getCharacteristicRolename(PlatformCharacteristic obj) {
		return obj.getRolename();
	}

	@Override
	public PlatformEntity getSpecializes(PlatformEntity ent) {
		// TODO Auto-generated method stub
		return ent.getSpecializes();
	}

	@Override
	public boolean isAssociation(PlatformEntity ent) {
		// TODO Auto-generated method stub
		return (ent instanceof PlatformAssociation);
	}

	@Override
	public PlatformAssociation conv2Association(PlatformEntity ent) {
		// TODO Auto-generated method stub
		return (PlatformAssociation)ent;
	}

	@Override
	public EList<PlatformComposition> getComposition(PlatformEntity obj) {
		// TODO Auto-generated method stub
		return obj.getComposition();
	}

	@Override
	public EList<PlatformParticipant> getParticipant(PlatformAssociation obj) {
		// TODO Auto-generated method stub
		return obj.getParticipant();
	}

}
