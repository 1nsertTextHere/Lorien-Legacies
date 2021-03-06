package lorien.legacies.proxy;

import lorien.legacies.blocks.ModBlocks;
import lorien.legacies.entities.ModEntities;
import lorien.legacies.entities.Chimaera.Chimaera;
import lorien.legacies.items.ModItems;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ModEntities.register();
    }
	
	@Override
	public void postInit(FMLPostInitializationEvent e) {
		
		//Chimaera.MorphHandler.postInit();
		super.postInit(e);
	}
	
	
	
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
    	ModBlocks.registerModels();
    	ModItems.registerModels();
    }
	
}
