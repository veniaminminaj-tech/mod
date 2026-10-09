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

        @Override
        public void onClose() {
            Minecraft.getInstance().setScreen(parent);
        }
    }
}
