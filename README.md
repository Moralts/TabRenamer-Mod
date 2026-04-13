# TabRenamer

一个简单的，允许服务器管理员在客户端视觉上的 Tab 列表中的玩家名称进行替换的模组。

A simple mod that allows server admins to visually replace player names in the client-side Tab list.

## 运行要求 / Requirements

- Minecraft `1.20.1`
- Fabric Loader `>=0.16.9`
- Java `>=17`
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin)
- [Cloth Config](https://modrinth.com/mod/cloth-config) `>=11.0.0`
- [Mod Menu](https://modrinth.com/mod/modmenu) `>=7.0.0`（可选 / Optional）

## 功能与命令 / Features & Commands

服务端管理员（权限等级 ≥ 2）可使用以下命令管理重命名规则，规则会自动同步到所有在线客户端：

| 命令 / Command | 说明 / Description |
|---|---|
| `/tabrenamer set <玩家选择器> "<替换文本>"` | 设置重命名规则，支持 `&` 颜色代码 |
| `/tabrenamer remove <原名>` | 移除指定玩家的重命名规则 |
| `/tabrenamer list` | 列出所有重命名规则 |
| `/tabrenamer clear` | 清除所有重命名规则 |

客户端功能：
- 通过 Cloth Config / Mod Menu 配置面板可开关 Tab 列表重命名功能
- 支持多语言（中文 / English）

## 技术栈 / Tech Stack

- **语言**: Kotlin + Java (Mixin)
- **框架**: Fabric Mod Loader / Fabric API
- **构建**: Gradle + Fabric Loom
- **配置**: Cloth Config API
- **网络**: Fabric Networking v1（自定义通道 `tabrenamer:sync_rules`）
- **注入**: Mixin（`PlayerInfo.getTabListDisplayName()`）

## License

Apache License 2.0
