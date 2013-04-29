/**
 * Class full of static properties
 * @author Brian McCarthy
 *
 */
public class InventoriesXtraSettings {

    public static PropertiesFile props;

    public static int MAX_EXTRA_INVENTORIES;
    
    public static void load() {
        props = new PropertiesFile(InventoriesXtra.CONFIG + InventoriesXtra.NAME + ".properties");
        setup();
        save(); // Make sure any new defaults have been saved
    }

    private static void setup() {
        MAX_EXTRA_INVENTORIES = props.getInt("MAX_EXTRA_INVENTORIES", 1);
    }
    
    public static void save() {
        try {
            props.save();
        } catch (Exception e) {
            InventoriesXtra.LOG.warning(InventoriesXtra.SPRE + "Exception while saving to file.");
        }
    }

}