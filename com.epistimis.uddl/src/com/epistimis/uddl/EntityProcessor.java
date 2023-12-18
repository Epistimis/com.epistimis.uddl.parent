/**
 * 
 */
package com.epistimis.uddl;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

import com.epistimis.uddl.exceptions.CharacteristicNotFoundException;
import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.UddlElement;
import com.google.inject.Inject;

/**
 * 
 */
public abstract class EntityProcessor<Characteristic extends EObject, Entity extends UddlElement, Association extends Entity, Composition extends Characteristic, Participant extends Characteristic> {
	// @Inject
//		private Provider<ResourceSet> resourceSetProvider;
	//
	//
//		@Inject
//		IResourceServiceProvider.Registry reg;
	//
//		IResourceServiceProvider queryRSP;
//		IResourceFactory queryResFactory;

//		@Inject
//		ParseHelper<QuerySpecification> parseHelper;

	@Inject
	IndexUtilities ndxUtil;

	@Inject
	IQualifiedNameProvider qnp;

	@Inject
	IQualifiedNameConverter qnc;

	@Inject
	CLPExtractors clp;

	static Logger logger = Logger.getLogger(QueryProcessor.class);

	/**
	 * Get the Characteristic's rolename
	 * 
	 * @param obj
	 * @return
	 */
	abstract public String getCharacteristicRolename(Characteristic obj);// abstract protected Characteristic
																			// getCharacteristicByRolename(Entity ent,
																			// String roleName) throws
																			// CharacteristicNotFoundException;

	static MessageFormat CharacteristicNotFoundMsgFmt = new MessageFormat(
			"Entity {0} does not have a characteristic with rolename {1}");

	abstract public EClass getEntityEClass();

	abstract public Entity getSpecializes(Entity ent);

	abstract public boolean isAssociation(Entity ent);

	abstract public Association conv2Association(Entity ent);

	abstract public EList<Composition> getComposition(Entity obj);

	abstract public EList<Participant> getParticipant(Association obj);

	/**
	 * Get the type parameters for this generic class See also
	 * https://stackoverflow.com/questions/4213972/java-generics-get-class-of-generic-methods-return-type
	 * 
	 * @param ndx the index into the list of type parameters
	 * @return
	 */
	public Class<?> returnedTypeParameter(int ndx) {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class<?>) parameterizedType.getActualTypeArguments()[ndx];
	}

	/**
	 * Methods to return each of the parameter types - these warnings must remain
	 * because the alternative is a compile error when these values get used.
	 * 
	 * @return
	 */
	public Class getCharacteristicType() {
		return returnedTypeParameter(0);
	}

	public Class getEntityType() {
		return returnedTypeParameter(1);
	}

	public Class getAssociationType() {
		return returnedTypeParameter(2);
	}

	public Class getParticipantType() {
		return returnedTypeParameter(3);
	}

	/**
	 * Taken from the book, SmallJavaLib.getSmallJavaObjectClass - and converted
	 * from XTend to Java
	 * 
	 * Additionally, this checks for RQNs instead of just leaf names, or FQNs
	 * 
	 * Also note that case doesn't matter
	 * 
	 * @param context - Check visibility to this object
	 * @param type    - Filter for only for instances of this type
	 * @param name    - Filter for only instances that match this RQN
	 * @return A list of matching objects
	 * 
	 *         TODO:
	 */
	protected List<IEObjectDescription> searchAllVisibleObjects(EObject context, EClass type, String name) {
		List<IEObjectDescription> lod = ndxUtil.searchAllVisibleEObjectDescriptions(context, type, name);
		return lod;
	}

	/**
	 * Find all the Entities in this hierarchy
	 * 
	 * @param q
	 * @param context
	 * @return
	 */
	protected IScope entityScope(EObject context) {
		/*
		 * the object will either be the original query or a containing PDM - so
		 * containers will always be a (C/L/P)DM or a DataModel
		 */
		final Iterable<Entity> entities = IterableExtensions
				.<Entity>filter(IteratorExtensions.<EObject>toIterable(context.eAllContents()), getEntityType());
		EObject container = context.eContainer();
		if (container != null) {
			return Scopes.scopeFor(entities, entityScope(container));
		} else {
			return Scopes.scopeFor(entities, IScope.NULLSCOPE);
		}
	}

//		/**
//		 * Get all the characteristics
//		 * 
//		 * @param obj
//		 * @return the list of characteristics.
//		 */
//		protected Collection<Characteristic> getCharacteristics(Entity obj) {
//			Map<String, Characteristic> characteristics = new HashMap<>();
//			clp.getCharacteristicsAndRecurse(obj, characteristics);
//			return characteristics.values();
//		}

