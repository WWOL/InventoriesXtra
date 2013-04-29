import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Brian McCarthy
 *
 */
public class InventoriesXtraActions {

    private static Map<String, Map<Integer, InventoryWrapper>> inventoriesMap;

    static {
        inventoriesMap = new HashMap<String, Map<Integer, InventoryWrapper>>();
    }

    public static void sendHelp(Player p) {
        sendMessage(p, "==== " + InventoriesXtra.NAME + " Help ====");
        sendMessage(p, "==== " + InventoriesXtra.NAME + " Help ====");
    }

    public static void sendMessage(Player p, String message) {
        p.sendMessage(InventoriesXtra.PRE + message);
    }

    public static void sendError(Player p, String message) {
        sendMessage(p, Colors.Red + Colors.Bold + "ERROR: " + Colors.Reset + Colors.Red + message);
    }

    public static void onNoPerms(Player p, String perm) {
        sendError(p, "You do not have permission to use the command " + Colors.Blue + perm + Colors.Red + "!");
    }

    public static InventoryWrapper newInventory(Player p) {
        return newInventory(p, 9);
    }

    public static InventoryWrapper newInventory(Player p, int size) {
        return newInventory(p, 9, p.getName());
    }

    public static InventoryWrapper newInventory(Player p, int size, String name) {
        if (etc.getServer().getMCVersion().equalsIgnoreCase("1.5.1")) {
            return new InventoryWrapper151(p, size, name);
        }
        etc.getLoader().disablePlugin(InventoriesXtra.NAME);
        InventoriesXtra.LOG.warning("The minecraft version (" + etc.getServer().getMCVersion() + ") is not supported by InventoriesXtra!");
        return null;
    }

    public static void addInventory(Player p, InventoryWrapper inventory, int index) {
        Map<Integer, InventoryWrapper> inventories = inventoriesMap.get(p.getName());
        if (inventories == null) {
            inventories = new HashMap<Integer, InventoryWrapper>();
        }
        inventories.put(index, inventory);
        inventoriesMap.put(p.getName(), inventories);
    }

    public static InventoryWrapper getInventory(Player p) {
        return getInventory(p, 0);
    }

    public static InventoryWrapper getInventory(Player p, int index) {
        return inventoriesMap.get(p.getName()).get(index);
    }

    public static int getInt(String[] args, int index, int other) {
        try {
            return Integer.parseInt(args[index]);
        } catch (Exception e) {
            return other;
        }
    }
    public static String getString(String[] args, int index, String other) {
        try {
            return args[index];
        } catch (Exception e) {
            return other;
        }
    }

    public static void loadInventories(Player p) {
        String path = InventoriesXtra.CONFIG + "inventories" + File.separator + p.getName() + File.separator;
        File pathFile = new File(path);
        String[] files = pathFile.list(new FilenameFilterTXT());
        if (files == null) {
            return;
        }
        for (int fileNum = 0; fileNum < files.length; fileNum++) {
            String file = files[fileNum];
            PropertiesFile props = new PropertiesFile(path + file);
            int size = props.getInt("size", 9);
            String name = props.getString("name", p.getName());
            InventoryWrapper inventory = newInventory(p, size, name);
            for (int i = 0; i < size; i++) {
                Item item = readItem(props.getString(String.valueOf(i)));
                inventory.setItem(i, item);
            }
            addInventory(p, inventory, fileNum);
        }
    }
    
    public static void saveInventories(Player p) {
        String path = InventoriesXtra.CONFIG + "inventories" + File.separator + p.getName() + File.separator;
        Map<Integer, InventoryWrapper> inventories = inventoriesMap.get(p.getName());
        for (int i = 0; i < inventoriesMap.size(); i++) {
            InventoryWrapper inventory = inventories.get(i);
            PropertiesFile props = new PropertiesFile(path + i + ".txt");
            for (int j = 0; j < inventory.getSize(); j++) {
                props.setString(String.valueOf(j), writeItem(inventory.getItem(j)));
            }
        }
        inventoriesMap.remove(p.getName());
    }

    public static Item readItem(String string) {
        String[] data = string.split(",");
        if (data.length == 0) {
            return null;
        }
        int itemID = 0;
        int damage = 0;
        int amount = 0;

        try {
            itemID = Integer.parseInt(data[0]);
        } catch (Exception e) {
            return null;
        }
        if (data.length >= 2) {
            try {
                damage = Integer.parseInt(data[1]);
            } catch (Exception e) {
                return null;
            }
        }
        if (data.length >= 3) {
            try {
                amount = Integer.parseInt(data[2]);
            } catch (Exception e) {
                return null;
            }
        }
        Item item = new Item(itemID, damage, amount);
        if (data.length >= 4) {
            List<Enchantment> enchantments = readEnchantments(data[3]);
            for (Enchantment enchantment : enchantments) {
                item.addEnchantment(enchantment);
            }
        }
        if (data.length >= 5) {
            List<String> lore = readLore(data[4]);
            item.setLore(lore.toArray(new String[0]));
        }
        return new Item();
    }

    public static List<Enchantment> readEnchantments(String string) {
        List<Enchantment> enchantments = new ArrayList<Enchantment>();
        String[] data = string.split("@");
        for (String s : data) {
            Enchantment enchantment = readEnchantment(s);
            enchantments.add(enchantment);
        }
        return enchantments;
    }

    public static Enchantment readEnchantment(String string) {
        String[] data = string.split(":");
        int id = 0;
        int level = 0;
        try {
            id = Integer.parseInt(data[2]);
        } catch (Exception e) {
            return null;
        }
        try {
            level = Integer.parseInt(data[2]);
        } catch (Exception e) {
            return null;
        }
        return new Enchantment(Enchantment.Type.fromId(id), level);
    }

    public static List<String> readLore(String string) {
        List<String> strings = new ArrayList<String>();
        String[] data = string.split(":");
        for (String s : data) {
            strings.add(s);
        }
        return strings;
    }

    public static String writeItem(Item item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getItemId());
        sb.append(",");
        sb.append(item.getDamage());
        sb.append(",");
        sb.append(item.getAmount());
        sb.append(",");
        sb.append(writeEnchantments(item));
        sb.append(",");
        sb.append(writeLore(item));
        return sb.toString();
    }

    public static String writeEnchantments(Item item) {
        StringBuilder sb = new StringBuilder();
        for (Enchantment enchantment : item.getEnchantments()) {
            sb.append(writeEnchantment(enchantment));
            sb.append("@");
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    public static String writeEnchantment(Enchantment enchantment) {
        StringBuilder sb = new StringBuilder();
        sb.append(enchantment.getType().getType());
        sb.append(",");
        sb.append(enchantment.getLevel());
        return sb.toString();
    }
    
    public static String writeLore(Item item) {
        String[] lores = item.getLore();
        StringBuilder sb = new StringBuilder();
        for (String lore : lores) {
            sb.append(lore);
            sb.append("@");
        }
        return sb.substring(0, sb.length() - 1);
    }

}