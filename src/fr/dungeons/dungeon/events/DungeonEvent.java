/*     */ package fr.dungeons.dungeon.events;
/*     */ 
/*     */ import fr.dungeons.Dungeons;
/*     */ import fr.dungeons.Logger;
/*     */ import fr.dungeons.dungeon.actions.DungeonAction;
/*     */ import fr.dungeons.dungeon.events.data.AreaDetectionData;
/*     */ import fr.dungeons.dungeon.events.data.BlockPlaceData;
/*     */ import fr.dungeons.dungeon.events.data.EventData;
/*     */ import fr.dungeons.dungeon.events.data.InteractData;
/*     */ import fr.dungeons.dungeon.events.data.MobWaveEndedData;
/*     */ import fr.dungeons.dungeon.events.data.PutItemData;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.InvalidConfigurationException;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DungeonEvent
/*     */ {
/*  26 */   private static HashMap<Integer, DungeonEvent> loadedEvents = new HashMap<>();
/*     */   
/*     */   private EventData data;
/*     */   private boolean done = false;
/*     */   
/*     */   private DungeonEvent(EventData data) {
/*  32 */     this.data = data;
/*  33 */     loadedEvents.put(Integer.valueOf(getReferenceID()), this);
/*     */   }
/*     */   
/*     */   public int getReferenceID() {
/*  37 */     return this.data.getReferenceID();
/*     */   }
/*     */   
/*     */   public EventData getData() {
/*  41 */     return this.data;
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  45 */     return this.done;
/*     */   }
/*     */   
/*     */   public void setDone(boolean b) {
/*  49 */     this.done = b;
/*     */   }
/*     */   
/*     */   public void setData(EventData data) {
/*  53 */     this.data = data;
/*     */   }
/*     */   
/*     */   public void event() {
/*  57 */     for (DungeonAction da : this.data.getActionsTodo()) da.action(); 
/*  58 */     this.done = true;
/*     */   }
/*     */   
/*     */   public static DungeonEvent createMobWaveEndedEvent(List<DungeonAction> mobFromActions, List<DungeonAction> todoActions, int id) {
/*  62 */     MobWaveEndedData data = new MobWaveEndedData(mobFromActions, todoActions, id);
/*  63 */     DungeonEvent de = new DungeonEvent((EventData)data);
/*     */     
/*  65 */     return de;
/*     */   }
/*     */   
/*     */   public static DungeonEvent createAreaActivationEvent(List<DungeonAction> todoActions, List<Vector> eventActivations, double activationRange, int id) {
/*  69 */     AreaDetectionData data = new AreaDetectionData(eventActivations, todoActions, activationRange, id);
/*  70 */     DungeonEvent de = new DungeonEvent((EventData)data);
/*     */     
/*  72 */     return de;
/*     */   }
/*     */   
/*     */   public static DungeonEvent createInteractEvent(Vector blockToInteractLocation, List<DungeonAction> actionsTodo, int id) {
/*  76 */     InteractData data = new InteractData(blockToInteractLocation, actionsTodo, id);
/*  77 */     DungeonEvent de = new DungeonEvent((EventData)data);
/*     */     
/*  79 */     return de;
/*     */   }
/*     */   
/*     */   public static DungeonEvent createPutItemEvent(ItemStack item, Vector chestToPutIn, List<DungeonAction> actionsTodo, int id) {
/*  83 */     PutItemData data = new PutItemData(item, chestToPutIn, actionsTodo, id);
/*  84 */     DungeonEvent de = new DungeonEvent((EventData)data);
/*     */     
/*  86 */     return de;
/*     */   }
/*     */   
/*     */   public static DungeonEvent createPlaceBlockEvent(Material blockType, Vector position, List<DungeonAction> actionsTodo, int id) {
/*  90 */     BlockPlaceData data = new BlockPlaceData(position, blockType, actionsTodo, id);
/*  91 */     DungeonEvent de = new DungeonEvent((EventData)data);
/*     */     
/*  93 */     return de;
/*     */   }
/*     */   
/*     */   public static DungeonEvent getByID(int id) {
/*  97 */     return loadedEvents.get(Integer.valueOf(id));
/*     */   }
/*     */   
/*     */   public static int generateID() {
/* 101 */     int id = 0;
/* 102 */     for (; loadedEvents.containsKey(Integer.valueOf(id)); id++);
/* 103 */     return id;
/*     */   }
/*     */   
/*     */   public static DungeonEvent unserialize(String serialized) {
/* 107 */     YamlConfiguration yml = new YamlConfiguration();
/*     */     
/*     */     try {
/* 110 */       yml.loadFromString(serialized);
/* 111 */     } catch (InvalidConfigurationException e) {
/* 112 */       Logger.error(new String[] { "Unable to unserialize DungeonEvent: " + e.getMessage() });
/* 113 */       return null;
/*     */     } 
/*     */     
/* 116 */     if (yml.isInt("Reference_ID") && yml.isString("eventType")) {
/* 117 */       AreaDetectionData areaDetectionData; InteractData interactData; MobWaveEndedData mobWaveEndedData; BlockPlaceData blockPlaceData; PutItemData data; DungeonEventType type = DungeonEventType.valueOf(yml.getString("eventType"));
/*     */       
/* 119 */       switch (type) {
/*     */         case AREA_DETECTION:
/* 121 */           areaDetectionData = AreaDetectionData.unserialize(serialized);
/* 122 */           return createAreaActivationEvent(areaDetectionData.getActionsTodo(), areaDetectionData.getActivationLocations(), areaDetectionData.getActivationRadius(), areaDetectionData.getReferenceID());
/*     */         
/*     */         case INTERACT:
/* 125 */           interactData = InteractData.unserialize(serialized);
/* 126 */           return createInteractEvent(interactData.getBlockLocation(), interactData.getActionsTodo(), interactData.getReferenceID());
/*     */         
/*     */         case MOB_WAVE_ENDED:
/* 129 */           mobWaveEndedData = MobWaveEndedData.unserialize(serialized);
/* 130 */           return createMobWaveEndedEvent(mobWaveEndedData.getMobFromActions(), mobWaveEndedData.getActionsTodo(), mobWaveEndedData.getReferenceID());
/*     */         
/*     */         case PLACE_BLOCK:
/* 133 */           blockPlaceData = BlockPlaceData.unserialize(serialized);
/* 134 */           return createPlaceBlockEvent(blockPlaceData.getBlockType(), blockPlaceData.getBlockLocation(), blockPlaceData.getActionsTodo(), blockPlaceData.getReferenceID());
/*     */         
/*     */         case PUT_ITEM:
/* 137 */           data = PutItemData.unserialize(serialized);
/* 138 */           return createPutItemEvent(data.getItem(), data.getChestLocation(), data.getActionsTodo(), data.getReferenceID());
/*     */       } 
/*     */       
/* 141 */       Logger.error(new String[] { "Unable to unserialize DungeonEvent, no Reference_ID or no EventType" });
/* 142 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 147 */     Logger.error(new String[] { "Unable to unserialize DungeonEvent, no Reference_ID or no EventType" });
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void loadAllFromFiles() {
/* 153 */     File folder = new File(Dungeons.getPlugin().getDataFolder().getAbsolutePath() + File.separatorChar + "DungeonEvents");
/* 154 */     if (!folder.exists()) folder.mkdir();
/*     */     
/* 156 */     for (File f : folder.listFiles()) {
/* 157 */       if (f.isFile() && 
/* 158 */         f.canRead() && 
/* 159 */         f.getName().endsWith(".yml")) {
/*     */         
/* 161 */         YamlConfiguration yml = new YamlConfiguration();
/*     */         try {
/* 163 */           yml.load(f);
/* 164 */           DungeonEvent de = unserialize(yml.saveToString());
/* 165 */           Logger.info(new String[] { "DungeonEvent ID: " + de.getReferenceID() + " loaded" });
/* 166 */         } catch (IOException|InvalidConfigurationException e) {
/* 167 */           Logger.error(new String[] { "Unable to load DungeonEvent from file " + f.getName() + " : " + e.getMessage() });
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/tancou/Documents/Katail/Dungeon.jar!/fr/dungeons/dungeon/events/DungeonEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */