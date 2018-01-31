# Ermes

Ermes è un'app Android che permette di organizzare partite amatoriali e di aggiungere alla mappa nuovi campi da gioco pubblici per diversi sport.
E' stata sviluppata per l'esame di Linguaggi di Programmazione per sistemi mobili e tablet (A. A. 2016/17) per l'Università degli Studi di Trento.


## Cosa fa Ermes? 

Crea una community di sportivi tramite l'organizzazione di partite, richieste di amicizia e creazione di team.

### Requisiti

L'unico requisito per poter installare l'applicazione è avere uno smartphone con sistema operativo Android Lollipop 5.1 o superiore.

### Installazione

Si può scaricare l'APK sul proprio dispositivo attraverso 
 [questo link](https://drive.google.com/file/d/1r_BN19eDrULXr4I1pwZAl1fveYB3W2oz/view?usp=sharing) oppure clonare il progetto

```
git clone https://github.com/davidmarinangeli/ErmesAndroid.git
```

oppure scaricare lo zip del progetto.

## Funzionalità

### Luoghi

Ermes include una mappa in cui ogni utente può aggiungere luoghi pubblici in cui poter giocare.
Per aggiungere un nuovo luogo, navigare all'apposita schermata

```
Home > FAB menu > Aggiungi un luogo
```

Toccare sulla mappa il luogo che si vuole salvare e aggiungere nome e sport che si possono giocare.

Al momento della creazione di una partita, i luoghi vengono proposti in una lista ordinata per vicinanza.

### Partite

Ogni utente può creare delle partite.
Per creare una nuova partita, navigare all'apposita schermata

```
Home > FAB menu > Crea una partita
```

Oltre a sport, data, ora, luogo e numero massimo di giocatori, è possibile specificare il tipo di partita, che può essere:

* Pubblica: chiunque abbia un account su Ermes può partecipare e invitare amici o team.
* Privata: solo il creatore della partita può invitare amici o team.

Una delle caratteristiche delle partite è la lista degli oggetti mancanti.
Il creatore della partita può aggiungerli al momento della creazione della partita e ogni partecipante potrà poi specificare, se può, quali può
portare.

### Dialog Flow

E' possibile creare una partita semplicemente parlando al telefono: dopo aver toccato l'apposita icona nella schermata della creazione dell'evento,
l'applicazione riempirà i campi con quello che avrà ascoltato.

Esempio

```
_Crea una partita privata di calcetto domani alle 18:00 per 10 persone_
```
oppure
```
_Aggiungi un evento pubblico di basket il 28 ottobre alle 20 con 5 giocatori_
```

Altri campi come il luogo e gli oggetti mancanti sono ancora in fase di sviluppo/test e per il momento vanno riempiti a mano.

### Creazione squadre in una partita

Accedendo alla schermata dei partecipanti di una partita e premendo il pulsante "Crea due squadre", l'applicazione creerà automaticamente due squadre
scegliendo a caso tra i giocatori.

### Lista di amici

Ogni utente ha una propria lista di amici, che può arricchire mandando richieste agli altri utenti.
In ogni elemento di qualsiasi lista di utenti (partecipanti o invitati a una partita, membri di un team) è visualizzato lo stato della relazione
tra l'utente in questione e l'utente loggato. Può essere:

* ME: l'utente è l'utente loggato in quel momento su Ermes
* AMICI: viene visualizzata la data in cui è stata stretta l'amicizia
* NESSUNA RELAZIONE: viene visualizzato il pulsante "Richiedi amicizia"
* RICHIESTA INVIATA: il pulsante è disattivato e presenta il testo "Richiesta inviata"
* ACCETTA RICHIESTA: viene visualizzato il pulsante "Accetta richiesta"

Avere amici è fondamentale per poter creare team o invitare persone a una partita.

### Team

Ogni utente può avere i propri team, in cui può venire aggiunto dall'amico creatore del team al momento della creazione oppure da altri amici che ne
fanno già parte.
Per creare un nuovo team è sufficiente navigare all'apposita schermata

```
Home > FAB menu > Crea team
```
oppure
```
Account > Team > Nuovo team (icona '+' nell'appbar)
```

Dopo aver aggiunto il nome e almeno un amico, è possibile salvare il team.

### Notifiche

Ermes notifica l'utente al verificarsi di alcuni eventi:

* Richiesta di amicizia
* Amicizia accettata
* Nuovo team (sei stato aggiunto)
* Abbandono di un team da parte di un altro utente
* Invito a una partita

Richieste di amicizia e inviti alle partite possono essere accettati o rifiutati direttamente dal corpo della notifica.

### Utenti anonimi

Gli utenti che non hanno effettuato il login possono navigare all'interno dell'applicazione, senza poter interagire in alcun modo con essa.

## Deployment

### Librerie utilizzate

* [Firebase](https://firebase.google.com/)
* [Material dialogs](https://github.com/afollestad/material-dialogs)
* [Chips input layout](https://github.com/tylersuehr7/chips-input-layout)
* [Vega layout manager](https://github.com/xmuSistone/VegaLayoutManager)
* [Material edit text](https://github.com/rengwuxian/MaterialEditText)
* [Circular image view](https://github.com/lopspower/CircularImageView)
* [Bottom navigation](https://github.com/aurelhubert/ahbottomnavigation)
* [Floating action button](https://github.com/Clans/FloatingActionButton)
* [Material datetime picker](https://github.com/wdullaer/MaterialDateTimePicker)
* [Picasso](http://square.github.io/picasso/)
* [Material number picker](https://github.com/KasualBusiness/MaterialNumberPicker)

### Sviluppatori

* [**Enrico Marcantoni**](https://github.com/mpcicco)
* [**David Marinangeli**](https://github.com/davidmarinangeli)
* [**Nicola Schiavon**](https://github.com/nicolaburetta)

