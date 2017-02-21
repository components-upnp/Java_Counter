package com.irit.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

import java.io.IOException;

/**
 * Created by IDA on 01/02/2017.
 */
public class CompteurServer implements Runnable {

    /**
     * Main
     * Copy code if you need to add a Upnp service on your device
     * @param args
     * @throws Exception
     */

    private CompteurFrame frameC;

    public static void main(String[] args) throws Exception {
        // Start a user thread that runs the UPnP stack
        Thread serverThread = new Thread(new CompteurServer());
        serverThread.setDaemon(false);
        serverThread.start();
    }

    /**
     * Run the UPnP service
     */
    public void run() {
        try {

            final UpnpService upnpService = new UpnpServiceImpl();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    upnpService.shutdown();
                }
            });

            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(
                    createDevice()
            );
            
        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    /**
     * Permet de cr�er un device
     * Il est possible de cr�er plusieurs service pour un m�me device, dans ce cas confer commentaires en fin de methode.
     * @return LocalDevice
     * @throws ValidationException
     * @throws LocalServiceBindingException
     * @throws IOException
     */
    public LocalDevice createDevice()
            throws ValidationException, LocalServiceBindingException, IOException {

        /**
         * Description du Device
         */
        DeviceIdentity identity =
                new DeviceIdentity(
                        UDN.uniqueSystemIdentifier("Compteur")
                );

        DeviceType type =
                new UDADeviceType("TypeCompteur", 1);

        DeviceDetails details =
                new DeviceDetails(
                        "Friendly Compteur",					// Friendly Name
                        new ManufacturerDetails(
                                "CreaTech",								// Manufacturer
                                ""),								// Manufacturer URL
                        new ModelDetails(
                                "CompteurTest",						// Model Name
                                "un simple compteur.",	// Model Description
                                "v1" 								// Model Number
                        )
                );


        // Ajout du service
        
        
        LocalService<Compteur> compteurService =
                new AnnotationLocalServiceBinder().read(Compteur.class);

        compteurService.setManager(
                new DefaultServiceManager(compteurService, Compteur.class)
        );

        
       new CompteurFrame(compteurService).setVisible(true);


        // retour en cas de 1 service
        return new LocalDevice(identity, type, details, compteurService);


		/* Si jamais plusieurs services pour un device (adapter code)
	    return new LocalDevice(
	            identity, type, details,
	            new LocalService[] {switchPowerService, myOtherService}
	    );
	    */
    }


}
