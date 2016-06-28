package in.twizmwaz.cardinal.chat;

import in.twizmwaz.cardinal.Cardinal;
import org.bukkit.ChatColor;
import org.jdom2.Document;
import org.jdom2.Element;

public enum ChatConstant {

    ERROR_NO_MESSAGES("error.noMessages"),
    ERROR_RESTART_DURING_MATCH("error.restartDuringMatch"),
    ERROR_CYCLE_DURING_MATCH("error.cycleDuringMatch"),
    ERROR_MATCH_RUNNING("error.matchRunning"),
    ERROR_NO_RESUME("error.matchNoResume"),
    ERROR_NO_START("error.matchNoStart"),
    ERROR_NO_END("error.matchNoEnd"),
    ERROR_NO_SET_NEXT("error.noSetNext"),
    ERROR_DISABLED_COMMAND("error.disabledCommand"),
    ERROR_ROTATION_POINT_INVALID("error.rotPointInvalid"),
    ERROR_INVALID_TIME("error.invalidTime"),
    ERROR_TEAM_CHAT_DEFAULT("error.teamChatDefault"),
    ERROR_TEAM_CHANNEL("error.teamChannel"),
    ERROR_NO_CLASS("error.noClass"),
    ERROR_CLASS_RESTRICTED("error.classRestricted"),
    ERROR_NO_CLASS_CHANGE("error.noClassChange"),
    ERROR_CLASSES_DISABLED("error.classesDisabled"),
    ERROR_NO_TEAM("error.noTeam"),
    ERROR_ALREADY_OBSERVER("error.alreadyObs"),
    ERROR_ALREADY_JOINED("error.alreadyJoined"),
    ERROR_ALREADY_ON_TEAM("error.alreadyOnTeam"),
    ERROR_TEAM_FULL("error.teamFull"),
    ERROR_FORCE_EXEMPTION("error.forceExemption"),
    ERROR_NO_SHUFFLE("error.noShuffle"),
    ERROR_TEAM_EXISTS("error.teamExists"),
    ERROR_MAY_NOT_JOIN("error.mayNotJoin"),
    ERROR_MATCH_OVER("error.matchOver"),
    ERROR_CYCLE("error.cycle"),
    ERROR_PLAYER_COMMAND("error.playerCommand"),
    ERROR_NO_MAP_MATCH("error.noMapMatch"),
    ERROR_MULTIPLE_MAP_MATCH("error.multipleMapMatch"),
    ERROR_NO_TEAM_MATCH("error.noTeamMatch"),
    ERROR_MULTIPLE_TEAM_MATCH("error.multipleTeamMatch"),
    ERROR_NO_ROT_MATCH("error.noRotMatch"),
    ERROR_NO_PLAYER_MATCH("error.noPlayerMatch"),
    ERROR_NO_RANK_MATCH("error.noRankMatch"),
    ERROR_NO_SETTING_MATCH("error.noSettingMatch"),
    ERROR_NO_VALUE_MATCH("error.noValueMatch"),
    ERROR_NO_RESULT_MATCH("error.noResultMatch"),
    ERROR_NOT_ENOUGH_PLAYERS("error.notEnoughPlayers"),
    ERROR_MULTIPLE_PLAYERS("error.multiplePlayers"),
    ERROR_UNKNOWN_ERROR("error.unknownError"),
    ERROR_MOVED_FOR_BALANCE("error.movedForBalance"),
    ERROR_AUTO_JOIN_SWITCH("error.autoJoinSwitch"),
    ERROR_OWN_CORE("error.ownCore"),
    ERROR_OWN_OBJECTIVE("error.ownObjective"),
    ERROR_ENEMY_OBJECTIVE("error.enemyObjective"),
    ERROR_REPAIR_OBJECTIVE("error.repairObjective"),
    ERROR_NO_POTIONS("error.noPotions"),
    ERROR_TEAM_PLACE("error.teamPlace"),
    ERROR_BLOCK_PLACE("error.blockPlace"),
    ERROR_NO_CRAFT("error.noCraft"),
    ERROR_BEDS_DISABLED("error.bedsDisabled"),
    ERROR_PEARL_OUT("error.pearlOut"),
    ERROR_LANE_REENTER("error.laneReEnter"),
    ERROR_LANDMINE_PLACE("error.landminePlace"),
    ERROR_LANDMINE_EXISTS("error.landmineExists"),
    ERROR_LANDMINE_PROXIMITY("error.landmineProximity"),
    ERROR_TUTORIAL_TP("error.tutorialTp"),
    ERROR_DEFUSE_TNT_WATER("error.defuseTntWater"),
    ERROR_DEFUSE_TNT_ENEMY("error.defuseTntEnemy"),
    ERROR_NO_PERMISSION("error.noPermission"),
    ERROR_NUMBER_STRING("error.numberString"),
    ERROR_TIME_FORMAT_STRING("error.timeFormatString"),
    ERROR_REJOIN("error.errorRejoin"),
    ERROR_SERVER_FULL("error.serverFull"),
    ERROR_TEAMS_FULL("error.teamsFull"),
    ERROR_TEAMS_CAPACITY("error.teamsCapacity"),
    ERROR_INVALID_MATCH("error.invalidMatch"),
    ERROR_RATING_RANGE("error.ratingRange"),
    ERROR_RATINGS_DISABLED("error.ratingsDisabled"),
    ERROR_LOW_PARTICIPATION("error.lowParticipation"),
    ERROR_RATE_WHILE_PLAYING("error.rateWhilePlaying"),
    ERROR_NO_RECENT_PM("error.noRecentPM"),
    ERROR_PLAYER_NOT_FOUND("error.playerNotFound"),
    ERROR_PLAYER_ALREADY_BANNED("error.playerAlreadyBanned"),
    ERROR_PLAYER_ALREADY_MUTED("error.playerAlreadyMuted"),
    ERROR_PLAYER_NOT_MUTED("error.playerNotMuted"),
    ERROR_PLAYER_NOT_AFFECTED("error.playerNotAffected"),
    ERROR_PLAYER_DISABLED_PMS("error.playerDisabledPMs"),
    ERROR_PLAYABLE_LEAVE("error.playableLeave"),
    ERROR_PLAYABLE_INTERACT("error.playableInteract"),
    ERROR_GLOBAL_MUTE_ENABLED("error.globalMuteEnabled"),
    ERROR_NOT_ENOUGH_ARGS_BROADCAST("error.notEnoughArgsBroadcast"),
    ERROR_NOT_ENOUGH_ARGS_SAY("error.notEnoughArgsSay"),
    ERROR_TEAM_ABSENT("error.teamAbsent"),
    ERROR_ALREADY_HAS_RANK("error.alreadyHasRank"),
    ERROR_ALREADY_DOESNT_HAVE_RANK("error.alreadyDoesntHaveRank"),
    ERROR_GLOBAL_ALREADY_DEAFULT("error.globalAlreadyDefault"),
    ERROR_TEAM_ALREADY_DEAFULT("error.teamAlreadyDefault"),
    ERROR_ADMIN_ALREADY_DEAFULT("error.adminAlreadyDefault"),
    ERROR_CONSOLE_NO_USE("error.consoleNoUse"),
    ERROR_READY_BEFORE_MATCH("error.readyBeforeMatch"),
    ERROR_UNREADY_BEFORE_MATCH("error.unreadyBeforeMatch"),
    ERROR_TEAM_ALREADY_READY("error.teamAlreadyReady"),
    ERROR_TEAM_ALREADY_NOT_READY("error.teamAlreadyNotReady"),
    ERROR_TEAM_CAN_NOT_UNREADY("error.teamCanNotUnready"),
    ERROR_INVALID_PAGE_NUMBER("error.invalidPageNumber"),
    ERROR_INVALID_ARGUMENTS("error.invalidArguments"),
    ERROR_TOO_FEW_ARGUMENTS("error.tooFewArguments"),
    ERROR_CANNOT_CALCULATE_SCORES("error.cannotCalculateScores"),
    ERROR_PROXIMITY_OBS_ONLY("error.proximityObsOnly"),
    ERROR_PROXIMITY_NO_SCORING("error.proximityNoScoring"),
    ERROR_INVENTORY_NOT_VIEWABLE("error.inventoryNotViewable"),
    ERROR_PLAYER_ALREADY_FROZEN("error.playerAlreadyFrozen"),
    ERROR_PLAYER_NOT_FROZEN("error.playerNotFrozen"),
    ERROR_POLL_NEED_ID("error.pollNeedId"),
    ERROR_POLL_NO_POLLS("error.pollNoPolls"),
    ERROR_POLL_NO_SUCH_POLL("error.pollNoSuchPoll"),
    ERROR_POLL_ALREADY_VOTED("error.pollAlreadyVoted"),
    ERROR_POLL_USAGE("error.pollUsage"),
    ERROR_BREAK_FLAG("error.breakFlag"),
    ERROR_BREAK_BLOCK_UNDER_FLAG("error.breakBlockUnderFlag"),
    ERROR_NO_TEAMS("error.noTeams"),
    ERROR_NO_PLAYERS("error.noPlayers"),

