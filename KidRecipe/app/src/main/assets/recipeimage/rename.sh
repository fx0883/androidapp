for i in ./*.jpeg;
do mv "$i" "${i%.jpeg}.jpg";
done
