package com.epistimis.uddl;

import java.io.ByteArrayInputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

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

import com.epistimis.uddl.CLPExtractors;
import com.epistimis.uddl.query.query.QueryIdentifier;
import com.epistimis.uddl.query.query.QuerySpecification;
import com.epistimis.uddl.query.query.SelectedEntity;
import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.PlatformCompositeQuery;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformQuery;
import com.epistimis.uddl.uddl.PlatformQueryComposition;
import com.epistimis.uddl.uddl.PlatformView;
import com.epistimis.uddl.uddl.UddlElement;
import com.epistimis.uddl.uddl.UddlPackage;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

public abstract class QueryProcessor<Characteristic extends EObject,  Entity extends UddlElement, Association extends Entity, Participant extends Characteristic, View extends UddlElement, Query extends View, CompositeQuery extends View, QueryComposition extends EObject> {
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

//	@Inject
//	CLPExtractors clp;

	abstract protected EList<QueryComposition> getComposition(CompositeQuery ent);
	abstract protected View getType(QueryComposition obj);
	abstract protected boolean isCompositeQuery(View obj);
	abstract protected EClass getRelatedPackageEntityInstance(Query obj);

	/**
	 * Get the type parameters for this generic class
	 * @param ndx the index into the list of type parameters 
	 * @return
	 */
	public Class returnedTypeParameter(int ndx) {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class) parameterizedType.getActualTypeArguments()[ndx];
	}
	
	public Class getCharacteristicType() 	{ return returnedTypeParameter(0); }
	public Class getEntityType() 			{ return returnedTypeParameter(1); }
	public Class getAssociationType() 		{ return returnedTypeParameter(2); }
	public Class getParticipantType() 		{ return returnedTypeParameter(3); }
	public Class getViewType() 				{ return returnedTypeParameter(4); }
	public Class getQueryType() 			{ return returnedTypeParameter(5); }
	public Class getCompositeQueryType() 	{ return returnedTypeParameter(6); }
	public Class getQueryCompositionType() 	{ return returnedTypeParameter(7); }


	public QueryProcessor() {
		// this.reg = reg;
//		URI queryURI = URI.createURI(QueryPackageImpl.eNS_URI);

//		queryRSP = reg.getResourceServiceProvider(queryURI);
//		// Now register the file type so we don't need a file extension
//		reg.getContentTypeToFactoryMap().put("quddl", queryRSP);
//		queryResFactory = queryRSP.get(IResourceFactory.class);

	}



	// Set up to process with correct parser
	public QuerySpecification processQuery(Query query) {
		String queryText = CLPExtractors.getSpecification(query);
		QuerySpecification qspec = null;
		try {
			// See https://www.eclipse.org/forums/index.php?t=msg&th=173070/ for explanation
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource resource = resourceSet.createResource(URI.createURI("temp.quddl"));
			ByteArrayInputStream bais = new ByteArrayInputStream(queryText.getBytes());
			resource.load(bais, null);
			qspec = (QuerySpecification) resource.getContents().get(0);

		} catch (Exception e) {
			// TODO: This should also check for Parse errors - like unit tests do - and
			// print out something.
			System.out.println("Query " + qnp.getFullyQualifiedName(query).toString() + " contains a malformed query: '"
					+ queryText + "'");
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public List<QuerySpecification> processCompositeQuery(CompositeQuery query) {
		List<QuerySpecification> results = new ArrayList<QuerySpecification>();
		for (EObject comp : CLPExtractors.getComposition(query)) {
			// comp is actually a QueryComposition object but shows up as an EObject because
			// of XTend dispatch
			View v = (View) CLPExtractors.getType(comp);
			if (CLPExtractors.isCompositeQuery(v)) {
				results.addAll(processCompositeQuery((CompositeQuery) v));				
			} else {
				results.add(processQuery((Query) v));				
			}
		}
		return results;
	}

	/**
	 * To properly process a parsed query, we need to match names against UDDL
	 * objects. That means we need to determine the relative names and find a match,
	 * working from inside out. The names will be relative to the Query instance
	 * containing the parsed specification.
	 */
	public  List<Entity> matchQuerytoUDDL(Query q, QuerySpecification qspec) {
		Resource resource = q.eResource();
		// Initially, we just get the Entity names
		List<Entity> chosenEntities = new ArrayList<Entity>();
		String queryFQN = qnp.getFullyQualifiedName(q).toString();
		final Iterable<SelectedEntity> selectedEntities = Iterables.<SelectedEntity>filter(
				IteratorExtensions.<EObject>toIterable(qspec.eAllContents()), SelectedEntity.class);
		for (SelectedEntity se : selectedEntities) {
			QueryIdentifier qid = (QueryIdentifier) se.getEntityType();
			String entityName = qid.getId();

			/**
			 * TODO: Gets a scope that is for this container hierarchy only. For imports,
			 * need to use IndexUtilities to get all visible IEObjectDescriptions and filter
			 * those. That will require RQNs processing.
			 */	
			IScope searchScope = entityScope(q.eContainer());
			List<IEObjectDescription> lod = IterableExtensions
					.<IEObjectDescription>toList(searchScope.getElements(qnc.toQualifiedName(entityName)));
//			List<IEObjectDescription> lod = new ArrayList<IEObjectDescription>();
//			for (IEObjectDescription desc : descriptions) {
//				lod.add(desc);
//			}

			/**
			 * There should be only 1 for each entityName - otherwise, we have ambiguity
			 */
			switch (lod.size()) {
			case 0: {
				// If nothing found so far, check all visible objects
				List<IEObjectDescription> globalDescs = ndxUtil.searchAllVisibleEObjectDescriptions(q,
						CLPExtractors.getRelatedPackageEntityInstance(q), entityName);
				switch (globalDescs.size()) {
				case 0:
					System.out.println("No Entities found for name: " + entityName + " from Query " + queryFQN);
					break;
				case 1:
					chosenEntities
							.add((Entity) IndexUtilities.objectFromDescription(resource, globalDescs.get(0)));
					break;
				default:
					/** found multiple - so print out their names */
					ndxUtil.printIEObjectDescriptionNameCollisions(queryFQN, getQueryType().getName(),
							globalDescs);
					break;
				}
			}
				break;
			case 1:
				IEObjectDescription description = lod.get(0);
				chosenEntities.add((Entity) IndexUtilities.objectFromDescription(resource, description));
				break;
			default:
				/** found multiple - so print out their names */
				ndxUtil.printIEObjectDescriptionNameCollisions(queryFQN, getQueryType().getName(), lod);
			}
		}
		/* at this point we have identified all the entities */
		return chosenEntities;
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
	protected  IScope entityScope(EObject context) {
		/*
		 * the object will either be the original query or a containing PDM - so
		 * containers will always be a (C/L/P)DM or a DataModel
		 */
		final Iterable<Entity> entities = IterableExtensions.<Entity>filter(
				IteratorExtensions.<EObject>toIterable(context.eAllContents()), getEntityType());
		EObject container = context.eContainer();
		if (container != null) {
			return Scopes.scopeFor(entities, entityScope(container));
		} else {
			return Scopes.scopeFor(entities, IScope.NULLSCOPE);
		}
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
