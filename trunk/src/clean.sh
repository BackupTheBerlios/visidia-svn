# nettoyage 
case $# in
0)
  CLNDIR=./ ;;
*)
  CLNDIR=$1
  shift ;;
esac
find $CLNDIR -name cvsroot -prune -o -type f \( -name core -o -name trace.txt -o -name "*.class" -o -name "*~" -o -name ".*~" -o -name "#*" -o -name ".#*" \) -print -exec rm -f {} \; 2>/dev/null 1>&2

while [ $# -ne 0 ]
do
  CLNDIR=$1
  shift
  find $CLNDIR -name cvsroot -o -type f \( -name core -o -name trace.txt -o -name "*.class" -o -name "*~" -o -name ".*~" -o -name "#*" -o -name ".#*"  \) -print -exec rm -f {} \; 2>/dev/null 1>&2
done

unset CLNDIR
