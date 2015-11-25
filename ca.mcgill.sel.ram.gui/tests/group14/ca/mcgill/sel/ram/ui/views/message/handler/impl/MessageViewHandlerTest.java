package ca.mcgill.sel.ram.ui.views.message.handler.impl;

import static org.junit.Assert.*;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;

import org.eclipse.emf.common.util.EList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTMouseInputEvt;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputSources.MouseInputSource;
import org.mt4j.sceneManagement.ISceneChangeListener;
import org.mt4j.sceneManagement.SceneChangeEvent;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import ca.mcgill.sel.commons.emf.util.AdapterFactoryRegistry;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.FragmentContainer;
import ca.mcgill.sel.ram.Lifeline;
import ca.mcgill.sel.ram.Message;
import ca.mcgill.sel.ram.MessageView;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.impl.ContainerMapImpl;
import ca.mcgill.sel.ram.provider.RamItemProviderAdapterFactory;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.scenes.DisplayAspectScene;
import ca.mcgill.sel.ram.ui.views.message.LifelineView;
import ca.mcgill.sel.ram.ui.views.message.MessageCallView;
import ca.mcgill.sel.ram.ui.views.message.MessageViewView;
import ca.mcgill.sel.ram.ui.views.message.handler.MessageViewHandlerFactory;
import ca.mcgill.sel.ram.ui.views.message.handler.impl.MessageViewHandler;
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;
import net.jodah.concurrentunit.Waiter;

public class MessageViewHandlerTest {

