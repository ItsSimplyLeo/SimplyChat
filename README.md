# SimplyChat
(PLUGIN CURRENTLY UNDER EARLY DEVELOPMENT USE AT OWN RISK!)

Modern chat plugin developed for multiple use-cases utilizing modern PaperAPI. This project will not make any promises to support legacy version support.

Download
------
Right now the only way to obtain a jar is to build it yourself with the instructions below. In the future there will be a direct download.

To compile yourself:
1) Clone this repo
2) Run `./gradlew build` in your terminal
3) Use the `SimplyChat-{version}.jar` file from the `build/libs/` directory

Format configuration
-----
We utilize the use of the adventure and MiniMessage APIs. You can test your MiniMessage strings with their web-tool https://webui.advntr.dev/

An example for the default chat, we created a `<style:{NAME}>` tag for MiniMessage which will implement the predefined styles in `styles.yml` - Do note that this is planned to be changed into a MySQL/Database system.
```yaml
formats:
  default:
    chat: "<prefix><gray><style:default><action:name><name></action></style><dark_gray><suffix>:</dark_gray> <message>"
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