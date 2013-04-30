
public class InventoryWrapper151 extends InventoryWrapper {
    
    public InventoryWrapper151(Player owner, int size, String name) {
        super(owner, size, name);
    }

    @Override
    public String getName() {
        return inventory.b();
    }
    
    @Override
    public int getSize() {
        return inventory.j_();
    }

    @Override
    public void show(Player p) {
        p.getEntity().a(inventory);
    }

    @Override
    public void setItem(int slot, Item item) {
        if (item == null || item.getBaseItem() == null) {
            return;
        }
        inventory.a(slot, item.getBaseItem());
    }

    @Override
    public Item getItem(int slot) {
        OItemStack oStack = inventory.a(slot);
        return oStack == null ? null : new Item(oStack);
    }

}
