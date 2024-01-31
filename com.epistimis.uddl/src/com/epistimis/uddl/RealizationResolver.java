/**
 * 
 */
package com.epistimis.uddl;

import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

import com.epistimis.uddl.uddl.PlatformAssociation;
import com.epistimis.uddl.uddl.PlatformComposableElement;
import com.epistimis.uddl.uddl.PlatformDataType;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * 
 */
public class RealizationResolver {

	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	IQualifiedNameProvider qnp;

	/**
	 * 'Realize' and resolve all the EObjects in the specified resource. The results
	 * are cached.
	 * 
	 * @param resource
	 */
	public static void resolve(Resource resource) {

		ResourceSet rs = resource.getResourceSet();
		resolve(rs);
	}

	/**
	 * 'Realize' and resolve all the EObjects in the specified ResourceSet. The
	 * results are cached.
	 * 
	 * @param resource
	 */
	public static void resolve(ResourceSet rs) {

		/**
		 * Before doing anything else, resolve all ECore cross references - because we
		 * don't know if some have been lazy linked
		 */
		// EcoreUtil2.resolveAll(rs);

		// Before creating new instances, check to see if we already have an instance.
		// TODO: We need a way to determine if the AST has changed so we can invalidate the cache.
		// How do we do that? Since the cache is keyed by instance ID, it is sufficient that a change
		// will cause a new instance to be created, so the old instance won't be found? The problem
		// is that approach leaves the old instances in the cache, just unused - which creates a memory
		// leak. We need to know to remove old instances. Alternatively, we just always flush the cache
		// at this point - which is what we do here.
		RealizedComposableElement.allComposable2Realized.clear();
		RealizedComposableElement.allRealized2Composable.clear();

		for (Resource res : rs.getResources()) {
			/**
			 * This will only collect all the realization info from a PlatformEntity ->
			 * LogicalEntity -> ConceptualEntity to determine what is available. The results
			 * can then be used to create instances, generate code, or ....
			 */
			final Iterable<PlatformComposableElement> elements = Iterables.<PlatformComposableElement>filter(
					IteratorExtensions.<EObject>toIterable(res.getAllContents()), PlatformComposableElement.class);
				
			for (final PlatformComposableElement elem : elements) {
				if ((elem instanceof PlatformAssociation)) {
					new RealizedAssociation(((PlatformAssociation) elem));
				} else {
					if ((elem instanceof PlatformDataType)) {
						new RealizedDataType(((PlatformDataType) elem));
					} else {
						if ((elem instanceof PlatformEntity)) {
							new RealizedEntity(((PlatformEntity) elem));
						} else {
							logger.warn(MessageFormat.format("No processing available for type {0}",
									elem.getClass().toString()));
						}
					}
				}
			}
		}
		/**
		 * Now go back and link all the PlatformEntity types
		 * 
		 */
		RealizedComposableElement.linkTypes();
	}
}
