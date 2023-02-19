/**
 * generated by Xtext 2.28.0
 * Copyright (c) 2022, 2023 Epistimis LLC (http://www.epistimis.com) and others.
 */
package com.epistimis.uddl.uddl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Conceptual Composition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.epistimis.uddl.uddl.ConceptualComposition#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see com.epistimis.uddl.uddl.UddlPackage#getConceptualComposition()
 * @model
 * @generated
 */
public interface ConceptualComposition extends ConceptualCharacteristic
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' reference.
   * @see #setType(ConceptualComposableElement)
   * @see com.epistimis.uddl.uddl.UddlPackage#getConceptualComposition_Type()
   * @model
   * @generated
   */
  ConceptualComposableElement getType();

  /**
   * Sets the value of the '{@link com.epistimis.uddl.uddl.ConceptualComposition#getType <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' reference.
   * @see #getType()
   * @generated
   */
  void setType(ConceptualComposableElement value);

} // ConceptualComposition
