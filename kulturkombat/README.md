<KulturKombat>
===========

Développé par <Camille GOLLIOT> <Thibault CROISIER>
Contacts : <camille.golliot.etu@univ-lille.fr> , <thibault.croisier.etu@univ-lille.fr>

# Présentation de KulturKombat

KulturKombat est un petit jeu de combat en tour par tour qui teste votre culture général.

Deux joueurs vont s'affronter à l'aide de trois coup:

![choixQuestion](/shots/choixQuestion.png)

Le direct qui, pour être réalisé, pose une question au joueur courant et lui propose deux réponse dont la bonn
Ce coup retire un point de vie

![direct](/shots/direct.png)

Le crochet qui, pour être réalisé, pose une question au joueur courant et lui propose quatre réponse dont la bonne.
Ce coup retire deux point de vie

![crochet](/shots/crochet.png)

L'uppercut qui, pour être réalisé, pose une question au joueur courant, celui ci va devoir taper au clavier la bonne réponse.
Ce coup retire quatre point de vie

![uppercut](/shots/uppercut.png)

KulturKombat fonctionne à l'aide d'un fichier joueurs.csv et questions.csv. Joueurs.csv contient les joueurs qui ont déjà joué au jeux et leurs statistique et question.csv les question et les différentes réponses possibles. La bonne réponse est toujours la première. Si le fichier joueurs.csv n'existe pas, il est automatiquement créé par le jeu.

Le jeu possède un menu principale qui permet d'accéder au scoreboard (qui est triable en fonction des différentes stats), de voir les règles du jeu, de lancer une partie et de quitter.

![page_d'acceuille](/shots/page_d'acceuille.png)

Lorsqu'une partie est lancée. Les deux joueurs vont devoir soit se "connecter" à leurs compte existant, soit en
créer un.


# Utilisation de KulturKombat

Afin d'utiliser le projet, il suffit de taper les commandes suivantes dans un terminal :

```
./compile.sh
```
Permet la compilation des fichiers présents dans 'src' et création des fichiers '.class' dans 'classes'

```
./run.sh
```
Permet le lancement du jeu
