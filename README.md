# World-Of-Warcraf

IN CAZUL DE TERMINAL

    -Utilizatorului ii este cerut sa introduca credentialele sale: emailul si parola. Daca acestea sunt
    introduse incorect acestuia i se va comunica printr-un mesaj corespunzator, iar apoi va trebui sa le reintroduca

    -Daca acestea au fost corecte va urma sa se afiseze lista caracterelor din cont ca jucatorul sa isi aleaga unul

    -In urma acestor pasi jocul incepe, jucatorului cerandu-i-se sa introduca miscarile pe care doreste sa le efectueze.
    Dupa fiecare parcurgere de celula din mapa de joc, caracterul va primii banuti.

    -Daca acesta ajunge pe celule de tip Shop, i se va afisa o lista de potiuni pe care le poate cumpara daca are bani
    daca nu doreste sa cumpere am implementat sa poata iesi din magazin la dorinta sa.

    -Daca acesta ajunge pe o celula de tip enemy i se va fisa o lista cu 3 optiuni: Use ability, Attack enemy si Use potion

    -In cazul use ability, inamicul si caracterul vor primii un damage in functie de principiile stabilite in metoda useAbility

    -In cazul Attack enemy, atat caracterul cat si inamicul vor primii un damage de asemea stabilit in cadrul fiecarui caracter
    (in functie de caracteristicile sale principale si secundare)

    -Iar in cazul Use potion, se va afisa lista de potiuni a personajului din care acesta sa aleaga ulterior.
     Caracterul isi poate regenera viata sau mana in functia de potiunea aleasa

    -Dupa fiecare miscare realizata verific daca caracterul sau inamicul mai au viata deoarece daca caracterul nu mai are viata
    jocul s a terminat. Dar daca inamicul este omorat jocul continua, caracterul acumuland bani si experienta

IN CAZUL DE GRAFIC

    -Am aplicat aceleasi principii din cazul de terminal

    -Am implementat pagina de autentificare care in caz ca informatiile nu sunt corecte se va colora specific
    si va aparea si un mesaj pentru player

    -Bonus am implementat si butonul pentru show password astfel incat jucatorul sa o verifice daca a scris corect
    parola

    -Daca informatiile sunt corecte va aparea urmatoarea pagina in care jucatorul trebuie sa aleaga un caracter
    prin intermediul unui scroll pane iar la final trebuie sa apese butonul get character pentru a trece la pagina
    urmatoare

    -Pagina urmatoare este cea de joc.

    -In partea de sus a paginii am creat un panel special pentru detaliile personajului(experienta, viata, mana si asa mai departe)
    care se actualizeaza in urma fiecarei miscari realizate

    -In mijlocul paginii este tabla de joc pe care putem vedea cum se misca jucatorul

    -Iar in josul paginii am creat un panel unde avem butoanele pentru mutarile din joc

    -Tot in acest panel apar optiunile pentru fiecare celula(shop/enemy), dar si povesti pentru celulele nevizuite

    -Pentru fiecare celula am implementat butoane pentru optiuni astfel incat jucatorul sa le poata alege cu usurinta
