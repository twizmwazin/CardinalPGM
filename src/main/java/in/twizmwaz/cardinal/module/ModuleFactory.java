package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegionBuilder;
import in.twizmwaz.cardinal.module.modules.armorKeep.ArmorKeepBuilder;
import in.twizmwaz.cardinal.module.modules.blitz.BlitzBuilder;
import in.twizmwaz.cardinal.module.modules.blockdrops.BlockdropsBuilder;
import in.twizmwaz.cardinal.module.modules.bookModule.BookBuilder;
import in.twizmwaz.cardinal.module.modules.bossBar.BossBarBuilder;
import in.twizmwaz.cardinal.module.modules.broadcasts.BroadcastModuleBuilder;
import in.twizmwaz.cardinal.module.modules.buildHeight.BuildHeightBuilder;
import in.twizmwaz.cardinal.module.modules.chat.ChatModuleBuilder;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannelModuleBuilder;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModuleBuilder;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjectiveBuilder;
import in.twizmwaz.cardinal.module.modules.cycleTimer.CycleTimerBuilder;
import in.twizmwaz.cardinal.module.modules.deathMessages.DeathMessagesBuilder;
import in.twizmwaz.cardinal.module.modules.deathTracker.DeathTrackerBuilder;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjectiveBuilder;
import in.twizmwaz.cardinal.module.modules.difficulty.MapDifficultyBuilder;
import in.twizmwaz.cardinal.module.modules.disableDamage.DisableDamageBuilder;
import in.twizmwaz.cardinal.module.modules.doubleKillPatch.DoubleKillPatchBuilder;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.friendlyFire.FriendlyFireBuilder;
import in.twizmwaz.cardinal.module.modules.gameComplete.GameCompleteBuilder;
import in.twizmwaz.cardinal.module.modules.gamerules.GamerulesBuilder;
import in.twizmwaz.cardinal.module.modules.header.HeaderModuleBuilder;
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
import in.twizmwaz.cardinal.module.modules.matchTranscript.MatchTranscriptBuilder;
import in.twizmwaz.cardinal.module.modules.mob.MobModuleBuilder;
import in.twizmwaz.cardinal.module.modules.monumentModes.MonumentModesBuilder;
import in.twizmwaz.cardinal.module.modules.motd.MOTDBuilder;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModuleBuilder;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.playable.PlayableBuilder;
import in.twizmwaz.cardinal.module.modules.portal.PortalBuilder;
import in.twizmwaz.cardinal.module.modules.potionRemover.PotionRemoverBuilder;
import in.twizmwaz.cardinal.module.modules.projectiles.ProjectilesBuilder;
import in.twizmwaz.cardinal.module.modules.proximityAlarm.ProximityAlarmBuilder;
import in.twizmwaz.cardinal.module.modules.rage.RageBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.respawn.RespawnModuleBuilder;
import in.twizmwaz.cardinal.module.modules.score.ScoreModuleBuilder;
import in.twizmwaz.cardinal.module.modules.scoreboard.ScoreboardModuleBuilder;
import in.twizmwaz.cardinal.module.modules.scorebox.ScoreboxBuilder;
import in.twizmwaz.cardinal.module.modules.snowflakes.SnowflakesBuilder;
import in.twizmwaz.cardinal.module.modules.sound.SoundModuleBuilder;
import in.twizmwaz.cardinal.module.modules.spawn.SpawnModuleBuilder;
import in.twizmwaz.cardinal.module.modules.startTimer.StartTimerBuilder;
import in.twizmwaz.cardinal.module.modules.stats.StatsBuilder;
import in.twizmwaz.cardinal.module.modules.tasker.TaskerModuleBuilder;
import in.twizmwaz.cardinal.module.modules.team.TeamModuleBuilder;
import in.twizmwaz.cardinal.module.modules.teamManager.TeamManagerModuleBuilder;
import in.twizmwaz.cardinal.module.modules.teamPicker.TeamPickerBuilder;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimitBuilder;
import in.twizmwaz.cardinal.module.modules.timeLock.TimeLockBuilder;
import in.twizmwaz.cardinal.module.modules.timeNotifications.TimeNotificationsBuilder;
import in.twizmwaz.cardinal.module.modules.tnt.TntBuilder;
import in.twizmwaz.cardinal.module.modules.tntDefuse.TntDefuseBuilder;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTrackerBuilder;
import in.twizmwaz.cardinal.module.modules.toolRepair.ToolRepairBuilder;
import in.twizmwaz.cardinal.module.modules.tracker.TrackerBuilder;
import in.twizmwaz.cardinal.module.modules.tutorial.TutorialBuilder;
import in.twizmwaz.cardinal.module.modules.updateNotification.UpdateNotificationBuilder;
import in.twizmwaz.cardinal.module.modules.visibility.VisibilityBuilder;
import in.twizmwaz.cardinal.module.modules.wildcard.WildCardBuilder;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjectiveBuilder;
import in.twizmwaz.cardinal.module.modules.worldFreeze.WorldFreezeBuilder;

