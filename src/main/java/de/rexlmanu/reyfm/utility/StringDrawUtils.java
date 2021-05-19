package de.rexlmanu.reyfm.utility;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class StringDrawUtils {

    public static void drawCenteredColorizedString(String text, int x, int y, double size, int color) {
        StringDrawUtils.drawColorizedString(text, (int) (x - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) * size / 2)), y, size, color);
    }

    public static void drawColorizedString(String text, int x, int y, double size, int color) {
        GL11.glPushMatrix();
        GL11.glScaled(size, size, size);
        Minecraft.getMinecraft().fontRendererObj.drawString(
                text,
                (int) (x / size),
                (int) (y / size),
                color
        );
        GL11.glPopMatrix();
    }

}