//		/**
//		 * Return the named characteristic - which could be a composition or a
//		 * participant
//		 * 
//		 * @param ent      The Entity containing the characteristic
//		 * @param roleName The rolename of the characteristic to find
//		 * @return The found characteristic
//		 */
//		protected Characteristic getCharacteristicByRolename(Entity ent, String roleName)
//				/*throws CharacteristicNotFoundException*/ {
//			return clp.getCharacteristicByRolename(ent, roleName);
//		}

	/**
	 * Get all the characteristics
	 * 
	 * @param obj
	 * @return the list of characteristics.
	 */
	public Map<String, Characteristic> getCharacteristics(Entity obj) {
		Map<String, Characteristic> characteristics = new HashMap<String, Characteristic>();
		getCharacteristicsAndRecurse(obj, characteristics);
		return characteristics;
	}

	/**
	 * Get the characteristics from this Entity, without following the
	 * specialization hierarchy
	 * 
	 * @param obj
	 * @param characteristics
	 */
	public void getLocalCharacteristics(Entity obj, Map<String, Characteristic> characteristics) {
		for (Composition pc : getComposition(obj)) {
			characteristics.putIfAbsent(getCharacteristicRolename(pc), pc);
		}
		if (isAssociation(obj)) {
			Association assoc = (Association) obj;
			for (Participant pp : getParticipant(assoc)) {
				characteristics.putIfAbsent(getCharacteristicRolename(pp), (Characteristic) pp);
			}
		}

	}

	/**
	 * Get the set of all characteristics from this entity - across the entire
	 * specialization hierarchy. Note that, because we start from the bottom, any
	 * specializing characteristics will override same named elements higher in the
	 * hierarchy
	 * 
	 * This actually implements collecting the characteristics. It handles the
	 * recursion
	 * 
	 * @param obj
	 * @param the map of characteristics. Starts empty and gets filled.
	 */
	protected void getCharacteristicsAndRecurse(Entity obj, Map<String, Characteristic> characteristics) {

		getLocalCharacteristics(obj, characteristics);

		// Now check for specialization
		Entity specializes = getSpecializes(obj);
		if (specializes != null) {
			getCharacteristicsAndRecurse(specializes, characteristics);
		}

	}

	/**
	 * Return the named characteristic - which could be a composition or a
	 * participant
	 * 
	 * @param ent      The Entity containing the characteristic
	 * @param roleName The rolename of the characteristic to find
	 * @return The found characteristic
	 */
	public Characteristic getCharacteristicByRolename(Entity ent, String roleName)
	/* throws CharacteristicNotFoundException */ {
		// Look for the characteristic in this Entity and, if not found, go up the
		// specializes chain until we find it
		for (Composition comp : getComposition(ent)) {
			if (getCharacteristicRolename(comp).equals(roleName))
				return comp;
		}
		if (isAssociation(ent)) {
			Association assoc = (Association) ent;
			for (Characteristic part : getParticipant(assoc)) {
				if (getCharacteristicRolename(part).equals(roleName))
					return part;

			}
		}
		// If we get here, we haven't found it yet - check for specializes
		if (getSpecializes(ent) != null) {
			return getCharacteristicByRolename(getSpecializes(ent), roleName);
		}
		// If we get here, it wasn't found
		Object[] args = { ent, roleName };
		throw new CharacteristicNotFoundException(CharacteristicNotFoundMsgFmt.format(args));
	}

	/**
	 * Given a string containing a (possibly qualified) rolename, return a map of
	 * all Characteristics that contain that rolename in their FQN somewhere. In
	 * some ways this is the inverse of Scopes.scopeFor. A Scope determines what can
	 * be found from the current context point using the specified name and
	 * reference type. That means that RQNs are relative to this point.
	 * 
	 * What we want here is anything where the specified RQN is a part of the name
	 * of something contained in or referenced by the context object - and the name
	 * may not give a complete enough path to be reachable just using the RQN from
	 * this context point.
	 * 
	 * Net result: We can't just use Scopes.scopeFor to find what we want.
	 */
	public Map<QualifiedName, Characteristic> getFQRoleName(Entity ent, String roleName) {
		// The simple approach just looks at what is contained in the specified entity.
		// It does not follow references to other entities.
		Map<QualifiedName, Characteristic> result = new HashMap<QualifiedName, Characteristic>();
		try {
			Characteristic c = getCharacteristicByRolename(ent, roleName);
			result.put(qnp.getFullyQualifiedName(c), c);

		} catch (CharacteristicNotFoundException excp) {
			// do nothing
		}
		// TODO: We also need to scan all the Compositions that have Entity types and
		// drill down into those
		for (Composition comp : getComposition(ent)) {
			// if (comp.type instanceof Entity)
			// TODO: THIS IS NOT FINISHED !!!!
		}

		return result;

		// TODO: follow participants
	}

	/**
	 * Return the in order list of specializations starting with this object and
	 * walking up the specialization hierarchy
	 * 
	 * @param start - the first entity
	 * @return - a list of entities that are the specialization ancestry of this
	 *         starting entity
	 */
	public List<Entity> specializationAncestry(Entity start) {
		Entity spec = getSpecializes(start);
		List<Entity> result = new ArrayList<Entity>();
		result.add(start);
		if (spec != null) {
			result.addAll(specializationAncestry(spec));
		}
		return result;
	}

	/**
	 * oclIsKindOf uses the metamodel. We want to follow the UDDL specialization
	 * hierarchy
	 */
	public boolean isTypeOrSpecializationOf(Entity obj, Entity targetType) {
		if (obj == targetType) {
			return true;
		}
		Entity spec = getSpecializes(obj);
		if (spec != null) {
			return isTypeOrSpecializationOf(spec, targetType);
		}
		// else
		return false;
	}

	/**
	 * Get all the Entities that specialize the root. (this is traversing
	 * specialization in the inverse direction. If we cannot follow the inverse
	 * directly, then we have to use allInstances)
	 */
	public Set<Entity> specializationHierarchy(Entity root) {
		Set<Entity> result = new HashSet<Entity>();
		EClass clz = getEntityEClass();
		Iterable<EObject> vobjs = ndxUtil.getVisibleObjects(root, clz);
		for (EObject obj : vobjs) {
			@SuppressWarnings("unchecked") // getVisibleObjects has already filtered by type
			Entity entity = (Entity) obj;
			if (isTypeOrSpecializationOf(entity, root)) {
				result.add(entity);
			}
		}
		return result;
	}

}
