/**
 * generated by Xtext 2.28.0
 * Copyright (c) 2022, 2023 Epistimis LLC (http://www.epistimis.com) and others.
 */
package com.epistimis.uddl.uddl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Logical Measurement System</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.epistimis.uddl.uddl.LogicalMeasurementSystem#getMeasurementSystemAxis <em>Measurement System Axis</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.LogicalMeasurementSystem#getCoordinateSystem <em>Coordinate System</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.LogicalMeasurementSystem#getExternalStandardReference <em>External Standard Reference</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.LogicalMeasurementSystem#getOrientation <em>Orientation</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.LogicalMeasurementSystem#getReferencePoint <em>Reference Point</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.LogicalMeasurementSystem#getConstraint <em>Constraint</em>}</li>
 * </ul>
 *
 * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalMeasurementSystem()
 * @model
 * @generated
 */
public interface LogicalMeasurementSystem extends LogicalAbstractMeasurementSystem
{
  /**
   * Returns the value of the '<em><b>Measurement System Axis</b></em>' reference list.
   * The list contents are of type {@link com.epistimis.uddl.uddl.LogicalMeasurementSystemAxis}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Measurement System Axis</em>' reference list.
   * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalMeasurementSystem_MeasurementSystemAxis()
   * @model
   * @generated
   */
  EList<LogicalMeasurementSystemAxis> getMeasurementSystemAxis();

  /**
   * Returns the value of the '<em><b>Coordinate System</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Coordinate System</em>' reference.
   * @see #setCoordinateSystem(LogicalCoordinateSystem)
   * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalMeasurementSystem_CoordinateSystem()
   * @model
   * @generated
   */
  LogicalCoordinateSystem getCoordinateSystem();

  /**
   * Sets the value of the '{@link com.epistimis.uddl.uddl.LogicalMeasurementSystem#getCoordinateSystem <em>Coordinate System</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Coordinate System</em>' reference.
   * @see #getCoordinateSystem()
   * @generated
   */
  void setCoordinateSystem(LogicalCoordinateSystem value);

  /**
   * Returns the value of the '<em><b>External Standard Reference</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>External Standard Reference</em>' attribute.
   * @see #setExternalStandardReference(String)
   * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalMeasurementSystem_ExternalStandardReference()
   * @model
   * @generated
   */
  String getExternalStandardReference();

  /**
   * Sets the value of the '{@link com.epistimis.uddl.uddl.LogicalMeasurementSystem#getExternalStandardReference <em>External Standard Reference</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>External Standard Reference</em>' attribute.
   * @see #getExternalStandardReference()
   * @generated
   */
  void setExternalStandardReference(String value);

  /**
   * Returns the value of the '<em><b>Orientation</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Orientation</em>' attribute.
   * @see #setOrientation(String)
   * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalMeasurementSystem_Orientation()
   * @model
   * @generated
   */
  String getOrientation();

  /**
   * Sets the value of the '{@link com.epistimis.uddl.uddl.LogicalMeasurementSystem#getOrientation <em>Orientation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Orientation</em>' attribute.
   * @see #getOrientation()
   * @generated
   */
  void setOrientation(String value);

  /**
   * Returns the value of the '<em><b>Reference Point</b></em>' containment reference list.
   * The list contents are of type {@link com.epistimis.uddl.uddl.LogicalReferencePoint}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Reference Point</em>' containment reference list.
   * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalMeasurementSystem_ReferencePoint()
   * @model containment="true"
   * @generated
   */
  EList<LogicalReferencePoint> getReferencePoint();

  /**
   * Returns the value of the '<em><b>Constraint</b></em>' containment reference list.
   * The list contents are of type {@link com.epistimis.uddl.uddl.LogicalMeasurementConstraint}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Constraint</em>' containment reference list.
   * @see com.epistimis.uddl.uddl.UddlPackage#getLogicalMeasurementSystem_Constraint()
   * @model containment="true"
   * @generated
   */
  EList<LogicalMeasurementConstraint> getConstraint();

} // LogicalMeasurementSystem