    private static Waiter waiter = new Waiter();
    private static Aspect aspect;
    private static Message message;
    private static MessageView messageView;
    private static MessageCallView messageCallView;
    private static MessageViewView messageViewView;
    private static MessageViewHandler messageViewHandler;
    private static UnistrokeEvent unistrokeEvent;
    private static MTPolygon mtPolygon;
    private static InputCursor testInputCursor;
    private static MouseInputSource mouseInputSource;
    private static MTMouseInputEvt mouseInputEvent1;
    private static MTMouseInputEvt mouseInputEvent2;
    private static int id;
    private static Vector3D startPosition;
    private static Vector3D endPosition;
    private static LifelineView from;
    private static LifelineView to;
    

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

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        
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
    }

    @After
    public void tearDown() throws Exception {
        ResourceManager.unloadResource(aspect.eResource());
    }

    @Test
    public void testProcessUnistrokeEvent1() throws TimeoutException {
        aspect = (Aspect) ResourceManager.loadModel("models/concern1/aspect1.ram");
        setUpAspect();
        
        //Start the cursor, create the events and fire them so that they get added to the cursor
        testInputCursor = new InputCursor();
        mouseInputSource = new MouseInputSource(RamApp.getApplication());
        
        mouseInputEvent1 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 159, 180, MouseEvent.MOUSE_PRESSED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent1.onFired();
        
        mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 300, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent2.onFired();
        
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                //Create the rest of the input and then test
                id = MTGestureEvent.GESTURE_STARTED;
                mtPolygon = new MTPolygon(RamApp.getApplication(), new Vertex[] {new Vertex()});
                unistrokeEvent = new UnistrokeEvent(null, id, messageViewView, mtPolygon, null, null);
                messageViewHandler = (MessageViewHandler) MessageViewHandlerFactory.INSTANCE.getMessageViewHandler();
                assertEquals(true, messageViewHandler.processUnistrokeEvent(unistrokeEvent));
                assertEquals(messageViewView, unistrokeEvent.getTarget());
                assertEquals(true, messageViewView.getUnistrokeLayer().containsChild(mtPolygon));
                assertEquals(id, unistrokeEvent.getId());
                waiter.resume();
            }
        });
        
        // Wait for UI to be updated.
        waiter.await();
    }
    
    @Test
    public void testProcessUnistrokeEvent2() throws TimeoutException {
        aspect = (Aspect) ResourceManager.loadModel("models/concern1/aspect1.ram");
        setUpAspect();
        
        testInputCursor = new InputCursor();
        mouseInputSource = new MouseInputSource(RamApp.getApplication());
        
        mouseInputEvent1 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 159, 180, MouseEvent.MOUSE_PRESSED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent1.onFired();
        
        mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 160, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent2.onFired();
                
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                id = MTGestureEvent.GESTURE_UPDATED;
                mtPolygon = new MTPolygon(RamApp.getApplication(), new Vertex[] {new Vertex()});
                unistrokeEvent = new UnistrokeEvent(null, id, messageViewView, mtPolygon, null, testInputCursor);
                messageViewHandler = (MessageViewHandler) MessageViewHandlerFactory.INSTANCE.getMessageViewHandler();
                assertEquals(true, messageViewHandler.processUnistrokeEvent(unistrokeEvent));
                assertEquals(messageViewView, unistrokeEvent.getTarget());
                assertEquals(id, unistrokeEvent.getId());
                waiter.resume();
            }
        });
        
        // Wait for UI to be updated.
        waiter.await();
    }
    
    @Test
    public void testProcessUnistrokeEvent3() throws TimeoutException {
        aspect = (Aspect) ResourceManager.loadModel("models/concern1/aspect1.ram");
        setUpAspect();
 
        testInputCursor = new InputCursor();
        mouseInputSource = new MouseInputSource(RamApp.getApplication());
        
        mouseInputEvent1 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 159, 180, MouseEvent.MOUSE_PRESSED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent1.onFired();
        
        mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 160, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent2.onFired();
        
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                id = 3;
                mtPolygon = new MTPolygon(RamApp.getApplication(), new Vertex[] {new Vertex()});
                unistrokeEvent = new UnistrokeEvent(null, id, messageViewView, mtPolygon, null, testInputCursor);
                messageViewHandler = (MessageViewHandler) MessageViewHandlerFactory.INSTANCE.getMessageViewHandler();
                assertEquals(true, messageViewHandler.processUnistrokeEvent(unistrokeEvent));
                assertEquals(messageViewView, unistrokeEvent.getTarget());
                assertEquals(id, unistrokeEvent.getId());
                waiter.resume();
            }
        });
        
        // Wait for UI to be updated.
        waiter.await();
    }
    
    @Test
    public void testProcessUnistrokeEvent4() throws TimeoutException {
        aspect = (Aspect) ResourceManager.loadModel("models/concern1/aspect1.ram");
        setUpAspect();
        
        testInputCursor = new InputCursor();
        mouseInputSource = new MouseInputSource(RamApp.getApplication());
        
        mouseInputEvent1 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 159, 180, MouseEvent.MOUSE_PRESSED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent1.onFired();
        
        mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 413, 400, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        //mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 0, 413, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        //mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 0, 160, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent2.onFired();
        
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                id = MTGestureEvent.GESTURE_UPDATED;
                mtPolygon = new MTPolygon(RamApp.getApplication(), new Vertex[] {new Vertex()});
                unistrokeEvent = new UnistrokeEvent(null, id, messageViewView, mtPolygon, null, testInputCursor);
                messageViewHandler = (MessageViewHandler) MessageViewHandlerFactory.INSTANCE.getMessageViewHandler();
                assertEquals(true, messageViewHandler.processUnistrokeEvent(unistrokeEvent));
                assertEquals(messageViewView, unistrokeEvent.getTarget());
                assertEquals(id, unistrokeEvent.getId());
                waiter.resume();
            }
        });
        
        // Wait for UI to be updated.
        waiter.await();
    }
    
    //Reaches first ? statement
    @Test
    public void testProcessUnistrokeEvent5() throws TimeoutException {
        aspect = (Aspect) ResourceManager.loadModel("models/concern1/aspect1.ram");
        setUpAspect();
        
        testInputCursor = new InputCursor();
        MouseInputSource mouseInputSource = new MouseInputSource(RamApp.getApplication());
        
        mouseInputEvent1 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 159, 180, MouseEvent.MOUSE_PRESSED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent1.onFired();
        
        mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 413, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        //mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 0, 413, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        //mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 0, 160, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent2.onFired();
        
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                id = MTGestureEvent.GESTURE_ENDED;
                mtPolygon = new MTPolygon(RamApp.getApplication(), new Vertex[] {new Vertex()});
                unistrokeEvent = new UnistrokeEvent(null, id, messageViewView, mtPolygon, null, testInputCursor);
                messageViewHandler = (MessageViewHandler) MessageViewHandlerFactory.INSTANCE.getMessageViewHandler();
                assertEquals(true, messageViewHandler.processUnistrokeEvent(unistrokeEvent));
                assertEquals(messageViewView, unistrokeEvent.getTarget());
                assertEquals(id, unistrokeEvent.getId());
                waiter.resume();
            }
        });
        
        // Wait for UI to be updated.
        waiter.await();
    }
    
    @Test
    public void testProcessUnistrokeEvent6() throws TimeoutException {
        aspect = (Aspect) ResourceManager.loadModel("models/concern1/aspect1.ram");
        setUpAspect();
        
        testInputCursor = new InputCursor();
        mouseInputSource = new MouseInputSource(RamApp.getApplication());
        
        mouseInputEvent1 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 159, 220, MouseEvent.MOUSE_PRESSED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent1.onFired();
        
        mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 413, 220, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        //mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 0, 413, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        //mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 0, 160, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent2.onFired();
        
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                id = MTGestureEvent.GESTURE_ENDED;
                mtPolygon = new MTPolygon(RamApp.getApplication(), new Vertex[] {new Vertex()});
                unistrokeEvent = new UnistrokeEvent(null, id, messageViewView, mtPolygon, null, testInputCursor);
                messageViewHandler = (MessageViewHandler) MessageViewHandlerFactory.INSTANCE.getMessageViewHandler();
                assertEquals(true, messageViewHandler.processUnistrokeEvent(unistrokeEvent));
                assertEquals(messageViewView, unistrokeEvent.getTarget());
                assertEquals(id, unistrokeEvent.getId());
                waiter.resume();
            }
        });
        
        // Wait for UI to be updated.
        waiter.await();
    }
    
    //Reaches second ? statement
    @Test
    public void testProcessUnistrokeEvent7() throws TimeoutException {
        aspect = (Aspect) ResourceManager.loadModel("models/concern1/aspect1.ram");
        setUpAspect();
        
        testInputCursor = new InputCursor();
        mouseInputSource = new MouseInputSource(RamApp.getApplication());
        
        mouseInputEvent1 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 159, 180, MouseEvent.MOUSE_PRESSED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent1.onFired();
        
        mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 
                0, 500, 500, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        //mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 0, 413, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        //mouseInputEvent2 = new MTMouseInputEvt(mouseInputSource, messageViewView, 0, 160, 180, MouseEvent.MOUSE_DRAGGED, testInputCursor, MouseEvent.BUTTON1_MASK);
        mouseInputEvent2.onFired();
        
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                id = MTGestureEvent.GESTURE_ENDED;
                mtPolygon = new MTPolygon(RamApp.getApplication(), new Vertex[] {new Vertex()});
                unistrokeEvent = new UnistrokeEvent(null, id, messageViewView, mtPolygon, null, testInputCursor);
                messageViewHandler = (MessageViewHandler) MessageViewHandlerFactory.INSTANCE.getMessageViewHandler();
                assertEquals(true, messageViewHandler.processUnistrokeEvent(unistrokeEvent));
                assertEquals(messageViewView, unistrokeEvent.getTarget());
                assertEquals(id, unistrokeEvent.getId());
                waiter.resume();
            }
        });
        
        // Wait for UI to be updated.
        waiter.await();
    }
    
    @Test
    public void testHandleCreateFragment1() throws TimeoutException {
        aspect = (Aspect) ResourceManager.loadModel("models/concern3/aspect3.ram");
        setUpAspect();
        
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                EList<Lifeline> list1 = messageViewView.getSpecification().getLifelines();
                Lifeline lifelineTest = list1.get(0);
                
                Collection<LifelineView> collection = messageViewView.getLifelineViews();
                Iterator<LifelineView> iterator = collection.iterator();
                LifelineView lifeLineViewTest = iterator.next();
                
                ContainerMapImpl layout = EMFModelUtil.getEntryFromMap(aspect.getLayout().getContainers(), messageView);
                //LayoutElement layoutElement = (LayoutElement) layout.getValue().get(lifelineTest);
                
                Vector3D location = new Vector3D(159, 180);
                
                message = messageView.getSpecification().getMessages().get(1);
                messageCallView = new MessageCallView(message,
                                new RamRectangleComponent(0, 0, 0, 0),
                                new RamRectangleComponent(0, 0, 700, 700));
                TapAndHoldEvent tapAndHoldEvent = new TapAndHoldEvent(null, 0, messageCallView, 
                        null, true, location, 0, 0, 0);
                
                MessageHandler messageHandler = new MessageHandler();
                
                messageHandler.processTapAndHoldEvent(tapAndHoldEvent);
                
                //RamApp.getApplication().dispatchEvent(new MouseEvent(RamApp.getApplication(), MouseEvent.MOUSE_PRESSED, 0, MouseEvent.BUTTON1_MASK, 159, 180, 159, 180, 1, false, MouseEvent.BUTTON1));
                //RamApp.getApplication().dispatchEvent(new MouseEvent(RamApp.getApplication(), MouseEvent.MOUSE_RELEASED, 0, MouseEvent.BUTTON1_MASK, 159, 180, 159, 180, 1, false, MouseEvent.BUTTON1));
                
                //tapAndHold.onFired();
                //RamApp.getApplication().dispatchEvent(new MouseEvent(RamApp.getApplication(), MouseEvent.MOUSE_PRESSED, 0, MouseEvent.BUTTON1_MASK, 400, 400, 400, 400, 1, false, MouseEvent.BUTTON1));
                //RamApp.getApplication().dispatchEvent(new MouseEvent(RamApp.getApplication(), MouseEvent.MOUSE_RELEASED, 0, MouseEvent.BUTTON1_MASK, 159, 180, 159, 180, 1, false, MouseEvent.BUTTON1));
                //FragmentContainer container = lifeLineViewTest.getFragmentContainerAt(location);
                
                //LifelineView lifelineView = new LifelineView(messageViewView, )
                //layout.getValue().get(null);
                
                //messageViewHandler.handleCreateFragment(messageViewView, lifeLineViewTest, location, container);

                waiter.resume();
            }
        });
        
        // Wait for UI to be updated.
        waiter.await();
    }
    
    public void setUpAspect() throws TimeoutException {
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
        
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                messageView = (MessageView) aspect.getMessageViews().get(0);
                ContainerMapImpl layout = EMFModelUtil.getEntryFromMap(aspect.getLayout().getContainers(), messageView);
                messageViewView = new MessageViewView(messageView, layout, 1024, 768);   
                RamApp.getActiveAspectScene().switchToView(messageViewView);
                
                waiter.resume();
            }
        });
        
        // Wait for UI to be updated.
        waiter.await();
    }

//    @Test
//    public void testHandleUnistrokeGesture() {
//        fail("Not yet implemented");
//    }

//    @Test
//    public void testProcessWheelEvent() {
//        fail("Not yet implemented");
//    }

//    @Test
//    public void testProcessZoomEvent() {
//        fail("Not yet implemented");
//    }

    /*
    @Test
    public void testHandleCreateFragment() {
        fail("Not yet implemented");
    }
    */

}
