package rsf.game.model;

/**
 * @author Thomas
 */
public class Model {

    /** The identification number of this model */
    protected final int id;

    /** The active flag for the model. Determines if the model is active in-game */
    protected boolean active;

    public Model(int id) {
        this.id = id;
    }

    /**
     * Gets the identification number of the model
     * @return the model id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the models active flag
     * @return {@code true} if the model is currently active, otherwise {@code false}
     */
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
