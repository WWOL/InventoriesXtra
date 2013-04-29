
public abstract class InventoryWrapper {
    
    protected OIInventory inventory;
    protected String owner;
    
    public InventoryWrapper(Player owner, int size, String name) {
        this.inventory = new OInventoryBasic(name, true, size);
        this.owner = owner.getName();
    }
    
    public String getOwnerName() {
        return owner;
    }
    
    public abstract String getName();
    
    public abstract int getSize();
    
    public abstract void show(Player p);
    
    public abstract void setItem(int slot, Item item);
    
    public abstract Item getItem(int slot);
    
}
