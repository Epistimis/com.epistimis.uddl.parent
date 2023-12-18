/**
 * 
 */
package com.epistimis.uddl;

import org.eclipse.emf.common.util.EList;

import com.epistimis.uddl.uddl.LogicalAssociation;
import com.epistimis.uddl.uddl.LogicalCharacteristic;
import com.epistimis.uddl.uddl.LogicalComposition;
import com.epistimis.uddl.uddl.LogicalEntity;
import com.epistimis.uddl.uddl.LogicalParticipant;

/**
 * 
 */
public class LogicalEntityProcessor extends
		EntityProcessor<LogicalCharacteristic, LogicalEntity, LogicalAssociation, LogicalComposition, LogicalParticipant> {


	public String getCharacteristicRolename(LogicalCharacteristic obj) {
		return obj.getRolename();
	}

	@Override
	public LogicalEntity getSpecializes(LogicalEntity ent) {
		// TODO Auto-generated method stub
		return ent.getSpecializes();
	}

	@Override
	public boolean isAssociation(LogicalEntity ent) {
		// TODO Auto-generated method stub
		return (ent instanceof LogicalAssociation);
	}

	@Override
	public LogicalAssociation conv2Association(LogicalEntity ent) {
		// TODO Auto-generated method stub
		return (LogicalAssociation)ent;
	}

	@Override
	public EList<LogicalComposition> getComposition(LogicalEntity obj) {
		// TODO Auto-generated method stub
		return obj.getComposition();
	}

	@Override
	public EList<LogicalParticipant> getParticipant(LogicalAssociation obj) {
		// TODO Auto-generated method stub
		return obj.getParticipant();
	}

}
