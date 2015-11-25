package ca.mcgill.sel.ram.ui.views.message.handler.impl;

import static org.junit.Assert.*;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.sceneManagement.ISceneChangeListener;
import org.mt4j.sceneManagement.SceneChangeEvent;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.commons.emf.util.AdapterFactoryRegistry;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Gate;
import ca.mcgill.sel.ram.Message;
import ca.mcgill.sel.ram.MessageOccurrenceSpecification;
import ca.mcgill.sel.ram.MessageSort;
import ca.mcgill.sel.ram.MessageView;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.impl.ContainerMapImpl;
import ca.mcgill.sel.ram.provider.RamItemProviderAdapterFactory;
import ca.mcgill.sel.ram.ui.views.message.handler.impl.MessageHandler;
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.scenes.DisplayAspectScene;
import ca.mcgill.sel.ram.ui.views.message.MessageCallView;
import ca.mcgill.sel.ram.ui.views.message.MessageViewView;
import net.jodah.concurrentunit.Waiter;

public class TestMessageHandler {
    private static Waiter waiter = new Waiter();
    static MessageHandler handler;
    static Aspect aspect;
    static MessageView mesView;
    static Message[] message;
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
        
     // Load model to use in test.
        aspect = (Aspect) ResourceManager.loadModel("models/test/testing.ram");
        

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
        
        // initialize global variables to use in tests
        mesView = (MessageView) aspect.getMessageViews().get(0);
        message = new Message[mesView.getSpecification().getMessages().size()];
        for (int i = 0; i < message.length; i++)
            message[i] = mesView.getSpecification().getMessages().get(i);
         

