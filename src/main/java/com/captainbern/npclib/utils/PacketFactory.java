package com.captainbern.npclib.utils;

import com.captainbern.npclib.npc.NPC;
import com.captainbern.npclib.npc.SlotType;
import com.captainbern.npclib.utils.protocol.Packet;
import com.captainbern.npclib.utils.protocol.Protocol;
import com.captainbern.npclib.utils.protocol.Sender;
import com.google.common.collect.Lists;

import java.util.List;

public class PacketFactory {

    public static Object craftSpawnPacket(NPC npc) {
        Packet packet = new Packet(Protocol.PLAY, Sender.SERVER, 12);
        packet.write("a", npc.getEntityID());
        packet.write("b", npc.getGameProfile());
        packet.write("c", asFixedPoint(npc.getLocation().getX()));
        packet.write("d", asFixedPoint(npc.getLocation().getY()));
        packet.write("e", asFixedPoint(npc.getLocation().getZ()));
        packet.write("f", toPackedByte(npc.getLocation().getYaw()));
        packet.write("g", toPackedByte(npc.getLocation().getPitch()));
        packet.write("h", npc.getInventory(SlotType.ITEM_IN_HAND).getTypeId());// item in hand (id)
        packet.write("i", npc.getDataWatcher().getHandle());
        return packet.getHandle();
    }

    public static Object craftSleepPacket(NPC npc) {
        Packet packet = new Packet(Protocol.PLAY, Sender.SERVER, 10);
        packet.write("a", npc.getEntityID());
        packet.write("b", npc.getLocation().getBlockX());
        packet.write("c", npc.getLocation().getBlockY());
        packet.write("d", npc.getLocation().getBlockZ());
        return packet.getHandle();
    }

    public static List<Object> craftEquipmentPacket(NPC npc) {
        List<Object> packets = Lists.newArrayList();

        for(SlotType type : SlotType.values()) {
            if(npc.getInventory(type) != null) {
                Packet packet = new Packet(Protocol.PLAY, Sender.SERVER, 4);
                packet.write("a", npc.getEntityID());
                packet.write("b", type.toNumeric());
                packet.write("c", ItemUtil.toNMS(npc.getInventory(type)));
                packets.add(packet.getHandle());
            }
        }
        return packets;
    }

    public static Object craftArmSwingPacket(NPC npc) {
        Packet packet = new Packet(Protocol.PLAY, Sender.SERVER, 11);
        packet.write("a", npc.getEntityID());
        packet.write("b", 0);
        return packet.getHandle();
    }

    public static Object craftHurtPacket(NPC npc) {
        Packet packet = new Packet(Protocol.PLAY, Sender.SERVER, 11);
        packet.write("a", npc.getEntityID());
        packet.write("b", 1);
        return packet.getHandle();
    }

    public static Object craftLeaveBedPacket(NPC npc) {
        Packet packet = new Packet(Protocol.PLAY, Sender.SERVER, 11);
        packet.write("a", npc.getEntityID());
        packet.write("b", 3);
        return packet.getHandle();
    }

    public static Object craftMetaDataPacket(NPC npc) {
        Packet packet = new Packet(Protocol.PLAY, Sender.SERVER, 28);
        packet.write("a", npc.getEntityID());
        packet.write("b", npc.getDataWatcher().getHandle());
        return packet.getHandle();
    }

    public static Object craftDestroyPacket(NPC npc) {
        Packet packet = new Packet(Protocol.PLAY, Sender.SERVER, 19);
        packet.write("a", new int[]{npc.getEntityID()});
        return packet.getHandle();
    }

    private static int asFixedPoint(double value) {
        return (int) (value * 32.0D);
    }

    private static double fromFixedPoint(double value) {
        return value / 32.0D;
    }

    private static byte toPackedByte(float f) {
        return (byte) ((byte) f * 256.0F / 360.0F);
    }
}
