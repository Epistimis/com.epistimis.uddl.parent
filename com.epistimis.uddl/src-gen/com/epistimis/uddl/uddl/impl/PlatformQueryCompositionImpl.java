/**
 * generated by Xtext 2.28.0
 * Copyright (c) 2022, 2023 Epistimis LLC (http://www.epistimis.com) and others.
 */
package com.epistimis.uddl.uddl.impl;

import com.epistimis.uddl.uddl.LogicalQueryComposition;
import com.epistimis.uddl.uddl.PlatformQueryComposition;
import com.epistimis.uddl.uddl.PlatformView;
import com.epistimis.uddl.uddl.UddlPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Platform Query Composition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.epistimis.uddl.uddl.impl.PlatformQueryCompositionImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.impl.PlatformQueryCompositionImpl#getRolename <em>Rolename</em>}</li>
 *   <li>{@link com.epistimis.uddl.uddl.impl.PlatformQueryCompositionImpl#getRealizes <em>Realizes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PlatformQueryCompositionImpl extends MinimalEObjectImpl.Container implements PlatformQueryComposition
{
  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected PlatformView type;

  /**
   * The default value of the '{@link #getRolename() <em>Rolename</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRolename()
   * @generated
   * @ordered
   */
  protected static final String ROLENAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRolename() <em>Rolename</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRolename()
   * @generated
   * @ordered
   */
  protected String rolename = ROLENAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getRealizes() <em>Realizes</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRealizes()
   * @generated
   * @ordered
   */
  protected LogicalQueryComposition realizes;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PlatformQueryCompositionImpl()
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
    return UddlPackage.Literals.PLATFORM_QUERY_COMPOSITION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PlatformView getType()
  {
    if (type != null && type.eIsProxy())
    {
      InternalEObject oldType = (InternalEObject)type;
      type = (PlatformView)eResolveProxy(oldType);
      if (type != oldType)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, UddlPackage.PLATFORM_QUERY_COMPOSITION__TYPE, oldType, type));
      }
    }
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PlatformView basicGetType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setType(PlatformView newType)
  {
    PlatformView oldType = type;
    type = newType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, UddlPackage.PLATFORM_QUERY_COMPOSITION__TYPE, oldType, type));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getRolename()
  {
    return rolename;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRolename(String newRolename)
  {
    String oldRolename = rolename;
    rolename = newRolename;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, UddlPackage.PLATFORM_QUERY_COMPOSITION__ROLENAME, oldRolename, rolename));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public LogicalQueryComposition getRealizes()
  {
    if (realizes != null && realizes.eIsProxy())
    {
      InternalEObject oldRealizes = (InternalEObject)realizes;
      realizes = (LogicalQueryComposition)eResolveProxy(oldRealizes);
      if (realizes != oldRealizes)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, UddlPackage.PLATFORM_QUERY_COMPOSITION__REALIZES, oldRealizes, realizes));
      }
    }
    return realizes;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LogicalQueryComposition basicGetRealizes()
  {
    return realizes;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRealizes(LogicalQueryComposition newRealizes)
  {
    LogicalQueryComposition oldRealizes = realizes;
    realizes = newRealizes;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, UddlPackage.PLATFORM_QUERY_COMPOSITION__REALIZES, oldRealizes, realizes));
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
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__TYPE:
        if (resolve) return getType();
        return basicGetType();
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__ROLENAME:
        return getRolename();
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__REALIZES:
        if (resolve) return getRealizes();
        return basicGetRealizes();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__TYPE:
        setType((PlatformView)newValue);
        return;
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__ROLENAME:
        setRolename((String)newValue);
        return;
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__REALIZES:
        setRealizes((LogicalQueryComposition)newValue);
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
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__TYPE:
        setType((PlatformView)null);
        return;
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__ROLENAME:
        setRolename(ROLENAME_EDEFAULT);
        return;
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__REALIZES:
        setRealizes((LogicalQueryComposition)null);
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
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__TYPE:
        return type != null;
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__ROLENAME:
        return ROLENAME_EDEFAULT == null ? rolename != null : !ROLENAME_EDEFAULT.equals(rolename);
      case UddlPackage.PLATFORM_QUERY_COMPOSITION__REALIZES:
        return realizes != null;
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
    result.append(" (rolename: ");
    result.append(rolename);
    result.append(')');
    return result.toString();
  }

} //PlatformQueryCompositionImpl
