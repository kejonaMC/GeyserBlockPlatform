[![Discord](https://img.shields.io/discord/853331530004299807?color=7289da&label=discord&logo=discord&logoColor=white)](https://discord.gg/M2SvqCu4e9)

[Download](https://ci.kejonamc.dev/job/GeyserBlockPlatform/job/master/)

# GeyserBlockPlatform
Prevent specific Bedrock platforms from joining your server!

## Permission

| Info               | Permission                   |
|--------------------|------------------------------|
| Give player bypass | `geyserblockplatform.bypass` |

## Configuration

### `no-access-message`:
*default::* ` "&6Sorry your platform does not have access to this server!"`

Message the blocked player will receive, Supports colorcode.

### `deny-server-access`:
*Default:* `- "serverhere"`

Proxy options to exclude specified server. Setting this to `- "all"` will block blocked platforms on all back-end servers.


### `Note`
#### Running GeyserBlockPlatforms on a proxy will require our fork of floodgate [kejonaMC-Floodgate](https://ci.kejona.dev/job/Floodgate/job/master/)
