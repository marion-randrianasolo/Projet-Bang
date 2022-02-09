# ![](ressources/logo.jpeg)

# Introduction aux interfaces homme-machine (IHM)

### IUT Montpellier-Sète – Département Informatique

* **Cours:** [M2105](http://cache.media.enseignementsup-recherche.gouv.fr/file/25/09/7/PPN_INFORMATIQUE_256097.pdf) - Le support de cours est [consultable ici](https://iutinfomontp-m2105.github.io/Cours).
* **Enseignants:** [Kevin Chapuis](mailto:kevin.chapuis@umontpellier.fr), [Sophie Nabitz](mailto:sophie.nabitz@univ-avignon.fr), [Rémy Portier](mailto:remyportier34@gmail.com)
* Le [forum Piazza](https://piazza.com/class/kopinpctu3p678) de ce cours pour poser vos questions
* [Email](mailto:sophie.nabitz@univ-avignon.fr) pour une question d'ordre privée concernant le cours.


# Projet - _IHM Bang !_

**Date de rendu : 12 juin 2021 à 23h00**

Le lien pour le fork avec GitHub Classroom et les consignes du projet vous sont données dans le fichier [Consignes.md](Consignes.md).
_**Le non-respect de ces consignes impliquera des pénalités significatives sur la note du projet**_.

## Présentation
Le but de ce projet est de produire une implémentation en _JavaFX_ de l'IHM du jeu de cartes [_Bang!_](https://fr.wikipedia.org/wiki/Bang!_(jeu_de_cartes)). 
Vous connaissez déjà ce jeu pour l'avoir implémenté dans votre projet POO, et, si besoin, vous pouvez en retrouver les règles dans le [dépôt consacré à ce projet précédent](https://github.com/IUTInfoMontp-M2103/ProjetBang).

Dans ce projet IHM, vous partez d'une version du moteur déjà implémentée (la logique de l'application), que vous n'aurez, a priori, pas à modifier. Vous considèrerez donc cette partie comme totalement encapsulée, non invocable directement, et vous la ferez exécuter en utilisant des classes Java jouant un rôle de façade permettant de communiquer avec la logique interne. Les méthodes de ces classes sont sommairement commentées, vous devez vous y référer (en complément du présent document) afin d'identifier laquelle utiliser dans quelle situation.

Vous allez implémenter plusieurs classes héritant de classes abstraites prédéfinies, qui définissent des méthodes elles-aussi brièvement commentées.

Ces classes sont exposées dans un diagramme de classes simplifié de l'application, à consulter en détails pour mieux vous situer.

**Remarque :** les identificateurs des classes, attributs et méthodes fournis sont en anglais, et la documentation du code en français.

## Le diagramme de classes de l'application

Pour consulter le [diagramme](ressources/Model.png), cliquez.

Ce modèle ne détaille pas les classes et relations de la partie logique, ni ne liste les fonctions des classes que vous utiliserez. Pour ne pas surcharger le diagramme, les attributs, de nombreuses fonctions, ainsi que les paramètres des fonctions ne sont pas présentés. Vous y voyez surtout les classes qui sont nécessaires à votre travail, et précisemment les méthodes que vous devez implémenter.

Le package *logic*, en gris sur le diagramme, représente la logique interne du jeu. Vous y accédez en utilisant les classes en bleu, dont le nom (qui commence par un *I majuscule*) fait référence aux classes apparaillées. Dans ces classes "façades", vous verrez que de nombreuses propriétés sont déclarées, vous devez les utiliser et définir vos *listeners* qui mettront à jour votre IHM.

La partie qui vous intéresse se trouve dans le package *views*, qui contient les classes d'IHM, classes abstraites à implémenter. Quant à vos classes, qui apparaissent en orange sur le diagramme, elles seront dans le package *ourviews* et peuvent être renommées comme vous le souhaitez.

Les méthodes présentées dans les classes du package *views* sont donc celles que vous allez travailler. Certaines sont abstraites, donc totalement à implémenter. D'autres sont concrètes mais protégées, définies dans la classe de base, mais à invoquer dans la classe dérivée, en général en passant en paramètre les éléments manquants dans la base, le plus souvent vos propres *listeners*.

Les classes d'IHM qui sont proposées héritent, pour la plupart, de la classe [Pane](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/Pane.html), mais vous pouvez changer cette classe de base afin d'utiliser un composant qui vous parait plus approprié, à condition de conserver les fonctions demandées. Toutefois, pour information, le fait de conserver *Pane* en classe de base ne limite probablement pas vos choix de conception de l'IHM, vous pouvez donc utiliser tout *layout* souhaité dans vos classes.

## Quelques mots sur le fonctionnement du moteur du jeu

Il n'est pas utile de lire le code métier proposé pour pouvoir en réaliser l'IHM. Voici toutefois quelques éléments que vous devez avoir à l'esprit.

* Tout d'abord, par rapport à la version totale du jeu, certains personnages n'ont pas été implémentés. Dans tous les cas, leur implémentation n'affecterait que la logique du jeu, et n'aurait aucun effect sur l'IHM que vous allez réaliser. Voici les personnages non implémentés : CalamityJanet, ElGringo, LuckyDuke, PedroRamirez, SidKetchum.

* Le code gère deux listes essentielles : la liste des prochaines cartes jouables, et, dans le cas d'une attaque, la liste des joueurs cibles possibles. Ainsi, toute sélection d'une carte ou d'une cible non attendue n'a aucun effet.

* De nombreuses actions ont été implémentées de façon automatique :
	- Lorsqu'un joueur n'a plus de carte qu'il pourrait jouer, le tour passe automatiquement au joueur suivant.
	- Les cartes sont piochées automatiquement, lorsque le jeu nécessite que le joueur pioche (quelques exemples d'une telle situation : en début de tour, lorsque la carte *General Store* est jouée, si un *Barrel*, un *Jail*, une *Dynamite* font partie des cartes en jeu du joueur,... )
	- Dans le cas d'une attaque, si le joueur cible possède une carte qui lui permet d'esquiver l'attaque, celle-ci est placée systématiquement en fin de la liste des cartes qui représentent la main du joueur. Dans le cas où le joueur attaqué possède plus d'une carte d'esquive, une d'entre elles est choisie aléatoirement pour être placée à la fin. Si le joueur choisit de passer (il ne perd donc pas sa carte), sa main est mélangée, afin de ne pas laisser la carte d'esquive à la fin.
	- Quand la carte *General Store* est jouée, lorsque tous les joueurs sauf le dernier ont choisi leur carte, la dernière carte est automatiquement distribuée au joueur restant.
	- Quand une carte *Panic* ou *CatBalou* est jouée, l'attaquant (le joueur qui a le tour) doit explicitement sélectionner la carte qui doit disparaitre. Si aucune carte d'un autre joueur ne peut être choisie, le jeu passe automatiquement à l'action suivante. 
	- Lorsqu'un joueur a la possibilité de dégainer pour se sortir d'une situation (*Jail*, *Dynamite*, *Bang* avec *Barrel*, ...), si la carte piochée ne résoud pas le problème, le jeu passe automatiquement à l'action suivante.<br/>
&nbsp;

* La classe d'énumération *GameState*, qui représente l'état courant du jeu, peut servir pour identifier la prochaine interaction possible sur l'IHM. Sa méthode *toString()* retourne un message d'information (en anglais) qui serait utile à l'utilisateur. Par exemple, lorsque le joueur (dont c'est le tour) peut choisir une de ses cartes en main, la méthode *toString()* renvoie *"Choose card to play"*.

* La pioche n'est, le plus souvent, pas utile, puisque les cartes sont piochées automatiquement (donc, la plupart du temps, il n'est pas nécessaire &mdash; mais pas interdit &mdash; de la présenter). Par contre, si une carte piochée (dégainée) permet au joueur de se sortir d'une situation, il faut le justifier et pouvoir afficher la carte piochée. Une liste *drawnCards* est gérée en interne dans le jeu, et vous y accèdez via la propriété correspondante déclarée dans *IGame*. Le jeu peut (si vous le lui demandez avec les mots magiques...) supprimer automatiquement la carte piochée de la liste *drawnCards* lorsqu'elle est sélectionnée par l'utilisateur. Ce fonctionnement peut aussi être mis en place lorsque la carte *General Store* est jouée.

