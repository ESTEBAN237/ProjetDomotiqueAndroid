# PolyHome – Application Android de Domotique

PolyHome est une application Android développée en Kotlin permettant de contrôler une maison connectée via une API REST.

L’application permet de gérer :
-  Les lumières
-  Les volets roulants
-  La porte de garage
-  Les utilisateurs d’une maison (selon le rôle)

---

## Participants

| Nom | Rôle |
|------|------|
| **Mimouni Oumnia** | Frontend (UI/UX, Authentification, Layouts, Design) |
| **Mezazem Esteban** | Backend Android (Logique métier, API, Gestion des devices) |

---

##  Fonctionnalités

### Authentification
- Inscription
- Connexion
- Gestion du token
- Distinction des rôles : **Propriétaire / Invité**

---

### Gestion des maisons
- Affichage des maisons de l’utilisateur
- Accès aux détails d’une maison
- Gestion des utilisateurs (uniquement pour le propriétaire)

---

### Gestion des lumières
- Voir la liste des lumières
- Allumer / Éteindre une lumière
- Éteindre toutes les lumières
- Affichage du statut (Allumée / Éteinte)

---

### Gestion des volets
- Ouvrir / Fermer / Stop un volet
- Ouvrir tous les volets
- Fermer tous les volets
- Affichage dynamique du statut (Ouvert / Fermé)

---

### Gestion du garage
- Ouvrir / Fermer / Stop la porte
- Affichage de l’état

---

## Design

- Couleur principale : `#20484d`
- Cartes avec bordures arrondies
- Navigation inférieure style iOS
- Interface cohérente sur toutes les activités
- Compatible téléphone réel et émulateur

---

## Technologies utilisées

- Kotlin
- Android SDK
- ConstraintLayout
- ListView + Custom Adapters
- CardView
- API REST (GET / POST / DELETE)

---

## API utilisée : https://polyhome.lesmoulinsdudev.com/api


Endpoints principaux :

- `/houses`
- `/houses/{id}/users`
- `/houses/{id}/devices`
- `/houses/{id}/devices/{deviceId}/command`

---

## Installation

1. Cloner le projet :

```bash
git clone https://github.com/ESTEBAN237/ProjetDomotiqueAndroid.git


