package group14;

import static org.junit.Assert.*;

import java.util.EventObject;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.sceneManagement.ISceneChangeListener;
import org.mt4j.sceneManagement.SceneChangeEvent;

import ca.mcgill.sel.commons.emf.util.AdapterFactoryRegistry;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Message;
import ca.mcgill.sel.ram.MessageSort;
import ca.mcgill.sel.ram.MessageView;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.impl.LifelineImpl;
import ca.mcgill.sel.ram.provider.RamItemProviderAdapterFactory;
import ca.mcgill.sel.ram.ui.views.message.handler.impl.MessageHandler;
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.scenes.DisplayAspectScene;
import ca.mcgill.sel.ram.ui.views.message.MessageCallView;
import net.jodah.concurrentunit.Waiter;

public class TestMessageHandler {
    private static Waiter waiter = new Waiter();
    MessageHandler handler = new MessageHandler();
    static Aspect aspect;
    MessageView mesView;
    Message message;
    MessageCallView messageCallView;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Initialize ResourceManager.
        ResourceManager.initialize();
        // Initialize packages.
        RamPackage.eINSTANCE.eClass();

        // Register resource factories.
        ResourceManager.registerExtensionFactory("ram", new RamResourceFactoryImpl());

        // Initialize adapter factories.
        AdapterFactoryRegistry.INSTANCE.addAdapterFactory(RamItemProviderAdapterFactory.class);

        RamApp.initialize(new Runnable() {

            @Override
            public void run() {
                waiter.resume();
            }
        });

        // Wait for RamApp to be initialized.
        waiter.await();
    }

    /**
     * Assumes that each test case uses the same test model.
     * Closes and reloads the aspect scene.
     */
    @Before
    public void setUp() throws Exception {
        // Close current aspect.
        if (aspect != null) {
            RamApp.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    RamApp.getApplication().closeAspectScene(RamApp.getActiveAspectScene());                    
                }
            });
        }

        // Load model to use in test.
        aspect = (Aspect) ResourceManager.loadModel("models/test/test.ram");

        RamApp.getApplication().addSceneChangeListener(new ISceneChangeListener() {

            @Override
            public void processSceneChangeEvent(SceneChangeEvent event) {
                // Resume once the new aspect scene is loaded (switched to).
                if (event.getNewScene() instanceof DisplayAspectScene) {
                    RamApp.getApplication().removeSceneChangeListener(this);
                    waiter.resume();
                }
            }
        });

        RamApp.getApplication().loadAspect(aspect);

        // Wait for UI to be updated.
        waiter.await();
    }


    @After
    public void tearDown() throws Exception {}
    
   
   @Test
   public void test() {
       mesView = (MessageView) aspect.getMessageViews().get(0);
       message = mesView.getSpecification().getMessages().get(0);
       startMethod();
       System.out.println(message);
       // initialize a tap=and-hold event
       TapAndHoldEvent tapAndHoldEvent = new TapAndHoldEvent(null, 0,
               messageCallView, null, true, null, 0, 0, 0);
       // testing if the tap-and-hold event has been set properly to false
       assertEquals(true, tapAndHoldEvent.isHoldComplete());
       // if tap-and-hold is false, processTapAndHoldEvent method will return true
       assertEquals(true, handler.processTapAndHoldEvent(tapAndHoldEvent));
   }

   @Test
    public void testprocessTapAndHoldEventWithFalseIsHoldComplete() {
      // startMethod();
        // initialize a tap=and-hold event
        TapAndHoldEvent tapAndHoldEvent = new TapAndHoldEvent(null, 0,
                null, null, false, null, 0, 0, 0);
        // testing if the tap-and-hold event has been set properly to false
        assertEquals(false, tapAndHoldEvent.isHoldComplete());
        // if tap-and-hold is false, processTapAndHoldEvent method will return true
        assertEquals(true, handler.processTapAndHoldEvent(tapAndHoldEvent));
    }
   
   
   
   
   // this will be called at the beginning of each test method
   private void startMethod(){
       // Close current aspect.
       if (aspect != null) {
           RamApp.getApplication().invokeLater(new Runnable() {
               @Override
               public void run() {
                   RamApp.getApplication().closeAspectScene(RamApp.getActiveAspectScene());                    
               }
           });
       }

       // Load model to use in test.
       aspect = (Aspect) ResourceManager.loadModel("models/test/test.ram");

       RamApp.getApplication().addSceneChangeListener(new ISceneChangeListener() {

           @Override
           public void processSceneChangeEvent(SceneChangeEvent event) {
               // Resume once the new aspect scene is loaded (switched to).
               if (event.getNewScene() instanceof DisplayAspectScene) {
                   RamApp.getApplication().removeSceneChangeListener(this);
                   messageCallView = new MessageCallView(message, 
                           new RamRectangleComponent(0,0,0,0),
                           new RamRectangleComponent(0,0,64,64));
                   waiter.resume();
               }
           }
       });

       RamApp.getApplication().loadAspect(aspect);

       // Wait for UI to be updated.
       try {
        waiter.await();
    } catch (TimeoutException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
   }

}




