package dev.klewi.rpm.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

@Mod(ResourcePackManagerForge.MOD_ID)
public final class ResourcePackManagerForge {
    public static final String MOD_ID = "resourcepackmanager";

    public ResourcePackManagerForge() {
        // Client-only behavior is registered by the nested event subscribers below.
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class ModEvents {
        private static KeyMapping openManager;

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            openManager = new KeyMapping(
                    "key.resourcepackmanager.open",
                    GLFW.GLFW_KEY_K,
                    KeyMapping.Category.MISC
            );
            event.register(openManager);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static final class ClientEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent.Post event) {
            if (ModEvents.openManager == null || !ModEvents.openManager.consumeClick()) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.screen == null) {
                minecraft.setScreen(new ManagerScreen(null));
            }
        }
    }

    private static final class ManagerScreen extends Screen {
        private final Screen parent;

        private ManagerScreen(Screen parent) {
            super(Component.literal("ResourcePack Manager"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            int centerX = width / 2;
            int startY = height / 2 - 38;

            addRenderableWidget(Button.builder(Component.literal("Manage Resource Packs"), button -> {
                Minecraft minecraft = Minecraft.getInstance();
                minecraft.setScreen(new PackSelectionScreen(
                        this,
                        minecraft.getResourcePackRepository(),
                        minecraft.getResourcePackDirectory(),
                        Component.literal("Resource Packs")
                ));
            }).bounds(centerX - 145, startY, 290, 20).build());

            addRenderableWidget(Button.builder(Component.literal("Featured Packs & Performance Mods"), button ->
                    Minecraft.getInstance().setScreen(new CatalogScreen(this)))
                    .bounds(centerX - 145, startY + 25, 290, 20).build());

            addRenderableWidget(Button.builder(Component.literal("Browse Modrinth"), button ->
                    net.minecraft.Util.getPlatform().openUri("https://modrinth.com"))
                    .bounds(centerX - 145, startY + 50, 290, 20).build());

            addRenderableWidget(Button.builder(Component.literal("Close"), button -> onClose())
                    .bounds(centerX - 145, startY + 75, 290, 20).build());
        }

        @Override
        public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            renderBackground(graphics, mouseX, mouseY, delta);
            graphics.drawCenteredString(font, title, width / 2, height / 2 - 72, 0xFFFFFF);
            super.render(graphics, mouseX, mouseY, delta);
        }

        @Override
        public void onClose() {
            Minecraft.getInstance().setScreen(parent);
        }
    }

    private static final class CatalogScreen extends Screen {
        private final Screen parent;

        private CatalogScreen(Screen parent) {
            super(Component.literal("Featured Packs & Performance Mods"));
            this.parent = parent;
        }

        private void open(String url) {
            net.minecraft.Util.getPlatform().openUri(url);
        }

        @Override
        protected void init() {
            int centerX = width / 2;
            int startY = Math.max(34, height / 2 - 105);

            addRenderableWidget(Button.builder(Component.literal("Faithful 32x — Vanilla-style textures"), button ->
                    open("https://modrinth.com/resourcepack/faithful-32x"))
                    .bounds(centerX - 150, startY, 300, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Ashen — Medieval fantasy"), button ->
                    open("https://modrinth.com/resourcepack/ashen"))
                    .bounds(centerX - 150, startY + 24, 300, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Excalibur — Medieval fantasy"), button ->
                    open("https://modrinth.com/resourcepack/excal"))
                    .bounds(centerX - 150, startY + 48, 300, 20).build());

            addRenderableWidget(Button.builder(Component.literal("Sodium — Rendering / FPS"), button ->
                    open("https://modrinth.com/mod/sodium"))
                    .bounds(centerX - 150, startY + 78, 300, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Lithium — Game logic optimization"), button ->
                    open("https://modrinth.com/mod/lithium"))
                    .bounds(centerX - 150, startY + 102, 300, 20).build());
            addRenderableWidget(Button.builder(Component.literal("FerriteCore — Memory optimization"), button ->
                    open("https://modrinth.com/mod/ferrite-core"))
                    .bounds(centerX - 150, startY + 126, 300, 20).build());
            addRenderableWidget(Button.builder(Component.literal("ImmediatelyFast — Rendering"), button ->
                    open("https://modrinth.com/mod/immediatelyfast"))
                    .bounds(centerX - 150, startY + 150, 300, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Entity Culling — Culling"), button ->
                    open("https://modrinth.com/mod/entityculling"))
                    .bounds(centerX - 150, startY + 174, 300, 20).build());

            addRenderableWidget(Button.builder(Component.literal("Back"), button ->
                    Minecraft.getInstance().setScreen(parent))
                    .bounds(centerX - 150, startY + 204, 300, 20).build());
        }

        @Override
        public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            renderBackground(graphics, mouseX, mouseY, delta);
            graphics.drawCenteredString(font, title, width / 2, 12, 0xFFFFFF);
            graphics.drawCenteredString(font, "Opens official project pages in your browser", width / 2, 24, 0xAAAAAA);
            super.render(graphics, mouseX, mouseY, delta);
        }

        @Override
        public void onClose() {
            Minecraft.getInstance().setScreen(parent);
        }
    }
}