    GENERIC_MAP_SET("generic.mapSet"),
    GENERIC_MARKED_FOR_RELOADING("generic.markedForReloading"),
    GENERIC_ALL_MARKED_RELOADING("generic.allMarkedReloading"),
    GENERIC_COUNTDOWN_CANELLED("generic.countdownCancelled"),
    GENERIC_AUTO_START("generic.autoStart"),
    GENERIC_AUTO_START_DISABLED("generic.autoStartDisabled"),
    GENERIC_SKIPPED_OVER("generic.skippedOver"),
    GENERIC_SKIPPED_FROM_TO("generic.skippedFromTo"),
    GENERIC_CONFIG_RELOAD("generic.configReload"),
    GENERIC_RANKS_RELOAD("generic.ranksReload"),
    GENERIC_REPO_RELOAD("generic.repoReload"),
    GENERIC_REPO_RELOAD_FAIL("generic.repoReloadFailed"),
    GENERIC_MAP_NEXT("generic.mapNext"),
    GENERIC_ROTATION_SET("generic.rotationSet"),
    GENERIC_ROTATION_RELOAD("generic.rotationReload"),
    GENERIC_ROTATION_APPEND("generic.rotationAppend"),
    GENERIC_INSTERTED_INDEX("generic.insertedIndex"),
    GENERIC_REMOVED_INSTANCE("generic.removedInstance"),
    GENERIC_REMOVED_MAP("generic.removedMap"),
    GENERIC_PLAYER_FORCE("generic.playerForce"),
    GENERIC_TEAM_SHUFFLE("generic.teamShuffle"),
    GENERIC_TEAM_ALIAS("generic.teamAlias"),
    GENERIC_CHANNEL_SET("generic.channelSet"),
    GENERIC_MESSAGE_SENT("generic.messageSent"),
    GENERIC_ON_TEAM("generic.onTeam"),
    GENERIC_HAVE_SELECTED("generic.haveSelected"),
    GENERIC_CHANGE_ON_SPAWN("generic.changeOnSpawn"),
    GENERIC_CLASS_CURRENT("generic.classCurrent"),
    GENERIC_CLASS_LIST("generic.classList"),
    GENERIC_ERRORS_CLEARED("generic.errorsCleared"),
    GENERIC_NO_ERRORS("generic.noErrors"),
    GENERIC_JOINED("generic.joined"),
    GENERIC_WORLD_DEFUSE("generic.worldDefuse"),
    GENERIC_DEFUSE_PLAYER_TNT("generic.defusePlayerTnt"),
    GENERIC_LANDMINE_PLANTED("generic.landminePlanted"),
    GENERIC_RATING_SAVED("generic.ratingSaved"),
    GENERIC_RATING_CHANGE("generic.ratingChange"),
    GENERIC_MAP_RATED("generic.mapRated"),
    GENERIC_MAP_RE_RATED("generic.mapReRated"),
    GENERIC_AUTO_BALANCE("generic.autoBalance"),
    GENERIC_YOU_CHANGED_RATING("generic.youChangedRating"),
    GENERIC_TEAM_SIZE_CHANGED("generic.teamSizeChanged"),
    GENERIC_RANK_REMOVED("generic.rankRemoved"),
    GENERIC_OWN_RANK_REMOVED("generic.ownRankRemoved"),
    GENERIC_RANK_GIVEN("generic.rankGiven"),
    GENERIC_OWN_RANK_GIVEN("generic.ownRankGiven"),
    GENERIC_NO_RANKS("generic.noRanks"),
    GENERIC_RANKS("generic.ranks"),
    GENERIC_RANK_INFO("generic.rankInfo"),
    GENERIC_RANKS_MORE_INFO("generic.ranksMoreInfo"),
    GENERIC_SELF_RANK_GIVEN("generic.selfRankGiven"),
    GENERIC_SELF_RANK_REMOVED("generic.selfRankRemoved"),
    GENERIC_TELEPORTED("generic.teleported"),
    GENERIC_TELEPORTED_BY("generic.teleportedBy"),
    GENERIC_TEAM_NOW_READY("generic.teamNowReady"),
    GENERIC_TEAM_NO_LONGER_READY("generic.teamNoLongerReady"),
    GENERIC_UNREADY_CANCEL_COUNTDOWN("generic.unreadyCancelCountdown"),
    GENERIC_TIME_LIMIT_CANCELLED("generic.timeLimitCancelled"),
    GENERIC_NO_TIME_LIMIT("generic.noTimeLimit"),
    GENERIC_TIME_LIMIT_WITH_RESULT("generic.timeLimitWithResult"),
    GENERIC_WHITELIST_RELOADED("generic.whitelistReloaded"),
    GENERIC_WHITELIST_ENABLED("generic.whitelistEnabled"),
    GENERIC_WHITELIST_DISABLED("generic.whitelistDisabled"),
    GENERIC_PLAYER_ADD_WHITELIST("generic.playerAddWhitelist"),
    GENERIC_PLAYER_REMOVE_WHITELIST("generic.playerRemoveWhitelist"),
    GENERIC_ADDED_PLAYERS_WHITELIST("generic.addedPlayersWhitelist"),
    GENERIC_REMOVED_PLAYERS_WHITELIST("generic.removedPlayersWhitelist"),
    GENERIC_KICKED_NOT_WHITELISTED("generic.kickedNotWhitelisted"),
    GENERIC_NO_WHITELISTED_PLAYERS("generic.noWhitelistedPlayers"),
    GENERIC_WHITELISTED_PLAYERS("generic.whitelistedPlayers"),
    GENERIC_PLAYER_ONLINE("generic.playerOnline"),
    GENERIC_PLAYERS_ONLINE("generic.playersOnline"),
    GENERIC_WHITELISTED_PLAYER_ONLINE("generic.whitelistedPlayerOnline"),
    GENERIC_WHITELISTED_PLAYERS_ONLINE("generic.whitelistedPlayersOnline"),
    GENERIC_WHITELISTED_PLAYER_OUT_OF_ONLINE("generic.whitelistedPlayerOutOfOnline"),
    GENERIC_WHITELISTED_PLAYERS_OUT_OF_ONLINE("generic.whitelistedPlayersOutOfOnline"),
    GENERIC_MONUMENT_MODES("generic.monumentModes"),
    GENERIC_NEXT_MODE("generic.nextMode"),
    GENERIC_MODES_PUSHED_FORWARDS("generic.modesPushedForwards"),
    GENERIC_MODES_PUSHED_BACKWARDS("generic.modesPushedBackwards"),
    GENERIC_CALCULATING_SCORES_FOR("generic.calculatingScoresFor"),
    GENERIC_TEAMS_ARE_TIED("generic.teamsAreTied"),
    GENERIC_TEAMS_ARE_TIED_WITH_POINTS("generic.teamsAreTiedWithPoints"),
    GENERIC_TEAMS_ARE_TIED_WITH_PLAYERS("generic.teamsAreTiedWithPlayers"),
    GENERIC_TEAM_IS_WINNING("generic.teamIsWinning"),
    GENERIC_TEAM_IS_WINNING_WITH_POINTS("generic.teamIsWinningWithPoints"),
    GENERIC_TEAM_IS_WINNING_WITH_PLAYERS("generic.teamIsWinningWithPlayers"),
    GENERIC_TEAM_IS_WINNING_WITH_OBJECTIVES("generic.teamIsWinningWithObjectives"),
    GENERIC_OBJECTIVE_IS_UNTOUCHED("generic.objectiveIsUntouched"),
    GENERIC_OBJECTIVE_WAS_TOUCHED("generic.objectiveWasTouched"),
    GENERIC_OBJECTIVE_WAS_COMPLETED("generic.objectiveWasCompleted"),
    GENERIC_MUTED("generic.muted"),
    GENERIC_MUTED_BY("generic.mutedBy"),
    GENERIC_UNMUTED("generic.unmuted"),
    GENERIC_UNMUTED_BY("generic.unmutedBy"),
    GENERIC_CREATED_BY("generic.createdBy"),
    GENERIC_FROZEN_BY("generic.frozenBy"),
    GENERIC_UNFROZEN_BY("generic.unfrozenBy"),
    GENERIC_FROZE("generic.froze"),
    GENERIC_UNFROZE("generic.unfroze"),
    GENERIC_POLL_VOTED("generic.pollVoted"),
    GENERIC_POLL_VOTED_AGAINST("generic.pollVotedAgainst"),
    GENERIC_POLL_SUCCEEDED("generic.pollSucceeded"),
    GENERIC_POLL_FAILED("generic.pollFailed"),
    GENERIC_POLL_VETOED("generic.pollVetoed"),
    GENERIC_CHECKING_UPDATES("generic.checkingUpdates"),
    GENERIC_ALREADY_DOWNLOADED("generic.alreadyDownloaded"),
    GENERIC_DOWNLOAD_STARTING("generic.downloadStarting"),
    GENERIC_DOWNLOAD_READY("generic.downloadReady"),
    GENERIC_DOWNLOAD_FAILED("generic.downloadFailed"),