* Enfin, lorsque le joueur (dont c'est le tour) a pour personnage Jesse Jones (qui a la possibilité d'utiliser la pioche ou la main d'un autre joueur en début de tour), vous pouvez faire apparaître la pioche (dans le cas où vous choisissez de ne pas le faire en permanence). A cet effet, vous pouvez utiliser la propriété *canDrawPileBeSelected* déclarée dans *IGame* qui permettra de rendre visible ou pas la pioche (*drawPile*).

## Les ressources

Toutes les images représentant les cartes, les rôles, les personnages, se trouvent dans le répertoire *src/main/resources* (un seul s, c'est de l'anglais...). Vous pouvez les utiliser ou définir les vôtres (vous remarquerez qu'il existe une image pour la carte *Colt .45*). Ces images ne présentent pas le nom de la carte, vous pouvez, si souhaité, l'ajouter en le gérant par du code. 

Vous y trouverez aussi des fontes, et un répertoire dans lequel vous pouvez, si vous choisissez cette option, placer vos fichiers FXML.

## Quelques remarques sur certaines des classes abstraites à implémenter

La classe *BangIHM*, qui vous est presque entièrement fournie, est celle qui démarre le jeu. Vous devez instancier les vues de départ, la vue principale du jeu et la vue des résultats en utilisant vos propres classes.

La classe *StartView* est celle qui permet de choisir le nombre de joueurs et renseigner leurs noms. Bien qu'elle apparaisse au début, on peut s'en dispenser dans un premier temps pour faire tourner l'IHM principale, représentée par *GameView*.

Vous remarquerez que la classe *PlayerArea* possède à la fois une méthode *getPlayer()* et une méthode *getIPlayer()*, cette dernière permettant plutôt de rester au niveau de la façade. Vous pouvez être amenés à utiliser l'une à la suite de l'autre si vous souhaitez accéder au joueur effectif.

La classe *DrawnCardView* peut être utilisée quand cela a un sens d'afficher les cartes piochées (par exemple dans le cas de dégaine sur *Barrel*, *Jail*, *Dynamite* ; ou pour proposer de distribuer les cartes piochées par *General Store* ; ou lors de la première étape du tour de certains personnages, ...). Vous pouvez en faire un composant de la vue principale ou une popup. Comme indiqué plus haut, on utilise *drawnCards* pour obtenir les cartes automatiquement piochées, qui sont toutes ajoutées en une seule fois à cette liste (et non pas une par une). 
Par contre, elles sont enlevées une par une, au fur et à mesure des sélections de l'utilisateur.

La classe *ResultsView*, qui permettra d'afficher les résultats lorsque le gagnant est connu, dépendra de la propriété *winners* déclarée dans *IGame*. Dans cette classe, il est nécessaire d'accéder à *BangIHM* si on souhaite proposer à l'utilisateur d'arrêter de jouer ou de recommencer une partie.

## Rendu attendu

L'intégralité du code source du projet doit résider dans le dépôt GitHub associé à votre équipe de projet. Vous devez compléter les classes Java qui vous sont données, tout en en ajoutant de nouvelles si souhaité.

Vous devez implémenter <u>toutes les méthodes</u> qui sont mentionnées sur le diagramme de classe en en <u>respectant les signatures</u>. Dans vos classes, vous pouvez ajouter toutes les méthodes et tous les attributs qui vous sont nécessaires. A priori, il n'est pas nécessaire d'en ajouter dans les classes abstraites, bien que cela ne soit pas interdit.

## Évaluation

L'évaluation du projet se fera sous la forme d'une soutenance d'environ une demi-heure au cours de laquelle vous expliquerez le travail réalisé, la répartition des tâches, et nous ferez une démonstration (que nous dirigerons). Lors de cette soutenance, nous évaluerons aussi bien la partie ergonomie que la partie implémentation. Nous vous informerons prochainement des principaux critères, la réflexion est encore en cours...

# Bon travail à tous !