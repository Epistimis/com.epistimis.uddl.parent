/**
 * 
 */
package com.epistimis.uddl.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

import com.epistimis.uddl.LPRealizationProcessor;
import com.epistimis.uddl.LogicalEntityProcessor;
import com.epistimis.uddl.PlatformEntityProcessor;
import com.epistimis.uddl.uddl.LogicalAssociation;
import com.epistimis.uddl.uddl.LogicalCharacteristic;
import com.epistimis.uddl.uddl.LogicalComposition;
import com.epistimis.uddl.uddl.LogicalEntity;
import com.epistimis.uddl.uddl.LogicalParticipant;
import com.epistimis.uddl.uddl.PlatformAssociation;
import com.epistimis.uddl.uddl.PlatformCharacteristic;
import com.epistimis.uddl.uddl.PlatformComposition;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformParticipant;

/**
 * 
 */
public class LPRealizationProposalProcessor extends
		RealizationProposalProcessor<LogicalEntity, PlatformEntity, LogicalCharacteristic, PlatformCharacteristic, 
		LogicalComposition, PlatformComposition, LogicalParticipant, PlatformParticipant, 
		LogicalAssociation, PlatformAssociation, LPRealizationProcessor, LogicalEntityProcessor, PlatformEntityProcessor> {

	@Override
	protected void completeSuperRealizingComposition(UddlProposalProvider pp, EObject obj, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		pp.superComplete_PlatformComposition(obj, ruleCall, context, acceptor);	
	}

	@Override
	protected void completeSuperRealizingComposition_Rolename(UddlProposalProvider pp, EObject obj,
			Assignment assignment,ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		pp.superCompletePlatformComposition_Rolename(obj, assignment, context, acceptor);
		
	}

	@Override
	protected String proposalDisplayString(LogicalCharacteristic bc) {

		return proposalPrefix + bc.getRolename() + proposalSuffix;
	}

	@Override
	protected String compositionInsertionString(LogicalComposition bc) {
		return String.format(dummyType + compositionFormatString, bc.getRolename(),
				bc.getLowerBound(), bc.getUpperBound(), bc.getDescription(),
				qnp.getFullyQualifiedName(bc).toString());
	}

	@Override
	protected String participantInsertionString(LogicalParticipant bc) {
		return String.format(dummyType + participantFormatString, bc.getRolename(),
				bc.getLowerBound(), bc.getUpperBound(), bc.getDescription(),
				qnp.getFullyQualifiedName(bc).toString(), bc.getSourceLowerBound(), bc.getSourceUpperBound());
	}



}