import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ModuleFactory {

    private final List<ModuleBuilder> builders;

    private static Class[] builderClasses = {
            BuildHeightBuilder.class,
            WoolObjectiveBuilder.class,
            CoreObjectiveBuilder.class,
            DestroyableObjectiveBuilder.class,
            ItemRemoveBuilder.class,
            ToolRepairBuilder.class,
            DisableDamageBuilder.class,
            GamerulesBuilder.class,
            KitBuilder.class,
            TimeLockBuilder.class,
            FriendlyFireBuilder.class,
            HungerBuilder.class,
            MapDifficultyBuilder.class,
            HungerBuilder.class,
            ProjectilesBuilder.class,
            TntTrackerBuilder.class,
            VisibilityBuilder.class,
            MOTDBuilder.class,
            WorldFreezeBuilder.class,
            TeamManagerModuleBuilder.class,
            RespawnModuleBuilder.class,
            ObserverModuleBuilder.class,
            KillStreakBuilder.class,
            TeamPickerBuilder.class,
            ScoreboardModuleBuilder.class,
            TeamModuleBuilder.class,
            SpawnModuleBuilder.class,
            DeathMessagesBuilder.class,
            TntDefuseBuilder.class,
            ScoreModuleBuilder.class,
            GameCompleteBuilder.class,
            RegionModuleBuilder.class,
            DoubleKillPatchBuilder.class,
            TaskerModuleBuilder.class,
            MatchTimerBuilder.class,
            ItemKeepBuilder.class,
            ArmorKeepBuilder.class,
            BroadcastModuleBuilder.class,
            MatchModuleBuilder.class,
            TimeNotificationsBuilder.class,
            HillObjectiveBuilder.class,
            ChatModuleBuilder.class,
            MonumentModesBuilder.class,
            RageBuilder.class,
            BlitzBuilder.class,
            MapNotificationBuilder.class,
            FilterModuleBuilder.class,
            AppliedRegionBuilder.class,
            KillRewardBuilder.class,
            PortalBuilder.class,
            ClassModuleBuilder.class,
            TrackerBuilder.class,
            ScoreboxBuilder.class,
            BlockdropsBuilder.class,
            ProximityAlarmBuilder.class,
            PermissionModuleBuilder.class,
            ChatChannelModuleBuilder.class,
            TntBuilder.class,
            MobModuleBuilder.class,
            DeathTrackerBuilder.class,
            SnowflakesBuilder.class,
            SoundModuleBuilder.class,
            StartTimerBuilder.class,
            StatsBuilder.class,
            HeaderModuleBuilder.class,
            CycleTimerBuilder.class,
            TimeLimitBuilder.class,
            PlayableBuilder.class,
            TutorialBuilder.class,
            WildCardBuilder.class,
            BossBarBuilder.class,
            UpdateNotificationBuilder.class,
            MatchTranscriptBuilder.class,
            PotionRemoverBuilder.class,
            BookBuilder.class
    };

    @SuppressWarnings("unchecked")
    public ModuleFactory() {
        this.builders = new ArrayList<>();
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
    public ModuleCollection<Module> build(Match match, ModuleLoadTime time) {
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

    public void registerBuilder(ModuleBuilder builder) {
        builders.add(builder);
    }

    public void unregisterBuilder(ModuleBuilder builder) {
        builders.remove(builder);
    }

}
