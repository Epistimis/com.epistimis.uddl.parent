/**
 * 
 */
package com.epistimis.uddl.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

import com.epistimis.uddl.CLRealizationProcessor;
import com.epistimis.uddl.ConceptualEntityProcessor;
import com.epistimis.uddl.LogicalEntityProcessor;
import com.epistimis.uddl.uddl.ConceptualAssociation;
import com.epistimis.uddl.uddl.ConceptualCharacteristic;
import com.epistimis.uddl.uddl.ConceptualComposition;
import com.epistimis.uddl.uddl.ConceptualEntity;
import com.epistimis.uddl.uddl.ConceptualParticipant;
import com.epistimis.uddl.uddl.LogicalAssociation;
import com.epistimis.uddl.uddl.LogicalCharacteristic;
import com.epistimis.uddl.uddl.LogicalComposition;
import com.epistimis.uddl.uddl.LogicalEntity;
import com.epistimis.uddl.uddl.LogicalParticipant;

/**
 * 
 */
public class CLRealizationProposalProcessor extends
		RealizationProposalProcessor<ConceptualEntity, LogicalEntity, ConceptualCharacteristic, LogicalCharacteristic, 
		ConceptualComposition, LogicalComposition, ConceptualParticipant, LogicalParticipant, 
		ConceptualAssociation, LogicalAssociation, CLRealizationProcessor, ConceptualEntityProcessor, LogicalEntityProcessor> {

	@Override
	protected void completeSuperRealizingComposition(UddlProposalProvider pp, EObject obj, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		
		pp.superComplete_LogicalComposition(obj, ruleCall, context, acceptor);	
	}

	@Override
	protected void completeSuperRealizingComposition_Rolename(UddlProposalProvider pp, EObject obj,
			Assignment assignment,ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		pp.superCompleteLogicalComposition_Rolename(obj, assignment, context, acceptor);
		
	}
	
	@Override
	protected String proposalDisplayString(ConceptualCharacteristic bc) {

		return proposalPrefix + bc.getRolename() + proposalSuffix;
	}

	@Override
	protected String compositionInsertionString(ConceptualComposition bc) {
		return String.format(dummyType + compositionFormatString, bc.getRolename(),
				bc.getLowerBound(), bc.getUpperBound(), bc.getDescription(),
				qnp.getFullyQualifiedName(bc).toString());
	}

	@Override
	protected String participantInsertionString(ConceptualParticipant bc) {
		return String.format(dummyType + participantFormatString, bc.getRolename(),
				bc.getLowerBound(), bc.getUpperBound(), bc.getDescription(),
				qnp.getFullyQualifiedName(bc).toString(), bc.getSourceLowerBound(), bc.getSourceUpperBound());
	}





}
