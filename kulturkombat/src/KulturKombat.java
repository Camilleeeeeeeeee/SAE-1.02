import extensions.CSVFile;
import extensions.File;
class KulturKombat extends Program {

    final int DIRECT = 1;
    final int CROCHET = 2;
    final int UPPERCUT = 3;

//QUESTIONS

    Reponse newReponse(String reponse){
        Reponse r = new Reponse ();
        r.reponse = reponse;
        r.bonneReponse = false;
        return r;
    }

    Reponse[] chargerReponsesFaciles(CSVFile questionsCsv, int numQuestion){
        Reponse[] resultat = new Reponse [2];
        int caseBonneRep = auHasard(length(resultat));
        resultat[caseBonneRep] = newReponse(getCell(questionsCsv, numQuestion, 1));
        resultat[caseBonneRep].bonneReponse = true; 
        if (caseBonneRep == 1){
            resultat[0] = newReponse(getCell(questionsCsv, numQuestion, auHasard(3)+2));
        }else{
            resultat[1] = newReponse(getCell(questionsCsv, numQuestion, auHasard(3)+2));
        }
        return resultat;
    }   

    Reponse[] chargerReponsesMoyennes(CSVFile questionsCsv, int numQuestion){
        Reponse[] resultat = new Reponse [4];
        int[] indicesUtilise = new int [4];
        int compteur = 0;
        while (compteur < 4){
            int valeurTestee = auHasard (5);
            if (indiceValide(indicesUtilise, compteur, valeurTestee)){
                resultat[compteur] = newReponse(getCell(questionsCsv, numQuestion, valeurTestee));
                indicesUtilise[compteur] = valeurTestee;
                if(valeurTestee == 1){
                    resultat[compteur].bonneReponse = true;
                }
                compteur++;
            }
        }
        return resultat;
    }

    Reponse[] chargerReponsesDifficiles(CSVFile questionsCsv, int numQuestion){
        Reponse[] resultat = new Reponse [1];
        resultat[0] = newReponse(getCell(questionsCsv, numQuestion, 1));
        return resultat;
    }

    Question newQuestion (CSVFile questionsCsv, int numQuestion, int choixCoup){
        Question q = new Question ();
        q.question = getCell(questionsCsv, numQuestion, 0);
        if (choixCoup == DIRECT){
            q.reponses = chargerReponsesFaciles(questionsCsv, numQuestion);
        }else if (choixCoup == CROCHET){
            q.reponses = chargerReponsesMoyennes(questionsCsv, numQuestion);
        }else{
            q.reponses = chargerReponsesDifficiles(questionsCsv, numQuestion);
        }
        return q;
    }

    Question[] chargerQuestions (CSVFile questionsCsv, int choixCoup){
        Question[] resultat = new Question [rowCount(questionsCsv)];
        for (int i = 0; i <  length(resultat); i++){
            resultat[i]= newQuestion(questionsCsv, i, choixCoup);
        }
        return resultat;
    }

    String toString(Question questionCourante){
        String resultat = "****"+questionCourante.question+"****\n";
        if(length(questionCourante.reponses) > 1){
            for(int i = 0; i < length(questionCourante.reponses); i++){
                resultat += "           "+(i+1)+". "+questionCourante.reponses[i].reponse;
                if(i == 1){
                    resultat += "\n";
                }
            }
        }
        return resultat;
    }



    int auHasard (int nbr) {
        int resultat = (int)(random() * nbr);
        return resultat;
    }
    
    boolean indiceValide (int[] indicesUtilise, int compteur, int valeurTestee){
        if(valeurTestee == 0){
            return false;
        }else if(compteur != 0){
            for(int i = 0; i < compteur; i++){
                if (indicesUtilise[i] == valeurTestee){
                    return false;
                }
            }
        }
        return true;
    }

