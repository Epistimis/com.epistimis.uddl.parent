/*
 * generated by Xtext 2.28.0
 */
package com.epistimis.uddl.scoping;

//import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
//import org.eclipse.xtext.EcoreUtil2;
//import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
//import org.eclipse.xtext.scoping.Scopes;
//import org.eclipse.xtext.scoping.impl.FilteringScope;
//import org.eclipse.emf.ecore.resource.Resource;
//
//import com.epistimis.uddl.uddl.ConceptualComposition;
//import com.epistimis.uddl.uddl.ConceptualEntity;
//import com.epistimis.uddl.uddl.LogicalEntity;
import com.epistimis.uddl.uddl.UddlPackage;

/**
 * This class contains custom scoping description.
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
public class UddlScopeProvider extends AbstractUddlScopeProvider {

	UddlPackage epackage = UddlPackage.eINSTANCE;
	

	@Override
	public IScope getScope(EObject context, EReference reference) {

//		if (reference == epackage.getLogicalComposition_Realizes()) {
//			/**
//			 * Per https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping,
//			 * should be able to use FilteringScope to limit this to only things that will work
//			 */
//			LogicalEntity lentity = (LogicalEntity) context.eContainer();
//			ConceptualEntity centity = lentity.getRealizes();
//			if (centity != null) {
//	            IScope scope = Scopes.scopeFor(centity.getComposition(),null);
//				
//			}
//            EObject rootElement = EcoreUtil2.getRootContainer(context);
//            Resource resource = context.eResource();
//            List<ConceptualComposition> candidates = EcoreUtil2.getAllContentsOfType(rootElement, ConceptualComposition.class);
//            IScope existingScope = Scopes.scopeFor(candidates);
//            // Scope that filters out the context element from the candidates list
//            return new FilteringScope(existingScope, (IEObjectDescription description) -> {
//            	EObject instance = description.getEObjectOrProxy();
//    			if (instance.eIsProxy()) {
//    				instance = resource.getEObject(description.getEObjectURI().fragment());
//    			}
//    			return centity.getComposition().contains(instance);
//            });
//
//		}
//		if (reference == epackage.getPlatformComposition_Realizes()) {
//			PlatformEntity pentity = (PlatformEntity) context.eContainer();
//			LogicalEntity lentity = pentity.getRealizes();
//			return Scopes.scopeFor(lentity.getComposition());
//		}
//		if (reference == epackage.getLogicalParticipant_Realizes()) {
//			LogicalAssociation lassoc = (LogicalAssociation) context.eContainer();
//			ConceptualAssociation cassoc = (ConceptualAssociation) lassoc.getRealizes();
//			return Scopes.scopeFor(cassoc.getParticipant());
//		}
//		if (reference == epackage.getPlatformParticipant_Realizes()) {
//			PlatformAssociation passoc = (PlatformAssociation) context.eContainer();
//			LogicalAssociation lassoc = (LogicalAssociation) passoc.getRealizes();
//			return Scopes.scopeFor(lassoc.getParticipant());
//		}
		// TODO Auto-generated method stub
		return super.getScope(context, reference);
	}
}
