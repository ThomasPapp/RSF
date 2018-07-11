package rsf.game.render.flag;

import rsf.networking.session.packet.ByteBuf;

/**
 * @author Thomas
 */
public abstract class UpdateFlag<T> {

    protected final T of;

    public UpdateFlag(T of) {
        this.of = of;
    }

    public abstract void encode(ByteBuf buf);

    public abstract int bitmask();

    public abstract int ordinal();

}
