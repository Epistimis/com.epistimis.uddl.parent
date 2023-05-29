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
	public void hoverOverKeyword() {
		String model =
				"dm TestRule \"This is a test description\"		{\r\n"
				+ "	\r\n"
				+ "}";
		hasHoverOver(model, "dm", "A DataModel is a container for ConceptualDataModels, LogicalDataModels, and PlatformDataModels"
				);
	}

	@Test
	public void hoverOverRuleHeader() {
		String model =
				"dm TestRule \"This is a test description\"		{\r\n"
				+ "	\r\n"
				+ "}";
		hasHoverOver(model, "TestRule", "TestRule");
	}
	
	@Test
	public void hoverOverRuleDescription() {
		String model =
				"dm TestRule \"This is a test description\"		{\r\n"
				+ "	\r\n"
				+ "}";
		hasHoverOver(model, "TestRule", "This is a test description");
	}

}
