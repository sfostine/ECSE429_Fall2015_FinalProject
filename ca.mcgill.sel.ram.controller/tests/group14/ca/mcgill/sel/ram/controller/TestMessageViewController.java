package ca.mcgill.sel.ram.controller;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.sel.commons.emf.util.AdapterFactoryRegistry;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.FragmentContainer;
import ca.mcgill.sel.ram.Interaction;
import ca.mcgill.sel.ram.Lifeline;
import ca.mcgill.sel.ram.Message;
import ca.mcgill.sel.ram.MessageOccurrenceSpecification;
import ca.mcgill.sel.ram.MessageView;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.TypedElement;
import ca.mcgill.sel.ram.impl.ContainerMapImpl;
import ca.mcgill.sel.ram.impl.GateImpl;
import ca.mcgill.sel.ram.impl.MessageOccurrenceSpecificationImpl;
import ca.mcgill.sel.ram.provider.RamItemProviderAdapterFactory;
import ca.mcgill.sel.ram.util.RamResourceFactoryImpl;

public class TestMessageViewController {
    
    private static MessageViewController TMV;
    private Aspect aspect;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TMV = ControllerFactory.INSTANCE.getMessageViewController();
        
        // Initialize ResourceManager.
        ResourceManager.initialize();
        // Initialize packages.
        RamPackage.eINSTANCE.eClass();

        // Register resource factories.
        ResourceManager.registerExtensionFactory("ram", new RamResourceFactoryImpl());

