package com.epistimis.uddl;

import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformParticipant;

public class RealizedParticipant extends RealizedCharacteristic {

	/**
	 * Track the original type because we need this later to do linkage
	 */
	private PlatformEntity type;

	private int sourceLowerBound;

	private int sourceUpperBound;

	public RealizedParticipant(String rolename) {
		super(rolename);
		// TODO Auto-generated constructor stub
		sourceLowerBound = 1;
		sourceUpperBound = 1;
	}
	public RealizedParticipant(PlatformParticipant pp, RealizedComposableElement rce) {
		super(pp,rce);
		this.type = pp.getType();
		sourceLowerBound = pp.getSourceLowerBound();
		sourceUpperBound = pp.getSourceUpperBound();
	}

	public void update(PlatformParticipant pc, RealizedComposableElement rce) {
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
		return this.type;
	}
	
	public int getSourceLowerBound() {
		return sourceLowerBound;
	}
	public int getSourceUpperBound() {
		return sourceUpperBound;
	}

}
