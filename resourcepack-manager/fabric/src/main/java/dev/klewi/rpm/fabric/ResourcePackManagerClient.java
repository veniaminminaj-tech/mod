package dev.klewi.rpm.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class ResourcePackManagerClient implements ClientModInitializer {
    private static KeyMapping openManager;

    @Override
    public void onInitializeClient() {
        openManager = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.resourcepackmanager.open",
                GLFW.GLFW_KEY_K,
                KeyMapping.Category.MISC
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openManager.consumeClick()) {
                if (client.screen == null) client.setScreen(new ManagerScreen(null));
            }
        });
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
            int y = height / 2 - 37;
            addRenderableWidget(Button.builder(Component.literal("Manage Resource Packs"), b -> {
                Minecraft mc = Minecraft.getInstance();
                mc.setScreen(new PackSelectionScreen(this, mc.getResourcePackRepository(),
                        mc.getResourcePackDirectory(), Component.literal("Resource Packs")));
            }).bounds(cx - 110, y, 220, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Featured Packs & Performance Mods"), b ->
                    Minecraft.getInstance().setScreen(new CatalogScreen(this)))
                    .bounds(cx - 110, y + 25, 220, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Browse Modrinth"), b ->
                    net.minecraft.Util.getPlatform().openUri("https://modrinth.com"))
                    .bounds(cx - 110, y + 50, 220, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Close"), b -> onClose())
                    .bounds(cx - 110, y + 75, 220, 20).build());
        }

        @Override
        public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            renderBackground(graphics, mouseX, mouseY, delta);
            graphics.drawCenteredString(font, title, width / 2, height / 2 - 72, 0xFFFFFF);
            super.render(graphics, mouseX, mouseY, delta);
        }

        @Override public void onClose() { Minecraft.getInstance().setScreen(parent); }
    }

    private static final class CatalogScreen extends Screen {
        private final Screen parent;
        CatalogScreen(Screen parent) {
            super(Component.literal("Featured Packs & Performance Mods"));
            this.parent = parent;
        }

        private void open(String url) {
            net.minecraft.Util.getPlatform().openUri(url);
        }

        @Override
        protected void init() {
            int cx = width / 2;
            int y = Math.max(28, height / 2 - 105);
            addRenderableWidget(Button.builder(Component.literal("Faithful 32x — Vanilla-style textures"), b ->
                    open("https://modrinth.com/resourcepack/faithful-32x"))
                    .bounds(cx - 145, y, 290, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Ashen 16x — Medieval fantasy"), b ->
                    open("https://modrinth.com/resourcepack/ashen"))
                    .bounds(cx - 145, y + 24, 290, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Excalibur — Medieval fantasy"), b ->
                    open("https://modrinth.com/resourcepack/excal"))
                    .bounds(cx - 145, y + 48, 290, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Sodium — Rendering / FPS"), b ->
                    open("https://modrinth.com/mod/sodium"))
                    .bounds(cx - 145, y + 78, 290, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Lithium — Game logic optimization"), b ->
                    open("https://modrinth.com/mod/lithium"))
                    .bounds(cx - 145, y + 102, 290, 20).build());
            addRenderableWidget(Button.builder(Component.literal("FerriteCore — Memory optimization"), b ->
                    open("https://modrinth.com/mod/ferrite-core"))
                    .bounds(cx - 145, y + 126, 290, 20).build());
            addRenderableWidget(Button.builder(Component.literal("ImmediatelyFast — Rendering optimization"), b ->
                    open("https://modrinth.com/mod/immediatelyfast"))
                    .bounds(cx - 145, y + 150, 290, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Entity Culling — Skip unseen entities"), b ->
                    open("https://modrinth.com/mod/entityculling"))
                    .bounds(cx - 145, y + 174, 290, 20).build());
            addRenderableWidget(Button.builder(Component.literal("Back"), b ->
                    Minecraft.getInstance().setScreen(parent))
                    .bounds(cx - 145, y + 204, 290, 20).build());
        }

        @Override
        public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            renderBackground(graphics, mouseX, mouseY, delta);
            graphics.drawCenteredString(font, title, width / 2, 12, 0xFFFFFF);
            graphics.drawCenteredString(font, "Opens official Modrinth project pages", width / 2, 24, 0xAAAAAA);
            super.render(graphics, mouseX, mouseY, delta);
        }

        @Override public void onClose() { Minecraft.getInstance().setScreen(parent); }
    }
}
