import java.io.File;
import java.util.logging.Logger;

/**
 * 
 * @author Brian McCarthy
 *
 */
public class InventoriesXtra extends Plugin {
	public final static String NAME = "InventoriesXtra";
	public final static String AUTHOR = "WWOL";
	public final static String VER = "1.0";
	public final static String SPRE = "[" + NAME + "] ";
	public final static String PRE = Colors.Blue + SPRE + Colors.Gold;
	public final static String CONFIG = "plugins" + File.separator + "config" + File.separator + NAME + File.separator;
	public final static Logger LOG = Logger.getLogger("Minecraft");

	public static InventoriesXtra instance;
	InventoriesXtraListener listener = new InventoriesXtraListener();

	public void enable() {
	    instance = this;
		LOG.info(SPRE + NAME + " by " + AUTHOR + " Ver:" + VER + " enabled!");
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_CONNECT, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_DISCONNECT, listener, this, PluginListener.Priority.MEDIUM);

		File file = new File(CONFIG);
		try {
			file.mkdirs();
		} catch (Exception e) {
			LOG.warning(InventoriesXtra.SPRE + "Could not create configuration dir!");
			e.printStackTrace();
		}
		file = new File(CONFIG + "inventories" + File.separator);
		try {
            file.mkdirs();
        } catch (Exception e) {
            LOG.warning(InventoriesXtra.SPRE + "Could not create inventories dir!");
            e.printStackTrace();
        }
		InventoriesXtraSettings.load();
	}

	public void disable() {
		LOG.info(SPRE + NAME + " by " + AUTHOR + " Ver:" + VER + " disabled!");
		InventoriesXtraSettings.save();
	}

}