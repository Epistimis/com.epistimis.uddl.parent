package com.epistimis.uddl;


import java.util.HashMap;
import java.util.Map;

import com.epistimis.uddl.uddl.PlatformComposableElement;


public class RealizedComposableElement {

	/**
	 * This is, essentially, a compiler name table. It contains the results of processing all the composable elements in the model to
	 * get the net realization.  This can be used as is, or can be used by the QueryProcessor when selecting Entities, Associations, and Compositions.
	 * 
	 * Note that this contains Platform level info
	 */
	public static Map<PlatformComposableElement,RealizedComposableElement> allComposableElements = new HashMap<PlatformComposableElement, RealizedComposableElement>();

	public RealizedComposableElement(PlatformComposableElement pce) {
		allComposableElements.put(pce, this);
	}

	public static void linkTypes() {
		for (Map.Entry<PlatformComposableElement, RealizedComposableElement> entry: allComposableElements.entrySet()) {
			PlatformComposableElement pce = entry.getKey();
			RealizedComposableElement rce = entry.getValue();
			if (rce instanceof RealizedEntity) {
				RealizedEntity re = (RealizedEntity) rce;
				for (RealizedComposition rc: re.getComposition().values()) {
					PlatformComposableElement type = rc.getType();
					RealizedComposableElement realizedType = allComposableElements.get(type);
					if (realizedType == null) {
						if (type != null) {
							System.out.println("Could not find realization for " + type.getName());
						}
						else {
							System.out.println("Could not find realization for null type for " + rc.getRolename() + " of " +re.getName());
							
						}
					}
					rc.setRealizedType(realizedType);
				}
			}
		}
	}
}
