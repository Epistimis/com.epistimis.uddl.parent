package com.epistimis.uddl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.epistimis.uddl.uddl.PlatformComposition;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformParticipant;
import com.epistimis.uddl.scoping.IndexUtilities;
import com.epistimis.uddl.uddl.PlatformAssociation;

public class RealizedAssociation extends RealizedEntity {

	/**
	 * The key will be the rolename of the participant. If a specialization results in a change in rolename, then the entry will be
	 * removed from the old rolename and reinserted using the new rolename.
	 */
	private Map<String, RealizedParticipant> participant;

	public RealizedAssociation(@NonNull PlatformAssociation pa) {
		super(pa);

		participant = processSpecializationForParticipants(pa);

	}

	protected Map<String, RealizedParticipant> processSpecializationForParticipants(PlatformAssociation pa) {
		Map<String, RealizedParticipant> participantsSoFar;
		/**
		 * First recurse if this is also specialized, the process locally. That allows locals to override anything
		 * inherited via specialization
		 */
		PlatformEntity specializedEntity = IndexUtilities.unProxiedEObject(pa.getSpecializes(),pa);
		if ((specializedEntity != null) && (specializedEntity instanceof PlatformAssociation)) {
			/**
			 * This assumes that once we inherit from PlatformAssociation, everything down the specialization hierarchy
			 * must also be a PlatformAssociation.
			 */
			participantsSoFar = processSpecializationForParticipants((PlatformAssociation)specializedEntity);
		} else {
			participantsSoFar = new HashMap<String, RealizedParticipant>();
		}
		return processLocalParticipants(pa,participantsSoFar);
	}

	protected Map<String, RealizedParticipant> processLocalParticipants(PlatformAssociation pe, Map<String, RealizedParticipant> participantsSoFar) {
		/**
		 * NOTE: We do not merge participantsSoFar into results deliberately. Why? Because we might have multiple
		 * compositions that rename on specialization. In fact, we might have several that swap names. If we 
		 * were to merge these maps, we would not be able to tell if we could remove something from 'results'
		 * or not, because we wouldn't know if the 'results' map entry was an updated version reusing a name. 
		 * By keeping the maps separate, we we can do that safely. Then, at the very end, we merge what is left 
		 * of participantsSoFar into results - everything we want to 'override' has already been removed from it.
		 */
		Map<String,RealizedParticipant> results = new HashMap<String, RealizedParticipant>();
		for (PlatformParticipant pc: pe.getParticipant()) {
			RealizedParticipant rp = null;
			PlatformParticipant specializedPart = (PlatformParticipant) IndexUtilities.unProxiedEObject(pc.getSpecializes(),pc);
			if (specializedPart != null) {
				/** this is already in the map, find it by the rolename */
				 rp = participantsSoFar.remove(specializedPart.getRolename());
				/**
				 * By removing from the first list under the original rolename and inserting in the
				 * new results by the new rolename, we also address any change to the rolename that might
				 * occur as part of specialization.
				 */
				rp.update(pc, null);
			}
			else {
				/**
				 * It wasn't specializing anything, so create a new one
				 */
				rp = new RealizedParticipant(pc,null);
  			}
			results.put(pc.getRolename(), rp);

		}
		// Merge remaining previous results
		results.putAll(participantsSoFar);
		return results;
	}

	public Map<String, RealizedParticipant> getParticipant() {
		return participant;
	}

}
