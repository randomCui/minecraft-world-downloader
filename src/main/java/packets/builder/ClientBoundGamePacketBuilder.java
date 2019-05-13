package packets.builder;

import game.Game;
import game.data.Coordinate2D;
import game.data.Coordinate3D;
import game.data.WorldManager;
import game.data.chunk.ChunkFactory;
import packets.DataTypeProvider;

public class ClientBoundGamePacketBuilder extends PacketBuilder {
    private final int CHUNK_DATA = 0x20;
    private final int UNLOAD_CHUNK = 0x1D;

    private final int PLAYER_POSITION_LOOK = 0x2F;
    private final int VEHICLE_MOVE = 0x29;

    @Override
    public boolean build(int size) {
        DataTypeProvider typeProvider = getReader().withSize(size);
        int packetId = typeProvider.readVarInt();

        switch (packetId) {
            case CHUNK_DATA:
                try {
                    ChunkFactory.addChunk(typeProvider);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case UNLOAD_CHUNK:
                WorldManager.unloadChunk(new Coordinate2D(typeProvider.readInt(), typeProvider.readInt()));
                break;
            case PLAYER_POSITION_LOOK:
            case VEHICLE_MOVE:
                double x = typeProvider.readDouble();
                double y = typeProvider.readDouble();
                double z = typeProvider.readDouble();
                Game.setPlayerPosition(new Coordinate3D(x, y, z).offset());

                break;
        }

        return true;
    }
}
