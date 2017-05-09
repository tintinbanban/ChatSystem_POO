# ChatSystem_POO
Implémentation en 4ème année IR (INSA Toulouse) d'un chat system fonctionnant sur un réseau local. Chat développé en JAVA, intégrant des tests unitaires (JUnit), et respectant les spécifications fonctionnelles décidées en groupe de TPs.

## Quentin VIOZELANGE ## INSA Toulouse 4IR ## 2016/2017
# ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
# Ce ChatSystem a été développé et compilé en Java 1.7. Cependant, Java 1.8 doit être inclu dans les librairies afin d'utiliser le JAR 'ChatSystemUtil'. 

    Pour pouvoir compiler cette application :  
        1. Téléchargez le projet et importez-le dans l'IDE Eclipse. 
        2. Si l'environnement Java d'Eclipse est le 1.7, alors il faut configurer le buildpath du projet pour ajouter la 
            librairie Java 1.8, avant d'exécuter le programme. Dans le cas d'un environnement en Java 1.8, il n'y a aucun 
            paramétrage préalable à faire.
        3. Lancez ensuite en tant qu'Application Java, la classe MainChatSystem.java dans le package 
            src/'com.chat_system.app'
            
# ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////#   Fonctionnalités du ChatSystem
        
    * Connexion sur un chat local (utilisateur identifié par une adresse IP et un pseudo)
    * Communication sur le chat :
        envoi / réception de messages "unicasts" (protocole TCP)
        envoi / réception de méta-données pour gérer la liste d'utilisateurs (technologie UDP multicast)
    * Déconnexion du chat :
        envoi message de déconnexion aux autres utilisateurs
        gestion des déconnexions automatiques suite à des problèmes réseaux 
            (si un utilisateur n'envoit pas ses méta-données au bout d'un certain temps sur le réseau, il est supprimé de la liste)
                
# ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////#   Rapport de tests unitaires (JUnit)

    Les tests unitaires menés sur ce projet, concernent seulement la partie 'modèle' du ChatSystem, 
    à savoir les classes : User, UserList et Discussion. 
    On les retrouve dans le package tests/'com.chat_system.junit.model'
    
    # UserTests
    méthodes testés : 
    [validé] equals => garantir l'égalité d'un User sur la comparaison de son adresse IP 
                + de son pseudo 
    
                                
    # UserListTests
    méthodes testés : 
    [validé] getUser => s'assurer de la cohérence des résultats retournés par getUser, 
                notamment quand elle doit retourner 'null'
        
    [validé] userIsDeconnecting => tester la gestion de la déconnexion du chat, pour identifier les
                deux types de déconnexion : volontaire ou accidentelle (problèmes réseaux)
                    
    [validé] getDiscussionByUser => garantir la cohérence des données entre deux listes : 
                la liste des utilisateurs à gérer, et la liste des fils de discussions associées
                à ces utilisateurs. Etre capable d'identifier qu'aucune discussion n'exite (retourne
                'null')
    
    [validé] setLocalDiscussionByUser => tester le formatage des messages envoyés sur le chat, et la mise à jour
                du fil de discussion au fur et à mesure de la conversation
    
    [validé] setDiscussionByUser => tester le formatage des messages reçus sur le chat, et la mise à jour
                du fil de discussion au fur et à mesure de la conversation    

    [validé] addUser => test sur la gestion de l'ajout d'un utilisateur dans la classe, pour s'assurer
                que l'ajout soit correctement effectué sur la liste d'utilisateurs, sur les fils de discussion,
                puis sur la liste des timeouts utilisateurs
                
    [validé] updateTimeout => s'assurer de la mise à jour du timeout associé à l'utilisateur, comportant
                une valeur plus grande
    
    [validé] removeUser => test sur la gestion de la suppression d'un utilisateur dans la classe, afin 
                de s'assurer que la suppression soit correctement effectuée sur la liste d'utilisateurs, 
                sur les fils de discussion, puis sur la liste des timeouts utilisateurs
                
                
    # DiscussionTests
    méthodes testés : 
    [validé] completeLocalDiscussion => test du bon formatage d'une chaîne de caractères encapsulée
                dans un message véhiculé à travers le chat, et envoyé par l'utilisateur local
    
    [validé] completeDiscussion => test du bon formatage d'une chaîne de caractères encapsulée
                dans un message véhiculé à travers le chat, et reçu par l'utilisateur local

# ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////#   Rapport de tests inter-projets

    Des tests de compatibilité ont été exécutés avec un autre binôme du groupe de TP (SARION-RODRIGUEZ VALLEJO). Les test   
    suivants ont été effectués :
        * Lancement des deux chats : phase de connexion [OK]
        * Identification des utilisateurs sur le chat   [OK]
        * Envoi de messages à travers le chat           [OK]
        * Réception de messages à travers le chat       [OK]
        * Phase de déconnexion : suppression et mise à jour de la liste utilisateurs    [OK]
    
    En guise d'illustrations, des captures d'écran résultant des tests sont contenues dans le dossier 'tests_inter-projets'. 
    
    N.B : seule la partie envoi/réception de fichiers n'est pas possible entre ces deux chats, puisqu'elle n'a été implémentée 
          de mon côté.
        
