/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.irit.upnp;

import java.awt.Frame;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;

import org.fourthline.cling.binding.annotations.*;

/**
 *
 * @author IDA
 */

@UpnpService(
        serviceId = @UpnpServiceId("CompteurS"),
        serviceType = @UpnpServiceType(value = "CompteurS", version = 1)
)

public class Compteur {
    
    
    private final PropertyChangeSupport propertyChangeSupport;

    public Compteur() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }
    
    
    /**
     * Variable D'Etat, non �venemenc�e
     * Permet d'envoyer le message de l'�tat dans lequel la lampe doit �tre
     */
    @UpnpStateVariable(defaultValue = "0", sendEvents = false)
    private int target = 0;

    /**
     * Variable d'etat �venemmenc�e
     * Permet de v�rifier si la lampe est bien dans le bon �tat.
     */
    @UpnpStateVariable(defaultValue = "0")
    private int status = 0;

    /**
     * Add 1 to the counter
     */

    @UpnpAction
    public void add() {
        int targetOldValue = target;
        target ++;
        int statusOldValue = status;
        status = target;

        // These have no effect on the UPnP monitoring but it's JavaBean compliant
        getPropertyChangeSupport().firePropertyChange("target", targetOldValue, target);
        getPropertyChangeSupport().firePropertyChange("status", statusOldValue, status);

        // This will send a UPnP event, it's the name of a state variable that sends events
        getPropertyChangeSupport().firePropertyChange("Status", statusOldValue, status);
        
    }

    /**
     * Get Status of the lamp
     * Methode Upnp grace au syst�me d'annotation
     * @return boolean
     */
    @UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
    public int getStatus() {
        // Pour ajouter des informations suppl�mentaires UPnP en cas d'erreur :
        // throw new ActionException(ErrorCode.ACTION_NOT_AUTHORIZED);
        return status;
    }
    
 
    
    /**
     * Print the version of the code
     * Ceci n'est pas une methode UPnP
     */
    public void printVersion(){
        System.out.println("Version : 1.0");
    }

}
