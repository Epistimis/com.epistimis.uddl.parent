package com.epistimis.uddl

import org.eclipse.emf.common.util.EList;

import com.epistimis.uddl.uddl.ConceptualAssociation;
import com.epistimis.uddl.uddl.ConceptualComposition;
import com.epistimis.uddl.uddl.ConceptualEntity;
import com.epistimis.uddl.uddl.ConceptualParticipant;
import com.epistimis.uddl.uddl.LogicalAssociation;
import com.epistimis.uddl.uddl.LogicalComposition;
import com.epistimis.uddl.uddl.LogicalEntity;
import com.epistimis.uddl.uddl.LogicalParticipant;
import com.epistimis.uddl.uddl.PlatformAssociation;
import com.epistimis.uddl.uddl.PlatformComposition;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformParticipant;
import com.epistimis.uddl.uddl.ConceptualQuery
import com.epistimis.uddl.uddl.LogicalQuery
import com.epistimis.uddl.uddl.PlatformQuery
import com.epistimis.uddl.uddl.UddlElement
import com.epistimis.uddl.uddl.PlatformQueryComposition
import com.epistimis.uddl.uddl.PlatformCompositeQuery
import com.epistimis.uddl.uddl.LogicalQueryComposition
import com.epistimis.uddl.uddl.LogicalCompositeQuery
import com.epistimis.uddl.uddl.ConceptualCompositeQuery
import com.epistimis.uddl.uddl.ConceptualQueryComposition
import com.epistimis.uddl.uddl.ConceptualView
import com.epistimis.uddl.uddl.PlatformView
import com.epistimis.uddl.uddl.LogicalView
import com.epistimis.uddl.uddl.UddlPackage
import org.eclipse.emf.ecore.EClass

/**
 * This is a set of methods that extract values from instances for use with templated methods
 * @author stevehickman
 *
 */
abstract class CLPExtractors {

	
	def dispatch static ConceptualEntity getSpecializes(ConceptualEntity ent) {
		return  ent.getSpecializes();
	}
	def dispatch static LogicalEntity getSpecializes(LogicalEntity ent) {
		return ent.getSpecializes();
	}
	def dispatch static PlatformEntity getSpecializes(PlatformEntity ent) {
		return ent.getSpecializes();
	}
	
	
	def dispatch static boolean isAssociation(ConceptualEntity ent) {
		return  (ent instanceof ConceptualAssociation);
	}
	def dispatch static boolean isAssociation(LogicalEntity ent) {
		return  (ent instanceof LogicalAssociation);
	}
	def dispatch static boolean isAssociation(PlatformEntity ent) {
		return  (ent instanceof PlatformAssociation);
	}

	def dispatch static ConceptualAssociation conv2Association(ConceptualEntity ent) {
		return ent as ConceptualAssociation;
	}	
	def dispatch static LogicalAssociation conv2Association(LogicalEntity ent) {
		return ent as LogicalAssociation;
	}
	def dispatch static PlatformAssociation conv2Association(PlatformEntity ent) {
		return  ent as PlatformAssociation;
	}

	def dispatch static EList<ConceptualComposition> getComposition(ConceptualEntity obj) {
		return obj.getComposition();
	}
	def dispatch static EList<LogicalComposition> getComposition(LogicalEntity obj) {
		return obj.getComposition();
	}
	def dispatch static EList<PlatformComposition> getComposition(PlatformEntity obj) {
		return obj.getComposition();
	}

	
	def dispatch static EList<ConceptualParticipant> getParticipant(ConceptualAssociation obj) {
		return obj.getParticipant();
	}
	def dispatch static EList<LogicalParticipant> getParticipant(LogicalAssociation obj) {
		return obj.getParticipant();
	}
	def dispatch static EList<PlatformParticipant> getParticipant(PlatformAssociation obj) {
		return obj.getParticipant();
	}

	def dispatch static String getSpecification(ConceptualQuery obj) {
		return obj.specification;
	}
	def dispatch static String getSpecification(LogicalQuery obj) {
		return obj.specification;
	}
	def dispatch static String getSpecification(PlatformQuery obj) {
		return obj.specification;
	}

	def dispatch static Class<? extends UddlElement> getClassForQuery(ConceptualQuery obj) {
		return typeof(ConceptualEntity);
	}
	def dispatch static Class<? extends UddlElement> getClassForQuery(LogicalQuery obj) {
		return typeof(LogicalEntity);
	}
	def dispatch static Class<? extends UddlElement> getClassForQuery(PlatformQuery obj) {
		return typeof(PlatformEntity);
	}

	def dispatch static EList<ConceptualQueryComposition> getComposition(ConceptualCompositeQuery ent) {
		return ent.composition;
	}
	def dispatch static EList<LogicalQueryComposition> getComposition(LogicalCompositeQuery ent) {
		return ent.composition;
	}
	def dispatch static EList<PlatformQueryComposition> getComposition(PlatformCompositeQuery ent) {
		return ent.composition;
	}

	def dispatch static ConceptualView getType(ConceptualQueryComposition obj) {
		return obj.type;
	}
	def dispatch static LogicalView getType(LogicalQueryComposition obj) {
		return obj.type;
	}
	def dispatch static PlatformView getType(PlatformQueryComposition obj) {
		return obj.type;
	}

	
	def dispatch static boolean isCompositeQuery(ConceptualView obj) {
		return (obj instanceof ConceptualCompositeQuery);
	}
	def dispatch static boolean isCompositeQuery(LogicalView obj) {
		return (obj instanceof LogicalCompositeQuery);
	}
	def dispatch static boolean isCompositeQuery(PlatformView obj) {
		return (obj instanceof PlatformCompositeQuery);
	}

	def dispatch static EClass getRelatedPackageEntityInstance(ConceptualQuery obj) {
		return 	UddlPackage.eINSTANCE.getConceptualEntity()
	}
	def dispatch static EClass getRelatedPackageEntityInstance(LogicalQuery obj) {
		return 	UddlPackage.eINSTANCE.getLogicalEntity()
	}
	def dispatch static EClass getRelatedPackageEntityInstance(PlatformQuery obj) {
		return 	UddlPackage.eINSTANCE.getPlatformEntity()
	}
	

	

}
