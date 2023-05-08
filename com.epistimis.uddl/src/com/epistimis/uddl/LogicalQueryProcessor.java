package com.epistimis.uddl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import com.epistimis.uddl.uddl.LogicalAssociation;
import com.epistimis.uddl.uddl.LogicalCharacteristic;
import com.epistimis.uddl.uddl.LogicalCompositeQuery;
import com.epistimis.uddl.uddl.LogicalComposition;
import com.epistimis.uddl.uddl.LogicalEntity;
import com.epistimis.uddl.uddl.LogicalParticipant;
import com.epistimis.uddl.uddl.LogicalQuery;
import com.epistimis.uddl.uddl.LogicalQueryComposition;
import com.epistimis.uddl.uddl.LogicalView;
import com.epistimis.uddl.uddl.UddlPackage;

public class LogicalQueryProcessor extends
		QueryProcessor<LogicalCharacteristic, LogicalEntity, LogicalAssociation, LogicalParticipant, LogicalView, LogicalQuery, LogicalCompositeQuery, LogicalQueryComposition> {

	protected EList<LogicalQueryComposition> getComposition(LogicalCompositeQuery ent) {
		return ent.getComposition();
	}

	protected LogicalView getType(LogicalQueryComposition obj) {
		return obj.getType();
	}

	
	protected boolean isCompositeQuery(LogicalView obj) {
		return (obj instanceof LogicalCompositeQuery);
	}

	protected EClass getRelatedPackageEntityInstance(LogicalQuery obj) {
		return 	UddlPackage.eINSTANCE.getLogicalEntity();
	}


	protected List<LogicalCharacteristic> getCharacteristics(LogicalEntity obj) {
		
		List<LogicalCharacteristic> characteristics = new ArrayList<>();
		for (LogicalComposition pc: obj.getComposition()) {
			characteristics.add(pc);
		}
		if (obj instanceof LogicalAssociation) {
			for (LogicalParticipant pp: ((LogicalAssociation)obj).getParticipant()) {
				characteristics.add(pp);
			}
		}
		return characteristics;
	}
	protected String getCharacteristicRolename(LogicalCharacteristic obj) { return obj.getRolename(); }

}
