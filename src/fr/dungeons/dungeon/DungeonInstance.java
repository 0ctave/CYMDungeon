/*     */ package fr.dungeons.dungeon;
/*     */ 
/*     */ import fr.dungeons.Dungeons;
/*     */ import fr.dungeons.Logger;
/*     */ import fr.dungeons.dungeon.events.DungeonEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DungeonInstance
/*     */ {
/*  17 */   private static List<String> players = new ArrayList<>();
/*     */   
/*     */   private static Dungeon dungeon;
/*     */   private static int remainingHealth;
/*  21 */   private static List<DungeonEvent> events = new ArrayList<>();
/*     */   
/*     */   public static void initInstance(Dungeon d) {
/*  24 */     dungeon = d;
/*     */     
/*  26 */     Logger.info(new String[] { "Creation d'instance du Dongeon " + dungeon.getName() });
/*     */     
/*  28 */     for (DungeonEvent de : dungeon.getEvents()) events.add(de);
/*     */     
/*  30 */     setRemainingHealth(dungeon.getMaxHealth());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> getPlayers() {
/*  37 */     return new ArrayList<>(players);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void kickPlayer(Player pla) {
/*  42 */     pla.kickPlayer("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Player> getOnlinePlayers() {
/*  49 */     return new ArrayList<>(Bukkit.getOnlinePlayers());
/*     */   }
/*     */   
/*     */   public static Dungeon getDungeon() {
/*  53 */     return dungeon;
/*     */   }
/*     */   
/*     */   public static List<DungeonEvent> getEvents() {
/*  57 */     List<DungeonEvent> todoEvents = new ArrayList<>();
/*  58 */     for (DungeonEvent de : events) { if (!de.isDone()) todoEvents.add(de);  }
/*  59 */      return todoEvents;
/*     */   }
/*     */   
/*     */   public static List<DungeonEvent> getEventsNoCheck() {
/*  63 */     return events;
/*     */   }
/*     */   
/*     */   public static void addEvents(List<DungeonEvent> eventsTodo) {
/*  67 */     events.addAll(eventsTodo);
/*     */   }
/*     */   
/*     */   public static void addEvent(DungeonEvent event) {
/*  71 */     events.add(event);
/*     */   }
/*     */   
/*     */   public static void enableEvent(int EventID) {
/*  75 */     for (DungeonEvent de : events) {
/*  76 */       if (de.getData().getReferenceID() == EventID) {
/*  77 */         de.setDone(false);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getRemainingHealth() {
/*  84 */     return remainingHealth;
/*     */   }
/*     */   
/*     */   public static void setRemainingHealth(int remainingHealth) {
/*  88 */     DungeonInstance.remainingHealth = remainingHealth;
/*     */   }
/*     */   
/*     */   private static void kill(final List<String> reason) {
/*  92 */     DungeonSpectator.respawnAll();
/*     */     
/*  94 */     for (Player pla : Bukkit.getOnlinePlayers()) {
/*  95 */       if (pla.isDead()) pla.spigot().respawn(); 
/*  96 */       (new BukkitRunnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 100 */             DungeonInstance.kickPlayer(pla);
/*     */           }
/* 102 */         }).runTaskLater(Dungeons.getPlugin(), 40L);
/*     */     } 
/*     */     
/* 105 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 109 */           for (String s : reason) { Logger.info(new String[] { s }); }
/* 110 */            Logger.info(new String[] { "Dungeon server stopping" });
/*     */           
/* 112 */           Bukkit.getServer().shutdown();
/*     */         }
/* 114 */       }).runTaskLater(Dungeons.getPlugin(), 100L);
/*     */   }
/*     */   
/*     */   public static void win(String... args) {
/* 118 */     for (Player pla : getOnlinePlayers()) {
/* 119 */       pla.sendMessage(Messages.DUNGEON_PREFIX + Messages.WIN1);
/* 120 */       pla.sendMessage(Messages.DUNGEON_PREFIX + Messages.WIN2);
/*     */       
/* 122 */       (new BukkitRunnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 126 */             DungeonInstance.kickPlayer(pla);
/*     */           }
/* 128 */         }).runTaskLater(Dungeons.getPlugin(), 200L);
/*     */     } 
/*     */     
/* 131 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 135 */           List<String> reason = new ArrayList<>();
/* 136 */           reason.add("Dungeon termine (victoire)");
/* 137 */           for (String s : args) reason.add(s); 
/* 138 */           DungeonInstance.kill(reason);
/*     */         }
/* 140 */       }).runTaskLater(Dungeons.getPlugin(), 250L);
/*     */   }
/*     */   
/*     */   public static void loose(String... args) {
/* 144 */     for (Player pla : getOnlinePlayers()) {
/* 145 */       pla.sendMessage(Messages.DUNGEON_PREFIX + Messages.LOOSE1);
/* 146 */       pla.sendMessage(Messages.DUNGEON_PREFIX + Messages.LOOSE2);
/*     */       
/* 148 */       (new BukkitRunnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 152 */             DungeonInstance.kickPlayer(pla);
/*     */           }
/* 154 */         }).runTaskLater(Dungeons.getPlugin(), 200L);
/*     */       
/* 156 */       (new BukkitRunnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 160 */             List<String> reason = new ArrayList<>();
/* 161 */             reason.add("Dungeon termine (defaite)");
/* 162 */             for (String s : args) reason.add(s); 
/* 163 */             DungeonInstance.kill(reason);
/*     */           }
/* 165 */         }).runTaskLater(Dungeons.getPlugin(), 250L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static World getWorld() {
/* 170 */     return Bukkit.getWorlds().get(0);
/*     */   }
/*     */ }


/* Location:              /Users/tancou/Documents/Katail/Dungeon.jar!/fr/dungeons/dungeon/DungeonInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */