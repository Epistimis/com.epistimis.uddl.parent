/**
 * 
 */
package com.epistimis.uddl.ui.contentassist;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

import com.epistimis.uddl.EntityProcessor;
import com.epistimis.uddl.RealizationProcessor;
import com.epistimis.uddl.UddlQNP;
import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.UddlElement;
import com.google.inject.Inject;

/**
 * 
 */
abstract class EntityRealizationProposalProcessor<BaseComposableElement extends UddlElement, RealizingComposableElement extends UddlElement, 
											BaseEntity extends BaseComposableElement, RealizingEntity extends RealizingComposableElement, 
											BaseCharacteristic extends EObject, RealizingCharacteristic extends EObject, 
											BaseComposition extends BaseCharacteristic, RealizingComposition extends RealizingCharacteristic, 
											BaseParticipant extends BaseCharacteristic, RealizingParticipant extends RealizingCharacteristic, 
											BaseAssociation extends BaseEntity, RealizingAssociation extends RealizingEntity, 
											RezProcessor extends RealizationProcessor<BaseComposableElement, RealizingComposableElement, BaseEntity, RealizingEntity, BaseCharacteristic, RealizingCharacteristic, BaseComposition, RealizingComposition, BaseParticipant, RealizingParticipant, BaseAssociation, RealizingAssociation, BaseProcessor, RealizingProcessor>, 
											BaseProcessor extends EntityProcessor<BaseComposableElement, BaseCharacteristic, BaseEntity, BaseAssociation, BaseComposition, BaseParticipant, ?, ?>, 
											RealizingProcessor extends EntityProcessor<RealizingComposableElement, RealizingCharacteristic, RealizingEntity, RealizingAssociation, RealizingComposition, RealizingParticipant, ?, ?>> {

	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	protected static String compositionFormatString = " %s[%d:%d] \"%s\" -> %s;\n";
	protected static String participantFormatString = " %s[%d:%d] \"%s\" -> %s { source: [ %s : %d ] };\n";
	protected static String dummyType = "__ReplaceMe__";
	protected static String defaultComment = "// Replace " + dummyType
			+ " with the ComposableElement type for each composition\n";
	protected static String proposalPrefix = "(Default) ";
	protected static String proposalSuffix = "";
	protected static String realizeAll = "<<Default Realize All>>";
	protected static String realizeRemaining = "<<Default Realize Remaining>>";

	@Inject	UddlQNP qnp;
	@Inject PropUtils pu;
	
	@Inject IndexUtilities ndxUtil;
	
	@Inject RezProcessor rezProc;
	@Inject BaseProcessor bProc;
	@Inject RealizingProcessor rProc; 

	abstract protected void completeSuperRealizingComposition(UddlProposalProvider pp, EObject obj, RuleCall ruleCall,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor);

	abstract protected void completeSuperRealizingComposition_Rolename(UddlProposalProvider pp, EObject obj,
			Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor);

	abstract protected String proposalDisplayString(BaseCharacteristic bc);

	abstract protected String compositionInsertionString(BaseComposition bc, String indent);

	abstract protected String participantInsertionString(BaseParticipant bc, String indent);

	/**
	 * Realizing types don't have a common ancestor other than EObject. Cast them appropriately
	 * in the implementation.
	 * @param realizingType
	 * @return
	 */
	abstract protected String getRealizingTypeName(EObject realizingType);
	
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
	@SuppressWarnings("rawtypes")
	public Class getBaseComposableElementType() {
		return returnedTypeParameter(0);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingComposableElementType() {
		return returnedTypeParameter(1);
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseEntityType() {
		return returnedTypeParameter(2);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingEntityType() {
		return returnedTypeParameter(3);
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseCharacteristicType() {
		return returnedTypeParameter(4);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingCharacteristicType() {
		return returnedTypeParameter(5);
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseCompositionType() {
		return returnedTypeParameter(6);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingCompositionType() {
		return returnedTypeParameter(7);
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseParticipantType() {
		return returnedTypeParameter(8);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingParticipantType() {
		return returnedTypeParameter(9);
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseAssociationType() {
		return returnedTypeParameter(10);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingAssociationType() {
		return returnedTypeParameter(11);
	}

	@SuppressWarnings("rawtypes")
	public Class getRezProcessorType() {
		return returnedTypeParameter(12);
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseProcessorType() {
		return returnedTypeParameter(13);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingProcessorType() {
		return returnedTypeParameter(14);
	}

	/**
	 * In general, we want to indent the content of any container based on the number of levels so far,
	 * which we can determine by looking at the number segments in the FQN
	 * @param object
	 * @return
	 */
	protected String contentIndent(EObject object) {
		return PropUtils.indent(qnp.getFullyQualifiedName(object).getSegmentCount());

	}
	public void complete_Composition(UddlProposalProvider pp, RezProcessor rproc, RealizingEntity rentity,
			RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		// Get all the standard stuff first
		completeSuperRealizingComposition(pp, rentity, ruleCall, context, acceptor);

		String indent = contentIndent(rentity);
		// Now add customization here
		// When doing this, propose that all ConceptualCompositions be realized - but
		// only those that
		List<BaseComposition> realized = rproc.getRealizedCompositions(rentity);
		Collection<BaseComposition> unrealized = rproc.getUnrealizedCompositions(rentity);
		List<BaseParticipant> realizedParticipants = rproc.getRealizedParticipants(rentity);
		Collection<BaseParticipant> unrealizedParticipants = rproc.getUnrealizedParticipants(rentity);

		String result = defaultComment;
		for (BaseComposition cc : unrealized) {
			// If this one isn't already realized, then add it to the proposal
			String oneRealizedCC = compositionInsertionString(cc,indent);
			String displayString = proposalDisplayString(cc);
			acceptor.accept(pp.createCompletionProposal(oneRealizedCC, displayString, null, context));
			result += oneRealizedCC;
		}
		if (!unrealizedParticipants.isEmpty()) {
			result += "\n participants: [";

			for (BaseParticipant cp : unrealizedParticipants) {
				// If this one isn't already realized, then add it to the proposal
				String oneRealizedCP = participantInsertionString(cp,indent);
				String proposalString = proposalDisplayString(cp);
				acceptor.accept(pp.createCompletionProposal(oneRealizedCP, proposalString, null, context));
				result += oneRealizedCP;
			}
			result += "]";
		}
		/**
		 * Only do the "all" if nothing has been done yet
		 */
		if (realized.isEmpty() && realizedParticipants.isEmpty()) {
			acceptor.accept(pp.createCompletionProposal(result, realizeAll, null, context));
		} else if (!unrealized.isEmpty() || !unrealizedParticipants.isEmpty()) {
			acceptor.accept(pp.createCompletionProposal(result, realizeRemaining, null, context));
		}

	}

	private Collection<EObject> getRealizingTypes(BaseComposition bcomp) {
		BaseComposableElement baseType = bProc.getCompositionType(bcomp);
		if (baseType != null) {
			return rezProc.getRealizingTypes(baseType);
		}
		return new HashSet<>();
	}
	/**
	 * The type must be a type that realizes the BaseComposition, if it has been
	 * specified. If not, then it must be a type that realizes one of the
	 * BaseCompositions from the BaseEntity realized by the containing
	 * RealizingEntity.
	 * 
	 * @param pp
	 * @param rentity
	 * @param assignment
	 * @param context
	 * @param acceptor
	 */
	public void completeComposition_Type(UddlProposalProvider pp,  RealizingEntity rentity, //RealizingComposition rcomp,
					Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
//		BaseComposition bcomp = rezProc.getRealizedComposition(rcomp);
//		BaseComposableElement baseType = bProc.getCompositionType(bcomp);
		Set<EObject> realizingTypes = new HashSet<EObject>();
//		if (baseType != null) {
//			realizingTypes = rezProc.getRealizingTypes(baseType);
//		}
//		else {
			// Get the types for all the composition elements in the realized Entity
			BaseEntity bentity = rezProc.getRealizedEntity(rentity);
			Collection<BaseComposition> bcomps = bProc.allCompositions(bentity).values();
			for (BaseComposition bc : bcomps) {
				realizingTypes.addAll(getRealizingTypes(bc));
			}
//		}
		/*
		 * If there are **no** realizing types, then we've got a lot of work to do
		 */
		if (realizingTypes.isEmpty()) {
			String msg = MessageFormat.format("No realizing types found for any composition elements of {0}",qnp.getFullyQualifiedName(rentity));
			logger.info(msg);
		}
		for (EObject rce: realizingTypes) {
			String insertionString = pu.minimalReferenceString( rce, rentity);
			String displayString = getRealizingTypeName(rce);
			acceptor.accept(pp.createCompletionProposal(insertionString, displayString, null, context));			
		}
	}

	public void completeComposition_Rolename(UddlProposalProvider pp, RezProcessor rproc, RealizingEntity rentity,
			Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		completeSuperRealizingComposition_Rolename(pp, rentity, assignment, context, acceptor);

		// Pick out the roles from the list of unrealized Compositions
		for (BaseComposition cc : rproc.getUnrealizedCompositions(rentity)) {
			// If this one isn't already realized, then add it to the proposal
			String oneRealizedCC = compositionInsertionString(cc,"");
			String displayString = proposalDisplayString(cc);
			acceptor.accept(pp.createCompletionProposal(oneRealizedCC, displayString, null, context));
		}
	}

	public void completeComposition_Realizes(UddlProposalProvider pp, RezProcessor rproc, RealizingEntity rentity,
			Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {

		for (BaseComposition cc : rproc.getUnrealizedCompositions(rentity)) {
			String displayString = proposalDisplayString(cc);
			acceptor.accept(pp.createCompletionProposal(qnp.getFullyQualifiedName(cc).toString(), displayString, null,
					context));
		}

	}

}
