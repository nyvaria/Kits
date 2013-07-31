Kits
====

Kits is a small but deceptively powerful plugin designed for the Bukkit API, with simple, easy-to-use commands and a Graphical User Interface provided during the creation of a kit.

Commands
====

    /kits - Show all available kits.

    /kits reload - Reload Kits.

    /kit <kitname> - Spawn the specified kit.

    /kit <kitname> [playername] - Spawn the specified kit for the specified player.

    /kit create <kitname> - Create a kit with the specified name.

    /kit create <kitname> [bars] - Same as above with a specified number of inventory bars.

    /kit create <kitname> [bars] [overwrite] - Same as above with a specified overwrite variable (true / false).

    /kit create <kitname> [bars] [overwrite] [delay] - Same as above with a specified delay between kit spawning.

    /kit edit <kitname> - Edit the contents of the specified kit.

    /kit remove <kitname> - Remove the specified kit.

    /kit delay <kitname> - View the delay for the specified kit.

    /kit delay <kitname> [delay] - Change the delay for the specified kit.

    /kit overwrite <kitname> - View the overwrite settings for the specified kit.

    /kit overwrite <kitname> [on/true | off/false] - Change the overwrite settings for the specified kit.

Permissions
====

    kits.spawn.kitname - Players can spawn the specified kit.

    kits.spawn.others.kitname - Players can spawn the specified kit for another player.

    kits.admin - Players can issue Kits administration commands specified above.

    kits.bypassdelay.kitname - Players can bypass the delay on the specified kit.

To apply permissions to all kits instead of adding each individual kit, use the '*' wildcard in the place of "kitname".

Signs
====
Kits can be spawned by right-clicking Kit signs. Kit signs are signs which are set up to display the following text:

     [kit]
    kitname
