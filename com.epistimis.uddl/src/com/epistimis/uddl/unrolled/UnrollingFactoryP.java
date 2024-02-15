/**
 * 
 */
package com.epistimis.uddl.unrolled;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;

import com.epistimis.uddl.uddl.PlatformAssociation;
import com.epistimis.uddl.uddl.PlatformCharacteristic;
import com.epistimis.uddl.uddl.PlatformComposableElement;
import com.epistimis.uddl.uddl.PlatformComposition;
import com.epistimis.uddl.uddl.PlatformDataModel;
import com.epistimis.uddl.uddl.PlatformDataType;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformParticipant;

/**
 * 
 */
public class UnrollingFactoryP extends
		UnrollingFactory<PlatformComposableElement, 
							PlatformCharacteristic, 
							PlatformEntity, 
							PlatformAssociation, 
							PlatformComposition, 
							PlatformParticipant, 
							PlatformDataType, 
							PlatformDataModel,
							UnrolledComposableElementP,
							UnrolledCompositionP,
							UnrolledEntityP,
							UnrolledParticipantP,
							UnrolledAssociationP
							> {



	@Override
	UnrolledEntityP createEntity(PlatformEntity entity) {
		// TODO Auto-generated method stub
		UnrolledEntityP retval = new UnrolledEntityP(entity);
//		updateMaps(entity, retval);
		return retval;
	}


	@Override
	public Set<Entry<PlatformComposableElement, UnrolledComposableElement<PlatformComposableElement>>> getC2REntrySet() {
		return UnrolledComposableElementP.allComposable2Unrolled.entrySet();
	}

	@SuppressWarnings("unchecked") // Return type matches type of map values
	@Override
	public UnrolledComposableElement<PlatformComposableElement> getUnrolledForComposable(PlatformComposableElement type) {
		// TODO Auto-generated method stub
		return  UnrolledComposableElementP.allComposable2Unrolled.get(type);
	}

	@Override
	PlatformComposableElement getType(UnrolledCompositionP uc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Map<String, UnrolledCompositionP> getComposition(UnrolledEntityP entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	boolean isUEntity(Object uce) {
		// TODO Auto-generated method stub
		return (uce instanceof UnrolledEntityP);
	}


	@Override
	void clearMaps() {
		// TODO Auto-generated method stub
		UnrolledComposableElementP.allComposable2Unrolled.clear();
		UnrolledComposableElementP.allUnrolled2Composable.clear();
	}


	@Override
	String getName(PlatformComposableElement obj) {
		return obj.getName();
//		// TODO Auto-generated method stub
//		if (obj instanceof UnrolledComposableElementP) {
//			return ((UnrolledComposableElementP)obj).getName();
//		}
//		if (obj instanceof UnrolledEntityP) {
//			return ((UnrolledEntityP)obj).getName();			
//		}
//		if (obj instanceof UnrolledAssociationP) {
//			return ((UnrolledAssociationP)obj).getName();			
//		}
//
//		return "";
	}

//	@Override
//	UnrolledEntityP createEntity(PlatformEntity entity) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
