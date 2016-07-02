#!/bin/sh

# controllo numero di parametri
if ! test $# -eq 3
then echo "Numero di parametri errato"
exit 1
fi

case $3 in
/*) ;;
*) echo "$3 non è un direttorio assoluto"
exit 2;;
esac

if ! test -d "$3"
then echo "$3 non è un direttorio valido"
exit 3
fi

case $1 in
/*) ;;
*) echo "$1 non è un direttorio assoluto"
exit 4;;
esac

if ! test -d "$1"
then echo "$1 non è un direttorio valido"
exit 5
fi

oldpath=$PATH
PATH=$PATH:`pwd`
copy_rec.sh "$1" "$2" "$3"
PATH=$oldpat

