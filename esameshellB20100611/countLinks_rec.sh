#!/bin/sh

cd "$1"
n=0
for f in *
do
    if test -d "$f"
    then
        $0 "$f" "$2" "$3"
    elif test -L "$f"
    then
        own=`ls -l "$f" | cut -d ' ' -f4`
        if test "$own" = "$2"
        then
            n=`expr $n + 1`
        fi
    fi
done
if test "$n" -gt "$3"
then
    echo "Trovati "$n" link di "$2" in `pwd`"
fi
