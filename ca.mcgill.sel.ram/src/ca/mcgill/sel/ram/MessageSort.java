/**
 */
package ca.mcgill.sel.ram;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Message Sort</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see ca.mcgill.sel.ram.RamPackage#getMessageSort()
 * @model
 * @generated
 */
public enum MessageSort implements Enumerator {
    /**
     * The '<em><b>Synch Call</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SYNCH_CALL_VALUE
     * @generated
     * @ordered
     */
    SYNCH_CALL(0, "synchCall", "synchCall"),

    /**
     * The '<em><b>Create Message</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CREATE_MESSAGE_VALUE
     * @generated
     * @ordered
     */
    CREATE_MESSAGE(1, "createMessage", "createMessage"),

    /**
     * The '<em><b>Delete Message</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DELETE_MESSAGE_VALUE
     * @generated
     * @ordered
     */
    DELETE_MESSAGE(2, "deleteMessage", "deleteMessage"),

    /**
     * The '<em><b>Reply</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #REPLY_VALUE
     * @generated
     * @ordered
     */
    REPLY(3, "reply", "reply");

    /**
     * The '<em><b>Synch Call</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Synch Call</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #SYNCH_CALL
     * @model name="synchCall"
     * @generated
     * @ordered
     */
    public static final int SYNCH_CALL_VALUE = 0;

    /**
     * The '<em><b>Create Message</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Create Message</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CREATE_MESSAGE
     * @model name="createMessage"
     * @generated
     * @ordered
     */
    public static final int CREATE_MESSAGE_VALUE = 1;

    /**
     * The '<em><b>Delete Message</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Delete Message</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #DELETE_MESSAGE
     * @model name="deleteMessage"
     * @generated
     * @ordered
     */
    public static final int DELETE_MESSAGE_VALUE = 2;

    /**
     * The '<em><b>Reply</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Reply</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #REPLY
     * @model name="reply"
     * @generated
     * @ordered
     */
    public static final int REPLY_VALUE = 3;

    /**
     * An array of all the '<em><b>Message Sort</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final MessageSort[] VALUES_ARRAY =
        new MessageSort[] {
            SYNCH_CALL,
            CREATE_MESSAGE,
            DELETE_MESSAGE,
            REPLY,
        };

    /**
     * A public read-only list of all the '<em><b>Message Sort</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<MessageSort> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Message Sort</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static MessageSort get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            MessageSort result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Message Sort</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static MessageSort getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            MessageSort result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Message Sort</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static MessageSort get(int value) {
        switch (value) {
            case SYNCH_CALL_VALUE: return SYNCH_CALL;
            case CREATE_MESSAGE_VALUE: return CREATE_MESSAGE; 
            case DELETE_MESSAGE_VALUE: return DELETE_MESSAGE;
            case REPLY_VALUE: return REPLY;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private MessageSort(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //MessageSort
