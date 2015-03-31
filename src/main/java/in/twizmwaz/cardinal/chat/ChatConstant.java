package in.twizmwaz.cardinal.chat;

import in.twizmwaz.cardinal.Cardinal;
import org.jdom2.Document;
import org.jdom2.Element;

public enum  ChatConstant {

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
    ERROR_TEAM_FULL("error.teamFull"),
    ERROR_FORCE_EXEMPTION("error.forceExemption"),
    ERROR_NO_SHUFFLE("error.noShuffle"),
    ERROR_TEAM_EXISTS("error.teamExists"),
    ERROR_MAY_NOT_JOIN("error.mayNotJoin"),
    ERROR_MATCH_OVER("error.matchOver"),
    ERROR_CYCLE("error.cycle"),
    ERROR_PLAYER_COMMAND("error.playerCommand"),
    ERROR_NO_MAP_MATCH("error.noMapMatch"),
    ERROR_NO_TEAM_MATCH("error.noTeamMatch"),
    ERROR_NO_ROT_MATCH("error.noRotMatch"),
    ERROR_NO_PLAYER_MATCH("error.noPlayerMatch"),
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
    ERROR_PEARL_OUT("error.bedsDisabled"),
    ERROR_LANE_REENTER("error.laneReEnter"),
    ERROR_LANDMINE_PLACE("error.landminePlace"),
    ERROR_LANDMINE_EXISTS("error.landmineExists"),
    ERROR_LANDMINE_PROXIMITY("error.landmineProximity"),
    ERROR_TUTORIAL_TP("error.tutorialTp"),
    ERROR_DEFUSE_TNT_WATER("error.defuseTntWater"),
    ERROR_DEFUSE_TNT_ENEMY("error.defuseTntEnemy"),
    ERROR_NO_PERMISSION("error.noPermission"),
    ERROR_NUMBER_STRING("error.numberString"),
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
    ERROR_PLAYER_DISABLED_PMS("error.playerDisabledPMs"),
    ERROR_PLAYABLE_LEAVE("error.playableLeave"),
    ERROR_PLAYABLE_INTERACT("error.playableInteract"),
    
    GENERIC_MAP_SET("generic.mapSet"),
    GENERIC_MARKED_FOR_RELOADING("generic.markedForReloading"),
    GENERIC_ALL_MARKED_RELOADING("generic.allMarkedReloading"),
    GENERIC_COUNTDOWN_CANELLED("generic.countdownCancelled"),
    GENERIC_AUTO_START("generic.autoStart"),
    GENERIC_AUTO_START_DISABLED("generic.autoStartDisabled"),
    GENERIC_SKIPPED_OVER("generic.skippedOver"),
    GENERIC_SKIPPED_FROM_TO("generic.skippedFromTo"),
    GENERIC_CONFIG_RELOAD("generic.configReload"),
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
    
    MISC_ENEMY("misc.enemy"),
    MISC_FATE("misc.fate"),
    MISC_OTHERS("misc.others"),
    MISC_BLOCKS("misc.blocks"),
    MISC_OWNERSHIP("misc.ownership"),
    MISC_AND("misc.and"),
    MISC_BY("misc.by"),
    MISC_TEAM("misc.team"),
    
    UI_MAPLOADED("userInterface.mapLoaded"),
    UI_MAP_OBJECTIVE("userInterface.mapObjective"),
    UI_MAP_AUTHOR("userInterface.mapAuthor"),
    UI_MAP_AUTHORS("userInterface.mapAuthors"),
    UI_MAP_CONTRIBUTORS("userInterface.mapContributors"),
    UI_MAP_RULES("userInterface.mapRules"),
    UI_MAP_MAX("userInterface.mapMax"),
    UI_ROTATION_CURRENT("userInterface.rotationCurrent"),
    UI_ROTATION_LOADED("userInterface.rotationLoaded"),
    UI_MATCH_INFO("userInterface.matchInfo"),
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
    UI_TEAM_CAPACITY("userInterface.teamCapacity"),
    UI_TEAM_CAN_PICK("userInterface.teamCanPick"),
    UI_TEAM_SELECTION("userInterface.teamSelection"),
    UI_TUTORIAL_VIEW("userInterface.tutorialView"),
    UI_TUTORIAL_LORE("userInterface.tutorialLore"),
    UI_POTION_EFFECTS("userInterface.potionEffects"),
    UI_HUNGER_LEVEL("userInterface.hungerLevel"),
    UI_HEALTH_LEVEL("userInterface.healthLevel"),
    UI_ONE_MAP("userInterface.oneMap"),
    UI_MAPS("userInterface.maps"),
    UI_SECOND("userInterface.second"),
    UI_SECONDS("userInterface.seconds"),
    UI_MATCH_STARTED("userInterface.matchStarted"),
    UI_MATCH_START_CANCELLED("userInterface.matchStartCancelled"),
    UI_MATCH_OVER("userInterface.matchOver"),
    UI_MATCH_WIN("userInterface.matchWin"),
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
    TAB_PLUGIN("tab.pl_name"),
    
    private final String path;
    
    private ChatConstant(String path) {
        this.path = path;
    }
    
    public String getMessage(String locale) {
        Document localized = Cardinal.getLocaleHandler().getLocaleDocument(locale.split("_")[0]);
        String message = null;
        Element work = localized.getRootElement();
        for (String element : this.path.split("\\.")) {
            work = work.getChild(element);
            message = work.getText();
        }
        return message;
    }
    
    public ChatMessage asMessage(ChatMessage... messages) {
        return new LocalizedChatMessage(this, messages);
    }
    
}
