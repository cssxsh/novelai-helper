# NovelAi Helper

> 基于 [NovelAi](https://novelai.net/image) 的 AI图片生成插件

[![Release](https://img.shields.io/github/v/release/cssxsh/novelai-helper)](https://github.com/cssxsh/novelai-helper/releases)
![Downloads](https://img.shields.io/github/downloads/cssxsh/novelai-helper/total)
[![MiraiForum](https://img.shields.io/badge/post-on%20MiraiForum-yellow)](https://mirai.mamoe.net/topic/1657)

**使用前应该查阅的相关文档或项目**

*   [User Manual](https://github.com/mamoe/mirai/blob/dev/docs/UserManual.md)
*   [Permission Command](https://github.com/mamoe/mirai/blob/dev/mirai-console/docs/BuiltInCommands.md#permissioncommand)
*   [Chat Command](https://github.com/project-mirai/chat-command)

会自动下载 [EhTagTranslation](https://github.com/EhTagTranslation/Database) 翻译词库  
可以对接 [NaiFu](#NaiFu) 本地搭建  
或者你可以 利用 [colab](https://colab.research.google.com/drive/1_Ma71L6uGbtt6UQyA3FjqW2lcZ5Bjck-#scrollTo=KZ88G-iWCTs7) 进行在线搭建  
将搭建得到的 url 例如 `https://express-disco-environmental-friends.trycloudflare.com/` 填入 `config.yml` 的 `naifu_api` 配置项  

**如果你是用 Stable Diffusion web UI 搭建的API** 请移步 https://github.com/cssxsh/stable-diffusion-helper

## 指令

*   `/nai <word>` 生成一张图片 官方API需要登录  
    例如 `/nai 连裤袜 双马尾` (只有部分词条会自动翻译)  
    例如 `/nai swimsuit #seed=12346` (设置种子)  
    例如 `/nai swimsuit #steps=3` (AI迭代次数)  
    例如 `/nai "swimsuit, ahegao"` (如果需要以 `,` 分割词条, 请用 `"` 包裹)  
    例如 `/nai 连裤袜 [图片]` (以图生图, `[图片]` 是指指令消息中包含有图片)
    可用的配置项有  
    `seed` 种子  
    `steps` 迭代次数  
    `width` 宽度  
    `height` 高度  
    `scale` 比例  
    `sampler` 采样器 可选值 `k_euler_ancestral`, `k_euler`, `k_lms`, `plms`, `ddim`  
    `strength` 以图出图中对原图的更改程度 可选值 [0.00, 0.99]  
    `noise` 以图出图中的噪声 可选值 [0.00, 0.99]

*   `/nai-fu <word>` 生成一张图片 自建API需要配置 naifu_api  
    对接 `naifu`, `naifu` 是基于 novelai 官方 web 端的修改版，所以指令用法 和 `nai` 一致

*   `/nai-login <mail> <password>` 登录账号  
    例如 `/nai-login 114514@gmail.com 1919810`

*   `/nai-reload` 重新载入 `config.yml` 配置文件

## 配置

*   `config.yml` 配置文件 包括 `proxy`, `doh`, `ipv6`, `naifu_api` 等配置
    *   `proxy` 代理
    *   `doh` DNS
    *   `ipv6` 是否使用ipv6
    *   `naifu_api` 自建 naifu 地址
    *   `command_interval` 命令间隔延迟时间 单位毫秒

*   `ban.txt` 屏蔽的词条，可热编辑，保存后一段时间会自动启用

## NaiFu

`naifu` 是基于 novelai 官方 web 端的修改版  
相关信息可以看这 <https://colab.research.google.com/drive/1_Ma71L6uGbtt6UQyA3FjqW2lcZ5Bjck-#scrollTo=KZ88G-iWCTs7>

## TODO

* [ ] 更好的翻译
* [ ] 更多配置项