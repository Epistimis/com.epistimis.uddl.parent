package com.epistimis.uddl;

import java.io.ByteArrayInputStream;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

import com.epistimis.uddl.exceptions.CharacteristicNotFoundException;
import com.epistimis.uddl.exceptions.NameCollisionException;
import com.epistimis.uddl.exceptions.NamedObjectNotFoundException;
import com.epistimis.uddl.exceptions.QueryMatchException;
import com.epistimis.uddl.exceptions.WrongTypeException;
import com.epistimis.uddl.query.query.ExplicitSelectedEntityCharacteristicReference;
//import com.epistimis.uddl.query.query.ProjectedCharacteristicAlias;
import com.epistimis.uddl.query.query.ProjectedCharacteristicExpression;
import com.epistimis.uddl.query.query.ProjectedCharacteristicList;
import com.epistimis.uddl.query.query.QueryIdentifier;
import com.epistimis.uddl.query.query.QueryStatement;
import com.epistimis.uddl.query.query.SelectedEntity;
import com.epistimis.uddl.query.query.SelectedEntityCharacteristicReference;
import com.epistimis.uddl.query.query.SelectedEntityCharacteristicWildcardReference;
import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.UddlElement;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/***
 * Processing Queries requires the following: 1) Identify all the entities
 * referenced in the query (matchQuerytoUDDL) 2) Identify all the
 * characteristics referenced in the query (selectCharacteristicsFromUDDL) 3)
 * Process the joins (which will determine cardinality of the result)
 * 
 * 
 * @author stevehickman
 *
 * @param <Characteristic>
 * @param <Entity>
 * @param <Association>
 * @param <Participant>
 * @param <View>
 * @param <Query>
 * @param <CompositeQuery>
 * @param <QueryComposition>
 */

public abstract class QueryProcessor<Characteristic extends EObject, Entity extends UddlElement, Association extends Entity, Participant extends Characteristic, View extends UddlElement, Query extends View, CompositeQuery extends View, QueryComposition extends EObject> {
	// @Inject
//	private Provider<ResourceSet> resourceSetProvider;
//
//
//	@Inject
//	IResourceServiceProvider.Registry reg;
//
//	IResourceServiceProvider queryRSP;
//	IResourceFactory queryResFactory;

//	@Inject
//	ParseHelper<QuerySpecification> parseHelper;

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
	 * Get the individual QueryCompositions from this CompositeQuery
	 * 
	 * @param ent
	 * @return
	 */
	abstract protected EList<QueryComposition> getComposition(CompositeQuery ent);

	/**
	 * Get the type from a QueryComposition - this will be a View (Query or
	 * CompositeQuery)
	 * 
	 * @param obj
	 * @return
	 */
	abstract protected View getType(QueryComposition obj);

	/**
	 * Determine if this View is composite or not
	 * 
	 * @param obj
	 * @return
	 */
	abstract protected boolean isCompositeQuery(View obj);

	/**
	 * Get the PackageEntity instance related to this query
	 * 
	 * @param obj
	 * @return
	 */
	abstract protected EClass getRelatedPackageEntityInstance(Query obj);

	/**
	 * Get the Characteristic's rolename
	 * 
	 * @param obj
	 * @return
	 */
	abstract protected String getCharacteristicRolename(Characteristic obj);// abstract protected Characteristic
																			// getCharacteristicByRolename(Entity ent,
																			// String roleName) throws
																			// CharacteristicNotFoundException;

	static MessageFormat CharacteristicNotFoundMsgFmt = new MessageFormat(
			"Entity {0} does not have a characteristic with rolename {1}");

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

	public Class getViewType() {
		return returnedTypeParameter(4);
	}

	public Class getQueryType() {
		return returnedTypeParameter(5);
	}

	public Class getCompositeQueryType() {
		return returnedTypeParameter(6);
	}

	public Class getQueryCompositionType() {
		return returnedTypeParameter(7);
	}

