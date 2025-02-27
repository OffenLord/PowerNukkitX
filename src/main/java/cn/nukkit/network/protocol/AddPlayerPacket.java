package cn.nukkit.network.protocol;

import cn.nukkit.Server;
import cn.nukkit.api.PowerNukkitXOnly;
import cn.nukkit.api.Since;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.types.EntityLink;
import cn.nukkit.network.protocol.types.PropertySyncData;
import cn.nukkit.utils.Binary;
import lombok.ToString;

import java.util.UUID;

/**
 * @author MagicDroidX (Nukkit Project)
 */
@ToString
public class AddPlayerPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.ADD_PLAYER_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public UUID uuid;
    public String username;
    public long entityUniqueId;
    public long entityRuntimeId;
    public String platformChatId = "";
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public float pitch;
    public float yaw;
    public Item item;
    public int gameType = Server.getInstance().getGamemode();
    public EntityMetadata metadata = new EntityMetadata();
    @PowerNukkitXOnly
    @Since("1.19.40-r1")
    public PropertySyncData syncedProperties = new PropertySyncData(new int[]{}, new float[]{});
    public EntityLink[] links = EntityLink.EMPTY_ARRAY;
    public String deviceId = "";
    public int buildPlatform = -1;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putUUID(this.uuid);
        this.putString(this.username);
//        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putString(this.platformChatId);
        this.putVector3f(this.x, this.y, this.z);
        this.putVector3f(this.speedX, this.speedY, this.speedZ);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw); //TODO headrot
        this.putLFloat(this.yaw);
        this.putSlot(this.item);
        this.putVarInt(this.gameType);
        this.put(Binary.writeMetadata(this.metadata));
        //syncedProperties
        this.putUnsignedVarInt(this.syncedProperties.intProperties().length);
        for (int i = 0, len = this.syncedProperties.intProperties().length; i < len; ++i) {
            this.putUnsignedVarInt(i);
            this.putVarInt(this.syncedProperties.intProperties()[i]);
        }
        this.putUnsignedVarInt(this.syncedProperties.floatProperties().length);
        for (int i = 0, len = this.syncedProperties.floatProperties().length; i < len; ++i) {
            this.putUnsignedVarInt(i);
            this.putLFloat(this.syncedProperties.floatProperties()[i]);
        }
//        this.putUnsignedVarInt(0); //TODO: Adventure settings
//        this.putUnsignedVarInt(0);
//        this.putUnsignedVarInt(0);
//        this.putUnsignedVarInt(0);
//        this.putUnsignedVarInt(0);
        this.putLLong(entityUniqueId);
        this.putUnsignedVarInt(0); // playerPermission
        this.putUnsignedVarInt(0); // commandPermission
        this.putUnsignedVarInt(1); // abilitiesLayer size
        this.putLShort(1); // BASE layer type
        this.putLInt(262143); // abilitiesSet - all abilities
        this.putLInt(63); // abilityValues - survival abilities
        this.putLFloat(0.1f); // flySpeed
        this.putLFloat(0.05f); // walkSpeed
        this.putUnsignedVarInt(this.links.length);
        for (EntityLink entityLink : this.links) {
            this.putEntityLink(entityLink);
        }
        this.putString(deviceId);
        this.putLInt(buildPlatform);
    }
}
