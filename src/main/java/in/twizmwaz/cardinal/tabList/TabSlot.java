package in.twizmwaz.cardinal.tabList;

import in.twizmwaz.cardinal.tabList.entries.TabEntry;

public class TabSlot {

    private TabView view;
    private TabEntry entry;
    private TabEntry newEntry;
    private int slot;

    public TabSlot(TabView view, TabEntry entry, int slot) {
        this.view = view;
        this.slot = slot;
        this.entry = entry;
        this.entry.setSlot(view.getViewer(), slot, 0);
        view.getEmptyPlayers().add(entry);
    }

    public void setNewEntry(TabEntry entry) {
        this.newEntry = entry;
    }

    public void removeEntry(TabEntry entry, int newSlot) {
        if (newSlot != this.slot) {
            if (this.entry == entry) this.entry = null;
            if (this.newEntry == entry) this.newEntry = null;
        }
    }

    public void update() {
        if (newEntry != entry && newEntry != null) {
            if (entry != null) view.hideEntry(entry);
            setSlot(newEntry);
            newEntry = null;
        } else if (entry == null) setSlot(TabList.getFakePlayer(view));
    }

    private void setSlot(TabEntry entry) {
        this.entry = entry;
        view.setSlot(this.entry, slot);
    }

}
