# guild

Plugin de guild réalisé pour le network Evershell. Toutes les donnés sont synchronisés entres les serveurs
via les channels messages & MySQL ( 2 requêtes par connexion ). 

Commandes:
  - /guild create (guild.command.create)
  - /guild delete (guild.command.delete)
  - /guild accept (guild.command.accept)
  - /guild deny (guild.command.deny)
  - /guild invite (guild.command.invite)
  - /guild kick (guild.command.kick)
  - /guild promote (guild.command.promote)
  - /guild demote (guild.command.demote)
  - /guild info (guild.command.info)
  - /guild help (guild.command.help)
  - /guild sethome (guild.command.sethome)
  - /guild home (guild.command.home)

Fonctionnalités:
  - Système de home de guilde entre les serveurs.
  - Chat de guilde entre les serveurs.
  - 3 grades dans les guildes ( Chef, Officiers, Membre)
  - Nécessite de mettre les informations qu'une seule fois sur le fichier config.yml du bungee
  pour que tous les serveurs spigot puissent s'y connecter.
  - Possibilité de création d'une "île de guilde" sur un serveur et monde différent, c'est à dire une île sur laquelle
  les membres d'une guilde pourront faire évoluer.
  - PNJ sur l'île de guilde peut-être bougé.
  - Coffre de guilde sur l'île de guilde.
  - Système d'achat de schématique avec les points boutiques ( nécessite EverPB ) et de stockage de ceux-ci. Les membres
  de la guilde peuvent ensuite placer les schématiques à leurs guises en le sélectionnant et en sneakant à l'endroit souhaité.
  
Nécessite:
 - EverPB ( Points boutiques )
 - guild-bungee ( Synchronisation des données des guildes )
  