	public QueryProcessor() {
		// this.reg = reg;
//		URI queryURI = URI.createURI(QueryPackageImpl.eNS_URI);

//		queryRSP = reg.getResourceServiceProvider(queryURI);
//		// Now register the file type so we don't need a file extension
//		reg.getContentTypeToFactoryMap().put("quddl", queryRSP);
//		queryResFactory = queryRSP.get(IResourceFactory.class);

	}

	/**
	 * Get the characteristics selected in the query. TODO: This needs to return a
	 * structure that includes cardinality info for templates to use.
	 * 
	 * @param query
	 * @return A map of (rolename, characteristic) for all the characteristics
	 *         specified by the query
	 */
	public Map<String, Characteristic> getSelectedCharacteristics(Query query) {

		// try {
		QueryStatement qs = parseQuery(query);
		Map<String, Entity> matchedEntities = matchQuerytoUDDL(query, qs);
		Map<String, Characteristic> selectedChars = selectCharacteristicsFromUDDL(query, qs, matchedEntities);
		return selectedChars;
//		} catch (Exception excp) {
//			excp.printStackTrace();
//			return new HashMap<String, Characteristic>();
//		}
	}

	/**
	 * Get the characteristics selected in the query.
	 * 
	 * @param query
	 * @return A map of (rolename, characteristic) for all the characteristics
	 *         specified by the query
	 */
	public Map<String, Characteristic> getCompositeQuerySelectedCharacteristics(CompositeQuery query) {
		Map<String, Characteristic> result = new HashMap<>();
		Map<Query, QueryStatement> qs = parseCompositeQuery(query);
		qs.forEach((q, qstmt) -> {
			Map<String, Entity> matchedEntities = matchQuerytoUDDL(q, qstmt);
			Map<String, Characteristic> selectedChars;
			try {
				selectedChars = selectCharacteristicsFromUDDL(q, qstmt, matchedEntities);
				result.putAll(selectedChars);
			} catch (CharacteristicNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// return new HashMap<String,Characteristic>();
			}
		});
		return result;

	}

