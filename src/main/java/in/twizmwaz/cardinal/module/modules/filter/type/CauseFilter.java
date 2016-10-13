package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.CauseFilterParser;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.WorldEvent;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class CauseFilter extends FilterModule {

    private final EventCause cause;

    public CauseFilter(final CauseFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.cause = parser.getCause();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Event) {
                Boolean result = evaluate((Event) object);
                if (result != null) return result ? ALLOW : DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

    private Boolean evaluate(Event event) {
        if (!(event instanceof EntityDamageEvent)) {
            switch (cause) {
                case WORLD:
                    return event instanceof WorldEvent;
                case LIVING:
                    return event instanceof EntityEvent && ((EntityEvent) event).getEntity() instanceof LivingEntity;
                case MOB:
                    return event instanceof EntityEvent && ((EntityEvent) event).getEntity() instanceof Creature;
                case PLAYER:
                    return event instanceof PlayerEvent || event instanceof BlockPlaceEvent || event instanceof BlockBreakEvent;

                case PUNCH:
                    return event instanceof PlayerInteractEvent
                            && ((PlayerInteractEvent) event).getAction().equals(Action.LEFT_CLICK_BLOCK);
                case TRAMPLE:
                    return event instanceof PlayerMoveEvent;
                case MINE:
                    return event instanceof BlockBreakEvent;

                case EXPLOSION:
                    return event instanceof EntityExplodeEvent;
            }
        } else {
            EntityDamageEvent.DamageCause damageCause = ((EntityDamageEvent) event).getCause();
            switch (cause) {
                case MELEE:
                    return damageCause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK);
                case PROJECTILE:
                    return damageCause.equals(EntityDamageEvent.DamageCause.PROJECTILE);
                case POTION:
                    return damageCause.equals(EntityDamageEvent.DamageCause.MAGIC)
                            || damageCause.equals(EntityDamageEvent.DamageCause.POISON)
                            || damageCause.equals(EntityDamageEvent.DamageCause.WITHER)
                            || damageCause.equals(EntityDamageEvent.DamageCause.DRAGON_BREATH);
                case EXPLOSION:
                    return damageCause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
                            || damageCause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
                case COMBUSTION:
                    return damageCause.equals(EntityDamageEvent.DamageCause.FIRE)
                            || damageCause.equals(EntityDamageEvent.DamageCause.FIRE_TICK)
                            || damageCause.equals(EntityDamageEvent.DamageCause.MELTING)
                            || damageCause.equals(EntityDamageEvent.DamageCause.LAVA)
                            || damageCause.equals(EntityDamageEvent.DamageCause.HOT_FLOOR);
                case FALL:
                    return damageCause.equals(EntityDamageEvent.DamageCause.FALL);
                case GRAVITY:
                    return damageCause.equals(EntityDamageEvent.DamageCause.FALL)
                            || damageCause.equals(EntityDamageEvent.DamageCause.VOID);
                case VOID:
                    return damageCause.equals(EntityDamageEvent.DamageCause.VOID);
                case SQUASH:
                    return damageCause.equals(EntityDamageEvent.DamageCause.FALLING_BLOCK);
                case SUFFOCATION:
                    return damageCause.equals(EntityDamageEvent.DamageCause.SUFFOCATION);
                case DROWNING:
                    return damageCause.equals(EntityDamageEvent.DamageCause.DROWNING);
                case STARVATION:
                    return damageCause.equals(EntityDamageEvent.DamageCause.STARVATION);
                case LIGHTNING:
                    return damageCause.equals(EntityDamageEvent.DamageCause.LIGHTNING);
                case CACTUS:
                    return damageCause.equals(EntityDamageEvent.DamageCause.CONTACT);
                case THORNS:
                    return damageCause.equals(EntityDamageEvent.DamageCause.THORNS);
            }
        }
        return null;
    }

    public enum EventCause {

        WORLD(),
        LIVING(),
        MOB(),
        PLAYER(),

        PUNCH(),
        TRAMPLE(),
        MINE(),

        MELEE(),
        PROJECTILE(),
        POTION(),
        EXPLOSION(),
        COMBUSTION(),
        FALL(),
        GRAVITY(),
        VOID(),
        SQUASH(),
        SUFFOCATION(),
        DROWNING(),
        STARVATION(),
        LIGHTNING(),
        CACTUS(),
        THORNS();


        public static EventCause getEventCause(String string) {
            switch (string.toLowerCase().replaceAll(" ", "")) {
                case "world":
                    return WORLD;
                case "living":
                    return LIVING;
                case "mob":
                    return MOB;
                case "player":
                    return PLAYER;
                case "punch":
                    return PUNCH;
                case "trample":
                    return TRAMPLE;
                case "mine":
                    return MINE;
                case "melee":
                    return MELEE;
                case "projectile":
                    return PROJECTILE;
                case "potion":
                    return POTION;
                case "tnt":
                case "explosion":
                    return EXPLOSION;
                case "combustion":
                    return COMBUSTION;
                case "fall":
                    return FALL;
                case "gravity":
                    return GRAVITY;
                case "void":
                    return VOID;
                case "squash":
                    return SQUASH;
                case "suffocation":
                    return SUFFOCATION;
                case "drowning":
                    return DROWNING;
                case "starvation":
                    return STARVATION;
                case "lightning":
                    return LIGHTNING;
                case "cactus":
                    return CACTUS;
                case "thorns":
                    return THORNS;
                default:
                    return null;
            }
        }

    }

}
