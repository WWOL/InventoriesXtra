import java.lang.reflect.Field;

/**
 * 
 * @author Brian McCarthy
 *
 */
public class InventoriesXtraListener extends PluginListener {

    public boolean onCommand(Player p, String[] s) {
        if(s[0].equalsIgnoreCase("/" + InventoriesXtra.NAME + "settings")) {
            if (!p.canUseCommand(s[0])) {
                InventoriesXtraActions.onNoPerms(p, s[0]);
                return true;
            }
            if (s.length != 2) {
                InventoriesXtraActions.sendError(p, "Please specify a setting to view!");
                return true;
            }
            try {
                Field field = InventoriesXtraSettings.class.getField(s[1]);
                InventoriesXtraActions.sendMessage(p, "Key: " + field.getName() + ", Value: " + field.get(null));
            } catch (Exception e) {
                InventoriesXtraActions.sendError(p, "Could not get setting! Are you sure it exists?");
                return true;
            }
            return true;
        } else if(s[0].equalsIgnoreCase("/gui")) {
            if (!p.canUseCommand(s[0])) {
                InventoriesXtraActions.onNoPerms(p, s[0]);
                return true;
            }
            int index = InventoriesXtraActions.getInt(s, 1, -1);
            if (index == -1) {
                InventoriesXtraActions.sendError(p, "Not a valid index!");
                return true;
            }
            InventoryWrapper inventory = InventoriesXtraActions.getInventory(p, index);
            if (inventory == null) {
                InventoriesXtraActions.sendError(p, "Inventory doesn't exist!");
                return true;
            }
            inventory.show(p);
            return true;
        } else if(s[0].equalsIgnoreCase("/guinew")) {
            if (!p.canUseCommand(s[0])) {
                InventoriesXtraActions.onNoPerms(p, s[0]);
                return true;
            }
            int index = InventoriesXtraActions.getInt(s, 1, -1);
            if (index == -1) {
                InventoriesXtraActions.sendError(p, "Not a valid index!");
                return true;
            }
            int size = InventoriesXtraActions.getInt(s, 2, -1);
            if (size == -1) {
                InventoriesXtraActions.sendError(p, "Not a valid size!");
                return true;
            }
            String name = InventoriesXtraActions.getString(s, 3, null);
            InventoryWrapper inventory;
            if (name == null) {
                inventory = InventoriesXtraActions.newInventory(p, size);
            } else {
                inventory = InventoriesXtraActions.newInventory(p, size, name);
            }
            InventoriesXtraActions.addInventory(p, inventory, index);
            InventoriesXtraActions.sendMessage(p, "Created a new inventory.\nAccess it with \"/gui " + index + "\"");
            return true;
        }

        return false;
    }
    
    public HookParametersConnect onPlayerConnect(Player p, HookParametersConnect hookParametersConnect) {
        InventoriesXtraActions.loadInventories(p);
        InventoriesXtra.LOG.finer("Loaded inventories for " + p.getName());
        return hookParametersConnect;
    }
    
    public HookParametersDisconnect onPlayerDisconnect(Player p, HookParametersDisconnect hookParametersDisconnect) {
        InventoriesXtraActions.saveInventories(p);
        InventoriesXtra.LOG.finer("Saved inventories for " + p.getName());
        return hookParametersDisconnect;
    }

}