    boolean testeReponse(Question questionCourante, int choixCoup){
        if(choixCoup == DIRECT){
            int entreJoueurInt = choixValideInt(1, 2);
            if (questionCourante.reponses[entreJoueurInt-1].bonneReponse){
                return true;
            }
            return false;
        }else if(choixCoup == CROCHET){
            int entreJoueurInt = choixValideInt(1, 4);
            if (questionCourante.reponses[entreJoueurInt-1].bonneReponse){
                return true;
            }
            return false;
        }
        String entreJoueurString = readString();
        if(equals(questionCourante.reponses[0].reponse, entreJoueurString)){
            return true;
        }
        return false;
    }

    Question choisirQuestion(int choixCoup){
        CSVFile questionsCsv = loadCSV("ressources/questions/questions.csv");
        Question[] questions = chargerQuestions(questionsCsv, choixCoup);
        return questions[auHasard(length(questions))];
    }

//JOUEURS

    Joueur newJoueur(String nomJoueur, int nbrParties, int nbrVictoires){
        Joueur j = new Joueur ();
        j.nomJoueur = nomJoueur;
        j.pointsVie = 10;
        j.vivant = true;
        j.nbrParties = nbrParties;
        j.nbrVictoires = nbrVictoires;
        j.avgBonneReponseFacile = 0;
        j.avgBonneReponseMoyenne = 0;
        j.avgBonneReponseDure = 0;
        return j;
    }

    int rechercheJoueur(String[][] tabJoueurs, String nomJoueur){
        for (int i = 0; i < length(tabJoueurs, 1); i++){
            if (equals(tabJoueurs[i][0], nomJoueur)){
                return i;
            }
        }
        return -1;
    }

    String[][] csvToArray(){
        CSVFile joueursCsv = loadCSV("ressources/joueurs/joueurs.csv");
        String[][] resultat = new String [rowCount(joueursCsv)][columnCount(joueursCsv)];
        for(int i = 0; i < length(resultat, 1); i++){
            for(int j = 0; j < length(resultat, 2); j++){
                resultat[i][j]=getCell(joueursCsv, i, j);
            }
        }
        return resultat;
    }

    String[][] tabJoueurs(){
        String[] fichiers = getAllFilesFromDirectory("ressources/joueurs");
        if(length(fichiers) > 0){
            for(int k = 0; k < length(fichiers); k++){
                if(equals(fichiers[k], "joueurs.csv")){
                    return csvToArray();
                }
            }
        }
        String[][] nouveauFichiers = new String [1][6];
        saveCSV( nouveauFichiers, "ressources/joueurs/joueurs.csv");
        return csvToArray();
    }

    String tabJoueursToString(String[][] tabJoueurs){
        String resultat = "";
        for(int i = 1; i < length(tabJoueurs, 1); i++){
            for(int j = 0; j < length(tabJoueurs, 2); j++){
                resultat +="\t" + tabJoueurs[i][j] + "\t";
            }
            resultat += "\n";
        }
        return resultat;
    }

    Joueur creerJoueur(String[][] tabJoueurs){
        println("Veuillez rentrer votre nom");
        String nomJoueur = readString();
        while(rechercheJoueur(tabJoueurs, nomJoueur) != -1){
            print("Ce nom existe déjà, veuillez réésayer : ");
            nomJoueur = readString();
        }
        Joueur resultat = newJoueur(nomJoueur, 0, 0);
        ajouterJoueur(tabJoueurs, resultat);
        return resultat;
    }

    void ajouterJoueur(String[][] tabJoueurs, Joueur joueur){
        String[][] resultat = new String [length(tabJoueurs, 1)+1][length(tabJoueurs, 2)];
        for(int i = 0; i < length(resultat, 1)-1; i++){
            for(int j = 0; j < length(resultat, 2); j++){
                resultat[i][j] = tabJoueurs[i][j];
            }
        }
        resultat[length(resultat, 1)-1][0] = joueur.nomJoueur;
        resultat[length(resultat, 1)-1][1] = ""+joueur.nbrParties;
        resultat[length(resultat, 1)-1][2] = ""+joueur.nbrVictoires;
        resultat[length(resultat, 1)-1][3] = ""+joueur.avgBonneReponseFacile;
        resultat[length(resultat, 1)-1][4] = ""+joueur.avgBonneReponseMoyenne;
        resultat[length(resultat, 1)-1][5] = ""+joueur.avgBonneReponseDure;
        saveCSV(resultat, "ressources/joueurs/joueurs.csv");
    }