        RamApp.getApplication().invokeLater(new Runnable() {

            @Override
            public void run() {
                handler = new MessageHandler();
                      
                 RamApp.getActiveAspectScene().showMessageView(mesView);
                 waiter.resume();
                 
                waiter.resume();
            }
        });
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
                    //RamApp.getActiveAspectScene().showMessageView(mesView);
                    waiter.resume();
                }
            });
            waiter.await();
        }
        
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testprocessTapAndHoldEvent_FalseHoldComplete() {
        // initialize a tap=and-hold event
        TapAndHoldEvent tapAndHoldEvent = new TapAndHoldEvent(null, 0,
                null, null, false, null, 0, 0, 0);
        // testing if the tap-and-hold event has been set properly to false
        assertEquals(false, tapAndHoldEvent.isHoldComplete());
        // if tap-and-hold is false, processTapAndHoldEvent method will return true
        assertEquals(true, handler.processTapAndHoldEvent(tapAndHoldEvent));
    }

    @Test
    public void test_FromGateToOccurence_MessageSort_SYNCH() {
        Message mes = message[0];
        UpdatingtheMessageCallView(mes);
        // initialize a tap=and-hold event
        TapAndHoldEvent tapAndHoldEvent = new TapAndHoldEvent(null, 0,
                messageCallView, null, true, null, 500, 500, 1);
        // testing if the tap-and-hold event has been set properly to true
        assertEquals(true, tapAndHoldEvent.isHoldComplete());
        // In the Message view, this message is supposed to have as operation makeLotsOfMoney
        assertEquals("makeLotsOfMoney", mes.getSignature().getName().toString());
        // assert to test if the message send event is really a Gate message
        assertEquals(true, mes.getSendEvent() instanceof Gate);
        // the received event must be messageOccurenceSoecification
        assertEquals(true, mes.getReceiveEvent() instanceof MessageOccurrenceSpecification);
        // assert to check if the messagesort is really a synchcall messagesort
        assertEquals(true, mes.getMessageSort().equals(MessageSort.SYNCH_CALL));
        // the sent event of message is Instance of Gate
        // shouldProcessTapAndHold will return false
        // then the processTapAndHoldEvent will return true
        assertEquals(true, handler.processTapAndHoldEvent(tapAndHoldEvent));
    }

    @Test
    public void test_FromOccurenceToGate_MessageSort_Reply() {
        // initializing the messageview and use it to retreive the mCallViewessage
        Message mes = message[1];
        UpdatingtheMessageCallView(mes);
        // initialize a tap=and-hold event
        TapAndHoldEvent tapAndHoldEvent = new TapAndHoldEvent(null, 0,
                messageCallView, null, true, null, 500, 500, 1);
        // testing if the tap-and-hold event has been set properly to true
        assertEquals(true, tapAndHoldEvent.isHoldComplete());
        // In the Message view, this message is supposed to have as operation makeLotsOfMoney
        assertEquals("makeLotsOfMoney", mes.getSignature().getName().toString());
        // assert to test if the message send is from occurence
        assertEquals(true, mes.getSendEvent() instanceof MessageOccurrenceSpecification);
        // to Gate
        assertEquals(true, mes.getReceiveEvent() instanceof Gate);
        // assert to check if the messagesort is really a synchcall messagesort
        assertEquals(true, mes.getMessageSort().equals(MessageSort.REPLY));
        // the sent event of message is Instance of Gate
        // shouldProcessTapAndHold will return false
        // then the processTapAndHoldEvent will return true
        assertEquals(true, handler.processTapAndHoldEvent(tapAndHoldEvent));
    }

    @Test
    public void test_FromOccurenceToOccurrence_MessageSort_Create() {
        // initializing the messageview and use it to retreive the mCallViewessage
        final Message mes = message[2];
        UpdatingtheMessageCallView(mes);
        // initialize a tap=and-hold event
        final TapAndHoldEvent tapAndHoldEvent = new TapAndHoldEvent(null, 0,
                messageCallView, null, true, new Vector3D(185, 185), 500, 500, 1);
        // testing if the tap-and-hold event has been set properly to true
        assertEquals(true, tapAndHoldEvent.isHoldComplete());
        // In the Message view, this message is supposed to have as operation makeLostOfMoney
        assertEquals("create", mes.getSignature().getName().toString());
        // from occurence
        assertEquals(true, mes.getSendEvent() instanceof MessageOccurrenceSpecification);
        // to occurence
        assertEquals(true, mes.getReceiveEvent() instanceof MessageOccurrenceSpecification);
        // assert to check if the messagesort is really a synchcall messagesort
        assertEquals(true, mes.getMessageSort().equals(MessageSort.CREATE_MESSAGE));
        // the sent event of message is Instance of Gate
        // shouldProcessTapAndHold will return false
        // then the processTapAndHoldEvent will return true
        // boolean handle = handler.processTapAndHoldEvent(tapAndHoldEvent);
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                //handler = new MessageHandler();
                
                RamApp app = RamApp.getApplication();
                app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_PRESSED, 0,
                        InputEvent.BUTTON1_MASK, 150, 170, 310, 205, 1, false, MouseEvent.BUTTON1));
                app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_RELEASED, 0,
                        InputEvent.BUTTON1_MASK, 150, 170, 310, 205, 1, false, MouseEvent.BUTTON1));
                waiter.resume();
            }
        });
        try {
            waiter.await();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       

    }

    @Test
    public void test_FromOccurenceToOccurrence_MessageSort_Synchall_Deposit() {
        // initializing the messageview and use it to retreive the mCallViewessage
        final Message mes = message[3];
        UpdatingtheMessageCallView(mes);
        
        // initialize a tap=and-hold event
        final TapAndHoldEvent tapAndHoldEvent = new TapAndHoldEvent(null, 0,
                messageCallView, null, true, new Vector3D(165, 230), 500, 500, 1);
        // testing if the tap-and-hold event has been set properly to true
        assertEquals(true, tapAndHoldEvent.isHoldComplete());
        // In the Message view, this message is supposed to have as operation makeLostOfMoney
        assertEquals("deposit", mes.getSignature().getName().toString());
        // from occurence
        assertEquals(true, mes.getSendEvent() instanceof MessageOccurrenceSpecification);
        // to occurence
        assertEquals(true, mes.getReceiveEvent() instanceof MessageOccurrenceSpecification);
        // assert to check if the messagesort is really a synchcall messagesort
        assertEquals(true, mes.getMessageSort().equals(MessageSort.SYNCH_CALL));
        // the sent event of message is Instance of Gate
        // shouldProcessTapAndHold will return false
        // then the processTapAndHoldEvent will return true
        // boolean handle = handler.processTapAndHoldEvent(tapAndHoldEvent);
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                RamApp app = RamApp.getApplication();
                app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_PRESSED, 0,
                        InputEvent.BUTTON1_MASK, 152, 222, 296, 253, 1, false, MouseEvent.BUTTON1));
                app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_RELEASED, 0,
                        InputEvent.BUTTON1_MASK, 152, 170, 296, 253, 1, false, MouseEvent.BUTTON1));
                //MessageView messageView = (MessageView) aspect.getMessageViews().get(0);
               // ContainerMapImpl layout = EMFModelUtil.getEntryFromMap(aspect.getLayout().getContainers(), mesView);

                //MessageViewView messageViewView = new MessageViewView(mesView, layout, 1024, 768);
                //messageViewView.
                app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_PRESSED, 0,
                        InputEvent.BUTTON1_MASK, 152, 222, 296, 253, 1, false, MouseEvent.BUTTON1));
                boolean handle = handler.processTapAndHoldEvent(tapAndHoldEvent);
                waiter.resume();
                assertEquals(true, handle);
            }
        });
        // Wait for UI to be updated.
        try {
            waiter.await();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
 
    @Test
    public void test_FromOccurenceToOccurrence_MessageSort_Synchall_Withdraw() {
        // initializing the messageview and use it to retreive the mCallViewessage
        final Message mes = message[4];
        UpdatingtheMessageCallView(mes);
        
        // initialize a tap=and-hold event
        final TapAndHoldEvent tapAndHoldEvent = new TapAndHoldEvent(null, 0,
                messageCallView, null, true, new Vector3D(170, 275), 500, 500, 1);
        // testing if the tap-and-hold event has been set properly to true
        assertEquals(true, tapAndHoldEvent.isHoldComplete());
        // In the Message view, this message is supposed to have as operation makeLostOfMoney
        assertEquals("withdraw", mes.getSignature().getName().toString());
        // from occurence
        assertEquals(true, mes.getSendEvent() instanceof MessageOccurrenceSpecification);
        // to occurence
        assertEquals(true, mes.getReceiveEvent() instanceof MessageOccurrenceSpecification);
        // assert to check if the messagesort is really a synchcall messagesort
        assertEquals(true, mes.getMessageSort().equals(MessageSort.SYNCH_CALL));
        // the sent event of message is Instance of Gate
        // shouldProcessTapAndHold will return false
        // then the processTapAndHoldEvent will return true
        // boolean handle = handler.processTapAndHoldEvent(tapAndHoldEvent);
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                //handler = new MessageHandler();
                RamApp app = RamApp.getApplication();
                app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_PRESSED, 0,
                        InputEvent.BUTTON1_MASK, 152, 262, 280, 293, 1, false, MouseEvent.BUTTON1));
                app.dispatchEvent(new MouseEvent(app, MouseEvent.MOUSE_RELEASED, 0,
                        InputEvent.BUTTON1_MASK, 152, 260, 280, 293, 1, false, MouseEvent.BUTTON1));
                boolean handle = handler.processTapAndHoldEvent(tapAndHoldEvent);
                waiter.resume();
                assertEquals(true, handle);
            }
        });
        // Wait for UI to be updated.
        try {
            waiter.await();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

     

    // this will be called at the beginning of each test method
    private void UpdatingtheMessageCallView(final Message mess) {
        // Close current aspect.
        if (aspect != null) {
            RamApp.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    messageCallView = new MessageCallView(mess,
                            new RamRectangleComponent(0, 0, 0, 0),
                            new RamRectangleComponent(0, 0, 700, 700));
                    waiter.resume();
                }
            });
        }
        // Wait for UI to be updated.
        try {
            waiter.await();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
