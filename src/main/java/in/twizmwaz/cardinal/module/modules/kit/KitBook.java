package in.twizmwaz.cardinal.module.modules.kit;

import java.util.List;

class KitBook {

    private String title;
    private String author;
    private List<String> pages;
    private int slot;

    protected KitBook(String title, String author, List<String> pages, int slot) {
        this.title = title;
        this.author = author;
        this.pages = pages;
        this.slot = slot;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPage(int index) {
        return pages.get(index);
    }

    public List<String> getPages(){
        return pages;
    }

    public int getSlot() {
        return slot;
    }

    public boolean hasSlot() {
        return slot != -1;
    }

}