    Joueur chargerJoueur(String[][] tabJoueurs){
        println("1.Vous avez déjà un compte ? Connectez vous !");
        println("2.Vous n'avez pas encore de compte ? Créez en un !");
        int choix = choixValideInt(1, 2);
        if(choix == 1){
            println("Veuillez rentrer votre nom");
            String nomJoueur = readString();
            while(rechercheJoueur(tabJoueurs, nomJoueur) == -1){
                print("Nous n'avons pas trouvé votre nom, veuillez réésayeez : ");
                nomJoueur = readString();
            }
            int lig = rechercheJoueur(tabJoueurs, nomJoueur);
            Joueur resultat = newJoueur(nomJoueur, stringToInt(tabJoueurs[lig][1]),stringToInt(tabJoueurs[lig][2]));
            return resultat;
        }
        Joueur resultat = creerJoueur(tabJoueurs);
        return resultat;
    }

    Joueur actualiserCoupJoueur(Joueur joueurCourant, int choixCoup, boolean reponseJuste){
        if(reponseJuste){
            joueurCourant = incrementerCoupJoueurs(joueurCourant, choixCoup);
        }
        return joueurCourant;
    }

    Joueur incrementerCoupJoueurs(Joueur joueurCourant, int choixCoup){
        if(choixCoup == DIRECT){
            joueurCourant.avgBonneReponseFacile++;
        }
        else if(choixCoup == CROCHET){
            joueurCourant.avgBonneReponseMoyenne++;
        }
        else if(choixCoup == UPPERCUT){
            joueurCourant.avgBonneReponseDure++;
        }
        return joueurCourant;
    }

//JEUX

    Joueur changerJoueurCourant(Joueur j1, Joueur j2, int nbrTour){
        if(nbrTour == 0){
            return j1;
        }else if(nbrTour%2 == 0){
            return j1;
        }
        return j2;
    }

    Joueur changerJoueurPassif(Joueur j1, Joueur j2, int nbrTour){
        if(nbrTour == 0){
            return j2;
        }else if(nbrTour%2 == 0){
            return j2;
        }
        return j1;
    }

    void afficherDifficultes(){
        println("Quel coup voulez vous porter ?");
        println("1. Direct");
        println("2. Crochet");
        println("3. Uppercut");
        println();
    }

    void appliquerRegles(Joueur joueurPassif, int choixCoup, boolean reponseJuste){
        if (reponseJuste){
            println("Felicitation, c'est la bonne réponse !");
            infligerDegat(joueurPassif, choixCoup);
            println();
        }else{
            println("Désolé, c'est une mauvaise réponse...");
            println();
        }
    }

    void infligerDegat(Joueur joueurPassif, int choixCoup){
        if (choixCoup == DIRECT){
            joueurPassif.pointsVie = joueurPassif.pointsVie - 1;
            println(joueurPassif.nomJoueur+" perds 1 points de vie");
            println("Il lui en reste "+joueurPassif.pointsVie);
            println();
        }else if (choixCoup == CROCHET){
            joueurPassif.pointsVie = joueurPassif.pointsVie - 2;
            println(joueurPassif.nomJoueur+" perds 2 points de vie");
            println("Il lui en reste "+joueurPassif.pointsVie);
            println();
        }else if (choixCoup == UPPERCUT){
            joueurPassif.pointsVie = joueurPassif.pointsVie - 4;
            println(joueurPassif.nomJoueur+" perds 4 points de vie");
            println("Il lui en reste "+joueurPassif.pointsVie);
            println();
        }
        if (joueurPassif.pointsVie <= 0){
            joueurPassif.vivant = false;
        }
    }

