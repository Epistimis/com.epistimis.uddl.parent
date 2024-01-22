/**
 * 
 */
package com.epistimis.uddl.ui.contentassist;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.core.Resource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.naming.QualifiedName;

import com.epistimis.uddl.UddlQNP;
import com.epistimis.uddl.scoping.IndexUtilities;
import com.google.inject.Inject;

/**
 * 
 */
public class PropUtils {

	@Inject IndexUtilities ndxUtils;
	@Inject UddlQNP qnp;
	
	final static String INDENT 						= "\t";

	public static String indent(int cnt) {
		StringBuilder ndentBldr = new StringBuilder();
		for (int i = 0; i < cnt; i++) {
			ndentBldr.append(INDENT);
		}
		return  ndentBldr.toString();
	}
	
	/**
	 * Return the shortest QN as string that will work as a valid reference, taking into account
	 * all the imported namespaces in the resource where the reference will go.
	 * @param ref The object to which we want the reference
	 * @param ctx The context object - where the reference will go 
	 * @return The reference string
	 */
	public String minimalReferenceString( EObject ref, EObject ctx) {
		// A minimal reference can be created based on the RQN - and then we can look at the imports
		// in the Resource containing the context object and shorten it further from there.
		QualifiedName result = qnp.relativeQualifiedName(ref, ctx);
		
		Resource res = ctx.eResource();
		EList<EObject>content = res.getContents();
		// Get the root instance, then all the includes. The challenge here is that the type
		// of the root object can differ depending on the file type. So we need to query for the includes using
		// AQL
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("self", content.get(0));
		
		// This works because all grammars use the same 'includes' feature name including.
		Collection<EObject> importedNamespaces = ndxUtils.processAQL(res.getResourceSet(), variables, "self.eGet('includes').eGet('importedNamespace')");
		
		// Now get the 'importedNamespace' from each, remove the wildcard (if it's there) and convert the remainder
		// to a QualifiedName. Then compare to the rqn to see if we can shorten the RQN.
		for (Object o: importedNamespaces) {
			String str = o.toString();
			QualifiedName testQN = qnp.minimalQualifiedName(ref, str);
			if (testQN.getSegmentCount() < result.getSegmentCount()) {
				result = testQN;
			}
		}
		return result.toString();
	}

}
