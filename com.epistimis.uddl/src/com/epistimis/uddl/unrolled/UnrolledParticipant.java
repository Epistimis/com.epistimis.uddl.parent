package com.epistimis.uddl;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.eclipse.jdt.annotation.NonNull;

import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformParticipant;

public class RealizedParticipant extends RealizedCharacteristic {

	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	/**
	 * Track the original type because we need this later to do linkage
	 */
	private @NonNull PlatformEntity type;

	private int sourceLowerBound;

	private int sourceUpperBound;

	public RealizedParticipant(@NonNull String rolename) {
		super(rolename);
		// TODO Auto-generated constructor stub
		sourceLowerBound = 1;
		sourceUpperBound = 1;
	}
	public RealizedParticipant(@NonNull PlatformParticipant pp, RealizedComposableElement rce) {
		super(pp,rce);
		this.type = IndexUtilities.unProxiedEObject(pp.getType(),pp);
		sourceLowerBound = pp.getSourceLowerBound();
		sourceUpperBound = pp.getSourceUpperBound();
	}

	public void update(@NonNull PlatformParticipant pc, RealizedComposableElement rce) {
		super.update(pc, rce);

		// TODO: https://app.clickup.com/t/86bx15uh4
		// Characteristic type specialization could be tightened on realization
		if (pc.getSourceLowerBound()  > this.sourceLowerBound) {
			this.sourceLowerBound = pc.getSourceLowerBound();
		}
		if (pc.getSourceUpperBound()  < this.sourceUpperBound) {
			this.sourceUpperBound = pc.getSourceUpperBound();
		}

	}

	public PlatformEntity getType() {
		if (type == null) {
			logger.error("Returning null type from RealizedParticipant " + realizedCharacteristic.toString());
		}
		return this.type;
	}
	
	public int getSourceLowerBound() {
		return sourceLowerBound;
	}
	public int getSourceUpperBound() {
		return sourceUpperBound;
	}

}
