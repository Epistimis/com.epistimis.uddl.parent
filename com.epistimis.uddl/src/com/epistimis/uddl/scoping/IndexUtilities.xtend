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
import java.util.Set
import java.util.HashSet
import org.eclipse.emf.common.util.EList
import org.eclipse.acceleo.query.runtime.IQueryEnvironment
import org.eclipse.acceleo.query.runtime.Query
import org.eclipse.acceleo.query.runtime.impl.QueryBuilderEngine
import org.eclipse.acceleo.query.runtime.IQueryBuilderEngine.AstResult
import org.eclipse.acceleo.query.runtime.impl.QueryEvaluationEngine
import java.util.Map
import com.google.common.collect.Maps
import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.acceleo.query.runtime.EvaluationResult
import org.eclipse.ocl.pivot.values.OrderedSet

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
	 * Identify the EObjects in the closure starting at root and following the specified 
	 * feature. This assumes the feature points from parent to child.
	 * 
	 * @param context  The context from which we start - we search for everything
	 *                 visible in to this context
	 * @param root The root of the hierarchy
	 * @param type The type of object to find
	 * @param feat The feature used to determine the hierarchy
	 * 
	 * Note that to get the full hierarchy, we need the closure that comes from following the specified 
	 * feature through an unknown number of levels. This means we need to loop on all the objects and keep
	 * adding to the found list until the list doesn't change after a complete pass.
	 * 
	 * This version handles features of all cardinality
	 * 
	 * 
	 * @return A set of the EObjects of the specified type in the specified hierarchy
	 *         that are visible from the context
	 */
	def Set<EObject> closure(EObject context, EObject root, EClass type, EStructuralFeature feat) {
		// If this is a collection feature, handle that differently
		if (feat.isMany) {
			return closureColn(context,root, type, feat);
		}

		var Set<EObject> found  = new HashSet<EObject>();
		found.add(root);
		var EObject toCheck = root.eGet(feat)as EObject;
		while ((toCheck !== null) && !found.contains(toCheck)) {
			val EObject temp = toCheck.eGet(feat) as EObject;
			// Log that we found this one, remove it from the list to inspect and add it's children to the
			found.add(toCheck);
			toCheck = temp;				
		}
		return found;
		
	}
	
	/**
	 * Identify the EObjects in the closure starting at root and following the specified 
	 * feature. This assumes the feature points from parent to child.
	 * 
	 * @param context  The context from which we start - we search for everything
	 *                 visible in to this context
	 * @param root The root of the hierarchy
	 * @param type The type of object to find
	 * @param feat The feature used to determine the hierarchy
	 * 
	 * Note that to get the full hierarchy, we need the closure that comes from following the specified 
	 * feature through an unknown number of levels. This means we need to loop on all the objects and keep
	 * adding to the found list until the list doesn't change after a complete pass.
	 * 
	 * This version is for features that are cardinality > 1, use visibleClosure
	 * 
	 * 
	 * @return A set of the EObjects of the specified type in the specified hierarchy
	 *         that are visible from the context
	 */
	def Set<EObject> closureColn(EObject context, EObject root, EClass type, EStructuralFeature feat) {
		var Set<EObject> found  = new HashSet<EObject>();
		found.add(root);
		val EList<EObject> toCheck = root.eGet(feat)as EList<EObject>;
		for (EObject e : toCheck) {
			if (!found.contains(e)) {
				val EList<EObject> temp = e.eGet(feat) as EList<EObject>;
				// Log that we found this one, remove it from the list to inspect and add it's children to the
				found.add(e);
				toCheck.addAll(temp);				
			}
			// Whether this one was new or not, remove it from the set to check. Because we do this here,
			// we also ensure that it can't be added back in if it is found in a another reference (because we are adding
			// newly found elements to a Set (so duplicates just overwrite/ don't get added), and then we remove just once.
			toCheck.remove(e);				
		}						
		return found;
		
	}
	

	/**
	 * Identify the EObjects in the closure starting at root and following the specified 
	 * feature. This assumes the feature points from child to parent, so we follow the inverse of the 
	 * feature.
	 * 
	 * @param context  The context from which we start - we search for everything
	 *                 visible in to this context
	 * @param root The root of the hierarchy
	 * @param type The type of object to find
	 * @param feat The feature used to determine the hierarchy
	 * 
	 * Note that to get the full hierarchy, we need the closure that comes from following the specified 
	 * feature through an unknown number of levels. This means we need to loop on all the objects and keep
	 * adding to the found list until the list doesn't change after a complete pass.
	 * 
	 * use Acceleo Query Language to navigate the EMF model.
	 * 
	 * This assumes the evaluation will return a set of objects
	 * 
	 * 
	 * @return A set of the EObjects of the specified type in the specified hierarchy
	 *         that are visible from the context
	 */
	def Set<EObject> visibleInverseClosure(EObject context, EObject root, EClass type, EStructuralFeature feat) {
		val IQueryEnvironment queryEnvironment = Query.newEnvironmentWithDefaultServices(null);
		queryEnvironment.registerEPackage(UddlPackage.eINSTANCE);
		val QueryEvaluationEngine engine = new QueryEvaluationEngine(queryEnvironment);
		val QueryBuilderEngine builder = new QueryBuilderEngine(queryEnvironment);
		var Map<String, Object> variables = Maps.newHashMap();
		val AstResult astResult = builder.build("root.eInverse("+feat.name+")");
		/* From here we loop through all all found objects drilling down. Always check to see if something found is already
		 * in the list so we don't infinite loop.
		 */
		variables.put("root", root);
		var found = new HashSet<EObject>();
		found.add(root);
		var EvaluationResult evaluationResult = engine.eval(astResult, variables);
		var Set<EObject> toCheck = evaluationResult.result as Set<EObject>;
		for (e: toCheck) {
			if (!found.contains(e)) {
				variables.put("root",e);
				evaluationResult = engine.eval(astResult, variables);
				val Set<EObject> temp =  evaluationResult.result as Set<EObject>;
				// Log that we found this one, remove it from the list to inspect and add it's children to the
				found.add(e);
				toCheck.addAll(temp);				
			}
			// Whether this one was new or not, remove it from the set to check. Because we do this here,
			// we also ensure that it can't be added back in if it is found in a another reference (because we are adding
			// newly found elements to a Set (so duplicates just overwrite/ don't get added), and then we remove just once.
			toCheck.remove(e);
			// Note that we could use removeAll here to remove everything that has already been found from the toCheck set.
			// I think this would be, on average, less efficient. Eventually, each item in toCheck will be compared to found.
			// If we remove them all here, it is possible that they will be added back in and removed multiple times. At worst
			// this will be just as efficient this way as the removeAll way
			
		}		
		return found;		
		
		// An alternative approach
//		/**
//		 * To find all specializations, we must find all references to the root in the collection of all
//		 * EObjects of the appropriate type. Those references will be in the specified' feature.
//		 */
//		val List<EObject> allEObjects = IterableExtensions.toList(getVisibleObjects(context, type));
//
//		/** find the instance */
//		try {
//				found.add(root);
//				val EList<EObject> xrefs = root.eCrossReferences;
//				for (EObject xref: xrefs) {
//					if (root == xref.eGet(feat)) {
//						found.add(xref);
//					}
//				}
//				val java.util.Collection<EStructuralFeature.Setting> usages = EcoreUtil.UsageCrossReferencer
//						.find(root, allEObjects);
//				// Now filter those usages for only the specializes feature - loop
//				for (EStructuralFeature.Setting usage : usages) {
//					if (usage.getEStructuralFeature().equals(feat)) {
//						found.add(usage.getEObject());
//					}
//				}
//			}
//
//		// Once we get here, that set should contain all the CEntity
//		// hierarchy
//		return allEObjects;
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
