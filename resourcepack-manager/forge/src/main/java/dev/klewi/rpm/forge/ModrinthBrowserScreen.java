package dev.klewi.rpm.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ModrinthBrowserScreen extends Screen {
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(12)).followRedirects(HttpClient.Redirect.NORMAL).build();

    private final Screen parent;
    private EditBox searchBox;
    private final List<PackResult> results = new ArrayList<>();
    private String status = "Search Modrinth for Minecraft 1.21.11 resource packs.";
    private int pageOffset;

    public ModrinthBrowserScreen(Screen parent) {
        super(Component.literal("Modrinth Resource Packs"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = width / 2;
        searchBox = new EditBox(font, cx - 155, 36, 230, 20, Component.literal("Search resource packs"));
        searchBox.setHint(Component.literal("e.g. Faithful, medieval, PvP"));
        addRenderableWidget(searchBox);
        addRenderableWidget(Button.builder(Component.literal("Search"), b -> search())
                .bounds(cx + 80, 36, 75, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Previous"), b -> {
            if (pageOffset > 0) { pageOffset -= 5; renderResults(); }
        }).bounds(cx - 155, height - 32, 90, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Next"), b -> {
            if (pageOffset + 5 < results.size()) { pageOffset += 5; renderResults(); }
        }).bounds(cx - 45, height - 32, 90, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Back"), b -> Minecraft.getInstance().setScreen(parent))
                .bounds(cx + 65, height - 32, 90, 20).build());
        search();
    }

    private void search() {
        String query = searchBox == null ? "" : searchBox.getValue().trim();
        status = "Searching Modrinth…";
        results.clear();
        pageOffset = 0;
        String url = "https://api.modrinth.com/v2/search?facets=%5B%5B%22project_type%3Aresourcepack%22%5D%5D&limit=20&query="
                + URLEncoder.encode(query, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(20)).header("User-Agent", "KlewiResourcePackManager/0.3.0").GET().build();
        HTTP.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    JsonObject root = JsonParser.parseString(body).getAsJsonObject();
                    JsonArray hits = root.getAsJsonArray("hits");
                    List<PackResult> found = new ArrayList<>();
                    for (int i = 0; i < hits.size(); i++) {
                        JsonObject hit = hits.get(i).getAsJsonObject();
                        found.add(new PackResult(
                                hit.get("project_id").getAsString(),
                                hit.get("slug").getAsString(),
                                safe(hit, "title", "Untitled resource pack"),
                                safe(hit, "description", "No description provided."),
                                safe(hit, "icon_url", "")
                        ));
                    }
                    Minecraft.getInstance().execute(() -> {
                        results.clear(); results.addAll(found); pageOffset = 0;
                        status = found.isEmpty() ? "No resource packs found." : "Found " + found.size() + " resource packs. Select one to download.";
                        renderResults();
                    });
                })
                .exceptionally(error -> {
                    Minecraft.getInstance().execute(() -> status = "Search failed. Check your internet connection and try again.");
                    return null;
                });
    }

    private static String safe(JsonObject object, String key, String fallback) {
        return object.has(key) && !object.get(key).isJsonNull() ? object.get(key).getAsString() : fallback;
    }

    private void renderResults() {
        String currentQuery = searchBox == null ? "" : searchBox.getValue();
        clearWidgets();
        int cx = width / 2;
        searchBox = new EditBox(font, cx - 155, 36, 230, 20, Component.literal("Search resource packs"));
        searchBox.setValue(currentQuery);
        searchBox.setHint(Component.literal("e.g. Faithful, medieval, PvP"));
        addRenderableWidget(searchBox);
        addRenderableWidget(Button.builder(Component.literal("Search"), b -> search())
                .bounds(cx + 80, 36, 75, 20).build());

        int visible = Math.min(5, Math.max(0, results.size() - pageOffset));
        for (int i = 0; i < visible; i++) {
            PackResult result = results.get(pageOffset + i);
            int y = 72 + i * 42;
            addRenderableWidget(Button.builder(Component.literal("Download: " + trim(result.title, 34)), b -> download(result))
                    .bounds(cx - 155, y, 310, 20).build());
        }
        addRenderableWidget(Button.builder(Component.literal("Previous"), b -> {
            if (pageOffset > 0) { pageOffset -= 5; renderResults(); }
        }).bounds(cx - 155, height - 32, 90, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Next"), b -> {
            if (pageOffset + 5 < results.size()) { pageOffset += 5; renderResults(); }
        }).bounds(cx - 45, height - 32, 90, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Back"), b -> Minecraft.getInstance().setScreen(parent))
                .bounds(cx + 65, height - 32, 90, 20).build());
    }

    private static String trim(String value, int max) {
        return value.length() <= max ? value : value.substring(0, max - 1) + "…";
    }

    private void download(PackResult pack) {
        status = "Finding a compatible Forge file for " + pack.title + "…";
        String versionsUrl = "https://api.modrinth.com/v2/project/" + pack.projectId
                + "/version?game_versions=%5B%221.21.11%22%5D&loaders=%5B%22forge%22%5D";
        HttpRequest request = HttpRequest.newBuilder(URI.create(versionsUrl))
                .timeout(Duration.ofSeconds(20)).header("User-Agent", "KlewiResourcePackManager/0.3.0").GET().build();
        HTTP.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    JsonArray versions = JsonParser.parseString(body).getAsJsonArray();
                    if (versions.isEmpty()) throw new IllegalStateException("No compatible version");
                    JsonObject version = versions.get(0).getAsJsonObject();
                    JsonArray files = version.getAsJsonArray("files");
                    JsonObject chosen = files.get(0).getAsJsonObject();
                    for (int i = 0; i < files.size(); i++) {
                        JsonObject f = files.get(i).getAsJsonObject();
                        if (f.has("primary") && f.get("primary").getAsBoolean()) { chosen = f; break; }
                    }
                    String fileUrl = chosen.get("url").getAsString();
                    String fileName = chosen.get("filename").getAsString();
                    if (!fileName.toLowerCase().endsWith(".zip")) throw new IllegalStateException("Not a ZIP resource pack");
                    HttpRequest fileRequest = HttpRequest.newBuilder(URI.create(fileUrl))
                            .timeout(Duration.ofMinutes(2)).header("User-Agent", "KlewiResourcePackManager/0.3.0").GET().build();
                    HttpResponse<InputStream> response = HTTP.send(fileRequest, HttpResponse.BodyHandlers.ofInputStream());
                    Path targetDir = Minecraft.getInstance().getResourcePackDirectory();
                    Files.createDirectories(targetDir);
                    Path target = targetDir.resolve(fileName).normalize();
                    if (!target.getParent().equals(targetDir.normalize())) throw new IllegalStateException("Invalid file name");
                    try (InputStream input = response.body()) {
                        Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                    Minecraft.getInstance().execute(() -> status = "Downloaded " + fileName + ". Open Manage Resource Packs to enable it.");
                })
                .exceptionally(error -> {
                    Minecraft.getInstance().execute(() -> status = "Download unavailable for Minecraft 1.21.11 Forge, or the network failed.");
                    return null;
                });
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(font, title, width / 2, 12, 0xFFFFFF);
        graphics.drawCenteredString(font, status, width / 2, 61, 0xBBBBBB);
        for (int i = 0; i < Math.min(5, results.size() - pageOffset); i++) {
            PackResult result = results.get(pageOffset + i);
            int y = 96 + i * 42;
            graphics.drawString(font, trim(result.title + " — " + result.description, Math.max(20, width / 6)), width / 2 - 150, y, 0xDDDDDD, false);
            if (!result.iconUrl.isBlank()) {
                graphics.drawString(font, "Image: Modrinth thumbnail available", width / 2 - 150, y + 12, 0x888888, false);
            }
        }
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parent);
    }

    private record PackResult(String projectId, String slug, String title, String description, String iconUrl) {}
}
