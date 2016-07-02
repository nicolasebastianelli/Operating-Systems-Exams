#!/bin/sh

cd "$1"
cond=0
for f in *
do
    if test -d "$f"
    then
        $0 "$f" "$2"
    elif test -f "$f"
    then
        cond=`tail -n10 "$f" | grep -o "$2" | wc -l`
        if test $cond -ne 0
        then
            echo "`pwd`/"$f" con "$cond" occorrenze">> report.txt
        fi
    fi
done