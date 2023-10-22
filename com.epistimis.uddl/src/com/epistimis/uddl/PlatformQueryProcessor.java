package com.epistimis.uddl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import com.epistimis.uddl.uddl.PlatformAssociation;
import com.epistimis.uddl.uddl.PlatformCharacteristic;
import com.epistimis.uddl.uddl.PlatformCompositeQuery;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformParticipant;
import com.epistimis.uddl.uddl.PlatformQuery;
import com.epistimis.uddl.uddl.PlatformQueryComposition;
import com.epistimis.uddl.uddl.PlatformView;
import com.epistimis.uddl.uddl.UddlPackage;

public class PlatformQueryProcessor extends
		QueryProcessor<PlatformCharacteristic, PlatformEntity, PlatformAssociation, PlatformParticipant, PlatformView, PlatformQuery, PlatformCompositeQuery, PlatformQueryComposition> {

	protected EList<PlatformQueryComposition> getComposition(PlatformCompositeQuery ent) {
		return ent.getComposition();
	}

	protected PlatformView getType(PlatformQueryComposition obj) {
		return obj.getType();
	}

	protected boolean isCompositeQuery(PlatformView obj) {
		return (obj instanceof PlatformCompositeQuery);
	}

	protected EClass getRelatedPackageEntityInstance(PlatformQuery obj) {
		return UddlPackage.eINSTANCE.getPlatformEntity();
	}

	protected String getCharacteristicRolename(PlatformCharacteristic obj) { return obj.getRolename(); }

}
