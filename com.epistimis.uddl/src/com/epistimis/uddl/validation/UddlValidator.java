/*
 * generated by Xtext 2.28.0
 */
package com.epistimis.uddl.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.ocl.xtext.completeocl.validation.CompleteOCLEObjectValidator;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;
import org.eclipse.xtext.validation.EValidatorRegistrar;
import org.eclipse.ocl.pivot.model.OCLstdlib;
import org.eclipse.ocl.pivot.utilities.ClassUtil;
import org.eclipse.ocl.pivot.utilities.NameUtil;

import com.epistimis.uddl.CLPExtractors;
import com.epistimis.uddl.uddl.ConceptualCharacteristic;
import com.epistimis.uddl.uddl.ConceptualEntity;
import com.epistimis.uddl.uddl.LogicalEntity;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.UddlElement;
import com.epistimis.uddl.uddl.UddlPackage;

/**
 * This class contains custom validation rules.
 *
 * See
 * https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
public class UddlValidator extends AbstractUddlValidator {

	protected static String ISSUE_CODE_PREFIX = "com.epistimis.uddl.";
	public static String ENTITY_NEEDS_2_ELEMENTS = ISSUE_CODE_PREFIX + "EntityNeeds2Elements";


	public EPackage getPackage() {
		return UddlPackage.eINSTANCE;
	}

	protected void loadAndRegister(EValidatorRegistrar registrar, String resourceAddress) {
		EPackage ePackage = getPackage();
		loadAndRegister(registrar,resourceAddress,ePackage);
	}
	protected void loadAndRegister(EValidatorRegistrar registrar, String resourceAddress, EPackage ePackage) {
		/**
		 * NOTE: AbstractInjectableValidator::register registers validators for the
		 * entire inheritance hierarchy ( because of the base class implementation of
		 * getEPackages() )
		 * 
		 * This does not do that. Each OCL file is associated with a specific package,
		 * so it need not be registered to others. If there is a need, manually
		 * re-register the OCL file for multiple packages.
		 * 
		 * See
		 * https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.ocl.doc%2Fhelp%2FInstallation.html
		 * for sample code
		 * 
		 * getInputURI replaces that example's URI creation
		 */
		URI oclURI = getInputURI(resourceAddress);
		CompleteOCLEObjectValidator v = new CompleteOCLEObjectValidator(ePackage, oclURI);
		for (EPackage pkg : getEPackages()) {
			registrar.register(pkg, v);
		}
		//registrar.register(ePackage, v);
	}
	protected void loadAndRegister(EValidatorRegistrar registrar, String resourceAddress, EPackage ePackage, @NonNull String pluginId) {
		/**
		 * NOTE: AbstractInjectableValidator::register registers validators for the
		 * entire inheritance hierarchy ( because of the base class implementation of
		 * getEPackages() )
		 * 
		 * This does not do that. Each OCL file is associated with a specific package,
		 * so it need not be registered to others. If there is a need, manually
		 * re-register the OCL file for multiple packages.
		 * 
		 * See
		 * https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.ocl.doc%2Fhelp%2FInstallation.html
		 * for sample code
		 * 
		 * getInputURI replaces that example's URI creation
		 */
		URI oclURI = getInputURI(resourceAddress, pluginId);
		registrar.register(ePackage, new CompleteOCLEObjectValidator(ePackage, oclURI));
	}

	@Override
	public void register(EValidatorRegistrar registrar) {
		super.register(registrar);

		/**
		 * Registrations here are for OCL we ALWAYS want available.
		 */
		OCLstdlib.install();
		loadAndRegister(registrar, "src/com/epistimis/uddl/constraints/realizedObservables.ocl"	,UddlPackage.eINSTANCE,com.epistimis.uddl.UddlRuntimeModule.PLUGIN_ID);
		loadAndRegister(registrar, "src/com/epistimis/uddl/constraints/logicalExtensions.ocl"	,UddlPackage.eINSTANCE,com.epistimis.uddl.UddlRuntimeModule.PLUGIN_ID);
//        loadAndRegister(registrar,"src/com/epistimis/uddl/constraints/specialCategoriesOfData.ocl");
		/**
		 * TODO: These don't appear to be having any effect. It could be because we have
		 * no way to invoke the validators created here. Or that they are invoked and
		 * fail silently.
		 * 
		 * Commented out to eliminate potential performance problems. These should be loaded and run only 
		 * on command - not here where they get triggered in the editor constantly.
		 */
//		loadAndRegister(registrar, "src/com/epistimis/uddl/constraints/uddl.ocl",UddlPackage.eINSTANCE,com.epistimis.uddl.UddlRuntimeModule.PLUGIN_ID);
//		loadAndRegister(registrar, "src/com/epistimis/uddl/constraints/datamodel.ocl",UddlPackage.eINSTANCE,com.epistimis.uddl.UddlRuntimeModule.PLUGIN_ID);
//		loadAndRegister(registrar, "src/com/epistimis/uddl/constraints/conceptual.ocl",UddlPackage.eINSTANCE,com.epistimis.uddl.UddlRuntimeModule.PLUGIN_ID);
//		loadAndRegister(registrar, "src/com/epistimis/uddl/constraints/logical.ocl",UddlPackage.eINSTANCE,com.epistimis.uddl.UddlRuntimeModule.PLUGIN_ID);
//		loadAndRegister(registrar, "src/com/epistimis/uddl/constraints/platform.ocl",UddlPackage.eINSTANCE,com.epistimis.uddl.UddlRuntimeModule.PLUGIN_ID);

	}

	/**
	 * Copied from org.eclipse.ocl.examples.pivot.tests.PivotTestCase.java:
	 * getModelURI See
	 * https://eclipse.googlesource.com/ocl/org.eclipse.ocl/+/refs/heads/master/tests/org.eclipse.ocl.examples.xtext.tests/src/org/eclipse/ocl/examples/pivot/tests/PivotTestCase.java
	 * and
	 * https://eclipse.googlesource.com/ocl/org.eclipse.ocl/+/refs/heads/master/tests/org.eclipse.ocl.examples.xtext.tests/src/org/eclipse/ocl/examples/test/xtext/PivotDocumentationExamples.java
	 * and
	 * https://eclipse.googlesource.com/ocl/org.eclipse.ocl/+/refs/heads/master/tests/org.eclipse.ocl.examples.xtext.tests/models/documentation/parsingDocumentsExample.ocl?autodive=0%2F%2F
	 * 
	 * @param localFileName - relative to the plugin root directory (not the Maven
	 *                      parent directory) - see examples
	 * @return a properly constructed URI
	 */
	protected @NonNull URI getInputURI(@NonNull String localFileName) {
		return getInputURI(localFileName, com.epistimis.uddl.UddlRuntimeModule.PLUGIN_ID);
	}

	protected static @NonNull URI getInputURI(@NonNull String localFileName, @NonNull String pluginId) {
		String plugInPrefix = pluginId + "/";
		URI plugURI = EcorePlugin.IS_ECLIPSE_RUNNING ? URI.createPlatformPluginURI(plugInPrefix, true)
				: URI.createPlatformResourceURI(plugInPrefix, true);
		URI localURI = URI.createURI(localFileName.startsWith("/") ? localFileName.substring(1) : localFileName);
		return localURI.resolve(plugURI);
	}


	protected void augmentRegistry(EPackage.Registry registry) {
		registry.put(UddlPackage.eNS_URI, UddlPackage.eINSTANCE);
	}

	/**
	 * In case we need a minimal registry (standalone runs - where we need to create
	 * the resource set as well) See
	 * https://eclipse.googlesource.com/ocl/org.eclipse.ocl/+/refs/heads/master/tests/org.eclipse.ocl.examples.xtext.tests/src/org/eclipse/ocl/examples/test/xtext/PivotDocumentationExamples.java
	 * 
	 * @return a Package Registry for this package
	 */
	protected EPackage.Registry createMinimalRegistry() {
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(UddlPackage.eNS_URI, UddlPackage.eINSTANCE);
		return registry;
	}

	/**
	 * Structures must have more than 1 member - but they can be inherited - so
	 * check entire specialization hierarchy for: (C/L/P)Entity TODO: Actually,
	 * since participants are for Associations, there must be at least 2
	 * Participants also Also -must check both composition and participant lists -
	 * The net across all of them must be at least 2
	 */

	@SuppressWarnings("unchecked")
	private static <Entity extends UddlElement, Characteristic, Association extends Entity, Participant extends Characteristic> List<Characteristic> getEntityCharacteristics(
			Entity ent) {

		List<Characteristic> results = new ArrayList<>();
		if (CLPExtractors.getSpecializes(ent) != null) {
			// If this specializes, then recursively get everything from what it specializes
			Entity ce = (Entity) ent;
			results.addAll(getEntityCharacteristics(ce));
		}
		/**
		 * Now check mine
		 */
		results.addAll((Collection<? extends Characteristic>) CLPExtractors.getComposition(ent));
		if (CLPExtractors.isAssociation(ent)) {
			Association ca = (Association) CLPExtractors.conv2Association(ent);
			results.addAll((Collection<? extends Characteristic>) CLPExtractors.getParticipant(ca));
		}
		return results;
	}

	@Check(CheckType.EXPENSIVE)
	public void checkCharacteristicCount(ConceptualEntity ent) {
		List<ConceptualCharacteristic> chars = getEntityCharacteristics(ent);
		if (chars.size() < 2) {
			error("Entity '" + ent.getName() + "' should have at least 2 characteristics",
					UddlPackage.eINSTANCE.getConceptualEntity_Composition(), ENTITY_NEEDS_2_ELEMENTS, ent.getName());
		}
	}

	@Check(CheckType.EXPENSIVE)
	public void checkCharacteristicCount(LogicalEntity ent) {
		List<ConceptualCharacteristic> chars = getEntityCharacteristics(ent);
		if (chars.size() < 2) {
			error("Entity '" + ent.getName() + "' should have at least 2 characteristics",
					UddlPackage.eINSTANCE.getLogicalEntity_Composition(), ENTITY_NEEDS_2_ELEMENTS, ent.getName());
		}
	}

	@Check(CheckType.EXPENSIVE)
	public void checkCharacteristicCount(PlatformEntity ent) {
		List<ConceptualCharacteristic> chars = getEntityCharacteristics(ent);
		if (chars.size() < 2) {
			error("Entity '" + ent.getName() + "' should have at least 2 characteristics",
					UddlPackage.eINSTANCE.getPlatformEntity_Composition(), ENTITY_NEEDS_2_ELEMENTS, ent.getName());
		}
	}
}
