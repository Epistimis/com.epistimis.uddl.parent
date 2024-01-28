/**
 * 
 */
package com.epistimis.uddl.ui.contentassist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.core.Resource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.impl.QualifiedNameValueConverter;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.hover.IEObjectHover;

import com.epistimis.uddl.UddlQNP;
import com.epistimis.uddl.scoping.IndexUtilities;
import com.google.inject.Inject;

/**
 * 
 */
public class PropUtils {

	@Inject
	IndexUtilities ndxUtils;

	@Inject
	UddlQNP qnp;

	@Inject
	private IScopeProvider scopeProvider;

	@Inject
	private IEObjectHover hover;

	final static String INDENT = "\t";

	@Inject
	private IQualifiedNameConverter qualifiedNameConverter;

	public IQualifiedNameConverter getQualifiedNameConverter() {
		return qualifiedNameConverter;
	}

	public QualifiedNameValueConverter getQualifiedNameValueConverter() {
		return qnp.getQualifiedNameValueConverter();
	}

	public static String indent(int cnt) {
		StringBuilder ndentBldr = new StringBuilder();
		for (int i = 0; i < cnt; i++) {
			ndentBldr.append(INDENT);
		}
		return ndentBldr.toString();
	}

	/**
	 * Generic list of candidates for a reference. Some cases can limit this based
	 * on some criteria.
	 * 
	 * TODO: Should I just use IndexUtilities.getVisibleEObjectDescriptions() ?
	 * @param model     The instance where the reference is
	 * @param reference The reference needing candidates
	 * @return
	 */
	Iterable<IEObjectDescription> getCandidateDescriptions(EObject model, EReference reference) {
		return scopeProvider.getScope(model, reference).getAllElements();
	}

	/**
	 * TODO: Should I just use IndexUtilities.getVisibleEObjects() ?
	 * @param model
	 * @param reference
	 * @return
	 */
	List<EObject> getCandidates(EObject model, EReference reference) {
		Iterable<IEObjectDescription> descriptions = getCandidateDescriptions(model, reference);
		List<EObject> result = new ArrayList<EObject>();
		for (IEObjectDescription description : descriptions) {
			result.add(IndexUtilities.objectFromDescription(model.eResource(),description));
		}
		return result;
	}

	/**
	 * Return the shortest QN as string that will work as a valid reference, taking
	 * into account all the imported namespaces in the resource where the reference
	 * will go.
	 * 
	 * @param ref The object to which we want the reference
	 * @param ctx The context object - where the reference will go
	 * @return The reference string
	 */
	public String minimalReferenceString(EObject ref, EObject ctx) {
		// A minimal reference can be created based on the RQN - and then we can look at
		// the imports
		// in the Resource containing the context object and shorten it further from
		// there.
		QualifiedName result = qnp.relativeQualifiedName(ref, ctx);

		Resource res = ctx.eResource();
		EList<EObject> content = res.getContents();
		// Get the root instance, then all the includes. The challenge here is that the
		// type
		// of the root object can differ depending on the file type. So we need to query
		// for the includes using
		// AQL
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("self", content.get(0));

		// This works because all grammars use the same 'includes' feature name
		// including.
		Collection<EObject> importedNamespaces = ndxUtils.processAQL(res.getResourceSet(), variables,
				"self.eGet('includes').eGet('importedNamespace')");

		// Now get the 'importedNamespace' from each, remove the wildcard (if it's
		// there) and convert the remainder
		// to a QualifiedName. Then compare to the rqn to see if we can shorten the RQN.
		for (Object o : importedNamespaces) {
			String str = o.toString();
			QualifiedName testQN = qnp.minimalQualifiedName(ref, str);
			if (testQN.getSegmentCount() < result.getSegmentCount()) {
				result = testQN;
			}
		}
		return result.toString();
	}

	/**
	 * Replace proposal insertion text with a minimized RQN if possible. Cloned from
	 * https://www.eclipse.org/forums/index.php/t/583114/
	 * 
	 * @param context
	 * @param typeScope
	 * @param qualifiedNameConverter
	 * @param valueConverter
	 * @return
	 */
	public ConfigurableCompletionProposal.IReplacementTextApplier createTextApplier(Resource resource,
			IScope typeScope, IQualifiedNameConverter qualifiedNameConverter, IValueConverter<String> valueConverter) {
		return new FQNShortener(resource, typeScope, qualifiedNameConverter, valueConverter);
	}

	/**
	 * Modify the proposal as needed - cloned from
	 * https://www.eclipse.org/forums/index.php/t/583114/
	 * 
	 * TODO:  It doesn't 'grey' out the FQNs of unselected alternatives. 
	 * 
	 * @param theProposal
	 * @param context
	 * @param ref
	 * @return
	 */
	public ICompletionProposal modifyConfigurableCompletionProposal(ICompletionProposal theProposal,
			ContentAssistContext context, EReference ref, String additionalInfo) {
		IScope typeScope = null;
		if (context.getCurrentModel() != null) {
			typeScope = scopeProvider.getScope(context.getCurrentModel(), ref);
		}
		if (theProposal != null && theProposal instanceof ConfigurableCompletionProposal) {
			ConfigurableCompletionProposal configurableCompletionProposal = (ConfigurableCompletionProposal) theProposal;
			// Use 'new Provider<EObject>() {}' if EObject should be found in the first
			// place
			configurableCompletionProposal.setAdditionalProposalInfo(additionalInfo);
			configurableCompletionProposal.setHover(hover);
			configurableCompletionProposal.setTextApplier(createTextApplier(context.getResource(), typeScope,
					getQualifiedNameConverter(), getQualifiedNameValueConverter()));
		}
		return theProposal;
	}


}
