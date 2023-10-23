package com.epistimis.uddl.scoping

import com.epistimis.uddl.uddl.UddlPackage
import com.google.inject.Inject
import java.text.MessageFormat
import java.util.List
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.resource.IContainer
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider
import java.util.ArrayList
import com.epistimis.uddl.exceptions.NamedObjectNotFoundException
import com.epistimis.uddl.exceptions.NameCollisionException
import java.util.Collections
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter
import org.eclipse.emf.common.notify.Adapter
import java.util.LinkedList
import java.util.Collection
import org.eclipse.emf.ecore.EStructuralFeature.Setting
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
//import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer
import org.eclipse.emf.ecore.EStructuralFeature

/**
 * This is modified from the book (See https://github.com/LorenzoBettini/packtpub-xtext-book-2nd-examples)
 * Smalljava = SmallJavaIndex.xtend
 */
class IndexUtilities {
	@Inject ResourceDescriptionsProvider rdp
	@Inject IContainer.Manager cm
	@Inject IQualifiedNameProvider qnp

	def getVisibleEObjectDescriptions(EObject o, EClass type) {
		o.getVisibleContainers.map [ container |
			container.getExportedObjectsByType(type)
		].flatten
	}

	def getVisibleObjects(EObject context, EClass type) {
		context.getVisibleEObjectDescriptions(type).map([
			context.eResource.objectFromDescription(it)
		]);
	}

	/**
	 * Find all containers visible from this object 
	 */
	def getVisibleContainers(EObject o) {
		val index = rdp.getResourceDescriptions(o.eResource)
		val rd = index.getResourceDescription(o.eResource.URI)
		// TODO: rd should never be null - but occasionally it is
		// For reasons I don't yet understand. This avoids an NPE.
		if (rd !== null) {
			cm.getVisibleContainers(rd, index)
		} else {
			return new ArrayList();
		}
	}

	def getResourceDescription(EObject o) {
		val index = rdp.getResourceDescriptions(o.eResource)
		index.getResourceDescription(o.eResource.URI)
	}

	def getExportedEObjectDescriptions(EObject o) {
		o.getResourceDescription.getExportedObjects
	}

	def getExportedEObjectDescriptionsByType(EObject o, EClass type) {
		o.getResourceDescription.getExportedObjectsByType(type)
	}

	def getVisibleExternalEObjectDescriptionsByType(EObject o, EClass type) {
		val allVisibleEObjectDescriptions = o.getVisibleEObjectDescriptions(type)
		val allExportedEObjectDescriptions = o.getExportedEObjectDescriptionsByType(type)
		val difference = allVisibleEObjectDescriptions.toSet
		difference.removeAll(allExportedEObjectDescriptions.toSet)
		return difference.toMap[qualifiedName]
	}

	/**
	 * Find all visible EObjects that match this type and name. The name may be 
	 * a leaf, RQN, or FQN. Note the following:
	 * 1) We are checking the name from the leaf up. The assumption here is that
	 * in most cases, a leaf or RQN will be used - so starting with that comparison
	 * will return success more quickly.
	 * 2) If the leaf is not in the name, then the name won't match at all - so 
	 * there is no point in continuing to check.
	 * 
	 * NOTE: This defaults to ignoring case. You can specify if you want to use case with
	 * an optional 4th parameter.
	 * @param context The context to search
	 * @param type The EClass to search for
	 * @param name The name of the instance to search for
	 * 
	 * @return A list of all the all the objects visible in this context of that type
	 * with that name
	 */
	def searchAllVisibleEObjectDescriptions(EObject context, EClass type, String name) {
		return searchAllVisibleEObjectDescriptions(context, type, name, true);
	}

	/**
	 * Find all visible EObjects that match this type and name. The name may be 
	 * a leaf, RQN, or FQN. Note the following:
	 * 1) We are checking the name from the leaf up. The assumption here is that
	 * in most cases, a leaf or RQN will be used - so starting with that comparison
	 * will return success more quickly.
	 * 2) If the leaf is not in the name, then the name won't match at all - so 
	 * there is no point in continuing to check.
	 * 
	 * @param context The context to search
	 * @param type The EClass to search for
	 * @param name The name of the instance to search for
	 * @param ignoreCase true to ignore case, false to use case
	 * 
	 * NOTE: This method returns a synchronized list just in case there is a threading issue
	 * @return A list of all the all the objects visible in this context of that type
	 * with that name

	 */
	def searchAllVisibleEObjectDescriptions(EObject context, EClass type, String name, boolean ignoreCase) {

		Collections.synchronizedList( context.getVisibleEObjectDescriptions(type).filter [
			val QualifiedName qn = it.getQualifiedName();
			for (var i = qn.getSegmentCount() - 1; i >= 0; i--) {
				val rqn = qn.skipFirst(i).toString();

				val int ndx = ignoreCase ? name.toLowerCase.indexOf(rqn.toLowerCase) : name.indexOf(rqn);
				if (ndx == -1) {
					return false; // can't possibly match, so exit without trying anything more
				} else if (ndx == 0) {
					return true; // matches - return success
				} else {
					// keep going - and try a longer QN
				}
			}
			/**
			 * If we get here, there wasn't a match on this description
			 */
			return false;
		].toList);
	}

	/**
	 * 
	 */
	def searchAllVisibleObjects(EObject context, EClass type, String name) {
		context.searchAllVisibleEObjectDescriptions(type, name).map([
			context.eResource.objectFromDescription(it)
		]);
	}