    void recapPv(Joueur j1, Joueur j2){
        println(j1.nomJoueur + " : " + j1.pointsVie + "PV           " + j2.nomJoueur + " : " + j2.pointsVie + "PV");
        println();
    }

    String[][] calculAvg(String[][] tabJoueurs, Joueur joueur){
        int lig = rechercheJoueur(tabJoueurs, joueur.nomJoueur);
        if(joueur.nbrParties < 2){
            tabJoueurs[lig][1] = ""+joueur.nbrParties;
            tabJoueurs[lig][2] = ""+joueur.nbrVictoires;
            tabJoueurs[lig][3] = ""+joueur.avgBonneReponseFacile;
            tabJoueurs[lig][4] = ""+joueur.avgBonneReponseMoyenne;
            tabJoueurs[lig][5] = ""+joueur.avgBonneReponseDure;
            return tabJoueurs;
        }
        tabJoueurs[lig][1] = ""+joueur.nbrParties;
        tabJoueurs[lig][2] = ""+joueur.nbrVictoires;
        tabJoueurs[lig][3] = ""+(joueur.avgBonneReponseFacile + stringToInt(tabJoueurs[lig][3])/2);
        tabJoueurs[lig][4] = ""+(joueur.avgBonneReponseMoyenne + stringToInt(tabJoueurs[lig][4])/2);
        tabJoueurs[lig][5] = ""+(joueur.avgBonneReponseDure + stringToInt(tabJoueurs[lig][5])/2);
        return tabJoueurs;
    }

    void enregistrerDonnee(String[][] tabJoueurs, Joueur j1, Joueur j2){
        j1.nbrParties++;
        j2.nbrParties++;
        if(j1.vivant){
            j1.nbrVictoires++;
        }else{
            j2.nbrVictoires++;
        }
        tabJoueurs = calculAvg(tabJoueurs, j1);
        tabJoueurs = calculAvg(tabJoueurs, j2);
        saveCSV(tabJoueurs, "ressources/joueurs/joueurs.csv");
    }

//TABLEAU DES SCORES

    String[][] permuterLignes(String [][] tabJoueurs, int c0, int c1){
        for (int i=0; i<length(tabJoueurs, 2); i++){
            String produit = tabJoueurs[c0][i];
            tabJoueurs[c0][i]=tabJoueurs[c1][i];
            tabJoueurs[c1][i]=produit;
      }
      return tabJoueurs;
    }

    void trierSurColonne(String[][] tabJoueurs, int indexTri){
        int borneMax = length((tabJoueurs),1)-1;
        boolean permutation = true;
        while (permutation){
        permutation = false;
        for (int i=1; i<borneMax; i++){
            if (compare(tabJoueurs[i][indexTri], tabJoueurs[i+1][indexTri])<0){
                permuterLignes(tabJoueurs,i, i+1);
                permutation=true;
                }
            }
        }
    }

//MENU


    String fileToString(String nomFichier){
        extensions.File fichier = newFile("ressources/menu/"+nomFichier+".txt");
        String resultat = "";
        while(ready(fichier)){
            resultat = resultat + readLine(fichier) + "\n";
        }
        return resultat;
    }

    void afficherRegles (){
            println(fileToString("regles_1"));
            print("Appuyez sur \"s\" pour passer à la page suivante : ");
            choixValideString("s");
            println(fileToString("regles_2"));
            print("Appuyez sur \"m\" pour retourner à la page d'acceuil : ");
            choixValideString("m");
    }

    void afficherGagnant(Joueur j1, Joueur j2){
        if(j1.vivant){
            println(j1.nomJoueur+" à gagné !!!!");
        }else{
            println(j2.nomJoueur+" à gagné !!!!");
        }
    }

