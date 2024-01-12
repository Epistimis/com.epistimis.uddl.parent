/**
 * 
 */
package com.epistimis.uddl;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;

import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.UddlElement;
import com.google.inject.Inject;

/**
 * 
 */
public abstract class RealizationProcessor<BaseEntity extends UddlElement, RealizingEntity extends UddlElement, BaseCharacteristic extends EObject, RealizingCharacteristic extends EObject, BaseComposition extends BaseCharacteristic, RealizingComposition extends RealizingCharacteristic, BaseParticipant extends BaseCharacteristic, RealizingParticipant extends RealizingCharacteristic, BaseProcessor extends EntityProcessor<?, BaseCharacteristic, BaseEntity, ?, BaseComposition, BaseParticipant, ?, ?>, RealizingProcessor extends EntityProcessor<?, RealizingCharacteristic, RealizingEntity, ?, RealizingComposition, RealizingParticipant, ?, ?>> {
// @Inject
//private Provider<ResourceSet> resourceSetProvider;
//
//
//@Inject
//IResourceServiceProvider.Registry reg;
//
//IResourceServiceProvider queryRSP;
//IResourceFactory queryResFactory;

//@Inject
//ParseHelper<QuerySpecification> parseHelper;

	@Inject
	IndexUtilities ndxUtil;

	@Inject
	IQualifiedNameProvider qnp;

	@Inject
	IQualifiedNameConverter qnc;

	@Inject
	BaseProcessor baseProcessor;
	@Inject
	RealizingProcessor realizingProcessor;

	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	static MessageFormat CharacteristicNotFoundMsgFmt = new MessageFormat(
			"Entity {0} does not have a characteristic with rolename {1}");

	abstract public BaseEntity getRealizeEntity(RealizingEntity rent);

	abstract public BaseComposition getRealizedComposition(RealizingComposition rcomp);

	abstract public BaseParticipant getRealizedParticipant(RealizingParticipant rpart);

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
	public Class getBaseEntityType() {
		return returnedTypeParameter(0);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingEntityType() {
		return returnedTypeParameter(1);
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseCharacteristicType() {
		return returnedTypeParameter(2);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingCharacteristicType() {
		return returnedTypeParameter(3);
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseCompositionType() {
		return returnedTypeParameter(4);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingCompositionType() {
		return returnedTypeParameter(5);
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseParticipantType() {
		return returnedTypeParameter(6);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingParticipantType() {
		return returnedTypeParameter(7);
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseProcessorType() {
		return returnedTypeParameter(8);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingProcessorType() {
		return returnedTypeParameter(9);
	}

	public List<BaseComposition> getRealizedCompositions(RealizingEntity rentity) {
		Map<String, RealizingComposition> allCompositions = realizingProcessor.allCompositions(rentity);
		// Select the keys from allCompositions - filter out all other CCs
		List<BaseComposition> result = new ArrayList<BaseComposition>();
		for (RealizingComposition c : allCompositions.values()) {
			result.add(getRealizedComposition(c));
		}
		return result;
	}

	public Collection<BaseComposition> getUnrealizedCompositions(RealizingEntity rentity) {
		List<BaseComposition> realized = getRealizedCompositions(rentity);

		BaseEntity baseEntity = getRealizeEntity(rentity);
		Map<String, BaseComposition> allBaseCompositions = baseProcessor.allCompositions(baseEntity);

		Collection<BaseComposition> remainingValues = allBaseCompositions.values();
		remainingValues.removeAll(realized);
		return remainingValues;
	}

}
