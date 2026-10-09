package dev.klewi.rpm.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(value = ResourcePackManagerNeoForge.MOD_ID, dist = Dist.CLIENT)
public final class ResourcePackManagerNeoForge {
    public static final String MOD_ID = "resourcepackmanager";
    private final net.neoforged.bus.api.IEventBus modBus;

    public ResourcePackManagerNeoForge(FMLJavaModLoadingContext context) {
        this.modBus = context.getModEventBus();
        modBus.addListener(this::registerKey);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onClientTick);
    }

    private KeyMapping openManager;

    private void registerKey(RegisterKeyMappingsEvent event) {
        openManager = new KeyMapping("key.resourcepackmanager.open", GLFW.GLFW_KEY_K, KeyMapping.Category.MISC);
        event.register(openManager);
    }

    private void onClientTick(ClientTickEvent.Post event) {
        if (openManager != null && openManager.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null) mc.setScreen(new ManagerScreen(null));
        }
    }

    private static final class ManagerScreen extends Screen {
        private final Screen parent;
        ManagerScreen(Screen parent) {
            super(Component.literal("ResourcePack Manager"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            int cx = width / 2;
            int y = height / 2 - 24;
            addRenderableWidget(Button.builder(Component.literal("Manage Resource Packs"), b -> {
                Minecraft mc = Minecraft.getInstance();
                mc.setScreen(new PackSelectionScreen(this, mc.getResourcePackRepository(),
                        mc.getResourcePackDirectory(), Component.literal("Resource Packs")));
            }).bounds(cx - 110, y, 220, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Browse Modrinth"), b ->
                    net.minecraft.Util.getPlatform().openUri("https://modrinth.com/resourcepacks"))
                    .bounds(cx - 110, y + 27, 220, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Close"), b -> onClose())
                    .bounds(cx - 110, y + 54, 220, 20).build());
        }

        @Override
        public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            renderBackground(graphics, mouseX, mouseY, delta);
            graphics.drawCenteredString(font, title, width / 2, height / 2 - 60, 0xFFFFFF);
            super.render(graphics, mouseX, mouseY, delta);
        }

        @Override public void onClose() { Minecraft.getInstance().setScreen(parent); }
    }
}
