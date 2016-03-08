package in.twizmwaz.cardinal.module.modules.tutorial;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DisplayHandler {

    private Player player;
    private Tutorial tutorial;
    private int pos = 0;

    public DisplayHandler(Player player, Tutorial tutorial) {
        this.player = player;
        this.tutorial = tutorial;
    }

    public void displayNext() {
        if (++this.pos >= this.tutorial.getAllStages().size()) {
            this.pos = this.tutorial.getAllStages().size() - 1;
            return;
        }
        this.displayCurrent();
    }

    public void displayPrev() {
        if (--this.pos < 0) {
            this.pos = 0;
            return;
        }
        this.displayCurrent();
    }

    private void displayCurrent() {
        Stage stage = this.tutorial.getAllStages().get(this.pos);


        if (stage == this.tutorial.getPrefix() || stage == this.tutorial.getSuffix()) {
            this.player.playSound(this.player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 0.5f, 2);
            if (stage == this.tutorial.getPrefix()) {
                this.player.sendMessage("");
            }
        }


        this.player.sendMessage("");
        this.player.sendMessage(stage.getFormattedTitle());

        for (String line : stage.getLines()) {
            this.player.sendMessage(line);
        }
        this.player.sendMessage("");

        if (stage.getTeleport() != null) {
            Location location = stage.getTeleport().getRandomPoint().getLocation();
            if (!location.getBlock().getType().equals(Material.AIR) || !location.add(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
                this.player.sendMessage("   " + ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.ERROR_TUTORIAL_TP).getMessage(ChatUtil.getLocale(this.player)));
            } else {
                this.player.setFlying(true);
                this.player.teleport(stage.getTeleport().getRandomPoint().getLocation());
                this.player.playSound(this.player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 0.5f, 1);
            }
        }

        this.updateItem(player);
    }

    private void updateItem(Player player) {
        ItemStack emerald = player.getItemInHand();
        ItemMeta meta = emerald.getItemMeta();

        String left = null;
        String right = null;

        StringBuilder name = new StringBuilder();
        String sep = "";

        if (this.pos > 0) {
            left = ChatColor.RED + this.tutorial.getAllStages().get(this.pos - 1).getTitle();
        }

        if (this.pos < this.tutorial.getAllStages().size() - 1) {
            right = ChatColor.GREEN + this.tutorial.getAllStages().get(this.pos + 1).getTitle();
        }

        if (left != null && right != null) {
            sep = ChatColor.AQUA + " | ";
        }

        if (left != null) {
            name.append(ChatColor.GRAY).append("Left click ").append(ChatColor.AQUA).append("« ").append(left).append(sep);
        }

        if (right != null) {
            name.append(right).append(" ").append(ChatColor.AQUA).append("» ").append(ChatColor.GRAY).append("Right click");
        }

        meta.setDisplayName(name.toString());
        emerald.setItemMeta(meta);

        for (int pos : player.getInventory().all(Material.EMERALD).keySet()) {
            player.getInventory().setItem(pos, emerald);
        }
    }
}