	// Set up to process with correct parser
	/**
	 * Parse a query and return a QueryStatement object
	 * 
	 * @param query The UDDL query containing the specification string.
	 * @return A QueryStatement object
	 */
	public QueryStatement parseQuery(Query query) {
		String queryText = CLPExtractors.getSpecification(query);
		QueryStatement qspec = null;
		try {
			// See https://www.eclipse.org/forums/index.php?t=msg&th=173070/ for explanation
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource resource = resourceSet.createResource(URI.createURI("temp.quddl"));
			ByteArrayInputStream bais = new ByteArrayInputStream(queryText.getBytes());
			resource.load(bais, null);
			qspec = (QueryStatement) resource.getContents().get(0);

		} catch (Exception e) {
			// TODO: This should also check for Parse errors - like unit tests do - and
			// print out something.
			logger.error("Query " + qnp.getFullyQualifiedName(query).toString() + " contains a malformed query: '"
					+ queryText + "'",e);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return qspec;
	}

	/**
	 * Processing a CompositeQuery just means drilling down and processing each
	 * contained individual query
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<Query, QueryStatement> parseCompositeQuery(CompositeQuery query) {
		Map<Query, QueryStatement> results = new HashMap<>();
		for (EObject comp : CLPExtractors.getComposition(query)) {
			// comp is actually a QueryComposition object but shows up as an EObject because
			// of XTend dispatch
			View v = (View) CLPExtractors.getType(comp);
			if (CLPExtractors.isCompositeQuery(v)) {
				results.putAll(parseCompositeQuery((CompositeQuery) v));
			} else {
				Query q = (Query) v;
				results.put(q, parseQuery(q));
			}
		}
		return results;
	}

	/**
	 * Get all the Entities referenced by this Query.
	 * 
	 * @param conn
	 * @return
	 */
	public Map<String, Entity> getReferencedEntities(Query query) {
		Map<String, Entity> entities = new HashMap<String, Entity>();
		QueryStatement spec = parseQuery(query);
		entities.putAll(matchQuerytoUDDL(query, spec));

		return entities;
	}

	/**
	 * Get all the Entities referenced by this CompositeQuery.
	 * 
	 * @param conn
	 * @return
	 */
	public Map<String, Entity> getReferencedEntitiesComposite(CompositeQuery query) {
		Map<String, Entity> entities = new HashMap<String, Entity>();
		Map<Query, QueryStatement> specs = parseCompositeQuery(query);
		for (Map.Entry<Query, QueryStatement> entry : specs.entrySet()) {
			entities.putAll(matchQuerytoUDDL(entry.getKey(), entry.getValue()));
		}

		return entities;
	}

	/**
	 * To properly process a parsed query, we need to match names against UDDL
	 * objects. That means we need to determine the relative names and find a match,
	 * working from inside out. The names will be relative to the Query instance
	 * containing the parsed specification.
	 * 
	 * @param q     The query that provides the context
	 * @param qstmt The parsed query statement whose content will be matched to the
	 *              Uddl based on the context provided by the query
	 * 
	 * @return A map of <nameOrAlias,Entity> that identifies what was matched. The
	 *         key is the alias specified in the qstmt (defaults to name). Can throw
	 *         exceptions if any name specified in the qstmt is not found, is
	 *         ambiguous, or references an EObject that not an Entity
	 * 
	 *         TODO: Don't just throw these exceptions. Collect them and then
	 *         convert them to errors at the appropriate level - but collect all of
	 *         them
	 */
	public Map<String, Entity> matchQuerytoUDDL(Query q, QueryStatement qstmt)
			throws NamedObjectNotFoundException, NameCollisionException, WrongTypeException {
		// Create an exception that we might throw later. We will store suppressed
		// exceptions here and throw only if somethin was suppressed.
		QueryMatchException potentialExcp = new QueryMatchException(
				"Errors occurred while attempting to match Query to UDDL. See suppressed:");
		Resource resource = q.eResource();
		// Initially, we just get the Entity names
		Map<String, Entity> chosenEntities = new HashMap<String, Entity>();
		String queryFQN = qnp.getFullyQualifiedName(q).toString();
		/**
		 * NOTE: This filter picks up *all* SelectedEntity instances. That means both
		 * the first one and all those in JOIN statements. So we know that we've got
		 * them all after this.
		 */
		final Iterable<SelectedEntity> selectedEntities = Iterables.<SelectedEntity>filter(
				IteratorExtensions.<EObject>toIterable(qstmt.eAllContents()), SelectedEntity.class);
		for (SelectedEntity se : selectedEntities) {

			QueryIdentifier qid = (QueryIdentifier) se.getEntityType();
			String entityName = qid.getId();
			String alias = entityName;
			QueryIdentifier sea = (QueryIdentifier) se.getSelectedEntityAlias();
			if (sea != null && (sea.getId().trim().length() > 0)) {
				alias = sea.getId();
			}

			/**
			 * TODO: Gets a scope that is for this container hierarchy only. For imports,
			 * need to use IndexUtilities to get all visible IEObjectDescriptions and filter
			 * those. That will require RQNs processing.
			 * 
			 * NOTE: The unmodifiableLists below are to address an NPE I was getting when attempting 
			 * to get a value from globalDecs. 
			 */
			IScope searchScope = entityScope(q.eContainer());
			List<IEObjectDescription> listOfDescriptions = Collections.unmodifiableList(IterableExtensions
					.<IEObjectDescription>toList(searchScope.getElements(qnc.toQualifiedName(entityName))));

			/**
			 * There should be only 1 for each entityName - otherwise, we have ambiguity
			 */
			switch (listOfDescriptions.size()) {
			case 0: {
				// If nothing found so far, check all visible objects
				List<IEObjectDescription> globalDescs = Collections
						.unmodifiableList(ndxUtil.searchAllVisibleEObjectDescriptions(q,
								CLPExtractors.getRelatedPackageEntityInstance(q), entityName));
				switch (globalDescs.size()) {
				case 0:
					String msg = MessageFormat.format("No Entities found for name: {0} from Query {1}", entityName,
							queryFQN);
					potentialExcp.addSuppressed(new NamedObjectNotFoundException(msg));
					break;
				case 1: {
					EObject obj = IndexUtilities.objectFromDescription(resource, globalDescs.get(0));
					addChosenEntity(obj, alias, chosenEntities);
				}
					break;
				default:
					/** found multiple - so print out their names */
					String nameCollisionsMsg = ndxUtil.printIEObjectDescriptionNameCollisions(queryFQN,
							getQueryType().getName(), globalDescs);
					potentialExcp.addSuppressed(new NameCollisionException(nameCollisionsMsg));
					// logger.info(nameCollisionsMsg);
					break;
				}
			}
				break;
			case 1: {
				IEObjectDescription description = listOfDescriptions.get(0);
				EObject obj = IndexUtilities.objectFromDescription(resource, description);
				addChosenEntity(obj, alias, chosenEntities);
			}
				break;
			default:
				/** found multiple - so print out their names */
				String nameCollisionsMsg = ndxUtil.printIEObjectDescriptionNameCollisions(queryFQN,
						getQueryType().getName(), listOfDescriptions);
				potentialExcp.addSuppressed(new NameCollisionException(nameCollisionsMsg));
				// logger.info(nameCollisionsMsg);
				break;
			}
		}
		// Only throw if we captured any suppressed exceptions during this process.
		if (potentialExcp.getSuppressed().length > 0) {
			throw potentialExcp;
		}
		/* at this point we have identified all the entities */
		return chosenEntities;
	}

	@SuppressWarnings("unchecked")
	private void addChosenEntity(EObject obj, String key, Map<String, Entity> chosenEntities) {
		Class<Entity> realType = getEntityType();
		if (realType.isInstance(obj)) {
			chosenEntities.put(key, (Entity) obj);
		} else {
			String msg = MessageFormat.format(WRONG_TYPE_FMT, qnp.getFullyQualifiedName(obj).toString(), "Entity",
					obj.getClass().toString());
			throw new WrongTypeException(msg);
		}
	}

	static final String WRONG_TYPE_FMT = "Instance {0} expected to be of type {0} but was type {0}";

	/**
	 * Match selected characteristics to specific characteristics of matched
	 * Entities
	 * 
	 * @param q               The query - needed for context
	 * @param qspec           The query specification
	 * @param matchedEntities Entities previously matched to this query
	 * @throws Exception
	 * @returns A map of (rolename, characteristic)
	 */
	public Map<String, Characteristic> selectCharacteristicsFromUDDL(Query q, QueryStatement qstmt,
			Map<String, Entity> matchedEntities)  { 

		// Initially, we just get the Entity names
		Map<String, Characteristic> selectedCharacteristics = new HashMap<String, Characteristic>();
		// String queryFQN = qnp.getFullyQualifiedName(q).toString();
		ProjectedCharacteristicList pcl = qstmt.getProjectedCharacteristicList();
		if (pcl.getAll() != null) {
			// All characteristics have been selected
			for (Entity ent : matchedEntities.values()) {

				for (Characteristic c : getCharacteristics(ent)) {
					selectedCharacteristics.put(getCharacteristicRolename(c), c);
				}
			}
		} else {
			for (ProjectedCharacteristicExpression pce : pcl.getCharacteristic()) {
				if (pce instanceof SelectedEntityCharacteristicWildcardReference) {
					SelectedEntityCharacteristicWildcardReference secwr = (SelectedEntityCharacteristicWildcardReference) pce;
					QueryIdentifier qi = (QueryIdentifier) secwr.getSelectedEntity();
					// qi is the entity name or alias
					Entity ent = matchedEntities.get(qi.getId());
					for (Characteristic c : getCharacteristics(ent)) {
						selectedCharacteristics.put(getCharacteristicRolename(c), c);
					}
				} else {
					// pce must be an ExplicitSelectedEntityCharacteristicReference
					ExplicitSelectedEntityCharacteristicReference esecr = (ExplicitSelectedEntityCharacteristicReference) pce;
					SelectedEntityCharacteristicReference secr = esecr.getSelectedEntityCharacteristicReference();
//					ProjectedCharacteristicAlias alias = esecr.getProjectedCharacteristicAlias();
					QueryIdentifier se = (QueryIdentifier) secr.getSelectedEntity();
					if (se == null) {
						// If no selected entity, that means the characteristic only exists in one of
						// the entities specified
						// in the query. Figure out which one it is.
						for (Entity ent : matchedEntities.values()) {
							try {
								Characteristic c = getCharacteristicByRolename(ent,
										((QueryIdentifier) secr.getCharacteristic()).getId());
								selectedCharacteristics.put(getCharacteristicRolename(c), c);
							} catch (CharacteristicNotFoundException e) {
								// Not found in that ent - try the next one
							}
						}

					} else {
						String entityOrAlias = ((QueryIdentifier) secr.getSelectedEntity()).getId();
						Entity ent = matchedEntities.get(entityOrAlias);
						Characteristic c = getCharacteristicByRolename(ent,
								((QueryIdentifier) secr.getCharacteristic()).getId());
						selectedCharacteristics.put(getCharacteristicRolename(c), c);
					}

				}
			}
		}
		return selectedCharacteristics;

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

	/**
	 * Get all the characteristics
	 * 
	 * @param obj
	 * @return the list of characteristics.
	 */
	protected Collection<Characteristic> getCharacteristics(Entity obj) {
		Map<String, Characteristic> characteristics = new HashMap<>();
		clp.getCharacteristicsAndRecurse(obj, characteristics);
		return characteristics.values();
	}

	/**
	 * Return the named characteristic - which could be a composition or a
	 * participant
	 * 
	 * @param ent      The Entity containing the characteristic
	 * @param roleName The rolename of the characteristic to find
	 * @return The found characteristic
	 */
	protected Characteristic getCharacteristicByRolename(Entity ent, String roleName)
			throws CharacteristicNotFoundException {
		return clp.getCharacteristicByRolename(ent, roleName);
	}

	public void setupParsing() {
//
//		// obtain a resourceset from the injector
//		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
//		// load a resource by URI, in this case from the file system
//		Resource resource = resourceSet.getResource(URI.createFileURI("./mymodel.mydsl"), true);
//		//If you want to load a bunch of files that have references to each other, you should add them all to the resourceset at this point, by calling
//		resourceSet.getResource(URI.createFileURI("./anothermodel.mydsl"), true);
//
//		String t = "...";
//		Injector guiceInjector = new MyDSLStandaloneSetup().createInjectorAndDoEMFRegistration();
//		IParser parser = guiceInjector.getInstance(IAntlrParser.class);
////		IParseResult result = parser.parse(new StringInputStream(t));
////		List<SyntaxError> errors = result.getParseErrors();
////		Assert.assertTrue(errors.size() == 0);
////		EObject eRoot = result.getRootASTElement();
////		MyDSLRoot root = (MyDSLRoot) eRoot;
////
		/**
		 * Use this code to initialize the RSP
		 */

//
//
//		/**
//		 * When reading the content do this
//		 */
//		// create a resource for language 'Quddl'
//		Resource query = resourceSet.createResource(URI.createURI("string.quddl"));
//		// parse some contents
//		query.load(new StringInputStream(".."), Collections.emptyMap());
//		// After loading all the relevant resources
//		EcoreUtil.resolveAll(query);
//
//		queryRSP.get(I)
//
	}

//	protected XtextResource createResource(String content) {
//		URI tempURI = URI.createFileURI("temp.uddl");
//		XtextResource result = (XtextResource) queryResFactory.createResource(tempURI);
//		try {
//			result.load(new StringInputStream(content, result.getEncoding()), Collections.emptyMap());
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//		return result;
//	}
}
