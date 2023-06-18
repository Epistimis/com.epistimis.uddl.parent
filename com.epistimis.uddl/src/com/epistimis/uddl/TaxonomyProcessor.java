package com.epistimis.uddl;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

import com.epistimis.uddl.scoping.IndexUtilities;
import com.google.inject.Inject;

/**
 * This generic base class is for when we want something more than an enum but less than a full class system. Taxonomies that
 * specialize this processor consist of four grammar rules. In Uddl.xtext, see these rules as an example:
 * LogicalEnumeratedBase:
	LogicalEnumeratedSet |
	LogicalEnumerationLabel |
	LogicalEnumerated
 * 
 * @param <Base> This is the type returned by the base rule (by default spelled the same as the base rule name)
 */
public abstract class TaxonomyProcessor<Base extends EObject> {
	@Inject
	IndexUtilities ndxUtil;

	@Inject
	IQualifiedNameProvider qnp;

	public TaxonomyProcessor() {
		// TODO Auto-generated constructor stub
	}
	public enum TriBool {
		FALSE, SOMETIMES, TRUE, UNNKNOWN
	};

	abstract public EClass getBaseMetaClass();
	abstract public boolean isCastableToBase(EObject obj);
	abstract public String getBaseName(EObject obj);
	
	/**
	 * For generic classes, I sometimes need to know the type parameters. This general method
	 * gets them for me
	 * @param ndx The 0-based index of the type parameter I want
	 * @return The Class instance of the type
	 */
	public Class<?> returnedTypeParameter(int ndx) {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class<?>) parameterizedType.getActualTypeArguments()[ndx];
	}

	/**
	 * 
	 * @return The taxonomy base class underpinning the specializing type
	 */
	public Class getBaseType() {
		return returnedTypeParameter(0);
	}

	/**
	 * From start, walk up containment hierarchy
	 * 
	 * @param start
	 * @return
	 */
	public  Collection<Base> collectAncestors(Base start, Class realType) {
		List<Base> ancestors = new ArrayList<>();
		EObject current = start;
		while ((current != null) && realType.isInstance(current)) {
			ancestors.add((Base) current);
			current = current.eContainer();
		}

		return ancestors;
	}

	/**
	 * Get all the descendants of the starting point (including the starting point)
	 * 
	 * @param start
	 * @return
	 */
	public  Collection<Base> collectDescendants(Base start) {
		List<Base> descendants = new ArrayList<>();
		descendants.add(start);
		TreeIterator<EObject> titer = start.eAllContents();
		while (titer.hasNext()) {
			Base jb = (Base) titer.next();
			descendants.add(jb);
		}

		return descendants;
	}

	/**
	 * Is the test value anywhere in the ancestry of start. In other words,
	 * is 'start' contained in the 'test' hierarchy?
	 * @param start
	 * @param test
	 * @return
	 */
	public  boolean containedIn(Base start, String test) {
		Collection<Base> ancestors = collectAncestors(start, getBaseType());
		return ancestors.stream().anyMatch(a -> getBaseName(a).equalsIgnoreCase(test));
	}

	/**
	 * The invalidValue is not in the taxonomic hierarchy starting at the container
	 * @param container
	 * @param invalidValue
	 */
	public static  void invalidValue(String container, String invalidValue) {
		System.out.println(MessageFormat.format("Container {0} contains an invalid value: {1}",
				container, invalidValue));
		
	}

	/**
	 * Look up the object with the specified name, as visible from the context instance
	 * (In case we get a RQN and there is more than one)
	 * 
	 * @param name
	 * @return The found Object, or null if we can't find it or can't determine
	 *         which it is. TODO: Should probably throw exceptions for failure here.
	 */
	public Base getObjectForName(String name, EObject context) {

		EObject inst = ndxUtil.getUniqueObjectForName(context, getBaseMetaClass(), name);
		if (inst == null) {
			return null;
		}
		if (isCastableToBase(inst)) {
			return (Base)inst;
		}

		/** If we get here, this isn't a purpose */
		invalidValue(name,inst.toString());
		return null;
	}

	/**
	 * Get a list of Objects, one per name. Will never be null or contain nulls but
	 * the list itself may be empty.
	 * 
	 * @param names
	 * @return a (possibly empty) list of objects.
	 */
	public  List<Base> getObjectsForNames(List<String> names, EObject context) {
		List<Base> result = new ArrayList<Base>();
		for (String n : names) {
			Base p = getObjectForName(n, context);
			if (p != null) {
				result.add(p);
			}
		}
		return result;
	}


	/**
	 * When we want to check to see if an invariant affects something in a taxonomy, we need to check for 
	 * invariants associated with the concept under consideration. However, when concepts 
	 * can be defined & used hierarchically, we also need to consider:
	 * 
	 * A) The entire containment hierarchy for the specified concept - because 'this' concept
	 * 'is-a' concept for each of its containers. Because each concept specializes its containers,
	 * every invariant that applies to one of its containers necessarily applies to it as well.
	 * 
	 * B) Its entire contents (everything contained within the concept under consideration) - 
	 * because any code used for 'this' concept could be used for any of its specializing Purposes. 
	 * Since invariants / constraints must be true for the specified concept, they must be true for
	 * all instances of that concept, which include all specializations of that concept.  A)
	 * differs from B) in that A) is a guaranteed problem, whereas B) could be a problem.
	 * 
	 * This implies several things:
	 * 1) The concept hierarchy should be constructed assiduously.
	 * 2) Constraints/invariants/checks should be associated with concept carefully
	 * 
	 * Without both of these, constraints can have unintended far reaching impact.
	 * 
	 * @param p
	 * @return
	 */
	public  List<Base> getAffectingConcepts(Base p) {
		List<Base> results = new ArrayList<Base>();
		results.add(p);
		/**
		 * Get all the containment hierarchy
		 */
		EObject current = (EObject) p;
		while ((current.eContainer() != null) && (isCastableToBase(current.eContainer()))) {
			current =  current.eContainer();
			results.add((Base)current);
		}
		/**
		 * We also get all the content of the original concept, if it is a set.
		 * eAllContents returns a TreeIterator so we don't need to recurse separately
		 */
		for (EObject tp : IteratorExtensions.<EObject>toIterable(((EObject)p).eAllContents())) {
			if (isCastableToBase(tp)) {
				results.add((Base) tp);
			} else {
				invalidValue(qnp.getFullyQualifiedName(tp).toString(),tp.toString());			
			}
		}
		return results;
	}

	/**
	 * Check to see if the testedHierarchy overlaps with the hierarchyToCheck.
	 * There are three possible results: 1) FALSE - the testedHierarchy is
	 * completely unrelated to the hierarchyToCheck - this occurs when they have
	 * no common ancestor or when they have a common ancestor but one is not the
	 * ancestor of the other 2) UNNKNOWN - When TRUE and FALSE are not appropriate
	 * 3) TRUE - if the testedHierarchy is wholly contained in the
	 * hierarchyToCheck (includes when they are they same)
	 * 
	 * TODO: A strictly hierarchical definition of Hierarchies cannot handle
	 * EU/Schengen area (https://en.wikipedia.org/wiki/Schengen_Area) - or anywhere
	 * hierarchies intersect but aren't nested. This must be addressed at some
	 * point.
	 * 
	 * @param testedHierarchy
	 * @param hierarchyToCheck
	 * @return
	 */
	public TriBool intersectingHierarchies(Base testedHierarchy,
			Base hierarchyToCheck) {

		/**
		 * Quickly check to see if they are equal
		 */
		if (testedHierarchy == hierarchyToCheck)
			return TriBool.TRUE;

		/**
		 * Get the entire lineage of both - if the hierarchyToCheck is an ancestor of
		 * the testedHierarchy, then yes
		 */
		Collection<Base> tHAncestors = collectAncestors(testedHierarchy, getBaseType());
		if (tHAncestors.contains(hierarchyToCheck))
			return TriBool.TRUE;

		/**
		 * If the testedHierarchy is an ancestor of the hierarchyToCheck, then
		 * MAYBE (because we might be dealing with something in the hierarchyToCheck)
		 */
		Collection<Base> hTCAncestors = collectAncestors(hierarchyToCheck, getBaseType());
		if (hTCAncestors.contains(testedHierarchy))
			return TriBool.SOMETIMES;

		/**
		 * Should this be FALSE or UNKNOWN?
		 */
		return TriBool.FALSE;
	}

}