        // Initialize adapter factories.
        AdapterFactoryRegistry.INSTANCE.addAdapterFactory(RamItemProviderAdapterFactory.class);
    }
    
    @Before
    public void setUp(){    
        this.aspect = (Aspect) ResourceManager.loadModel("../ca.mcgill.sel.ram.gui/models/testConcern/testAspect.ram");

    }
    @After
    public void tearDown(){
        //Unload
        ResourceManager.unloadResource(this.aspect.eResource());
    }

    
    @Test
    public void testCreateLifeline() {
       //Setup method parameters
       MessageView mv = (MessageView) this.aspect.getMessageViews().get(1);
       Interaction interaction = mv.getSpecification();
       Lifeline lifeline = interaction.getLifelines().get(0);
       TypedElement represents = lifeline.getRepresents();
       
       //Setup comparison variables before call
       int oldLifelineLength = mv.getSpecification().getLifelines().size();
       float x = 50, y = 50;
       
       //Method call
       TMV.createLifeline(interaction, represents, x, y);
       
      //Setup comparison variables after call
       int newLifelineLength = mv.getSpecification().getLifelines().size();
       Lifeline newLifeline = interaction.getLifelines().get(newLifelineLength - 1);
       
       ContainerMapImpl layout = EMFModelUtil.getEntryFromMap(this.aspect.getLayout().getContainers(), mv);
       float newX = layout.getValue().get(newLifeline).getX();
       float newY = layout.getValue().get(newLifeline).getY();

       //Check results
       assertEquals(oldLifelineLength + 1, newLifelineLength);
       assertEquals(represents, newLifeline.getRepresents());
       assertEquals(x, newX, 0.0001);
       assertEquals(y, newY, 0.0001);
       
    }
    
    @Test
    //INCOMPLETE COVERAGE
    public void testCreateLifelineWithMessage() {
        //Setup method parameters
        MessageView mv = (MessageView) this.aspect.getMessageViews().get(1);
        Interaction interaction = mv.getSpecification();
        Message msg = interaction.getMessages().get(2);
        Lifeline lifelineFrom = interaction.getLifelines().get(0);
        Lifeline lifelineToCreate = interaction.getLifelines().get(1);
        TypedElement represents = lifelineToCreate.getRepresents();
        FragmentContainer container = interaction.getFragments().get(2).getContainer();
        Operation sign = msg.getSignature();
        
      //Setup comparison variables before call
        float x = 50, y = 50;
        int messagesLength = interaction.getMessages().size();
        int fragmentsLength = interaction.getFragments().size();
        int oldLifelineLength = mv.getSpecification().getLifelines().size();

        
        TMV.createLifelineWithMessage(interaction, represents, x, y, lifelineFrom, container, sign, interaction.getMessages().size());
        
      //Setup comparison variables after call
        int newLifelineLength = mv.getSpecification().getLifelines().size();
        Lifeline newLifeline = interaction.getLifelines().get(newLifelineLength - 1);
        
        ContainerMapImpl layout = EMFModelUtil.getEntryFromMap(this.aspect.getLayout().getContainers(), mv);
        float newX = layout.getValue().get(newLifeline).getX();
        float newY = layout.getValue().get(newLifeline).getY();
        
        int newMessagesLength = interaction.getMessages().size();
        int newFragmentsLength = interaction.getFragments().size();
        Message createdMsg = interaction.getMessages().get(interaction.getMessages().size() - 1);
        
        //Check results
        assertEquals(oldLifelineLength + 1, newLifelineLength);
        assertEquals(represents, newLifeline.getRepresents());
        assertEquals(x, newX, 0.0001);
        assertEquals(y, newY, 0.0001);
        
        assertEquals(messagesLength + 1, newMessagesLength);
        assertEquals(fragmentsLength + 2, newFragmentsLength);  //There are 2 extra fragment when creating a message
        assertEquals(msg.getSignature(), createdMsg.getSignature());
        assertEquals(msg.getMessageSort(), createdMsg.getMessageSort());
        
        assertEquals(lifelineFrom, ((MessageOccurrenceSpecificationImpl) createdMsg.getSendEvent()).getCovered().get(0));
        assertEquals(newLifeline, ((MessageOccurrenceSpecificationImpl) createdMsg.getReceiveEvent()).getCovered().get(0));
    
    }
    
    @Test
    public void testMoveLifeline() {   
        //Setup method parameters
        MessageView mv = (MessageView) this.aspect.getMessageViews().get(1);
        Lifeline lifeline = mv.getSpecification().getLifelines().get(0);
        ContainerMapImpl layout = EMFModelUtil.getEntryFromMap(this.aspect.getLayout().getContainers(), mv);
        
      //Setup comparison variables before call
        float oldX = layout.getValue().get(lifeline).getX();
        float oldY = layout.getValue().get(lifeline).getY();

        //Method call
        TMV.moveLifeline(lifeline, oldX + 10, oldY + 10);
        
      //Setup comparison variables after call
        float newX = layout.getValue().get(lifeline).getX();
        float newY = layout.getValue().get(lifeline).getY();
        
        //Check results
        assertEquals(oldX + 10, newX, 0.001);
        assertEquals(oldY + 10, newY, 0.001);
    }
    
    @Test
    //INCOMPLETE COVERAGE
    public void testCreateMessage() {
        //Setup method parameters for the second create message
        MessageView mv = (MessageView) this.aspect.getMessageViews().get(1);
        Interaction owner = mv.getSpecification();
        Message msg = owner.getMessages().get(2);
        
        Lifeline lifelineFrom = owner.getLifelines().get(0);
        Lifeline lifelineTo = owner.getLifelines().get(1);
        
        FragmentContainer container = owner.getFragments().get(2).getContainer();
        Operation sign = msg.getSignature();
        
      //Setup comparison variables before call
        int messagesLength = owner.getMessages().size();
        int fragmentsLength = owner.getFragments().size();
            
        TMV.createMessage(owner, lifelineFrom, lifelineTo, container, sign, owner.getMessages().size());
        
      //Setup comparison variables after call
        int newMessagesLength = owner.getMessages().size();
        int newFragmentsLength = owner.getFragments().size();
        Message createdMsg = owner.getMessages().get(owner.getMessages().size() - 1);
        
        assertEquals(messagesLength + 1, newMessagesLength);
        assertEquals(fragmentsLength + 2, newFragmentsLength);  //There are 2 extra fragment when creating a message
        assertEquals(msg.getSignature(), createdMsg.getSignature());
        assertEquals(msg.getMessageSort(), createdMsg.getMessageSort());
        
        assertEquals(lifelineFrom, ((MessageOccurrenceSpecificationImpl) createdMsg.getSendEvent()).getCovered().get(0));
        assertEquals(lifelineTo, ((MessageOccurrenceSpecificationImpl) createdMsg.getReceiveEvent()).getCovered().get(0));

    }
    
    @Test
    public void testCreateReplyMessage() {
      //Setup method parameters
        MessageView mv = (MessageView) this.aspect.getMessageViews().get(0);
        Interaction owner = mv.getSpecification();
        Message msg = owner.getMessages().get(1);
        
        Lifeline lifelineFrom = owner.getLifelines().get(0);
        Lifeline lifelineTo = null;
        
        FragmentContainer container = owner.getFragments().get(0).getContainer();
        Operation sign = msg.getSignature();
        
      //Setup comparison variables before call
        int messagesLength = owner.getMessages().size();
        int fragmentsLength = owner.getFragments().size();
            
        TMV.createReplyMessage(owner, lifelineFrom, lifelineTo, container, sign, owner.getMessages().size());
        
      //Setup comparison variables after call
        int newMessagesLength = owner.getMessages().size();
        int newFragmentsLength = owner.getFragments().size();
        Message createdMsg = owner.getMessages().get(owner.getMessages().size() - 1);
        
        assertEquals(messagesLength + 1, newMessagesLength);
        assertEquals(fragmentsLength + 1, newFragmentsLength);  // only 1 extra fragment when reply to gate
        assertEquals(msg.getSignature(), createdMsg.getSignature());
        assertEquals(msg.getMessageSort(), createdMsg.getMessageSort());
        
        assertEquals(lifelineFrom, ((MessageOccurrenceSpecificationImpl) createdMsg.getSendEvent()).getCovered().get(0));
        assertTrue(createdMsg.getReceiveEvent() instanceof GateImpl);
    }
    
    @Test
    public void testRemoveMessages() {   
        //Setup method parameters
        MessageView mv = (MessageView) this.aspect.getMessageViews().get(1);
        Message msg = mv.getSpecification().getMessages().get(1);
        MessageOccurrenceSpecification sendEvent = (MessageOccurrenceSpecification) msg.getSendEvent();
        FragmentContainer container = (FragmentContainer) sendEvent.eContainer();
        Interaction interaction = sendEvent.getMessage().getInteraction();
        
      //Setup comparison variables before call
        int oldMessagesLength = mv.getSpecification().getMessages().size();
        
        //Method call
        TMV.removeMessages(interaction, container, sendEvent);
        
      //Setup comparison variables after call
        int newMessagesLength = mv.getSpecification().getMessages().size();
        
        //check results
        assertFalse(mv.getSpecification().getMessages().contains(msg));
        assertEquals(oldMessagesLength - 1, newMessagesLength);
    }
    


}
