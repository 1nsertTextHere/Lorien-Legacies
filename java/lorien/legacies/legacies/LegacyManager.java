package lorien.legacies.legacies;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import lorien.legacies.core.LorienLegacies;
import lorien.legacies.gui.LegacyGui;
import lorien.legacies.legacies.implementations.AccelixLegacy;
import lorien.legacies.legacies.implementations.AvexLegacy;
import lorien.legacies.legacies.implementations.FortemLegacy;
import lorien.legacies.legacies.implementations.GlacenLegacy;
import lorien.legacies.legacies.implementations.LumenLegacy;
import lorien.legacies.legacies.implementations.NovisLegacy;
import lorien.legacies.legacies.implementations.NoxenLegacy;
import lorien.legacies.legacies.implementations.PondusLegacy;
import lorien.legacies.legacies.implementations.RegenerasLegacy;
import lorien.legacies.legacies.implementations.SubmariLegacy;
import lorien.legacies.legacies.implementations.Telekinesis;
import lorien.legacies.legacies.worldSave.LegacyWorldSaveData;
import lorien.legacies.network.NetworkHandler;
import lorien.legacies.network.mesages.legacyActions.LegacyAction;
import lorien.legacies.network.mesages.legacyActions.LegacyActionConverter;
import lorien.legacies.network.mesages.legacyActions.MessageLegacyAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LegacyManager {
	
	public EntityPlayer player;
	
	public boolean legaciesEnabled;
	
	// Used in legacies.levels package
	public List legacyList = new ArrayList<Legacy>(); // I don't have a clue why I'm not using a fancy array instead of all the code below,
													// All I can remember is that I probably had a good reason... (by all means refactor the 
													// code if you want to)
	
	public LumenLegacy lumenLegacy;
	public boolean lumenLegacyEnabled;
	
	public NoxenLegacy noxenLegacy;
	public boolean noxenLegacyEnabled;
	
	public SubmariLegacy submariLegacy;
	public boolean submariLegacyEnabled;
	
	public NovisLegacy novisLegacy;
	public boolean novisLegacyEnabled;
	
	public AccelixLegacy accelixLegacy;
	public boolean accelixLegacyEnabled;
	
	public FortemLegacy fortemLegacy;
	public boolean fortemLegacyEnabled;
	
	public PondusLegacy pondusLegacy;
	public boolean pondusLegacyEnabled;
	
	public RegenerasLegacy regenerasLegacy;
	public boolean regenerasLegacyEnabled;
	
	public AvexLegacy avexLegacy;
	public boolean avexLegacyEnabled;
	
	public GlacenLegacy glacenLegacy;
	public boolean glacenLegacyEnabled;
	
	public Telekinesis telekinesis;
	
	public LegacyManager(EntityPlayer player)
	{
		this.player = player;
		MinecraftForge.EVENT_BUS.register(this);
		
		// Setup legacies
		lumenLegacy = new LumenLegacy();
		noxenLegacy = new NoxenLegacy();
		submariLegacy = new SubmariLegacy();
		accelixLegacy = new AccelixLegacy();
		fortemLegacy = new FortemLegacy();
		novisLegacy = new NovisLegacy();
		pondusLegacy = new PondusLegacy();
		regenerasLegacy = new RegenerasLegacy();
		avexLegacy = new AvexLegacy();
		glacenLegacy = new GlacenLegacy();
		
		telekinesis = new Telekinesis();
		
		// Add legacies to an array for levels purposes
		legacyList.add(lumenLegacy);
		legacyList.add(noxenLegacy);
		legacyList.add(submariLegacy);
		legacyList.add(accelixLegacy);
		legacyList.add(fortemLegacy);
		legacyList.add(novisLegacy);
		legacyList.add(pondusLegacy);
		legacyList.add(regenerasLegacy);
		legacyList.add(avexLegacy);
		legacyList.add(glacenLegacy);
		legacyList.add(telekinesis);
	}
	
	public void computeLegacyTick(boolean isServer)
	{
		if (lumenLegacyEnabled && lumenLegacy.toggled)
			lumenLegacy.computeLegacyTick(player);
			
		if (noxenLegacyEnabled)
			noxenLegacy.computeLegacyTick(player);
			
		if (submariLegacyEnabled)
			submariLegacy.computeLegacyTick(player);
			
		if (accelixLegacyEnabled)
			accelixLegacy.computeLegacyTick(player);
			
		if (fortemLegacyEnabled)
			fortemLegacy.computeLegacyTick(player);
			
		if (novisLegacyEnabled)
			novisLegacy.computeLegacyTick(player);
			
		if (pondusLegacyEnabled)
			pondusLegacy.computeLegacyTick(player);
			
		if (regenerasLegacyEnabled)
			regenerasLegacy.computeLegacyTick(player);
			
		if (avexLegacyEnabled)
			avexLegacy.computeLegacyTick(player);
		
		if (glacenLegacyEnabled)
			glacenLegacy.computeLegacyTick(player);
		
		//telekinesis.computeLegacyTick(player, isServer);
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{	
		
		if (event.player == null || player == null)
			return;

		if (event.player.world.isRemote && legaciesEnabled) // Client
			computeLegacyTick(false);
		else if (legaciesEnabled) // Server
			computeLegacyTick(true);
	}
	
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event)
	{	
		
		onKeyClient();
		
		if (player == null)
			return;

		//telekinesis.computeLegacyTick(player, event.side.isServer());
	}
	
	private boolean previousWaterDecision = false;
	
	// So you can't just spam it and crash the server (trust me, it was hilarious)
	private boolean lumenFireballShot = false;
	
	public void onKeyClient()
	{
		LegacyAction action = null;
		
		if (KeyBindings.lumenFireball.isPressed() && lumenLegacyEnabled) // Don't worry, I'm not doing client-side verification!
			action = LegacyAction.LumenFireball;
		else if (KeyBindings.igniteLumen.isPressed() && lumenLegacyEnabled) // I'm testing it on the server-side too!
			action = LegacyAction.LumenIgnition;
		else if (KeyBindings.toggleAccelix.isPressed() && accelixLegacyEnabled) // Phew!
			action = LegacyAction.Accelix;
		else if (KeyBindings.toggleFortem.isPressed() && fortemLegacyEnabled)
			action = LegacyAction.Fortem;
		else if (KeyBindings.toggleNovis.isPressed() && novisLegacyEnabled)
			action = LegacyAction.Novis;
		else if (KeyBindings.togglePondus.isPressed() && pondusLegacyEnabled)
			action = LegacyAction.Pondus;
		else if (KeyBindings.glacenFreeze.isKeyDown() && glacenLegacyEnabled)
			action = LegacyAction.GlacenFreeze;
		else if (KeyBindings.toggleAvex.isPressed() && avexLegacyEnabled)
			action = LegacyAction.Avex;
		else if (KeyBindings.avexHover.isPressed() && avexLegacyEnabled)
			action = LegacyAction.AvexHover;

		if (action != null)
			NetworkHandler.sendToServer(new MessageLegacyAction(LegacyActionConverter.intFromLegacyAction(action)));
		
		// --- Also apply toggling on client-side --- \\
		
		if (action == null)
			return;
				
		// Accelix toggle
		if (action == LegacyAction.Accelix && legaciesEnabled && accelixLegacyEnabled)
			accelixLegacy.toggle(player);
					
		// Fortem toggle
		if (action == LegacyAction.Fortem && legaciesEnabled && fortemLegacyEnabled)
			fortemLegacy.toggle(player);
				
		// Novis toggle
		if (action == LegacyAction.Novis && legaciesEnabled && novisLegacyEnabled)
			novisLegacy.toggle(player);
		
		// Pondus toggle
		if (action == LegacyAction.Pondus && legaciesEnabled && pondusLegacyEnabled)
			pondusLegacy.toggle(player);
		
		// Avex toggle
		if (action == LegacyAction.Avex && legaciesEnabled && avexLegacyEnabled)
			avexLegacy.toggle(player);
		
		// Avex hover
		if (action == LegacyAction.AvexHover && legaciesEnabled && avexLegacyEnabled)
			avexLegacy.hover(player);
		
	}
	
	
}
