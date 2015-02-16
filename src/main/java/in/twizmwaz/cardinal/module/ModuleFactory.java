package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegionBuilder;
import in.twizmwaz.cardinal.module.modules.armorKeep.ArmorKeepBuilder;
import in.twizmwaz.cardinal.module.modules.blitz.BlitzBuilder;
import in.twizmwaz.cardinal.module.modules.blockdrops.BlockdropsBuilder;
import in.twizmwaz.cardinal.module.modules.broadcasts.BroadcastModuleBuilder;
import in.twizmwaz.cardinal.module.modules.buildHeight.BuildHeightBuilder;
import in.twizmwaz.cardinal.module.modules.chat.ChatModuleBuilder;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannelModuleBuilder;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModuleBuilder;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjectiveBuilder;
import in.twizmwaz.cardinal.module.modules.deathMessages.DeathMessagesBuilder;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjectiveBuilder;
import in.twizmwaz.cardinal.module.modules.difficulty.MapDifficultyBuilder;
import in.twizmwaz.cardinal.module.modules.disableDamage.DisableDamageBuilder;
import in.twizmwaz.cardinal.module.modules.doubleKillPatch.DoubleKillPatchBuilder;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.friendlyFire.FriendlyFireBuilder;
import in.twizmwaz.cardinal.module.modules.gameComplete.GameCompleteBuilder;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameScoreboardBuilder;
import in.twizmwaz.cardinal.module.modules.gamerules.GamerulesBuilder;
import in.twizmwaz.cardinal.module.modules.hill.HillObjectiveBuilder;
import in.twizmwaz.cardinal.module.modules.hunger.HungerBuilder;
import in.twizmwaz.cardinal.module.modules.itemKeep.ItemKeepBuilder;
import in.twizmwaz.cardinal.module.modules.itemRemove.ItemRemoveBuilder;
import in.twizmwaz.cardinal.module.modules.killReward.KillRewardBuilder;
import in.twizmwaz.cardinal.module.modules.killStreakCount.KillStreakBuilder;
import in.twizmwaz.cardinal.module.modules.kit.KitBuilder;
import in.twizmwaz.cardinal.module.modules.mapNotification.MapNotificationBuilder;
import in.twizmwaz.cardinal.module.modules.match.MatchModuleBuilder;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimerBuilder;
import in.twizmwaz.cardinal.module.modules.mob.MobModuleBuilder;
import in.twizmwaz.cardinal.module.modules.monumentModes.MonumentModesBuilder;
import in.twizmwaz.cardinal.module.modules.motd.MOTDBuilder;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModuleBuilder;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.portal.PortalBuilder;
import in.twizmwaz.cardinal.module.modules.projectiles.ProjectilesBuilder;
import in.twizmwaz.cardinal.module.modules.proximityAlarm.ProximityAlarmBuilder;
import in.twizmwaz.cardinal.module.modules.rage.RageBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.respawn.RespawnModuleBuilder;
import in.twizmwaz.cardinal.module.modules.score.ScoreModuleBuilder;
import in.twizmwaz.cardinal.module.modules.scorebox.ScoreboxBuilder;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModuleBuilder;
import in.twizmwaz.cardinal.module.modules.tasker.TaskerModuleBuilder;
import in.twizmwaz.cardinal.module.modules.team.TeamModuleBuilder;
import in.twizmwaz.cardinal.module.modules.teamManager.TeamManagerModuleBuilder;
import in.twizmwaz.cardinal.module.modules.teamPicker.TeamPickerBuilder;
import in.twizmwaz.cardinal.module.modules.timeLock.TimeLockBuilder;
import in.twizmwaz.cardinal.module.modules.timeNotifications.TimeNotificationsBuilder;
import in.twizmwaz.cardinal.module.modules.tnt.TntBuilder;
import in.twizmwaz.cardinal.module.modules.tntDefuse.TntDefuseBuilder;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTrackerBuilder;
import in.twizmwaz.cardinal.module.modules.toolRepair.ToolRepairBuilder;
import in.twizmwaz.cardinal.module.modules.tracker.TrackerBuilder;
import in.twizmwaz.cardinal.module.modules.visibility.VisibilityBuilder;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjectiveBuilder;
import in.twizmwaz.cardinal.module.modules.worldFreeze.WorldFreezeBuilder;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ModuleFactory {

    private final Match match;
    private final List<Class<? extends ModuleBuilder>> builderClasses;
    private final List<ModuleBuilder> builders;

    @SuppressWarnings("unchecked")
    public ModuleFactory(Match match) {
        this.match = match;
        this.builderClasses = new ArrayList<>();
        this.builders = new ArrayList<>();
        registerBuilders();
        for (Class clazz : builderClasses) {
            try {
                builders.add((ModuleBuilder) clazz.getConstructor().newInstance());
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().log(Level.SEVERE, clazz.getName() + " is an invalid ModuleBuilder.");
                e.printStackTrace();
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public ModuleCollection<Module> build(ModuleLoadTime time) {
        ModuleCollection results = new ModuleCollection();
        for (ModuleBuilder builder : builders) {
            try {
                if (builder.getClass().getAnnotation(BuilderData.class).load().equals(time)) {
                    try {
                        results.addAll(builder.load(match));
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                if (time != ModuleLoadTime.NORMAL) ;
                else try {
                    results.addAll(builder.load(match));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        return results;
    }

    private void registerBuilders() {
        builderClasses.add(BuildHeightBuilder.class);
        builderClasses.add(WoolObjectiveBuilder.class);
        builderClasses.add(CoreObjectiveBuilder.class);
        builderClasses.add(DestroyableObjectiveBuilder.class);
        builderClasses.add(ItemRemoveBuilder.class);
        builderClasses.add(ToolRepairBuilder.class);
        builderClasses.add(DisableDamageBuilder.class);
        builderClasses.add(GamerulesBuilder.class);
        builderClasses.add(KitBuilder.class);
        builderClasses.add(TimeLockBuilder.class);
        builderClasses.add(FriendlyFireBuilder.class);
        builderClasses.add(HungerBuilder.class);
        builderClasses.add(MapDifficultyBuilder.class);
        builderClasses.add(HungerBuilder.class);
        builderClasses.add(ProjectilesBuilder.class);
        builderClasses.add(TntTrackerBuilder.class);
        builderClasses.add(VisibilityBuilder.class);
        builderClasses.add(MOTDBuilder.class);
        builderClasses.add(WorldFreezeBuilder.class);
        builderClasses.add(TeamManagerModuleBuilder.class);
        builderClasses.add(RespawnModuleBuilder.class);
        builderClasses.add(ObserverModuleBuilder.class);
        builderClasses.add(KillStreakBuilder.class);
        builderClasses.add(TeamPickerBuilder.class);
        builderClasses.add(GameScoreboardBuilder.class);
        builderClasses.add(TeamModuleBuilder.class);
        builderClasses.add(SpawnModuleBuilder.class);
        builderClasses.add(DeathMessagesBuilder.class);
        builderClasses.add(TntDefuseBuilder.class);
        builderClasses.add(ScoreModuleBuilder.class);
        builderClasses.add(GameCompleteBuilder.class);
        builderClasses.add(RegionModuleBuilder.class);
        builderClasses.add(DoubleKillPatchBuilder.class);
        builderClasses.add(TaskerModuleBuilder.class);
        builderClasses.add(MatchTimerBuilder.class);
        builderClasses.add(ItemKeepBuilder.class);
        builderClasses.add(ArmorKeepBuilder.class);
        builderClasses.add(BroadcastModuleBuilder.class);
        builderClasses.add(MatchModuleBuilder.class);
        builderClasses.add(TimeNotificationsBuilder.class);
        builderClasses.add(HillObjectiveBuilder.class);
        builderClasses.add(ChatModuleBuilder.class);
        builderClasses.add(MonumentModesBuilder.class);
        builderClasses.add(RageBuilder.class);
        builderClasses.add(BlitzBuilder.class);
        builderClasses.add(MapNotificationBuilder.class);
        builderClasses.add(FilterModuleBuilder.class);
        builderClasses.add(AppliedRegionBuilder.class);
        builderClasses.add(KillRewardBuilder.class);
        builderClasses.add(PortalBuilder.class);
        builderClasses.add(ClassModuleBuilder.class);
        builderClasses.add(TrackerBuilder.class);
        builderClasses.add(ScoreboxBuilder.class);
        builderClasses.add(BlockdropsBuilder.class);
        builderClasses.add(ProximityAlarmBuilder.class);
        builderClasses.add(PermissionModuleBuilder.class);
        builderClasses.add(ChatChannelModuleBuilder.class);
        builderClasses.add(TntBuilder.class);
        builderClasses.add(MobModuleBuilder.class);
    }
}
