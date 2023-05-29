/**
 * 
 */
package com.epistimis.uddl.ui.tests;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.ui.testing.AbstractHoverTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author HA PHUONG
 *
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(UddlUiInjectorProvider.class)
public class UddlHoverTest extends AbstractHoverTest {
	@Test
	public void hoverOverDataModel() {
		String model =
				"dm TestRuleDm \"This is a test description\"		{\r\n"
				+ "	\r\n"
				+ "}";
		hasHoverOver(model, "dm", "A DataModel is a container for ConceptualDataModels, LogicalDataModels, and PlatformDataModels"
				);
		hasHoverOver(model, "TestRule", "TestRule");
		hasHoverOver(model, "TestRule", "This is a test description");
	}
	
	@Test
	public void hoverOverConceptualDataModel() {
		String model =
				"dm TestRuleDm \"This is a test description\"		{\r\n"
				+ "	cdm TestRuleCdm \"Test description Cdm\" {\r\n"
				+ "		\r\n"
				+ "	}\r\n"
				+ "}";
		hasHoverOver(model, "cdm", "A ConceptualDataModel is a container for CDM Elements (including nested CDMs)."
				);
		hasHoverOver(model, "TestRuleCdm", "TestRuleCdm");
		hasHoverOver(model, "TestRuleCdm", "Test description Cdm");
	}

}
