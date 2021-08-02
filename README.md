# Idősek otthona - vizsgaremek

A programban lakókat lehet felvenni, a szedett gyógyszereiket és a szobabeosztást eltárolni.
Három entitás van: lakó, gyógyszer és szoba.

## Lakók

Adatok: név, születési dátum, nem (MALE, FEMALE), státusz (RESIDENT, MOVED_OUT, DIED)
Lakó felvételének feltétele a 60 és 120 év közötti kor. (application.properties)
Le lehet kérni a lakók listáját név, státusz, életkor szűréssel, valamint hogy melyik szobában lakik, egy adott lakó összes adatát és statusz alapú statisztikát. 
Státuszváltozás esetén a rednszerből törlődnek a szedett gyógyszerek és a szobából is törlésre kerül.


## Gyógyszerek

Adatok: név, napi adag, típus(TABLET, INJECTION, DROPS, CREAM)
Gyógyszert csak lakóhoz lehet rendelni és a napi adagot lehet változtatni.
Le lehet kérdezni gyógyszereket név szerinti szűréssel és statisztikát a gyógyszerek listáját a napi szükséges mennyiséggel.

## Szobák

Adatok: szoba száma, ágyak száma (SINGLE, DOUBLE, TRIPLE, FOUR_BED)
Létre lehet hozni szobát és üres szobát lehet törölni.
Lakókat lehet be- és átköltöztetni.