    MISC_ENEMY("misc.enemy"),
    MISC_FATE("misc.fate"),
    MISC_OTHERS("misc.others"),
    MISC_BLOCKS("misc.blocks"),
    MISC_OWNERSHIP("misc.ownership"),
    MISC_AND("misc.and"),
    MISC_BY("misc.by"),
    MISC_TEAM("misc.team"),
    MISC_MATCH("misc.match"),
    MISC_YOU_HAVE("misc.youHave"),
    MISC_HAS("misc.has"),
    MISC_ONLINE("misc.online"),
    MISC_OFFLINE("misc.offline"),
    MISC_SCORE("misc.score"),
    MISC_NAME("misc.name"),
    MISC_PARENT("misc.parent"),
    MISC_STAFF("misc.staff"),
    MISC_DEFAULT("misc.default"),
    MISC_PERMISSIONS("misc.permissions"),
    MISC_PLAYERS("misc.players"),

    UI_MAPLOADED("userInterface.mapLoaded"),
    UI_REPOLOADED("userInterface.repoLoaded"),
    UI_MAP_OBJECTIVE("userInterface.mapObjective"),
    UI_MAP_AUTHOR("userInterface.mapAuthor"),
    UI_MAP_AUTHORS("userInterface.mapAuthors"),
    UI_MAP_CONTRIBUTORS("userInterface.mapContributors"),
    UI_MAP_RULES("userInterface.mapRules"),
    UI_MAP_MAX("userInterface.mapMax"),
    UI_ROTATION_CURRENT("userInterface.rotationCurrent"),
    UI_MATCH_INFO("userInterface.matchInfo"),
    UI_MATCH("userInterface.match"),
    UI_TIME("userInterface.time"),
    UI_MATCH_TIME("userInterface.matchTime"),
    UI_GOALS("userInterface.goals"),
    UI_CLASSES("userInterface.classes"),
    UI_OF("userInterface.of"),
    UI_XML_ERRORS("userInterface.xmlErrors"),
    UI_CLOSE("userInterface.close"),
    UI_TEAM_CLASS_SELECTION("userInterface.teamClassSelection"),
    UI_TEAM_JOIN_TIP("userInterface.teamJoinTip"),
    UI_TEAM_PICK("userInterface.teamPick"),
    UI_TEAM_JOIN_AUTO("userInterface.teamJoinAuto"),
    UI_TEAM_JOIN_AUTO_LORE("userInterface.teamJoinAutoLore"),
    UI_TEAM_LEAVE("userInterface.teamLeave"),
    UI_TEAM_LEAVE_LORE("userInterface.teamLeaveLore"),
    UI_TEAM_CAPACITY("userInterface.teamCapacity"),
    UI_TEAM_CAN_PICK("userInterface.teamCanPick"),
    UI_TEAM_SELECTION("userInterface.teamSelection"),
    UI_TUTORIAL_VIEW("userInterface.tutorialView"),
    UI_TUTORIAL_LORE("userInterface.tutorialLore"),
    UI_POTION_EFFECTS("userInterface.potionEffects"),
    UI_NO_POTION_EFFECTS("userInterface.noPotionEffects"),
    UI_HUNGER_LEVEL("userInterface.hungerLevel"),
    UI_HEALTH_LEVEL("userInterface.healthLevel"),
    UI_ONE_MAP("userInterface.oneMap"),
    UI_MAPS("userInterface.maps"),
    UI_SECOND("userInterface.second"),
    UI_SECONDS("userInterface.seconds"),
    UI_MATCH_STARTED("userInterface.matchStarted"),
    UI_MATCH_START_TITLE("userInterface.matchStartTitle"),
    UI_MATCH_START_CANCELLED("userInterface.matchStartCancelled"),
    UI_MATCH_OVER("userInterface.matchOver"),
    UI_MATCH_WIN("userInterface.matchWin"),
    UI_MATCH_WINS("userInterface.matchWins"),
    UI_MATCH_TEAM_WIN("userInterface.matchTeamWin"),
    UI_MATCH_TEAM_LOSE("userInterface.matchTeamLose"),
    UI_MATCH_MAX_SCORE_REACHED("userInterface.matchMaxScoreReached"),
    UI_PLAYER_JOIN("userInterface.playerJoin"),
    UI_PLAYER_LEAVE("userInterface.playerLeave"),
    UI_SERVER_RESTART("userInterface.serverRestart"),
    UI_MAP_PLAYING("userInterface.mapPlaying"),
    UI_OBJECTIVE_PLACED("userInterface.objectivePlaced"),
    UI_OBJECTIVE_LEAKED("userInterface.objectiveLeaked"),
    UI_OBJECTIVE_DESTROYED("userInterface.objectiveDestroyed"),
    UI_OBJECTIVE_TOUCHED_FOR("userInterface.objectiveTouchedFor"),
    UI_OBJECTIVE_TOUCHED("userInterface.objectiveTouched"),
    UI_OBJECTIVE_DAMAGED_FOR("userInterface.objectiveDamagedFor"),
    UI_OBJECTIVE_DAMAGED("userInterface.objectiveDamaged"),
    UI_CREDIT_RECEIVED("userInterface.creditReceived"),
    UI_OBJECTIVE_PICKED_FOR("userInterface.objectivePickedFor"),
    UI_OBJECTIVE_PICKED("userInterface.objectivePicked"),
    UI_SCORED_FOR("userInterface.scoredFor"),
    UI_SCORED("userInterface.scored"),
    UI_ONE_POINT("userInterface.onePoint"),
    UI_POINTS("userInterface.points"),
    UI_AMOUNT_REMAINING("userInterface.amountRemaining"),
    UI_ONE_LIFE("userInterface.oneLife"),
    UI_LIVES("userInterface.lives"),
    UI_TIMER("userInterface.timer"),
    UI_LEFT_LANE("userInterface.leftLane"),
    UI_SCORES("userInterface.scores"),
    UI_TYPE_FOR_MAP_INFO("userInterface.typeForMapInfo"),
    UI_CYCLING_TIMER("userInterface.cyclingTimer"),
    UI_CYCLED_TO("userInterface.cycledTo"),
    UI_MATCH_STARTING_IN("userInterface.matchStartingIn"),
    UI_SERVER_RESTARTING_IN("userInterface.serverRestartingIn"),
    UI_TNT_DEFUSER("userInterface.tntDefuser"),
    UI_RATE_MAP("userInterface.rateMap"),
    UI_TERRIBLE("userInterface.terrible"),
    UI_BAD("userInterface.bad"),
    UI_OK("userInterface.ok"),
    UI_GOOD("userInterface.good"),
    UI_AMAZING("userInterface.amazing"),
    UI_TEAM_CHOOSE("userInterface.teamChoose"),
    UI_ONLINE("userInterface.online"),
    UI_LATEST_VERSION("userInterface.latestVersion"),
    UI_UPDATE_AVAILABLE("userInterface.updateAvailable"),
    UI_VERSION("userInterface.version"),
    UI_JAVA_VERSION("userInterface.java-version"),
    UI_JAVA_UPDATE("userInterface.java-update"),
    UI_MATCH_REPORT_UPLOAD("userInterface.matchReportUpload"),
    UI_MATCH_REPORT_SUCCESS("userInterface.matchReportSuccess"),
    UI_MATCH_REPORT_FAILED("userInterface.matchReportFailed"),
    UI_GLOBAL_MUTE_ENABLED("userInterface.globalMuteEnabled"),
    UI_GLOBAL_MUTE_DISABLED("userInterface.globalMuteDisabled"),
    UI_DEFAULT_CHANNEL_GLOBAL("userInterface.defaultChannelGlobal"),
    UI_DEFAULT_CHANNEL_ADMIN("userInterface.defaultChannelAdmin"),
    UI_DEFAULT_CHANNEL_TEAM("userInterface.defaultChannelTeam"),
    UI_TIME_ELAPSED("userInterface.timeElapsed"),
    UI_MODE_IN_TIME("userInterface.modeInTime"),
    UI_COMPASS("userInterface.compass"),
    UI_WAITING_PLAYER("userInterface.waitingPlayer"),
    UI_WAITING_PLAYERS("userInterface.waitingPlayers"),
    UI_FREEZE_ITEM("userInterface.freezeItem"),
    UI_POLL_BOSSBAR("userInterface.pollBossbar"),
    UI_FLAG_CAPTURED("userInterface.flagCaptured"),
    UI_FLAG_PICKED_UP("userInterface.flagPickedUp"),
    UI_FLAG_DROPPED("userInterface.flagDropped"),
    UI_FLAG_RESPAWNED("userInterface.flagRespawned"),
    UI_STATS_DISPLAY("userInterface.statsDisplay"),
    UI_SPECTATOR_TOOLS("userInterface.spectatorTools"),
    UI_SPECTATOR_TOOLS_LORE("userInterface.spectatorToolsLore"),
    UI_TEAMS("userInterface.teams"),
    UI_TELEPORT_TO_TEAM_MEMBER("userInterface.teleportToTeamMember"),
    UI_TELEPORT_TO_TEAM_MEMBER_LORE("userInterface.teleportToTeamMemberLore"),
    UI_TOGGLE_OBSERVERS("userInterface.toggleObservers"),
    UI_TOGGLE_OBSERVERS_LORE("userInterface.toggleObserversLore"),
    UI_TOGGLE_ELYTRA("userInterface.toggleElytra"),
    UI_TOGGLE_ELYTRA_LORE("userInterface.toggleElytraLore"),
    UI_CHANGE_EFFECTS("userInterface.changeEffects"),
    UI_CHANGE_EFFECTS_LORE("userInterface.changeEffectsLore"),
    UI_TOGGLE_GAMEMODE("userInterface.toggleGamemode"),
    UI_TOGGLE_GAMEMODE_LORE("userInterface.toggleGamemodeLore"),
    UI_TOGGLE_SPEED("userInterface.toggleSpeed"),
    UI_TOGGLE_SPEED_LORE("userInterface.toggleSpeedLore"),
    UI_TOGGLE_NIGHT_VISION("userInterface.toggleNightVision"),
    UI_TOGGLE_NIGHT_VISION_LORE("userInterface.toggleNightVisionLore"),
    UI_GO_BACK("userInterface.goBack"),
    UI_GO_BACK_LORE("userInterface.goBackLore"),

