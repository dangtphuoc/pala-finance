#!/bin/sh
declare -A map
while read line; do
    if grep "^[a-zA-Z]" <<< "$line" > /dev/null; then
        current="$line"
        if [ -z "${map[$current]}" ]; then 
            map[$current]=0
        fi
    elif grep "^[0-9]" <<<"$line" >/dev/null; then
        for i in $(cut -f 1,2 <<< "$line"); do
            map[$current]=$((map[$current] + $i))
        done
    fi
done <<< "$(git log --numstat --pretty="%aN")"

for i in "${!map[@]}"; do
    echo -e "$i:${map[$i]}"
done | sort -nr -t ":" -k 2 | column -t -s ":"