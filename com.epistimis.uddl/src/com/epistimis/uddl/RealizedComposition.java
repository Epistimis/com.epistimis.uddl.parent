package com.epistimis.uddl;

import com.epistimis.uddl.uddl.PlatformComposableElement;
import com.epistimis.uddl.uddl.PlatformComposition;

public class RealizedComposition extends RealizedCharacteristic {

	/**
	 * Track the original type because we need this later to do linkage
	 */
	private PlatformComposableElement type;

	/**
	 * The max precision - use this to define a rounding function for this composition?
	 */
	private float precision;


	public RealizedComposition(String rolename) {
		super(rolename);
		// TODO Auto-generated constructor stub
		this.precision = 1;

	}

	public RealizedComposition(PlatformComposition pc, RealizedComposableElement rce) {
		super(pc, rce);
		// TODO Auto-generated constructor stub
		this.type = pc.getType();
		this.precision = pc.getPrecision();

	}

	public void update(PlatformComposition pc, RealizedComposableElement rce) {
		super.update(pc, rce);

		// TODO: https://app.clickup.com/t/86bx15uh4
		// Characteristic type specialization could be tightened on realization
		if (pc.getPrecision()  > this.precision) {
			this.precision = pc.getPrecision();
		}

	}

	public PlatformComposableElement getType() {
		return this.type;
	}

	public float getPrecision() {return this.precision; }

}
