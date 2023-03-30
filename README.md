# Laboratoire hibernate

## Objectifs du devoir

- Utiliser le pattern `repository` et la couche applicative `contexte` afin de supporter une application avec
  plusieurs types de persistences
- Coder des tests de bordure afin de tester un repository

Le laboratoire utilisera également hibernate, un ORM qui est très présent dans l'entreprise. Un des objectifs
est également d'être capable d'apprendre rapidement une nouvelle technologie en feuilletant de la documentation,
chose que vous ferrez toute votre vie (et c'est un critère du BCAPG)!

## Déroulement

Bien lire l'exercice au complet avant de commencer. Dans certains cas, plusieurs solutions sont possibles, mais
nous vous en imposerons une particulière soit pour des fins pédagogiques, soit pour que ça soit plus facile à corriger!

### Analyser le code existant

Actuellement, il est possible de démarrer le code seulement avec la version "in memory" de la persistence. Pour ce
faire, vous pouvez utiliser intellij avec l'option `-Dgarage.persistence=memory` (VM options - l'autre option
étant `hibernate`).

Un projet postman est également fournis. Vous pouvez l'utiliser pour vous assurer que tout démarre bien.

Quelques règles d'affaires en vrac pour comprendre le projet :

- On peut prendre un rendez-vous pour changer une pièce dans un garage. Le rendez-vous est pour le jour même.
- Lors de la prise du rendez-vous, celui-ci est enregistré avec les informations du client.
- En même temps, une commande est passée afin de commander les pièces nécessaires pour l'entretient.
- Un client ne peut pas prendre 2 rendez-vous dans la même journée.
- Si la commande ne fonctionne pas (peu importe l'erreur), le rendez-vous n'est pas sauvegardé (rollback). **CETTE FONCTIONNALITÉ** est présentement manquante avec la version en mémoire, puisque ça ne peut juste pas arriver.

Quelques décisions architecturales prises (et qui ne peuvent **pas** être changées) :

- La gestion des rendez-vous et les commandes ne semblaient pas avoir le même cycle de vie. Ils sont donc séparés dans
  2 aggrégats (chacun à un repository et une factory).
- L'application service sert à coordonner les deux aggrégats
- La couche de persistence s'occupe de valider l'unicité des rendez-vous (via le `AppointmentNumber`).
- Le contexte utilise HK2 pour tout sauf ce qui est rest/jersey.
- Les tests peuvent être roulés avec `mvn test`. Les tests de plus haut niveau sont nommés `*ITest` et sont roulés
  avec `mvn integration-test`
- Les tests de bordure sont copiés/collés entre les deux types de repository

Quelques limitations pour le cadre du cours (certaines consignes deviendront nécessaire dans les étapes suivantes :P) :

- Vous ne devez PAS modifier les resources rest.
- Vous ne devez PAS modifier la logique actuelle ou l'architecture ou la structure des package. Les appels aux
  factory/repository ne doivent pas changer, mais vous pouvez ajouter des lignes avant/après.
- Vous ne pouvez PAS utiliser le pattern Unit Of Work (même si c'est tentant! Ce pattern est très intéressant,
  mais laissé pour le cours GLO-4003)
- Vous devez gérer les transactions SANS utiliser hk2/le @RequestScope. Il faut apprendre à le faire à bras avant!
- Les `id` BD des classes doivent être des `int` avec la génération par identité:
  `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- Pour l'application, vous devez avoir un `persistence.xml` dans `src/main/resources/META-INF` avec un persistence unit
  nommé `garage`. C'est **obligatoire**! Celui-ci contient les configuration pour le dialect, le driver, etc.
- Vous ne devez PAS enregistrer les entités dans le fichier xml, utilisez plutôt l'auto détection avec
  `<property name="hibernate.archive.autodetection" value="class, hbm" />`
- Pour les tests, vous devez avoir un `persistence.xml` séparé dans `src/test/resources/META-INF` avec un
  persistence unit nommé `garage-test`. C'est **obligatoire**!
- Pour les tests, vous devez implémenter les tests qui sont déjà là (mais vide). Vous ne pouvez pas en
  ajouter/retirer/changer les noms. Par contre, l'implémentation est libre à vous.
- Vous pouvez ajouter des choses au `pom.xml` (ça ne devrait pas être nécessaire), mais vous ne pouvez pas modifier
  ce qui est présentement là
- Ne pas prendre pour acquis que tous les choix de design dans l'application sont bons - certains sont là pour simplifier
  le laboratoire!
- Vous pouvez, mais vous ne devriez pas avoir à changer aucun des tests existants si tout est bien fait.

### Crash course hibernate

Lire la section 8.5.2 de [ce document](https://docs.jboss.org/hibernate/orm/5.3/userguide/html_single/Hibernate_User_Guide.html#session-per-request)
(note : Session et EntityManager sont équivalents, le second est l'implémentation du pattern JPA de java).

Il nous faudra donc un _EntityManagerFactory_ et un _EntityManager_. Cependant, notre contexte web demande quelque
chose de spécial : on doit avoir un et un seul EntityManager par requête. Pour ce faire, on aura :

- Un EntityManagerFactory par application (c'est donc un singleton).
- Un EntityManger par unité de travail. On utilisera une variable [ThreadLocal](http://docs.oracle.com/javase/8/docs/api/java/lang/ThreadLocal.html).
  - En REST, ceci signifie que la durée de l'unité de travail correspond généralement à une requête. On créera donc un entity manager par requête, et celui-ci sera utilisé pour toute la durée de cette requête.

### Implémenter les repository hibernate

Hibernate et H2 sont déjà importés dans le projet.

Voici la structure de table à laquelle on s'attend (celle-ci doit être respectée à 100% - nom des tables, case, nom
des colonnes, etc). Lire la documentation sur les annotations afin de savoir comment arriver à ce résultat :

```sql
    create table appointments (
       id integer not null auto_increment,
        appointmentNumber varchar(255),
        clientName varchar(255),
        clientPhone varchar(255),
        primary key (id)
    );

    create table orders (
       id integer not null auto_increment,
        date datetime,
        appointmentNumber varchar(255),
        primary key (id)
    );

    create table parts (
       id integer not null auto_increment,
        name varchar(255),
        quantity integer,
        order_id integer,
        primary key (id)
    );

```

Vous pouvez regarder le titre des tests (ou l'implémentation de ceux-ci pour le repository in memory) afin d'avoir une idée des comportements à avoir!

Vous devez ajouter les annotions hibernate au bon endroit. Pour les fins du devoir, il est correct d'ajouter les annotations directement sur les objets du domaine (
au lieu de faire une couche de DTO, ce qui sera plus rapide pour vous). Assurez-vous d'avoir vraiment toutes les annotations nécessaires afin
d'obtenir le schéma de BD ci-haut (prenez le temps de quadruple-vérifier, la correction échouera automatiquement si ce n'est pas le cas). À vous de trouver une
façon de vous assurer de cette structure. Évidement la syntax exacte du SQL pourrait changer (entre autre, auto_increment n'existe pas pour H2), mais la structure devrait être la même.

Question bonus: Pourquoi est-ce acceptable en java de mettre ces annotations dans le domaine? Dans quel cas voudrions-nous plutot avoir des DTO pour la couche persistence?

**BD utilisée**

Pour les fins du devoir, vous devez utiliser H2 pour votre code et vos tests.

Voici un exemple de configuration pour hibernate et H2:

```xml
<?xml version="1.0"  encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd" version="2.0">

    <persistence-unit name="XYZ" transaction-type="RESOURCE_LOCAL">
        <description>H2 In Memory DB</description>
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

        <exclude-unlisted-classes>false</exclude-unlisted-classes>

        <properties>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect" />
            <property name="hibernate.connection.driver_class" value="org.h2.Driver" />
            <property name="hibernate.hbm2ddl.auto" value="create-drop" />
            <property name="hibernate.show_sql" value="true" /> <!-- DEBUG -->
            <property name="hibernate.format_sql" value="true" /> <!-- DEBUG -->

            <property name="javax.persistence.jdbc.url" value="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1" />
            <property name="javax.persistence.jdbc.user" value="sa" />
            <property name="javax.persistence.jdbc.password" value="" />

            <property name="hibernate.archive.autodetection" value="class, hbm" />
            <property name="hibernate.id.new_generator_mappings" value="true" />

        </properties>
    </persistence-unit>
</persistence>
```

Prenez le temps de comprendre ce que ça fait par contre!!

**Gestion des transactions**

La gestion des transaction (qu'est-ce qui doit être atomique) est en fait un problème qui appartient à la logique
d'application (couche d'application service). Les repositories ne devrait pas avoir à gérer de transaction.

Il est possible de faire ceci assez facilement en utilisant le fait que dans jetty, 1 requête = 1 thread. On utilise
par exemple un [threadlocal](https://docs.oracle.com/javase/7/docs/api/java/lang/ThreadLocal.html) et un
[servlet filter](http://tutorials.jenkov.com/java-servlets/servlet-filters.html).

Le filter est déjà fournis dans le code de base du projet (prenez le temps de voir comment il est enregistré), mais il
ne fait rien. Vous pouvez par contre y placer du code avant et après la requête!

Si on décortique un "save" dans hibernate, on a :

```java
EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("XYZ"); // Une seule fois par application! C'est très long à faire!!

EntityManager entityManager = entityManagerFactory.createEntityManager(); // L'équivalent d'une "connexion", ou souvent 1 entity manager = 1 transaction. Ici on veut partager le même pour toute la requête rest!

entityManager.getTransaction().begin(); // Doit être fait dans l'application service (en étant idéalement abstrait de entity manager - d'où l'interface DomainTransaction)

entityManager.persist(object); // Doit être fait dans l'implémentation hibernate du repository

entityManager.getTransaction().commit(); // ou rollback, aussi dans l'application service.
```

Si vous n'êtes pas sur de comprendre comment les servlet filters fonctionnent, vous pouvez vous réferer
à [cette introduction](./servlet.md).

Vous devez trouver une façon d'arriver à cela! N'hésitez pas à nous le demander au laboratoire si vous êtes bloqués.

### Écrire les tests de bordure

Vous devez vous assurer que le comportement ne change pas et que votre code fonctionne bien avec hibernate!
Entre autre, est-ce que les annotations sont les bonnes? Est-ce que la gestion des duplicats est gérée?

Pour ce faire, vous avez déjà deux `*ITest` présent, il faut simplement les compléter. Si vous avez trouvé une solution
élégante pour la gestion des transactions, ces tests devraient être simple à écrire!

Vous aurez probablement un bug avec l'utilisation du `persistence.xml` dans les tests. Voir
[cette explication](http://stackoverflow.com/questions/4726521/hibernate-jpa-unit-tests-autodection-does-not-work) de stack overflow.

## Indices supplémentaires

1. N'essayez pas d'obtenir **exactement** le schéma de BD montré. Les noms de table et de colonnes doivent
   être **identiques**, mais la syntax exacte est celle de MySQL et non H2.

2. Relire les limitations/contraintes ci-haut

3. Informations supplémentaires sur la gestion des objets de [JPA](https://www.javaworld.com/article/3379043/what-is-jpa-introduction-to-the-java-persistence-api.html)

Premièrement, [l'EntityManagerFactory](https://docs.oracle.com/javaee/7/api/javax/persistence/EntityManagerFactory.html) sert à créer des connections à la BD. Pour le créer, on utilise `Persistence.createEntityManagerFactory` (d'où la suggestion d'utiliser un singleton). Créer cette objet est extrèmement long, donc on en veut 1 seul et on doit le démarrer en même temps que l'application. Il y a quelques options ici, dans le `main()` du serveur, dans l'initialisation du servlet filter, etc...

Ensuite, pour faire des opérations sur la BD (transactions, select, insert, etc), on doit utiliser un `EntityManager`. On peut voir un entity manager comme une connexion à la BD. On doit ouvrir une connexion par requête dans ce laboratoire-ci (dans la vrai vie il est possible de réutiliser les connexions entre autre - n'essayez pas de le faire). On doit donc ouvrir la connexion au début de la requête et la fermer après. C'est pas mal la définition d'un servlet filter d'agir avant/après la requête.

Afin de conserver la connexion par requête, il est intéressant de noter que Jetty va partir 1 thread par requête, d'où la suggestion ci-haut d'utiliser un ThreadLocal (dans un deuxième singleton? Il y a plusieurs solutions possibles ici).

Maintenant le vrai défi du laboratoire est de trouver une façon élégante pour que le code dans la couche d'infrastructure ait accès à l'entity manager créer au début de la requête. Vous ne pouvez pas le passer à chaque appel de méthode. Il y a moyen d'utiliser l'injection de dépendences, mais si vous avez un singleton déjà, il existe des façons plus simples de faire.

L'objectif final était que c'est le use case qui dicte la portée de la transaction (begin/commit).

En bref:

- EntityManagerFactory : 1 par application
- EntityManager: 1 par requête
- entityManager::begin() / ::commit() : fait par l'application service (indirectement, via l'interface `DomainTransaction`)
- entityManager::save / ::createQuery / etc : fait par les repositories

4. Relire les limitations/contraintes ci-haut

5. Mettez les annotations JPA dans le domaine directement, c'est plus simple.

Ce n'est pas la seule façon de faire et ce n'est pas toujours souhaitable, mais ici ça va grandement vous simplifier
la vie!! Il y a une raison pourquoi en java c'est OK de faire ainsi, essayez de la trouver! Indice: regardez ça vient
de quel librairie...

Il serait possible de mettre des DTO ici avec des mappers, mais c'est trop complexe pour ce lab, n'essayez pas.
Les annotations sont dans le domaine, et hibernate s'occupe de faire le DTO vers la BD. Ce n'est pas une mauvaise solution
si vous voyez ça en entreprise, mais ça ajoute quand même une bonne compelxité avec hibernate. Vous pouvez tout faire ce
qui est demandé juste avec des annotations (LISEZ LA DOC!!!!)

6. Relire les limitations/contraintes ci-haut

7. Concernant `DomainTransaction`.

Le but de cette classe est "d'enrober" un bout de code dans une transaction. La portée de la transaction est décidée par
le use case, mais on ne veut pas le faire dépendre de `EntityManager` directement (sinon la version en mémoire ne marcherait
plus). On a donc créé cette abstraction!
