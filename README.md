# Idősek otthona - vizsgaremek

A programban idősek otthonába lehet lakókat felvenni, a szedett gyógyszereiket és a szobabeosztást eltárolni.
Szigorú rend van: a felvétel korhoz kötött, gyógyszereket csak bentlakók kaphatnak.

Három entitás van: Resident, Medicine és Room.

## Resident

* Adatok: name, dateOfBirth, gender (MALE, FEMALE), status (RESIDENT, MOVED_OUT, DIED)

####Metódusok

- CREATE **/api/nursinghome/residents**

Lakó felvétele név, életkor és nem megadásával. Feltétel a 60 és 120 év közötti életkor. A határok beállíthatók az *application.properties* fájlban. Státusz: RESIDENT

- UPDATE **/api/nursinghome/residents/{id}**

Lakó státuszának állítása. A gyógyszerek és a szobafoglaltság törlődik ha elköltözik vagy elhunyt.

- DELETE **/api/nursinghome/residents/{id}**

Lakó törlése. Törlődnek a gyógyszerek és a szobafoglalása.

- GET **/api/nursinghome/residents**

Lakók listájának lekérése név, státusz, életkor szűréssel.

- GET **/api/nursinghome/residents/{id}**

Lakó lekérése id alapján. Visszaadja a gyógyszereket és a szobaszámot is.

- GET **/api/nursinghome/residents/{id}/room**

Lakó szobáját lehet lekérdezni az összes lakótárssal.

- GET **api/nursinghome/residents/summary**

Státusz alapú statisztika.


## Medicine

Adatok: name, dailyDose, type (TABLET, INJECTION, DROPS, CREAM), resident

####Metódusok

- CREATE **api/nursinghome/medicines**

Gyógyszer felvétele név, adagolás, típus és lakó azonosító alapján.

- UPDATE **api/nursinghome/medicines/{id}**

Napi adag módosítása gyógyszer azonosító alapján.

- DELETE **api/nursinghome/medicines/{id}**

Gyógyszer törlése azonosító alapján.

- GET **api/nursinghome/medicines**

Gyógyszerek listája név szűréssel

- GET **api/nursinghome/medicines/dailysum**

Statisztika gyógyszerek neve és típusa szerint.

## Room

Adatok: roomNumber, capacity (SINGLE, DOUBLE, TRIPLE, FOUR_BED)

####Metódusok

- CREATE **api/nursinghome/rooms**

Szoba felvétele szobaszám és kapacitás alapján.

- UPDATE **api/nursinghome/rooms/{id}**

Lakót lehet be- vagy átköltöztetni. Csak üres helyre kerülhet lakó. Ha egy szobát már foglal és átköltöztetjük másikba, az eredeti helye felszabadul.

- DELETE **api/nursinghome/rooms/{id}**

Üres szoba törlése

- GET **api/nursinghome/rooms**

Szobák listája szobaszámmal és üres helyek számával.

- GET **api/nursinghome/rooms/{id}**

Egy szoba lekérése lakókkal.
