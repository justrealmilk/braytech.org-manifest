javac -d "." -cp "cleanFiles/lib/jackson/*" "cleanFiles/src/CleanFiles.java"
java -cp ".:cleanFiles/lib/jackson/*" -Dfile.encoding=UTF-8 CleanFiles $1