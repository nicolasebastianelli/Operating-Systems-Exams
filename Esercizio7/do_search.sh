#!/bin/bash

if ! [[ -e "$3" ]] ; then
    echo -e "Il file $3 non esiste\n" >&2
    exit 1
fi 
if ! test -d "$3" ; then
	size=`stat -c %s $3`
	size=`expr $size / 1024`
	echo "il file $3 ha dimensione $size"
	if [ $size -lt $2 -a $size -gt $1 ] ; then
    		echo `pwd`/"$3" >> $HOME/f_out.txt
	fi
else
    cd "$3"
    for file in * ; do
       "$0" "$1" "$2" "$file"
    done 
fi