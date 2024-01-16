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

import com.epistimis.uddl.exceptions.RealizationException;
import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.UddlElement;
import com.google.inject.Inject;

/**
 * 
 */
public abstract class RealizationProcessor<BaseEntity extends EObject, RealizingEntity extends UddlElement, 
											BaseCharacteristic extends EObject, RealizingCharacteristic extends EObject, 
											BaseComposition extends BaseCharacteristic, RealizingComposition extends RealizingCharacteristic, 
											BaseParticipant extends BaseCharacteristic, RealizingParticipant extends RealizingCharacteristic, 
											BaseAssociation extends BaseEntity, RealizingAssociation extends RealizingEntity,
											BaseProcessor extends EntityProcessor<?, BaseCharacteristic, BaseEntity, BaseAssociation, BaseComposition, BaseParticipant, ?, ?>, 
											RealizingProcessor extends EntityProcessor<?, RealizingCharacteristic, RealizingEntity, RealizingAssociation, RealizingComposition, RealizingParticipant, ?, ?>> {
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
	public Class getBaseAssociationType() {
		return returnedTypeParameter(8);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingAssociationType() {
		return returnedTypeParameter(9);
	}


	@SuppressWarnings("rawtypes")
	public Class getBaseProcessorType() {
		return returnedTypeParameter(10);
	}

	@SuppressWarnings("rawtypes")
	public Class getRealizingProcessorType() {
		return returnedTypeParameter(11);
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

	public List<BaseParticipant> getRealizedParticipants(RealizingEntity rentity) {
		if (!realizingProcessor.isAssociation(rentity)) {
			// If it isn't an association then it has no participants
			BaseEntity be = getRealizeEntity(rentity);
			if (baseProcessor.isAssociation(be)) {
				// If the base is an association but the realization isn't, then that's an error
				String msg = MessageFormat.format("Association {0} must be realized by an Association but {0} is an Entity", 
						qnp.getFullyQualifiedName(be).toString(), qnp.getFullyQualifiedName(rentity).toString());
				logger.error(msg);
				//throw new RealizationException(msg); // TODO: Throw or return empty list? See also (logical/platform)Extensions.ocl invariants - this is checked there
				return new ArrayList<BaseParticipant>();
			}
			else {
				// This is not an association - it has no participants
				return new ArrayList<BaseParticipant>();
			}
		}
		else {
			Map<String, RealizingParticipant> allParticipants = realizingProcessor.allParticipants(realizingProcessor.conv2Association(rentity));
			// Select the keys from allCompositions - filter out all other CCs
			List<BaseParticipant> result = new ArrayList<BaseParticipant>();
			for (RealizingParticipant p : allParticipants.values()) {
				result.add(getRealizedParticipant(p));
			}
			return result;
			
		}
	}

	public Collection<BaseParticipant> getUnrealizedParticipants(RealizingEntity rentity) {
		List<BaseParticipant> realized = getRealizedParticipants(rentity);

		BaseEntity baseEntity = getRealizeEntity(rentity);
		if (!baseProcessor.isAssociation(baseEntity)) {
			// Base isn't an association so there are no participants
			return new ArrayList<BaseParticipant>();
		} else {
			Map<String, BaseParticipant> allBaseParticipants = baseProcessor.allParticipants(baseProcessor.conv2Association(baseEntity));

			Collection<BaseParticipant> remainingValues = allBaseParticipants.values();
			remainingValues.removeAll(realized);
			return remainingValues;
			
		}
	}

}
