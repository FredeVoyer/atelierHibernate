# Introduction Servlet

## Serveur HTTP

Il s'agit du niveau le plus "bas", qui sert à comprendre le protocol HTTP :

- Écoute sur un port donné à une ou plusieurs adresses données
- Reçoit une requête suivant le protocole HTTP
- Renvoie une réponse à la requête en utilisant le protocole HTTP

![alt-text](https://ruslanspivak.com/lsbaws-part1/LSBAWS_HTTP_request_response.png 'Serveur HTTP')

Resources

- Very basic Python web server: https://ruslanspivak.com/lsbaws-part1/
- Very basic NodeJS web server: https://blog.risingstack.com/your-first-node-js-http-server/

## Servlet Container ou Web Container

Le serveur web comprend et analyse les requêtes HTTP entrantes et les envoie au bon "request handler" en fonction des paramètres.

Dans ce cours, Jetty est votre conteneur Web. Il est responsable d'envoyer les requêtes aux "request handlers" qu'on appelle "Servlets" en Java.

![alt-text](http://www.programcreek.com/wp-content/uploads/2013/04/web-server-servlet-container.jpg 'Web Container')

Qu'est-ce qu'un servlet? Un servlet est une interface standard en Java qui permet de prendre en charge des requêtes HTTP (request handler)

Ressources

- Basic Servlet Example: https://fr.wikipedia.org/wiki/Servlet
- https://dzone.com/articles/what-servlet-container

## Servlet Filters

Un filtre de servlet est un objet qui intercepte les requêtes HTTP.

Le filtre se place avant les autres composantes (servlets, JSP, fichiers statics), tel qu'illustré ici :

![alt-text](http://tutorials.jenkov.com/images/java-servlets/servlet-filters-1.png 'Servlet Filter')

Il peut également agir sur la réponse retournée dans certains cas.

Ressources:

- http://tutorials.jenkov.com/java-servlets/servlet-filters.html

### Implémentation

Pour ce faire, on doit hériter de `ContainerRequestFilter` (contraiment au CORSFilter déjà en place qui lui hérite de `ContainerResponseFilter`. Réfléchissez-y, vous verrez la différence :P).

Un coup dans un `ContainerRequestFilter`, il est possible d'obtenir le [User agent](https://en.wikipedia.org/wiki/User_agent) de la requête avec le code suivant :

```java
String userAgent = request.getHeaders().getFirst("User-Agent");
```

Dans un premier temps, faites simplement `logger` ça pour voir ce qui est retourné. Essayez de trouver dans la string un pattern qui vous permettrait de détecter que c'est Chrome (ne soyez pas trop zélé sur ça non plus, c'est un exercice :P)

Vous pouvez ensuite utiliser le filter pour arrêter carrément la requête avec ceci :

```java
request.abortWith(Response.status(Status.BAD_REQUEST).entity(/* ... */).build());
```

Pour ceux qui connaissent le pattern OO, ce principe est en fait un [Pipe and filter](https://www.enterpriseintegrationpatterns.com/patterns/messaging/PipesAndFilters.html), qui est une sorte de [Chain of responsability](https://sourcemaking.com/design_patterns/chain_of_responsibility).

## Et Jersey?

Jersey est un framework pour développer des services web RESTful. Il agit d'abord comme un servlet qui permet aux programmeurs de facilement enregistrer des `resources` via un framework d'inversion de contrôle (IoC).

Jersey regarde à chaque requête qui lui est attribué et, basé sur l'URL, le type de contenu demandé, etc., il réparti la requête à la bonne ressource.

## Stack complète en production

Firewall --> WAF --> Web Server (Nginx) --> Web Application (Jetty) --> Requeset Handler (Jersey) --> Application Logic (REST resource + domain) --> Database (Oracle/MongoDB)
