package rsf.networking.session.packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies the packet(s) upon startup
 * @author Thomas
 */
@Target(ElementType.TYPE)
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
public @interface PacketIdentifier {

    /**
     * Gets the ids which identifies the packet decoder
     * @return the array of ids
     */
    int[] getIds();

}
