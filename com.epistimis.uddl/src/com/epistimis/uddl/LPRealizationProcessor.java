/**
 * 
 */
package com.epistimis.uddl;

import com.epistimis.uddl.uddl.LogicalCharacteristic;
import com.epistimis.uddl.uddl.LogicalComposition;
import com.epistimis.uddl.uddl.LogicalEntity;
import com.epistimis.uddl.uddl.LogicalParticipant;
import com.epistimis.uddl.uddl.PlatformCharacteristic;
import com.epistimis.uddl.uddl.PlatformComposition;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformParticipant;

/**
 * 
 */
public class LPRealizationProcessor extends
		RealizationProcessor<LogicalEntity, PlatformEntity, LogicalCharacteristic, PlatformCharacteristic, LogicalComposition, PlatformComposition, LogicalParticipant, PlatformParticipant, LogicalEntityProcessor, PlatformEntityProcessor> {

	@Override
	public LogicalEntity getRealizeEntity(PlatformEntity rent) {
		// TODO Auto-generated method stub
		return rent.getRealizes();
	}

	@Override
	public LogicalComposition getRealizedComposition(PlatformComposition rcomp) {
		// TODO Auto-generated method stub
		return rcomp.getRealizes();
	}

	@Override
	public LogicalParticipant getRealizedParticipant(PlatformParticipant rpart) {
		// TODO Auto-generated method stub
		return rpart.getRealizes();
	}

}