    int choixValideInt(int debut, int fin){
        boolean valide = false;
        String resultat = readString();
        while (!valide){
            for(int i = debut; i <= fin; i++){
             if(equals(resultat, ""+i)){
                valide = true;               
                }         
            }
            if(!valide){
            print("Choix invalide, veuillez rééssayez : ");
            resultat = readString();       
                }
        }
        return stringToInt(resultat);
    }

    void afficherScores(String[][] tabJoueurs){
        println(fileToString("scoreboard"));
        trierSurColonne(tabJoueurs, 2);
        println(tabJoueursToString(tabJoueurs));    
        boolean quitter = false;
        while(!quitter){
            int choix = choixValideInt(1,6);
            if(choix == 1){
                println(fileToString("scoreboard"));
                trierSurColonne(tabJoueurs, 1);
                println(tabJoueursToString(tabJoueurs));                 
            }else if(choix == 2){
                println(fileToString("scoreboard"));
                trierSurColonne(tabJoueurs, 2);
                println(tabJoueursToString(tabJoueurs));                 
            }else if(choix == 3){
                println(fileToString("scoreboard"));
                trierSurColonne(tabJoueurs, 3);
                println(tabJoueursToString(tabJoueurs));                 
            }else if(choix == 4){
                println(fileToString("scoreboard"));
                trierSurColonne(tabJoueurs, 4);
                println(tabJoueursToString(tabJoueurs));                 
            }else if(choix == 5){
                println(fileToString("scoreboard"));
                trierSurColonne(tabJoueurs, 5);
                println(tabJoueursToString(tabJoueurs));                 
            }else if(choix == 6){
                quitter = true;         
            }
        }
    }


    String choixValideString(String choixPossible){
        boolean valide = false;
        String resultat = readString();
        while (!valide){
             if(equals(resultat, choixPossible)){
                valide = true;               
                }         
            if(!valide){
            print("Choix invalide, veuillez rééssayez : ");
            resultat = readString();          
            }
        }
        return resultat;
    }

    void algorithm(){
        String[][] joueurs = tabJoueurs();
        boolean quitter = false;
        while (!quitter){
            println(fileToString("page_acceuil"));
            int choixMenu = choixValideInt(1, 4);
            if (choixMenu == 1){
                Joueur j1 = chargerJoueur(joueurs);
                println("Joueur 1 : " + j1.nomJoueur);
                println();
                joueurs = tabJoueurs(); //au cas où il y ai eu la création d'un nouveau joueur.
                Joueur j2 = chargerJoueur(joueurs);
                println("Joueur 2 : " + j2.nomJoueur);
                println();
                joueurs = tabJoueurs(); //au cas où il y ai eu la création d'un nouveau joueur.
                int nbrTour = 0;
                println(fileToString("partie_lancee"));
                while (j1.vivant && j2.vivant){
                    recapPv(j1, j2);
                    Joueur joueurCourant = changerJoueurCourant(j1, j2, nbrTour);
                    Joueur joueurPassif = changerJoueurPassif(j1, j2, nbrTour);
                    println("C'est au tour de "+joueurCourant.nomJoueur);
                    afficherDifficultes();
                    int choixCoup = choixValideInt(1, 3);
                    Question questionCourante = choisirQuestion(choixCoup);
                    println(toString(questionCourante));
                    print("Veuillez rentrer votre réponse : ");
                    boolean reponseJuste = testeReponse(questionCourante, choixCoup);
                    joueurCourant = actualiserCoupJoueur(joueurCourant, choixCoup, reponseJuste);
                    appliquerRegles(joueurPassif, choixCoup, reponseJuste);
                    nbrTour++;
                }
                afficherGagnant(j1, j2);
                enregistrerDonnee(joueurs, j1, j2);
            }else if (choixMenu == 2){
                afficherRegles();
            }else if (choixMenu == 3){
                afficherScores(joueurs);
            }else{
                quitter = true;
                println("A bientot !");
            }
        }
    }
}