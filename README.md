# NovelAi Helper

> 基于 [NovelAi](https://novelai.net/image) 的 AI图片生成插件

[![Release](https://img.shields.io/github/v/release/cssxsh/novelai-helper)](https://github.com/cssxsh/novelai-helper/releases)
![Downloads](https://img.shields.io/github/downloads/cssxsh/novelai-helper/total)
[![maven-central](https://img.shields.io/maven-central/v/xyz.cssxsh.mirai/novelai-helper)](https://search.maven.org/artifact/xyz.cssxsh.mirai/novelai-helper)

**使用前应该查阅的相关文档或项目**

* [User Manual](https://github.com/mamoe/mirai/blob/dev/docs/UserManual.md)
* [Permission Command](https://github.com/mamoe/mirai/blob/dev/mirai-console/docs/BuiltInCommands.md#permissioncommand)
* [Chat Command](https://github.com/project-mirai/chat-command)

会自动下载 [EhTagTranslation](https://github.com/EhTagTranslation/Database) 翻译词库

## 指令

* `/nai <word>` 生成一张图片   
  例如 `/nai 连裤袜 双马尾`
* `/nai-login <mail> <password>` 登录账号  
  例如 `/nai-login 114514@gmail.com 1919810`

## 配置

* `config.yml` 配置文件 包括 `proxy`, `doh`, `ipv6` 等配置
* `ban.txt` 屏蔽的词条，可热编辑，保存后一点时间会自动启用

## TODO

* [ ] 更好的翻译
* [ ] 更多配置项