package com.epistimis.uddl;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.eclipse.jdt.annotation.NonNull;

import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.PlatformComposableElement;
import com.epistimis.uddl.uddl.PlatformComposition;

public class RealizedComposition extends RealizedCharacteristic {

	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Track the original type because we need this later to do linkage
	 */
	private @NonNull PlatformComposableElement type;

	/**
	 * The max precision - use this to define a rounding function for this composition?
	 */
	private float precision;


	public RealizedComposition(@NonNull String rolename) {
		super(rolename);
		// TODO Auto-generated constructor stub
		this.precision = 1;

	}

	public RealizedComposition(@NonNull PlatformComposition pc, RealizedComposableElement rce) {
		super(pc, rce);
		// TODO Auto-generated constructor stub
		this.type = IndexUtilities.unProxiedEObject(pc.getType(), pc);
		this.precision = pc.getPrecision();

	}

	public void update(@NonNull PlatformComposition pc, RealizedComposableElement rce) {
		super.update(pc, rce);

		// TODO: https://app.clickup.com/t/86bx15uh4
		// Characteristic type specialization could be tightened on realization
		if (pc.getPrecision()  > this.precision) {
			this.precision = pc.getPrecision();
		}

	}

	public PlatformComposableElement getType() {
		if (type == null) {
			logger.error("Returning null type from RealizedComposition " + realizedCharacteristic.toString());
		}
		return this.type;
	}

	public float getPrecision() {return this.precision; }

}