	def static objectFromDescription(Resource res, IEObjectDescription desc) {
		if (desc === null)
			return null;
		var EObject o = desc.getEObjectOrProxy();
		if (o.eIsProxy()) {
			o = res.getResourceSet().getEObject(desc.getEObjectURI(), true);
		}
		return o;
	}

	/**
	 * Find a unique object of the specified type and name. Log errors if none or more
	 * than one is found.
	 */
	def getUniqueObjectForName(EObject context, EClass type, String name) {
		val List<EObject> objList = searchAllVisibleObjects(context, type, name);
		switch objList.size() {
			case 0: {
				val msg = MessageFormat.format("No EObject found with name {0}", name)
				throw new NamedObjectNotFoundException(msg);
			}
			case 1:
				objList.get(0)
			default: {
				throw new NameCollisionException(printEObjectNameCollisions(name, type.getName(), objList));
				//return null;
			}
		}
	}

	static String COLLISION_MSG_FMT = "{0} makes ambiguous reference to instances of type {1}. It could be: \n";
	/**
	 * It is possible that there are name collisions from searches. This is a standard way to list them.
	 * @param qn A string version of a possibly qualified name
	 * @param typeName The type we were looking for
	 * @param descriptions What we found
	 */
	def printIEObjectDescriptionNameCollisions(String qn, String typeName, List<IEObjectDescription> descriptions) {
		var msg = MessageFormat.format(COLLISION_MSG_FMT,qn,typeName);
		for (IEObjectDescription d : descriptions) {
			msg += MessageFormat.format("\t {0}\n",d.getQualifiedName().toString());
		}
		return msg;
	}

	/**
	 * It is possible that there are name collisions from searches. This is a standard way to list them.
	 * @param qn A string version of a possibly qualified name
	 * @param typeName The type we were looking for
	 * @param objects What we found
	 */
	def printEObjectNameCollisions(String qn, String typeName, List<EObject> objects) {
		var msg = MessageFormat.format(COLLISION_MSG_FMT,qn,typeName);
		for (EObject o : objects) {
			msg += MessageFormat.format("\t {0}\n",qnp.getFullyQualifiedName(o).toString());
		}
		return msg;
	}

	/**
	 * These methods are specific to this package
	 */
	def getVisibleDataModelDescriptions(EObject o) {
		o.getVisibleEObjectDescriptions(UddlPackage.eINSTANCE.dataModel)
	}

	def getExportedDataModelEObjectDescriptions(EObject o) {
		o.getResourceDescription.getExportedObjectsByType(UddlPackage.eINSTANCE.dataModel)
	}

	def getVisibleExternalDataModelDescriptions(EObject o) {
		o.getVisibleExternalEObjectDescriptionsByType(UddlPackage.eINSTANCE.dataModel)
	}
/*	
 * The original version of this method is below. This was recoded to use the general
 * implementation to make any maintenance simpler.
 */
/*
 * 	def getVisibleExternalClassesDescriptions(EObject o) {
 * 		val allVisibleClasses =
 * 			o.getVisibleClassesDescriptions
 * 		val allExportedClasses =
 * 			o.getExportedClassesEObjectDescriptions
 * 		val difference = allVisibleClasses.toSet
 * 		difference.removeAll(allExportedClasses.toSet)
 * 		return difference.toMap[qualifiedName]
 * 	}
 */
 
 	/**
 	 * Find all the resources that reference the referenceTarget
 	 * from: https://www.eclipse.org/forums/index.php/t/165076/
 	 * NOTE: This is from an old post - UsageCrossReferencer is likely newer
 	 */
	 def List<Resource> getReferencingResources(EObject referenceTarget) {
		val Resource res = referenceTarget.eResource();
		val ResourceSet rs = res.getResourceSet();
		var ECrossReferenceAdapter crossReferencer = null;
		var List<Adapter> adapters = rs.eAdapters();
		for (Adapter adapter : adapters) {
			if(adapter instanceof ECrossReferenceAdapter){
				crossReferencer =  adapter as ECrossReferenceAdapter;
				//break;
			}
		}
		if(crossReferencer === null){
			crossReferencer = new ECrossReferenceAdapter();
			rs.eAdapters().add(crossReferencer);
		}
		var List<Resource> referers = new LinkedList<Resource>();
		val Collection<Setting> referencers = crossReferencer.getInverseReferences(referenceTarget, true);
		for (Setting setting : referencers) {
			val EObject referer = setting.getEObject();
			val Resource resource = referer.eResource();
			if(!resource.equals(res)){
				// Only need to check the other resources not containing the referenceTarget
				var URI uri = resource.getURI();
				uri = rs.getURIConverter().normalize(uri);
				if(uri.isPlatformResource()){
					referers.add(resource);
				}
			}
		}
		return referers;
	}
	
	/**
	 * Find all the cross references to the specified target, no matter
	 * where they are in the resource set
	 * 
	 * See EcoreUtil.delete() for an example of why it might be done this way
	 */
 	def Collection<EStructuralFeature.Setting> getReferencingObjects(EObject referenceTarget) {
		val Resource res = referenceTarget.eResource();
		val ResourceSet rs = res.getResourceSet();
 		return EcoreUtil.UsageCrossReferencer.find(referenceTarget,rs);
 	}
}
