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

	@Inject IQualifiedNameProvider qnp;
	
	/**
	 * 'Realize' and resolve all the EObjects in the specified resource. The results are cached.
	 * @param resource
	 */
	public static void resolve(Resource resource) {
		
		ResourceSet rs = resource.getResourceSet();
		resolve(rs);
	}

	/**
	 * 'Realize' and resolve all the EObjects in the specified ResourceSet. The results are cached.
	 * @param resource
	 */
	public static void resolve(ResourceSet rs) {
		
		/**
		 * Before doing anything else, resolve all ECore cross references - because we don't know if some have been lazy linked
		 */
		//EcoreUtil2.resolveAll(rs);
		
		for (Resource res: rs.getResources()) {			
			/**
			 * This will only collect all the realization
			 * info from a PlatformEntity -> LogicalEntity -> ConceptualEntity to determine what is available. The results can then be used
			 * to create instances, generate code, or ....
			 */
			    final Iterable<PlatformComposableElement> elements = Iterables.<PlatformComposableElement>filter(IteratorExtensions.<EObject>toIterable(res.getAllContents()), PlatformComposableElement.class);
			    for (final PlatformComposableElement elem : elements) {
			      if ((elem instanceof PlatformAssociation)) {
			        new RealizedAssociation(((PlatformAssociation)elem));
			      } else {
			        if ((elem instanceof PlatformDataType)) {
			          new RealizedDataType(((PlatformDataType)elem));
			        } else {
			          if ((elem instanceof PlatformEntity)) {
			            new RealizedEntity(((PlatformEntity)elem));
			          } else {
			            logger.warn(MessageFormat.format("No processing available for type {0}",elem.getClass().toString()));
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
