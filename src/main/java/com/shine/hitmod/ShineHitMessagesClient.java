package com.shine.hitmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

/**
 * SHINE Hit Messages
 * <p>
 * Client-side only mod. Shows a short-lived message on the right side of the
 * HUD whenever the local player lands a real attack on an entity:
 * <ul>
 *   <li>CRIT (red) - a genuine vanilla critical hit</li>
 *   <li>YEAHH (green) - a normal hit while sprinting</li>
 *   <li>AHH.. (yellow) - any other normal hit</li>
 * </ul>
 * This mod does not modify combat, damage, or timing in any way - it only
 * reads state to decide what text to draw.
 */
public class ShineHitMessagesClient implements ClientModInitializer {

    /** How long each message stays on screen, in milliseconds. */
    private static final long DISPLAY_DURATION_MS = 650L; // ~0.65s, inside the requested 0.6-0.7s window

    private static final int COLOR_CRIT = 0xFFFF3B30;   // red
    private static final int COLOR_NORMAL = 0xFFFFD500;  // yellow
    private static final int COLOR_SPRINT = 0xFF3CE04A;  // green

    private static String activeMessage = "";
    private static int activeColor = 0xFFFFFFFF;
    private static long expiresAtMs = 0L;

    @Override
    public void onInitializeClient() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient && player instanceof PlayerEntity attacker && entity instanceof LivingEntity) {
                boolean critical = isVanillaCriticalHit(attacker);
                boolean sprinting = attacker.isSprinting();

                if (critical) {
                    show("CRIT", COLOR_CRIT);
                } else if (sprinting) {
                    show("YEAHH", COLOR_SPRINT);
                } else {
                    show("AHH..", COLOR_NORMAL);
                }
            }
            return ActionResult.PASS;
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> renderMessage(drawContext));
    }

    private static boolean isVanillaCriticalHit(PlayerEntity player) {
        return player.fallDistance > 0.0F
                && !player.isOnGround()
                && !player.isClimbing()
                && !player.isTouchingWater()
                && !player.hasVehicle()
                && !player.isSprinting()
                && !player.hasStatusEffect(StatusEffects.BLINDNESS);
    }

    private static void show(String text, int argbColor) {
        activeMessage = text;
        activeColor = argbColor;
        expiresAtMs = System.currentTimeMillis() + DISPLAY_DURATION_MS;
    }

    private static void renderMessage(DrawContext context) {
        if (activeMessage.isEmpty() || System.currentTimeMillis() >= expiresAtMs) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.textRenderer == null || client.getWindow() == null) {
            return;
        }
        if (client.currentScreen != null) {
            return;
        }

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int textWidth = client.textRenderer.getWidth(activeMessage);
        int x = screenWidth - textWidth - 14;
        int y = screenHeight / 2 - 10;

        context.drawTextWithShadow(client.textRenderer, activeMessage, x, y, activeColor);
    }
}
