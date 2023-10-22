package com.epistimis.uddl;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
//import org.eclipse.xtext.xbase.lib.IteratorExtensions;

import com.epistimis.uddl.exceptions.NameCollisionException;
import com.epistimis.uddl.exceptions.NamedObjectNotFoundException;
import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.Taxonomy;
import com.google.inject.Inject;

/**
 * This generic base class is for when we want something more than an enum but
 * less than a full class system. Taxonomies that specialize this processor
 * consist of four grammar rules. In Uddl.xtext, see these rules as an example:
 * LogicalEnumeratedBase: LogicalEnumeratedSet | LogicalEnumerationLabel |
 * LogicalEnumerated
 * 
 * @param <Base> This is the type returned by the base rule (by default spelled
 *               the same as the base rule name)
 */
public abstract class TaxonomyProcessor<Base extends Taxonomy> {
	@Inject
	IndexUtilities ndxUtil;

	@Inject
	UddlQNP qnp;

	@Inject
	IQualifiedNameConverter qnc;

	static Logger logger = Logger.getLogger(TaxonomyProcessor.class);

	public TaxonomyProcessor() {
		// TODO Auto-generated constructor stub
	}

	public static String DFLT_QN_DELIMITER = "\\.";

	public enum TriBool {
		FALSE, SOMETIMES, TRUE, UNNKNOWN
	};

	abstract public EClass getBaseMetaClass();

	public boolean isCastableToBase(EObject obj) {
		return getBaseType().isInstance(obj);
	}
	
	public String getBaseName(EObject obj) {
		return qnp.getFullyQualifiedName(obj).getLastSegment();
	}

	/**
	 * For generic classes, I sometimes need to know the type parameters. This
	 * general method gets them for me
	 * 
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
	public Class<?> getBaseType() {
		return returnedTypeParameter(0);
	}

	/**
	 * Is the test value anywhere in the ancestry of start. In other words, is
	 * 'start' contained in the 'test' hierarchy?
	 * 
	 * If this returns true, it also means that 'start' is a valid value for the
	 * 'test' label (if you treat 'test' as defining a set of values, then 'start'
	 * can be one of them).
	 * 
	 * NOTE: Because these are EObjects with names, we compare qualified names,
	 * which may be relative	 * 
	 * @param start
	 * @param test
	 * @return
	 */
	public boolean containedIn(@NonNull Base start, @NonNull String test) {
		return containedIn(start,test,qnp);
	}

	/**
	 * Is the test value anywhere in the ancestry of start. In other words, is
	 * 'start' contained in the 'test' hierarchy?
	 * 
	 * If this returns true, it also means that 'start' is a valid value for the
	 * 'test' label (if you treat 'test' as defining a set of values, then 'start'
	 * can be one of them).
	 * 
	 * NOTE: Because these are EObjects with names, we compare qualified names,
	 * which may be relative	 * 
	 * @param start
	 * @param test
	 * @return
	 */
	public boolean containedIn(@NonNull Base start, @NonNull QualifiedName testName) {
		return containedIn(start,testName,qnp);
	}

	/**
	 * Is the test value anywhere in the ancestry of start. In other words, is
	 * 'start' contained in the 'test' hierarchy?
	 * 
	 * If this returns true, it also means that 'start' is a valid value for the
	 * 'test' label (if you treat 'test' as defining a set of values, then 'start'
	 * can be one of them.
	 * 
	 * NOTE: Because these are EObjects with names, we compare qualified names,
	 * which may be relative
	 * 
	 * This version can be used if you want a different QNP
	 * 
	 * @param start
	 * @param test
	 * @return
	 */
	public boolean containedIn(@NonNull Base start, @NonNull String test, @NonNull UddlQNP qnp) {
		QualifiedName testName = qnc.toQualifiedName(test);
		return containedIn(start,testName,qnp);
	}

	/**
	 * Is the test value anywhere in the ancestry of start. In other words, is
	 * 'start' contained in the 'test' hierarchy?
	 * 
	 * If this returns true, it also means that 'start' is a valid value for the
	 * 'test' label (if you treat 'test' as defining a set of values, then 'start'
	 * can be one of them.
	 * 
	 * NOTE: Because these are EObjects with names, we compare qualified names,
	 * which may be relative
	 * 
	 * This version can be used if you want a different QNP
	 * 
	 * @param start
	 * @param test
	 * @return
	 */
	public boolean containedIn(@NonNull Base start, @NonNull QualifiedName testName, @NonNull UddlQNP qnp) {
		Collection<Base> ancestors = collectAncestors(start);
		return ancestors.stream().anyMatch(a -> {
			QualifiedName aqn = qnp.getFullyQualifiedName(a);
			return qnp.partialMatch(aqn, testName);
		});
	}

	/**
	 * Standardized error message for use when the invalidValue is not in the
	 * taxonomic hierarchy starting at the container
	 * 
	 * @param container
	 * @param invalidValue
	 */
	public static String msgInvalidValue(String container, String invalidValue) {
		String msg = MessageFormat.format("Container {0} contains an invalid value: {1}", container, invalidValue);
		logger.error(msg);
		return msg;
	}

