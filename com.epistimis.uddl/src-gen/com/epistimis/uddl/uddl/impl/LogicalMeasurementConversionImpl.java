/**
 * generated by Xtext 2.28.0
 * Copyright (c) 2022, 2023 Epistimis LLC (http://www.epistimis.com) and others.
 */
package com.epistimis.uddl.uddl.impl;

import com.epistimis.uddl.uddl.LogicalMeasurementConversion;
import com.epistimis.uddl.uddl.LogicalMeasurementSystem;
import com.epistimis.uddl.uddl.UddlPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Logical Measurement Conversion</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.epistimis.uddl.uddl.impl.LogicalMeasurementConversionImpl#getSource <em>Source</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.impl.LogicalMeasurementConversionImpl#getDestination <em>Destination</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.impl.LogicalMeasurementConversionImpl#getEquation <em>Equation</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.impl.LogicalMeasurementConversionImpl#getConversionLossDescription <em>Conversion Loss Description</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LogicalMeasurementConversionImpl extends LogicalElementImpl implements LogicalMeasurementConversion
{
  /**
   * The cached value of the '{@link #getSource() <em>Source</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSource()
   * @generated
   * @ordered
   */
  protected LogicalMeasurementSystem source;

  /**
   * The cached value of the '{@link #getDestination() <em>Destination</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDestination()
   * @generated
   * @ordered
   */
  protected LogicalMeasurementSystem destination;

  /**
   * The cached value of the '{@link #getEquation() <em>Equation</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEquation()
   * @generated
   * @ordered
   */
  protected EList<String> equation;

  /**
   * The default value of the '{@link #getConversionLossDescription() <em>Conversion Loss Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConversionLossDescription()
   * @generated
   * @ordered
   */
  protected static final String CONVERSION_LOSS_DESCRIPTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getConversionLossDescription() <em>Conversion Loss Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConversionLossDescription()
   * @generated
   * @ordered
   */
  protected String conversionLossDescription = CONVERSION_LOSS_DESCRIPTION_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LogicalMeasurementConversionImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return UddlPackage.Literals.LOGICAL_MEASUREMENT_CONVERSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public LogicalMeasurementSystem getSource()
  {
    if (source != null && source.eIsProxy())
    {
      InternalEObject oldSource = (InternalEObject)source;
      source = (LogicalMeasurementSystem)eResolveProxy(oldSource);
      if (source != oldSource)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__SOURCE, oldSource, source));
      }
    }
    return source;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LogicalMeasurementSystem basicGetSource()
  {
    return source;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSource(LogicalMeasurementSystem newSource)
  {
    LogicalMeasurementSystem oldSource = source;
    source = newSource;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__SOURCE, oldSource, source));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public LogicalMeasurementSystem getDestination()
  {
    if (destination != null && destination.eIsProxy())
    {
      InternalEObject oldDestination = (InternalEObject)destination;
      destination = (LogicalMeasurementSystem)eResolveProxy(oldDestination);
      if (destination != oldDestination)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__DESTINATION, oldDestination, destination));
      }
    }
    return destination;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LogicalMeasurementSystem basicGetDestination()
  {
    return destination;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setDestination(LogicalMeasurementSystem newDestination)
  {
    LogicalMeasurementSystem oldDestination = destination;
    destination = newDestination;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__DESTINATION, oldDestination, destination));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<String> getEquation()
  {
    if (equation == null)
    {
      equation = new EDataTypeEList<String>(String.class, this, UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__EQUATION);
    }
    return equation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getConversionLossDescription()
  {
    return conversionLossDescription;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setConversionLossDescription(String newConversionLossDescription)
  {
    String oldConversionLossDescription = conversionLossDescription;
    conversionLossDescription = newConversionLossDescription;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__CONVERSION_LOSS_DESCRIPTION, oldConversionLossDescription, conversionLossDescription));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__SOURCE:
        if (resolve) return getSource();
        return basicGetSource();
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__DESTINATION:
        if (resolve) return getDestination();
        return basicGetDestination();
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__EQUATION:
        return getEquation();
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__CONVERSION_LOSS_DESCRIPTION:
        return getConversionLossDescription();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__SOURCE:
        setSource((LogicalMeasurementSystem)newValue);
        return;
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__DESTINATION:
        setDestination((LogicalMeasurementSystem)newValue);
        return;
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__EQUATION:
        getEquation().clear();
        getEquation().addAll((Collection<? extends String>)newValue);
        return;
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__CONVERSION_LOSS_DESCRIPTION:
        setConversionLossDescription((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__SOURCE:
        setSource((LogicalMeasurementSystem)null);
        return;
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__DESTINATION:
        setDestination((LogicalMeasurementSystem)null);
        return;
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__EQUATION:
        getEquation().clear();
        return;
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__CONVERSION_LOSS_DESCRIPTION:
        setConversionLossDescription(CONVERSION_LOSS_DESCRIPTION_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__SOURCE:
        return source != null;
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__DESTINATION:
        return destination != null;
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__EQUATION:
        return equation != null && !equation.isEmpty();
      case UddlPackage.LOGICAL_MEASUREMENT_CONVERSION__CONVERSION_LOSS_DESCRIPTION:
        return CONVERSION_LOSS_DESCRIPTION_EDEFAULT == null ? conversionLossDescription != null : !CONVERSION_LOSS_DESCRIPTION_EDEFAULT.equals(conversionLossDescription);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (equation: ");
    result.append(equation);
    result.append(", conversionLossDescription: ");
    result.append(conversionLossDescription);
    result.append(')');
    return result.toString();
  }

} //LogicalMeasurementConversionImpl
