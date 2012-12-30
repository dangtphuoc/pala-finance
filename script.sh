#!/bin/bash
cd E:/development/projects/git/pala-finance;
#git log --author="Yahoo Phuoc" --oneline --shortstat
HELLO=$(git log --author="Yahoo Phuoc" --oneline --shortstat)
HELLO1=$(git log --author="Phuoc Dang" --pretty=tformat: --numstat)
git log --author="Phuoc Dang" --numstat > report.txt
#echo $HELLO
#echo $HELLO1
for entry in $( git log --author="Phuoc Dang" --pretty=tformat: --numstat | cut -f 1- -d$'\n') ; do echo ${entry}; done
#while read line ; do
#    echo "$line"
#done < report.txt