    UI_DEATH_RESPAWN_UNCONFIRMED("userInterface.deathRespawnUnconfirmed"),
    UI_DEATH_RESPAWN_UNCONFIRMED_TIME("userInterface.deathRespawnUnconfirmedTime"),
    UI_DEATH_RESPAWN_CONFIRMED_TIME("userInterface.deathRespawnConfirmedTime"),
    UI_DEATH_RESPAWN_CONFIRMED_WAITING("userInterface.deathRespawnConfirmedWaiting"),
    UI_DEATH_RESPAWN_CONFIRMED_WAITING_FLAG_DROPPED("userInterface.deathRespawnConfirmedWaitingFlagDropped"),

    SNOWFLAKES_SNOWFLAKE("snowflakes.snowflake"),
    SNOWFLAKES_SNOWFLAKES("snowflakes.snowflakes");

    private final String path;

    ChatConstant(String path) {
        this.path = path;
    }

    public static ChatConstant fromPath(String path) {
        if (path != null) {
            path = path.replace(".","");
            path = "userInterface." + path;
            for (ChatConstant chatConstant : ChatConstant.values()) {
                if (path.equalsIgnoreCase(chatConstant.path)) {
                    return chatConstant;
                }
            }
        }
        return null;
    }

    public String getMessage(String locale) {
        Document localized = Cardinal.getLocaleHandler().getLocaleDocument(locale.split("_")[0]);
        String message = null;
        Element work = localized.getRootElement();
        try {
            for (String element : this.path.split("\\.")) {
                work = work.getChild(element);
                message = work.getTextNormalize();
            }
        } catch (NullPointerException e) {
            message = getMessage("en_US");
        }
        return message;
    }

    public ChatMessage asMessage(ChatMessage... messages) {
        return new LocalizedChatMessage(this, messages);
    }

    public ChatMessage asMessage(ChatColor color, ChatMessage... messages) {
        return new UnlocalizedChatMessage(color + "{0}", asMessage(messages));
    }

}
