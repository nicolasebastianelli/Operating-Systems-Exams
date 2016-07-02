#!/bin/sh
if ! test $# -eq 2
then echo "Numero di parametri errato"
exit 1
fi

case $1 in
/*) ;;
*) echo "$1 non è un direttorio assoluto"
exit 2;;
esac

if ! test -d "$1"
then echo "$1 non è un direttorio valido"
exit 3
fi


oldpath=$PATH
PATH=$PATH:`pwd`
count_rec.sh "$1" "$2"
PATH=$oldpat

