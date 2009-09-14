/*
 * Created on Jul 31, 2003
 *
 * Eclipse GEF redbook sample application
 * $Source: /usr/local/cvsroot/SAL330RGEFDemoApplication/src/com/ibm/itso/sal330r/gefdemo/figures/ConditionPortRequestListener.java,v $
 * $Revision: 1.2 $
 * 
 * (c) Copyright IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 * 		ddean			Initial version
 */
package figures;

/**
 * A simple interface for notification from the figure
 * to the EditPart for creating a new port or deleting one
 * @author ddean
 *
 */
public interface ConditionPortRequestListener {
	public void addPortRequest();
	public void removePortRequest( String portName );
}
