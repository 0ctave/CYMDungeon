package fr.dungeons.dungeon.actions;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import fr.dungeons.Dungeons;
import fr.dungeons.Logger;
import fr.dungeons.custommob.CustomMob;
import fr.dungeons.dungeon.Dungeon;
import fr.dungeons.dungeon.DungeonInstance;
import fr.dungeons.dungeon.actions.data.*;
import fr.dungeons.dungeon.events.DungeonEvent;
import fr.dungeons.dungeon.events.DungeonEventManager;
import fr.dungeons.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DungeonAction {
    private ActionData data;
    private static HashMap<Integer, DungeonAction> loadedActions = new HashMap<>();

    private static HashMap<Integer, BukkitRunnable> runningTimeOut = new HashMap<>();

    private DungeonAction(ActionData data) {
        loadedActions.put(Integer.valueOf(data.getReferenceID()), this);
        this.data = data;
    }

    public static int generateID() {
        int id = 0;
        for (; loadedActions.containsKey(Integer.valueOf(id)); id++) ;
        return id;
    }


    public ActionData getData() {
        return this.data;
    }

    public int getReferenceID() {
        return getData().getReferenceID();
    }

    public DungeonActionType getType() {
        return getData().getType();
    }

    public static DungeonAction createMessageAction(List<String> messages, int id) {
        final DispMessageData data = new DispMessageData(messages, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                for (Player pla : DungeonInstance.getOnlinePlayers()) {
                    for (String s : data.getMessages())
                        pla.sendMessage(s);
                }

            }
        };
        return da;
    }

    public static DungeonAction createMobSpawnAction(Vector where, CustomMob mob, int count, int id) {
        final SpawnMobData data = new SpawnMobData(where, mob, id);
        data.setCustomMobCount(count);

        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                List<LivingEntity> spawnedEntities = new ArrayList<>();

                Location toSpawn = data.getLocToSpawn().toLocation(Bukkit.getWorlds().get(0));

                for (int i = 0; i < data.getCustomMobCount(); ) {
                    spawnedEntities.add(data.getCustomMob().spawn(toSpawn));
                    i++;
                }

                DungeonEventManager.setEntitiesFrom(spawnedEntities, this);
            }
        };

        return da;
    }

    public static DungeonAction createSchematicPasteAction(Vector where, String schematicName, boolean replaceAir, int id) {
        final SchematicData data = new SchematicData(where, schematicName, id);
        data.setAir(replaceAir);

        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                Clipboard clipboard = Dungeon.getSchematic(data.getSchematicName());

                try {
                    BukkitWorld world = new BukkitWorld(Bukkit.getWorlds().get(0));
                    EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
                    Operation operation = new ClipboardHolder(clipboard)
                            .createPaste(editSession)
                            .to(BlockVector3.at(data.getLocToPast().getX(), data.getLocToPast().getY(), data.getLocToPast().getZ()))
                            .ignoreAirBlocks(false)
                            .build();
                    Operations.complete(operation);
                } catch (WorldEditException e) {
                    Logger.error(new String[]{"Can't paste schematic : " + e.getMessage()});
                }
            }
        };

        return da;
    }

    public static DungeonAction createTitleAction(String title, String subTitle, int id) {
        final TitleData data = new TitleData(title, subTitle, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                Player[] players = new Player[DungeonInstance.getOnlinePlayers().size()];
                DungeonInstance.getOnlinePlayers().toArray((Object[]) players);

                Title.sendTitle(data.getTitle(), data.getSubTitle(), players);
            }
        };

        return da;
    }

    public static DungeonAction createTeleportAction(Vector where, int id) {
        final TeleportData data = new TeleportData(where, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                for (Player pla : DungeonInstance.getOnlinePlayers()) {
                    pla.teleport(data.getLocToTeleport().toLocation(pla.getWorld()));
                }
            }
        };
        return da;
    }

    public static DungeonAction createTimeOutAction(final int tick, List<DungeonAction> todos, int id) {
        final TimeOutData data = new TimeOutData(tick, todos, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                BukkitRunnable run = new BukkitRunnable() {
                    public void run() {
                        for (DungeonAction da : data.getTodoActions()) da.action();
                    }
                };
                run.runTaskLater(Dungeons.getPlugin(), tick);

                DungeonAction.runningTimeOut.put(Integer.valueOf(getReferenceID()), run);
            }
        };

        return da;
    }

    public static DungeonAction createExpAction(int exp, int id) {
        final ExpData data = new ExpData(exp, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                for (Player pla : DungeonInstance.getOnlinePlayers()) pla.giveExp(data.getExpAmount());

            }
        };
        return da;
    }

    public static DungeonAction createGiveItemAction(ItemStack is, int id) {
        final GiveItemData data = new GiveItemData(is, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                for (Player pla : DungeonInstance.getOnlinePlayers()) {
                    pla.getInventory().addItem(new ItemStack[]{data.getItem()});
                }
            }
        };
        return da;
    }

    public static DungeonAction createSpawnItemAction(ItemStack is, Vector where, int id) {
        final SpawnItemData data = new SpawnItemData(is, where, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                DungeonInstance.getWorld().dropItem(data.getLocToSpawn().toLocation(DungeonInstance.getWorld()), data.getItem());
            }
        };

        return da;
    }

    public static DungeonAction createPotionAction(PotionEffectType type, int duration, int amplifier, int id) {
        final PotionData data = new PotionData(type, duration, amplifier, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                PotionEffect pe = new PotionEffect(data.getEffectType(), data.getDuration(), data.getAmplifier(), data.isAmbient(), data.isParticles());
                for (Player pla : DungeonInstance.getOnlinePlayers()) pla.addPotionEffect(pe);

            }
        };
        return da;
    }

    public static DungeonAction createStopTimeOutAction(List<DungeonAction> timeOutFrom, int id) {
        final StopTimeOutData data = new StopTimeOutData(timeOutFrom, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                for (DungeonAction da : data.getTimeOutFromActions()) {
                    if (DungeonAction.runningTimeOut.containsKey(Integer.valueOf(da.getReferenceID()))) {
                        ((BukkitRunnable) DungeonAction.runningTimeOut.get(Integer.valueOf(da.getReferenceID()))).cancel();
                        DungeonAction.runningTimeOut.remove(Integer.valueOf(da.getReferenceID()));
                    }
                }

            }
        };
        return da;
    }

    public static DungeonAction createExplosionAction(Vector where, float explosionStrength, boolean fire, boolean breakBlocks, int id) {
        final ExplosionData data = new ExplosionData(explosionStrength, where, fire, breakBlocks, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                DungeonInstance.getWorld().createExplosion(data.getExplosionLocation().getX(), data.getExplosionLocation().getY(), data
                        .getExplosionLocation().getZ(), data.getExplosionStrength(), data.isFire(), data.isBreakBlocks());
            }
        };

        return da;
    }

    public static DungeonAction createActivateEventAction(List<Integer> todos, int id) {
        final ActivateEventData data = new ActivateEventData(todos, id);
        DungeonAction da = new DungeonAction((ActionData) data) {
            public void action() {
                List<Integer> currentEventsID = new ArrayList<>();
                for (DungeonEvent de : DungeonInstance.getEventsNoCheck())
                    currentEventsID.add(Integer.valueOf(de.getReferenceID()));
                for (Integer IDEventToAdd : data.getTodoEvents()) {
                    if (currentEventsID.contains(IDEventToAdd)) {
                        DungeonInstance.enableEvent(IDEventToAdd.intValue());
                        continue;
                    }
                    DungeonInstance.addEvent(DungeonEvent.getByID(IDEventToAdd.intValue()));
                }
            }
        };


        return da;
    }

    public static DungeonAction unserialize(String serialized) {
        YamlConfiguration yml = new YamlConfiguration();

        try {
            yml.loadFromString(serialized);
        } catch (InvalidConfigurationException e) {
            Logger.error(new String[]{"Unable to unserialize DungeonAction: " + e.getMessage()});
            return null;
        }

        if (yml.isInt("Reference_ID") && yml.isString("actionType")) {
            DispMessageData dispMessageData;
            TitleData titleData;
            ExpData expData;
            GiveItemData giveItemData;
            PotionData potionData;
            SchematicData schematicData;
            SpawnItemData spawnItemData;
            SpawnMobData spawnMobData;
            TeleportData teleportData;
            TimeOutData timeOutData;
            StopTimeOutData stopTimeOutData;
            ExplosionData explosionData;
            ActivateEventData data;
            DungeonAction da;
            DungeonActionType type = DungeonActionType.valueOf(yml.getString("actionType"));

            switch (type) {
                case DISP_MESSAGE:
                    dispMessageData = DispMessageData.unserialize(serialized);
                    return createMessageAction(dispMessageData.getMessages(), dispMessageData.getReferenceID());

                case DISP_TITLE:
                    titleData = TitleData.unserialize(serialized);
                    return createTitleAction(titleData.getTitle(), titleData.getSubTitle(), titleData.getReferenceID());

                case GIVE_EXP:
                    expData = ExpData.unserialize(serialized);
                    return createExpAction(expData.getExpAmount(), expData.getReferenceID());

                case GIVE_ITEM:
                    giveItemData = GiveItemData.unserialize(serialized);
                    return createGiveItemAction(giveItemData.getItem(), giveItemData.getReferenceID());

                case GIVE_POTION:
                    potionData = PotionData.unserialize(serialized);
                    da = createPotionAction(potionData.getEffectType(), potionData.getDuration(), potionData.getAmplifier(), potionData.getReferenceID());
                    ((PotionData) da.getData()).setAmbient(potionData.isAmbient());
                    ((PotionData) da.getData()).setParticles(potionData.isParticles());
                    return da;

                case KILL_MOBS:
                    return getByID(-1);

                case PASTE_SCHEMATIC:
                    schematicData = SchematicData.unserialize(serialized);
                    return createSchematicPasteAction(schematicData.getLocToPast(), schematicData.getSchematicName(), schematicData.isAir(), schematicData.getReferenceID());

                case SPAWN_ITEM:
                    spawnItemData = SpawnItemData.unserialize(serialized);
                    return createSpawnItemAction(spawnItemData.getItem(), spawnItemData.getLocToSpawn(), spawnItemData.getReferenceID());

                case SPAWN_MOB:
                    spawnMobData = SpawnMobData.unserialize(serialized);
                    return createMobSpawnAction(spawnMobData.getLocToSpawn(), spawnMobData.getCustomMob(), spawnMobData.getCustomMobCount(), spawnMobData.getReferenceID());

                case TELEPORT:
                    teleportData = TeleportData.unserialize(serialized);
                    return createTeleportAction(teleportData.getLocToTeleport(), teleportData.getReferenceID());

                case TIME_OUT:
                    timeOutData = TimeOutData.unserialize(serialized);
                    return createTimeOutAction(timeOutData.getTick(), timeOutData.getTodoActions(), timeOutData.getReferenceID());

                case STOP_TIME_OUT:
                    stopTimeOutData = StopTimeOutData.unserialize(serialized);
                    return createStopTimeOutAction(stopTimeOutData.getTimeOutFromActions(), stopTimeOutData.getReferenceID());

                case CREATE_EXPLOSION:
                    explosionData = ExplosionData.unserialize(serialized);
                    return createExplosionAction(explosionData.getExplosionLocation(), explosionData.getExplosionStrength(), explosionData.isFire(), explosionData.isBreakBlocks(), explosionData.getReferenceID());

                case ACTIVATE_EVENT:
                    data = ActivateEventData.unserialize(serialized);
                    return createActivateEventAction(data.getTodoEvents(), data.getReferenceID());
            }

            Logger.error(new String[]{"Unable to unserialize DungeonAction, no Reference_ID or no ActionType"});
            return null;
        }


        Logger.error(new String[]{"Unable to unserialize DungeonAction, no Reference_ID or no ActionType"});
        return null;
    }


    public static void loadAllFromFiles() {
        File folder = new File(Dungeons.getPlugin().getDataFolder().getAbsolutePath() + File.separatorChar + "DungeonActions");
        if (!folder.exists()) folder.mkdir();

        for (File f : folder.listFiles()) {
            if (f.isFile() &&
                    f.canRead() &&
                    f.getName().endsWith(".yml")) {

                YamlConfiguration yml = new YamlConfiguration();
                try {
                    yml.load(f);
                    DungeonAction da = unserialize(yml.saveToString());
                    Logger.info(new String[]{"DungeonAction ID: " + da.getReferenceID() + " loaded"});
                } catch (IOException | InvalidConfigurationException e) {
                    Logger.error(new String[]{"Unable to load DungeonAction from file " + f.getName() + " : " + e.getMessage()});
                }
            }
        }
    }

    public static DungeonAction getByID(int id) {
        if (id == -1) {
            ActionData data = new ActionData(DungeonActionType.KILL_MOBS, -1) {
                public String serialize() {
                    return null;
                }
            };

            DungeonAction da = new DungeonAction(data) {
                public void action() {
                    for (LivingEntity le : DungeonInstance.getWorld().getLivingEntities()) {
                        if (le.getType() != EntityType.PLAYER) le.remove();
                    }
                }
            };
            return da;
        }
        if (id == -2) {
            ActionData data = new ActionData(DungeonActionType.WIN_DUNGEON, -2) {
                public String serialize() {
                    return null;
                }
            };

            DungeonAction da = new DungeonAction(data) {
                public void action() {
                    DungeonInstance.win(new String[]{"Win action"});
                }
            };

            return da;
        }
        if (id == -3) {
            ActionData data = new ActionData(DungeonActionType.LOOSE_DUNGEON, -3) {
                public String serialize() {
                    return null;
                }
            };

            DungeonAction da = new DungeonAction(data) {
                public void action() {
                    DungeonInstance.loose(new String[]{"Loose action"});
                }
            };

            return da;
        }
        return loadedActions.get(Integer.valueOf(id));
    }


    public boolean equals(Object o) {
        if (!(o instanceof DungeonAction)) return false;
        return (((DungeonAction) o).getReferenceID() == getReferenceID());
    }

    public abstract void action();
}


