# SimplyChat
(PLUGIN CURRENTLY UNDER EARLY DEVELOPMENT USE AT OWN RISK!)

Modern chat plugin developed for multiple use-cases utilizing modern PaperAPI. This project will not make any promises to support legacy version support.

Download
------
Right now the only way to obtain a jar is to build it yourself with the instructions below. In the future there will be a direct download.

To compile yourself:
1) Clone this repo
2) Run `./gradlew build` in your terminal
3) Grab the `SimplyChat-1.0.0.jar` file from the `build/libs/` directory

Format configuration
-----
To avoid clustered strings in the configurations from the nature of MiniMessage, this system uses a `<chat:{NAME}>` key to apply Hover and Click actions.
```yaml
formats:
  default:
    chat: "<prefix><gray><chat:name><name></chat><dark_gray><suffix>:</dark_gray> <message>"
    actions:
      name:
        HOVER_TEXT:
          - "<aqua><b><name>'s Profile"
          - ""
          - "<gray>Nickname: <aqua><nickname>"
          - ""
          - "<aqua>Click to /message user."
        SUGGEST_COMMAND: "/msg <name>"
```