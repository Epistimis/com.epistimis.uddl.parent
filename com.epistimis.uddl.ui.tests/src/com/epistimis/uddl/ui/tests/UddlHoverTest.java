/**
 * 
 */
package com.epistimis.uddl.ui.tests;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.ui.testing.AbstractHoverTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.epistimis.uddl.uddl.ConceptualDataModel;
import com.epistimis.uddl.uddl.DataModel;
import com.epistimis.uddl.uddl.LogicalDataModel;
import com.epistimis.uddl.uddl.PlatformDataModel;
import com.epistimis.uddl.ui.hover.UddlKeywordHoverText;
import com.google.inject.Inject;

/**
 * @author HA PHUONG
 *
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(UddlUiInjectorProvider.class)
public class UddlHoverTest extends AbstractHoverTest {
	@Inject
	ParseHelper<DataModel> parseHelper;

	String dmKeyword = "dm";
	String cdmKeyword = "cdm";
	String ldmKeyword = "ldm";
	String pdmKeyword = "pdm";

	String dmName = "TestNameDm";
	String cdmName = "TestNameCdm";
	String ldmName = "TestNameLdm";
	String pdmName = "TestNamePdm";

	String dmDescription = "Test description for DataModel";
	String cdmDescription = "Test description for ConceptualDataModel";
	String ldmDescription = "Test description for LogicDataModel";
	String pdmDescription = "Test description for PlatformDataModel";

	@Test
	public void hoverOverDataModel() throws Exception {
		String model = String.format("%s %s \"%s\" { }", dmKeyword, dmName, dmDescription);
		DataModel parseDm = parseHelper.parse(model);
		hasHoverOver(model, dmKeyword, UddlKeywordHoverText.dmKeywordHover);
		hasHoverOver(model, dmName, parseDm.getName());
		hasHoverOver(model, dmName, parseDm.getDescription());
	}

	@Test
	public void hoverOverConceptualDataModel() throws Exception {
		String model = String.format("%s %s \"%s\" { " + "%s %s \"%s\" { " + " } " + "}", dmKeyword, dmName,
				dmDescription, cdmKeyword, cdmName, cdmDescription);
		ConceptualDataModel parseCdm = parseHelper.parse(model).getCdm().get(0);
		hasHoverOver(model, cdmKeyword, UddlKeywordHoverText.cdmKeywordHover);
		hasHoverOver(model, cdmName, parseCdm.getName());
		hasHoverOver(model, cdmName, parseCdm.getDescription());
	}

	@Test
	public void hoverOverLogicDataModel() throws Exception {
		String model = String.format("%s %s \"%s\" { " + "%s %s \"%s\" { " + " } " + "}", dmKeyword, dmName,
				dmDescription, ldmKeyword, ldmName, ldmDescription);
		LogicalDataModel parseLdm = parseHelper.parse(model).getLdm().get(0);
		hasHoverOver(model, ldmKeyword, UddlKeywordHoverText.ldmKeywordHover);
		hasHoverOver(model, ldmName, parseLdm.getName());
		hasHoverOver(model, ldmName, parseLdm.getDescription());
	}

	@Test
	public void hoverOverPlatformDataModel() throws Exception {
		String model = String.format("%s %s \"%s\" { " + "%s %s \"%s\" { " + " } " + "}", dmKeyword, dmName,
				dmDescription, pdmKeyword, pdmName, pdmDescription);
		PlatformDataModel parseLdm = parseHelper.parse(model).getPdm().get(0);
		hasHoverOver(model, pdmKeyword, UddlKeywordHoverText.pdmKeywordHover);
		hasHoverOver(model, pdmName, parseLdm.getName());
		hasHoverOver(model, pdmName, parseLdm.getDescription());
	}

}
