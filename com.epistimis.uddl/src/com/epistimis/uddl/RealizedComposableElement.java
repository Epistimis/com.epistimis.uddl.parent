package com.epistimis.uddl;


import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jdt.annotation.NonNull;

import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.PlatformComposableElement;
import com.google.common.base.Optional;


public class RealizedComposableElement {

	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * This is, essentially, a compiler name table. It contains the results of processing all the composable elements in the model to
	 * get the net realization.  This can be used as is, or can be used by the QueryProcessor when selecting Entities, Associations, and Compositions.
	 * 
	 * Note that this contains Platform level info
	 */
	public static Map<PlatformComposableElement,RealizedComposableElement> allComposable2Realized = new HashMap<PlatformComposableElement, RealizedComposableElement>();
	public static Map<RealizedComposableElement,PlatformComposableElement> allRealized2Composable = new HashMap<RealizedComposableElement,PlatformComposableElement>();

	private String name;

	private String description;

	public RealizedComposableElement(@NonNull PlatformComposableElement pce) {
		this.name = pce.getName(); // Always has to have a name, so just do that
		// Set the description - it might not always have a value
		setDescription(pce);
		
		allComposable2Realized.put(pce, this);
		allRealized2Composable.put(this,pce);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the description - and give it a default empty value (not null)
	 * @param pce
	 */
	public void setDescription(PlatformComposableElement pce) {
		this.description  = Optional.fromNullable(pce.getDescription()).or("").trim();
		
	}

	static final String FMT_STRING = "Could not find realization for type [{0}] and role [{1}] when processing [{2}] with description [{3}]";
	
	/**
	 * Use the maps to match types. This uses the PlatformComposableElement instances as keys, not FQNs. This should work but it is based on
	 * object identity.
	 */
	public static void linkTypes() {
		for (Map.Entry<PlatformComposableElement, RealizedComposableElement> entry: allComposable2Realized.entrySet()) {
			PlatformComposableElement pce = entry.getKey();
			RealizedComposableElement rce = entry.getValue();
			if (rce instanceof RealizedEntity) {
				RealizedEntity re = (RealizedEntity) rce;
				for (RealizedComposition rc: re.getComposition().values()) {
					PlatformComposableElement type = IndexUtilities.unProxiedEObject(rc.getType(),pce);
					RealizedComposableElement realizedType = allComposable2Realized.get(type);
					if (realizedType == null) {
						String typename = (type != null ? type.getName() : "null");
						logger.warn(MessageFormat.format(FMT_STRING,typename,rc.getRolename(), re.getName(), re.getDescription()));
					}
					rc.setRealizedType(realizedType);
				}
			}
		}
	}
}
