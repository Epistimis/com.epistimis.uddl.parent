/**
 * 
 */
package com.epistimis.uddl;

import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.PlatformAssociation;
import com.epistimis.uddl.uddl.PlatformComposableElement;
import com.epistimis.uddl.uddl.PlatformDataType;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.unrolled.UnrollingFactoryP;
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

		UnrollingFactoryP fp = new UnrollingFactoryP();
		fp.resolve(rs);
//		/**
//		 * Before doing anything else, resolve all ECore cross references - because we
//		 * don't know if some have been lazy linked
//		 */
//		// EcoreUtil2.resolveAll(rs);
//
//		// Before creating new instances, check to see if we already have an instance.
//		// TODO: We need a way to determine if the AST has changed so we can invalidate the cache.
//		// How do we do that? Since the cache is keyed by instance ID, it is sufficient that a change
//		// will cause a new instance to be created, so the old instance won't be found? The problem
//		// is that approach leaves the old instances in the cache, just unused - which creates a memory
//		// leak. We need to know to remove old instances. Alternatively, we just always flush the cache
//		// at this point - which is what we do here.
//		UnrolledComposableElement.allComposable2Realized.clear();
//		UnrolledComposableElement.allRealized2Composable.clear();
//
//		for (Resource res : rs.getResources()) {
//			/**
//			 * This will only collect all the realization info from a PlatformEntity ->
//			 * LogicalEntity -> ConceptualEntity to determine what is available. The results
//			 * can then be used to create instances, generate code, or ....
//			 */
//			final Iterable<PlatformComposableElement> elements = Iterables.<PlatformComposableElement>filter(
//					IteratorExtensions.<EObject>toIterable(res.getAllContents()), PlatformComposableElement.class);
//				
//			for (final PlatformComposableElement elem : elements) {
//				if ((elem instanceof PlatformAssociation)) {
//					new UnrolledAssociation(((PlatformAssociation) elem));
//				} else {
//					if ((elem instanceof PlatformDataType)) {
//						new UnrolledDataType(((PlatformDataType) elem));
//					} else {
//						if ((elem instanceof PlatformEntity)) {
//							new RealizedEntity(((PlatformEntity) elem));
//						} else {
//							logger.warn(MessageFormat.format("No processing available for type {0}",
//									elem.getClass().toString()));
//						}
//					}
//				}
//			}
//		}
//		/**
//		 * Now go back and link all the PlatformEntity types
//		 * 
//		 */
//		UnrolledComposableElement.linkTypes();
	}
	
//	static final String FMT_STRING = "Could not find realization for type [{0}] and role [{1}] when processing [{2}] with description [{3}]";
//	/**
//	 * Use the maps to match types. This uses the PlatformComposableElement instances as keys, not FQNs. This should work but it is based on
//	 * object identity.
//	 */
//	public void linkTypes() {
//		for (Map.Entry<ComposableElement, RealizedComposableElement<ComposableElement>> entry: getC2REntrySet()) {
//			ComposableElement ce = entry.getKey();
//			RealizedComposableElement<ComposableElement> rce = entry.getValue();
//			if (rce instanceof RealizedEntity) {
//				RealizedEntity re = (RealizedEntity) rce;
//				for (RealizedComposition rc: re.getComposition().values()) {
//					ComposableElement type = IndexUtilities.unProxiedEObject(rc.getType(),ce);
//					RealizedComposableElement<ComposableElement> realizedType = allComposable2Unrolled.get(type);
//					if (realizedType == null) {
//						String typename = (type != null ? type.getName() : "null");
//						logger.warn(MessageFormat.format(FMT_STRING,typename,rc.getRolename(), re.getName(), re.getDescription()));
//					}
//					rc.setRealizedType(realizedType);
//				}
//			}
//		}
//	}

}
