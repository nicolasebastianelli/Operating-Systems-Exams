#!/bin/sh

cd "$1"

for f in *
do
    if test -d "$f"
    then
        $0 "$f" "$2" "$3"
    elif test -f "$f"
    then
        nric=`head -n10 "$f" | grep -o "$2" | wc -l`
        if [[ $nric -gt 0 ]]
        then
            if ! test -d "$3"/"$nric"
            then
                mkdir "$3"/"$nric"
            fi
            cp "$f" "$3"/"$nric"
        fi
    fi

done
