package com.epistimis.uddl

import com.epistimis.uddl.uddl.ConceptualAssociation
import com.epistimis.uddl.uddl.ConceptualComposition
import com.epistimis.uddl.uddl.ConceptualEntity
import com.epistimis.uddl.uddl.ConceptualObservable
import com.epistimis.uddl.uddl.ConceptualParticipant
import com.epistimis.uddl.uddl.LogicalAssociation
import com.epistimis.uddl.uddl.LogicalComposition
import com.epistimis.uddl.uddl.LogicalEntity
import com.epistimis.uddl.uddl.LogicalMeasurement
import com.epistimis.uddl.uddl.LogicalParticipant
import com.epistimis.uddl.uddl.PlatformAssociation
import com.epistimis.uddl.uddl.PlatformComposition
import com.epistimis.uddl.uddl.PlatformDataType
import com.epistimis.uddl.uddl.PlatformEntity
import com.epistimis.uddl.uddl.PlatformParticipant
import java.util.HashSet
import java.util.Set
import java.util.stream.Collectors

/**
 * A set of dispatch methods that can be used to drill down through a UDDL model to find the observables that are 
 * actually realized by something. Why does this matter? Because Logical and Platform levels may down-select (not include)
 * all the composition elements they could
 */
class RealizedConceptuals {

	def dispatch Set<String> constituentObservables(ConceptualObservable elem) {

		return Set.of(elem.name.toLowerCase);
	}

	def dispatch Set<String> constituentObservables(ConceptualEntity elem) {

		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentComposition = elem.composition.stream.flatMap([constituentObservables.stream()]).collect(
			Collectors.toSet())

		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentComposition !== null) {
			constituents.addAll(constituentComposition);
		}
		return constituents;
	}

	def dispatch Set<String> constituentObservables(ConceptualAssociation elem) {
		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentComposition = elem.composition.stream.flatMap([constituentObservables.stream()]).collect(
			Collectors.toSet())
		val constituentParticipant = elem.participant.stream.flatMap([constituentObservables.stream()]).collect(
			Collectors.toSet())

		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentComposition !== null) {
			constituents.addAll(constituentComposition);
		}
		if (constituentParticipant !== null) {
			constituents.addAll(constituentParticipant);
		}

		return constituents;

	}

	def dispatch Set<String> constituentObservables(ConceptualComposition elem) {
		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentType = elem.type.constituentObservables;
		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentType !== null) {
			constituents.addAll(constituentType);
		}

		return constituents;

	}

	def dispatch Set<String> constituentObservables(ConceptualParticipant elem) {

		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentType = elem.type.constituentObservables;
		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentType !== null) {
			constituents.addAll(constituentType);
		}

		return constituents;
	}

	def dispatch Set<String> constituentObservables(LogicalMeasurement elem) {

		if (elem !== null) {
			return elem.realizes.constituentObservables;
		} else {
			return new HashSet<String>();
		}
	}

	/**
	 * Note that we do *not* look at the ConceptualEntity that this LogicalEntity realizes. Why? Because
	 * what we care about is not what *could* have been realized but what was *actually* realized. That 
	 * means looking at the composition elements only (directly or via specialization)
	 */
	def dispatch Set<String> constituentObservables(LogicalEntity elem) {

		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentComposition = elem.composition.stream.flatMap([realizes?.constituentObservables.stream()]).
			collect(Collectors.toSet())

		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentComposition !== null) {
			constituents.addAll(constituentComposition);
		}
		return constituents;
	}

	/**
	 * Note that we do *not* look at the ConceptualAssociation that this LogicalAssociation realizes. Why? Because
	 * what we care about is not what *could* have been realized but what was *actually* realized. That 
	 * means looking at the composition elements only (directly or via specialization)
	 */
	def dispatch Set<String> constituentObservables(LogicalAssociation elem) {

		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentComposition = elem.composition.stream.flatMap([realizes?.constituentObservables.stream()]).
			collect(Collectors.toSet())
		val constituentParticipant = elem.participant.stream.flatMap([realizes?.constituentObservables.stream()]).
			collect(Collectors.toSet())

		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentComposition !== null) {
			constituents.addAll(constituentComposition);
		}
		if (constituentParticipant !== null) {
			constituents.addAll(constituentParticipant);
		}

		return constituents;

	}

	def dispatch Set<String> constituentObservables(LogicalComposition elem) {
		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentType = elem.type.constituentObservables;
		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentType !== null) {
			constituents.addAll(constituentType);
		}

		return constituents;
	}

	def dispatch Set<String> constituentObservables(LogicalParticipant elem) {

		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentType = elem.type.constituentObservables;
		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentType !== null) {
			constituents.addAll(constituentType);
		}

		return constituents;
	}

	def dispatch Set<String> constituentObservables(PlatformDataType elem) {

		if (elem !== null) {
			return elem.realizes.constituentObservables;
		} else {
			return new HashSet<String>();
		}
	}

	/**
	 * Note that we do *not* look at the Conceptual/LogicalEntity that this PlatformEntity realizes. Why? Because
	 * what we care about is not what *could* have been realized but what was *actually* realized. That 
	 * means looking at the composition elements only (directly or via specialization)
	 */
	def dispatch Set<String> constituentObservables(PlatformEntity elem) {

		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentComposition = elem.composition.stream.flatMap([realizes?.realizes?.constituentObservables.stream()]).
			collect(Collectors.toSet())

		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentComposition !== null) {
			constituents.addAll(constituentComposition);
		}
		return constituents;
	}

	/**
	 * Note that we do *not* look at the Conceptual/LogicalAssociation that this PlatformAssociation realizes. Why? Because
	 * what we care about is not what *could* have been realized but what was *actually* realized. That 
	 * means looking at the composition elements only (directly or via specialization)
	 * 
	 * Note that this implementation drills down via realization immediately, getting to the Conceptual level without
	 * going through other paths.
	 */
	def dispatch Set<String> constituentObservables(PlatformAssociation elem) {

		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentComposition = elem.composition.stream.flatMap([realizes?.realizes?.constituentObservables.stream()]).
			collect(Collectors.toSet())
		val constituentParticipant = elem.participant.stream.flatMap([realizes?.realizes?.constituentObservables.stream()]).
			collect(Collectors.toSet())

		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentComposition !== null) {
			constituents.addAll(constituentComposition);
		}
		if (constituentParticipant !== null) {
			constituents.addAll(constituentParticipant);
		}

		return constituents;
	}

/**
 * TODO: Is it possible that the type of a PlatformComposition will be complex and introduce Conceptual/Logical elements via
 * realization that would not otherwise show up by following the realization path directly? I don't think so - but need to
 * think that through.
 */
	def dispatch Set<String> constituentObservables(PlatformComposition elem) {
		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentType = elem.type.constituentObservables;
		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentType !== null) {
			constituents.addAll(constituentType);
		}

		return constituents;
	}

	def dispatch Set<String> constituentObservables(PlatformParticipant elem) {

		var constituents = new HashSet<String>();
		val constituentSpecializes = elem.specializes?.constituentObservables;
		val constituentType = elem.type.constituentObservables;
		if (constituentSpecializes !== null) {
			constituents.addAll(constituentSpecializes);
		}
		if (constituentType !== null) {
			constituents.addAll(constituentType);
		}

		return constituents;
	}

}
