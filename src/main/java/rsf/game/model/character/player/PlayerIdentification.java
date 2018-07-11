package rsf.game.model.character.player;

/**
 * @author Thomas
 */
public final class PlayerIdentification {

    private final String username;

    private final String display_name;

    private final String password;

    private final long hashed_password;

    public PlayerIdentification(String username, String display_name, String password, long hashed_password) {
        this.username = username;
        this.display_name = display_name;
        this.password = password;
        this.hashed_password = hashed_password;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getPassword() {
        return password;
    }

    public long getHashed_password() {
        return hashed_password;
    }
}