	/**
	 * Look up the object with the specified name, as visible from the context
	 * instance (In case we get a RQN and there is more than one)
	 * 
	 * @param name
	 * @return The found Object. If no object found, throws NamedObjectNotFoundException. Throws
	 * 		NameCollisionException if multiple found with specified name (e.g. name is ambiguous)
	 */
	public Base getObjectForName(String name, EObject context) throws NamedObjectNotFoundException, NameCollisionException {
			
			EObject inst = ndxUtil.getUniqueObjectForName(context, getBaseMetaClass(), name);
			if (inst == null) {
				return null;
			}
			if (isCastableToBase(inst)) {
				return (Base) inst;
			}
	
			/** If we get here, this isn't a purpose */
			msgInvalidValue(name, inst.toString());
			return null;
	}

	/**
	 * Get a list of Objects, one per name. Will never be null or contain nulls but
	 * the list itself may be empty.
	 * 
	 * @param names
	 * @return a (possibly empty) list of objects. If no object found for a name, it is ignored. Throws
	 * 		NameCollisionException if multiple found with specified name (e.g. name is ambiguous)
	 */
	public List<Base> getObjectsForNames(List<String> names, EObject context)  throws NameCollisionException{
		List<Base> result = new ArrayList<Base>();
		for (String n : names) {
			try {
				Base p = getObjectForName(n, context);
				if (p != null) {
					result.add(p);
				}
			} catch (NamedObjectNotFoundException excp) {
				// Just ignore it
			}

		}
		return result;
	}

	/**
	 * From start, walk up containment hierarchy. This needs the
	 * 
	 * @param start    The starting point in the taxonomy
	 * @param realType The base type of this taxonomy. Needed because eContainment
	 *                 can continue walking up through unrelated types. We need to
	 *                 stop when we exit this type.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Base> collectAncestors(Base start) {
		Class<?> realType = getBaseType();
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
	 * TODO: Do we need to check the contents to select only those of the correct
	 * type? I don't think so since taxonomies contain only the correct type by
	 * definition
	 * 
	 * @param start
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Base> collectDescendants(Base start) {
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
	 * When we want to check to see if an invariant affects something related to a
	 * taxonomy, we need to check for invariants associated with the taxonomy member
	 * under consideration. However, because taxonomies are defined & used
	 * hierarchically, we also need to consider:
	 * 
	 * A) The entire containment hierarchy for the specified element - because
	 * 'this' element 'is-a' element for each of its containers in that taxonomy.
	 * Because each taxonomy element specializes its containers, every invariant
	 * that applies to one of its containers necessarily applies to it as well.
	 * 
	 * B) Its entire contents (everything contained within the element under
	 * consideration) - because any code used for 'this' element could be used for
	 * any of its specializing elements. Since invariants / constraints must be true
	 * for the specified element, they must be true for all instances of that
	 * element, which include all specializations of that element. A) differs from
	 * B) in that A) is a guaranteed problem, whereas B) could be a problem.
	 * 
	 * This implies several things: 1) Each hierarchy should be constructed
	 * assiduously. 2) Constraints/invariants/checks should be associated with an
	 * element carefully
	 * 
	 * Without both of these, constraints can have unintended far reaching impact.
	 * 
	 * @param p
	 * @return
	 */
	public List<Base> getAffectingElements(Base p) {

		List<Base> results = collectAncestors(p);
		results.addAll(collectDescendants(p));
		return results;

	}

//	public List<Base> getAffectingElements(Base p) {
//		List<Base> results = new ArrayList<Base>();
//		results.add(p);
//		/**
//		 * Get all the containment hierarchy
//		 */
//		EObject current = (EObject) p;
//		while ((current.eContainer() != null) && (isCastableToBase(current.eContainer()))) {
//			current = current.eContainer();
//			results.add((Base) current);
//		}
//		/**
//		 * We also get all the content of the original concept, if it is a set.
//		 * eAllContents returns a TreeIterator so we don't need to recurse separately
//		 */
//		for (EObject tp : IteratorExtensions.<EObject>toIterable(((EObject) p).eAllContents())) {
//			if (isCastableToBase(tp)) {
//				results.add((Base) tp);
//			} else {
//				msgInvalidValue(qnp.getFullyQualifiedName(tp).toString(), tp.toString());
//			}
//		}
//		return results;
//	}
	
	/**
	 * Check to see if the testedHierarchy overlaps with the hierarchyToCheck. There
	 * are three possible results: 1) FALSE - the testedHierarchy is completely
	 * unrelated to the hierarchyToCheck - this occurs when they have no common
	 * ancestor or when they have a common ancestor but one is not the ancestor of
	 * the other 2) UNNKNOWN - When TRUE and FALSE are not appropriate 3) TRUE - if
	 * the testedHierarchy is wholly contained in the hierarchyToCheck (includes
	 * when they are they same)
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
	public TriBool intersectingHierarchies(Base testedHierarchy, Base hierarchyToCheck) {

		/**
		 * Quickly check to see if they are equal
		 */
		if (testedHierarchy == hierarchyToCheck)
			return TriBool.TRUE;

		/**
		 * Get the entire lineage of both - if the hierarchyToCheck is an ancestor of
		 * the testedHierarchy, then yes
		 */
		Collection<Base> tHAncestors = collectAncestors(testedHierarchy);
		if (tHAncestors.contains(hierarchyToCheck))
			return TriBool.TRUE;

		/**
		 * If the testedHierarchy is an ancestor of the hierarchyToCheck, then MAYBE
		 * (because we might be dealing with something in the hierarchyToCheck)
		 */
		Collection<Base> hTCAncestors = collectAncestors(hierarchyToCheck);
		if (hTCAncestors.contains(testedHierarchy))
			return TriBool.SOMETIMES;

		/**
		 * Should this be FALSE or UNKNOWN?
		 */
		return TriBool.FALSE;
	}

}
