# TabRenamer

A simple mod that allows server admins to visually replace player names in the client-side Tab list.

一个简单的模组，允许服务器管理员在客户端视觉上的 Tab 列表中替换玩家名称。

## Requirements / 运行要求

- Minecraft `1.20.1`
- Fabric Loader `>=0.16.9`
- Java `>=17`
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)
- [Cloth Config](https://modrinth.com/mod/cloth-config) `>=11.0.0`
- [Mod Menu](https://modrinth.com/mod/modmenu) `>=7.0.0` (Optional / 可选)

## Features & Commands / 功能与命令

Server admins (permission level ≥ 2) can use the following commands to manage renaming rules. Rules are automatically synchronized to all online clients.

服务端管理员（权限等级 ≥ 2）可使用以下命令管理重命名规则，规则会自动同步到所有在线客户端：

| Command / 命令 | Description / 说明 |
|---|---|
| `/tabrenamer set <player selector> "<replacement text>"` | Set a renaming rule; supports `&` color codes / 设置重命名规则，支持 `&` 颜色代码 |
| `/tabrenamer remove <original name>` | Remove the renaming rule for a specific player / 移除指定玩家的重命名规则 |
| `/tabrenamer list` | List all renaming rules / 列出所有重命名规则 |
| `/tabrenamer clear` | Clear all renaming rules / 清除所有重命名规则 |

Client-side features / 客户端功能：
- Toggle Tab list renaming via the Cloth Config / Mod Menu configuration panel / 通过 Cloth Config / Mod Menu 配置面板可开关 Tab 列表重命名功能
- Supports multiple languages (Chinese / English) / 支持多语言（中文 / English）

## Tech Stack / 技术栈

- **Language / 语言**: Kotlin + Java (Mixin)
- **Framework / 框架**: Fabric Mod Loader / Fabric API
- **Build / 构建**: Gradle + Fabric Loom
- **Config / 配置**: Cloth Config API
- **Network / 网络**: Fabric Networking v1 (Custom channel `tabrenamer:sync_rules` / 自定义通道 `tabrenamer:sync_rules`)
- **Injection / 注入**: Mixin (`PlayerInfo.getTabListDisplayName()`)

## License / 许可证

Apache License 2.0
