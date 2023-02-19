/**
 * generated by Xtext 2.28.0
 * Copyright (c) 2022, 2023 Epistimis LLC (http://www.epistimis.com) and others.
 */
package com.epistimis.uddl.uddl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Logical Query Composition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.epistimis.uddl.uddl.LogicalQueryComposition#getType <em>Type</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.LogicalQueryComposition#getRolename <em>Rolename</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.LogicalQueryComposition#getRealizes <em>Realizes</em>}</li>
 * </ul>
 *
 * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalQueryComposition()
 * @model
 * @generated
 */
public interface LogicalQueryComposition extends EObject
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' reference.
   * @see #setType(LogicalView)
   * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalQueryComposition_Type()
   * @model
   * @generated
   */
  LogicalView getType();

  /**
   * Sets the value of the '{@link com.epistimis.uddl.uddl.LogicalQueryComposition#getType <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' reference.
   * @see #getType()
   * @generated
   */
  void setType(LogicalView value);

  /**
   * Returns the value of the '<em><b>Rolename</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rolename</em>' attribute.
   * @see #setRolename(String)
   * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalQueryComposition_Rolename()
   * @model
   * @generated
   */
  String getRolename();

  /**
   * Sets the value of the '{@link com.epistimis.uddl.uddl.LogicalQueryComposition#getRolename <em>Rolename</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Rolename</em>' attribute.
   * @see #getRolename()
   * @generated
   */
  void setRolename(String value);

  /**
   * Returns the value of the '<em><b>Realizes</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Realizes</em>' reference.
   * @see #setRealizes(ConceptualQueryComposition)
   * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalQueryComposition_Realizes()
   * @model
   * @generated
   */
  ConceptualQueryComposition getRealizes();

  /**
   * Sets the value of the '{@link com.epistimis.uddl.uddl.LogicalQueryComposition#getRealizes <em>Realizes</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Realizes</em>' reference.
   * @see #getRealizes()
   * @generated
   */
  void setRealizes(ConceptualQueryComposition value);

} // LogicalQueryComposition
